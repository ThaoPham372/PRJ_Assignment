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
            <button class="btn-secondary" onclick="previousStep()">
                <i class="fas fa-arrow-left"></i> Quay Lại
            </button>
            <button class="btn-primary" onclick="confirmBooking()">
                <i class="fas fa-check"></i> Xác Nhận Đặt Lịch
            </button>
        </div>
    </div>
</div>

<script>
// Get context path
const contextPath = '${pageContext.request.contextPath}';

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
        gymCard.onclick = () => selectGym(gym, gymCard);
        gymCard.innerHTML = `
            <div class="gym-icon">
                <i class="fas fa-dumbbell"></i>
            </div>
            <div class="gym-name">${gym.gymName}</div>
            <div class="gym-address">${gym.location || 'Địa chỉ'}</div>
        `;
        gymGrid.appendChild(gymCard);
    });
}

// Select gym
function selectGym(gym, element) {
    bookingState.selectedGym = gym;
    
    // Update UI
    document.querySelectorAll('.gym-card').forEach(card => {
        card.classList.remove('selected');
    });
    if (element) {
        element.classList.add('selected');
    }
    
    // Load trainers for this gym
    loadTrainers(gym.gymId);
    
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
        trainerCard.onclick = () => selectTrainer(trainer, trainerCard);
        trainerCard.innerHTML = `
            <div class="trainer-avatar">
                <i class="fas fa-user"></i>
            </div>
            <div class="trainer-name">${trainer.name}</div>
            <div class="trainer-specialty">${trainer.specialization || 'Huấn luyện viên'}</div>
            <div class="trainer-rating">
                <i class="fas fa-star"></i>
                <i class="fas fa-star"></i>
                <i class="fas fa-star"></i>
                <i class="fas fa-star"></i>
                <i class="fas fa-star"></i>
            </div>
        `;
        trainerGrid.appendChild(trainerCard);
    });
}

// Select trainer
function selectTrainer(trainer, element) {
    bookingState.selectedTrainer = trainer;
    
    // Update UI
    document.querySelectorAll('.trainer-card').forEach(card => {
        card.classList.remove('selected');
    });
    if (element) {
        element.classList.add('selected');
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
    document.getElementById('calendarMonth').textContent = `${monthNames[currentMonth]}, ${currentYear}`;
    
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
    for (let day = 1; day <= daysInMonth; day++) {
        const dayCell = document.createElement('div');
        const cellDate = new Date(currentYear, currentMonth, day);
        const todayStart = new Date(today.getFullYear(), today.getMonth(), today.getDate());
        const isPast = cellDate < todayStart;
        
        dayCell.className = 'calendar-day';
        if (isPast) {
            dayCell.classList.add('disabled');
        }
        if (today.getDate() === day && today.getMonth() === currentMonth && today.getFullYear() === currentYear) {
            dayCell.classList.add('today');
        }
        dayCell.textContent = day;
        
        if (!isPast) {
            dayCell.onclick = () => selectDate(cellDate, dayCell);
        }
        
        calendarGrid.appendChild(dayCell);
    }
}

// Select date
function selectDate(date, element) {
    bookingState.selectedDate = date;
    
    // Update UI
    document.querySelectorAll('.calendar-day:not(.disabled)').forEach(cell => {
        cell.classList.remove('selected');
    });
    if (element) {
        element.classList.add('selected');
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
    const dateStr = selectedDate.toISOString().split('T')[0];
    
    fetch(contextPath + '/api/schedule/available-slots?trainerId=' + selectedTrainer.id + '&gymId=' + selectedGym.gymId + '&date=' + dateStr)
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
    
    allSlots.forEach(slot => {
        const isAvailable = availableSlotIds.includes(slot.slotId);
        const slotCard = document.createElement('div');
        slotCard.className = `timeslot-card ${isAvailable ? '' : 'unavailable'}`;
        if (isAvailable) {
            slotCard.onclick = () => selectTimeSlot(slot, slotCard);
        }
        slotCard.innerHTML = `
            <div class="timeslot-time">${slot.startTime} - ${slot.endTime}</div>
            <div class="timeslot-status">
                ${isAvailable ? '<i class="fas fa-check-circle"></i> Còn trống' : '<i class="fas fa-times-circle"></i> Đã đặt'}
            </div>
        `;
        timeslotGrid.appendChild(slotCard);
    });
}

// Select time slot
function selectTimeSlot(slot, element) {
    bookingState.selectedSlot = slot;
    
    // Update UI
    document.querySelectorAll('.timeslot-card').forEach(card => {
        card.classList.remove('selected');
    });
    if (element) {
        element.classList.add('selected');
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
    
    document.getElementById('summaryGym').textContent = selectedGym.gymName;
    document.getElementById('summaryTrainer').textContent = selectedTrainer.name;
    
    const dateStr = selectedDate.toLocaleDateString('vi-VN', { 
        weekday: 'long', 
        year: 'numeric', 
        month: 'long', 
        day: 'numeric' 
    });
    document.getElementById('summaryDate').textContent = dateStr;
    document.getElementById('summaryTime').textContent = `${selectedSlot.startTime} - ${selectedSlot.endTime}`;
}

// Confirm booking
function confirmBooking() {
    const { selectedGym, selectedTrainer, selectedDate, selectedSlot } = bookingState;
    const notes = document.getElementById('bookingNotes').value;
    
    const bookingData = {
        gymId: selectedGym.gymId,
        trainerId: selectedTrainer.id,
        date: selectedDate.toISOString().split('T')[0],
        slotId: selectedSlot.slotId,
        notes: notes
    };
    
    fetch(contextPath + '/api/schedule/bookings', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(bookingData)
    })
    .then(response => response.json())
    .then(result => {
        if (result.success) {
            showAlert('Đặt lịch thành công! Vui lòng chờ PT xác nhận.', 'success');
            setTimeout(() => {
                window.location.href = contextPath + '/member/dashboard';
            }, 2000);
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
        document.getElementById(`step${bookingState.currentStep}`).classList.add('hidden');
        document.getElementById(`step${bookingState.currentStep}-indicator`).classList.remove('active');
        document.getElementById(`step${bookingState.currentStep}-indicator`).classList.add('completed');
        
        bookingState.currentStep++;
        
        document.getElementById(`step${bookingState.currentStep}`).classList.remove('hidden');
        document.getElementById(`step${bookingState.currentStep}-indicator`).classList.add('active');
        
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }
}

function previousStep() {
    if (bookingState.currentStep > 1) {
        document.getElementById(`step${bookingState.currentStep}`).classList.add('hidden');
        document.getElementById(`step${bookingState.currentStep}-indicator`).classList.remove('active');
        
        bookingState.currentStep--;
        
        document.getElementById(`step${bookingState.currentStep}`).classList.remove('hidden');
        document.getElementById(`step${bookingState.currentStep}-indicator`).classList.add('active');
        document.getElementById(`step${bookingState.currentStep}-indicator`).classList.remove('completed');
        
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }
}

function previousMonth() {
    if (bookingState.currentMonth === 0) {
        bookingState.currentMonth = 11;
        bookingState.currentYear--;
    } else {
        bookingState.currentMonth--;
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

// Escape HTML to prevent XSS
function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Get icon class based on alert type
function getAlertIcon(type) {
    if (type === 'success') {
        return 'check-circle';
    } else if (type === 'danger') {
        return 'exclamation-circle';
    } else {
        return 'info-circle';
    }
}

// Show alert message
function showAlert(message, type) {
    if (!message) return;
    
    const alertContainer = document.getElementById('alertContainer');
    if (!alertContainer) return;
    
    const alert = document.createElement('div');
    alert.className = `alert alert-${type || 'info'}`;
    
    const iconClass = getAlertIcon(type);
    const escapedMessage = escapeHtml(message);
    
    alert.innerHTML = `
        <i class="fas fa-${iconClass}"></i>
        ${escapedMessage}
    `;
    alertContainer.appendChild(alert);
    
    setTimeout(() => {
        alert.remove();
    }, 5000);
}
</script>

<%@ include file="/views/common/footer.jsp" %>
