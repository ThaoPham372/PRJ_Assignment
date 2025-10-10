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

    .workout-card {
        background: var(--card);
        border-radius: 15px;
        box-shadow: 0 8px 25px var(--shadow);
        padding: 30px;
        margin-bottom: 30px;
        transition: all 0.3s ease;
    }

    .workout-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 12px 35px var(--shadow-hover);
    }

    .exercise-item {
        background: var(--muted);
        border-radius: 10px;
        padding: 20px;
        margin-bottom: 15px;
        border-left: 4px solid var(--accent);
        transition: all 0.3s ease;
    }

    .exercise-item:hover {
        background: var(--card);
        box-shadow: 0 4px 15px var(--shadow);
    }

    .btn-workout {
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

    .btn-workout:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(236, 139, 94, 0.4);
        color: white;
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

    .workout-title {
        color: var(--text);
        font-weight: 800;
        margin-bottom: 30px;
        text-transform: uppercase;
        letter-spacing: 1px;
    }

    .timer-display {
        font-size: 3rem;
        font-weight: 900;
        color: var(--accent);
        text-align: center;
        margin: 20px 0;
        }

        .progress-bar {
        background: var(--gradient-accent);
            border-radius: 10px;
        }

        .progress {
            height: 10px;
            border-radius: 10px;
        background: var(--muted);
        }
    </style>

<div class="container mt-5">
    <!-- Workout Header -->
    <div class="workout-card text-center">
        <h2 class="workout-title">Buổi Tập Hôm Nay</h2>
        <p class="text-muted mb-4">Theo dõi và ghi nhận buổi tập của bạn</p>
        
        <!-- Timer -->
        <div class="timer-display" id="timer">00:00:00</div>
        <div class="d-flex justify-content-center gap-3 mb-4">
            <button class="btn-workout" id="startBtn">
                <i class="fas fa-play me-2"></i>Bắt Đầu
            </button>
            <button class="btn-workout" id="pauseBtn" disabled>
                <i class="fas fa-pause me-2"></i>Tạm Dừng
            </button>
            <button class="btn-workout" id="stopBtn" disabled>
                <i class="fas fa-stop me-2"></i>Kết Thúc
                    </button>
            </div>
        </div>

    <!-- Workout Plan -->
    <div class="row">
        <div class="col-md-8">
            <div class="workout-card">
                <h4 class="workout-title">Kế Hoạch Tập Luyện</h4>
                
                <!-- Exercise 1 -->
                <div class="exercise-item">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h5 class="mb-1">Push-ups</h5>
                            <p class="text-muted mb-0">3 sets x 15 reps</p>
                        </div>
                        <div class="d-flex gap-2">
                            <button class="btn btn-sm btn-outline-success">✓</button>
                            <button class="btn btn-sm btn-outline-primary">Edit</button>
                        </div>
                    </div>
                    <div class="progress mt-2">
                        <div class="progress-bar" style="width: 0%"></div>
            </div>
        </div>

                <!-- Exercise 2 -->
                <div class="exercise-item">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h5 class="mb-1">Squats</h5>
                            <p class="text-muted mb-0">3 sets x 20 reps</p>
                        </div>
                        <div class="d-flex gap-2">
                            <button class="btn btn-sm btn-outline-success">✓</button>
                            <button class="btn btn-sm btn-outline-primary">Edit</button>
                        </div>
                    </div>
                    <div class="progress mt-2">
                        <div class="progress-bar" style="width: 0%"></div>
                </div>
            </div>
            
                <!-- Exercise 3 -->
                <div class="exercise-item">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h5 class="mb-1">Plank</h5>
                            <p class="text-muted mb-0">3 sets x 60 seconds</p>
                        </div>
                        <div class="d-flex gap-2">
                            <button class="btn btn-sm btn-outline-success">✓</button>
                            <button class="btn btn-sm btn-outline-primary">Edit</button>
                        </div>
                    </div>
                    <div class="progress mt-2">
                        <div class="progress-bar" style="width: 0%"></div>
                    </div>
                </div>

                <!-- Add Exercise Button -->
                <div class="text-center mt-4">
                    <button class="btn-workout">
                        <i class="fas fa-plus me-2"></i>Thêm Bài Tập
                    </button>
                </div>
                        </div>
                    </div>
                    
        <div class="col-md-4">
            <!-- Workout Stats -->
            <div class="workout-card">
                <h5 class="workout-title">Thống Kê</h5>
                <div class="row text-center">
                    <div class="col-6 mb-3">
                        <div class="border-end">
                            <h4 class="mb-1" style="color: var(--accent);">45</h4>
                            <small class="text-muted">Phút</small>
                        </div>
                    </div>
                    <div class="col-6 mb-3">
                        <h4 class="mb-1" style="color: var(--accent);">320</h4>
                        <small class="text-muted">Calories</small>
                    </div>
                </div>
                <div class="row text-center">
                    <div class="col-6 mb-3">
                        <div class="border-end">
                            <h4 class="mb-1" style="color: var(--accent);">3</h4>
                            <small class="text-muted">Bài Tập</small>
                        </div>
                    </div>
                    <div class="col-6 mb-3">
                        <h4 class="mb-1" style="color: var(--accent);">9</h4>
                        <small class="text-muted">Sets</small>
                </div>
            </div>
        </div>

            <!-- Quick Actions -->
                <div class="workout-card">
                <h5 class="workout-title">Thao Tác Nhanh</h5>
                <div class="d-grid gap-2">
                    <button class="btn-workout">
                        <i class="fas fa-history me-2"></i>Lịch Sử Tập
                    </button>
                    <button class="btn-workout">
                        <i class="fas fa-chart-line me-2"></i>Tiến Độ
                    </button>
                    <button class="btn-workout">
                        <i class="fas fa-download me-2"></i>Xuất Báo Cáo
                    </button>
                </div>
            </div>
        </div>
    </div>
                        </div>
                        
<script>
    let timer = 0;
    let isRunning = false;
    let interval;

    const timerDisplay = document.getElementById('timer');
    const startBtn = document.getElementById('startBtn');
    const pauseBtn = document.getElementById('pauseBtn');
    const stopBtn = document.getElementById('stopBtn');

    function formatTime(seconds) {
        const hrs = Math.floor(seconds / 3600);
        const mins = Math.floor((seconds % 3600) / 60);
        const secs = seconds % 60;
        return `${hrs.toString().padStart(2, '0')}:${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
    }

    function updateTimer() {
        timer++;
        timerDisplay.textContent = formatTime(timer);
    }

    startBtn.addEventListener('click', () => {
        isRunning = true;
        interval = setInterval(updateTimer, 1000);
        startBtn.disabled = true;
        pauseBtn.disabled = false;
        stopBtn.disabled = false;
    });

    pauseBtn.addEventListener('click', () => {
        isRunning = false;
        clearInterval(interval);
        startBtn.disabled = false;
        pauseBtn.disabled = true;
    });

    stopBtn.addEventListener('click', () => {
        isRunning = false;
        clearInterval(interval);
        timer = 0;
        timerDisplay.textContent = formatTime(timer);
        startBtn.disabled = false;
        pauseBtn.disabled = true;
        stopBtn.disabled = true;
    });

    // Exercise completion
    document.querySelectorAll('.exercise-item .btn-outline-success').forEach(btn => {
        btn.addEventListener('click', function() {
            const progressBar = this.closest('.exercise-item').querySelector('.progress-bar');
            const currentWidth = parseInt(progressBar.style.width) || 0;
            const newWidth = Math.min(currentWidth + 33.33, 100);
            progressBar.style.width = newWidth + '%';
            
            if (newWidth >= 100) {
                this.classList.remove('btn-outline-success');
                this.classList.add('btn-success');
                this.innerHTML = '✓';
            }
            });
        });
    </script>

<%@ include file="/views/common/footer.jsp" %>