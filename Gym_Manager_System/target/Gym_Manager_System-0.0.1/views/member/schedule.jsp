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

    .schedule-card {
        background: var(--card);
        border-radius: 15px;
        box-shadow: 0 8px 25px var(--shadow);
        padding: 30px;
        margin-bottom: 30px;
        transition: all 0.3s ease;
    }

    .schedule-card:hover {
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

    .schedule-title {
        color: var(--text);
        font-weight: 800;
        margin-bottom: 30px;
        text-transform: uppercase;
        letter-spacing: 1px;
        }

        .calendar-day {
        background: var(--muted);
        border-radius: 10px;
            padding: 15px;
        margin-bottom: 15px;
        border-left: 4px solid var(--accent);
    }

    .btn-schedule {
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

    .btn-schedule:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(236, 139, 94, 0.4);
            color: white;
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

    <!-- Schedule Header -->
    <div class="schedule-card text-center">
        <h2 class="schedule-title">Lịch Tập Của Tôi</h2>
        <p class="text-muted mb-4">Quản lý và đặt lịch tập luyện</p>
        <button class="btn-schedule">
                        <i class="fas fa-plus me-2"></i>Đặt Lịch Mới
                    </button>
        </div>

    <!-- Calendar View -->
    <div class="row">
        <div class="col-md-8">
            <div class="schedule-card">
                <h4 class="schedule-title">Tuần Này</h4>
                
                <!-- Monday -->
                <div class="calendar-day">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h5 class="mb-1">Thứ 2 - 15/01/2024</h5>
                            <p class="text-muted mb-0">Cardio & Strength Training</p>
                        </div>
                        <div class="d-flex gap-2">
                            <span class="badge bg-success">09:00 - 10:30</span>
                            <button class="btn btn-sm btn-outline-primary">Edit</button>
                        </div>
                </div>
            </div>

                <!-- Wednesday -->
                <div class="calendar-day">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h5 class="mb-1">Thứ 4 - 17/01/2024</h5>
                            <p class="text-muted mb-0">Yoga & Flexibility</p>
                        </div>
                        <div class="d-flex gap-2">
                            <span class="badge bg-info">18:00 - 19:00</span>
                            <button class="btn btn-sm btn-outline-primary">Edit</button>
                        </div>
                </div>
            </div>

                <!-- Friday -->
                <div class="calendar-day">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h5 class="mb-1">Thứ 6 - 19/01/2024</h5>
                            <p class="text-muted mb-0">HIIT Training</p>
                        </div>
                        <div class="d-flex gap-2">
                            <span class="badge bg-warning">07:00 - 08:00</span>
                            <button class="btn btn-sm btn-outline-primary">Edit</button>
                        </div>
                </div>
            </div>

                <!-- Sunday -->
                <div class="calendar-day">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h5 class="mb-1">Chủ Nhật - 21/01/2024</h5>
                            <p class="text-muted mb-0">Rest Day</p>
                        </div>
                        <div class="d-flex gap-2">
                            <span class="badge bg-secondary">Nghỉ ngơi</span>
                            <button class="btn btn-sm btn-outline-primary">Edit</button>
                        </div>
                    </div>
                </div>
            </div>
            </div>
            
            <div class="col-md-4">
            <!-- Quick Stats -->
                <div class="schedule-card">
                <h5 class="schedule-title">Thống Kê</h5>
                <div class="row text-center">
                    <div class="col-6 mb-3">
                        <div class="border-end">
                            <h4 class="mb-1" style="color: var(--accent);">4</h4>
                            <small class="text-muted">Buổi/Tuần</small>
                        </div>
                    </div>
                    <div class="col-6 mb-3">
                        <h4 class="mb-1" style="color: var(--accent);">12</h4>
                        <small class="text-muted">Giờ/Tuần</small>
                    </div>
                </div>
                <div class="row text-center">
                    <div class="col-6 mb-3">
                        <div class="border-end">
                            <h4 class="mb-1" style="color: var(--accent);">85%</h4>
                            <small class="text-muted">Hoàn Thành</small>
                        </div>
                    </div>
                    <div class="col-6 mb-3">
                        <h4 class="mb-1" style="color: var(--accent);">16</h4>
                        <small class="text-muted">Ngày Liên Tiếp</small>
                    </div>
                </div>
            </div>

            <!-- Available Classes -->
                <div class="schedule-card">
                <h5 class="schedule-title">Lớp Học Có Sẵn</h5>
                <div class="d-grid gap-2">
                    <button class="btn-schedule">
                        <i class="fas fa-dumbbell me-2"></i>Strength Training
                    </button>
                    <button class="btn-schedule">
                        <i class="fas fa-heart me-2"></i>Cardio Blast
                    </button>
                    <button class="btn-schedule">
                        <i class="fas fa-leaf me-2"></i>Yoga Flow
                    </button>
                    <button class="btn-schedule">
                        <i class="fas fa-fire me-2"></i>HIIT Workout
                    </button>
                </div>
                </div>
            </div>
        </div>
    </div>

<%@ include file="/views/common/footer.jsp" %>