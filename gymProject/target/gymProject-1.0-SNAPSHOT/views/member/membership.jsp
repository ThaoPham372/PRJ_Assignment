<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gói Tập - Stamina Gym</title>
    
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

        .membership-card {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            margin-bottom: 30px;
            border: 3px solid transparent;
            transition: all 0.3s ease;
        }

        .membership-card.current {
            border-color: var(--accent-color);
            background: linear-gradient(135deg, #fff 0%, #f8f9fa 100%);
        }

        .membership-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 30px rgba(0,0,0,0.15);
        }

        .membership-header {
            text-align: center;
            margin-bottom: 30px;
        }

        .membership-type {
            font-size: 2rem;
            font-weight: bold;
            color: var(--primary-color);
            margin-bottom: 10px;
        }

        .membership-price {
            font-size: 3rem;
            font-weight: bold;
            color: var(--accent-color);
            margin-bottom: 5px;
        }

        .membership-period {
            color: #666;
            font-size: 1.1rem;
        }

        .membership-status {
            display: inline-block;
            padding: 8px 20px;
            border-radius: 20px;
            font-weight: bold;
            font-size: 0.9rem;
            margin-bottom: 20px;
        }

        .status-active {
            background: #d4edda;
            color: #155724;
        }

        .status-expired {
            background: #f8d7da;
            color: #721c24;
        }

        .status-freezed {
            background: #fff3cd;
            color: #856404;
        }

        .feature-list {
            list-style: none;
            padding: 0;
            margin-bottom: 30px;
        }

        .feature-list li {
            padding: 8px 0;
            display: flex;
            align-items: center;
        }

        .feature-list li i {
            margin-right: 10px;
            width: 20px;
        }

        .feature-list li .fa-check {
            color: #28a745;
        }

        .feature-list li .fa-times {
            color: #dc3545;
        }

        .btn-upgrade {
            background: var(--accent-color);
            border: none;
            color: white;
            padding: 12px 30px;
            border-radius: 25px;
            font-weight: bold;
            transition: all 0.3s ease;
            width: 100%;
        }

        .btn-upgrade:hover {
            background: #d6734a;
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(236, 139, 94, 0.3);
            color: white;
        }

        .btn-renew {
            background: var(--primary-color);
            border: none;
            color: white;
            padding: 12px 30px;
            border-radius: 25px;
            font-weight: bold;
            transition: all 0.3s ease;
            width: 100%;
        }

        .btn-renew:hover {
            background: #2a1656;
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(59, 30, 120, 0.3);
            color: white;
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

        .upgrade-options {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }

        .package-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            margin-bottom: 20px;
            border: 2px solid transparent;
            transition: all 0.3s ease;
            text-align: center;
        }

        .package-card:hover {
            border-color: var(--accent-color);
            transform: translateY(-5px);
        }

        .package-card.recommended {
            border-color: var(--accent-color);
            background: linear-gradient(135deg, #fff 0%, #f8f9fa 100%);
        }

        .package-badge {
            background: var(--accent-color);
            color: white;
            padding: 5px 15px;
            border-radius: 15px;
            font-size: 0.8rem;
            font-weight: bold;
            margin-bottom: 15px;
            display: inline-block;
        }

        .package-name {
            font-size: 1.5rem;
            font-weight: bold;
            color: var(--primary-color);
            margin-bottom: 10px;
        }

        .package-price {
            font-size: 2rem;
            font-weight: bold;
            color: var(--accent-color);
            margin-bottom: 20px;
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
                        <a class="nav-link active" href="${pageContext.request.contextPath}/member/membership">
                            <i class="fas fa-id-card me-1"></i>Gói Tập
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/member/profile">
                            <i class="fas fa-user me-1"></i>Hồ Sơ
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
                    <h1 class="mb-2"><i class="fas fa-id-card me-2"></i>Gói Tập</h1>
                    <p class="text-muted mb-0">Quản lý gói tập và tùy chọn nâng cấp</p>
                </div>
                <div class="col-md-4 text-end">
                    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#renewModal">
                        <i class="fas fa-sync me-2"></i>Gia Hạn
                    </button>
                </div>
            </div>
        </div>

        <!-- Membership Stats -->
        <div class="stats-grid">
            <div class="stat-item">
                <div class="stat-icon">
                    <i class="fas fa-calendar-alt"></i>
                </div>
                <div class="stat-value">45</div>
                <div class="stat-label">Ngày Còn Lại</div>
            </div>
            <div class="stat-item">
                <div class="stat-icon">
                    <i class="fas fa-dumbbell"></i>
                </div>
                <div class="stat-value">12</div>
                <div class="stat-label">Buổi Đã Sử Dụng</div>
            </div>
            <div class="stat-item">
                <div class="stat-icon">
                    <i class="fas fa-clock"></i>
                </div>
                <div class="stat-value">24</div>
                <div class="stat-label">Tổng Buổi Tập</div>
            </div>
            <div class="stat-item">
                <div class="stat-icon">
                    <i class="fas fa-percentage"></i>
                </div>
                <div class="stat-value">50%</div>
                <div class="stat-label">Đã Sử Dụng</div>
            </div>
        </div>

        <!-- Current Membership -->
        <div class="row">
            <div class="col-12">
                <h4 class="mb-4"><i class="fas fa-star me-2"></i>Gói Tập Hiện Tại</h4>
            </div>
            
            <div class="col-md-8">
                <div class="membership-card current">
                    <div class="membership-header">
                        <div class="membership-type">Premium Membership</div>
                        <div class="membership-price">500K</div>
                        <div class="membership-period">/ tháng</div>
                        <div class="membership-status status-active">Đang Hoạt Động</div>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-6">
                            <ul class="feature-list">
                                <li><i class="fas fa-check"></i>Tất cả quyền lợi Basic</li>
                                <li><i class="fas fa-check"></i>2 buổi PT/tháng</li>
                                <li><i class="fas fa-check"></i>Tư vấn dinh dưỡng</li>
                                <li><i class="fas fa-check"></i>Massage thư giãn</li>
                            </ul>
                        </div>
                        <div class="col-md-6">
                            <ul class="feature-list">
                                <li><i class="fas fa-check"></i>Sử dụng tất cả thiết bị</li>
                                <li><i class="fas fa-check"></i>Lớp học nhóm không giới hạn</li>
                                <li><i class="fas fa-check"></i>Phòng tắm & tủ đồ</li>
                                <li><i class="fas fa-check"></i>WiFi miễn phí</li>
                            </ul>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-6">
                            <button class="btn btn-upgrade" data-bs-toggle="modal" data-bs-target="#upgradeModal">
                                <i class="fas fa-arrow-up me-2"></i>Nâng Cấp
                            </button>
                        </div>
                        <div class="col-md-6">
                            <button class="btn btn-renew" data-bs-toggle="modal" data-bs-target="#renewModal">
                                <i class="fas fa-sync me-2"></i>Gia Hạn
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-md-4">
                <div class="membership-card">
                    <h5 class="mb-3"><i class="fas fa-info-circle me-2"></i>Thông Tin Gói</h5>
                    
                    <div class="mb-3">
                        <strong>Ngày Bắt Đầu:</strong><br>
                        <span class="text-muted">01/10/2024</span>
                    </div>
                    
                    <div class="mb-3">
                        <strong>Ngày Hết Hạn:</strong><br>
                        <span class="text-muted">31/12/2024</span>
                    </div>
                    
                    <div class="mb-3">
                        <strong>Trạng Thái:</strong><br>
                        <span class="text-success">Đang hoạt động</span>
                    </div>
                    
                    <div class="mb-3">
                        <strong>Buổi Tập Còn Lại:</strong><br>
                        <span class="text-primary">12/24 buổi</span>
                    </div>
                    
                    <div class="mb-3">
                        <strong>Phương Thức Thanh Toán:</strong><br>
                        <span class="text-muted">Chuyển khoản</span>
                    </div>
                    
                    <button class="btn btn-outline-danger w-100" data-bs-toggle="modal" data-bs-target="#freezeModal">
                        <i class="fas fa-pause me-2"></i>Tạm Ngưng
                    </button>
                </div>
            </div>
        </div>

        <!-- Upgrade Options -->
        <div class="upgrade-options">
            <h4 class="mb-4"><i class="fas fa-arrow-up me-2"></i>Tùy Chọn Nâng Cấp</h4>
            
            <div class="row">
                <div class="col-md-4">
                    <div class="package-card">
                        <div class="package-badge">Nâng Cấp</div>
                        <div class="package-name">VIP Membership</div>
                        <div class="package-price">800K</div>
                        <div class="text-muted mb-3">/ tháng</div>
                        
                        <ul class="feature-list text-start">
                            <li><i class="fas fa-check"></i>Tất cả quyền lợi Premium</li>
                            <li><i class="fas fa-check"></i>PT không giới hạn</li>
                            <li><i class="fas fa-check"></i>Phòng tập riêng</li>
                            <li><i class="fas fa-check"></i>Sauna & Steam</li>
                            <li><i class="fas fa-check"></i>Personal Locker</li>
                        </ul>
                        
                        <button class="btn btn-primary w-100" onclick="upgradeMembership('vip')">
                            Nâng Cấp Ngay
                        </button>
                    </div>
                </div>
                
                <div class="col-md-4">
                    <div class="package-card recommended">
                        <div class="package-badge">Khuyến Nghị</div>
                        <div class="package-name">Annual Premium</div>
                        <div class="package-price">5,000K</div>
                        <div class="text-muted mb-3">/ năm (Tiết kiệm 17%)</div>
                        
                        <ul class="feature-list text-start">
                            <li><i class="fas fa-check"></i>Tất cả quyền lợi Premium</li>
                            <li><i class="fas fa-check"></i>3 buổi PT/tháng</li>
                            <li><i class="fas fa-check"></i>Tư vấn dinh dưỡng</li>
                            <li><i class="fas fa-check"></i>Massage thư giãn</li>
                            <li><i class="fas fa-check"></i>Ưu tiên đặt lịch</li>
                        </ul>
                        
                        <button class="btn btn-primary w-100" onclick="upgradeMembership('annual')">
                            Nâng Cấp Ngay
                        </button>
                    </div>
                </div>
                
                <div class="col-md-4">
                    <div class="package-card">
                        <div class="package-badge">Linh Hoạt</div>
                        <div class="package-name">Pay Per Session</div>
                        <div class="package-price">200K</div>
                        <div class="text-muted mb-3">/ buổi</div>
                        
                        <ul class="feature-list text-start">
                            <li><i class="fas fa-check"></i>Thanh toán theo buổi</li>
                            <li><i class="fas fa-check"></i>Không ràng buộc</li>
                            <li><i class="fas fa-check"></i>Sử dụng tất cả thiết bị</li>
                            <li><i class="fas fa-check"></i>Phòng tắm & tủ đồ</li>
                            <li><i class="fas fa-check"></i>Lớp học nhóm</li>
                        </ul>
                        
                        <button class="btn btn-outline-primary w-100" onclick="upgradeMembership('flexible')">
                            Chọn Gói
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Membership History -->
        <div class="row">
            <div class="col-12">
                <div class="membership-card">
                    <h5 class="mb-4"><i class="fas fa-history me-2"></i>Lịch Sử Gói Tập</h5>
                    
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>Gói Tập</th>
                                    <th>Thời Gian</th>
                                    <th>Trạng Thái</th>
                                    <th>Thanh Toán</th>
                                    <th>Hành Động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>
                                        <strong>Premium Membership</strong><br>
                                        <small class="text-muted">01/10/2024 - 31/12/2024</small>
                                    </td>
                                    <td>3 tháng</td>
                                    <td><span class="badge bg-success">Hoạt động</span></td>
                                    <td>500K/tháng</td>
                                    <td>
                                        <button class="btn btn-sm btn-outline-primary">Chi tiết</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <strong>Basic Membership</strong><br>
                                        <small class="text-muted">01/07/2024 - 30/09/2024</small>
                                    </td>
                                    <td>3 tháng</td>
                                    <td><span class="badge bg-secondary">Đã hết hạn</span></td>
                                    <td>300K/tháng</td>
                                    <td>
                                        <button class="btn btn-sm btn-outline-primary">Chi tiết</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <strong>Basic Membership</strong><br>
                                        <small class="text-muted">01/04/2024 - 30/06/2024</small>
                                    </td>
                                    <td>3 tháng</td>
                                    <td><span class="badge bg-secondary">Đã hết hạn</span></td>
                                    <td>300K/tháng</td>
                                    <td>
                                        <button class="btn btn-sm btn-outline-primary">Chi tiết</button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Upgrade Modal -->
    <div class="modal fade" id="upgradeModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Nâng Cấp Gói Tập</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form method="post" action="${pageContext.request.contextPath}/member/membership">
                        <input type="hidden" name="action" value="upgrade">
                        
                        <div class="mb-3">
                            <label for="membershipType" class="form-label">Chọn Gói Tập Mới</label>
                            <select class="form-control" id="membershipType" name="membershipType" required>
                                <option value="">Chọn gói tập</option>
                                <option value="vip">VIP - 800K/tháng</option>
                                <option value="annual">Annual Premium - 5,000K/năm</option>
                                <option value="flexible">Pay Per Session - 200K/buổi</option>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="paymentMethod" class="form-label">Phương Thức Thanh Toán</label>
                            <select class="form-control" id="paymentMethod" name="paymentMethod" required>
                                <option value="">Chọn phương thức</option>
                                <option value="bank">Chuyển khoản</option>
                                <option value="cash">Tiền mặt</option>
                                <option value="card">Thẻ tín dụng</option>
                            </select>
                        </div>
                        
                        <div class="alert alert-info">
                            <i class="fas fa-info-circle me-2"></i>
                            Gói tập mới sẽ có hiệu lực ngay sau khi thanh toán thành công.
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary" form="upgradeForm">Nâng Cấp</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Renew Modal -->
    <div class="modal fade" id="renewModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Gia Hạn Gói Tập</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form method="post" action="${pageContext.request.contextPath}/member/membership">
                        <input type="hidden" name="action" value="renew">
                        
                        <div class="mb-3">
                            <label for="renewalPeriod" class="form-label">Thời Gian Gia Hạn</label>
                            <select class="form-control" id="renewalPeriod" name="renewalPeriod" required>
                                <option value="">Chọn thời gian</option>
                                <option value="1">1 tháng</option>
                                <option value="3">3 tháng</option>
                                <option value="6">6 tháng</option>
                                <option value="12">12 tháng</option>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="paymentMethod" class="form-label">Phương Thức Thanh Toán</label>
                            <select class="form-control" id="paymentMethod" name="paymentMethod" required>
                                <option value="">Chọn phương thức</option>
                                <option value="bank">Chuyển khoản</option>
                                <option value="cash">Tiền mặt</option>
                                <option value="card">Thẻ tín dụng</option>
                            </select>
                        </div>
                        
                        <div class="alert alert-success">
                            <i class="fas fa-check-circle me-2"></i>
                            Gia hạn thành công! Gói tập sẽ được gia hạn thêm thời gian đã chọn.
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary" form="renewForm">Gia Hạn</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Freeze Modal -->
    <div class="modal fade" id="freezeModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Tạm Ngưng Gói Tập</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form method="post" action="${pageContext.request.contextPath}/member/membership">
                        <input type="hidden" name="action" value="freeze">
                        
                        <div class="mb-3">
                            <label for="freezeReason" class="form-label">Lý Do Tạm Ngưng</label>
                            <select class="form-control" id="freezeReason" name="freezeReason" required>
                                <option value="">Chọn lý do</option>
                                <option value="travel">Đi công tác/du lịch</option>
                                <option value="health">Vấn đề sức khỏe</option>
                                <option value="personal">Vấn đề cá nhân</option>
                                <option value="other">Khác</option>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="freezeDuration" class="form-label">Thời Gian Tạm Ngưng</label>
                            <select class="form-control" id="freezeDuration" name="freezeDuration" required>
                                <option value="">Chọn thời gian</option>
                                <option value="1">1 tháng</option>
                                <option value="2">2 tháng</option>
                                <option value="3">3 tháng</option>
                            </select>
                        </div>
                        
                        <div class="alert alert-warning">
                            <i class="fas fa-exclamation-triangle me-2"></i>
                            Gói tập sẽ được tạm ngưng và thời gian sẽ được gia hạn tương ứng.
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-warning" form="freezeForm">Tạm Ngưng</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Custom JS -->
    <script>
        function upgradeMembership(type) {
            document.getElementById('membershipType').value = type;
            $('#upgradeModal').modal('show');
        }

        // Handle form submissions
        document.querySelectorAll('form').forEach(form => {
            form.addEventListener('submit', function(e) {
                e.preventDefault();
                
                const formData = new FormData(this);
                const action = formData.get('action');
                
                switch(action) {
                    case 'upgrade':
                        alert('Nâng cấp gói tập thành công!');
                        break;
                    case 'renew':
                        alert('Gia hạn gói tập thành công!');
                        break;
                    case 'freeze':
                        alert('Tạm ngưng gói tập thành công!');
                        break;
                }
                
                // TODO: Implement actual form submission
                this.reset();
                $('.modal').modal('hide');
            });
        });

        // Update membership type display
        document.getElementById('membershipType').addEventListener('change', function() {
            const type = this.value;
            let price = '';
            
            switch(type) {
                case 'vip':
                    price = '800K/tháng';
                    break;
                case 'annual':
                    price = '5,000K/năm';
                    break;
                case 'flexible':
                    price = '200K/buổi';
                    break;
            }
            
            if (price) {
                // Update price display if needed
                console.log('Selected membership:', type, 'Price:', price);
            }
        });
    </script>
</body>
</html>
