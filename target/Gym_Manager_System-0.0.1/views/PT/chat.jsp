<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Chat với học viên - PT</title>
    <link
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
      rel="stylesheet"
    />
    <link
      href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800;900&display=swap"
      rel="stylesheet"
    />

    <style>
      :root {
        --primary: #141a49;
        --accent: #ec8b5a;
        --text: #2c3e50;
        --text-light: #5a6c7d;
        --card: #ffffff;
        --shadow: rgba(0, 0, 0, 0.1);
        --gradient-primary: linear-gradient(135deg, #141a49 0%, #1e2a5c 100%);
        --gradient-accent: linear-gradient(135deg, #ec8b5a 0%, #d67a4f 100%);
      }

      * {
        box-sizing: border-box;
        margin: 0;
        padding: 0;
      }

      body {
        font-family: 'Inter', sans-serif;
        background: #f9f9f9;
        color: var(--text);
      }

      .container {
        max-width: 1400px;
        margin: 0 auto;
        padding: 40px 20px;
      }

      .page-header {
        background: var(--gradient-primary);
        color: #fff;
        padding: 30px 40px;
        border-radius: 15px;
        margin-bottom: 40px;
        box-shadow: 0 8px 30px var(--shadow);
      }

      .page-header h1 {
        font-size: 2.5rem;
        margin-bottom: 10px;
      }

      .breadcrumb {
        display: flex;
        gap: 10px;
        font-size: 0.9rem;
        opacity: 0.9;
      }

      .breadcrumb a {
        color: #fff;
        text-decoration: none;
      }

      .breadcrumb a:hover {
        color: var(--accent);
      }

      .btn {
        background: var(--gradient-accent);
        color: #fff;
        border: none;
        border-radius: 8px;
        padding: 12px 24px;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s;
        display: inline-flex;
        align-items: center;
        gap: 8px;
        text-decoration: none;
      }

      .btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(236, 139, 90, 0.4);
      }

      .btn-back {
        background: #6c757d;
        margin-bottom: 20px;
      }

      .chat-container {
        background: var(--card);
        border-radius: 20px;
        box-shadow: 0 8px 30px var(--shadow);
        overflow: hidden;
        display: grid;
        grid-template-columns: 350px 1fr;
        height: calc(100vh - 300px);
        min-height: 600px;
      }

      /* Sidebar */
      .chat-sidebar {
        background: #f8f9fa;
        border-right: 2px solid #e9ecef;
        display: flex;
        flex-direction: column;
      }

      .sidebar-header {
        padding: 20px;
        background: var(--primary);
        color: #fff;
      }

      .sidebar-header h3 {
        font-size: 1.3rem;
      }

      .search-box {
        padding: 15px;
        border-bottom: 2px solid #e9ecef;
      }

      .search-box input {
        width: 100%;
        padding: 10px 15px;
        border: 2px solid #e0e0e0;
        border-radius: 8px;
        font-size: 0.95rem;
      }

      .search-box input:focus {
        outline: none;
        border-color: var(--accent);
      }

      .contacts-list {
        flex: 1;
        overflow-y: auto;
      }

      .contact-item {
        padding: 15px 20px;
        border-bottom: 1px solid #e9ecef;
        cursor: pointer;
        transition: all 0.2s;
        display: flex;
        align-items: center;
        gap: 15px;
      }

      .contact-item:hover {
        background: #fff;
      }

      .contact-item.active {
        background: #fff;
        border-left: 4px solid var(--accent);
      }

      .contact-avatar {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        background: var(--gradient-accent);
        display: flex;
        align-items: center;
        justify-content: center;
        color: #fff;
        font-size: 1.3rem;
        flex-shrink: 0;
        position: relative;
      }

      .online-status {
        position: absolute;
        bottom: 2px;
        right: 2px;
        width: 12px;
        height: 12px;
        background: #28a745;
        border: 2px solid #fff;
        border-radius: 50%;
      }

      .contact-info {
        flex: 1;
        min-width: 0;
      }

      .contact-name {
        font-weight: 600;
        color: var(--primary);
        margin-bottom: 3px;
      }

      .contact-message {
        font-size: 0.85rem;
        color: var(--text-light);
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }

      .contact-time {
        font-size: 0.75rem;
        color: var(--text-light);
      }

      .unread-badge {
        background: var(--accent);
        color: #fff;
        border-radius: 50%;
        width: 24px;
        height: 24px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 0.75rem;
        font-weight: 700;
      }

      /* Chat Main */
      .chat-main {
        display: flex;
        flex-direction: column;
      }

      .chat-header {
        padding: 20px 30px;
        background: #fff;
        border-bottom: 2px solid #e9ecef;
        display: flex;
        align-items: center;
        justify-content: space-between;
      }

      .chat-user {
        display: flex;
        align-items: center;
        gap: 15px;
      }

      .chat-user-avatar {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        background: var(--gradient-accent);
        display: flex;
        align-items: center;
        justify-content: center;
        color: #fff;
        font-size: 1.3rem;
      }

      .chat-user-info h3 {
        color: var(--primary);
        font-size: 1.2rem;
        margin-bottom: 3px;
      }

      .chat-user-status {
        font-size: 0.85rem;
        color: #28a745;
      }

      .chat-actions {
        display: flex;
        gap: 10px;
      }

      .icon-btn {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        background: #f8f9fa;
        border: none;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: all 0.2s;
        color: var(--text-light);
      }

      .icon-btn:hover {
        background: var(--accent);
        color: #fff;
      }

      .chat-messages {
        flex: 1;
        padding: 30px;
        overflow-y: auto;
        background: #f8f9fa;
      }

      .message {
        display: flex;
        margin-bottom: 20px;
        gap: 12px;
      }

      .message.sent {
        flex-direction: row-reverse;
      }

      .message-avatar {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        background: var(--gradient-accent);
        display: flex;
        align-items: center;
        justify-content: center;
        color: #fff;
        flex-shrink: 0;
      }

      .message.sent .message-avatar {
        background: var(--gradient-primary);
      }

      .message-content {
        max-width: 60%;
      }

      .message-bubble {
        background: #fff;
        padding: 12px 18px;
        border-radius: 18px;
        box-shadow: 0 2px 8px var(--shadow);
        margin-bottom: 5px;
      }

      .message.sent .message-bubble {
        background: var(--accent);
        color: #fff;
      }

      .message-text {
        margin: 0;
        line-height: 1.5;
      }

      .message-time {
        font-size: 0.75rem;
        color: var(--text-light);
        padding: 0 10px;
      }

      .message.sent .message-time {
        text-align: right;
      }

      .chat-input-area {
        padding: 20px 30px;
        background: #fff;
        border-top: 2px solid #e9ecef;
      }

      .chat-input-container {
        display: flex;
        gap: 15px;
        align-items: center;
      }

      .chat-input {
        flex: 1;
        padding: 12px 20px;
        border: 2px solid #e0e0e0;
        border-radius: 25px;
        font-size: 1rem;
        resize: none;
        max-height: 100px;
      }

      .chat-input:focus {
        outline: none;
        border-color: var(--accent);
      }

      .send-btn {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        background: var(--gradient-accent);
        border: none;
        color: #fff;
        font-size: 1.3rem;
        cursor: pointer;
        transition: all 0.3s;
      }

      .send-btn:hover {
        transform: scale(1.1);
        box-shadow: 0 4px 15px rgba(236, 139, 90, 0.4);
      }

      @media (max-width: 968px) {
        .chat-container {
          grid-template-columns: 1fr;
        }

        .chat-sidebar {
          display: none;
        }

        .message-content {
          max-width: 80%;
        }
      }
    </style>
  </head>
  <body>
    <div class="container">
      <a
        href="${pageContext.request.contextPath}/views/PT/homePT.jsp"
        class="btn btn-back"
      >
        <i class="fas fa-arrow-left"></i> Quay lại
      </a>

      <div class="page-header">
        <h1><i class="fas fa-comments"></i> Chat với học viên</h1>
        <div class="breadcrumb">
          <a href="${pageContext.request.contextPath}/views/PT/homePT.jsp"
            >Home</a
          >
          <span>/</span>
          <span>Chat</span>
        </div>
      </div>

      <div class="chat-container">
        <!-- Sidebar -->
        <div class="chat-sidebar">
          <div class="sidebar-header">
            <h3><i class="fas fa-users"></i> Danh sách học viên</h3>
          </div>
          <div class="search-box">
            <input type="text" placeholder="Tìm kiếm học viên..." />
          </div>
          <div class="contacts-list">
            <div class="contact-item active">
              <div class="contact-avatar">
                <i class="fas fa-user"></i>
                <span class="online-status"></span>
              </div>
              <div class="contact-info">
                <div class="contact-name">Nguyễn Văn A</div>
                <div class="contact-message">
                  Em muốn hỏi về chế độ dinh dưỡng...
                </div>
              </div>
              <div
                style="
                  display: flex;
                  flex-direction: column;
                  align-items: flex-end;
                  gap: 5px;
                "
              >
                <div class="contact-time">10:30</div>
                <div class="unread-badge">3</div>
              </div>
            </div>

            <div class="contact-item">
              <div class="contact-avatar">
                <i class="fas fa-user"></i>
              </div>
              <div class="contact-info">
                <div class="contact-name">Trần Thị B</div>
                <div class="contact-message">Cảm ơn thầy nhiều ạ!</div>
              </div>
              <div class="contact-time">Hôm qua</div>
            </div>

            <div class="contact-item">
              <div class="contact-avatar">
                <i class="fas fa-user"></i>
                <span class="online-status"></span>
              </div>
              <div class="contact-info">
                <div class="contact-name">Lê Văn C</div>
                <div class="contact-message">
                  Ngày mai em có thể đổi lịch được không ạ?
                </div>
              </div>
              <div class="contact-time">2 giờ trước</div>
            </div>

            <div class="contact-item">
              <div class="contact-avatar">
                <i class="fas fa-user"></i>
              </div>
              <div class="contact-info">
                <div class="contact-name">Phạm Thị D</div>
                <div class="contact-message">
                  Em đã làm theo hướng dẫn của thầy rồi
                </div>
              </div>
              <div class="contact-time">3 giờ trước</div>
            </div>

            <div class="contact-item">
              <div class="contact-avatar">
                <i class="fas fa-user"></i>
              </div>
              <div class="contact-info">
                <div class="contact-name">Hoàng Văn E</div>
                <div class="contact-message">
                  Thầy cho em xin thực đơn được không ạ?
                </div>
              </div>
              <div class="contact-time">Hôm qua</div>
            </div>
          </div>
        </div>

        <!-- Chat Main -->
        <div class="chat-main">
          <div class="chat-header">
            <div class="chat-user">
              <div class="chat-user-avatar">
                <i class="fas fa-user"></i>
              </div>
              <div class="chat-user-info">
                <h3>Nguyễn Văn A</h3>
                <div class="chat-user-status">
                  <i class="fas fa-circle" style="font-size: 0.6rem"></i> Đang
                  hoạt động
                </div>
              </div>
            </div>
            <div class="chat-actions">
              <button class="icon-btn" title="Gọi điện">
                <i class="fas fa-phone"></i>
              </button>
              <button class="icon-btn" title="Video call">
                <i class="fas fa-video"></i>
              </button>
              <button class="icon-btn" title="Thông tin">
                <i class="fas fa-info-circle"></i>
              </button>
            </div>
          </div>

          <div class="chat-messages" id="chatMessages">
            <!-- Received messages -->
            <div class="message">
              <div class="message-avatar">
                <i class="fas fa-user"></i>
              </div>
              <div class="message-content">
                <div class="message-bubble">
                  <p class="message-text">
                    Chào thầy ạ! Em muốn hỏi về chế độ dinh dưỡng phù hợp với
                    em.
                  </p>
                </div>
                <div class="message-time">10:25</div>
              </div>
            </div>

            <!-- Sent messages -->
            <div class="message sent">
              <div class="message-avatar">
                <i class="fas fa-dumbbell"></i>
              </div>
              <div class="message-content">
                <div class="message-bubble">
                  <p class="message-text">
                    Chào em! Thầy sẽ gửi cho em một thực đơn phù hợp với mục
                    tiêu giảm cân của em nhé.
                  </p>
                </div>
                <div class="message-time">10:26</div>
              </div>
            </div>

            <div class="message">
              <div class="message-avatar">
                <i class="fas fa-user"></i>
              </div>
              <div class="message-content">
                <div class="message-bubble">
                  <p class="message-text">Dạ vâng, em cảm ơn thầy ạ!</p>
                </div>
                <div class="message-time">10:27</div>
              </div>
            </div>

            <div class="message sent">
              <div class="message-avatar">
                <i class="fas fa-dumbbell"></i>
              </div>
              <div class="message-content">
                <div class="message-bubble">
                  <p class="message-text">
                    Em nên ăn nhiều rau xanh, protein từ thịt gà, cá. Tránh tinh
                    bột và đồ ngọt nhé.
                  </p>
                </div>
                <div class="message-time">10:28</div>
              </div>
            </div>

            <div class="message">
              <div class="message-avatar">
                <i class="fas fa-user"></i>
              </div>
              <div class="message-content">
                <div class="message-bubble">
                  <p class="message-text">
                    Em hiểu rồi ạ. Em sẽ cố gắng tuân thủ theo hướng dẫn của
                    thầy.
                  </p>
                </div>
                <div class="message-time">10:30</div>
              </div>
            </div>
          </div>

          <div class="chat-input-area">
            <div class="chat-input-container">
              <button class="icon-btn">
                <i class="fas fa-paperclip"></i>
              </button>
              <button class="icon-btn">
                <i class="fas fa-image"></i>
              </button>
              <textarea
                class="chat-input"
                id="messageInput"
                placeholder="Nhập tin nhắn..."
                rows="1"
              ></textarea>
              <button class="send-btn" onclick="sendMessage()">
                <i class="fas fa-paper-plane"></i>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <script>
      // Auto-resize textarea
      const messageInput = document.getElementById('messageInput');
      messageInput.addEventListener('input', function () {
        this.style.height = 'auto';
        this.style.height = this.scrollHeight + 'px';
      });

      // Send message on Enter (Shift+Enter for new line)
      messageInput.addEventListener('keydown', function (e) {
        if (e.key === 'Enter' && !e.shiftKey) {
          e.preventDefault();
          sendMessage();
        }
      });

      function sendMessage() {
        const input = document.getElementById('messageInput');
        const message = input.value.trim();

        if (message === '') return;

        const chatMessages = document.getElementById('chatMessages');
        const now = new Date();
        const time =
          now.getHours() + ':' + String(now.getMinutes()).padStart(2, '0');

        const messageHTML = `
                <div class="message sent">
                    <div class="message-avatar">
                        <i class="fas fa-dumbbell"></i>
                    </div>
                    <div class="message-content">
                        <div class="message-bubble">
                            <p class="message-text">${message}</p>
                        </div>
                        <div class="message-time">${time}</div>
                    </div>
                </div>
            `;

        chatMessages.insertAdjacentHTML('beforeend', messageHTML);
        input.value = '';
        input.style.height = 'auto';

        // Scroll to bottom
        chatMessages.scrollTop = chatMessages.scrollHeight;
      }

      // Auto-scroll to bottom on load
      window.addEventListener('load', function () {
        const chatMessages = document.getElementById('chatMessages');
        chatMessages.scrollTop = chatMessages.scrollHeight;
      });
    </script>
  </body>
</html>

