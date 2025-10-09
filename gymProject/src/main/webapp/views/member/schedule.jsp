<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lịch Tập - Stamina Gym</title>
    
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

        .schedule-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            margin-bottom: 20px;
            transition: transform 0.3s ease;
        }

        .schedule-card:hover {
            transform: translateY(-5px);
        }

        .time-slot {
            background: linear-gradient(135deg, var(--primary-color), var(--accent-color));
            color: white;
            padding: 15px;
            border-radius: 10px;
            text-align: center;
            margin-bottom: 15px;
        }

        .trainer-info {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
        }

        .trainer-avatar {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            margin-right: 15px;
            object-fit: cover;
        }

        .btn-book {
            background: var(--accent-color);
            border: none;
            color: white;
            padding: 10px 20px;
            border-radius: 25px;
            font-weight: bold;
            transition: all 0.3s ease;
        }

        .btn-book:hover {
            background: #d6734a;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(236, 139, 94, 0.3);
            color: white;
        }

        .btn-book.booked {
            background: #28a745;
        }

        .btn-book.booked:hover {
            background: #218838;
            color: white;
        }

        .calendar-nav {
            background: white;
            padding: 20px;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }

        .calendar-grid {
            display: grid;
            grid-template-columns: repeat(7, 1fr);
            gap: 10px;
        }

        .calendar-day {
            background: white;
            padding: 15px;
            border-radius: 10px;
            text-align: center;
            cursor: pointer;
            transition: all 0.3s ease;
            border: 2px solid transparent;
        }

        .calendar-day:hover {
            border-color: var(--accent-color);
            transform: translateY(-2px);
        }

        .calendar-day.active {
            background: var(--primary-color);
            color: white;
        }

        .calendar-day.has-sessions {
            background: var(--secondary-color);
            color: var(--primary-color);
            font-weight: bold;
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
                        <a class="nav-link active" href="${pageContext.request.contextPath}/member/schedule">
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
                    <h1 class="mb-2"><i class="fas fa-calendar-alt me-2"></i>Lịch Tập</h1>
                    <p class="text-muted mb-0">Đặt lịch và quản lý các buổi tập của bạn</p>
                </div>
                <div class="col-md-4 text-end">
                    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#bookSessionModal">
                        <i class="fas fa-plus me-2"></i>Đặt Lịch Mới
                    </button>
                </div>
            </div>
        </div>

        <!-- Calendar Navigation -->
        <div class="calendar-nav">
            <div class="row align-items-center mb-3">
                <div class="col-md-6">
                    <h5 class="mb-0">
                        <i class="fas fa-calendar-week me-2"></i>
                        Tuần từ 18/11/2024 đến 24/11/2024
                    </h5>
                </div>
                <div class="col-md-6 text-end">
                    <button class="btn btn-outline-primary me-2">
                        <i class="fas fa-chevron-left"></i> Tuần Trước
                    </button>
                    <button class="btn btn-outline-primary">
                        Tuần Sau <i class="fas fa-chevron-right"></i>
                    </button>
                </div>
            </div>
            
            <!-- Calendar Grid -->
            <div class="calendar-grid">
                <div class="calendar-day">
                    <div class="fw-bold">T2</div>
                    <div>18/11</div>
                </div>
                <div class="calendar-day has-sessions">
                    <div class="fw-bold">T3</div>
                    <div>19/11</div>
                </div>
                <div class="calendar-day">
                    <div class="fw-bold">T4</div>
                    <div>20/11</div>
                </div>
                <div class="calendar-day active">
                    <div class="fw-bold">T5</div>
                    <div>21/11</div>
                </div>
                <div class="calendar-day has-sessions">
                    <div class="fw-bold">T6</div>
                    <div>22/11</div>
                </div>
                <div class="calendar-day">
                    <div class="fw-bold">T7</div>
                    <div>23/11</div>
                </div>
                <div class="calendar-day">
                    <div class="fw-bold">CN</div>
                    <div>24/11</div>
                </div>
            </div>
        </div>

        <!-- Schedule List -->
        <div class="row">
            <div class="col-12">
                <h4 class="mb-4"><i class="fas fa-clock me-2"></i>Lịch Tập Hôm Nay - Thứ 5, 21/11/2024</h4>
            </div>
            
            <!-- Morning Sessions -->
            <div class="col-md-6">
                <div class="schedule-card">
                    <div class="time-slot">
                        <i class="fas fa-sun me-2"></i>06:00 - 07:00
                    </div>
                    <div class="trainer-info">
                        <img src="https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?ixlib=rb-4.0.3&auto=format&fit=crop&w=100&q=80" 
                             alt="Trainer" class="trainer-avatar">
                        <div>
                            <h6 class="mb-1">Nguyễn Văn Nam</h6>
                            <small class="text-muted">Personal Training</small>
                        </div>
                    </div>
                    <p class="text-muted mb-3">Buổi tập cá nhân - Cardio & Strength</p>
                    <button class="btn btn-book w-100">
                        <i class="fas fa-check me-2"></i>Đã Đặt Lịch
                    </button>
                </div>
            </div>

            <div class="col-md-6">
                <div class="schedule-card">
                    <div class="time-slot">
                        <i class="fas fa-sun me-2"></i>07:30 - 08:30
                    </div>
                    <div class="trainer-info">
                        <img src="https://images.unsplash.com/photo-1594824388852-890d1c5f3a75?ixlib=rb-4.0.3&auto=format&fit=crop&w=100&q=80" 
                             alt="Trainer" class="trainer-avatar">
                        <div>
                            <h6 class="mb-1">Trần Thị Lan</h6>
                            <small class="text-muted">Yoga Class</small>
                        </div>
                    </div>
                    <p class="text-muted mb-3">Lớp Yoga - Hatha Yoga cho người mới bắt đầu</p>
                    <button class="btn btn-book w-100">
                        <i class="fas fa-plus me-2"></i>Đặt Lịch
                    </button>
                </div>
            </div>

            <!-- Evening Sessions -->
            <div class="col-md-6">
                <div class="schedule-card">
                    <div class="time-slot">
                        <i class="fas fa-moon me-2"></i>18:00 - 19:00
                    </div>
                    <div class="trainer-info">
                        <img src="https://images.unsplash.com/photo-1560250097-0b93528c311a?ixlib=rb-4.0.3&auto=format&fit=crop&w=100&q=80" 
                             alt="Trainer" class="trainer-avatar">
                        <div>
                            <h6 class="mb-1">Lê Hoàng Minh</h6>
                            <small class="text-muted">HIIT Training</small>
                        </div>
                    </div>
                    <p class="text-muted mb-3">Lớp HIIT - Cường độ cao đốt cháy mỡ thừa</p>
                    <button class="btn btn-book w-100">
                        <i class="fas fa-plus me-2"></i>Đặt Lịch
                    </button>
                </div>
            </div>

            <div class="col-md-6">
                <div class="schedule-card">
                    <div class="time-slot">
                        <i class="fas fa-moon me-2"></i>19:30 - 20:30
                    </div>
                    <div class="trainer-info">
                        <img src="https://images.unsplash.com/photo-1494790108755-2616b612b786?ixlib=rb-4.0.3&auto=format&fit=crop&w=100&q=80" 
                             alt="Trainer" class="trainer-avatar">
                        <div>
                            <h6 class="mb-1">Phạm Thu Hà</h6>
                            <small class="text-muted">Pilates</small>
                        </div>
                    </div>
                    <p class="text-muted mb-3">Lớp Pilates - Tăng cường sức mạnh cốt lõi</p>
                    <button class="btn btn-book w-100">
                        <i class="fas fa-plus me-2"></i>Đặt Lịch
                    </button>
                </div>
            </div>
        </div>

        <!-- Upcoming Sessions -->
        <div class="row mt-5">
            <div class="col-12">
                <h4 class="mb-4"><i class="fas fa-calendar-check me-2"></i>Các Buổi Tập Sắp Tới</h4>
            </div>
            
            <div class="col-md-4">
                <div class="schedule-card">
                    <div class="time-slot">
                        <i class="fas fa-clock me-2"></i>Thứ 6, 22/11
                    </div>
                    <div class="trainer-info">
                        <img src="https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?ixlib=rb-4.0.3&auto=format&fit=crop&w=100&q=80" 
                             alt="Trainer" class="trainer-avatar">
                        <div>
                            <h6 class="mb-1">Nguyễn Văn Nam</h6>
                            <small class="text-muted">07:00 - 08:00</small>
                        </div>
                    </div>
                    <p class="text-muted mb-3">Personal Training - Upper Body</p>
                    <button class="btn btn-outline-danger w-100">
                        <i class="fas fa-times me-2"></i>Hủy Lịch
                    </button>
                </div>
            </div>

            <div class="col-md-4">
                <div class="schedule-card">
                    <div class="time-slot">
                        <i class="fas fa-clock me-2"></i>Chủ Nhật, 24/11
                    </div>
                    <div class="trainer-info">
                        <img src="https://images.unsplash.com/photo-1594824388852-890d1c5f3a75?ixlib=rb-4.0.3&auto=format&fit=crop&w=100&q=80" 
                             alt="Trainer" class="trainer-avatar">
                        <div>
                            <h6 class="mb-1">Trần Thị Lan</h6>
                            <small class="text-muted">09:00 - 10:00</small>
                        </div>
                    </div>
                    <p class="text-muted mb-3">Yoga Class - Vinyasa Flow</p>
                    <button class="btn btn-outline-danger w-100">
                        <i class="fas fa-times me-2"></i>Hủy Lịch
                    </button>
                </div>
            </div>

            <div class="col-md-4">
                <div class="schedule-card">
                    <div class="time-slot">
                        <i class="fas fa-clock me-2"></i>Thứ 2, 25/11
                    </div>
                    <div class="trainer-info">
                        <img src="https://images.unsplash.com/photo-1560250097-0b93528c311a?ixlib=rb-4.0.3&auto=format&fit=crop&w=100&q=80" 
                             alt="Trainer" class="trainer-avatar">
                        <div>
                            <h6 class="mb-1">Lê Hoàng Minh</h6>
                            <small class="text-muted">18:30 - 19:30</small>
                        </div>
                    </div>
                    <p class="text-muted mb-3">HIIT Training - Full Body</p>
                    <button class="btn btn-outline-danger w-100">
                        <i class="fas fa-times me-2"></i>Hủy Lịch
                    </button>
                </div>
            </div>
        </div>
    </div>

    <!-- Book Session Modal -->
    <div class="modal fade" id="bookSessionModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Đặt Lịch Tập</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form method="post" action="${pageContext.request.contextPath}/member/schedule">
                        <input type="hidden" name="action" value="book">
                        
                        <div class="mb-3">
                            <label for="sessionType" class="form-label">Loại Buổi Tập</label>
                            <select class="form-control" id="sessionType" name="sessionType" required>
                                <option value="">Chọn loại buổi tập</option>
                                <option value="personal">Personal Training</option>
                                <option value="yoga">Yoga Class</option>
                                <option value="hiit">HIIT Training</option>
                                <option value="pilates">Pilates</option>
                                <option value="cardio">Cardio</option>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="trainer" class="form-label">Huấn Luyện Viên</label>
                            <select class="form-control" id="trainer" name="trainer" required>
                                <option value="">Chọn huấn luyện viên</option>
                                <option value="1">Nguyễn Văn Nam</option>
                                <option value="2">Trần Thị Lan</option>
                                <option value="3">Lê Hoàng Minh</option>
                                <option value="4">Phạm Thu Hà</option>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="date" class="form-label">Ngày</label>
                            <input type="date" class="form-control" id="date" name="date" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="time" class="form-label">Giờ</label>
                            <select class="form-control" id="time" name="time" required>
                                <option value="">Chọn giờ</option>
                                <option value="06:00">06:00 - 07:00</option>
                                <option value="07:30">07:30 - 08:30</option>
                                <option value="18:00">18:00 - 19:00</option>
                                <option value="19:30">19:30 - 20:30</option>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="notes" class="form-label">Ghi Chú (Tùy chọn)</label>
                            <textarea class="form-control" id="notes" name="notes" rows="3" 
                                      placeholder="Nhập ghi chú về buổi tập..."></textarea>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary" form="bookSessionForm">Đặt Lịch</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Custom JS -->
    <script>
        // Handle calendar day clicks
        document.querySelectorAll('.calendar-day').forEach(day => {
            day.addEventListener('click', function() {
                document.querySelectorAll('.calendar-day').forEach(d => d.classList.remove('active'));
                this.classList.add('active');
                
                // TODO: Load schedule for selected day
                console.log('Selected day:', this.textContent);
            });
        });

        // Handle session booking
        document.querySelectorAll('.btn-book').forEach(btn => {
            btn.addEventListener('click', function() {
                if (this.classList.contains('booked')) {
                    // Cancel booking
                    if (confirm('Bạn có chắc chắn muốn hủy buổi tập này?')) {
                        // TODO: Implement cancel booking
                        this.innerHTML = '<i class="fas fa-plus me-2"></i>Đặt Lịch';
                        this.classList.remove('booked');
                        this.classList.add('btn-book');
                    }
                } else {
                    // Book session
                    // TODO: Implement booking logic
                    this.innerHTML = '<i class="fas fa-check me-2"></i>Đã Đặt Lịch';
                    this.classList.add('booked');
                }
            });
        });

        // Set today's date in the modal
        document.getElementById('date').valueAsDate = new Date();
    </script>
</body>
</html>
