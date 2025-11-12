<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/views/common/header.jsp" %>

<style>
:root {
    --primary: #141a46;
    --primary-light: #1e2a5c;
    --accent: #ec8b5e;
    --accent-hover: #d67a4f;
    --yellow: #ffde59;
    --yellow-dark: #f5d040;
    --text: #2c3e50;
    --text-light: #5a6c7d;
    --muted: #f8f9fa;
    --card: #ffffff;
    --shadow: rgba(0, 0, 0, 0.1);
    --shadow-hover: rgba(0, 0, 0, 0.15);
    --gradient-primary: linear-gradient(135deg, #141a46 0%, #1e2a5c 100%);
    --gradient-accent: linear-gradient(135deg, #ec8b5e 0%, #d67a4f 100%);
    --success: #28a745;
    --info: #17a2b8;
    --warning: #ffc107;
    --danger: #dc3545;
}

body {
    background: linear-gradient(135deg, #f5f7fa 0%, #e9ecef 100%);
    min-height: 100vh;
}

.schedule-container {
    max-width: 1400px;
    margin: 0 auto;
    padding: 30px 15px;
}

.page-header {
    background: var(--gradient-primary);
    color: white;
    border-radius: 20px;
    padding: 40px;
    margin-bottom: 30px;
    box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
    position: relative;
    overflow: hidden;
}

.page-header::before {
    content: '';
    position: absolute;
    top: -50%;
    right: -10%;
    width: 400px;
    height: 400px;
    background: radial-gradient(circle, rgba(236, 139, 94, 0.2) 0%, transparent 70%);
    border-radius: 50%;
}

.page-header-content {
    position: relative;
    z-index: 1;
}

.page-title {
    font-size: 2.5rem;
    font-weight: 900;
    margin-bottom: 10px;
    text-transform: uppercase;
    letter-spacing: 1px;
}

.page-subtitle {
    font-size: 1.1rem;
    opacity: 0.9;
}

.step-indicator {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-bottom: 40px;
    flex-wrap: wrap;
    gap: 10px;
}

.step {
    display: flex;
    align-items: center;
    gap: 10px;
}

.step-number {
    width: 45px;
    height: 45px;
    border-radius: 50%;
    background: #e9ecef;
    color: var(--text-light);
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: 900;
    font-size: 1.1rem;
    transition: all 0.3s ease;
    border: 3px solid transparent;
}

.step.active .step-number {
    background: var(--gradient-accent);
    color: white;
    border-color: var(--accent);
    box-shadow: 0 4px 15px rgba(236, 139, 94, 0.4);
}

.step.completed .step-number {
    background: var(--success);
    color: white;
}

.step-label {
    font-weight: 700;
    color: var(--text-light);
    font-size: 0.95rem;
}

.step.active .step-label {
    color: var(--accent);
}

.step.completed .step-label {
    color: var(--success);
}

.step-arrow {
    font-size: 1.2rem;
    color: #dee2e6;
    margin: 0 5px;
}

.booking-card {
    background: white;
    border-radius: 20px;
    padding: 35px;
    margin-bottom: 25px;
    box-shadow: 0 4px 20px var(--shadow);
    transition: all 0.3s ease;
}

.booking-card:hover {
    box-shadow: 0 8px 30px var(--shadow-hover);
}

.section-title {
    font-size: 1.4rem;
    font-weight: 800;
    color: var(--primary);
    margin-bottom: 25px;
    padding-bottom: 15px;
    border-bottom: 3px solid var(--accent);
    display: flex;
    align-items: center;
    gap: 10px;
}

.section-title i {
    color: var(--accent);
}

.gym-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
    gap: 20px;
}

.gym-card {
    background: white;
    border: 3px solid #e9ecef;
    border-radius: 15px;
    padding: 25px;
    cursor: pointer;
    transition: all 0.3s ease;
    text-align: center;
}

.gym-card:hover {
    border-color: var(--accent);
    transform: translateY(-5px);
    box-shadow: 0 8px 25px rgba(236, 139, 94, 0.2);
}

.gym-card.selected {
    border-color: var(--accent);
    background: linear-gradient(135deg, #fff5f0 0%, #ffe8e0 100%);
    box-shadow: 0 8px 25px rgba(236, 139, 94, 0.3);
}

.gym-icon {
    width: 80px;
    height: 80px;
    margin: 0 auto 15px;
    background: var(--gradient-accent);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 2.5rem;
    color: white;
}

.gym-name {
    font-size: 1.1rem;
    font-weight: 700;
    color: var(--primary);
    margin-bottom: 8px;
}

.gym-address {
    font-size: 0.9rem;
    color: var(--text-light);
}

.trainer-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
    gap: 20px;
}

.trainer-card {
    background: white;
    border: 3px solid #e9ecef;
    border-radius: 15px;
    padding: 20px;
    cursor: pointer;
    transition: all 0.3s ease;
    text-align: center;
}

.trainer-card:hover {
    border-color: var(--accent);
    transform: translateY(-5px);
    box-shadow: 0 8px 25px rgba(236, 139, 94, 0.2);
}

.trainer-card.selected {
    border-color: var(--accent);
    background: linear-gradient(135deg, #fff5f0 0%, #ffe8e0 100%);
}

.trainer-avatar {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    margin: 0 auto 15px;
    border: 4px solid var(--accent);
    overflow: hidden;
    background: #e9ecef;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 2.5rem;
    color: var(--text-light);
}

.trainer-avatar img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.trainer-name {
    font-size: 1.1rem;
    font-weight: 700;
    color: var(--primary);
    margin-bottom: 5px;
}

.trainer-specialty {
    font-size: 0.85rem;
    color: var(--text-light);
    margin-bottom: 10px;
}

.trainer-rating {
    color: #ffc107;
    font-size: 0.9rem;
}

.calendar-container {
    background: white;
    border-radius: 15px;
    padding: 20px;
    border: 2px solid #e9ecef;
}

.calendar-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}

.calendar-title {
    font-size: 1.2rem;
    font-weight: 700;
    color: var(--primary);
}

.calendar-nav {
    display: flex;
    gap: 10px;
}

.calendar-btn {
    background: var(--gradient-accent);
    color: white;
    border: none;
    border-radius: 8px;
    padding: 8px 15px;
    cursor: pointer;
    font-weight: 600;
    transition: all 0.3s ease;
}

.calendar-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(236, 139, 94, 0.3);
}

.calendar-grid {
    display: grid;
    grid-template-columns: repeat(7, 1fr);
    gap: 10px;
}

.calendar-day-header {
    text-align: center;
    font-weight: 700;
    color: var(--primary);
    padding: 10px;
    font-size: 0.9rem;
}

.calendar-day {
    aspect-ratio: 1;
    border: 2px solid #e9ecef;
    border-radius: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all 0.3s ease;
    font-weight: 600;
    color: var(--text);
    background: white;
}

.calendar-day:hover {
    border-color: var(--accent);
    background: #fff5f0;
}

.calendar-day.disabled {
    background: #f8f9fa;
    color: #dee2e6;
    cursor: not-allowed;
}

.calendar-day.selected {
    background: var(--gradient-accent);
    color: white;
    border-color: var(--accent);
}

.calendar-day.today {
    border-color: var(--info);
    font-weight: 900;
}

.timeslot-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
    gap: 15px;
}

.timeslot-card {
    background: white;
    border: 3px solid #e9ecef;
    border-radius: 12px;
    padding: 15px;
    text-align: center;
    cursor: pointer;
    transition: all 0.3s ease;
}

.timeslot-card:hover {
    border-color: var(--accent);
    transform: translateY(-3px);
    box-shadow: 0 4px 15px rgba(236, 139, 94, 0.2);
}

.timeslot-card.selected {
    border-color: var(--accent);
    background: linear-gradient(135deg, #fff5f0 0%, #ffe8e0 100%);
}

.timeslot-card.unavailable {
    background: #f8f9fa;
    border-color: #dee2e6;
    cursor: not-allowed;
    opacity: 0.6;
}

.timeslot-time {
    font-size: 1.1rem;
    font-weight: 700;
    color: var(--primary);
    margin-bottom: 5px;
}

.timeslot-status {
    font-size: 0.8rem;
    color: var(--success);
    font-weight: 600;
}

.timeslot-card.unavailable .timeslot-status {
    color: var(--danger);
}

.booking-summary {
    background: linear-gradient(135deg, #fff5f0 0%, #ffe8e0 100%);
    border-radius: 15px;
    padding: 25px;
    border: 2px solid var(--accent);
}

.summary-item {
    display: flex;
    justify-content: space-between;
    padding: 12px 0;
    border-bottom: 1px solid rgba(236, 139, 94, 0.2);
}

.summary-item:last-child {
    border-bottom: none;
}

.summary-label {
    font-weight: 600;
    color: var(--text-light);
}

.summary-value {
    font-weight: 700;
    color: var(--primary);
}

/* Booking Status Styles - Different colors for each status */
.booking-status {
    padding: 8px 20px;
    border-radius: 20px;
    font-weight: 700;
    font-size: 0.9rem;
    display: inline-block;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

/* Chờ Xác Nhận - Màu vàng */
.status-PENDING {
    background: var(--yellow);
    color: var(--primary);
    border: 2px solid var(--yellow-dark);
    box-shadow: 0 2px 8px rgba(255, 222, 89, 0.3);
}

/* Đã Xác Nhận - Màu xanh lá */
.status-CONFIRMED {
    background: #d4edda;
    color: #155724;
    border: 2px solid #28a745;
    box-shadow: 0 2px 8px rgba(40, 167, 69, 0.3);
}

/* Hoàn Thành - Màu xanh dương */
.status-COMPLETED {
    background: #d1ecf1;
    color: #0c5460;
    border: 2px solid #17a2b8;
    box-shadow: 0 2px 8px rgba(23, 162, 184, 0.3);
}

/* Đã Hủy - Màu đỏ nhạt */
.status-CANCELLED {
    background: #f8d7da;
    color: #721c24;
    border: 2px solid #dc3545;
    box-shadow: 0 2px 8px rgba(220, 53, 69, 0.3);
}

.booking-notes {
    width: 100%;
    border: 2px solid #e9ecef;
    border-radius: 12px;
    padding: 15px;
    font-size: 1rem;
    resize: vertical;
    min-height: 100px;
    transition: all 0.3s ease;
}

.booking-notes:focus {
    outline: none;
    border-color: var(--accent);
    box-shadow: 0 0 0 3px rgba(236, 139, 94, 0.1);
}

.btn-primary {
    background: var(--gradient-accent);
    color: white;
    border: none;
    border-radius: 25px;
    padding: 15px 35px;
    font-weight: 700;
    font-size: 1.1rem;
    cursor: pointer;
    transition: all 0.3s ease;
    box-shadow: 0 4px 15px rgba(236, 139, 94, 0.3);
    text-transform: uppercase;
    letter-spacing: 0.5px;
}

.btn-primary:hover:not(:disabled) {
    transform: translateY(-3px);
    box-shadow: 0 8px 25px rgba(236, 139, 94, 0.4);
}

.btn-primary:disabled {
    opacity: 0.5;
    cursor: not-allowed;
}

.btn-secondary {
    background: white;
    color: var(--accent);
    border: 2px solid var(--accent);
    border-radius: 25px;
    padding: 15px 35px;
    font-weight: 700;
    font-size: 1.1rem;
    cursor: pointer;
    transition: all 0.3s ease;
    text-transform: uppercase;
    letter-spacing: 0.5px;
}

.btn-secondary:hover {
    background: var(--accent);
    color: white;
    transform: translateY(-3px);
}

.btn-back {
    background: var(--yellow);
    color: var(--primary);
    border: 2px solid var(--yellow);
    border-radius: 25px;
    padding: 15px 35px;
    font-weight: 700;
    font-size: 1.1rem;
    cursor: pointer;
    transition: all 0.3s ease;
    text-transform: uppercase;
    letter-spacing: 0.5px;
    box-shadow: 0 4px 15px rgba(255, 222, 89, 0.3);
}

.btn-back:hover {
    background: var(--yellow-dark);
    border-color: var(--yellow-dark);
    transform: translateY(-3px);
    box-shadow: 0 8px 25px rgba(255, 222, 89, 0.4);
}

.action-buttons {
    display: flex;
    gap: 15px;
    justify-content: flex-end;
    margin-top: 25px;
}

.alert {
    padding: 15px 20px;
    border-radius: 12px;
    margin-bottom: 20px;
    font-weight: 600;
    display: flex;
    align-items: center;
    gap: 10px;
}

.alert-success {
    background: #d4edda;
    color: #155724;
    border: 2px solid #c3e6cb;
}

.alert-danger {
    background: #f8d7da;
    color: #721c24;
    border: 2px solid #f5c6cb;
}

.alert-warning {
    background: #fff3cd;
    color: #856404;
    border: 2px solid #ffeaa7;
}

.hidden {
    display: none !important;
}

@media (max-width: 768px) {
    .gym-grid,
    .trainer-grid {
        grid-template-columns: 1fr;
    }
    
    .calendar-grid {
        grid-template-columns: repeat(7, 1fr);
        gap: 5px;
    }
    
    .calendar-day {
        font-size: 0.85rem;
    }
    
    .timeslot-grid {
        grid-template-columns: repeat(2, 1fr);
    }
    
    .action-buttons {
        flex-direction: column;
    }
    
    .btn-primary,
    .btn-secondary {
        width: 100%;
    }
}
</style>

<div class="schedule-container">
    <!-- Page Header -->
    <div class="page-header">
        <div class="page-header-content text-center">
            <h1 class="page-title">
                <i class="fas fa-calendar-check"></i> Đặt Lịch PT
            </h1>
            <p class="page-subtitle">Đặt lịch tập với huấn luyện viên cá nhân của bạn</p>
        </div>
    </div>

    <!-- Step Indicator -->
    <div class="step-indicator">
        <div class="step active" id="step1-indicator">
            <div class="step-number">1</div>
            <div class="step-label">Chọn Cơ Sở</div>
        </div>
        <div class="step-arrow">→</div>
        <div class="step" id="step2-indicator">
            <div class="step-number">2</div>
            <div class="step-label">Chọn PT</div>
        </div>
        <div class="step-arrow">→</div>
        <div class="step" id="step3-indicator">
            <div class="step-number">3</div>
            <div class="step-label">Chọn Ngày</div>
        </div>
        <div class="step-arrow">→</div>
        <div class="step" id="step4-indicator">
            <div class="step-number">4</div>
            <div class="step-label">Chọn Giờ</div>
        </div>
        <div class="step-arrow">→</div>
        <div class="step" id="step5-indicator">
            <div class="step-number">5</div>
            <div class="step-label">Xác Nhận</div>
        </div>
    </div>

    <!-- Alerts -->
    <div id="alertContainer"></div>

    <!-- Step 1: Choose Gym -->
    <div class="booking-card" id="step1">
        <h3 class="section-title">
            <i class="fas fa-building"></i>
            Bước 1: Chọn Cơ Sở Tập
        </h3>
        <div class="gym-grid" id="gymGrid">
            <!-- Gyms will be loaded here -->
        </div>
        <div class="action-buttons" style="justify-content: flex-end; margin-top: 20px;">
            <!-- No back button for step 1 -->
        </div>
    </div>

    <!-- Step 2: Choose Trainer -->
    <div class="booking-card hidden" id="step2">
        <h3 class="section-title">
            <i class="fas fa-user-tie"></i>
            Bước 2: Chọn Huấn Luyện Viên
        </h3>
        <div class="trainer-grid" id="trainerGrid">
            <!-- Trainers will be loaded here -->
        </div>
        <div class="action-buttons" style="justify-content: flex-end; margin-top: 20px;">
            <button class="btn-back" onclick="previousStep()">
                <i class="fas fa-arrow-left"></i> Quay Lại
            </button>
        </div>
    </div>

    <!-- Step 3: Choose Date -->
    <div class="booking-card hidden" id="step3">
        <h3 class="section-title">
            <i class="fas fa-calendar-alt"></i>
            Bước 3: Chọn Ngày Tập
        </h3>
        <div class="calendar-container">
            <div class="calendar-header">
                <h4 class="calendar-title" id="calendarMonth">Tháng 11, 2025</h4>
                <div class="calendar-nav">
                    <button class="calendar-btn" onclick="previousMonth()">
                        <i class="fas fa-chevron-left"></i>
                    </button>
                    <button class="calendar-btn" onclick="nextMonth()">
                        <i class="fas fa-chevron-right"></i>
                    </button>
                </div>
            </div>
            <div class="calendar-grid" id="calendarGrid">
                <!-- Calendar will be generated here -->
            </div>
        </div>
        <div class="action-buttons" style="justify-content: flex-end; margin-top: 20px;">
            <button class="btn-back" onclick="previousStep()">
                <i class="fas fa-arrow-left"></i> Quay Lại
            </button>
        </div>
    </div>

    <!-- Step 4: Choose Time Slot -->
    <div class="booking-card hidden" id="step4">
        <h3 class="section-title">
            <i class="fas fa-clock"></i>
            Bước 4: Chọn Khung Giờ
        </h3>
        <div class="timeslot-grid" id="timeslotGrid">
            <!-- Time slots will be loaded here -->
        </div>
        <div class="action-buttons" style="justify-content: flex-end; margin-top: 20px;">
            <button class="btn-back" onclick="previousStep()">
                <i class="fas fa-arrow-left"></i> Quay Lại
            </button>
        </div>
    </div>

    <!-- Step 5: Confirm Booking -->
    <div class="booking-card hidden" id="step5">
        <h3 class="section-title">
            <i class="fas fa-check-circle"></i>
            Bước 5: Xác Nhận Đặt Lịch
        </h3>
        
        <div class="row">
            <div class="col-md-6">
                <div class="booking-summary">
                    <h5 style="font-weight: 800; color: var(--primary); margin-bottom: 20px;">
                        <i class="fas fa-info-circle"></i> Thông Tin Đặt Lịch
                    </h5>
                    <div class="summary-item">
                        <span class="summary-label">Cơ sở:</span>
                        <span class="summary-value" id="summaryGym">-</span>
                    </div>
                    <div class="summary-item">
                        <span class="summary-label">Huấn luyện viên:</span>
                        <span class="summary-value" id="summaryTrainer">-</span>
                    </div>
                    <div class="summary-item">
                        <span class="summary-label">Ngày tập:</span>
                        <span class="summary-value" id="summaryDate">-</span>
                    </div>
                    <div class="summary-item">
                        <span class="summary-label">Giờ tập:</span>
                        <span class="summary-value" id="summaryTime">-</span>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <h5 style="font-weight: 700; color: var(--primary); margin-bottom: 15px;">
                    Ghi chú (tùy chọn)
                </h5>
                <textarea 
                    class="booking-notes" 
                    id="bookingNotes" 
                    placeholder="Nhập mục tiêu tập luyện, yêu cầu đặc biệt..."></textarea>
            </div>
        </div>

        <div class="action-buttons">
            <button class="btn-back" onclick="previousStep()">
                <i class="fas fa-arrow-left"></i> Quay Lại
            </button>
            <button class="btn-primary" onclick="confirmBooking()">
                <i class="fas fa-check"></i> Xác Nhận Đặt Lịch
            </button>
        </div>
    </div>

    <!-- Alert message area (before Member Personal Schedule) -->
    <div id="bookingAlertContainer" style="margin-top: 30px;"></div>

    <!-- Member Personal Schedule Section -->
    <div class="booking-card" style="margin-top: 30px;">
        <h3 class="section-title">
            <i class="fas fa-calendar-check"></i>
            Lịch Tập Cá Nhân Của Tôi
        </h3>
        <div id="memberBookingsList">
            <!-- Member bookings will be loaded here -->
        </div>
    </div>
</div>

<script>
// Store context path in JavaScript variable
const contextPath = '${pageContext.request.contextPath}';

// Escape HTML to prevent XSS
function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// State management
let bookingState = {
    currentStep: 1,
    selectedGym: null,
    selectedTrainer: null,
    selectedDate: null,
    selectedSlot: null,
    currentMonth: new Date().getMonth(),
    currentYear: new Date().getFullYear()
};

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
    loadGyms();
    loadMemberBookings();
});

// Load gyms from server
function loadGyms() {
    fetch(contextPath + '/api/schedule/gyms')
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                displayGyms(result.data);
            } else {
                showAlert('Không thể tải danh sách cơ sở: ' + result.message, 'danger');
            }
        })
        .catch(error => {
            console.error('Error loading gyms:', error);
            showAlert('Không thể tải danh sách cơ sở. Vui lòng thử lại.', 'danger');
        });
}

// Display gyms
function displayGyms(gyms) {
    const gymGrid = document.getElementById('gymGrid');
    gymGrid.innerHTML = '';
    
    gyms.forEach(gym => {
        const gymCard = document.createElement('div');
        gymCard.className = 'gym-card';
        gymCard.onclick = (e) => selectGym(gym, e);
        const gymName = escapeHtml(gym.gymName || '');
        const gymAddress = escapeHtml(gym.location || 'Địa chỉ');
        gymCard.innerHTML = 
            '<div class="gym-icon">' +
                '<i class="fas fa-dumbbell"></i>' +
            '</div>' +
            '<div class="gym-name">' + gymName + '</div>' +
            '<div class="gym-address">' + gymAddress + '</div>';
        gymGrid.appendChild(gymCard);
    });
}

// Select gym
function selectGym(gym, event) {
    bookingState.selectedGym = gym;
    
    // Update UI
    document.querySelectorAll('.gym-card').forEach(card => {
        card.classList.remove('selected');
    });
    if (event && event.currentTarget) {
        event.currentTarget.classList.add('selected');
    }
    
    // Load trainers for this gym
    loadTrainers(gym.gymId || gym.id);
    
    // Move to next step
    setTimeout(() => {
        nextStep();
    }, 300);
}

// Load trainers for selected gym
function loadTrainers(gymId) {
    fetch(contextPath + '/api/schedule/trainers?gymId=' + gymId)
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                displayTrainers(result.data);
            } else {
                showAlert('Không thể tải danh sách PT: ' + result.message, 'danger');
            }
        })
        .catch(error => {
            console.error('Error loading trainers:', error);
            showAlert('Không thể tải danh sách PT. Vui lòng thử lại.', 'danger');
        });
}

// Display trainers
function displayTrainers(trainers) {
    const trainerGrid = document.getElementById('trainerGrid');
    trainerGrid.innerHTML = '';
    
    trainers.forEach(trainer => {
        const trainerCard = document.createElement('div');
        trainerCard.className = 'trainer-card';
        trainerCard.onclick = (e) => selectTrainer(trainer, e);
        const trainerName = escapeHtml(trainer.name || '');
        const trainerSpecialty = escapeHtml(trainer.specialization || 'Huấn luyện viên');
        trainerCard.innerHTML = 
            '<div class="trainer-avatar">' +
                '<i class="fas fa-user"></i>' +
            '</div>' +
            '<div class="trainer-name">' + trainerName + '</div>' +
            '<div class="trainer-specialty">' + trainerSpecialty + '</div>' +
            '<div class="trainer-rating">' +
                '<i class="fas fa-star"></i>' +
                '<i class="fas fa-star"></i>' +
                '<i class="fas fa-star"></i>' +
                '<i class="fas fa-star"></i>' +
                '<i class="fas fa-star"></i>' +
            '</div>';
        trainerGrid.appendChild(trainerCard);
    });
}

// Select trainer
function selectTrainer(trainer, event) {
    bookingState.selectedTrainer = trainer;
    
    // Update UI
    document.querySelectorAll('.trainer-card').forEach(card => {
        card.classList.remove('selected');
    });
    if (event && event.currentTarget) {
        event.currentTarget.classList.add('selected');
    }
    
    // Generate calendar
    generateCalendar();
    
    // Move to next step
    setTimeout(() => {
        nextStep();
    }, 300);
}

// Generate calendar
function generateCalendar() {
    const calendarGrid = document.getElementById('calendarGrid');
    const { currentMonth, currentYear } = bookingState;
    
    // Update month title
    const monthNames = ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6',
                       'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'];
    document.getElementById('calendarMonth').textContent = monthNames[currentMonth] + ', ' + currentYear;
    
    calendarGrid.innerHTML = '';
    
    // Day headers
    const dayHeaders = ['CN', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7'];
    dayHeaders.forEach(day => {
        const header = document.createElement('div');
        header.className = 'calendar-day-header';
        header.textContent = day;
        calendarGrid.appendChild(header);
    });
    
    // Get first day of month
    const firstDay = new Date(currentYear, currentMonth, 1).getDay();
    const daysInMonth = new Date(currentYear, currentMonth + 1, 0).getDate();
    const today = new Date();
    
    // Empty cells before first day
    for (let i = 0; i < firstDay; i++) {
        const emptyCell = document.createElement('div');
        emptyCell.className = 'calendar-day disabled';
        calendarGrid.appendChild(emptyCell);
    }
    
    // Day cells
    const todayDate = new Date();
    todayDate.setHours(0, 0, 0, 0);
    
    for (let day = 1; day <= daysInMonth; day++) {
        const dayCell = document.createElement('div');
        const cellDate = new Date(currentYear, currentMonth, day);
        cellDate.setHours(0, 0, 0, 0);
        const isPast = cellDate < todayDate;
        
        dayCell.className = 'calendar-day';
        if (isPast) {
            dayCell.classList.add('disabled');
        }
        if (todayDate.getDate() === day && todayDate.getMonth() === currentMonth && todayDate.getFullYear() === currentYear) {
            dayCell.classList.add('today');
        }
        dayCell.textContent = day;
        
        if (!isPast) {
            dayCell.onclick = (e) => selectDate(cellDate, e);
        }
        
        calendarGrid.appendChild(dayCell);
    }
}

// Select date
function selectDate(date, event) {
    bookingState.selectedDate = date;
    
    // Update UI
    document.querySelectorAll('.calendar-day:not(.disabled)').forEach(cell => {
        cell.classList.remove('selected');
    });
    if (event && event.currentTarget) {
        event.currentTarget.classList.add('selected');
    }
    
    // Load available time slots
    loadTimeSlots();
    
    // Move to next step
    setTimeout(() => {
        nextStep();
    }, 300);
}

// Load available time slots
function loadTimeSlots() {
    const { selectedGym, selectedTrainer, selectedDate } = bookingState;
    // Format date as YYYY-MM-DD to avoid timezone issues
    const year = selectedDate.getFullYear();
    const month = String(selectedDate.getMonth() + 1).padStart(2, '0');
    const day = String(selectedDate.getDate()).padStart(2, '0');
    const dateStr = year + '-' + month + '-' + day;
    const trainerId = selectedTrainer.id || selectedTrainer.trainerId;
    const gymId = selectedGym.gymId || selectedGym.id;
    
    fetch(contextPath + '/api/schedule/available-slots?trainerId=' + trainerId + '&gymId=' + gymId + '&date=' + dateStr)
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                displayAvailableSlots(result.data);
            } else {
                showAlert('Không thể tải khung giờ: ' + result.message, 'danger');
            }
        })
        .catch(error => {
            console.error('Error loading time slots:', error);
            showAlert('Không thể tải khung giờ. Vui lòng thử lại.', 'danger');
        });
}

// Display available time slots
function displayAvailableSlots(availableSlotIds) {
    // First, get all time slots
    fetch(contextPath + '/api/schedule/timeslots')
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                const allSlots = result.data;
                displayTimeSlots(allSlots, availableSlotIds);
            }
        })
        .catch(error => {
            console.error('Error loading all time slots:', error);
            showAlert('Không thể tải khung giờ. Vui lòng thử lại.', 'danger');
        });
}

// Display time slots
function displayTimeSlots(allSlots, availableSlotIds) {
    const timeslotGrid = document.getElementById('timeslotGrid');
    timeslotGrid.innerHTML = '';
    
    if (!allSlots || allSlots.length === 0) {
        timeslotGrid.innerHTML = '<p style="text-align: center; color: #6c757d; grid-column: 1 / -1;">Không có khung giờ nào</p>';
        return;
    }
    
    // Sort slots by start time
    allSlots.sort((a, b) => {
        const timeA = a.startTime || '';
        const timeB = b.startTime || '';
        return timeA.localeCompare(timeB);
    });
    
    // Normalize availableSlotIds to ensure we're comparing correctly
    const availableIds = (availableSlotIds && Array.isArray(availableSlotIds)) 
        ? availableSlotIds.map(id => parseInt(id)) 
        : [];
    
    // If availableSlotIds is empty, it means no slots are available (all booked or no schedule)
    // But we should still show all slots with proper status
    const hasAvailableSlots = availableIds.length > 0;
    
    allSlots.forEach(slot => {
        const slotId = parseInt(slot.slotId || slot.id);
        const isAvailable = hasAvailableSlots && availableIds.includes(slotId);
        
        const slotCard = document.createElement('div');
        slotCard.className = 'timeslot-card' + (isAvailable ? '' : ' unavailable');
        if (isAvailable) {
            slotCard.onclick = (e) => selectTimeSlot(slot, e);
        }
        const startTime = escapeHtml(slot.startTime || '');
        const endTime = escapeHtml(slot.endTime || '');
        const statusIcon = isAvailable 
            ? '<i class="fas fa-check-circle"></i> Còn trống' 
            : '<i class="fas fa-times-circle"></i> Đã đặt';
        slotCard.innerHTML = 
            '<div class="timeslot-time">' + startTime + ' - ' + endTime + '</div>' +
            '<div class="timeslot-status">' + statusIcon + '</div>';
        timeslotGrid.appendChild(slotCard);
    });
    
    // Show message if no slots available
    if (!hasAvailableSlots && allSlots.length > 0) {
        const messageDiv = document.createElement('div');
        messageDiv.style.cssText = 'grid-column: 1 / -1; text-align: center; padding: 20px; color: #856404; background: #fff3cd; border-radius: 8px; margin-top: 15px;';
        messageDiv.innerHTML = '<i class="fas fa-info-circle"></i> Huấn luyện viên không có lịch làm việc vào ngày này hoặc tất cả khung giờ đã được đặt.';
        timeslotGrid.appendChild(messageDiv);
    }
}

// Select time slot
function selectTimeSlot(slot, event) {
    bookingState.selectedSlot = slot;
    
    // Update UI
    document.querySelectorAll('.timeslot-card').forEach(card => {
        card.classList.remove('selected');
    });
    if (event && event.currentTarget) {
        event.currentTarget.classList.add('selected');
    }
    
    // Update summary
    updateSummary();
    
    // Move to next step
    setTimeout(() => {
        nextStep();
    }, 300);
}

// Update booking summary
function updateSummary() {
    const { selectedGym, selectedTrainer, selectedDate, selectedSlot } = bookingState;
    
    document.getElementById('summaryGym').textContent = selectedGym.gymName || '';
    document.getElementById('summaryTrainer').textContent = selectedTrainer.name || '';
    
    const dateStr = selectedDate.toLocaleDateString('vi-VN', { 
        weekday: 'long', 
        year: 'numeric', 
        month: 'long', 
        day: 'numeric' 
    });
    document.getElementById('summaryDate').textContent = dateStr;
    document.getElementById('summaryTime').textContent = (selectedSlot.startTime || '') + ' - ' + (selectedSlot.endTime || '');
}

// Confirm booking
function confirmBooking() {
    const { selectedGym, selectedTrainer, selectedDate, selectedSlot } = bookingState;
    const notes = document.getElementById('bookingNotes').value;
    
    // Format date as YYYY-MM-DD to avoid timezone issues
    const year = selectedDate.getFullYear();
    const month = String(selectedDate.getMonth() + 1).padStart(2, '0');
    const day = String(selectedDate.getDate()).padStart(2, '0');
    const dateStr = year + '-' + month + '-' + day;
    
    const bookingData = {
        gymId: selectedGym.gymId || selectedGym.id,
        trainerId: selectedTrainer.id || selectedTrainer.trainerId,
        date: dateStr,
        slotId: selectedSlot.slotId || selectedSlot.id,
        notes: notes
    };
    
    fetch(contextPath + '/api/schedule/bookings', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(bookingData)
    })
    .then(response => {
        // Check if response is ok
        if (!response.ok) {
            return response.json().then(data => {
                return { success: false, message: data.message || 'Đặt lịch thất bại. Vui lòng thử lại.' };
            });
        }
        return response.json();
    })
    .then(result => {
        if (result.success) {
            // Show success message using alert
            alert('Đặt lịch thành công! Vui lòng chờ PT xác nhận.');
            
            // Reset booking state first
            bookingState = {
                currentStep: 1,
                selectedGym: null,
                selectedTrainer: null,
                selectedDate: null,
                selectedSlot: null,
                currentMonth: new Date().getMonth(),
                currentYear: new Date().getFullYear()
            };
            
            // Reset to step 1 (but keep "Lịch Tập Cá Nhân Của Tôi" section visible)
            document.querySelectorAll('.booking-card').forEach(card => {
                // Don't hide the member bookings section
                if (!card.querySelector('#memberBookingsList')) {
                    card.classList.add('hidden');
                }
            });
            document.getElementById('step1').classList.remove('hidden');
            document.querySelectorAll('.step').forEach((step, idx) => {
                step.classList.remove('active', 'completed');
                if (idx === 0) step.classList.add('active');
            });
            
            // Clear selections
            document.getElementById('gymGrid').innerHTML = '';
            document.getElementById('trainerGrid').innerHTML = '';
            document.getElementById('timeslotGrid').innerHTML = '';
            document.getElementById('bookingNotes').value = '';
            
            // Reload gyms to reset the form
            loadGyms();
            
            // Reload member bookings after a short delay to ensure data is saved
            setTimeout(() => {
                loadMemberBookings();
                
                // Scroll to "Lịch Tập Cá Nhân Của Tôi" section after loading
                setTimeout(() => {
                    const memberBookingsList = document.getElementById('memberBookingsList');
                    if (memberBookingsList) {
                        // Find the parent booking-card container
                        const memberScheduleSection = memberBookingsList.closest('.booking-card');
                        if (memberScheduleSection) {
                            // Ensure the section is visible
                            memberScheduleSection.classList.remove('hidden');
                            memberScheduleSection.style.display = '';
                            
                            // Scroll to show the section title and content
                            memberScheduleSection.scrollIntoView({ behavior: 'smooth', block: 'center' });
                            
                            // Highlight the section with animation
                            memberScheduleSection.style.transition = 'all 0.5s ease';
                            memberScheduleSection.style.boxShadow = '0 0 30px rgba(255, 152, 0, 0.7)';
                            memberScheduleSection.style.border = '3px solid var(--orange)';
                            memberScheduleSection.style.transform = 'scale(1.01)';
                            
                            // Add a pulse animation
                            let pulseCount = 0;
                            const pulseInterval = setInterval(() => {
                                if (pulseCount < 3) {
                                    memberScheduleSection.style.boxShadow = 
                                        pulseCount % 2 === 0 
                                            ? '0 0 40px rgba(255, 152, 0, 0.8)' 
                                            : '0 0 20px rgba(255, 152, 0, 0.5)';
                                    pulseCount++;
                                } else {
                                    clearInterval(pulseInterval);
                                    // Remove highlight after animation
                                    setTimeout(() => {
                                        memberScheduleSection.style.boxShadow = '';
                                        memberScheduleSection.style.border = '';
                                        memberScheduleSection.style.transform = '';
                                    }, 1000);
                                }
                            }, 300);
                        } else {
                            // Fallback: scroll to the list itself
                            memberBookingsList.scrollIntoView({ behavior: 'smooth', block: 'center' });
                        }
                    }
                }, 1000);
            }, 500);
        } else {
            showAlert(result.message || 'Đặt lịch thất bại. Vui lòng thử lại.', 'danger');
        }
    })
    .catch(error => {
        console.error('Error confirming booking:', error);
        showAlert('Có lỗi xảy ra. Vui lòng thử lại.', 'danger');
    });
}

// Navigation functions
function nextStep() {
    if (bookingState.currentStep < 5) {
        document.getElementById('step' + bookingState.currentStep).classList.add('hidden');
        document.getElementById('step' + bookingState.currentStep + '-indicator').classList.remove('active');
        document.getElementById('step' + bookingState.currentStep + '-indicator').classList.add('completed');
        
        bookingState.currentStep++;
        
        document.getElementById('step' + bookingState.currentStep).classList.remove('hidden');
        document.getElementById('step' + bookingState.currentStep + '-indicator').classList.add('active');
        
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }
}

function previousStep() {
    if (bookingState.currentStep > 1) {
        document.getElementById('step' + bookingState.currentStep).classList.add('hidden');
        document.getElementById('step' + bookingState.currentStep + '-indicator').classList.remove('active');
        
        bookingState.currentStep--;
        
        document.getElementById('step' + bookingState.currentStep).classList.remove('hidden');
        document.getElementById('step' + bookingState.currentStep + '-indicator').classList.add('active');
        document.getElementById('step' + bookingState.currentStep + '-indicator').classList.remove('completed');
        
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }
}

function previousMonth() {
    const today = new Date();
    const currentDate = new Date(bookingState.currentYear, bookingState.currentMonth, 1);
    const minDate = new Date(today.getFullYear(), today.getMonth(), 1);
    
    if (bookingState.currentMonth === 0) {
        bookingState.currentMonth = 11;
        bookingState.currentYear--;
    } else {
        bookingState.currentMonth--;
    }
    
    // Don't allow going to past months
    const newDate = new Date(bookingState.currentYear, bookingState.currentMonth, 1);
    if (newDate < minDate) {
        bookingState.currentMonth = today.getMonth();
        bookingState.currentYear = today.getFullYear();
    }
    
    generateCalendar();
}

function nextMonth() {
    if (bookingState.currentMonth === 11) {
        bookingState.currentMonth = 0;
        bookingState.currentYear++;
    } else {
        bookingState.currentMonth++;
    }
    generateCalendar();
}

// Show alert message
function showAlert(message, type) {
    const alertContainer = document.getElementById('alertContainer');
    const alert = document.createElement('div');
    const iconClass = type === 'success' ? 'check-circle' : type === 'danger' ? 'exclamation-circle' : 'info-circle';
    alert.className = 'alert alert-' + type;
    alert.innerHTML = '<i class="fas fa-' + iconClass + '"></i> ' + escapeHtml(message);
    alertContainer.appendChild(alert);
    
    setTimeout(() => {
        alert.remove();
    }, 5000);
}

// Load member bookings
function loadMemberBookings() {
    fetch(contextPath + '/api/schedule/bookings')
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                displayMemberBookings(result.data);
            } else {
                document.getElementById('memberBookingsList').innerHTML = 
                    '<p style="text-align: center; color: #6c757d; padding: 20px;">Chưa có lịch tập nào</p>';
            }
        })
        .catch(error => {
            console.error('Error loading member bookings:', error);
            document.getElementById('memberBookingsList').innerHTML = 
                '<p style="text-align: center; color: #dc3545; padding: 20px;">Không thể tải lịch tập</p>';
        });
}

// Display member bookings
function displayMemberBookings(bookings) {
    const container = document.getElementById('memberBookingsList');
    
    if (!bookings || bookings.length === 0) {
        container.innerHTML = '<p style="text-align: center; color: #6c757d; padding: 20px;">Chưa có lịch tập nào</p>';
        return;
    }
    
    // Sort by date (upcoming first - lịch gần nhất lên trước)
    bookings.sort((a, b) => {
        const dateA = new Date(a.bookingDate);
        const dateB = new Date(b.bookingDate);
        // Sort ascending: upcoming dates first
        return dateA - dateB;
    });
    
    container.innerHTML = '';
    bookings.forEach(booking => {
        const card = createMemberBookingCard(booking);
        container.appendChild(card);
    });
}

// Create member booking card
function createMemberBookingCard(booking) {
    const card = document.createElement('div');
    card.className = 'booking-card';
    card.style.marginBottom = '15px';
    
    const statusClass = 'status-' + (booking.bookingStatus || 'PENDING');
    const statusText = getBookingStatusText(booking.bookingStatus);
    const canCancel = booking.bookingStatus === 'PENDING' || booking.bookingStatus === 'CONFIRMED';
    
    const trainerName = booking.trainer ? escapeHtml(booking.trainer.name) : 'N/A';
    const gymName = booking.gym ? escapeHtml(booking.gym.gymName || booking.gym.name) : 'N/A';
    const bookingDate = formatBookingDate(booking.bookingDate);
    const timeSlot = booking.timeSlot ? escapeHtml(booking.timeSlot.startTime) + ' - ' + escapeHtml(booking.timeSlot.endTime) : 'N/A';
    const notes = booking.notes ? '<div style="margin-top: 10px; padding: 10px; background: #f8f9fa; border-radius: 8px; font-size: 0.9rem;"><strong>Ghi chú:</strong> ' + escapeHtml(booking.notes) + '</div>' : '';
    // Get booking ID - handle both bookingId and id fields
    const bookingId = booking.bookingId || booking.id;
    const cancelButton = canCancel ? '<div style="margin-top: 15px;"><button class="btn btn-danger" onclick="cancelMemberBooking(' + bookingId + ', event)"><i class="fas fa-times-circle"></i> Hủy Lịch</button></div>' : '';
    
    card.innerHTML = 
        '<div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; padding-bottom: 15px; border-bottom: 2px solid #e9ecef;">' +
            '<h4 style="margin: 0; color: var(--primary);"><i class="fas fa-dumbbell"></i> Buổi Tập PT</h4>' +
            '<span class="booking-status ' + statusClass + '">' + escapeHtml(statusText) + '</span>' +
        '</div>' +
        '<div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 15px;">' +
            '<div><strong style="color: #6c757d; font-size: 0.9rem;">Cơ sở:</strong><br><span style="color: var(--primary); font-weight: 600;">' + gymName + '</span></div>' +
            '<div><strong style="color: #6c757d; font-size: 0.9rem;">Huấn luyện viên:</strong><br><span style="color: var(--primary); font-weight: 600;">' + trainerName + '</span></div>' +
            '<div><strong style="color: #6c757d; font-size: 0.9rem;">Ngày tập:</strong><br><span style="color: var(--primary); font-weight: 600;">' + bookingDate + '</span></div>' +
            '<div><strong style="color: #6c757d; font-size: 0.9rem;">Giờ tập:</strong><br><span style="color: var(--primary); font-weight: 600;">' + timeSlot + '</span></div>' +
        '</div>' +
        notes +
        cancelButton;
    
    return card;
}

// Get booking status text
function getBookingStatusText(status) {
    const statusMap = {
        'PENDING': 'Chờ Xác Nhận',
        'CONFIRMED': 'Đã Xác Nhận',
        'CANCELLED': 'Đã Hủy',
        'COMPLETED': 'Hoàn Thành'
    };
    return statusMap[status] || status || 'Chờ Xác Nhận';
}

// Format booking date
function formatBookingDate(dateStr) {
    const date = new Date(dateStr);
    return date.toLocaleDateString('vi-VN', {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
}

// Show alert message before "Lịch Tập Cá Nhân Của Tôi" section
function showBookingAlert(message, type) {
    const alertContainer = document.getElementById('bookingAlertContainer');
    if (!alertContainer) return;
    
    // Remove existing alerts
    alertContainer.innerHTML = '';
    
    // Create alert div
    const alertDiv = document.createElement('div');
    alertDiv.className = 'alert alert-' + (type === 'success' ? 'success' : 'danger');
    alertDiv.style.cssText = 'padding: 15px 20px; border-radius: 8px; margin-bottom: 20px; box-shadow: 0 4px 12px rgba(0,0,0,0.15); display: flex; align-items: center; gap: 10px;';
    
    const icon = type === 'success' 
        ? '<i class="fas fa-check-circle" style="font-size: 1.2rem;"></i>' 
        : '<i class="fas fa-exclamation-circle" style="font-size: 1.2rem;"></i>';
    
    alertDiv.innerHTML = icon + '<span>' + escapeHtml(message) + '</span>';
    
    alertContainer.appendChild(alertDiv);
    
    // Auto remove after 5 seconds for success messages
    if (type === 'success') {
        setTimeout(() => {
            if (alertDiv.parentNode) {
                alertDiv.style.transition = 'opacity 0.5s';
                alertDiv.style.opacity = '0';
                setTimeout(() => {
                    if (alertDiv.parentNode) {
                        alertDiv.remove();
                    }
                }, 500);
            }
        }, 5000);
    }
}

// Cancel member booking
function cancelMemberBooking(bookingId, event) {
    // Ensure bookingId is a number
    bookingId = parseInt(bookingId);
    if (isNaN(bookingId)) {
        alert('ID lịch đặt không hợp lệ');
        return;
    }
    
    if (!confirm('Bạn có chắc muốn hủy lịch tập này?\n\n⚠️ Lưu ý: Chỉ có thể hủy lịch trước 24 giờ diễn ra buổi tập.\n\nKhung giờ sẽ được giải phóng để người khác có thể đặt lịch.')) {
        return;
    }
    
    // Disable button and show loading
    const button = (event && event.target) ? event.target.closest('button') : document.querySelector('button[onclick*="cancelMemberBooking(' + bookingId + ')"]');
    if (!button) {
        alert('Không tìm thấy nút hủy lịch');
        return;
    }
    const originalText = button.innerHTML;
    button.disabled = true;
    button.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang xử lý...';
    
    console.log('Cancelling booking ID:', bookingId, 'URL:', contextPath + '/api/schedule/bookings/' + bookingId + '/cancel');
    fetch(contextPath + '/api/schedule/bookings/' + bookingId + '/cancel', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
    })
    .then(response => {
        // Always parse as text first, then try to parse as JSON
        return response.text().then(text => {
            try {
                // Try to parse as JSON
                const jsonData = JSON.parse(text);
                return jsonData;
            } catch (e) {
                // If not JSON, check response status
                if (response.ok) {
                    // Success but not JSON - return success
                    return { success: true, message: 'Hủy lịch thành công!' };
                } else {
                    // Error response - extract message from HTML if possible
                    let errorMsg = 'Không thể hủy lịch. Vui lòng thử lại.';
                    if (text.includes('405') || text.includes('Méthode non autorisée')) {
                        errorMsg = 'Phương thức không được phép. Vui lòng thử lại.';
                    } else if (text.includes('401') || text.includes('đăng nhập')) {
                        errorMsg = 'Vui lòng đăng nhập lại.';
                    } else if (text.length < 500 && text.trim().length > 0) {
                        // If text is short, might be a simple error message
                        errorMsg = text.trim();
                    }
                    return { success: false, message: errorMsg };
                }
            }
        });
    })
    .then(result => {
        console.log('Cancel booking result:', result);
        if (result && result.success) {
            // Show success message using alert
            alert('Hủy lịch thành công');
            
            // Find and remove the booking card from UI
            const card = button.closest('.booking-card');
            if (card) {
                // Add fade out animation
                card.style.transition = 'opacity 0.3s ease, transform 0.3s ease';
                card.style.opacity = '0';
                card.style.transform = 'translateX(-20px)';
                
                // Remove card after animation
                setTimeout(() => {
                    card.remove();
                    
                    // Check if there are no more bookings
                    const memberBookingsList = document.getElementById('memberBookingsList');
                    if (memberBookingsList && memberBookingsList.children.length === 0) {
                        memberBookingsList.innerHTML = '<p style="text-align: center; color: #6c757d; padding: 20px;">Chưa có lịch tập nào</p>';
                    }
                }, 300);
            } else {
                // Fallback: reload bookings if card not found
                setTimeout(() => {
                    loadMemberBookings();
                }, 500);
            }
        } else {
            const errorMsg = (result && result.message) ? result.message : 'Không thể hủy lịch. Vui lòng thử lại.';
            console.error('Cancel booking failed:', errorMsg, result);
            // Show error message using alert
            alert('Hủy lịch không thành công: ' + errorMsg);
            // Re-enable button
            button.disabled = false;
            button.innerHTML = originalText;
        }
    })
    .catch(error => {
        console.error('Error cancelling booking:', error);
        let errorMsg = 'Có lỗi xảy ra khi hủy lịch. Vui lòng thử lại.';
        if (error.message && !error.message.includes('<html') && !error.message.includes('<!doctype')) {
            errorMsg = error.message;
        }
        // Show error message using alert
        alert('Hủy lịch không thành công: ' + errorMsg);
        // Re-enable button
        button.disabled = false;
        button.innerHTML = originalText;
    });
}
</script>

<%@ include file="/views/common/footer.jsp" %>
