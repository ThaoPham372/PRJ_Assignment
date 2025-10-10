<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Gym Management System</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    
    <style>
        :root {
            --primary-color: #2c3e50;
            --secondary-color: #3498db;
            --success-color: #27ae60;
            --warning-color: #f39c12;
            --danger-color: #e74c3c;
            --light-bg: #f8f9fa;
            --dark-bg: #2c3e50;
        }
        
        body {
            background-color: var(--light-bg);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        
        .navbar {
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .navbar-brand {
            font-weight: bold;
            font-size: 1.5rem;
        }
        
        .sidebar {
            background: white;
            min-height: calc(100vh - 76px);
            box-shadow: 2px 0 10px rgba(0,0,0,0.1);
            position: sticky;
            top: 76px;
        }
        
        .sidebar .nav-link {
            color: #6c757d;
            padding: 12px 20px;
            border-radius: 8px;
            margin: 4px 12px;
            transition: all 0.3s ease;
        }
        
        .sidebar .nav-link:hover,
        .sidebar .nav-link.active {
            background-color: var(--secondary-color);
            color: white;
            transform: translateX(5px);
        }
        
        .sidebar .nav-link i {
            width: 20px;
            margin-right: 10px;
        }
        
        .main-content {
            padding: 30px;
        }
        
        .stats-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
            border-left: 5px solid var(--secondary-color);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            margin-bottom: 20px;
        }
        
        .stats-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 25px rgba(0,0,0,0.15);
        }
        
        .stats-card.success {
            border-left-color: var(--success-color);
        }
        
        .stats-card.warning {
            border-left-color: var(--warning-color);
        }
        
        .stats-card.danger {
            border-left-color: var(--danger-color);
        }
        
        .stats-icon {
            font-size: 2.5rem;
            opacity: 0.8;
            margin-bottom: 15px;
        }
        
        .stats-number {
            font-size: 2rem;
            font-weight: bold;
            color: var(--primary-color);
            margin-bottom: 5px;
        }
        
        .stats-label {
            color: #6c757d;
            font-size: 0.9rem;
            text-transform: uppercase;
            letter-spacing: 1px;
        }
        
        .chart-container {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
            margin-bottom: 30px;
        }
        
        .table-container {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
        }
        
        .table {
            margin-bottom: 0;
        }
        
        .table th {
            border-top: none;
            font-weight: 600;
            color: var(--primary-color);
            text-transform: uppercase;
            font-size: 0.85rem;
            letter-spacing: 0.5px;
        }
        
        .badge {
            font-size: 0.75rem;
            padding: 6px 12px;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, var(--secondary-color), #5dade2);
            border: none;
            border-radius: 8px;
            padding: 10px 20px;
            font-weight: 500;
            transition: all 0.3s ease;
        }
        
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(52, 152, 219, 0.4);
        }
        
        .recent-activity {
            max-height: 400px;
            overflow-y: auto;
        }
        
        .activity-item {
            padding: 15px;
            border-left: 3px solid var(--secondary-color);
            margin-bottom: 15px;
            background: #f8f9fa;
            border-radius: 0 8px 8px 0;
        }
        
        .activity-time {
            font-size: 0.8rem;
            color: #6c757d;
        }
        
        .welcome-section {
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            color: white;
            border-radius: 15px;
            padding: 30px;
            margin-bottom: 30px;
        }
        
        .welcome-title {
            font-size: 2rem;
            font-weight: bold;
            margin-bottom: 10px;
        }
        
        .welcome-subtitle {
            opacity: 0.9;
            font-size: 1.1rem;
        }
        
        @media (max-width: 768px) {
            .sidebar {
                min-height: auto;
                position: static;
            }
            
            .main-content {
                padding: 15px;
            }
            
            .stats-card {
                margin-bottom: 15px;
            }
        }
    </style>
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">
                <i class="fas fa-dumbbell me-2"></i>
                Gym Management System
            </a>
            
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-user-circle me-1"></i>
                            ${sessionScope.fullName}
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="#"><i class="fas fa-user me-2"></i>Profile</a></li>
                            <li><a class="dropdown-item" href="#"><i class="fas fa-cog me-2"></i>Settings</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/auth/logout">
                                <i class="fas fa-sign-out-alt me-2"></i>Logout
                            </a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <div class="col-md-3 col-lg-2 sidebar">
                <nav class="nav flex-column">
                    <a class="nav-link active" href="#">
                        <i class="fas fa-tachometer-alt"></i>
                        Dashboard
                    </a>
                    <a class="nav-link" href="#">
                        <i class="fas fa-users"></i>
                        Quản lý Members
                    </a>
                    <a class="nav-link" href="#">
                        <i class="fas fa-user-tie"></i>
                        Quản lý Coaches
                    </a>
                    <a class="nav-link" href="#">
                        <i class="fas fa-box"></i>
                        Gói Membership
                    </a>
                    <a class="nav-link" href="#">
                        <i class="fas fa-dumbbell"></i>
                        Buổi Tập
                    </a>
                    <a class="nav-link" href="#">
                        <i class="fas fa-credit-card"></i>
                        Thanh Toán
                    </a>
                    <a class="nav-link" href="#">
                        <i class="fas fa-chart-bar"></i>
                        Báo Cáo
                    </a>
                    <a class="nav-link" href="#">
                        <i class="fas fa-cog"></i>
                        Cài Đặt
                    </a>
                </nav>
            </div>

            <!-- Main Content -->
            <div class="col-md-9 col-lg-10 main-content">
                <!-- Welcome Section -->
                <div class="welcome-section">
                    <div class="row align-items-center">
                        <div class="col-md-8">
                            <h1 class="welcome-title">Chào mừng trở lại!</h1>
                            <p class="welcome-subtitle">
                                Xin chào <strong>${sessionScope.fullName}</strong>, 
                                đây là tổng quan về hệ thống quản lý phòng gym của bạn.
                            </p>
                        </div>
                        <div class="col-md-4 text-end">
                            <i class="fas fa-chart-line" style="font-size: 4rem; opacity: 0.3;"></i>
                        </div>
                    </div>
                </div>

                <!-- Statistics Cards -->
                <div class="row">
                    <div class="col-md-3 col-sm-6">
                        <div class="stats-card">
                            <div class="d-flex align-items-center">
                                <div class="stats-icon text-primary">
                                    <i class="fas fa-users"></i>
                                </div>
                                <div class="ms-3">
                                    <div class="stats-number">1,234</div>
                                    <div class="stats-label">Tổng Members</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-3 col-sm-6">
                        <div class="stats-card success">
                            <div class="d-flex align-items-center">
                                <div class="stats-icon text-success">
                                    <i class="fas fa-user-check"></i>
                                </div>
                                <div class="ms-3">
                                    <div class="stats-number">1,089</div>
                                    <div class="stats-label">Active Members</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-3 col-sm-6">
                        <div class="stats-card warning">
                            <div class="d-flex align-items-center">
                                <div class="stats-icon text-warning">
                                    <i class="fas fa-user-tie"></i>
                                </div>
                                <div class="ms-3">
                                    <div class="stats-number">25</div>
                                    <div class="stats-label">Coaches</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-3 col-sm-6">
                        <div class="stats-card danger">
                            <div class="d-flex align-items-center">
                                <div class="stats-icon text-danger">
                                    <i class="fas fa-dollar-sign"></i>
                                </div>
                                <div class="ms-3">
                                    <div class="stats-number">₫2.5M</div>
                                    <div class="stats-label">Doanh Thu Tháng</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Charts Row -->
                <div class="row">
                    <div class="col-md-8">
                        <div class="chart-container">
                            <h5 class="mb-4">
                                <i class="fas fa-chart-line me-2"></i>
                                Thống Kê Members Theo Tháng
                            </h5>
                            <canvas id="membersChart" height="100"></canvas>
                        </div>
                    </div>
                    
                    <div class="col-md-4">
                        <div class="chart-container">
                            <h5 class="mb-4">
                                <i class="fas fa-chart-pie me-2"></i>
                                Phân Bố Gói Membership
                            </h5>
                            <canvas id="packageChart" height="200"></canvas>
                        </div>
                    </div>
                </div>

                <!-- Recent Activity & Quick Actions -->
                <div class="row">
                    <div class="col-md-8">
                        <div class="table-container">
                            <h5 class="mb-4">
                                <i class="fas fa-clock me-2"></i>
                                Hoạt Động Gần Đây
                            </h5>
                            <div class="recent-activity">
                                <div class="activity-item">
                                    <div class="d-flex justify-content-between align-items-start">
                                        <div>
                                            <strong>Nguyễn Văn A</strong> đã đăng ký gói Premium
                                            <br>
                                            <small class="activity-time">2 giờ trước</small>
                                        </div>
                                        <span class="badge bg-success">Mới</span>
                                    </div>
                                </div>
                                
                                <div class="activity-item">
                                    <div class="d-flex justify-content-between align-items-start">
                                        <div>
                                            <strong>Trần Thị B</strong> đã hoàn thành buổi tập
                                            <br>
                                            <small class="activity-time">4 giờ trước</small>
                                        </div>
                                        <span class="badge bg-primary">Tập luyện</span>
                                    </div>
                                </div>
                                
                                <div class="activity-item">
                                    <div class="d-flex justify-content-between align-items-start">
                                        <div>
                                            <strong>Lê Văn C</strong> đã thanh toán thành công
                                            <br>
                                            <small class="activity-time">6 giờ trước</small>
                                        </div>
                                        <span class="badge bg-warning">Thanh toán</span>
                                    </div>
                                </div>
                                
                                <div class="activity-item">
                                    <div class="d-flex justify-content-between align-items-start">
                                        <div>
                                            <strong>Phạm Thị D</strong> đã cập nhật thông tin cá nhân
                                            <br>
                                            <small class="activity-time">1 ngày trước</small>
                                        </div>
                                        <span class="badge bg-info">Cập nhật</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-4">
                        <div class="table-container">
                            <h5 class="mb-4">
                                <i class="fas fa-bolt me-2"></i>
                                Thao Tác Nhanh
                            </h5>
                            <div class="d-grid gap-2">
                                <button class="btn btn-primary" type="button">
                                    <i class="fas fa-user-plus me-2"></i>
                                    Thêm Member Mới
                                </button>
                                <button class="btn btn-outline-primary" type="button">
                                    <i class="fas fa-file-invoice me-2"></i>
                                    Tạo Báo Cáo
                                </button>
                                <button class="btn btn-outline-primary" type="button">
                                    <i class="fas fa-envelope me-2"></i>
                                    Gửi Thông Báo
                                </button>
                                <button class="btn btn-outline-primary" type="button">
                                    <i class="fas fa-cog me-2"></i>
                                    Cài Đặt Hệ Thống
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
    
    <!-- Charts -->
    <script>
        // Members Chart
        const membersCtx = document.getElementById('membersChart').getContext('2d');
        new Chart(membersCtx, {
            type: 'line',
            data: {
                labels: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6'],
                datasets: [{
                    label: 'Members Mới',
                    data: [65, 78, 90, 81, 96, 105],
                    borderColor: '#3498db',
                    backgroundColor: 'rgba(52, 152, 219, 0.1)',
                    tension: 0.4,
                    fill: true
                }, {
                    label: 'Members Active',
                    data: [120, 135, 148, 142, 155, 168],
                    borderColor: '#27ae60',
                    backgroundColor: 'rgba(39, 174, 96, 0.1)',
                    tension: 0.4,
                    fill: true
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'top',
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });

        // Package Chart
        const packageCtx = document.getElementById('packageChart').getContext('2d');
        new Chart(packageCtx, {
            type: 'doughnut',
            data: {
                labels: ['Cơ Bản', 'Tiêu Chuẩn', 'Premium'],
                datasets: [{
                    data: [30, 45, 25],
                    backgroundColor: ['#3498db', '#27ae60', '#f39c12'],
                    borderWidth: 0
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                    }
                }
            }
        });
    </script>
</body>
</html>
