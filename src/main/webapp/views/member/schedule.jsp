<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %> <%@ include
file="/views/common/header.jsp" %>

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
    --success: #28a745;
    --info: #17a2b8;
    --warning: #ffc107;
    --secondary: #6c757d;
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

  .schedule-card {
    background: var(--card);
    border-radius: 20px;
    box-shadow: 0 4px 20px var(--shadow);
    padding: 30px;
    margin-bottom: 25px;
    transition: all 0.3s ease;
    border: 1px solid rgba(0, 0, 0, 0.05);
  }

  .schedule-card:hover {
    box-shadow: 0 8px 30px var(--shadow-hover);
  }

  .schedule-header-card {
    background: var(--gradient-primary);
    color: white;
    border-radius: 0px;
    padding: 40px;
    margin-bottom: 30px;
    box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
    position: relative;
    overflow: hidden;
  }

  .schedule-header-card::before {
    content: '';
    position: absolute;
    top: -50%;
    right: -10%;
    width: 400px;
    height: 400px;
    background: radial-gradient(
      circle,
      rgba(236, 139, 94, 0.2) 0%,
      transparent 70%
    );
    border-radius: 50%;
  }

  .schedule-header-content {
    position: relative;
    z-index: 1;
  }

  .btn-back {
    background: rgba(255, 255, 255, 0.15);
    color: white;
    border: 2px solid rgba(255, 255, 255, 0.3);
    border-radius: 25px;
    padding: 10px 20px;
    font-weight: 600;
    transition: all 0.3s ease;
    text-decoration: none;
    display: inline-flex;
    align-items: center;
    gap: 8px;
    font-size: 0.9rem;
    margin-bottom: 25px;
  }

  .btn-back:hover {
    background: rgba(255, 255, 255, 0.25);
    transform: translateX(-5px);
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

  .schedule-title {
    color: var(--text);
    font-weight: 800;
    margin-bottom: 25px;
    text-transform: uppercase;
    letter-spacing: 1px;
    font-size: 1.3rem;
    display: flex;
    align-items: center;
    gap: 10px;
    padding-bottom: 15px;
    border-bottom: 3px solid var(--accent);
  }

  .schedule-title i {
    color: var(--accent);
    font-size: 1.4rem;
  }

  .calendar-day {
    background: white;
    border-radius: 15px;
    padding: 25px;
    margin-bottom: 20px;
    border: 2px solid #e9ecef;
    border-left: 5px solid var(--accent);
    transition: all 0.3s ease;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  }

  .calendar-day:hover {
    transform: translateX(5px);
    box-shadow: 0 5px 20px rgba(236, 139, 94, 0.2);
    border-left-color: var(--accent-hover);
  }

  .calendar-day-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
    gap: 15px;
  }

  .calendar-day-info h5 {
    color: var(--text);
    font-weight: 700;
    margin-bottom: 8px;
    font-size: 1.1rem;
  }

  .calendar-day-info p {
    color: var(--text-light);
    margin-bottom: 0;
    font-weight: 500;
    font-size: 0.95rem;
  }

  .calendar-day-actions {
    display: flex;
    gap: 10px;
    align-items: center;
  }

  .time-badge {
    padding: 8px 16px;
    border-radius: 20px;
    font-size: 0.9rem;
    font-weight: 700;
    display: inline-flex;
    align-items: center;
    gap: 6px;
  }

  .time-badge.success {
    background: rgba(40, 167, 69, 0.1);
    color: var(--success);
    border: 2px solid var(--success);
  }

  .time-badge.info {
    background: rgba(23, 162, 184, 0.1);
    color: var(--info);
    border: 2px solid var(--info);
  }

  .time-badge.warning {
    background: rgba(255, 193, 7, 0.1);
    color: #d39e00;
    border: 2px solid #d39e00;
  }

  .time-badge.secondary {
    background: rgba(108, 117, 125, 0.1);
    color: var(--secondary);
    border: 2px solid var(--secondary);
  }

  .btn-schedule {
    background: var(--gradient-accent);
    color: white;
    border: none;
    border-radius: 25px;
    padding: 14px 28px;
    font-weight: 700;
    transition: all 0.3s ease;
    text-decoration: none;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    box-shadow: 0 4px 15px rgba(236, 139, 94, 0.3);
    font-size: 1rem;
    text-transform: uppercase;
    letter-spacing: 0.5px;
  }

  .btn-schedule:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 25px rgba(236, 139, 94, 0.4);
    color: white;
  }

  .btn-edit {
    background: transparent;
    color: var(--accent);
    border: 2px solid var(--accent);
    border-radius: 20px;
    padding: 8px 20px;
    font-weight: 600;
    transition: all 0.3s ease;
    text-decoration: none;
    display: inline-flex;
    align-items: center;
    gap: 6px;
    font-size: 0.9rem;
  }

  .btn-edit:hover {
    background: var(--accent);
    color: white;
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(236, 139, 94, 0.3);
  }

  .btn-class {
    background: white;
    color: var(--text);
    border: 2px solid #e9ecef;
    border-radius: 15px;
    padding: 18px 20px;
    font-weight: 600;
    transition: all 0.3s ease;
    text-decoration: none;
    display: flex;
    align-items: center;
    gap: 12px;
    font-size: 1rem;
    width: 100%;
    margin-bottom: 12px;
  }

  .btn-class:hover {
    background: var(--gradient-accent);
    color: white;
    border-color: var(--accent);
    transform: translateX(5px);
    box-shadow: 0 4px 15px rgba(236, 139, 94, 0.3);
  }

  .btn-class i {
    font-size: 1.3rem;
    width: 30px;
    text-align: center;
  }

  .stat-box {
    text-align: center;
    padding: 20px;
    background: linear-gradient(135deg, #fff5f0 0%, #ffe8e0 100%);
    border-radius: 15px;
    border: 2px solid rgba(236, 139, 94, 0.2);
    transition: all 0.3s ease;
  }

  .stat-box:hover {
    transform: translateY(-5px);
    box-shadow: 0 5px 20px rgba(236, 139, 94, 0.2);
    border-color: var(--accent);
  }

  .stat-number {
    font-size: 2.2rem;
    font-weight: 900;
    color: var(--accent);
    margin-bottom: 8px;
    display: block;
  }

  .stat-label {
    font-size: 0.85rem;
    color: var(--text-light);
    font-weight: 600;
    text-transform: uppercase;
    letter-spacing: 0.5px;
  }

  @media (max-width: 768px) {
    .calendar-day {
      padding: 20px;
    }

    .calendar-day-header {
      flex-direction: column;
      align-items: flex-start;
    }

    .calendar-day-actions {
      width: 100%;
      flex-direction: column;
    }

    .time-badge,
    .btn-edit {
      width: 100%;
      justify-content: center;
    }
  }
</style>

<div class="schedule-container">
  <!-- Back Button -->
  <a
    href="${pageContext.request.contextPath}/member/dashboard"
    class="btn-back"
  >
    <i class="fas fa-arrow-left"></i>
    Quay lại Dashboard
  </a>

  <!-- Schedule Header -->
  <div class="schedule-header-card text-center">
    <div class="schedule-header-content">
      <h1
        style="
          font-size: 2.5rem;
          font-weight: 900;
          margin-bottom: 15px;
          text-transform: uppercase;
          letter-spacing: 1px;
        "
      >
        📅 Lịch Tập Của Tôi
      </h1>
      <p style="font-size: 1.1rem; opacity: 0.9; margin-bottom: 25px">
        Quản lý và đặt lịch tập luyện
      </p>
      
      <a href="${pageContext.request.contextPath}/member/schedule/create" class="btn-schedule">
         <i class="fas fa-plus"></i>
             Đặt Lịch Mới
      </a>

      
    </div>
  </div>

  <!-- Calendar View -->
  <div class="row">
    <div class="col-lg-8">
      <div class="schedule-card">
        <h4 class="schedule-title">
          <i class="fas fa-calendar-week"></i>
          Tuần Này
        </h4>

        <!-- Monday -->
        <div class="calendar-day">
          <div class="calendar-day-header">
            <div class="calendar-day-info">
              <h5>Thứ 2 - 15/01/2024</h5>
              <p>Cardio & Strength Training</p>
            </div>
            <div class="calendar-day-actions">
              <span class="time-badge success">
                <i class="fas fa-clock"></i>
                09:00 - 10:30
              </span>
              <button class="btn-edit">
                <i class="fas fa-edit"></i>
                Edit
              </button>
            </div>
          </div>
        </div>

        <!-- Wednesday -->
        <div class="calendar-day">
          <div class="calendar-day-header">
            <div class="calendar-day-info">
              <h5>Thứ 4 - 17/01/2024</h5>
              <p>Yoga & Flexibility</p>
            </div>
            <div class="calendar-day-actions">
              <span class="time-badge info">
                <i class="fas fa-clock"></i>
                18:00 - 19:00
              </span>
              <button class="btn-edit">
                <i class="fas fa-edit"></i>
                Edit
              </button>
            </div>
          </div>
        </div>

        <!-- Friday -->
        <div class="calendar-day">
          <div class="calendar-day-header">
            <div class="calendar-day-info">
              <h5>Thứ 6 - 19/01/2024</h5>
              <p>HIIT Training</p>
            </div>
            <div class="calendar-day-actions">
              <span class="time-badge warning">
                <i class="fas fa-clock"></i>
                07:00 - 08:00
              </span>
              <button class="btn-edit">
                <i class="fas fa-edit"></i>
                Edit
              </button>
            </div>
          </div>
        </div>

        <!-- Sunday -->
        <div class="calendar-day">
          <div class="calendar-day-header">
            <div class="calendar-day-info">
              <h5>Chủ Nhật - 21/01/2024</h5>
              <p>Rest Day</p>
            </div>
            <div class="calendar-day-actions">
              <span class="time-badge secondary">
                <i class="fas fa-bed"></i>
                Nghỉ ngơi
              </span>
              <button class="btn-edit">
                <i class="fas fa-edit"></i>
                Edit
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="col-lg-4">
      <!-- Quick Stats -->
      <div class="schedule-card">
        <h5 class="schedule-title">
          <i class="fas fa-chart-bar"></i>
          Thống Kê
        </h5>
        <div class="row g-3">
          <div class="col-md-3 col-6">
            <div class="stat-box">
              <span class="stat-number">4</span>
              <span class="stat-label">Buổi/Tuần</span>
            </div>
          </div>
          <div class="col-md-3 col-6">
            <div class="stat-box">
              <span class="stat-number">12</span>
              <span class="stat-label">Giờ/Tuần</span>
            </div>
          </div>
          <div class="col-md-3 col-6">
            <div class="stat-box">
              <span class="stat-number">85%</span>
              <span class="stat-label">Hoàn Thành</span>
            </div>
          </div>
          <div class="col-md-3 col-6">
            <div class="stat-box">
              <span class="stat-number">16</span>
              <span class="stat-label">Ngày Liên Tiếp</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Available Classes -->
      <div class="schedule-card">
        <h5 class="schedule-title">
          <i class="fas fa-list"></i>
          Lớp Học Có Sẵn
        </h5>
        <button class="btn-class">
          <i class="fas fa-dumbbell"></i>
          <span>Strength Training</span>
        </button>
        <button class="btn-class">
          <i class="fas fa-heart"></i>
          <span>Cardio Blast</span>
        </button>
        <button class="btn-class">
          <i class="fas fa-leaf"></i>
          <span>Yoga Flow</span>
        </button>
        <button class="btn-class" style="margin-bottom: 0">
          <i class="fas fa-fire"></i>
          <span>HIIT Workout</span>
        </button>
      </div>
    </div>
  </div>
</div>

<%@ include file="/views/common/footer.jsp" %>

// Thêm vào cuối file schedule.jsp
<script>
    async function loadSchedule() {
        try {
            const res = await fetch('${pageContext.request.contextPath}/api/member/schedule/history');
            if (res.ok) {
                const bookings = await res.json();
                renderCalendar(bookings);
            }
        } catch (e) {
            console.error("Error loading schedule", e);
        }
    }

    function renderCalendar(bookings) {
        // Logic để loop qua bookings và tạo các thẻ HTML .calendar-day tương ứng
        // Đây là phần bạn cần tự implement dựa trên giao diện mong muốn.
        console.log("Loaded bookings:", bookings);
    }

    loadSchedule();
</script>
