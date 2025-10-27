<%@ page contentType="text/html;charset=UTF-8" language="java" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Profile Admin - GymFit</title>

    <!-- Font Awesome Icons -->
    <link
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
      rel="stylesheet"
    />

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
      href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800;900&display=swap"
      rel="stylesheet"
    />

    <style>
      :root {
        --primary: #141a49;
        --primary-light: #1e2a5c;
        --accent: #ec8b5a;
        --accent-hover: #d67a4f;
        --text: #2c3e50;
        --text-light: #5a6c7d;
        --muted: #f8f9fa;
        --card: #ffffff;
        --shadow: rgba(0, 0, 0, 0.1);
        --shadow-hover: rgba(0, 0, 0, 0.15);
        --gradient-primary: linear-gradient(135deg, #141a49 0%, #1e2a5c 100%);
        --gradient-accent: linear-gradient(135deg, #ec8b5a 0%, #d67a4f 100%);
      }

      * {
        box-sizing: border-box;
        margin: 0;
        padding: 0;
      }

      body {
        font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI',
          Roboto, sans-serif;
        color: var(--text);
        background: #f6f6f8;
        line-height: 1.6;
      }

      /* Admin Layout */
      .admin-container {
        display: flex;
        min-height: 100vh;
      }

      /* Sidebar */
      .sidebar {
        width: 280px;
        background: var(--gradient-primary);
        color: #fff;
        position: fixed;
        height: 100vh;
        overflow-y: auto;
        box-shadow: 4px 0 20px rgba(0, 0, 0, 0.15);
        z-index: 100;
        transition: all 0.3s ease;
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
        font-weight: 700;
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

      /* Top Bar */
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

      /* Content Area */
      .content-area {
        padding: 30px 40px;
        max-width: 1200px;
        margin: 0 auto;
      }

      /* Profile Card */
      .profile-card {
        background: #fff;
        border-radius: 15px;
        box-shadow: 0 4px 15px var(--shadow);
        overflow: hidden;
        margin-bottom: 30px;
      }

      .profile-header {
        background: var(--gradient-primary);
        padding: 40px;
        text-align: center;
        color: #fff;
      }

      .profile-avatar {
        width: 120px;
        height: 120px;
        border-radius: 50%;
        background: var(--accent);
        display: inline-flex;
        align-items: center;
        justify-content: center;
        font-size: 3rem;
        margin-bottom: 15px;
        border: 5px solid rgba(255, 255, 255, 0.3);
      }

      .profile-name {
        font-size: 2rem;
        font-weight: 700;
        margin-bottom: 5px;
      }

      .profile-role {
        font-size: 1.1rem;
        opacity: 0.9;
      }

      .profile-body {
        padding: 40px;
      }

      .section-title {
        font-size: 1.5rem;
        font-weight: 700;
        color: var(--primary);
        margin-bottom: 25px;
        display: flex;
        align-items: center;
        gap: 10px;
      }

      .section-title i {
        color: var(--accent);
      }

      /* Form Styles */
      .form-grid {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 20px;
        margin-bottom: 30px;
      }

      .form-group {
        display: flex;
        flex-direction: column;
      }

      .form-group.full-width {
        grid-column: 1 / -1;
      }

      .form-label {
        font-weight: 600;
        color: var(--text);
        margin-bottom: 8px;
        font-size: 0.95rem;
      }

      .form-input {
        padding: 12px 15px;
        border: 2px solid #e0e0e0;
        border-radius: 8px;
        font-size: 1rem;
        transition: all 0.3s ease;
        font-family: inherit;
      }

      .form-input:focus {
        outline: none;
        border-color: var(--accent);
        box-shadow: 0 0 0 3px rgba(236, 139, 94, 0.1);
      }

      .form-input:disabled {
        background: #f5f5f5;
        cursor: not-allowed;
      }

      .form-actions {
        display: flex;
        gap: 15px;
        justify-content: flex-end;
      }

      /* Password Section */
      .password-section {
        background: #f8f9fa;
        padding: 30px;
        border-radius: 12px;
        margin-top: 30px;
      }

      /* Alert */
      .alert {
        padding: 15px 20px;
        border-radius: 10px;
        margin-bottom: 20px;
        display: flex;
        align-items: center;
        gap: 10px;
      }

      .alert-success {
        background: #d4edda;
        color: #155724;
        border: 1px solid #c3e6cb;
      }

      .alert-danger {
        background: #f8d7da;
        color: #721c24;
        border: 1px solid #f5c6cb;
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

        .form-grid {
          grid-template-columns: 1fr;
        }

        .content-area {
          padding: 20px;
        }

        .profile-body {
          padding: 25px;
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
            href="${pageContext.request.contextPath}/views/admin/admin_home.jsp"
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
              <h4>
                ${sessionScope.user != null ? sessionScope.user.username :
                'Admin User'}
              </h4>
              <p>Administrator</p>
            </div>
          </div>
        </div>

        <ul class="sidebar-menu">
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/dashboard.jsp"
              class="sidebar-menu-link"
            >
              <i class="fas fa-home"></i>
              <span>Trang chủ</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/profile.jsp"
              class="sidebar-menu-link active"
            >
              <i class="fas fa-user-circle"></i>
              <span>Profile của Admin</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/account_management.jsp"
              class="sidebar-menu-link"
            >
              <i class="fas fa-users-cog"></i>
              <span>Quản lý tài khoản</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/member_management.jsp"
              class="sidebar-menu-link"
            >
              <i class="fas fa-users"></i>
              <span>Quản lý hội viên</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/service_schedule.jsp"
              class="sidebar-menu-link"
            >
              <i class="fas fa-calendar-alt"></i>
              <span>Dịch vụ & Lịch tập</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/trainer_management.jsp"
              class="sidebar-menu-link"
            >
              <i class="fas fa-chalkboard-teacher"></i>
              <span>Quản lý PT</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/order_management.jsp"
              class="sidebar-menu-link"
            >
              <i class="fas fa-box"></i>
              <span>Quản lý đơn hàng</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/payment_finance.jsp"
              class="sidebar-menu-link"
            >
              <i class="fas fa-money-bill-wave"></i>
              <span>Thanh toán & Tài chính</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/reports.jsp"
              class="sidebar-menu-link"
            >
              <i class="fas fa-chart-line"></i>
              <span>Báo cáo & Thống kê</span>
            </a>
          </li>
        </ul>
      </aside>

      <!-- Main Content -->
      <main class="main-content">
        <!-- Top Bar -->
        <div class="top-bar">
          <h1><i class="fas fa-user-circle"></i> Profile của Admin</h1>
          <div class="top-bar-actions">
            <a
              href="${pageContext.request.contextPath}/views/admin/dashboard.jsp"
              class="btn btn-outline"
            >
              <i class="fas fa-arrow-left"></i> Quay lại
            </a>
          </div>
        </div>

        <!-- Content Area -->
        <div class="content-area">
          <!-- Profile Card -->
          <div class="profile-card">
            <div class="profile-header">
              <div class="profile-avatar">
                <i class="fas fa-user-shield"></i>
              </div>
              <h2 class="profile-name">
                ${sessionScope.user != null ? sessionScope.user.username :
                'Admin User'}
              </h2>
              <p class="profile-role">Administrator</p>
            </div>

            <div class="profile-body">
              <!-- Edit Information Form -->
              <h3 class="section-title">
                <i class="fas fa-edit"></i>
                Chỉnh sửa thông tin cá nhân
              </h3>

              <form action="#" method="post">
                <div class="form-grid">
                  <div class="form-group">
                    <label class="form-label">Họ và tên</label>
                    <input
                      type="text"
                      class="form-input"
                      name="username"
                      value="Nguyễn Văn Admin"
                      required
                    />
                  </div>

                  <div class="form-group">
                    <label class="form-label">Email</label>
                    <input
                      type="email"
                      class="form-input"
                      name="email"
                      value="admin@gymfit.vn"
                      required
                    />
                  </div>

                  <div class="form-group">
                    <label class="form-label">Số điện thoại</label>
                    <input
                      type="tel"
                      class="form-input"
                      name="phone"
                      value="0123456789"
                      required
                    />
                  </div>

                  <div class="form-group">
                    <label class="form-label">Ngày sinh</label>
                    <input
                      type="date"
                      class="form-input"
                      name="birthday"
                      value="1990-01-15"
                    />
                  </div>

                  <div class="form-group full-width">
                    <label class="form-label">Địa chỉ</label>
                    <input
                      type="text"
                      class="form-input"
                      name="address"
                      value="123 Nguyễn Văn Linh, Đà Nẵng"
                    />
                  </div>

                  <div class="form-group">
                    <label class="form-label">Username</label>
                    <input
                      type="text"
                      class="form-input"
                      name="username"
                      value="admin"
                      disabled
                    />
                  </div>

                  <div class="form-group">
                    <label class="form-label">Vai trò</label>
                    <input
                      type="text"
                      class="form-input"
                      value="Administrator"
                      disabled
                    />
                  </div>
                </div>

                <div class="form-actions">
                  <button type="reset" class="btn btn-outline">
                    <i class="fas fa-times"></i> Hủy
                  </button>
                  <button type="submit" class="btn">
                    <i class="fas fa-save"></i> Lưu thay đổi
                  </button>
                </div>
              </form>

              <!-- Change Password Section -->
              <div class="password-section">
                <h3 class="section-title">
                  <i class="fas fa-key"></i>
                  Đổi mật khẩu
                </h3>

                <form action="#" method="post">
                  <div class="form-grid">
                    <div class="form-group full-width">
                      <label class="form-label">Mật khẩu hiện tại</label>
                      <input
                        type="password"
                        class="form-input"
                        name="currentPassword"
                        required
                      />
                    </div>

                    <div class="form-group">
                      <label class="form-label">Mật khẩu mới</label>
                      <input
                        type="password"
                        class="form-input"
                        name="newPassword"
                        required
                      />
                    </div>

                    <div class="form-group">
                      <label class="form-label">Xác nhận mật khẩu mới</label>
                      <input
                        type="password"
                        class="form-input"
                        name="confirmPassword"
                        required
                      />
                    </div>
                  </div>

                  <div class="form-actions">
                    <button type="reset" class="btn btn-outline">
                      <i class="fas fa-times"></i> Hủy
                    </button>
                    <button type="submit" class="btn">
                      <i class="fas fa-lock"></i> Đổi mật khẩu
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  </body>
</html>
