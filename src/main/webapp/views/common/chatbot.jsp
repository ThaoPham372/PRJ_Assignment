<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style>
    :root {
        --primary-dark-cb: #141a46; /* Đổi tên biến để tránh xung đột với trang chính */
        --primary-accent-cb: #ec8b5a;
        --bot-bg-cb: #e5e7eb;
        --container-bg-cb: #ffffff;
        --text-light-cb: #f9fafb;
        --text-dark-cb: #1f2937;
    }

    /* ======= Nút Chat Nổi ======= */
    /* Đảm bảo z-index đủ cao để đè lên các thành phần khác của about-us */
    #chatToggleButton {
        z-index: 9998 !important; 
    }

    .chat-popup {
        display: none;
        position: fixed;
        bottom: 100px;
        right: 30px;
        z-index: 9999 !important; /* Tăng z-index cao hơn header/footer */
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
        font-family: 'Inter', sans-serif; /* Đảm bảo font chữ đồng bộ */
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
    /* Custom Scrollbar cho chatbox */
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
            <span class="close-btn" id="closeChatBtn">×</span>
        </div>

        <div id="chatBox" class="chat-box">
            <div class="message bot">Chào bạn! Tôi là GymFit AI 💪<br>Hãy hỏi tôi về tập luyện, dinh dưỡng, hoặc kế hoạch fitness nhé!</div>
        </div>

        <div class="input-area">
            <input id="userInput" type="text" placeholder="Nhập câu hỏi..." autocomplete="off">
            <button class="send-button" id="sendBtn" title="Gửi">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                    <path d="M3.478 2.405a.75.75 0 00-.926.94l2.432 7.905H13.5a.75.75 0 010 1.5H4.984l-2.432 7.905a.75.75 0 00.926.94 60.519 60.519 0 0018.445-8.986.75.75 0 000-1.218A60.517 60.517 0 003.478 2.405z" />
                </svg>
            </button>
        </div>
    </div>
</div>

<!-- Add floating buttons CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/floating-buttons.css">

<script>
    (function() {
        const chatPopup = document.getElementById("chatPopup");
        const chatToggleButton = document.getElementById("chatToggleButton");
        const closeChatBtn = document.getElementById("closeChatBtn");
        const userInput = document.getElementById("userInput");
        const chatBox = document.getElementById("chatBox");
        const sendBtn = document.getElementById("sendBtn");

        if (!chatPopup || !chatToggleButton || !closeChatBtn || !userInput || !chatBox || !sendBtn) return;

        // =========================================
        // 1. QUẢN LÝ LỊCH SỬ CHAT (SESSION STORAGE)
        // =========================================
        let chatHistory = JSON.parse(sessionStorage.getItem("gymfit_chat_history")) || [];

        function saveHistory() {
            sessionStorage.setItem("gymfit_chat_history", JSON.stringify(chatHistory));
        }

        function loadHistory() {
            if (chatHistory.length > 0) {
                // Nếu đã có lịch sử, xóa nội dung mặc định và render lại từ đầu
                chatBox.innerHTML = '';
                chatHistory.forEach(msg => {
                    appendMessage(msg.type, msg.text, false, false); // false cuối cùng = không lưu trùng
                });
            } else {
                // Nếu chưa có lịch sử (lần đầu vào), lưu tin nhắn chào mừng mặc định vào storage
                const defaultWelcome = chatBox.querySelector('.message.bot');
                if (defaultWelcome) {
                    chatHistory.push({ type: 'bot', text: defaultWelcome.innerHTML });
                    saveHistory();
                }
            }
        }

        // Gọi hàm load ngay khi script chạy
        loadHistory();

        // =========================================
        // 2. CÁC SỰ KIỆN & LOGIC GỬI TIN
        // =========================================
        chatToggleButton.addEventListener("click", () => {
            chatPopup.classList.toggle("show");
            if (chatPopup.classList.contains("show")) {
                 // Tự động focus vào ô nhập khi mở chat
                setTimeout(() => userInput.focus(), 300);
            }
        });
        closeChatBtn.addEventListener("click", () => chatPopup.classList.remove("show"));

        async function sendMessage() {
            const message = userInput.value.trim();
            if (!message) return;

            appendMessage("user", message); // Tự động lưu vào lịch sử
            userInput.value = "";
            appendMessage("bot", "...", true); // true = isTyping (không lưu cái này)

            try {
                const res = await fetch("${pageContext.request.contextPath}/ChatAIServlet", {
                    method: "POST",
                    headers: {"Content-Type": "application/json"},
                    body: JSON.stringify({message})
                });

                if (!res.ok) throw new Error("Lỗi kết nối (" + res.status + ")");
                const data = await res.json();
                updateTypingMessage(data.reply || "Xin lỗi, tôi không hiểu câu hỏi.");

            } catch (err) {
                console.error("Chatbot Error:", err);
                updateTypingMessage("⚠️ " + err.message + ". Vui lòng thử lại.", true);
            }
        }

        userInput.addEventListener("keydown", (e) => {
            if (e.key === "Enter") sendMessage();
        });
        sendBtn.addEventListener("click", sendMessage);

        // =========================================
        // 3. HÀM RENDER TIN NHẮN (CÓ CẬP NHẬT)
        // =========================================
        // Thêm tham số `save = true` để kiểm soát việc lưu vào storage
        function appendMessage(type, text, isTyping = false, save = true) {
            const div = document.createElement("div");
            div.classList.add("message");
            if (type === "user") {
                div.classList.add("user");
            } else {
                div.classList.add("bot");
                if (isTyping) {
                    div.classList.add("typing-indicator");
                    div.innerHTML = '<span class="dot"></span><span class="dot"></span><span class="dot"></span>';
                }
            }
            if (!isTyping) div.innerHTML = text.replace(/\n/g, '<br>');
            
            chatBox.appendChild(div);
            chatBox.scrollTop = chatBox.scrollHeight;

            // Chỉ lưu vào lịch sử nếu không phải là typing indicator VÀ cờ save = true
            if (!isTyping && save) {
                chatHistory.push({ type, text });
                saveHistory();
            }
        }

        function updateTypingMessage(text, isError = false) {
            const typingIndicator = chatBox.querySelector(".typing-indicator");
            if (typingIndicator) {
                typingIndicator.className = "message bot";
                if (isError) typingIndicator.classList.add("error");
                typingIndicator.innerHTML = text.replace(/\n/g, '<br>');
                chatBox.scrollTop = chatBox.scrollHeight;

                // QUAN TRỌNG: Lưu tin nhắn phản hồi cuối cùng của bot vào lịch sử
                chatHistory.push({ type: 'bot', text: text });
                saveHistory();
            } else if (!isError) {
                appendMessage("bot", text);
            }
        }
    })();
</script>