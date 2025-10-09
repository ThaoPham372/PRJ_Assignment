<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hỗ Trợ - Stamina Gym</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
    <style>
        :root {
            --primary-color: #3B1E78;
            --secondary-color: #FFD700;
            --accent-color: #EC8B5E;
            --text-dark: #2C3E50;
            --bg-light: #F8F9FA;
        }

        body {
            background: var(--bg-light);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .navbar {
            background: var(--primary-color) !important;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .navbar-brand {
            color: var(--secondary-color) !important;
            font-weight: bold;
        }

        .nav-link {
            color: white !important;
            transition: color 0.3s ease;
        }

        .nav-link:hover {
            color: var(--secondary-color) !important;
        }

        .main-content {
            margin-top: 80px;
            padding: 20px 0;
        }

        .page-header {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }

        .support-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            margin-bottom: 20px;
            transition: transform 0.3s ease;
        }

        .support-card:hover {
            transform: translateY(-5px);
        }

        .ticket-card {
            background: white;
            border-radius: 15px;
            padding: 20px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            margin-bottom: 15px;
            border-left: 5px solid transparent;
            transition: all 0.3s ease;
        }

        .ticket-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
        }

        .ticket-open {
            border-left-color: #28a745;
        }

        .ticket-pending {
            border-left-color: #ffc107;
        }

        .ticket-closed {
            border-left-color: #6c757d;
        }

        .ticket-priority {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 15px;
            font-size: 0.8rem;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .priority-high {
            background: #f8d7da;
            color: #721c24;
        }

        .priority-medium {
            background: #fff3cd;
            color: #856404;
        }

        .priority-low {
            background: #d1ecf1;
            color: #0c5460;
        }

        .ticket-status {
            display: inline-block;
            padding: 6px 15px;
            border-radius: 20px;
            font-size: 0.9rem;
            font-weight: bold;
        }

        .status-open {
            background: #d4edda;
            color: #155724;
        }

        .status-pending {
            background: #fff3cd;
            color: #856404;
        }

        .status-closed {
            background: #f8d7da;
            color: #721c24;
        }

        .faq-item {
            background: white;
            border-radius: 15px;
            padding: 20px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            margin-bottom: 15px;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .faq-item:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
        }

        .faq-question {
            font-weight: bold;
            color: var(--primary-color);
            margin-bottom: 10px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .faq-answer {
            color: #666;
            line-height: 1.6;
            display: none;
            margin-top: 15px;
            padding-top: 15px;
            border-top: 1px solid #e9ecef;
        }

        .faq-answer.show {
            display: block;
        }

        .contact-info {
            background: linear-gradient(135deg, var(--primary-color), var(--accent-color));
            color: white;
            border-radius: 15px;
            padding: 30px;
            text-align: center;
            margin-bottom: 30px;
        }

        .contact-item {
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 20px;
        }

        .contact-item i {
            width: 50px;
            height: 50px;
            background: rgba(255,255,255,0.2);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 15px;
            font-size: 1.2rem;
        }

        .form-control {
            border: 2px solid #e9ecef;
            border-radius: 10px;
            padding: 12px 20px;
            font-size: 1rem;
            transition: border-color 0.3s ease;
        }

        .form-control:focus {
            border-color: var(--primary-color);
            box-shadow: 0 0 0 0.2rem rgba(59, 30, 120, 0.25);
        }

        .btn-primary {
            background: var(--primary-color);
            border: none;
            padding: 12px 30px;
            border-radius: 25px;
            font-weight: bold;
            transition: all 0.3s ease;
        }

        .btn-primary:hover {
            background: #2a1656;
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(59, 30, 120, 0.3);
        }

        .btn-success {
            background: var(--accent-color);
            border: none;
            padding: 12px 30px;
            border-radius: 25px;
            font-weight: bold;
            transition: all 0.3s ease;
        }

        .btn-success:hover {
            background: #d6734a;
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(236, 139, 94, 0.3);
        }

        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }

        .stat-item {
            background: white;
            padding: 20px;
            border-radius: 15px;
            text-align: center;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
        }

        .stat-icon {
            width: 60px;
            height: 60px;
            background: linear-gradient(135deg, var(--primary-color), var(--accent-color));
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 15px;
            color: white;
            font-size: 1.5rem;
        }

        .stat-value {
            font-size: 2rem;
            font-weight: bold;
            color: var(--primary-color);
            margin-bottom: 5px;
        }

        .stat-label {
            color: #666;
            font-size: 0.9rem;
        }
    </style>
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark fixed-top">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/member/dashboard">
                <i class="fas fa-dumbbell me-2"></i>STAMINA GYM
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/member/dashboard">
                            <i class="fas fa-home me-1"></i>Trang Chủ
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/member/schedule">
                            <i class="fas fa-calendar me-1"></i>Lịch Tập
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/member/workout">
                            <i class="fas fa-dumbbell me-1"></i>Buổi Tập
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/member/nutrition">
                            <i class="fas fa-apple-alt me-1"></i>Dinh Dưỡng
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/member/membership">
                            <i class="fas fa-id-card me-1"></i>Gói Tập
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/member/profile">
                            <i class="fas fa-user me-1"></i>Hồ Sơ
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/member/support">
                            <i class="fas fa-headset me-1"></i>Hỗ Trợ
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/logout">
                            <i class="fas fa-sign-out-alt me-1"></i>Đăng Xuất
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Main Content -->
    <div class="container main-content">
        <!-- Page Header -->
        <div class="page-header">
            <div class="row align-items-center">
                <div class="col-md-8">
                    <h1 class="mb-2"><i class="fas fa-headset me-2"></i>Hỗ Trợ Khách Hàng</h1>
                    <p class="text-muted mb-0">Chúng tôi luôn sẵn sàng hỗ trợ bạn 24/7</p>
                </div>
                <div class="col-md-4 text-end">
                    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#submitTicketModal">
                        <i class="fas fa-plus me-2"></i>Tạo Yêu Cầu Hỗ Trợ
                    </button>
                </div>
            </div>
        </div>

        <!-- Support Stats -->
        <div class="stats-grid">
            <div class="stat-item">
                <div class="stat-icon">
                    <i class="fas fa-ticket-alt"></i>
                </div>
                <div class="stat-value">2</div>
                <div class="stat-label">Yêu Cầu Đang Mở</div>
            </div>
            <div class="stat-item">
                <div class="stat-icon">
                    <i class="fas fa-check-circle"></i>
                </div>
                <div class="stat-value">8</div>
                <div class="stat-label">Đã Giải Quyết</div>
            </div>
            <div class="stat-item">
                <div class="stat-icon">
                    <i class="fas fa-clock"></i>
                </div>
                <div class="stat-value">2.5</div>
                <div class="stat-label">Giờ Phản Hồi TB</div>
            </div>
            <div class="stat-item">
                <div class="stat-icon">
                    <i class="fas fa-star"></i>
                </div>
                <div class="stat-value">4.9</div>
                <div class="stat-label">Đánh Giá TB</div>
            </div>
        </div>

        <!-- Contact Information -->
        <div class="contact-info">
            <h4 class="mb-4"><i class="fas fa-phone me-2"></i>Liên Hệ Trực Tiếp</h4>
            
            <div class="row">
                <div class="col-md-4">
                    <div class="contact-item">
                        <i class="fas fa-phone"></i>
                        <div>
                            <h6 class="mb-1">Hotline</h6>
                            <p class="mb-0">(028) 1234-5678</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="contact-item">
                        <i class="fas fa-envelope"></i>
                        <div>
                            <h6 class="mb-1">Email</h6>
                            <p class="mb-0">support@staminagym.vn</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="contact-item">
                        <i class="fas fa-clock"></i>
                        <div>
                            <h6 class="mb-1">Giờ Làm Việc</h6>
                            <p class="mb-0">24/7 Hỗ Trợ</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Support Tabs -->
        <ul class="nav nav-tabs" id="supportTabs">
            <li class="nav-item">
                <a class="nav-link active" data-bs-toggle="tab" href="#tickets">Yêu Cầu Hỗ Trợ</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" data-bs-toggle="tab" href="#faq">Câu Hỏi Thường Gặp</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" data-bs-toggle="tab" href="#contact">Liên Hệ</a>
            </li>
        </ul>

        <div class="tab-content">
            <!-- Support Tickets Tab -->
            <div class="tab-pane fade show active" id="tickets">
                <div class="row">
                    <div class="col-12">
                        <h4 class="mb-4"><i class="fas fa-ticket-alt me-2"></i>Yêu Cầu Hỗ Trợ Của Tôi</h4>
                    </div>
                    
                    <!-- Open Tickets -->
                    <div class="col-12">
                        <h5 class="mb-3">Đang Mở</h5>
                    </div>
                    
                    <div class="col-md-6">
                        <div class="ticket-card ticket-open">
                            <div class="ticket-priority priority-high">Ưu tiên cao</div>
                            <div class="d-flex justify-content-between align-items-start mb-3">
                                <div>
                                    <h6 class="mb-1">Vấn đề với thiết bị tập luyện</h6>
                                    <small class="text-muted">Ticket #ST001 - Tạo lúc 15:30, 20/11/2024</small>
                                </div>
                                <span class="ticket-status status-open">Đang mở</span>
                            </div>
                            
                            <p class="text-muted mb-3">
                                Máy chạy bộ số 3 không hoạt động bình thường. Khi bật máy, màn hình hiển thị lỗi và không thể điều chỉnh tốc độ.
                            </p>
                            
                            <div class="d-flex justify-content-between align-items-center">
                                <small class="text-muted">Cập nhật lần cuối: 16:45, 20/11/2024</small>
                                <div>
                                    <button class="btn btn-sm btn-outline-primary me-2">Xem Chi Tiết</button>
                                    <button class="btn btn-sm btn-outline-success">Cập Nhật</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-6">
                        <div class="ticket-card ticket-pending">
                            <div class="ticket-priority priority-medium">Ưu tiên trung bình</div>
                            <div class="d-flex justify-content-between align-items-start mb-3">
                                <div>
                                    <h6 class="mb-1">Yêu cầu thay đổi lịch tập</h6>
                                    <small class="text-muted">Ticket #ST002 - Tạo lúc 09:15, 19/11/2024</small>
                                </div>
                                <span class="ticket-status status-pending">Đang xử lý</span>
                            </div>
                            
                            <p class="text-muted mb-3">
                                Tôi muốn thay đổi buổi tập Personal Training từ thứ 3 sang thứ 5 do lịch công việc thay đổi.
                            </p>
                            
                            <div class="d-flex justify-content-between align-items-center">
                                <small class="text-muted">Cập nhật lần cuối: 10:30, 19/11/2024</small>
                                <div>
                                    <button class="btn btn-sm btn-outline-primary me-2">Xem Chi Tiết</button>
                                    <button class="btn btn-sm btn-outline-success">Cập Nhật</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Closed Tickets -->
                    <div class="col-12 mt-4">
                        <h5 class="mb-3">Đã Giải Quyết</h5>
                    </div>
                    
                    <div class="col-md-6">
                        <div class="ticket-card ticket-closed">
                            <div class="ticket-priority priority-low">Ưu tiên thấp</div>
                            <div class="d-flex justify-content-between align-items-start mb-3">
                                <div>
                                    <h6 class="mb-1">Thắc mắc về gói tập</h6>
                                    <small class="text-muted">Ticket #ST003 - Tạo lúc 14:20, 18/11/2024</small>
                                </div>
                                <span class="ticket-status status-closed">Đã đóng</span>
                            </div>
                            
                            <p class="text-muted mb-3">
                                Tôi muốn biết thêm thông tin về gói VIP và các quyền lợi đi kèm.
                            </p>
                            
                            <div class="d-flex justify-content-between align-items-center">
                                <small class="text-muted">Đóng lúc: 15:45, 18/11/2024</small>
                                <div>
                                    <button class="btn btn-sm btn-outline-primary">Xem Chi Tiết</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-6">
                        <div class="ticket-card ticket-closed">
                            <div class="ticket-priority priority-medium">Ưu tiên trung bình</div>
                            <div class="d-flex justify-content-between align-items-start mb-3">
                                <div>
                                    <h6 class="mb-1">Vấn đề thanh toán</h6>
                                    <small class="text-muted">Ticket #ST004 - Tạo lúc 11:30, 17/11/2024</small>
                                </div>
                                <span class="ticket-status status-closed">Đã đóng</span>
                            </div>
                            
                            <p class="text-muted mb-3">
                                Tôi đã thanh toán nhưng hệ thống vẫn hiển thị chưa thanh toán. Cần kiểm tra lại.
                            </p>
                            
                            <div class="d-flex justify-content-between align-items-center">
                                <small class="text-muted">Đóng lúc: 13:15, 17/11/2024</small>
                                <div>
                                    <button class="btn btn-sm btn-outline-primary">Xem Chi Tiết</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- FAQ Tab -->
            <div class="tab-pane fade" id="faq">
                <div class="row">
                    <div class="col-12">
                        <h4 class="mb-4"><i class="fas fa-question-circle me-2"></i>Câu Hỏi Thường Gặp</h4>
                    </div>
                    
                    <div class="col-12">
                        <div class="faq-item" onclick="toggleFAQ(this)">
                            <div class="faq-question">
                                <span>Làm thế nào để đặt lịch tập với huấn luyện viên?</span>
                                <i class="fas fa-chevron-down"></i>
                            </div>
                            <div class="faq-answer">
                                Bạn có thể đặt lịch tập bằng cách:
                                <br>1. Đăng nhập vào tài khoản của bạn
                                <br>2. Vào mục "Lịch Tập" 
                                <br>3. Chọn ngày và giờ phù hợp
                                <br>4. Chọn huấn luyện viên và loại buổi tập
                                <br>5. Xác nhận đặt lịch
                            </div>
                        </div>
                        
                        <div class="faq-item" onclick="toggleFAQ(this)">
                            <div class="faq-question">
                                <span>Tôi có thể hủy buổi tập trước bao lâu?</span>
                                <i class="fas fa-chevron-down"></i>
                            </div>
                            <div class="faq-answer">
                                Bạn có thể hủy buổi tập trước ít nhất 2 giờ. Nếu hủy trong vòng 2 giờ, buổi tập sẽ được tính là đã sử dụng.
                            </div>
                        </div>
                        
                        <div class="faq-item" onclick="toggleFAQ(this)">
                            <div class="faq-question">
                                <span>Làm thế nào để gia hạn gói tập?</span>
                                <i class="fas fa-chevron-down"></i>
                            </div>
                            <div class="faq-answer">
                                Bạn có thể gia hạn gói tập bằng cách:
                                <br>1. Vào mục "Gói Tập" trong tài khoản
                                <br>2. Chọn "Gia Hạn" 
                                <br>3. Chọn thời gian gia hạn
                                <br>4. Thanh toán theo phương thức bạn chọn
                                <br>5. Gói tập sẽ được gia hạn tự động
                            </div>
                        </div>
                        
                        <div class="faq-item" onclick="toggleFAQ(this)">
                            <div class="faq-question">
                                <span>Tôi có thể tạm ngưng gói tập không?</span>
                                <i class="fas fa-chevron-down"></i>
                            </div>
                            <div class="faq-answer">
                                Có, bạn có thể tạm ngưng gói tập tối đa 3 tháng. Thời gian tạm ngưng sẽ được cộng thêm vào thời gian sử dụng của gói tập.
                            </div>
                        </div>
                        
                        <div class="faq-item" onclick="toggleFAQ(this)">
                            <div class="faq-question">
                                <span>Làm thế nào để thay đổi thông tin cá nhân?</span>
                                <i class="fas fa-chevron-down"></i>
                            </div>
                            <div class="faq-answer">
                                Bạn có thể thay đổi thông tin cá nhân bằng cách:
                                <br>1. Vào mục "Hồ Sơ" trong tài khoản
                                <br>2. Chỉnh sửa thông tin cần thay đổi
                                <br>3. Nhấn "Cập Nhật Thông Tin"
                                <br>4. Thông tin sẽ được lưu ngay lập tức
                            </div>
                        </div>
                        
                        <div class="faq-item" onclick="toggleFAQ(this)">
                            <div class="faq-question">
                                <span>Tôi có thể sử dụng phòng gym vào những giờ nào?</span>
                                <i class="fas fa-chevron-down"></i>
                            </div>
                            <div class="faq-answer">
                                Phòng gym mở cửa từ 5:00 đến 23:00 hàng ngày, bao gồm cả cuối tuần và ngày lễ. Chúng tôi chỉ đóng cửa vào ngày Tết Nguyên Đán.
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Contact Tab -->
            <div class="tab-pane fade" id="contact">
                <div class="row">
                    <div class="col-md-8">
                        <div class="support-card">
                            <h5 class="mb-4"><i class="fas fa-envelope me-2"></i>Gửi Tin Nhắn</h5>
                            
                            <form method="post" action="${pageContext.request.contextPath}/member/support">
                                <input type="hidden" name="action" value="submitTicket">
                                
                                <div class="mb-3">
                                    <label for="category" class="form-label">Danh Mục</label>
                                    <select class="form-control" id="category" name="category" required>
                                        <option value="">Chọn danh mục</option>
                                        <option value="technical">Vấn đề kỹ thuật</option>
                                        <option value="billing">Thanh toán</option>
                                        <option value="schedule">Lịch tập</option>
                                        <option value="membership">Gói tập</option>
                                        <option value="equipment">Thiết bị</option>
                                        <option value="other">Khác</option>
                                    </select>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="priority" class="form-label">Mức Độ Ưu Tiên</label>
                                    <select class="form-control" id="priority" name="priority" required>
                                        <option value="">Chọn mức độ ưu tiên</option>
                                        <option value="low">Thấp</option>
                                        <option value="medium">Trung bình</option>
                                        <option value="high">Cao</option>
                                    </select>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="subject" class="form-label">Tiêu Đề</label>
                                    <input type="text" class="form-control" id="subject" name="subject" 
                                           placeholder="Nhập tiêu đề yêu cầu hỗ trợ..." required>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="description" class="form-label">Mô Tả Chi Tiết</label>
                                    <textarea class="form-control" id="description" name="description" 
                                              rows="6" placeholder="Mô tả chi tiết vấn đề bạn gặp phải..." required></textarea>
                                </div>
                                
                                <div class="text-end">
                                    <button type="submit" class="btn btn-success">
                                        <i class="fas fa-paper-plane me-2"></i>Gửi Yêu Cầu
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                    
                    <div class="col-md-4">
                        <div class="support-card">
                            <h5 class="mb-4"><i class="fas fa-info-circle me-2"></i>Thông Tin Liên Hệ</h5>
                            
                            <div class="mb-4">
                                <h6><i class="fas fa-map-marker-alt me-2"></i>Địa Chỉ</h6>
                                <p class="text-muted">123 Đường Fitness, Quận 1, TP.HCM</p>
                            </div>
                            
                            <div class="mb-4">
                                <h6><i class="fas fa-phone me-2"></i>Điện Thoại</h6>
                                <p class="text-muted">(028) 1234-5678</p>
                            </div>
                            
                            <div class="mb-4">
                                <h6><i class="fas fa-envelope me-2"></i>Email</h6>
                                <p class="text-muted">support@staminagym.vn</p>
                            </div>
                            
                            <div class="mb-4">
                                <h6><i class="fas fa-clock me-2"></i>Giờ Làm Việc</h6>
                                <p class="text-muted">24/7 Hỗ Trợ</p>
                            </div>
                            
                            <div class="mb-4">
                                <h6><i class="fas fa-share-alt me-2"></i>Mạng Xã Hội</h6>
                                <div class="d-flex gap-2">
                                    <a href="#" class="btn btn-outline-primary btn-sm">
                                        <i class="fab fa-facebook-f"></i>
                                    </a>
                                    <a href="#" class="btn btn-outline-info btn-sm">
                                        <i class="fab fa-instagram"></i>
                                    </a>
                                    <a href="#" class="btn btn-outline-danger btn-sm">
                                        <i class="fab fa-youtube"></i>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Submit Ticket Modal -->
    <div class="modal fade" id="submitTicketModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Tạo Yêu Cầu Hỗ Trợ</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form method="post" action="${pageContext.request.contextPath}/member/support">
                        <input type="hidden" name="action" value="submitTicket">
                        
                        <div class="mb-3">
                            <label for="category" class="form-label">Danh Mục</label>
                            <select class="form-control" id="category" name="category" required>
                                <option value="">Chọn danh mục</option>
                                <option value="technical">Vấn đề kỹ thuật</option>
                                <option value="billing">Thanh toán</option>
                                <option value="schedule">Lịch tập</option>
                                <option value="membership">Gói tập</option>
                                <option value="equipment">Thiết bị</option>
                                <option value="other">Khác</option>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="priority" class="form-label">Mức Độ Ưu Tiên</label>
                            <select class="form-control" id="priority" name="priority" required>
                                <option value="">Chọn mức độ ưu tiên</option>
                                <option value="low">Thấp</option>
                                <option value="medium">Trung bình</option>
                                <option value="high">Cao</option>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="subject" class="form-label">Tiêu Đề</label>
                            <input type="text" class="form-control" id="subject" name="subject" 
                                   placeholder="Nhập tiêu đề yêu cầu hỗ trợ..." required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="description" class="form-label">Mô Tả Chi Tiết</label>
                            <textarea class="form-control" id="description" name="description" 
                                      rows="4" placeholder="Mô tả chi tiết vấn đề bạn gặp phải..." required></textarea>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary" form="submitTicketForm">Gửi Yêu Cầu</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Custom JS -->
    <script>
        function toggleFAQ(element) {
            const answer = element.querySelector('.faq-answer');
            const icon = element.querySelector('.fa-chevron-down');
            
            if (answer.classList.contains('show')) {
                answer.classList.remove('show');
                icon.style.transform = 'rotate(0deg)';
            } else {
                // Close all other FAQs
                document.querySelectorAll('.faq-answer.show').forEach(item => {
                    item.classList.remove('show');
                    item.parentElement.querySelector('.fa-chevron-down').style.transform = 'rotate(0deg)';
                });
                
                // Open current FAQ
                answer.classList.add('show');
                icon.style.transform = 'rotate(180deg)';
            }
        }

        // Handle form submissions
        document.querySelectorAll('form').forEach(form => {
            form.addEventListener('submit', function(e) {
                e.preventDefault();
                
                const formData = new FormData(this);
                const action = formData.get('action');
                
                if (action === 'submitTicket') {
                    alert('Đã gửi yêu cầu hỗ trợ thành công! Chúng tôi sẽ phản hồi trong vòng 24 giờ.');
                    
                    // TODO: Implement actual form submission
                    this.reset();
                    $('.modal').modal('hide');
                }
            });
        });

        // Handle ticket actions
        document.querySelectorAll('.ticket-card button').forEach(button => {
            button.addEventListener('click', function() {
                const action = this.textContent.trim();
                
                if (action === 'Xem Chi Tiết') {
                    // TODO: Show ticket details
                    alert('Hiển thị chi tiết yêu cầu hỗ trợ');
                } else if (action === 'Cập Nhật') {
                    // TODO: Show update form
                    alert('Hiển thị form cập nhật yêu cầu hỗ trợ');
                }
            });
        });

        // Initialize FAQ icons
        document.querySelectorAll('.faq-item .fa-chevron-down').forEach(icon => {
            icon.style.transition = 'transform 0.3s ease';
        });
    </script>
</body>
</html>
