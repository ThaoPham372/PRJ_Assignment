<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hồ Sơ Cá Nhân - Stamina Gym</title>
    
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

        .profile-card {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }

        .profile-avatar {
            width: 120px;
            height: 120px;
            border-radius: 50%;
            object-fit: cover;
            border: 5px solid var(--secondary-color);
            margin: 0 auto 20px;
            display: block;
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

        .btn-outline-danger {
            border: 2px solid #dc3545;
            color: #dc3545;
            padding: 12px 30px;
            border-radius: 25px;
            font-weight: bold;
            transition: all 0.3s ease;
        }

        .btn-outline-danger:hover {
            background: #dc3545;
            color: white;
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(220, 53, 69, 0.3);
        }

        .progress-bar {
            background: linear-gradient(135deg, var(--primary-color), var(--accent-color));
            border-radius: 10px;
        }

        .progress {
            height: 10px;
            border-radius: 10px;
            background: #e9ecef;
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
                        <a class="nav-link" href="${pageContext.request.contextPath}/member/membership">
                            <i class="fas fa-id-card me-1"></i>Gói Tập
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/member/profile">
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
                    <h1 class="mb-2"><i class="fas fa-user me-2"></i>Hồ Sơ Cá Nhân</h1>
                    <p class="text-muted mb-0">Quản lý thông tin và cài đặt tài khoản của bạn</p>
                </div>
                <div class="col-md-4 text-end">
                    <button class="btn btn-outline-danger" data-bs-toggle="modal" data-bs-target="#changePasswordModal">
                        <i class="fas fa-key me-2"></i>Đổi Mật Khẩu
                    </button>
                </div>
            </div>
        </div>

        <!-- Profile Stats -->
        <div class="stats-grid">
            <div class="stat-item">
                <div class="stat-icon">
                    <i class="fas fa-calendar-check"></i>
                </div>
                <div class="stat-value">45</div>
                <div class="stat-label">Buổi Tập</div>
            </div>
            <div class="stat-item">
                <div class="stat-icon">
                    <i class="fas fa-fire"></i>
                </div>
                <div class="stat-value">15,600</div>
                <div class="stat-label">Calories Đốt</div>
            </div>
            <div class="stat-item">
                <div class="stat-icon">
                    <i class="fas fa-trophy"></i>
                </div>
                <div class="stat-value">12</div>
                <div class="stat-label">Ngày Liên Tiếp</div>
            </div>
            <div class="stat-item">
                <div class="stat-icon">
                    <i class="fas fa-clock"></i>
                </div>
                <div class="stat-value">75</div>
                <div class="stat-label">Phút/Buổi TB</div>
            </div>
        </div>

        <!-- Profile Information -->
        <div class="row">
            <div class="col-md-4">
                <div class="profile-card text-center">
                    <img src="https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-4.0.3&auto=format&fit=crop&w=300&q=80" 
                         alt="Profile Avatar" class="profile-avatar">
                    <h4 class="mb-2">Nguyễn Văn A</h4>
                    <p class="text-muted mb-3">Thành viên Premium</p>
                    
                    <div class="mb-3">
                        <label class="form-label">Ảnh Đại Diện</label>
                        <input type="file" class="form-control" accept="image/*">
                    </div>
                    
                    <button class="btn btn-primary w-100">
                        <i class="fas fa-upload me-2"></i>Cập Nhật Ảnh
                    </button>
                </div>
            </div>
            
            <div class="col-md-8">
                <div class="profile-card">
                    <h5 class="mb-4"><i class="fas fa-user-edit me-2"></i>Thông Tin Cá Nhân</h5>
                    
                    <form method="post" action="${pageContext.request.contextPath}/member/profile">
                        <input type="hidden" name="action" value="update">
                        
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="fullName" class="form-label">Họ và Tên</label>
                                <input type="text" class="form-control" id="fullName" name="fullName" 
                                       value="Nguyễn Văn A" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="email" class="form-label">Email</label>
                                <input type="email" class="form-control" id="email" name="email" 
                                       value="nguyenvana@email.com" required>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="phone" class="form-label">Số Điện Thoại</label>
                                <input type="tel" class="form-control" id="phone" name="phone" 
                                       value="0901234567" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="dateOfBirth" class="form-label">Ngày Sinh</label>
                                <input type="date" class="form-control" id="dateOfBirth" name="dateOfBirth" 
                                       value="1990-01-15">
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="gender" class="form-label">Giới Tính</label>
                                <select class="form-control" id="gender" name="gender">
                                    <option value="male" selected>Nam</option>
                                    <option value="female">Nữ</option>
                                    <option value="other">Khác</option>
                                </select>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="height" class="form-label">Chiều Cao (cm)</label>
                                <input type="number" class="form-control" id="height" name="height" 
                                       value="175" min="100" max="250">
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="weight" class="form-label">Cân Nặng (kg)</label>
                                <input type="number" class="form-control" id="weight" name="weight" 
                                       value="70" min="30" max="200" step="0.1">
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="bodyFat" class="form-label">Tỷ Lệ Mỡ (%)</label>
                                <input type="number" class="form-control" id="bodyFat" name="bodyFat" 
                                       value="18" min="5" max="50" step="0.1">
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="address" class="form-label">Địa Chỉ</label>
                            <textarea class="form-control" id="address" name="address" rows="3" 
                                      placeholder="Nhập địa chỉ của bạn...">123 Đường ABC, Quận 1, TP.HCM</textarea>
                        </div>
                        
                        <div class="mb-3">
                            <label for="goals" class="form-label">Mục Tiêu Tập Luyện</label>
                            <textarea class="form-control" id="goals" name="goals" rows="3" 
                                      placeholder="Nhập mục tiêu tập luyện của bạn...">Giảm cân và tăng cường sức mạnh</textarea>
                        </div>
                        
                        <div class="text-end">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-save me-2"></i>Cập Nhật Thông Tin
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Fitness Progress -->
        <div class="row">
            <div class="col-12">
                <div class="profile-card">
                    <h5 class="mb-4"><i class="fas fa-chart-line me-2"></i>Tiến Độ Tập Luyện</h5>
                    
                    <div class="row">
                        <div class="col-md-6 mb-4">
                            <div class="d-flex justify-content-between align-items-center mb-2">
                                <span class="fw-bold">Mục Tiêu Tuần</span>
                                <span class="text-muted">5/7 buổi</span>
                            </div>
                            <div class="progress">
                                <div class="progress-bar" role="progressbar" style="width: 71%"></div>
                            </div>
                        </div>
                        
                        <div class="col-md-6 mb-4">
                            <div class="d-flex justify-content-between align-items-center mb-2">
                                <span class="fw-bold">Mục Tiêu Calories</span>
                                <span class="text-muted">15,600/20,000</span>
                            </div>
                            <div class="progress">
                                <div class="progress-bar" role="progressbar" style="width: 78%"></div>
                            </div>
                        </div>
                        
                        <div class="col-md-6 mb-4">
                            <div class="d-flex justify-content-between align-items-center mb-2">
                                <span class="fw-bold">Mục Tiêu Cân Nặng</span>
                                <span class="text-muted">70/65 kg</span>
                            </div>
                            <div class="progress">
                                <div class="progress-bar" role="progressbar" style="width: 92%"></div>
                            </div>
                        </div>
                        
                        <div class="col-md-6 mb-4">
                            <div class="d-flex justify-content-between align-items-center mb-2">
                                <span class="fw-bold">Mục Tiêu Body Fat</span>
                                <span class="text-muted">18/15%</span>
                            </div>
                            <div class="progress">
                                <div class="progress-bar" role="progressbar" style="width: 83%"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Recent Activities -->
        <div class="row">
            <div class="col-12">
                <div class="profile-card">
                    <h5 class="mb-4"><i class="fas fa-history me-2"></i>Hoạt Động Gần Đây</h5>
                    
                    <div class="list-group">
                        <div class="list-group-item d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="mb-1">Hoàn thành buổi tập Personal Training</h6>
                                <small class="text-muted">Với huấn luyện viên Nguyễn Văn Nam</small>
                            </div>
                            <small class="text-muted">2 giờ trước</small>
                        </div>
                        
                        <div class="list-group-item d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="mb-1">Cập nhật tiến độ cân nặng</h6>
                                <small class="text-muted">70.2 kg → 70.0 kg</small>
                            </div>
                            <small class="text-muted">1 ngày trước</small>
                        </div>
                        
                        <div class="list-group-item d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="mb-1">Đặt lịch buổi tập Yoga</h6>
                                <small class="text-muted">Thứ 6, 22/11/2024 lúc 09:00</small>
                            </div>
                            <small class="text-muted">2 ngày trước</small>
                        </div>
                        
                        <div class="list-group-item d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="mb-1">Hoàn thành buổi tập HIIT</h6>
                                <small class="text-muted">Đốt cháy 450 calories</small>
                            </div>
                            <small class="text-muted">3 ngày trước</small>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Change Password Modal -->
    <div class="modal fade" id="changePasswordModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Đổi Mật Khẩu</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form method="post" action="${pageContext.request.contextPath}/member/profile">
                        <input type="hidden" name="action" value="changePassword">
                        
                        <div class="mb-3">
                            <label for="currentPassword" class="form-label">Mật Khẩu Hiện Tại</label>
                            <input type="password" class="form-control" id="currentPassword" 
                                   name="currentPassword" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="newPassword" class="form-label">Mật Khẩu Mới</label>
                            <input type="password" class="form-control" id="newPassword" 
                                   name="newPassword" required minlength="6">
                        </div>
                        
                        <div class="mb-3">
                            <label for="confirmPassword" class="form-label">Xác Nhận Mật Khẩu Mới</label>
                            <input type="password" class="form-control" id="confirmPassword" 
                                   name="confirmPassword" required minlength="6">
                        </div>
                        
                        <div class="alert alert-info">
                            <i class="fas fa-info-circle me-2"></i>
                            Mật khẩu phải có ít nhất 6 ký tự.
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary" form="changePasswordForm">Đổi Mật Khẩu</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Custom JS -->
    <script>
        // Handle password confirmation
        document.getElementById('confirmPassword').addEventListener('input', function() {
            const newPassword = document.getElementById('newPassword').value;
            const confirmPassword = this.value;
            
            if (newPassword !== confirmPassword) {
                this.setCustomValidity('Mật khẩu không khớp');
            } else {
                this.setCustomValidity('');
            }
        });

        // Handle form submission
        document.querySelector('form').addEventListener('submit', function(e) {
            e.preventDefault();
            
            // Show success message
            alert('Cập nhật thông tin thành công!');
            
            // TODO: Implement actual form submission
        });

        // Handle image upload
        document.querySelector('input[type="file"]').addEventListener('change', function(e) {
            const file = e.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    document.querySelector('.profile-avatar').src = e.target.result;
                };
                reader.readAsDataURL(file);
            }
        });

        // Animate progress bars on page load
        window.addEventListener('load', function() {
            const progressBars = document.querySelectorAll('.progress-bar');
            progressBars.forEach(bar => {
                const width = bar.style.width;
                bar.style.width = '0%';
                setTimeout(() => {
                    bar.style.width = width;
                }, 500);
            });
        });
    </script>
</body>
</html>
