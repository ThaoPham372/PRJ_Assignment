<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Member Dashboard - Stamina Gym</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    
    <style>
        .member-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px;
        }
        .stat-card {
            background: white;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            transition: transform 0.3s;
        }
        .stat-card:hover {
            transform: translateY(-5px);
        }
        .activity-item {
            border-left: 4px solid #667eea;
            padding-left: 15px;
            margin-bottom: 15px;
        }
        .session-item {
            background: #f8f9fa;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 10px;
        }
    </style>
</head>
<body class="bg-light">
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/member/dashboard">
                <i class="fas fa-dumbbell me-2"></i>STAMINA GYM
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/member/dashboard">
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
                        <a class="nav-link" href="${pageContext.request.contextPath}/member/support">
                            <i class="fas fa-headset me-1"></i>Hỗ Trợ
                        </a>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <span class="navbar-text me-3">
                            <i class="fas fa-user me-1"></i>
                            <c:out value="${currentUser.fullName}"/>
                        </span>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/logout">
                            <i class="fas fa-sign-out-alt"></i> Đăng xuất
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <!-- Welcome Card -->
        <div class="card member-card mb-4">
            <div class="card-body">
                <div class="row align-items-center">
                    <div class="col-md-8">
                        <h2 class="card-title">
                            <i class="fas fa-user-circle me-2"></i>
                            Chào mừng, <c:out value="${dashboardData.memberName}"/>
                        </h2>
                        <p class="card-text">
                            <i class="fas fa-crown me-1"></i>
                            Gói thành viên: <strong><c:out value="${dashboardData.packageType}"/></strong>
                        </p>
                        <p class="card-text">
                            <i class="fas fa-calendar me-1"></i>
                            Tham gia từ: <fmt:formatDate value="${dashboardData.joinDate}" pattern="dd/MM/yyyy"/>
                        </p>
                    </div>
                    <div class="col-md-4 text-end">
                        <i class="fas fa-heart fa-4x opacity-50"></i>
                    </div>
                </div>
            </div>
        </div>

        <!-- Statistics Cards -->
        <div class="row mb-4">
            <div class="col-md-3 mb-3">
                <div class="card stat-card text-center">
                    <div class="card-body">
                        <i class="fas fa-dumbbell fa-2x text-primary mb-2"></i>
                        <h5 class="card-title">Buổi tập</h5>
                        <h3 class="text-primary">${dashboardData.totalSessions}</h3>
                        <small class="text-muted">Tổng số buổi</small>
                    </div>
                </div>
            </div>
            <div class="col-md-3 mb-3">
                <div class="card stat-card text-center">
                    <div class="card-body">
                        <i class="fas fa-calendar-week fa-2x text-success mb-2"></i>
                        <h5 class="card-title">Tháng này</h5>
                        <h3 class="text-success">${dashboardData.sessionsThisMonth}</h3>
                        <small class="text-muted">Buổi tập</small>
                    </div>
                </div>
            </div>
            <div class="col-md-3 mb-3">
                <div class="card stat-card text-center">
                    <div class="card-body">
                        <i class="fas fa-clock fa-2x text-warning mb-2"></i>
                        <h5 class="card-title">Còn lại</h5>
                        <h3 class="text-warning">${dashboardData.remainingSessions}</h3>
                        <small class="text-muted">Buổi PT</small>
                    </div>
                </div>
            </div>
            <div class="col-md-3 mb-3">
                <div class="card stat-card text-center">
                    <div class="card-body">
                        <i class="fas fa-credit-card fa-2x text-info mb-2"></i>
                        <h5 class="card-title">Thanh toán</h5>
                        <h3 class="text-info">${dashboardData.nextPaymentDate}</h3>
                        <small class="text-muted">Ngày đến hạn</small>
                    </div>
                </div>
            </div>
        </div>

        <!-- Quick Actions -->
        <div class="row mb-4">
            <div class="col-12">
                <h4 class="mb-3"><i class="fas fa-bolt me-2"></i>Thao Tác Nhanh</h4>
            </div>
            
            <div class="col-md-2 mb-3">
                <div class="card stat-card text-center h-100">
                    <div class="card-body">
                        <a href="${pageContext.request.contextPath}/member/schedule" class="text-decoration-none">
                            <i class="fas fa-calendar-plus fa-3x text-primary mb-3"></i>
                            <h6 class="card-title text-primary">Đặt Lịch</h6>
                            <small class="text-muted">Đặt lịch tập mới</small>
                        </a>
                    </div>
                </div>
            </div>
            
            <div class="col-md-2 mb-3">
                <div class="card stat-card text-center h-100">
                    <div class="card-body">
                        <a href="${pageContext.request.contextPath}/member/workout" class="text-decoration-none">
                            <i class="fas fa-dumbbell fa-3x text-success mb-3"></i>
                            <h6 class="card-title text-success">Ghi Nhận</h6>
                            <small class="text-muted">Buổi tập hôm nay</small>
                        </a>
                    </div>
                </div>
            </div>
            
            <div class="col-md-2 mb-3">
                <div class="card stat-card text-center h-100">
                    <div class="card-body">
                        <a href="${pageContext.request.contextPath}/member/nutrition" class="text-decoration-none">
                            <i class="fas fa-apple-alt fa-3x text-warning mb-3"></i>
                            <h6 class="card-title text-warning">Dinh Dưỡng</h6>
                            <small class="text-muted">Ghi nhận bữa ăn</small>
                        </a>
                    </div>
                </div>
            </div>
            
            <div class="col-md-2 mb-3">
                <div class="card stat-card text-center h-100">
                    <div class="card-body">
                        <a href="${pageContext.request.contextPath}/member/membership" class="text-decoration-none">
                            <i class="fas fa-id-card fa-3x text-info mb-3"></i>
                            <h6 class="card-title text-info">Gói Tập</h6>
                            <small class="text-muted">Quản lý gói</small>
                        </a>
                    </div>
                </div>
            </div>
            
            <div class="col-md-2 mb-3">
                <div class="card stat-card text-center h-100">
                    <div class="card-body">
                        <a href="${pageContext.request.contextPath}/member/profile" class="text-decoration-none">
                            <i class="fas fa-user-edit fa-3x text-secondary mb-3"></i>
                            <h6 class="card-title text-secondary">Hồ Sơ</h6>
                            <small class="text-muted">Cập nhật thông tin</small>
                        </a>
                    </div>
                </div>
            </div>
            
            <div class="col-md-2 mb-3">
                <div class="card stat-card text-center h-100">
                    <div class="card-body">
                        <a href="${pageContext.request.contextPath}/member/support" class="text-decoration-none">
                            <i class="fas fa-headset fa-3x text-danger mb-3"></i>
                            <h6 class="card-title text-danger">Hỗ Trợ</h6>
                            <small class="text-muted">Liên hệ hỗ trợ</small>
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <!-- Recent Activities -->
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title mb-0">
                            <i class="fas fa-history me-2"></i>Hoạt động gần đây
                        </h5>
                    </div>
                    <div class="card-body">
                        <c:forEach var="activity" items="${dashboardData.recentActivities}">
                            <div class="activity-item">
                                <div class="d-flex justify-content-between align-items-start">
                                    <div>
                                        <h6 class="mb-1">
                                            <i class="fas fa-circle text-primary me-1" style="font-size: 8px;"></i>
                                            <c:out value="${activity.description}"/>
                                        </h6>
                                        <small class="text-muted">${activity.type}</small>
                                    </div>
                                    <small class="text-muted">${activity.time}</small>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>

            <!-- Upcoming Sessions -->
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title mb-0">
                            <i class="fas fa-calendar-alt me-2"></i>Buổi tập sắp tới
                        </h5>
                    </div>
                    <div class="card-body">
                        <c:forEach var="session" items="${dashboardData.upcomingSessions}">
                            <div class="session-item">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h6 class="mb-1">${session.type}</h6>
                                        <small class="text-muted">
                                            <i class="fas fa-user me-1"></i>${session.coach}
                                        </small>
                                    </div>
                                    <div class="text-end">
                                        <div class="fw-bold">${session.date}</div>
                                        <small class="text-muted">${session.time}</small>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>

        <!-- Quick Actions -->
        <div class="row mt-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title mb-0">
                            <i class="fas fa-bolt me-2"></i>Thao tác nhanh
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-3 mb-2">
                                <button class="btn btn-primary w-100">
                                    <i class="fas fa-calendar-plus me-2"></i>Đặt lịch tập
                                </button>
                            </div>
                            <div class="col-md-3 mb-2">
                                <button class="btn btn-success w-100">
                                    <i class="fas fa-credit-card me-2"></i>Thanh toán
                                </button>
                            </div>
                            <div class="col-md-3 mb-2">
                                <button class="btn btn-info w-100">
                                    <i class="fas fa-user-edit me-2"></i>Cập nhật hồ sơ
                                </button>
                            </div>
                            <div class="col-md-3 mb-2">
                                <button class="btn btn-warning w-100">
                                    <i class="fas fa-phone me-2"></i>Liên hệ hỗ trợ
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
