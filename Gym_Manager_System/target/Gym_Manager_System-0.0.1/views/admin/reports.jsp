<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Báo cáo & Thống kê - GymFit</title>

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

      /* Filter Bar */
      .filter-bar {
        background: #fff;
        padding: 20px;
        border-radius: 12px;
        box-shadow: 0 2px 10px var(--shadow);
        margin-bottom: 25px;
        display: flex;
        gap: 15px;
        flex-wrap: wrap;
        align-items: center;
      }

      .filter-select {
        padding: 10px 15px;
        border: 2px solid #e0e0e0;
        border-radius: 8px;
        font-size: 0.9rem;
        cursor: pointer;
      }

      /* Report Cards Grid */
      .reports-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
        gap: 25px;
        margin-bottom: 30px;
      }

      .report-card {
        background: #fff;
        border-radius: 15px;
        padding: 25px;
        box-shadow: 0 4px 15px var(--shadow);
      }

      .report-card-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 20px;
      }

      .report-card-title {
        font-size: 1.1rem;
        font-weight: 700;
        color: var(--primary);
      }

      .report-card-icon {
        width: 45px;
        height: 45px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 1.3rem;
        color: #fff;
      }

      .report-chart-placeholder {
        height: 200px;
        background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
        border-radius: 10px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #5a6c7d;
        margin-bottom: 15px;
      }

      .report-summary {
        display: flex;
        justify-content: space-between;
        padding-top: 15px;
        border-top: 2px solid #f0f0f0;
      }

      .summary-item {
        text-align: center;
      }

      .summary-label {
        font-size: 0.8rem;
        color: #5a6c7d;
        margin-bottom: 5px;
      }

      .summary-value {
        font-size: 1.3rem;
        font-weight: 700;
        color: var(--primary);
      }

      /* Large Chart Container */
      .large-chart {
        background: #fff;
        border-radius: 15px;
        padding: 25px;
        box-shadow: 0 4px 15px var(--shadow);
        margin-bottom: 25px;
      }

      .large-chart-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;
      }

      .large-chart-title {
        font-size: 1.3rem;
        font-weight: 700;
        color: var(--primary);
      }

      .large-chart-placeholder {
        height: 400px;
        background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
        border-radius: 10px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #5a6c7d;
        font-size: 1.1rem;
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
        .reports-grid {
          grid-template-columns: 1fr;
        }
        .filter-bar {
          flex-direction: column;
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
              <h4>Admin User</h4>
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
              <i class="fas fa-home"></i><span>Trang chủ</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/profile.jsp"
              class="sidebar-menu-link"
            >
              <i class="fas fa-user-circle"></i><span>Profile của Admin</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/account_management.jsp"
              class="sidebar-menu-link"
            >
              <i class="fas fa-users-cog"></i><span>Quản lý tài khoản</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/member_management.jsp"
              class="sidebar-menu-link"
            >
              <i class="fas fa-users"></i><span>Quản lý hội viên</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/service_schedule.jsp"
              class="sidebar-menu-link"
            >
              <i class="fas fa-calendar-alt"></i><span>Dịch vụ & Lịch tập</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/trainer_management.jsp"
              class="sidebar-menu-link"
            >
              <i class="fas fa-chalkboard-teacher"></i><span>Quản lý PT</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/order_management.jsp"
              class="sidebar-menu-link"
            >
              <i class="fas fa-box"></i><span>Quản lý đơn hàng</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/payment_finance.jsp"
              class="sidebar-menu-link"
            >
              <i class="fas fa-money-bill-wave"></i
              ><span>Thanh toán & Tài chính</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/reports.jsp"
              class="sidebar-menu-link active"
            >
              <i class="fas fa-chart-line"></i><span>Báo cáo & Thống kê</span>
            </a>
          </li>
        </ul>
      </aside>

      <!-- Main Content -->
      <main class="main-content">
        <div class="top-bar">
          <h1><i class="fas fa-chart-line"></i> Báo cáo & Thống kê</h1>
          <div class="top-bar-actions">
            <button class="btn">
              <i class="fas fa-download"></i> Xuất báo cáo
            </button>
            <a
              href="${pageContext.request.contextPath}/views/admin/dashboard.jsp"
              class="btn btn-outline"
            >
              <i class="fas fa-arrow-left"></i> Quay lại
            </a>
          </div>
        </div>

        <div class="content-area">
          <!-- Filter Bar -->
          <div class="filter-bar">
            <label style="font-weight: 600">Chọn khoảng thời gian:</label>
            <select class="filter-select">
              <option>7 ngày qua</option>
              <option>30 ngày qua</option>
              <option>3 tháng qua</option>
              <option>6 tháng qua</option>
              <option selected>12 tháng qua</option>
              <option>Tùy chỉnh</option>
            </select>

            <input type="date" class="filter-select" />
            <span>đến</span>
            <input type="date" class="filter-select" />

            <button class="btn"><i class="fas fa-filter"></i> Áp dụng</button>
          </div>

          <!-- Report Cards -->
          <div class="reports-grid">
            <!-- Members Report -->
            <div class="report-card">
              <div class="report-card-header">
                <h3 class="report-card-title">
                  Số lượng hội viên theo thời gian
                </h3>
                <div
                  class="report-card-icon"
                  style="
                    background: linear-gradient(
                      135deg,
                      #667eea 0%,
                      #764ba2 100%
                    );
                  "
                >
                  <i class="fas fa-users"></i>
                </div>
              </div>

              <div class="report-chart-placeholder">
                <i class="fas fa-chart-line"></i>
              </div>

              <div class="report-summary">
                <div class="summary-item">
                  <div class="summary-label">Tổng HV</div>
                  <div class="summary-value">1,245</div>
                </div>
                <div class="summary-item">
                  <div class="summary-label">HV mới</div>
                  <div class="summary-value">+125</div>
                </div>
                <div class="summary-item">
                  <div class="summary-label">Tăng trưởng</div>
                  <div class="summary-value">+18%</div>
                </div>
              </div>
            </div>

            <!-- Revenue Report -->
            <div class="report-card">
              <div class="report-card-header">
                <h3 class="report-card-title">
                  Doanh thu theo dịch vụ/gói tập
                </h3>
                <div
                  class="report-card-icon"
                  style="
                    background: linear-gradient(
                      135deg,
                      #11998e 0%,
                      #38ef7d 100%
                    );
                  "
                >
                  <i class="fas fa-dollar-sign"></i>
                </div>
              </div>

              <div class="report-chart-placeholder">
                <i class="fas fa-chart-pie"></i>
              </div>

              <div class="report-summary">
                <div class="summary-item">
                  <div class="summary-label">Basic</div>
                  <div class="summary-value">25%</div>
                </div>
                <div class="summary-item">
                  <div class="summary-label">Standard</div>
                  <div class="summary-value">35%</div>
                </div>
                <div class="summary-item">
                  <div class="summary-label">Premium</div>
                  <div class="summary-value">40%</div>
                </div>
              </div>
            </div>

            <!-- Check-in Report -->
            <div class="report-card">
              <div class="report-card-header">
                <h3 class="report-card-title">Tần suất check-in</h3>
                <div
                  class="report-card-icon"
                  style="background: var(--gradient-accent)"
                >
                  <i class="fas fa-calendar-check"></i>
                </div>
              </div>

              <div class="report-chart-placeholder">
                <i class="fas fa-chart-bar"></i>
              </div>

              <div class="report-summary">
                <div class="summary-item">
                  <div class="summary-label">Hôm nay</div>
                  <div class="summary-value">892</div>
                </div>
                <div class="summary-item">
                  <div class="summary-label">Trung bình</div>
                  <div class="summary-value">745</div>
                </div>
                <div class="summary-item">
                  <div class="summary-label">Cao nhất</div>
                  <div class="summary-value">1,024</div>
                </div>
              </div>
            </div>
          </div>

          <!-- Large Revenue Chart -->
          <div class="large-chart">
            <div class="large-chart-header">
              <h3 class="large-chart-title">
                Biểu đồ doanh thu và chi phí 12 tháng
              </h3>
              <select class="filter-select">
                <option>2025</option>
                <option>2024</option>
                <option>2023</option>
              </select>
            </div>
            <div class="large-chart-placeholder">
              <i class="fas fa-chart-area"></i> &nbsp; Biểu đồ chi tiết sẽ hiển
              thị ở đây (Doanh thu & Chi phí)
            </div>
          </div>

          <!-- Additional Stats Large Chart -->
          <div class="large-chart">
            <div class="large-chart-header">
              <h3 class="large-chart-title">
                Thống kê lớp training & PT sessions
              </h3>
              <button class="btn btn-outline">
                <i class="fas fa-sliders-h"></i> Tùy chỉnh
              </button>
            </div>
            <div class="large-chart-placeholder">
              <i class="fas fa-chart-column"></i> &nbsp; Biểu đồ lớp training và
              PT sessions
            </div>
          </div>
        </div>
      </main>
    </div>
  </body>
</html>
