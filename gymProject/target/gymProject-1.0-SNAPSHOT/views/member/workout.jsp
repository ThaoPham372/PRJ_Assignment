<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Buổi Tập - Stamina Gym</title>
    
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
            transition: transform 0.3s ease;
        }

        .stat-item:hover {
            transform: translateY(-5px);
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

        .workout-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            margin-bottom: 20px;
            transition: transform 0.3s ease;
        }

        .workout-card:hover {
            transform: translateY(-5px);
        }

        .workout-header {
            display: flex;
            justify-content: between;
            align-items: center;
            margin-bottom: 20px;
        }

        .workout-type {
            font-size: 1.2rem;
            font-weight: bold;
            color: var(--primary-color);
        }

        .workout-date {
            color: #666;
            font-size: 0.9rem;
        }

        .workout-stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
            gap: 15px;
            margin-bottom: 20px;
        }

        .workout-stat {
            text-align: center;
            padding: 15px;
            background: var(--bg-light);
            border-radius: 10px;
        }

        .workout-stat-value {
            font-size: 1.5rem;
            font-weight: bold;
            color: var(--accent-color);
            margin-bottom: 5px;
        }

        .workout-stat-label {
            color: #666;
            font-size: 0.8rem;
        }

        .btn-log-workout {
            background: var(--accent-color);
            border: none;
            color: white;
            padding: 12px 30px;
            border-radius: 25px;
            font-weight: bold;
            transition: all 0.3s ease;
        }

        .btn-log-workout:hover {
            background: #d6734a;
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(236, 139, 94, 0.3);
            color: white;
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

        .progress-bar {
            background: linear-gradient(135deg, var(--primary-color), var(--accent-color));
            border-radius: 10px;
        }

        .progress {
            height: 10px;
            border-radius: 10px;
            background: #e9ecef;
        }

        .chart-container {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }

        .tab-content {
            margin-top: 20px;
        }

        .nav-tabs .nav-link {
            color: var(--primary-color);
            border: none;
            border-bottom: 3px solid transparent;
            padding: 15px 25px;
            font-weight: bold;
        }

        .nav-tabs .nav-link.active {
            color: var(--accent-color);
            border-bottom-color: var(--accent-color);
            background: none;
        }

        .nav-tabs .nav-link:hover {
            border-bottom-color: var(--accent-color);
            color: var(--accent-color);
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
                        <a class="nav-link active" href="${pageContext.request.contextPath}/member/workout">
                            <i class="fas fa-dumbbell me-1"></i>Buổi Tập
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
                    <h1 class="mb-2"><i class="fas fa-dumbbell me-2"></i>Buổi Tập</h1>
                    <p class="text-muted mb-0">Theo dõi và ghi nhận các buổi tập của bạn</p>
                </div>
                <div class="col-md-4 text-end">
                    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#logWorkoutModal">
                        <i class="fas fa-plus me-2"></i>Ghi Nhận Buổi Tập
                    </button>
                </div>
            </div>
        </div>

        <!-- Workout Stats -->
        <div class="stats-grid">
            <div class="stat-item">
                <div class="stat-icon">
                    <i class="fas fa-calendar-check"></i>
                </div>
                <div class="stat-value">45</div>
                <div class="stat-label">Tổng Buổi Tập</div>
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

        <!-- Progress Charts -->
        <div class="chart-container">
            <h5 class="mb-4"><i class="fas fa-chart-line me-2"></i>Tiến Độ Tập Luyện</h5>
            
            <ul class="nav nav-tabs" id="progressTabs">
                <li class="nav-item">
                    <a class="nav-link active" data-bs-toggle="tab" href="#weekly">Tuần</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-bs-toggle="tab" href="#monthly">Tháng</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-bs-toggle="tab" href="#yearly">Năm</a>
                </li>
            </ul>
            
            <div class="tab-content">
                <div class="tab-pane fade show active" id="weekly">
                    <div class="row">
                        <div class="col-md-6">
                            <h6 class="mb-3">Buổi Tập Tuần Này</h6>
                            <div class="progress mb-3">
                                <div class="progress-bar" role="progressbar" style="width: 71%"></div>
                            </div>
                            <p class="text-muted">5/7 buổi (71%)</p>
                        </div>
                        <div class="col-md-6">
                            <h6 class="mb-3">Calories Đốt</h6>
                            <div class="progress mb-3">
                                <div class="progress-bar" role="progressbar" style="width: 85%"></div>
                            </div>
                            <p class="text-muted">4,250/5,000 calories (85%)</p>
                        </div>
                    </div>
                </div>
                <div class="tab-pane fade" id="monthly">
                    <div class="row">
                        <div class="col-md-6">
                            <h6 class="mb-3">Buổi Tập Tháng Này</h6>
                            <div class="progress mb-3">
                                <div class="progress-bar" role="progressbar" style="width: 73%"></div>
                            </div>
                            <p class="text-muted">22/30 buổi (73%)</p>
                        </div>
                        <div class="col-md-6">
                            <h6 class="mb-3">Calories Đốt</h6>
                            <div class="progress mb-3">
                                <div class="progress-bar" role="progressbar" style="width: 78%"></div>
                            </div>
                            <p class="text-muted">15,600/20,000 calories (78%)</p>
                        </div>
                    </div>
                </div>
                <div class="tab-pane fade" id="yearly">
                    <div class="row">
                        <div class="col-md-6">
                            <h6 class="mb-3">Buổi Tập Năm Nay</h6>
                            <div class="progress mb-3">
                                <div class="progress-bar" role="progressbar" style="width: 68%"></div>
                            </div>
                            <p class="text-muted">248/365 buổi (68%)</p>
                        </div>
                        <div class="col-md-6">
                            <h6 class="mb-3">Calories Đốt</h6>
                            <div class="progress mb-3">
                                <div class="progress-bar" role="progressbar" style="width: 72%"></div>
                            </div>
                            <p class="text-muted">180,000/250,000 calories (72%)</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Recent Workouts -->
        <div class="row">
            <div class="col-12">
                <h4 class="mb-4"><i class="fas fa-history me-2"></i>Buổi Tập Gần Đây</h4>
            </div>
            
            <div class="col-md-6">
                <div class="workout-card">
                    <div class="workout-header">
                        <div>
                            <div class="workout-type">Personal Training</div>
                            <div class="workout-date">Hôm nay, 14:00</div>
                        </div>
                        <div>
                            <span class="badge bg-success">Hoàn thành</span>
                        </div>
                    </div>
                    
                    <div class="workout-stats">
                        <div class="workout-stat">
                            <div class="workout-stat-value">75</div>
                            <div class="workout-stat-label">Phút</div>
                        </div>
                        <div class="workout-stat">
                            <div class="workout-stat-value">450</div>
                            <div class="workout-stat-label">Calories</div>
                        </div>
                        <div class="workout-stat">
                            <div class="workout-stat-value">85</div>
                            <div class="workout-stat-label">% HR Max</div>
                        </div>
                    </div>
                    
                    <p class="text-muted mb-3">
                        <strong>Huấn luyện viên:</strong> Nguyễn Văn Nam<br>
                        <strong>Nội dung:</strong> Cardio & Strength Training
                    </p>
                    
                    <div class="text-end">
                        <button class="btn btn-outline-primary btn-sm">Chi tiết</button>
                    </div>
                </div>
            </div>
            
            <div class="col-md-6">
                <div class="workout-card">
                    <div class="workout-header">
                        <div>
                            <div class="workout-type">HIIT Training</div>
                            <div class="workout-date">Hôm qua, 18:30</div>
                        </div>
                        <div>
                            <span class="badge bg-success">Hoàn thành</span>
                        </div>
                    </div>
                    
                    <div class="workout-stats">
                        <div class="workout-stat">
                            <div class="workout-stat-value">45</div>
                            <div class="workout-stat-label">Phút</div>
                        </div>
                        <div class="workout-stat">
                            <div class="workout-stat-value">520</div>
                            <div class="workout-stat-label">Calories</div>
                        </div>
                        <div class="workout-stat">
                            <div class="workout-stat-value">92</div>
                            <div class="workout-stat-label">% HR Max</div>
                        </div>
                    </div>
                    
                    <p class="text-muted mb-3">
                        <strong>Huấn luyện viên:</strong> Lê Hoàng Minh<br>
                        <strong>Nội dung:</strong> High Intensity Interval Training
                    </p>
                    
                    <div class="text-end">
                        <button class="btn btn-outline-primary btn-sm">Chi tiết</button>
                    </div>
                </div>
            </div>
            
            <div class="col-md-6">
                <div class="workout-card">
                    <div class="workout-header">
                        <div>
                            <div class="workout-type">Yoga Class</div>
                            <div class="workout-date">2 ngày trước, 09:00</div>
                        </div>
                        <div>
                            <span class="badge bg-success">Hoàn thành</span>
                        </div>
                    </div>
                    
                    <div class="workout-stats">
                        <div class="workout-stat">
                            <div class="workout-stat-value">60</div>
                            <div class="workout-stat-label">Phút</div>
                        </div>
                        <div class="workout-stat">
                            <div class="workout-stat-value">180</div>
                            <div class="workout-stat-label">Calories</div>
                        </div>
                        <div class="workout-stat">
                            <div class="workout-stat-value">65</div>
                            <div class="workout-stat-label">% HR Max</div>
                        </div>
                    </div>
                    
                    <p class="text-muted mb-3">
                        <strong>Huấn luyện viên:</strong> Trần Thị Lan<br>
                        <strong>Nội dung:</strong> Hatha Yoga cho người mới bắt đầu
                    </p>
                    
                    <div class="text-end">
                        <button class="btn btn-outline-primary btn-sm">Chi tiết</button>
                    </div>
                </div>
            </div>
            
            <div class="col-md-6">
                <div class="workout-card">
                    <div class="workout-header">
                        <div>
                            <div class="workout-type">Cardio</div>
                            <div class="workout-date">3 ngày trước, 07:00</div>
                        </div>
                        <div>
                            <span class="badge bg-success">Hoàn thành</span>
                        </div>
                    </div>
                    
                    <div class="workout-stats">
                        <div class="workout-stat">
                            <div class="workout-stat-value">90</div>
                            <div class="workout-stat-label">Phút</div>
                        </div>
                        <div class="workout-stat">
                            <div class="workout-stat-value">380</div>
                            <div class="workout-stat-label">Calories</div>
                        </div>
                        <div class="workout-stat">
                            <div class="workout-stat-value">78</div>
                            <div class="workout-stat-label">% HR Max</div>
                        </div>
                    </div>
                    
                    <p class="text-muted mb-3">
                        <strong>Huấn luyện viên:</strong> Tự tập<br>
                        <strong>Nội dung:</strong> Chạy bộ và đạp xe
                    </p>
                    
                    <div class="text-end">
                        <button class="btn btn-outline-primary btn-sm">Chi tiết</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Workout Goals -->
        <div class="row">
            <div class="col-12">
                <div class="workout-card">
                    <h5 class="mb-4"><i class="fas fa-target me-2"></i>Mục Tiêu Tập Luyện</h5>
                    
                    <div class="row">
                        <div class="col-md-3 mb-3">
                            <div class="text-center">
                                <h6 class="text-muted">Buổi Tập/Tuần</h6>
                                <div class="progress mb-2">
                                    <div class="progress-bar" role="progressbar" style="width: 71%"></div>
                                </div>
                                <p class="mb-0"><strong>5/7</strong> buổi</p>
                            </div>
                        </div>
                        
                        <div class="col-md-3 mb-3">
                            <div class="text-center">
                                <h6 class="text-muted">Calories/Tuần</h6>
                                <div class="progress mb-2">
                                    <div class="progress-bar" role="progressbar" style="width: 85%"></div>
                                </div>
                                <p class="mb-0"><strong>4,250/5,000</strong> calories</p>
                            </div>
                        </div>
                        
                        <div class="col-md-3 mb-3">
                            <div class="text-center">
                                <h6 class="text-muted">Thời Gian/Tuần</h6>
                                <div class="progress mb-2">
                                    <div class="progress-bar" role="progressbar" style="width: 68%"></div>
                                </div>
                                <p class="mb-0"><strong>4.5/6.5</strong> giờ</p>
                            </div>
                        </div>
                        
                        <div class="col-md-3 mb-3">
                            <div class="text-center">
                                <h6 class="text-muted">Ngày Liên Tiếp</h6>
                                <div class="progress mb-2">
                                    <div class="progress-bar" role="progressbar" style="width: 80%"></div>
                                </div>
                                <p class="mb-0"><strong>12/15</strong> ngày</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Log Workout Modal -->
    <div class="modal fade" id="logWorkoutModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Ghi Nhận Buổi Tập</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form method="post" action="${pageContext.request.contextPath}/member/workout">
                        <input type="hidden" name="action" value="logWorkout">
                        
                        <div class="mb-3">
                            <label for="workoutType" class="form-label">Loại Buổi Tập</label>
                            <select class="form-control" id="workoutType" name="workoutType" required>
                                <option value="">Chọn loại buổi tập</option>
                                <option value="personal">Personal Training</option>
                                <option value="hiit">HIIT Training</option>
                                <option value="yoga">Yoga Class</option>
                                <option value="cardio">Cardio</option>
                                <option value="strength">Strength Training</option>
                                <option value="pilates">Pilates</option>
                                <option value="other">Khác</option>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="duration" class="form-label">Thời Gian (phút)</label>
                            <input type="number" class="form-control" id="duration" name="duration" 
                                   required min="1" max="300">
                        </div>
                        
                        <div class="mb-3">
                            <label for="calories" class="form-label">Calories Đốt</label>
                            <input type="number" class="form-control" id="calories" name="calories" 
                                   required min="1" max="2000">
                        </div>
                        
                        <div class="mb-3">
                            <label for="heartRate" class="form-label">Nhịp Tim Trung Bình (BPM)</label>
                            <input type="number" class="form-control" id="heartRate" name="heartRate" 
                                   min="50" max="200">
                        </div>
                        
                        <div class="mb-3">
                            <label for="notes" class="form-label">Ghi Chú</label>
                            <textarea class="form-control" id="notes" name="notes" rows="3" 
                                      placeholder="Nhập ghi chú về buổi tập..."></textarea>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary" form="logWorkoutForm">Ghi Nhận</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Custom JS -->
    <script>
        // Handle form submission
        document.querySelector('form').addEventListener('submit', function(e) {
            e.preventDefault();
            
            // Show success message
            alert('Ghi nhận buổi tập thành công!');
            
            // TODO: Implement actual form submission
            this.reset();
            $('#logWorkoutModal').modal('hide');
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

        // Handle workout type change
        document.getElementById('workoutType').addEventListener('change', function() {
            const type = this.value;
            let avgCalories = 0;
            let avgDuration = 0;
            
            switch(type) {
                case 'personal':
                    avgCalories = 450;
                    avgDuration = 75;
                    break;
                case 'hiit':
                    avgCalories = 520;
                    avgDuration = 45;
                    break;
                case 'yoga':
                    avgCalories = 180;
                    avgDuration = 60;
                    break;
                case 'cardio':
                    avgCalories = 380;
                    avgDuration = 90;
                    break;
                case 'strength':
                    avgCalories = 350;
                    avgDuration = 60;
                    break;
                case 'pilates':
                    avgCalories = 200;
                    avgDuration = 55;
                    break;
            }
            
            if (avgCalories > 0) {
                document.getElementById('calories').value = avgCalories;
                document.getElementById('duration').value = avgDuration;
            }
        });

        // Tab switching
        document.querySelectorAll('[data-bs-toggle="tab"]').forEach(tab => {
            tab.addEventListener('shown.bs.tab', function() {
                // Animate progress bars when tab is shown
                const activeTab = document.querySelector('.tab-pane.active');
                const progressBars = activeTab.querySelectorAll('.progress-bar');
                progressBars.forEach(bar => {
                    const width = bar.style.width;
                    bar.style.width = '0%';
                    setTimeout(() => {
                        bar.style.width = width;
                    }, 100);
                });
            });
        });
    </script>
</body>
</html>
