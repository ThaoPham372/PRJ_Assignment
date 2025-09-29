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
    <section class="py-5 bg-white">
        <div class="container">
            <div class="row">
                <%-- Mock data for statistics using JSTL --%>
                <c:set var="totalMembers" value="1200" />
                <c:set var="trainers" value="24" />
                <c:set var="equipments" value="150" />
                <c:set var="successStories" value="980" />
                
                <div class="col-lg-3 col-md-6 mb-4">
                    <div class="stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-users"></i>
                        </div>
                        <div class="stat-number">${totalMembers}+</div>
                        <div class="stat-label">Thành Viên</div>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6 mb-4">
                    <div class="stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-user-tie"></i>
                        </div>
                        <div class="stat-number">${trainers}</div>
                        <div class="stat-label">Huấn Luyện Viên</div>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6 mb-4">
                    <div class="stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-dumbbell"></i>
                        </div>
                        <div class="stat-number">${equipments}+</div>
                        <div class="stat-label">Thiết Bị</div>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6 mb-4">
                    <div class="stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-trophy"></i>
                        </div>
                        <div class="stat-number">${successStories}</div>
                        <div class="stat-label">Câu Chuyện Thành Công</div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Packages Section -->
    <section id="packages" class="py-5">
        <div class="container">
            <div class="text-center mb-5">
                <h2 class="display-4 fw-bold mb-3">Gói Membership</h2>
                <p class="lead">Chọn gói tập phù hợp với nhu cầu và ngân sách của bạn</p>
            </div>
            <div class="row">
                <%-- Mock data for membership packages using JSTL --%>
                <c:set var="packages" value="<%=new java.util.ArrayList()%>" />
                <c:set var="basicPackage" value="<%=new java.util.HashMap()%>" />
                <c:set var="premiumPackage" value="<%=new java.util.HashMap()%>" />
                <c:set var="vipPackage" value="<%=new java.util.HashMap()%>" />
                
                <div class="col-lg-4 mb-4">
                    <div class="card package-card h-100">
                        <div class="card-body text-center">
                            <h3 class="card-title text-primary">Basic</h3>
                            <div class="display-4 fw-bold text-primary mb-3">300K</div>
                            <p class="text-muted">/ tháng</p>
                            <ul class="list-unstyled mt-4">
                                <li class="mb-2"><i class="fas fa-check text-success me-2"></i>Sử dụng tất cả thiết bị</li>
                                <li class="mb-2"><i class="fas fa-check text-success me-2"></i>Lớp học nhóm</li>
                                <li class="mb-2"><i class="fas fa-check text-success me-2"></i>Phòng tắm & tủ đồ</li>
                                <li class="mb-2"><i class="fas fa-times text-danger me-2"></i>Huấn luyện viên cá nhân</li>
                            </ul>
                            <a href="#contact" class="btn btn-outline-primary w-100 mt-4">Chọn Gói</a>
                        </div>
                    </div>
                </div>
                
                <div class="col-lg-4 mb-4">
                    <div class="card package-card featured h-100">
                        <span class="badge">Phổ Biến</span>
                        <div class="card-body text-center">
                            <h3 class="card-title text-primary">Premium</h3>
                            <div class="display-4 fw-bold text-primary mb-3">500K</div>
                            <p class="text-muted">/ tháng</p>
                            <ul class="list-unstyled mt-4">
                                <li class="mb-2"><i class="fas fa-check text-success me-2"></i>Tất cả quyền lợi Basic</li>
                                <li class="mb-2"><i class="fas fa-check text-success me-2"></i>2 buổi PT/tháng</li>
                                <li class="mb-2"><i class="fas fa-check text-success me-2"></i>Tư vấn dinh dưỡng</li>
                                <li class="mb-2"><i class="fas fa-check text-success me-2"></i>Massage thư giãn</li>
                            </ul>
                            <a href="#contact" class="btn btn-secondary w-100 mt-4">Chọn Gói</a>
                        </div>
                    </div>
                </div>
                
                <div class="col-lg-4 mb-4">
                    <div class="card package-card h-100">
                        <div class="card-body text-center">
                            <h3 class="card-title text-primary">VIP</h3>
                            <div class="display-4 fw-bold text-primary mb-3">800K</div>
                            <p class="text-muted">/ tháng</p>
                            <ul class="list-unstyled mt-4">
                                <li class="mb-2"><i class="fas fa-check text-success me-2"></i>Tất cả quyền lợi Premium</li>
                                <li class="mb-2"><i class="fas fa-check text-success me-2"></i>PT không giới hạn</li>
                                <li class="mb-2"><i class="fas fa-check text-success me-2"></i>Phòng tập riêng</li>
                                <li class="mb-2"><i class="fas fa-check text-success me-2"></i>Sauna & Steam</li>
                            </ul>
                            <a href="#contact" class="btn btn-outline-primary w-100 mt-4">Chọn Gói</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Coaches Section -->
    <section id="coaches" class="py-5 bg-white">
        <div class="container">
            <div class="text-center mb-5">
                <h2 class="display-4 fw-bold mb-3">Đội Ngũ Huấn Luyện Viên</h2>
                <p class="lead">Những chuyên gia hàng đầu sẽ đồng hành cùng bạn</p>
            </div>
            <div class="row">
                <%-- Mock data for coaches using JSTL --%>
                <c:forEach var="i" begin="1" end="4">
                    <div class="col-lg-3 col-md-6 mb-4">
                        <div class="card coach-card h-100">
                            <div class="card-body">
                                <img src="https://via.placeholder.com/150x150/3B1E78/FFD700?text=PT${i}" 
                                     alt="Coach ${i}" class="coach-image">
                                <h5 class="card-title">
                                    <c:choose>
                                        <c:when test="${i == 1}">Nguyễn Văn Nam</c:when>
                                        <c:when test="${i == 2}">Trần Thị Lan</c:when>
                                        <c:when test="${i == 3}">Lê Hoàng Minh</c:when>
                                        <c:otherwise>Phạm Thu Hà</c:otherwise>
                                    </c:choose>
                                </h5>
                                <p class="text-muted mb-2">
                                    <c:choose>
                                        <c:when test="${i == 1}">Chuyên gia Bodybuilding</c:when>
                                        <c:when test="${i == 2}">Yoga & Pilates Instructor</c:when>
                                        <c:when test="${i == 3}">Strength & Conditioning</c:when>
                                        <c:otherwise>Nutritionist & Fitness</c:otherwise>
                                    </c:choose>
                                </p>
                                <p class="card-text">
                                    <c:choose>
                                        <c:when test="${i == 1}">5+ năm kinh nghiệm, chuyên về xây dựng cơ bắp</c:when>
                                        <c:when test="${i == 2}">Chứng chỉ quốc tế Yoga Alliance, 3+ năm</c:when>
                                        <c:when test="${i == 3}">HLV đội tuyển quốc gia, 7+ năm kinh nghiệm</c:when>
                                        <c:otherwise>Thạc sĩ Dinh dưỡng, chuyên gia tư vấn</c:otherwise>
                                    </c:choose>
                                </p>
                                <div class="text-center">
                                    <div class="star-rating text-yellow">
                                        <i class="fas fa-star"></i>
                                        <i class="fas fa-star"></i>
                                        <i class="fas fa-star"></i>
                                        <i class="fas fa-star"></i>
                                        <i class="fas fa-star"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </section>

    <!-- Contact & Map Section -->
    <section id="contact" class="py-5">
        <div class="container">
            <div class="row">
                <div class="col-lg-6 mb-5">
                    <h2 class="display-5 fw-bold mb-4">Liên Hệ & Đăng Ký</h2>
                    <div class="mb-4">
                        <h5 class="text-primary"><i class="fas fa-map-marker-alt me-2"></i>Địa Chỉ</h5>
                        <p>123 Đường Fitness, Quận 1, TP.HCM</p>
                    </div>
                    <div class="mb-4">
                        <h5 class="text-primary"><i class="fas fa-phone me-2"></i>Điện Thoại</h5>
                        <p>(028) 1234-5678</p>
                    </div>
                    <div class="mb-4">
                        <h5 class="text-primary"><i class="fas fa-clock me-2"></i>Giờ Hoạt Động</h5>
                        <p>Thứ 2 - Chủ Nhật: 5:00 - 23:00</p>
                    </div>
                    
                    <%-- Registration Form --%>
                    <form class="mt-5">
                        <h5 class="text-primary mb-3">Đăng Ký Tư Vấn Miễn Phí</h5>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <input type="text" class="form-control" placeholder="Họ và tên" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <input type="tel" class="form-control" placeholder="Số điện thoại" required>
                            </div>
                        </div>
                        <div class="mb-3">
                            <input type="email" class="form-control" placeholder="Email" required>
                        </div>
                        <div class="mb-3">
                            <select class="form-control">
                                <option>Chọn gói quan tâm</option>
                                <option value="basic">Basic - 300K/tháng</option>
                                <option value="premium">Premium - 500K/tháng</option>
                                <option value="vip">VIP - 800K/tháng</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <textarea class="form-control" rows="3" placeholder="Lời nhắn (tùy chọn)"></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">
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
                        <li><i class="fas fa-map-marker-alt me-2"></i>123 Đường Fitness, Q1, HCM</li>
                        <li><i class="fas fa-phone me-2"></i>(028) 1234-5678</li>
                        <li><i class="fas fa-envelope me-2"></i>info@staminagym.vn</li>
                        <li><i class="fas fa-clock me-2"></i>5:00 - 23:00 (T2-CN)</li>
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
