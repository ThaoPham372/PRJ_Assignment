<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Admin Dashboard - GymFit</title>

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
      }

      /* Stats Cards */
      .stats-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        gap: 25px;
        margin-bottom: 30px;
      }

      .stat-card {
        background: #fff;
        padding: 25px;
        border-radius: 15px;
        box-shadow: 0 4px 15px var(--shadow);
        display: flex;
        align-items: center;
        gap: 20px;
        transition: all 0.3s ease;
      }

      .stat-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 6px 25px var(--shadow-hover);
      }

      .stat-icon {
        width: 60px;
        height: 60px;
        border-radius: 15px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 1.5rem;
        color: #fff;
      }

      .stat-icon.blue {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      .stat-icon.orange {
        background: var(--gradient-accent);
      }

      .stat-icon.green {
        background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
      }

      .stat-icon.purple {
        background: linear-gradient(135deg, #d299c2 0%, #fef9d7 100%);
      }

      .stat-info h3 {
        font-size: 2rem;
        font-weight: 700;
        color: var(--primary);
        margin-bottom: 5px;
      }

      .stat-info p {
        font-size: 0.9rem;
        color: var(--text-light);
      }

      /* Management Grid */
      .management-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
        gap: 25px;
      }

      .management-card {
        background: #fff;
        padding: 30px;
        border-radius: 15px;
        box-shadow: 0 4px 15px var(--shadow);
        transition: all 0.3s ease;
      }

      .management-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 6px 25px var(--shadow-hover);
      }

      .management-card-header {
        display: flex;
        align-items: center;
        gap: 15px;
        margin-bottom: 20px;
        padding-bottom: 15px;
        border-bottom: 2px solid #f0f0f0;
      }

      .management-card-icon {
        width: 50px;
        height: 50px;
        border-radius: 12px;
        background: var(--gradient-accent);
        display: flex;
        align-items: center;
        justify-content: center;
        color: #fff;
        font-size: 1.3rem;
      }

      .management-card-title {
        font-size: 1.2rem;
        font-weight: 700;
        color: var(--primary);
      }

      .management-card-description {
        color: var(--text-light);
        margin-bottom: 20px;
        font-size: 0.9rem;
        line-height: 1.6;
      }

      .management-card-actions {
        display: flex;
        gap: 10px;
        flex-wrap: wrap;
      }

      .btn-small {
        padding: 8px 16px;
        font-size: 0.85rem;
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

        .sidebar-header {
          padding: 20px 10px;
        }

        .sidebar-menu-link {
          justify-content: center;
          padding: 15px 10px;
        }

        .main-content {
          margin-left: 70px;
        }

        .top-bar {
          padding: 15px 20px;
        }

        .top-bar h1 {
          font-size: 1.3rem;
        }

        .content-area {
          padding: 20px;
        }

        .stats-grid,
        .management-grid {
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
              href="${pageContext.request.contextPath}/admin/dashboard"
              class="sidebar-menu-link active"
            >
              <i class="fas fa-home"></i>
              <span>Trang chủ</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/profile.jsp"
              class="sidebar-menu-link"
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
          <h1><i class="fas fa-home"></i> Dashboard Admin</h1>
          <div class="top-bar-actions">
            <a
              href="${pageContext.request.contextPath}/home.jsp"
              class="btn btn-outline"
            >
              <i class="fas fa-home"></i> Về trang chủ
            </a>
            <a
              href="${pageContext.request.contextPath}/views/login.jsp"
              class="btn"
            >
              <i class="fas fa-sign-out-alt"></i> Đăng xuất
            </a>
          </div>
        </div>

        <!-- Content Area -->
        <div class="content-area">
          <!-- Statistics Cards -->
          <div class="stats-grid">
            <div class="stat-card">
              <div class="stat-icon blue">
                <i class="fas fa-users"></i>
              </div>
              <div class="stat-info">
                <h3>1,245</h3>
                <p>Tổng số hội viên</p>
              </div>
            </div>

            <div class="stat-card">
              <div class="stat-icon orange">
                <i class="fas fa-dollar-sign"></i>
              </div>
              <div class="stat-info">
                <h3>456M</h3>
                <p>Doanh thu tháng này</p>
              </div>
            </div>

            <div class="stat-card">
              <div class="stat-icon green">
                <i class="fas fa-user-check"></i>
              </div>
              <div class="stat-info">
                <h3>892</h3>
                <p>Check-in hôm nay</p>
              </div>
            </div>

            <div class="stat-card">
              <div class="stat-icon purple">
                <i class="fas fa-calendar-check"></i>
              </div>
              <div class="stat-info">
                <h3>34</h3>
                <p>Lớp training hôm nay</p>
              </div>
            </div>
          </div>

          <!-- Management Cards -->
          <div class="management-grid">
            <!-- Profile Admin -->
            <div class="management-card">
              <div class="management-card-header">
                <div class="management-card-icon">
                  <i class="fas fa-user-circle"></i>
                </div>
                <h3 class="management-card-title">Profile của Admin</h3>
              </div>
              <p class="management-card-description">
                Chỉnh sửa thông tin cá nhân, email, số điện thoại và đổi mật
                khẩu của admin.
              </p>
              <div class="management-card-actions">
                <a
                  href="${pageContext.request.contextPath}/views/admin/profile.jsp"
                  class="btn btn-small"
                >
                  <i class="fas fa-edit"></i> Chỉnh sửa thông tin
                </a>
              </div>
            </div>

            <!-- Account Management -->
            <div class="management-card">
              <div class="management-card-header">
                <div class="management-card-icon">
                  <i class="fas fa-users-cog"></i>
                </div>
                <h3 class="management-card-title">Quản lý tài khoản</h3>
              </div>
              <p class="management-card-description">
                Quản lý danh sách tài khoản Admin, User, PT. Thêm, xóa, sửa và
                set quyền cho tài khoản.
              </p>
              <div class="management-card-actions">
                <a
                  href="${pageContext.request.contextPath}/views/admin/account_management.jsp"
                  class="btn btn-small"
                >
                  <i class="fas fa-list"></i> Xem danh sách
                </a>
              </div>
            </div>

            <!-- Member Management -->
            <div class="management-card">
              <div class="management-card-header">
                <div class="management-card-icon">
                  <i class="fas fa-users"></i>
                </div>
                <h3 class="management-card-title">Quản lý hội viên</h3>
              </div>
              <p class="management-card-description">
                Xem danh sách, thêm/sửa hội viên, quản lý gói tập, hạn sử dụng
                thẻ và thống kê số lượng.
              </p>
              <div class="management-card-actions">
                <a
                  href="${pageContext.request.contextPath}/views/admin/member_management.jsp"
                  class="btn btn-small"
                >
                  <i class="fas fa-list"></i> Quản lý
                </a>
              </div>
            </div>

            <!-- Service & Schedule -->
            <div class="management-card">
              <div class="management-card-header">
                <div class="management-card-icon">
                  <i class="fas fa-calendar-alt"></i>
                </div>
                <h3 class="management-card-title">Dịch vụ & Lịch tập</h3>
              </div>
              <p class="management-card-description">
                Quản lý booking, lớp training, phân công PT cho lớp và quản lý
                lịch tập hội viên.
              </p>
              <div class="management-card-actions">
                <a
                  href="${pageContext.request.contextPath}/views/admin/service_schedule.jsp"
                  class="btn btn-small"
                >
                  <i class="fas fa-calendar"></i> Xem lịch
                </a>
              </div>
            </div>

            <!-- Trainer Management -->
            <div class="management-card">
              <div class="management-card-header">
                <div class="management-card-icon">
                  <i class="fas fa-chalkboard-teacher"></i>
                </div>
                <h3 class="management-card-title">Quản lý huấn luyện viên</h3>
              </div>
              <p class="management-card-description">
                Xem danh sách PT, lịch làm việc và thống kê số lượng hội viên
                đang train.
              </p>
              <div class="management-card-actions">
                <a
                  href="${pageContext.request.contextPath}/views/admin/trainer_management.jsp"
                  class="btn btn-small"
                >
                  <i class="fas fa-list"></i> Xem danh sách
                </a>
              </div>
            </div>

            <!-- Order Management -->
            <div class="management-card">
              <div class="management-card-header">
                <div class="management-card-icon">
                  <i class="fas fa-box"></i>
                </div>
                <h3 class="management-card-title">Quản lý đơn hàng</h3>
              </div>
              <p class="management-card-description">
                Quản lý tồn kho, thêm/xóa sản phẩm, xem đơn hàng và thống kê
                doanh thu sản phẩm.
              </p>
              <div class="management-card-actions">
                <a
                  href="${pageContext.request.contextPath}/views/admin/order_management.jsp"
                  class="btn btn-small"
                >
                  <i class="fas fa-boxes"></i> Quản lý kho
                </a>
              </div>
            </div>

            <!-- Payment & Finance -->
            <div class="management-card">
              <div class="management-card-header">
                <div class="management-card-icon">
                  <i class="fas fa-money-bill-wave"></i>
                </div>
                <h3 class="management-card-title">Thanh toán & Tài chính</h3>
              </div>
              <p class="management-card-description">
                Quản lý hóa đơn, phương thức thanh toán và thống kê doanh thu
                theo thời gian.
              </p>
              <div class="management-card-actions">
                <a
                  href="${pageContext.request.contextPath}/views/admin/payment_finance.jsp"
                  class="btn btn-small"
                >
                  <i class="fas fa-file-invoice-dollar"></i> Xem chi tiết
                </a>
              </div>
            </div>

            <!-- Reports & Statistics -->
            <div class="management-card">
              <div class="management-card-header">
                <div class="management-card-icon">
                  <i class="fas fa-chart-line"></i>
                </div>
                <h3 class="management-card-title">Báo cáo & Thống kê</h3>
              </div>
              <p class="management-card-description">
                Số lượng hội viên theo thời gian, doanh thu theo dịch vụ/gói tập
                và tần suất check-in.
              </p>
              <div class="management-card-actions">
                <a
                  href="${pageContext.request.contextPath}/views/admin/reports.jsp"
                  class="btn btn-small"
                >
                  <i class="fas fa-chart-bar"></i> Xem báo cáo
                </a>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>

    <!-- JavaScript -->
    <script>
      // Active menu highlight
      document.addEventListener('DOMContentLoaded', function () {
        const currentPath = window.location.pathname;
        const menuLinks = document.querySelectorAll('.sidebar-menu-link');

        menuLinks.forEach((link) => {
          if (
            link.getAttribute('href').includes(currentPath.split('/').pop())
          ) {
            link.classList.add('active');
          } else {
            link.classList.remove('active');
          }
        });
      });
    </script>
  </body>
</html>
