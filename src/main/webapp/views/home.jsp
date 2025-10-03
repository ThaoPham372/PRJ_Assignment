<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Stamina Gym - Fitness & Strength Training</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
    
    <style>
        .hero-section {
            background: linear-gradient(135deg, #6B46C1 0%, #9333EA 100%);
            min-height: 70vh;
            display: flex;
            align-items: center;
            color: white;
            position: relative;
            overflow: hidden;
        }

        .hero-content {
            z-index: 2;
            position: relative;
        }

        .hero-title {
            font-size: 4rem;
            font-weight: 800;
            margin-bottom: 1.5rem;
            line-height: 1.1;
        }

        .hero-subtitle {
            font-size: 1.2rem;
            margin-bottom: 2rem;
            opacity: 0.9;
            max-width: 500px;
        }

        .btn-start {
            background: #FCD34D;
            color: #1F2937;
            padding: 15px 30px;
            border-radius: 50px;
            font-weight: 600;
            border: none;
            font-size: 1.1rem;
            transition: all 0.3s ease;
        }

        .btn-start:hover {
            background: #F59E0B;
            transform: translateY(-2px);
            box-shadow: 0 10px 25px rgba(0,0,0,0.2);
        }

        .stats-section {
            padding: 80px 0;
            background: #F9FAFB;
        }

        .stat-item {
            text-align: center;
            padding: 2rem;
        }

        .stat-icon {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 1.5rem;
            background: #6B46C1;
        }

        .stat-number {
            font-size: 3rem;
            font-weight: 800;
            color: #6B46C1;
            margin-bottom: 0.5rem;
        }

        .stat-label {
            font-size: 1rem;
            color: #6B7280;
            font-weight: 500;
        }

        .membership-section {
            padding: 80px 0;
            background: white;
        }

        .section-title {
            text-align: center;
            font-size: 2.5rem;
            font-weight: 700;
            color: #6B46C1;
            margin-bottom: 1rem;
        }

        .section-subtitle {
            text-align: center;
            color: #6B7280;
            margin-bottom: 3rem;
            font-size: 1.1rem;
        }

        .pricing-card {
            background: white;
            border-radius: 20px;
            padding: 2.5rem 2rem;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            transition: all 0.3s ease;
            position: relative;
            border: 2px solid transparent;
            height: 100%;
        }

        .pricing-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 20px 40px rgba(0,0,0,0.15);
        }

        .pricing-card.featured {
            border-color: #FCD34D;
            transform: scale(1.05);
        }

        .pricing-card.featured::before {
            content: "Phổ Biến";
            position: absolute;
            top: -10px;
            right: 20px;
            background: #FCD34D;
            color: #1F2937;
            padding: 5px 15px;
            border-radius: 15px;
            font-size: 0.8rem;
            font-weight: 600;
        }

        .plan-name {
            font-size: 1.5rem;
            font-weight: 600;
            color: #6B46C1;
            margin-bottom: 1rem;
        }

        .plan-price {
            font-size: 2.5rem;
            font-weight: 800;
            color: #6B46C1;
            margin-bottom: 0.5rem;
        }

        .plan-period {
            color: #6B7280;
            margin-bottom: 2rem;
        }

        .plan-features {
            list-style: none;
            padding: 0;
            margin-bottom: 2rem;
        }

        .plan-features li {
            padding: 0.5rem 0;
            display: flex;
            align-items: center;
        }

        .plan-features li i {
            color: #10B981;
            margin-right: 0.5rem;
        }

        .plan-features li.unavailable i {
            color: #EF4444;
        }

        .btn-plan {
            width: 100%;
            padding: 12px;
            border-radius: 50px;
            border: 2px solid #6B46C1;
            background: white;
            color: #6B46C1;
            font-weight: 600;
            transition: all 0.3s ease;
        }

        .btn-plan:hover, .btn-plan.featured {
            background: #6B46C1;
            color: white;
        }

        .btn-plan.featured {
            background: #FCD34D;
            border-color: #FCD34D;
            color: #1F2937;
        }

        .coaches-section {
            padding: 80px 0;
            background: #F9FAFB;
        }

        .coach-card {
            background: white;
            border-radius: 20px;
            padding: 2rem;
            text-align: center;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            transition: all 0.3s ease;
            height: 100%;
        }

        .coach-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 20px 40px rgba(0,0,0,0.15);
        }

        .coach-badge {
            background: #FCD34D;
            color: #1F2937;
            padding: 5px 15px;
            border-radius: 15px;
            font-size: 0.8rem;
            font-weight: 600;
            display: inline-block;
            margin-bottom: 1rem;
        }

        .coach-name {
            font-size: 1.3rem;
            font-weight: 600;
            color: #6B46C1;
            margin-bottom: 0.5rem;
        }

        .coach-specialty {
            color: #6B7280;
            margin-bottom: 1rem;
        }

        .coach-description {
            color: #6B7280;
            font-size: 0.9rem;
            margin-bottom: 1.5rem;
        }

        .coach-rating {
            color: #FCD34D;
        }

        .contact-section {
            padding: 80px 0;
            background: white;
        }

        .contact-info {
            margin-bottom: 2rem;
        }

        .contact-item {
            display: flex;
            align-items: center;
            margin-bottom: 1.5rem;
        }

        .contact-icon {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            background: #6B46C1;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 1rem;
        }

        .contact-icon i {
            color: white;
            font-size: 1.2rem;
        }

        .contact-details h6 {
            color: #6B46C1;
            font-weight: 600;
            margin-bottom: 0.25rem;
        }

        .contact-details p {
            color: #6B7280;
            margin: 0;
        }

        .form-control {
            border: 2px solid #E5E7EB;
            border-radius: 10px;
            padding: 12px 15px;
            transition: all 0.3s ease;
        }

        .form-control:focus {
            border-color: #6B46C1;
            box-shadow: 0 0 0 0.2rem rgba(107, 70, 193, 0.25);
        }

        .btn-submit {
            background: #6B46C1;
            color: white;
            padding: 12px 30px;
            border-radius: 50px;
            border: none;
            font-weight: 600;
            transition: all 0.3s ease;
        }

        .btn-submit:hover {
            background: #553C9A;
            transform: translateY(-2px);
        }

        .map-container {
            border-radius: 15px;
            overflow: hidden;
            height: 400px;
        }

        .navbar {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            box-shadow: 0 2px 20px rgba(0,0,0,0.1);
        }

        .navbar-brand {
            font-weight: 800;
            color: #6B46C1 !important;
            font-size: 1.5rem;
        }

        .nav-link {
            color: #374151 !important;
            font-weight: 500;
            transition: color 0.3s ease;
        }

        .nav-link:hover, .nav-link.active {
            color: #6B46C1 !important;
        }

        .btn-primary {
            background: #6B46C1;
            border-color: #6B46C1;
            border-radius: 25px;
            padding: 8px 20px;
        }

        .btn-primary:hover {
            background: #553C9A;
            border-color: #553C9A;
        }

        .btn-secondary {
            background: #FCD34D;
            border-color: #FCD34D;
            color: #1F2937;
            border-radius: 25px;
            padding: 12px 25px;
        }

        .btn-secondary:hover {
            background: #F59E0B;
            border-color: #F59E0B;
            color: #1F2937;
        }

        .footer {
            background: #1F2937;
            color: white;
            padding: 60px 0 20px;
        }

        .footer h5 {
            color: #FCD34D;
            margin-bottom: 1.5rem;
        }

        .footer a {
            color: #D1D5DB;
            text-decoration: none;
            transition: color 0.3s ease;
        }

        .footer a:hover {
            color: #FCD34D;
        }

        .social-links a {
            display: inline-block;
            width: 40px;
            height: 40px;
            background: #6B46C1;
            border-radius: 50%;
            text-align: center;
            line-height: 40px;
            margin-right: 10px;
            transition: all 0.3s ease;
        }

        .social-links a:hover {
            background: #FCD34D;
            color: #1F2937;
            transform: translateY(-2px);
        }

        .alert {
            border-radius: 10px;
            margin-bottom: 20px;
        }

        .alert-success {
            background-color: #D1FAE5;
            border-color: #10B981;
            color: #065F46;
        }

        .alert-danger {
            background-color: #FEE2E2;
            border-color: #EF4444;
            color: #991B1B;
        }
    </style>
</head>
<body>
    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-light fixed-top">
        <div class="container">
            <a class="navbar-brand" href="#home">
                <i class="fas fa-dumbbell me-2"></i>STAMINA GYM
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link active" href="#home">Trang Chủ</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#packages">Gói Tập</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#coaches">Huấn Luyện Viên</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#contact">Liên Hệ</a>
                    </li>
                    <li class="nav-item ms-3">
                        <a class="btn btn-primary" href="${pageContext.request.contextPath}/views/login.jsp">
                            <i class="fas fa-sign-in-alt me-1"></i>Đăng Nhập
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Hero Section -->
    <section id="home" class="hero-section">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-lg-6">
                    <div class="hero-content">
                        <h1 class="hero-title">Start a Better Shape of You!</h1>
                        <p class="hero-subtitle">
                            Chào mừng đến với Stamina Gym - nơi bạn có thể rèn luyện sức khỏe và xây dựng thể hình hoàn hảo với đội ngũ huấn luyện viên chuyên nghiệp.
                        </p>
                        <div class="mt-4">
                            <a href="#packages" class="btn btn-secondary btn-lg me-3">
                                <i class="fas fa-play me-2"></i>Bắt Đầu Ngay
                            </a>
                            <a href="#contact" class="btn btn-outline-primary btn-lg">
                                <i class="fas fa-phone me-2"></i>Liên Hệ
                            </a>
                        </div>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="text-center">
                        <!-- Placeholder for gym image -->
                        <div class="position-relative">
                            <img src="https://via.placeholder.com/500x400/3B1E78/FFD700?text=STAMINA+GYM" 
                                 alt="Stamina Gym" class="img-fluid rounded-3 shadow-custom">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Stats Section -->
    <section class="stats-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-3 col-md-6 mb-4">
                    <div class="stat-item">
                        <div class="stat-icon">
                            <i class="fas fa-users text-white fa-2x"></i>
                        </div>
                        <div class="stat-number">${totalMembers}+</div>
                        <div class="stat-label">Thành Viên</div>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6 mb-4">
                    <div class="stat-item">
                        <div class="stat-icon">
                            <i class="fas fa-user-tie text-white fa-2x"></i>
                        </div>
                        <div class="stat-number">${trainers}</div>
                        <div class="stat-label">Huấn Luyện Viên</div>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6 mb-4">
                    <div class="stat-item">
                        <div class="stat-icon">
                            <i class="fas fa-dumbbell text-white fa-2x"></i>
                        </div>
                        <div class="stat-number">${equipments}+</div>
                        <div class="stat-label">Thiết Bị</div>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6 mb-4">
                    <div class="stat-item">
                        <div class="stat-icon">
                            <i class="fas fa-trophy text-white fa-2x"></i>
                        </div>
                        <div class="stat-number">${successStories}</div>
                        <div class="stat-label">Thành Tích</div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Packages Section -->
    <section id="packages" class="membership-section">
        <div class="container">
            <div class="text-center mb-5">
                <h2 class="section-title">Gói Membership</h2>
                <p class="section-subtitle">Chọn gói tập phù hợp với nhu cầu và ngân sách của bạn</p>
            </div>
            <div class="row">
                <div class="col-lg-4 mb-4">
                    <div class="pricing-card">
                        <div class="plan-name">Basic</div>
                        <div class="plan-price">${basicPrice}</div>
                        <div class="plan-period">/ tháng</div>
                        <ul class="plan-features">
                            <li><i class="fas fa-check"></i> Sử dụng tất cả thiết bị</li>
                            <li><i class="fas fa-check"></i> Lớp học nhóm</li>
                            <li><i class="fas fa-check"></i> Phòng tắm & tủ đồ</li>
                            <li class="unavailable"><i class="fas fa-times"></i> Huấn luyện viên cá nhân</li>
                        </ul>
                        <a href="#contact" class="btn btn-plan">Chọn Gói</a>
                    </div>
                </div>
                
                <div class="col-lg-4 mb-4">
                    <div class="pricing-card featured">
                        <div class="plan-name">Premium</div>
                        <div class="plan-price">${premiumPrice}</div>
                        <div class="plan-period">/ tháng</div>
                        <ul class="plan-features">
                            <li><i class="fas fa-check"></i> Tất cả quyền lợi Basic</li>
                            <li><i class="fas fa-check"></i> 2 buổi PT/tháng</li>
                            <li><i class="fas fa-check"></i> Tư vấn dinh dưỡng</li>
                            <li><i class="fas fa-check"></i> Massage thư giãn</li>
                        </ul>
                        <a href="#contact" class="btn btn-plan featured">Chọn Gói</a>
                    </div>
                </div>
                
                <div class="col-lg-4 mb-4">
                    <div class="pricing-card">
                        <div class="plan-name">VIP</div>
                        <div class="plan-price">${vipPrice}</div>
                        <div class="plan-period">/ tháng</div>
                        <ul class="plan-features">
                            <li><i class="fas fa-check"></i> Tất cả quyền lợi Premium</li>
                            <li><i class="fas fa-check"></i> PT không giới hạn</li>
                            <li><i class="fas fa-check"></i> Phòng tập riêng</li>
                            <li><i class="fas fa-check"></i> Sauna & Steam</li>
                        </ul>
                        <a href="#contact" class="btn btn-plan">Chọn Gói</a>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Coaches Section -->
    <section id="coaches" class="coaches-section">
        <div class="container">
            <div class="text-center mb-5">
                <h2 class="section-title">Đội Ngũ Huấn Luyện Viên</h2>
                <p class="section-subtitle">Những chuyên gia hàng đầu sẽ đồng hành cùng bạn</p>
            </div>
            <div class="row">
                <c:forEach var="i" begin="0" end="3">
                    <div class="col-lg-3 col-md-6 mb-4">
                        <div class="coach-card">
                            <div class="coach-badge">Coach ${i + 1}</div>
                            <img src="https://via.placeholder.com/150x150/3B1E78/FFD700?text=PT${i + 1}" 
                                 alt="Coach ${i + 1}" class="rounded-circle mb-3" width="100" height="100">
                            <h4 class="coach-name">${coachNames[i]}</h4>
                            <p class="coach-specialty">${coachSpecialties[i]}</p>
                            <p class="coach-description">${coachDescriptions[i]}</p>
                            <div class="coach-rating">
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </section>

    <!-- Contact & Map Section -->
    <section id="contact" class="contact-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-6 mb-5">
                    <h2 class="section-title text-start">Liên Hệ & Đăng Ký</h2>
                    
                    <!-- Display success/error messages -->
                    <c:if test="${not empty successMessage}">
                        <div class="alert alert-success" role="alert">
                            <i class="fas fa-check-circle me-2"></i>${successMessage}
                        </div>
                    </c:if>
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger" role="alert">
                            <i class="fas fa-exclamation-circle me-2"></i>${errorMessage}
                        </div>
                    </c:if>
                    
                    <div class="contact-info">
                        <div class="contact-item">
                            <div class="contact-icon">
                                <i class="fas fa-map-marker-alt"></i>
                            </div>
                            <div class="contact-details">
                                <h6>Địa Chỉ</h6>
                                <p>${gymAddress}</p>
                            </div>
                        </div>
                        <div class="contact-item">
                            <div class="contact-icon">
                                <i class="fas fa-phone"></i>
                            </div>
                            <div class="contact-details">
                                <h6>Điện Thoại</h6>
                                <p>${gymPhone}</p>
                            </div>
                        </div>
                        <div class="contact-item">
                            <div class="contact-icon">
                                <i class="fas fa-clock"></i>
                            </div>
                            <div class="contact-details">
                                <h6>Giờ Hoạt Động</h6>
                                <p>${gymHours}</p>
                            </div>
                        </div>
                    </div>
                    
                    <%-- Registration Form --%>
                    <form method="post" action="${pageContext.request.contextPath}/home" class="mt-5">
                        <h5 class="text-primary mb-3">Đăng Ký Tư Vấn Miễn Phí</h5>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <input type="text" name="name" class="form-control" placeholder="Họ và tên" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <input type="tel" name="phone" class="form-control" placeholder="Số điện thoại" required>
                            </div>
                        </div>
                        <div class="mb-3">
                            <input type="email" name="email" class="form-control" placeholder="Email" required>
                        </div>
                        <div class="mb-3">
                            <select name="package" class="form-control">
                                <option value="">Chọn gói quan tâm</option>
                                <option value="basic">Basic - ${basicPrice}/tháng</option>
                                <option value="premium">Premium - ${premiumPrice}/tháng</option>
                                <option value="vip">VIP - ${vipPrice}/tháng</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <textarea name="message" class="form-control" rows="3" placeholder="Lời nhắn (tùy chọn)"></textarea>
                        </div>
                        <button type="submit" class="btn btn-submit">
                            <i class="fas fa-paper-plane me-2"></i>Gửi Đăng Ký
                        </button>
                    </form>
                </div>
                <div class="col-lg-6">
                    <h5 class="text-primary mb-3">Vị Trí Phòng Gym</h5>
                    <!-- Google Maps Embed -->
                    <div class="map-container">
                        <iframe 
                            src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3919.4326!2d106.6867861!3d10.7756538!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x31752f1b0c0b8b43%3A0x76b8b8b8b8b8b8b8!2zVHAuIEjhu5MgQ2jDrSBNaW5oLCBWaeG7h3QgTmFt!5e0!3m2!1svi!2s!4v1234567890123"
                            width="100%" 
                            height="450" 
                            style="border:0; border-radius: 15px;" 
                            allowfullscreen="" 
                            loading="lazy" 
                            referrerpolicy="no-referrer-when-downgrade">
                        </iframe>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Footer -->
    <footer class="footer">
        <div class="container">
            <div class="row">
                <div class="col-lg-4 mb-4">
                    <h5><i class="fas fa-dumbbell me-2"></i>STAMINA GYM</h5>
                    <p>Nơi bạn thực hiện ước mơ về thể hình hoàn hảo và sức khỏe tuyệt vời. Hãy bắt đầu hành trình của bạn cùng chúng tôi ngay hôm nay!</p>
                    <div class="social-links">
                        <a href="#"><i class="fab fa-facebook-f"></i></a>
                        <a href="#"><i class="fab fa-instagram"></i></a>
                        <a href="#"><i class="fab fa-youtube"></i></a>
                        <a href="#"><i class="fab fa-tiktok"></i></a>
                    </div>
                </div>
                <div class="col-lg-2 col-md-6 mb-4">
                    <h5>Liên Kết</h5>
                    <ul class="list-unstyled">
                        <li><a href="#home">Trang Chủ</a></li>
                        <li><a href="#packages">Gói Tập</a></li>
                        <li><a href="#coaches">Huấn Luyện Viên</a></li>
                        <li><a href="#contact">Liên Hệ</a></li>
                    </ul>
                </div>
                <div class="col-lg-3 col-md-6 mb-4">
                    <h5>Dịch Vụ</h5>
                    <ul class="list-unstyled">
                        <li><a href="#">Personal Training</a></li>
                        <li><a href="#">Group Classes</a></li>
                        <li><a href="#">Nutrition Consulting</a></li>
                        <li><a href="#">Massage Therapy</a></li>
                    </ul>
                </div>
                <div class="col-lg-3 col-md-6 mb-4">
                    <h5>Thông Tin</h5>
                    <ul class="list-unstyled">
                        <li><i class="fas fa-map-marker-alt me-2"></i>${gymAddress}</li>
                        <li><i class="fas fa-phone me-2"></i>${gymPhone}</li>
                        <li><i class="fas fa-envelope me-2"></i>${gymEmail}</li>
                        <li><i class="fas fa-clock me-2"></i>${gymHours}</li>
                    </ul>
                </div>
            </div>
            <hr class="my-4">
            <div class="row align-items-center">
                <div class="col-md-6">
                    <p class="mb-0">&copy; 2024 Stamina Gym. All rights reserved.</p>
                </div>
                <div class="col-md-6 text-md-end">
                    <a href="#" class="me-3">Privacy Policy</a>
                    <a href="#">Terms of Service</a>
                </div>
            </div>
        </div>
    </footer>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Custom JS -->
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
