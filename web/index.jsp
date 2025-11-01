<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GymFit AI Chat</title>
    <style>
        /* ======= CSS Reset & Font ======= */
        :root {
            --user-bg: #3b82f6;
            --bot-bg: #e5e7eb;
            --container-bg: #ffffff;
            --body-bg: #f0f2f5;
            --text-dark: #1f2937;
            --text-light: #f9fafb;
        }

        body {
            background-color: var(--body-bg);
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            padding: 16px;
            box-sizing: border-box;
        }

        /* ======= Khung chat ======= */
        .chat-container {
            width: 100%;
            max-width: 450px; /* Tăng chiều rộng một chút */
            height: 95vh;
            max-height: 700px; /* Thêm chiều cao tối đa */
            background: var(--container-bg);
            border-radius: 16px;
            box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.08), 0 4px 6px -2px rgba(0, 0, 0, 0.04);
            display: flex;
            flex-direction: column;
            overflow: hidden;
            border: 1px solid #e5e7eb;
        }

        /* ======= Header ======= */
        .header {
            background-color: var(--container-bg);
            color: var(--text-dark);
            padding: 16px 20px;
            font-size: 18px;
            font-weight: 600;
            display: flex;
            align-items: center;
            border-bottom: 1px solid #e5e7eb;
        }

        .header .logo {
            font-size: 28px;
            margin-right: 12px;
        }

        .header .title {
            display: flex;
            flex-direction: column;
        }

        .header .status {
            font-size: 13px;
            font-weight: 400;
            color: #16a34a; /* Màu xanh lá cây */
            display: flex;
            align-items: center;
        }

        .header .status-dot {
            width: 8px;
            height: 8px;
            background-color: #22c55e;
            border-radius: 50%;
            margin-right: 6px;
            /* Thêm hiệu ứng nhấp nháy */
            animation: pulse 1.5s infinite ease-in-out;
        }

        @keyframes pulse {
            0%, 100% { opacity: 1; }
            50% { opacity: 0.5; }
        }

        /* ======= Khu vực tin nhắn ======= */
        .chat-box {
            flex: 1;
            padding: 16px;
            background-color: #f9fafb; /* Màu nền hơi xám nhẹ */
            overflow-y: auto;
            scroll-behavior: smooth;
            display: flex;
            flex-direction: column;
            gap: 10px; /* Khoảng cách giữa các tin nhắn */
        }

        /* Tùy chỉnh thanh cuộn */
        .chat-box::-webkit-scrollbar {
            width: 6px;
        }
        .chat-box::-webkit-scrollbar-thumb {
            background: #cbd5e1;
            border-radius: 3px;
        }
        .chat-box::-webkit-scrollbar-thumb:hover {
            background: #94a3b8;
        }

        /* ======= Tin nhắn ======= */
        .message {
            padding: 10px 16px;
            margin: 0;
            line-height: 1.5;
            max-width: 80%; /* Giảm 1 chút cho đẹp hơn */
            word-wrap: break-word;
            font-size: 15px;
            box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
            /* Hiệu ứng tin nhắn mới */
            animation: fadeIn 0.3s ease-out;
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(10px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .user {
            background-color: var(--user-bg);
            color: var(--text-light);
            align-self: flex-end;
            /* Kiểu iMessage bo góc */
            border-radius: 18px 18px 4px 18px;
        }

        .bot, .error {
            background-color: var(--bot-bg);
            color: var(--text-dark);
            align-self: flex-start;
            /* Kiểu iMessage bo góc */
            border-radius: 18px 18px 18px 4px;
        }

        /* ======= Tin nhắn lỗi ======= */
        .error {
            background-color: #fee2e2;
            color: #b91c1c;
        }

        /* ======= Input & button ======= */
        .input-area {
            display: flex;
            align-items: center;
            padding: 12px 16px;
            border-top: 1px solid #e5e7eb;
            background-color: var(--container-bg);
            gap: 10px;
        }

        #userInput {
            flex: 1;
            padding: 12px 18px;
            border: 1px solid #cbd5e1;
            border-radius: 22px; /* Kiểu pill-shaped */
            outline: none;
            font-size: 15px;
            transition: all 0.2s ease;
        }

        #userInput:focus {
            border-color: #a5b4fc;
            /* Hiệu ứng Ring */
            box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.3);
        }

        .send-button {
            background-color: var(--user-bg);
            color: white;
            border: none;
            border-radius: 50%; /* Nút tròn */
            width: 44px;
            height: 44px;
            cursor: pointer;
            transition: background-color 0.2s ease;
            display: flex;
            justify-content: center;
            align-items: center;
            flex-shrink: 0; /* Không bị co lại */
        }

        .send-button:hover {
            background-color: #2563eb;
        }

        /* Icon SVG */
        .send-button svg {
            width: 22px;
            height: 22px;
            /* Dịch icon lên và sang phải 1 chút cho cân */
            transform: translate(1px, -1px); 
        }

    </style>
</head>
<body>

<div class="chat-container">
    
    <div class="header">
        <div class="logo">🤖</div>
        <div class="title">
            <div>GymFit AI</div>
            <div class="status">
                <span class="status-dot"></span> Đang hoạt động
            </div>
        </div>
    </div>

    <div id="chatBox" class="chat-box">
        <div class="message bot">Chào bạn! Tôi là GymFit AI 💪<br>Hãy hỏi tôi về tập luyện, dinh dưỡng, hoặc kế hoạch fitness nhé!</div>
    </div>

    <div class="input-area">
        <input id="userInput" type="text" placeholder="Nhập câu hỏi..." onkeydown="if(event.key==='Enter') sendMessage()">
        <button class="send-button" onclick="sendMessage()" title="Gửi">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                <path d="M3.478 2.405a.75.75 0 00-.926.94l2.432 7.905H13.5a.75.75 0 010 1.5H4.984l-2.432 7.905a.75.75 0 00.926.94 60.519 60.519 0 0018.445-8.986.75.75 0 000-1.218A60.517 60.517 0 003.478 2.405z" />
            </svg>
        </button>
    </div>
</div>

<script>
    const input = document.getElementById("userInput");
    const chatBox = document.getElementById("chatBox");

    async function sendMessage() {
        const message = input.value.trim();
        if (!message) return;

        // Hiển thị tin nhắn người dùng
        appendMessage("user", message);
        input.value = "";

        // Hiển thị trạng thái "đang gõ..." (Tùy chọn, thêm cho đẹp)
        appendMessage("bot", "...", true); // true = isTyping

        try {
            const res = await fetch("ChatAIServlet", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ message })
            });

            if (!res.ok) {
                throw new Error("Server trả lỗi " + res.status);
            }

            const data = await res.json();
            updateTypingMessage(data.reply || "Xin lỗi, tôi chưa thể phản hồi.");
        } catch (err) {
            updateTypingMessage("❌ " + err.message + ". Vui lòng thử lại sau.", true); // true = isError
        }

        chatBox.scrollTop = chatBox.scrollHeight;
    }
    
    // Bắt sự kiện nhấn phím Enter
    input.addEventListener("keydown", function(event) {
        if (event.key === "Enter") {
            sendMessage();
        }
    });

    function appendMessage(type, text, isTyping = false) {
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
        
        // Chỉ thêm text nếu không phải là "đang gõ"
        if (!isTyping) {
            // An toàn hơn khi dùng innerText, nhưng dùng innerHTML để hỗ trợ <br> từ AI
            div.innerHTML = text.replace(/\n/g, '<br>');
        }
        
        chatBox.appendChild(div);
        chatBox.scrollTop = chatBox.scrollHeight;
    }

    // Hàm cập nhật tin nhắn "đang gõ..."
    function updateTypingMessage(text, isError = false) {
        const typingMessage = chatBox.querySelector(".typing-indicator");
        if (typingMessage) {
            typingMessage.innerHTML = text.replace(/\n/g, '<br>');
            typingMessage.classList.remove("typing-indicator");
            if (isError) {
                typingMessage.classList.add("error");
            }
        }
    }

    // Thêm CSS cho hiệu ứng "đang gõ..."
    const styleSheet = document.createElement("style");
    styleSheet.type = "text/css";
    styleSheet.innerText = `
        .typing-indicator {
            padding: 12px 18px;
        }
        .typing-indicator .dot {
            display: inline-block;
            width: 8px;
            height: 8px;
            border-radius: 50%;
            background-color: #6b7280;
            margin: 0 2px;
            animation: typing-bounce 1.2s infinite ease-in-out;
        }
        .typing-indicator .dot:nth-child(2) {
            animation-delay: -0.2s;
        }
        .typing-indicator .dot:nth-child(3) {
            animation-delay: -0.4s;
        }
        @keyframes typing-bounce {
            0%, 60%, 100% { transform: translateY(0); }
            30% { transform: translateY(-4px); }
        }
    `;
    document.head.appendChild(styleSheet);

</script>

</body>
</html>