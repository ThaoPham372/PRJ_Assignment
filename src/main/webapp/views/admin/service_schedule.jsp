<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Quản lý dịch vụ & Lịch tập - GymFit</title>

    <link
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
      rel="stylesheet"
    />
    <link
      href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800;900&display=swap"
      rel="stylesheet"
    />

    <style>
      :root {
        --primary: #141a49;
        --accent: #ec8b5a;
        --text: #2c3e50;
        --shadow: rgba(0, 0, 0, 0.1);
        --gradient-primary: linear-gradient(135deg, #141a49 0%, #1e2a5c 100%);
        --gradient-accent: linear-gradient(135deg, #ec8b5a 0%, #d67a4f 100%);
      }

      * {
        box-sizing: border-box;
        margin: 0;
        padding: 0;
      }

      body {
        font-family: 'Inter', sans-serif;
        color: var(--text);
        background: #f6f6f8;
      }

      .admin-container {
        display: flex;
        min-height: 100vh;
      }

      /* Reuse sidebar from other pages */
      .sidebar {
        width: 280px;
        background: var(--gradient-primary);
        color: #fff;
        position: fixed;
        height: 100vh;
        overflow-y: auto;
        box-shadow: 4px 0 20px rgba(0, 0, 0, 0.15);
        z-index: 100;
      }

      .sidebar-header {
        padding: 30px 25px;
        border-bottom: 1px solid rgba(255, 255, 255, 0.1);
      }
      .sidebar-brand {
        font-size: 1.8rem;
        font-weight: 900;
        display: flex;
        align-items: center;
        gap: 10px;
        color: #fff;
        text-decoration: none;
      }

      .sidebar-user {
        margin-top: 15px;
        padding: 15px;
        background: rgba(255, 255, 255, 0.1);
        border-radius: 10px;
        display: flex;
        align-items: center;
        gap: 12px;
      }

      .sidebar-user-avatar {
        width: 45px;
        height: 45px;
        border-radius: 50%;
        background: var(--accent);
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 1.2rem;
      }

      .sidebar-user-info h4 {
        font-size: 0.95rem;
        margin-bottom: 3px;
      }
      .sidebar-user-info p {
        font-size: 0.75rem;
        opacity: 0.8;
      }

      .sidebar-menu {
        padding: 20px 0;
      }
      .sidebar-menu-item {
        list-style: none;
      }

      .sidebar-menu-link {
        display: flex;
        align-items: center;
        gap: 15px;
        padding: 15px 25px;
        color: #fff;
        text-decoration: none;
        font-weight: 500;
        font-size: 0.95rem;
        transition: all 0.3s ease;
        border-left: 3px solid transparent;
      }

      .sidebar-menu-link:hover,
      .sidebar-menu-link.active {
        background: rgba(255, 255, 255, 0.1);
        border-left-color: var(--accent);
        color: var(--accent);
      }

      .sidebar-menu-link i {
        font-size: 1.1rem;
        width: 20px;
      }

      .main-content {
        flex: 1;
        margin-left: 280px;
        background: #f6f6f8;
      }

      .top-bar {
        background: #fff;
        padding: 20px 40px;
        box-shadow: 0 2px 10px var(--shadow);
        display: flex;
        justify-content: space-between;
        align-items: center;
        position: sticky;
        top: 0;
        z-index: 90;
      }

      .top-bar h1 {
        font-size: 1.8rem;
        font-weight: 700;
        color: var(--primary);
      }
      .top-bar-actions {
        display: flex;
        gap: 15px;
      }

      .btn {
        background: var(--gradient-accent);
        color: #fff;
        border: none;
        border-radius: 50px;
        padding: 10px 20px;
        font-weight: 600;
        font-size: 0.9rem;
        cursor: pointer;
        transition: all 0.3s ease;
        box-shadow: 0 4px 15px rgba(236, 139, 94, 0.3);
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 8px;
      }

      .btn:hover {
        transform: translateY(-2px);
      }

      .btn-outline {
        background: transparent;
        border: 2px solid var(--primary);
        color: var(--primary);
        box-shadow: none;
      }

      .btn-outline:hover {
        background: var(--primary);
        color: #fff;
      }

      .content-area {
        padding: 30px 40px;
      }

      /* Tabs */
      .tabs {
        display: flex;
        gap: 5px;
        background: #fff;
        padding: 10px;
        border-radius: 12px;
        box-shadow: 0 2px 10px var(--shadow);
        margin-bottom: 25px;
      }

      .tab {
        flex: 1;
        padding: 12px 20px;
        text-align: center;
        background: transparent;
        border: none;
        border-radius: 8px;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s ease;
        color: var(--text);
      }

      .tab.active {
        background: var(--gradient-accent);
        color: #fff;
      }

      .tab-content {
        display: none;
      }

      .tab-content.active {
        display: block;
      }

      /* Calendar */
      .calendar-container {
        background: #fff;
        padding: 25px;
        border-radius: 12px;
        box-shadow: 0 2px 10px var(--shadow);
      }

      .calendar-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;
      }

      .calendar-header h3 {
        font-size: 1.3rem;
        color: var(--primary);
      }

      .calendar-nav {
        display: flex;
        gap: 10px;
      }

      .calendar-grid {
        display: grid;
        grid-template-columns: repeat(7, 1fr);
        gap: 10px;
      }

      .calendar-day {
        text-align: center;
        padding: 10px;
        border-radius: 8px;
        background: #f8f9fa;
        font-weight: 600;
        color: var(--text);
      }

      .calendar-cell {
        min-height: 100px;
        padding: 10px;
        border: 1px solid #e0e0e0;
        border-radius: 8px;
        background: #fff;
      }

      .calendar-date {
        font-weight: 600;
        margin-bottom: 5px;
        color: var(--primary);
      }

      .calendar-event {
        background: var(--gradient-accent);
        color: #fff;
        padding: 5px;
        margin: 3px 0;
        border-radius: 5px;
        font-size: 0.75rem;
        cursor: pointer;
      }

      /* Booking List */
      .booking-list {
        background: #fff;
        border-radius: 12px;
        box-shadow: 0 2px 10px var(--shadow);
      }

      .booking-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 20px;
        border-bottom: 1px solid #e0e0e0;
      }

      .booking-item:last-child {
        border-bottom: none;
      }

      .booking-info h4 {
        font-size: 1.1rem;
        color: var(--primary);
        margin-bottom: 5px;
      }

      .booking-info p {
        font-size: 0.9rem;
        color: var(--text);
      }

      .booking-actions {
        display: flex;
        gap: 10px;
      }

      .btn-small {
        padding: 8px 16px;
        font-size: 0.85rem;
      }

      @media (max-width: 768px) {
        .sidebar {
          width: 70px;
        }
        .sidebar-brand span,
        .sidebar-menu-link span,
        .sidebar-user-info {
          display: none;
        }
        .main-content {
          margin-left: 70px;
        }
        .calendar-grid {
          grid-template-columns: 1fr;
        }
      }
    </style>
  </head>
  <body>
    <div class="admin-container">
      <!-- Sidebar -->
      <aside class="sidebar">
        <div class="sidebar-header">
          <a
            href="${pageContext.request.contextPath}/admin/admin-home"
            class="sidebar-brand"
          >
            <i class="fas fa-dumbbell"></i>
            <span>GYMFIT</span>
          </a>

          <div class="sidebar-user">
            <div class="sidebar-user-avatar">
              <i class="fas fa-user-shield"></i>
            </div>
            <div class="sidebar-user-info">
              <h4>Admin User</h4>
              <p>Administrator</p>
            </div>
          </div>
        </div>

        <ul class="sidebar-menu">
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/admin/dashboard"
              class="sidebar-menu-link"
            >
              <i class="fas fa-home"></i><span>Trang chủ</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/admin/profile"
              class="sidebar-menu-link"
            >
              <i class="fas fa-user-circle"></i><span>Profile của Admin</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/admin/account-management"
              class="sidebar-menu-link"
            >
              <i class="fas fa-users-cog"></i><span>Quản lý tài khoản</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/admin/membership-management"
              class="sidebar-menu-link"
            >
              <i class="fas fa-users"></i><span>Quản lý hội viên</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/admin/service-schedule"
              class="sidebar-menu-link active"
            >
              <i class="fas fa-calendar-alt"></i><span>Dịch vụ & Lịch tập</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/admin/trainer-management"
              class="sidebar-menu-link"
            >
              <i class="fas fa-chalkboard-teacher"></i><span>Quản lý PT</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/admin/sales-management"
              class="sidebar-menu-link"
            >
              <i class="fas fa-box"></i><span>Quản lý đơn hàng</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/admin/payment-finance"
              class="sidebar-menu-link"
            >
              <i class="fas fa-money-bill-wave"></i
              ><span>Thanh toán & Tài chính</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/admin/reports"
              class="sidebar-menu-link"
            >
              <i class="fas fa-chart-line"></i><span>Báo cáo & Thống kê</span>
            </a>
          </li>
        </ul>
      </aside>

      <!-- Main Content -->
      <main class="main-content">
        <div class="top-bar">
          <h1>
            <i class="fas fa-calendar-alt"></i> Quản lý dịch vụ & Lịch tập
          </h1>
          <div class="top-bar-actions">
            <a
              href="${pageContext.request.contextPath}/admin/dashboard"
              class="btn btn-outline"
            >
              <i class="fas fa-arrow-left"></i> Quay lại
            </a>
          </div>
        </div>

        <div class="content-area">
          <!-- Tabs -->
          <div class="tabs">
            <button class="tab active" onclick="switchTab('booking')">
              <i class="fas fa-calendar-check"></i> Quản lý Booking
            </button>
            <button class="tab" onclick="switchTab('classes')">
              <i class="fas fa-users"></i> Lớp Training
            </button>
            <button class="tab" onclick="switchTab('schedule')">
              <i class="fas fa-calendar"></i> Lịch tập
            </button>
          </div>

          <!-- Booking Tab -->
          <div id="booking" class="tab-content active">
            <div style="margin-bottom: 15px">
              <button class="btn">
                <i class="fas fa-plus"></i> Tạo booking mới
              </button>
            </div>

            <div class="booking-list">
              <div class="booking-item">
                <div class="booking-info">
                  <h4>Personal Training - Nguyễn Văn A</h4>
                  <p>
                    <i class="fas fa-calendar"></i> 15/02/2025 - 10:00 |
                    <i class="fas fa-user-tie"></i> PT: Nguyễn Văn PT
                  </p>
                  <p>
                    <i class="fas fa-check-circle" style="color: #27ae60"></i>
                    Đã xác nhận
                  </p>
                </div>
                <div class="booking-actions">
                  <button class="btn btn-small" style="background: #3498db">
                    <i class="fas fa-edit"></i> Sửa
                  </button>
                  <button class="btn btn-small" style="background: #e74c3c">
                    <i class="fas fa-times"></i> Hủy
                  </button>
                </div>
              </div>

              <div class="booking-item">
                <div class="booking-info">
                  <h4>Group Yoga - Trần Thị B</h4>
                  <p>
                    <i class="fas fa-calendar"></i> 16/02/2025 - 07:00 |
                    <i class="fas fa-user-tie"></i> PT: Trần Thị Coach
                  </p>
                  <p>
                    <i class="fas fa-clock" style="color: #f39c12"></i> Đang chờ
                    xác nhận
                  </p>
                </div>
                <div class="booking-actions">
                  <button class="btn btn-small" style="background: #27ae60">
                    <i class="fas fa-check"></i> Xác nhận
                  </button>
                  <button class="btn btn-small" style="background: #e74c3c">
                    <i class="fas fa-times"></i> Từ chối
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- Classes Tab -->
          <div id="classes" class="tab-content">
            <div style="margin-bottom: 15px">
              <button class="btn">
                <i class="fas fa-plus"></i> Tạo lớp mới
              </button>
            </div>

            <div class="booking-list">
              <div class="booking-item">
                <div class="booking-info">
                  <h4>Yoga Morning Class</h4>
                  <p>
                    <i class="fas fa-users"></i> 15/25 học viên |
                    <i class="fas fa-clock"></i> Thứ 2, 4, 6 - 7:00
                  </p>
                  <p><i class="fas fa-user-tie"></i> PT: Trần Thị Coach</p>
                </div>
                <div class="booking-actions">
                  <button class="btn btn-small" style="background: #3498db">
                    <i class="fas fa-edit"></i> Sửa
                  </button>
                  <button class="btn btn-small" style="background: #f39c12">
                    <i class="fas fa-user-plus"></i> Phân công PT
                  </button>
                </div>
              </div>

              <div class="booking-item">
                <div class="booking-info">
                  <h4>Cardio Blast</h4>
                  <p>
                    <i class="fas fa-users"></i> 20/30 học viên |
                    <i class="fas fa-clock"></i> Thứ 3, 5 - 18:00
                  </p>
                  <p><i class="fas fa-user-tie"></i> PT: Lê Văn Trainer</p>
                </div>
                <div class="booking-actions">
                  <button class="btn btn-small" style="background: #3498db">
                    <i class="fas fa-edit"></i> Sửa
                  </button>
                  <button class="btn btn-small" style="background: #f39c12">
                    <i class="fas fa-user-plus"></i> Phân công PT
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- Schedule Tab -->
          <div id="schedule" class="tab-content">
            <div class="calendar-container">
              <div class="calendar-header">
                <h3>Tháng 2, 2025</h3>
                <div class="calendar-nav">
                  <button class="btn btn-small btn-outline">
                    <i class="fas fa-chevron-left"></i>
                  </button>
                  <button class="btn btn-small btn-outline">
                    <i class="fas fa-chevron-right"></i>
                  </button>
                </div>
              </div>

              <div class="calendar-grid">
                <div class="calendar-day">CN</div>
                <div class="calendar-day">T2</div>
                <div class="calendar-day">T3</div>
                <div class="calendar-day">T4</div>
                <div class="calendar-day">T5</div>
                <div class="calendar-day">T6</div>
                <div class="calendar-day">T7</div>

                <div class="calendar-cell">
                  <div class="calendar-date">1</div>
                </div>
                <div class="calendar-cell">
                  <div class="calendar-date">2</div>
                  <div class="calendar-event">Yoga - 7:00</div>
                </div>
                <div class="calendar-cell">
                  <div class="calendar-date">3</div>
                  <div class="calendar-event">Cardio - 18:00</div>
                </div>
                <div class="calendar-cell">
                  <div class="calendar-date">4</div>
                  <div class="calendar-event">Yoga - 7:00</div>
                </div>
                <div class="calendar-cell">
                  <div class="calendar-date">5</div>
                  <div class="calendar-event">Cardio - 18:00</div>
                </div>
                <div class="calendar-cell">
                  <div class="calendar-date">6</div>
                  <div class="calendar-event">Yoga - 7:00</div>
                </div>
                <div class="calendar-cell">
                  <div class="calendar-date">7</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>

    <script>
      function switchTab(tabName) {
        // Hide all tabs
        const tabs = document.querySelectorAll('.tab-content');
        tabs.forEach((tab) => tab.classList.remove('active'));

        // Remove active from all tab buttons
        const tabButtons = document.querySelectorAll('.tab');
        tabButtons.forEach((btn) => btn.classList.remove('active'));

        // Show selected tab
        document.getElementById(tabName).classList.add('active');

        // Activate button
        event.target.closest('.tab').classList.add('active');
      }
    </script>
  </body>
</html>
