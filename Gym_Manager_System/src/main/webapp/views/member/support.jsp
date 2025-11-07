<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/views/common/header.jsp" %>

    <style>
        :root {
        --primary: #141a46;
        --primary-light: #1e2a5c;
        --accent: #ec8b5e;
        --accent-hover: #d67a4f;
        --text: #2c3e50;
        --text-light: #5a6c7d;
        --muted: #f8f9fa;
        --card: #ffffff;
        --shadow: rgba(0, 0, 0, 0.1);
        --shadow-hover: rgba(0, 0, 0, 0.15);
        --gradient-primary: linear-gradient(135deg, #141a46 0%, #1e2a5c 100%);
        --gradient-accent: linear-gradient(135deg, #ec8b5e 0%, #d67a4f 100%);
        }

        .support-card {
        background: var(--card);
            border-radius: 15px;
        box-shadow: 0 8px 25px var(--shadow);
        padding: 30px;
        margin-bottom: 30px;
        transition: all 0.3s ease;
        }

        .support-card:hover {
            transform: translateY(-5px);
        box-shadow: 0 12px 35px var(--shadow-hover);
    }

    .member-nav {
        background: var(--gradient-primary);
        padding: 0;
        margin-bottom: 30px;
        border-radius: 20px;
        box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
        overflow: hidden;
        position: relative;
    }

    .member-nav::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 4px;
        background: var(--gradient-accent);
    }

    .member-nav .nav-container {
        padding: 20px 30px;
    }

    .member-nav .nav-link {
        color: rgba(255, 255, 255, 0.9);
        font-weight: 600;
        padding: 12px 20px;
        border-radius: 12px;
        transition: background-color 0.2s ease, color 0.2s ease;
        margin: 0 6px;
        position: relative;
        border: 1px solid rgba(255, 255, 255, 0.1);
        text-decoration: none;
        min-width: 140px;
        text-align: center;
    }

    .member-nav .nav-link:hover {
        background: rgba(255, 255, 255, 0.1);
        color: white;
    }

    .member-nav .nav-link.active {
        background: var(--accent);
        color: white;
    }

    .member-nav .nav-link i {
        margin-right: 8px;
        font-size: 1em;
        opacity: 0.9;
    }

    .member-nav .nav-link.active i {
        opacity: 1;
    }

    .member-nav .nav-container {
        display: flex;
        justify-content: center;
        align-items: center;
        flex-wrap: wrap;
        gap: 10px;
    }

    /* Responsive Navigation */
    @media (max-width: 768px) {
        .member-nav .nav-container {
            padding: 15px 20px;
        }
        
        .member-nav .nav-link {
            padding: 12px 20px;
            margin: 0 4px;
            font-size: 0.9rem;
        }
        
        .member-nav .nav-link i {
            margin-right: 8px;
            font-size: 1em;
        }
    }

    @media (max-width: 576px) {
        .member-nav .nav-container {
            padding: 10px 15px;
        }
        
        .member-nav .nav-link {
            padding: 10px 15px;
            margin: 0 2px;
            font-size: 0.85rem;
        }
        
        .member-nav .nav-link i {
            margin-right: 6px;
            font-size: 0.9em;
        }
    }

    .support-title {
        color: var(--text);
        font-weight: 800;
            margin-bottom: 30px;
        text-transform: uppercase;
        letter-spacing: 1px;
    }

    .support-item {
        background: var(--muted);
            border-radius: 10px;
        padding: 20px;
        margin-bottom: 15px;
        border-left: 4px solid var(--accent);
            transition: all 0.3s ease;
        }

    .support-item:hover {
        background: var(--card);
        box-shadow: 0 4px 15px var(--shadow);
    }

    .btn-support {
        background: var(--gradient-accent);
        color: white;
            border: none;
            border-radius: 25px;
        padding: 12px 25px;
        font-weight: 600;
            transition: all 0.3s ease;
        text-decoration: none;
        display: inline-block;
        }

    .btn-support:hover {
            transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(236, 139, 94, 0.4);
        color: white;
    }

    .chat-bubble {
        background: var(--muted);
            border-radius: 15px;
        padding: 15px;
        margin-bottom: 10px;
        max-width: 80%;
    }

    .chat-bubble.user {
        background: var(--accent);
            color: white;
        margin-left: auto;
    }

    .chat-bubble.support {
        background: var(--muted);
        margin-right: auto;
        }
    </style>

<div class="container mt-5">
    <!-- Back Button -->
    <div class="mb-4">
        <a href="${pageContext.request.contextPath}/member/dashboard" class="btn-back">
            <i class="fas fa-arrow-left"></i>
            <span>Quay lại Dashboard</span>
        </a>
    </div>

    <!-- Support Header -->
    <div class="support-card text-center">
        <h2 class="support-title">Hỗ Trợ Khách Hàng</h2>
        <p class="text-muted mb-4">Chúng tôi luôn sẵn sàng hỗ trợ bạn 24/7</p>
    </div>

    <!-- Contact Methods -->
    <div class="row mb-4">
        <div class="col-md-4 mb-3">
            <div class="support-card text-center">
                <div class="mb-3">
                    <i class="fas fa-phone fa-3x" style="color: var(--accent);"></i>
                </div>
                <h5 class="mb-2">Điện Thoại</h5>
                <p class="text-muted mb-3">0123-456-789</p>
                <button class="btn-support">
                    <i class="fas fa-phone me-2"></i>Gọi Ngay
                    </button>
            </div>
        </div>
        <div class="col-md-4 mb-3">
            <div class="support-card text-center">
                <div class="mb-3">
                    <i class="fas fa-envelope fa-3x" style="color: var(--accent);"></i>
                </div>
                <h5 class="mb-2">Email</h5>
                <p class="text-muted mb-3">support@gymfit.vn</p>
                <button class="btn-support">
                    <i class="fas fa-envelope me-2"></i>Gửi Email
                </button>
            </div>
                </div>
        <div class="col-md-4 mb-3">
            <div class="support-card text-center">
                <div class="mb-3">
                    <i class="fas fa-comments fa-3x" style="color: var(--accent);"></i>
                </div>
                <h5 class="mb-2">Chat Trực Tiếp</h5>
                <p class="text-muted mb-3">Trò chuyện với nhân viên</p>
                <button class="btn-support" id="startChat">
                    <i class="fas fa-comments me-2"></i>Bắt Đầu Chat
                </button>
            </div>
            </div>
        </div>

    <!-- Live Chat -->
            <div class="row">
        <div class="col-md-8">
            <div class="support-card" id="chatContainer" style="display: none;">
                <h4 class="support-title">Chat Trực Tiếp</h4>
                <div id="chatMessages" style="height: 400px; overflow-y: auto; border: 1px solid #e9ecef; border-radius: 10px; padding: 15px; margin-bottom: 15px;">
                    <div class="chat-bubble support">
                        <strong>Hỗ trợ:</strong> Xin chào! Tôi có thể giúp gì cho bạn?
                    </div>
                </div>
                <div class="input-group">
                    <input type="text" class="form-control" id="messageInput" placeholder="Nhập tin nhắn...">
                    <button class="btn-support" id="sendMessage">
                        <i class="fas fa-paper-plane"></i>
                    </button>
            </div>
        </div>

            <!-- FAQ -->
            <div class="support-card">
                <h4 class="support-title">Câu Hỏi Thường Gặp</h4>
                
                <div class="support-item">
                    <h6 class="mb-2">Làm thế nào để đặt lịch tập?</h6>
                    <p class="text-muted mb-0">Bạn có thể đặt lịch tập thông qua ứng dụng hoặc gọi điện trực tiếp đến phòng gym.</p>
                    </div>
                    
                <div class="support-item">
                    <h6 class="mb-2">Tôi có thể hủy buổi tập không?</h6>
                    <p class="text-muted mb-0">Có, bạn có thể hủy buổi tập trước 2 giờ mà không bị tính phí.</p>
                    </div>
                    
                <div class="support-item">
                    <h6 class="mb-2">Phòng gym mở cửa từ mấy giờ?</h6>
                    <p class="text-muted mb-0">Phòng gym mở cửa từ 6:00 sáng đến 22:00 tối hàng ngày.</p>
                            </div>
                            
                <div class="support-item">
                    <h6 class="mb-2">Tôi có thể đổi gói thành viên không?</h6>
                    <p class="text-muted mb-0">Có, bạn có thể đổi gói thành viên bất kỳ lúc nào. Liên hệ với chúng tôi để được hỗ trợ.</p>
                            </div>
                        </div>
                    </div>
                    
        <div class="col-md-4">
            <!-- Support Hours -->
            <div class="support-card">
                <h5 class="support-title">Giờ Hỗ Trợ</h5>
                <div class="mb-3">
                    <strong>Thứ 2 - Thứ 6:</strong><br>
                    <span class="text-muted">8:00 - 20:00</span>
                                </div>
                <div class="mb-3">
                    <strong>Thứ 7:</strong><br>
                    <span class="text-muted">8:00 - 18:00</span>
                            </div>
                <div class="mb-3">
                    <strong>Chủ Nhật:</strong><br>
                    <span class="text-muted">9:00 - 17:00</span>
                </div>
            </div>

            
            <!-- Emergency Contact -->
                        <div class="support-card">
                <h5 class="support-title">Liên Hệ Khẩn Cấp</h5>
                <p class="text-muted mb-3">Trong trường hợp khẩn cấp</p>
                <button class="btn-support w-100">
                    <i class="fas fa-exclamation-triangle me-2"></i>Hotline: 1900-xxxx
                                    </button>
            </div>
        </div>
    </div>
                        </div>
                        
<script>
    // Chat functionality
    const startChatBtn = document.getElementById('startChat');
    const chatContainer = document.getElementById('chatContainer');
    const chatMessages = document.getElementById('chatMessages');
    const messageInput = document.getElementById('messageInput');
    const sendMessageBtn = document.getElementById('sendMessage');

    startChatBtn.addEventListener('click', function() {
        chatContainer.style.display = 'block';
        startChatBtn.style.display = 'none';
    });

    function addMessage(message, isUser = false) {
        const messageDiv = document.createElement('div');
        messageDiv.className = `chat-bubble ${isUser ? 'user' : 'support'}`;
        messageDiv.innerHTML = `<strong>${isUser ? 'Bạn' : 'Hỗ trợ'}:</strong> ${message}`;
        chatMessages.appendChild(messageDiv);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }

    sendMessageBtn.addEventListener('click', function() {
        const message = messageInput.value.trim();
        if (message) {
            addMessage(message, true);
            messageInput.value = '';
            
            // Simulate support response
            setTimeout(() => {
                const responses = [
                    'Cảm ơn bạn đã liên hệ. Chúng tôi sẽ hỗ trợ bạn ngay.',
                    'Tôi hiểu vấn đề của bạn. Để tôi kiểm tra thông tin.',
                    'Bạn có thể cho tôi biết thêm chi tiết không?',
                    'Tôi sẽ chuyển yêu cầu của bạn đến bộ phận liên quan.'
                ];
                const randomResponse = responses[Math.floor(Math.random() * responses.length)];
                addMessage(randomResponse);
            }, 1000);
        }
    });

    messageInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            sendMessageBtn.click();
        }
        });
    </script>

<%@ include file="/views/common/footer.jsp" %>