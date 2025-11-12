<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style>
    :root {
        --primary-dark-cb: #141a46; 
        --primary-accent-cb: #ec8b5a;
        --bot-bg-cb: #e5e7eb;
        --container-bg-cb: #ffffff;
        --text-light-cb: #f9fafb;
        --text-dark-cb: #1f2937;
    }

    .chat-popup {
        display: none;
        position: fixed;
        bottom: 100px;
        right: 30px;
        z-index: 9999 !important;
        opacity: 0;
        transform: translateY(20px);
        transition: opacity 0.3s ease, transform 0.3s ease;
    }

    .chat-popup.show {
        display: block;
        opacity: 1;
        transform: translateY(0);
    }

    .chat-container {
        width: 370px;
        height: 80vh;
        max-height: 600px;
        background: var(--container-bg-cb);
        border-radius: 16px;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
        display: flex;
        flex-direction: column;
        overflow: hidden;
        border: 1px solid #e5e7eb;
        font-family: 'Inter', sans-serif;
    }

    .header {
        background-color: var(--primary-dark-cb);
        color: var(--text-light-cb);
        padding: 16px 20px;
        font-size: 18px;
        font-weight: 600;
        display: flex;
        align-items: center;
        justify-content: space-between;
        border-bottom: 1px solid var(--primary-dark-cb);
    }

    .header .logo { font-size: 28px; margin-right: 12px; }
    .header .title-group { display: flex; align-items: center; }
    .header .title { display: flex; flex-direction: column; line-height: 1.2; }
    .header .status { font-size: 13px; font-weight: 400; color: #22c55e; display: flex; align-items: center; margin-top: 4px; }
    .header .status-dot { width: 8px; height: 8px; background-color: #22c55e; border-radius: 50%; margin-right: 6px; animation: pulse 1.5s infinite ease-in-out; }
    .header .close-btn { font-size: 28px; color: var(--text-light-cb); cursor: pointer; transition: transform 0.2s; }
    .header .close-btn:hover { transform: scale(1.2); }

    @keyframes pulse { 0%, 100% { opacity: 1; } 50% { opacity: 0.5; } }

    .chat-box {
        flex: 1;
        padding: 16px;
        background-color: #f9fafb;
        overflow-y: auto;
        scroll-behavior: smooth;
        display: flex;
        flex-direction: column;
        gap: 10px;
    }
    .chat-box::-webkit-scrollbar { width: 6px; }
    .chat-box::-webkit-scrollbar-thumb { background: #cbd5e1; border-radius: 3px; }

    .message {
        padding: 10px 16px;
        margin: 0;
        line-height: 1.5;
        max-width: 80%;
        word-wrap: break-word;
        font-size: 15px;
        box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
        animation: fadeIn 0.3s ease-out;
    }
    @keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }

    .user {
        background-color: var(--primary-accent-cb);
        color: var(--text-light-cb);
        align-self: flex-end;
        border-radius: 18px 18px 4px 18px;
    }
    .bot {
        background-color: var(--bot-bg-cb);
        color: var(--text-dark-cb);
        align-self: flex-start;
        border-radius: 18px 18px 18px 4px;
    }
    .error { background-color: #fee2e2; color: #b91c1c; align-self: flex-start; border-radius: 18px; padding: 10px 16px; }

    .input-area {
        display: flex;
        align-items: center;
        padding: 12px 16px;
        border-top: 1px solid #e5e7eb;
        background-color: var(--container-bg-cb);
        gap: 10px;
    }
    #userInput {
        flex: 1;
        padding: 12px 18px;
        border: 1px solid #cbd5e1;
        border-radius: 22px;
        outline: none;
        font-size: 15px;
        transition: all 0.2s ease;
    }
    #userInput:focus {
        border-color: var(--primary-dark-cb);
        box-shadow: 0 0 0 3px rgba(20, 26, 70, 0.1);
    }
    .send-button {
        background-color: var(--primary-accent-cb);
        color: white;
        border: none;
        border-radius: 50%;
        width: 44px;
        height: 44px;
        cursor: pointer;
        transition: background-color 0.2s ease;
        display: flex; justify-content: center; align-items: center; flex-shrink: 0;
    }
    .send-button:hover { filter: brightness(90%); }
    .send-button:disabled { 
        opacity: 0.5; 
        cursor: not-allowed; 
        filter: none;
    }
    .send-button svg { width: 22px; height: 22px; transform: translate(1px, -1px); }
    
    /* Typing indicator style */
    .typing-indicator { padding: 12px 18px; }
    .typing-indicator .dot {
        display: inline-block; width: 8px; height: 8px; border-radius: 50%;
        background-color: #6b7280; margin: 0 2px;
        animation: typing-bounce 1.2s infinite ease-in-out;
    }
    .typing-indicator .dot:nth-child(2) { animation-delay: -0.2s; }
    .typing-indicator .dot:nth-child(3) { animation-delay: -0.4s; }
    @keyframes typing-bounce { 0%, 60%, 100% { transform: translateY(0); } 30% { transform: translateY(-4px); } }

    .clear-chat-btn {
        background: none;
        border: none;
        color: var(--text-light-cb);
        font-size: 14px;
        cursor: pointer;
        opacity: 0.8;
        transition: opacity 0.2s;
    }
    .clear-chat-btn:hover { opacity: 1; }
</style>

<button class="floating-btn chat-bot" id="chatToggleButton" title="Mở Chat AI">
    <i class="fas fa-comments"></i> GYMFIT AI
</button>

<div class="chat-popup" id="chatPopup">
    <div class="chat-container">
        <div class="header">
            <div class="title-group">
                <div class="logo">🤖</div>
                <div class="title">
                    <div>GymFit AI</div>
                    <div class="status">
                        <span class="status-dot"></span> Đang hoạt động
                    </div>
                </div>
            </div>
            <div style="display: flex; align-items: center; gap: 10px;">
                <button class="clear-chat-btn" id="clearChatBtn" title="Xóa lịch sử chat">
                    🗑️
                </button>
                <span class="close-btn" id="closeChatBtn">×</span>
            </div>
        </div>

        <div id="chatBox" class="chat-box">
            <!-- Chat messages will be loaded here -->
        </div>

        <div class="input-area">
            <input id="userInput" type="text" placeholder="Nhập câu hỏi của bạn..." autocomplete="off" maxlength="500">
            <button class="send-button" id="sendBtn" title="Gửi tin nhắn">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                    <path d="M3.478 2.405a.75.75 0 00-.926.94l2.432 7.905H13.5a.75.75 0 010 1.5H4.984l-2.432 7.905a.75.75 0 00.926.94 60.519 60.519 0 0018.445-8.986.75.75 0 000-1.218A60.517 60.517 0 003.478 2.405z" />
                </svg>
            </button>
        </div>
    </div>
</div>

<script>
(function() {
    const chatPopup = document.getElementById("chatPopup");
    const chatToggleButton = document.getElementById("chatToggleButton");
    const closeChatBtn = document.getElementById("closeChatBtn");
    const clearChatBtn = document.getElementById("clearChatBtn");
    const userInput = document.getElementById("userInput");
    const chatBox = document.getElementById("chatBox");
    const sendBtn = document.getElementById("sendBtn");

    if (!chatPopup || !chatToggleButton || !closeChatBtn || !userInput || !chatBox || !sendBtn) {
        console.warn("[ChatBot] Some elements not found");
        return;
    }

    // Chat history persistence với sessionStorage - chỉ duy trì trong phiên hiện tại
    // Tự động xóa khi đóng tab hoặc logout
    const STORAGE_KEY = "gymfit_chat_session";
    let chatHistory = [];
    let isLoading = false;

    // Load history từ sessionStorage (chỉ tồn tại trong phiên)
    function loadChatHistory() {
        try {
            const stored = sessionStorage.getItem(STORAGE_KEY);
            chatHistory = stored ? JSON.parse(stored) : [];
            
            // Giới hạn số lượng tin nhắn để tránh quá tải (max 50 tin nhắn)
            if (chatHistory.length > 50) {
                chatHistory = chatHistory.slice(-50);
                saveChatHistory();
            }
            
            displayChatHistory();
        } catch (e) {
            console.error("[ChatBot] Error loading chat history:", e);
            chatHistory = [];
            addWelcomeMessage();
        }
    }

    // Save history to sessionStorage (tự động xóa khi đóng tab)
    function saveChatHistory() {
        try {
            sessionStorage.setItem(STORAGE_KEY, JSON.stringify(chatHistory));
        } catch (e) {
            console.error("[ChatBot] Error saving chat history:", e);
        }
    }

    // Display chat history
    function displayChatHistory() {
        chatBox.innerHTML = '';
        
        if (chatHistory.length === 0) {
            addWelcomeMessage();
        } else {
            chatHistory.forEach(msg => {
                appendMessage(msg.type, msg.text, false, false);
            });
        }
        
        scrollToBottom();
    }

    // Add welcome message
    function addWelcomeMessage() {
        const welcomeMsg = "Chào bạn! Tôi là GymFit AI 💪<br>Hãy hỏi tôi về tập luyện, dinh dưỡng, các gói tập hoặc thông tin phòng gym nhé!";
        chatHistory = [{ type: 'bot', text: welcomeMsg, timestamp: Date.now() }];
        saveChatHistory();
        displayChatHistory();
    }

    // Clear chat history
    function clearChatHistory() {
        if (confirm("Bạn có chắc muốn xóa toàn bộ lịch sử chat không?")) {
            chatHistory = [];
            sessionStorage.removeItem(STORAGE_KEY);
            addWelcomeMessage();
        }
    }

    // Event listeners
    chatToggleButton.addEventListener("click", () => {
        chatPopup.classList.toggle("show");
        if (chatPopup.classList.contains("show")) {
            setTimeout(() => userInput.focus(), 300);
        }
    });

    closeChatBtn.addEventListener("click", () => {
        chatPopup.classList.remove("show");
    });

    clearChatBtn.addEventListener("click", clearChatHistory);

    userInput.addEventListener("keydown", (e) => {
        if (e.key === "Enter" && !e.shiftKey) {
            e.preventDefault();
            sendMessage();
        }
    });

    sendBtn.addEventListener("click", sendMessage);

    // Send message function
    async function sendMessage() {
        const message = userInput.value.trim();
        if (!message || isLoading) return;

        // Disable input during processing
        setLoading(true);
        userInput.value = "";

        // Add user message
        appendMessage("user", message, false, true);

        // Add typing indicator
        const typingDiv = appendMessage("bot", "", true, false);

        try {
            const response = await fetch("${pageContext.request.contextPath}/ChatAIServlet", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ message: message })
            });

            if (!response.ok) {
                throw new Error("Kết nối bị lỗi");
            }

            const data = await response.json();
            
            // Remove typing indicator and add bot response
            if (typingDiv && chatBox.contains(typingDiv)) {
                chatBox.removeChild(typingDiv);
            }
            
            const botReply = data.reply || "Hệ thống đang bị gián đoạn, hãy thử lại sau nhé...";
            appendMessage("bot", botReply, false, true);

        } catch (error) {
            console.error("[ChatBot] API Error:", error);
            
            // Remove typing indicator and show error
            if (typingDiv && chatBox.contains(typingDiv)) {
                chatBox.removeChild(typingDiv);
            }
            
            appendMessage("bot", "Hệ thống đang bị gián đoạn, hãy thử lại sau nhé...", false, true);
        } finally {
            setLoading(false);
        }
    }

    // Append message to chat
    function appendMessage(type, text, isTyping = false, save = true) {
        const div = document.createElement("div");
        div.classList.add("message");
        
        if (type === "user") {
            div.classList.add("user");
        } else {
            div.classList.add("bot");
        }
        
        if (isTyping) {
            div.classList.add("typing-indicator");
            div.innerHTML = '<span class="dot"></span><span class="dot"></span><span class="dot"></span>';
        } else {
            div.innerHTML = text.replace(/\n/g, '<br>');
        }
        
        chatBox.appendChild(div);
        scrollToBottom();

        // Save to history (except typing indicators)
        if (save && !isTyping && text.trim()) {
            chatHistory.push({
                type: type,
                text: text,
                timestamp: Date.now()
            });
            saveChatHistory();
        }
        
        return div;
    }

    // Set loading state
    function setLoading(loading) {
        isLoading = loading;
        sendBtn.disabled = loading;
        userInput.disabled = loading;
        
        if (loading) {
            sendBtn.style.opacity = "0.5";
            userInput.placeholder = "Đang xử lý...";
        } else {
            sendBtn.style.opacity = "1";
            userInput.placeholder = "Nhập câu hỏi của bạn...";
            userInput.focus();
        }
    }

    // Scroll to bottom
    function scrollToBottom() {
        setTimeout(() => {
            chatBox.scrollTop = chatBox.scrollHeight;
        }, 100);
    }

    // Initialize chat
    loadChatHistory();

    // Auto-cleanup: Xóa lịch sử khi user logout hoặc đóng tab
    // SessionStorage tự động xóa khi đóng tab/browser
    console.log("[ChatBot] Initialized with session-based storage");

})();
</script>
