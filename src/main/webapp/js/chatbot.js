/**
 * GYMFIT AI Chatbot JavaScript
 */
class GymfitChatbot {
    constructor() {
        this.isOpen = false;
        this.isTyping = false;
        this.init();
    }

    init() {
        this.createChatbotHTML();
        this.bindEvents();
        this.showWelcomeMessage();
    }

    createChatbotHTML() {
        // Tạo HTML cho chatbot
        const chatbotHTML = `
            <div id="gymfit-chatbot" class="chatbot-container">
                <div class="chatbot-header">
                    <h3>
                        <div class="ai-avatar">🤖</div>
                        GYMFIT AI
                    </h3>
                    <button class="chatbot-close" onclick="gymfitChatbot.closeChatbot()">×</button>
                </div>
                <div class="chatbot-messages" id="chatbot-messages">
                    <!-- Messages sẽ được thêm vào đây -->
                </div>
                <div class="chatbot-input">
                    <div class="input-group">
                        <textarea 
                            id="message-input" 
                            class="message-input" 
                            placeholder="Hỏi tôi về GYMFIT hoặc tập luyện..." 
                            rows="1"
                        ></textarea>
                        <button id="send-btn" class="send-btn" onclick="gymfitChatbot.sendMessage()">
                            <i class="fas fa-paper-plane"></i>
                        </button>
                    </div>
                </div>
            </div>
        `;

        // Thêm vào body
        document.body.insertAdjacentHTML('beforeend', chatbotHTML);
    }

    bindEvents() {
        const messageInput = document.getElementById('message-input');
        const sendBtn = document.getElementById('send-btn');

        // Auto resize textarea
        messageInput.addEventListener('input', function() {
            this.style.height = 'auto';
            this.style.height = Math.min(this.scrollHeight, 80) + 'px';
        });

        // Send message on Enter (but not Shift+Enter)
        messageInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                this.sendMessage();
            }
        });

        // Disable send button khi đang typing
        messageInput.addEventListener('input', () => {
            const hasText = messageInput.value.trim().length > 0;
            sendBtn.disabled = !hasText || this.isTyping;
        });
    }

    showWelcomeMessage() {
        const messagesContainer = document.getElementById('chatbot-messages');
        const welcomeHTML = `
            <div class="welcome-message">
                <strong>🏋️‍♂️ Chào mừng đến với GYMFIT AI!</strong><br>
                Tôi có thể giúp bạn:
                <ul style="text-align: left; margin-top: 10px; padding-left: 20px;">
                    <li>Thông tin về phòng gym</li>
                    <li>Gói tập và ưu đãi</li>
                    <li>Tư vấn tập luyện</li>
                    <li>Dinh dưỡng thể thao</li>
                </ul>
                Hãy đặt câu hỏi cho tôi! 💪
            </div>
        `;
        messagesContainer.innerHTML = welcomeHTML;
    }

    openChatbot() {
        if (this.isOpen) return;
        
        const chatbot = document.getElementById('gymfit-chatbot');
        chatbot.classList.add('show');
        this.isOpen = true;
        
        // Focus vào input
        setTimeout(() => {
            document.getElementById('message-input').focus();
        }, 300);
    }

    closeChatbot() {
        if (!this.isOpen) return;
        
        const chatbot = document.getElementById('gymfit-chatbot');
        chatbot.classList.remove('show');
        chatbot.classList.add('hide');
        
        setTimeout(() => {
            chatbot.classList.remove('hide');
            this.isOpen = false;
        }, 300);
    }

    toggleChatbot() {
        if (this.isOpen) {
            this.closeChatbot();
        } else {
            this.openChatbot();
        }
    }

    async sendMessage() {
        const messageInput = document.getElementById('message-input');
        const sendBtn = document.getElementById('send-btn');
        const message = messageInput.value.trim();

        if (!message || this.isTyping) return;

        // Disable input khi đang gửi
        this.isTyping = true;
        sendBtn.disabled = true;
        messageInput.disabled = true;

        // Thêm tin nhắn của user
        this.addMessage(message, 'user');
        
        // Clear input
        messageInput.value = '';
        messageInput.style.height = 'auto';

        // Thêm typing indicator
        this.showTypingIndicator();

        try {
            // Gửi request đến server
            const response = await fetch('/api/ai/chat', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8',
                },
                body: JSON.stringify({
                    message: message
                })
            });

            const data = await response.json();

            // Remove typing indicator
            this.hideTypingIndicator();

            if (data.success) {
                this.addMessage(data.reply, 'ai');
            } else {
                this.addMessage(data.error || 'Có lỗi xảy ra, vui lòng thử lại.', 'ai');
            }

        } catch (error) {
            console.error('Error sending message:', error);
            this.hideTypingIndicator();
            this.addMessage('Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng và thử lại.', 'ai');
        }

        // Enable lại input
        this.isTyping = false;
        messageInput.disabled = false;
        messageInput.focus();
        sendBtn.disabled = false;
    }

    addMessage(text, sender) {
        const messagesContainer = document.getElementById('chatbot-messages');
        const messageDiv = document.createElement('div');
        messageDiv.classList.add('message', sender);
        
        // Format text với line breaks
        const formattedText = text.replace(/\n/g, '<br>');
        messageDiv.innerHTML = formattedText;
        
        messagesContainer.appendChild(messageDiv);
        
        // Scroll xuống cuối
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }

    showTypingIndicator() {
        const messagesContainer = document.getElementById('chatbot-messages');
        const typingDiv = document.createElement('div');
        typingDiv.classList.add('message', 'typing');
        typingDiv.id = 'typing-indicator';
        typingDiv.innerHTML = `
            🤖 Đang trả lời...
            <div class="typing-indicator">
                <div class="typing-dot"></div>
                <div class="typing-dot"></div>
                <div class="typing-dot"></div>
            </div>
        `;
        
        messagesContainer.appendChild(typingDiv);
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }

    hideTypingIndicator() {
        const typingIndicator = document.getElementById('typing-indicator');
        if (typingIndicator) {
            typingIndicator.remove();
        }
    }
}

// Initialize chatbot khi DOM loaded
document.addEventListener('DOMContentLoaded', function() {
    // Đảm bảo CSS được load trước
    if (!document.querySelector('link[href*="chatbot.css"]')) {
        const link = document.createElement('link');
        link.rel = 'stylesheet';
        link.href = '/css/chatbot.css';
        document.head.appendChild(link);
    }
    
    // Initialize chatbot
    window.gymfitChatbot = new GymfitChatbot();
});

// Function để gọi từ button
function openGymfitChatbot() {
    if (window.gymfitChatbot) {
        window.gymfitChatbot.openChatbot();
    }
}
