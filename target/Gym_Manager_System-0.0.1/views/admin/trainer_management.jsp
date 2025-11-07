<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Quản lý huấn luyện viên - GymFit</title>

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
        --text-light: #5a6c7d;
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

      /* Sidebar - same as other pages */
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

      /* Main Content */
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
        align-items: center;
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
        box-shadow: 0 6px 20px rgba(236, 139, 94, 0.4);
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

      /* Trainer Cards Grid */
      .trainers-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
        gap: 25px;
      }

      .trainer-card {
        background: #fff;
        border-radius: 15px;
        padding: 25px;
        box-shadow: 0 4px 15px var(--shadow);
        transition: all 0.3s ease;
      }

      .trainer-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 6px 25px rgba(0, 0, 0, 0.15);
      }

      .trainer-header {
        display: flex;
        align-items: center;
        gap: 15px;
        margin-bottom: 20px;
      }

      .trainer-avatar {
        width: 70px;
        height: 70px;
        border-radius: 50%;
        background: var(--gradient-accent);
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 2rem;
        color: #fff;
      }

      .trainer-info h3 {
        font-size: 1.2rem;
        margin-bottom: 5px;
        color: var(--primary);
      }

      .trainer-info p {
        font-size: 0.85rem;
        color: var(--text-light);
      }

      .trainer-stats {
        display: flex;
        justify-content: space-between;
        margin: 15px 0;
        padding: 15px;
        background: #f8f9fa;
        border-radius: 10px;
      }

      .stat-item {
        text-align: center;
      }

      .stat-item strong {
        display: block;
        font-size: 1.3rem;
        color: var(--primary);
      }

      .stat-item span {
        font-size: 0.8rem;
        color: var(--text-light);
      }

      .trainer-schedule {
        margin-top: 15px;
      }

      .trainer-schedule h4 {
        font-size: 0.9rem;
        margin-bottom: 10px;
        color: var(--text);
      }

      .schedule-item {
        display: flex;
        justify-content: space-between;
        padding: 8px 0;
        border-bottom: 1px solid #e0e0e0;
        font-size: 0.85rem;
      }

      .trainer-actions {
        display: flex;
        gap: 10px;
        margin-top: 15px;
      }

      .btn-small {
        padding: 8px 16px;
        font-size: 0.85rem;
        flex: 1;
      }

      /* Actions Bar */
      .actions-bar {
        background: #fff;
        padding: 20px;
        border-radius: 12px;
        box-shadow: 0 2px 10px var(--shadow);
        margin-bottom: 25px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        flex-wrap: wrap;
        gap: 15px;
      }

      .filter-select {
        padding: 10px 15px;
        border: 2px solid #e0e0e0;
        border-radius: 8px;
        font-size: 0.9rem;
      }

      /* Responsive */
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
        .trainers-grid {
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
            <span>FITZ GYM</span>
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
              href="${pageContext.request.contextPath}/admin/member-management"
              class="sidebar-menu-link"
            >
              <i class="fas fa-users"></i><span>Quản lý hội viên</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/admin/service-schedule"
              class="sidebar-menu-link"
            >
              <i class="fas fa-calendar-alt"></i><span>Dịch vụ & Lịch tập</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/admin/trainer-management"
              class="sidebar-menu-link active"
            >
              <i class="fas fa-chalkboard-teacher"></i><span>Quản lý PT</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/admin/order-management"
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
            <i class="fas fa-chalkboard-teacher"></i> Quản lý huấn luyện viên
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
          <!-- Actions Bar -->
          <div class="actions-bar">
            <input
              type="text"
              class="filter-select"
              placeholder="Tìm kiếm PT..."
              style="width: 250px"
            />
            <button class="btn" onclick="alert('Thêm PT mới')">
              <i class="fas fa-user-plus"></i> Thêm PT mới
            </button>
          </div>

          <!-- Trainers Grid -->
          <div class="trainers-grid">
            <!-- Trainer 1 -->
            <div class="trainer-card">
              <div class="trainer-header">
                <div class="trainer-avatar">
                  <i class="fas fa-user-tie"></i>
                </div>
                <div class="trainer-info">
                  <h3>Nguyễn Văn PT</h3>
                  <p><i class="fas fa-envelope"></i> pt01@gymfit.vn</p>
                  <p><i class="fas fa-phone"></i> 0123456789</p>
                </div>
              </div>

              <div class="trainer-stats">
                <div class="stat-item">
                  <strong>45</strong>
                  <span>Học viên</span>
                </div>
                <div class="stat-item">
                  <strong>12</strong>
                  <span>Lớp/tuần</span>
                </div>
                <div class="stat-item">
                  <strong>4.8</strong>
                  <span>⭐ Rating</span>
                </div>
              </div>

              <div class="trainer-schedule">
                <h4><i class="fas fa-calendar-alt"></i> Lịch tuần này</h4>
                <div class="schedule-item">
                  <span>Thứ 2 - 7:00</span>
                  <span>Yoga Morning</span>
                </div>
                <div class="schedule-item">
                  <span>Thứ 4 - 18:00</span>
                  <span>Cardio Blast</span>
                </div>
                <div class="schedule-item">
                  <span>Thứ 6 - 19:00</span>
                  <span>Group Training</span>
                </div>
              </div>

              <div class="trainer-actions">
                <button class="btn btn-small" style="background: #3498db">
                  <i class="fas fa-edit"></i> Sửa
                </button>
                <button class="btn btn-small" style="background: #f39c12">
                  <i class="fas fa-calendar"></i> Lịch
                </button>
              </div>
            </div>

            <!-- Trainer 2 -->
            <div class="trainer-card">
              <div class="trainer-header">
                <div class="trainer-avatar">
                  <i class="fas fa-user-tie"></i>
                </div>
                <div class="trainer-info">
                  <h3>Trần Thị Coach</h3>
                  <p><i class="fas fa-envelope"></i> coach02@gymfit.vn</p>
                  <p><i class="fas fa-phone"></i> 0987654321</p>
                </div>
              </div>

              <div class="trainer-stats">
                <div class="stat-item">
                  <strong>38</strong>
                  <span>Học viên</span>
                </div>
                <div class="stat-item">
                  <strong>10</strong>
                  <span>Lớp/tuần</span>
                </div>
                <div class="stat-item">
                  <strong>4.9</strong>
                  <span>⭐ Rating</span>
                </div>
              </div>

              <div class="trainer-schedule">
                <h4><i class="fas fa-calendar-alt"></i> Lịch tuần này</h4>
                <div class="schedule-item">
                  <span>Thứ 3 - 6:00</span>
                  <span>Spin Class</span>
                </div>
                <div class="schedule-item">
                  <span>Thứ 5 - 17:00</span>
                  <span>Zumba Dance</span>
                </div>
              </div>

              <div class="trainer-actions">
                <button class="btn btn-small" style="background: #3498db">
                  <i class="fas fa-edit"></i> Sửa
                </button>
                <button class="btn btn-small" style="background: #f39c12">
                  <i class="fas fa-calendar"></i> Lịch
                </button>
              </div>
            </div>

            <!-- Trainer 3 -->
            <div class="trainer-card">
              <div class="trainer-header">
                <div class="trainer-avatar">
                  <i class="fas fa-user-tie"></i>
                </div>
                <div class="trainer-info">
                  <h3>Lê Văn Trainer</h3>
                  <p><i class="fas fa-envelope"></i> trainer03@gymfit.vn</p>
                  <p><i class="fas fa-phone"></i> 0369852147</p>
                </div>
              </div>

              <div class="trainer-stats">
                <div class="stat-item">
                  <strong>52</strong>
                  <span>Học viên</span>
                </div>
                <div class="stat-item">
                  <strong>15</strong>
                  <span>Lớp/tuần</span>
                </div>
                <div class="stat-item">
                  <strong>5.0</strong>
                  <span>⭐ Rating</span>
                </div>
              </div>

              <div class="trainer-schedule">
                <h4><i class="fas fa-calendar-alt"></i> Lịch tuần này</h4>
                <div class="schedule-item">
                  <span>Thứ 2-6 - 5:00</span>
                  <span>Boot Camp</span>
                </div>
                <div class="schedule-item">
                  <span>Thứ 7 - 8:00</span>
                  <span>CrossFit</span>
                </div>
              </div>

              <div class="trainer-actions">
                <button class="btn btn-small" style="background: #3498db">
                  <i class="fas fa-edit"></i> Sửa
                </button>
                <button class="btn btn-small" style="background: #f39c12">
                  <i class="fas fa-calendar"></i> Lịch
                </button>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  </body>
</html>
