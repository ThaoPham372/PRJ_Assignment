<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Quản lý thanh toán & Tài chính - GymFit</title>

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

      /* Revenue Cards */
      .revenue-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
        gap: 20px;
        margin-bottom: 30px;
      }

      .revenue-card {
        background: #fff;
        padding: 25px;
        border-radius: 15px;
        box-shadow: 0 4px 15px var(--shadow);
      }

      .revenue-card-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 15px;
      }

      .revenue-card-title {
        font-size: 0.95rem;
        color: #5a6c7d;
        font-weight: 600;
      }

      .revenue-card-icon {
        width: 45px;
        height: 45px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 1.3rem;
        color: #fff;
      }

      .revenue-amount {
        font-size: 2rem;
        font-weight: 700;
        color: var(--primary);
        margin-bottom: 10px;
      }

      .revenue-change {
        font-size: 0.85rem;
        display: flex;
        align-items: center;
        gap: 5px;
      }

      .revenue-change.up {
        color: #27ae60;
      }

      .revenue-change.down {
        color: #e74c3c;
      }

      /* Chart Container */
      .chart-container {
        background: #fff;
        padding: 25px;
        border-radius: 15px;
        box-shadow: 0 4px 15px var(--shadow);
        margin-bottom: 25px;
      }

      .chart-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;
      }

      .chart-title {
        font-size: 1.3rem;
        font-weight: 700;
        color: var(--primary);
      }

      .chart-placeholder {
        height: 300px;
        background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
        border-radius: 10px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #5a6c7d;
        font-size: 1.1rem;
      }

      /* Invoice Table */
      .table-container {
        background: #fff;
        border-radius: 12px;
        box-shadow: 0 2px 10px var(--shadow);
        overflow: hidden;
      }

      .table {
        width: 100%;
        border-collapse: collapse;
      }

      .table thead {
        background: var(--gradient-primary);
        color: #fff;
      }

      .table th {
        padding: 15px;
        text-align: left;
        font-weight: 600;
      }

      .table td {
        padding: 15px;
        border-bottom: 1px solid #e0e0e0;
      }

      .table tbody tr:hover {
        background: #f8f9fa;
      }

      .badge {
        display: inline-block;
        padding: 5px 12px;
        border-radius: 20px;
        font-size: 0.8rem;
        font-weight: 600;
      }

      .badge-paid {
        background: #27ae60;
        color: #fff;
      }

      .badge-pending {
        background: #f39c12;
        color: #fff;
      }

      .badge-overdue {
        background: #e74c3c;
        color: #fff;
      }

      /* Payment Methods */
      .payment-methods {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
        gap: 15px;
        margin-bottom: 25px;
      }

      .payment-method-card {
        background: #fff;
        padding: 20px;
        border-radius: 12px;
        box-shadow: 0 2px 10px var(--shadow);
        text-align: center;
      }

      .payment-method-icon {
        font-size: 2.5rem;
        margin-bottom: 10px;
      }

      .payment-method-name {
        font-weight: 600;
        color: var(--primary);
        margin-bottom: 5px;
      }

      .payment-method-count {
        font-size: 0.9rem;
        color: #5a6c7d;
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
        .revenue-grid {
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
              class="sidebar-menu-link active"
            >
              <i class="fas fa-money-bill-wave"></i
              ><span>Thanh toán & Tài chính</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/reports.jsp"
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
            <i class="fas fa-money-bill-wave"></i> Quản lý thanh toán & Tài
            chính
          </h1>
          <div class="top-bar-actions">
            <a
              href="${pageContext.request.contextPath}/views/admin/dashboard.jsp"
              class="btn btn-outline"
            >
              <i class="fas fa-arrow-left"></i> Quay lại
            </a>
          </div>
        </div>

        <div class="content-area">
          <!-- Revenue Cards -->
          <div class="revenue-grid">
            <div class="revenue-card">
              <div class="revenue-card-header">
                <span class="revenue-card-title">Doanh thu hôm nay</span>
                <div
                  class="revenue-card-icon"
                  style="
                    background: linear-gradient(
                      135deg,
                      #667eea 0%,
                      #764ba2 100%
                    );
                  "
                >
                  <i class="fas fa-dollar-sign"></i>
                </div>
              </div>
              <div class="revenue-amount">12.5M</div>
              <div class="revenue-change up">
                <i class="fas fa-arrow-up"></i>
                <span>+15% so với hôm qua</span>
              </div>
            </div>

            <div class="revenue-card">
              <div class="revenue-card-header">
                <span class="revenue-card-title">Doanh thu tháng này</span>
                <div
                  class="revenue-card-icon"
                  style="
                    background: linear-gradient(
                      135deg,
                      #11998e 0%,
                      #38ef7d 100%
                    );
                  "
                >
                  <i class="fas fa-chart-line"></i>
                </div>
              </div>
              <div class="revenue-amount">456M</div>
              <div class="revenue-change up">
                <i class="fas fa-arrow-up"></i>
                <span>+22% so với tháng trước</span>
              </div>
            </div>

            <div class="revenue-card">
              <div class="revenue-card-header">
                <span class="revenue-card-title">Doanh thu năm nay</span>
                <div
                  class="revenue-card-icon"
                  style="background: var(--gradient-accent)"
                >
                  <i class="fas fa-coins"></i>
                </div>
              </div>
              <div class="revenue-amount">2.8B</div>
              <div class="revenue-change up">
                <i class="fas fa-arrow-up"></i>
                <span>+18% so với năm trước</span>
              </div>
            </div>

            <div class="revenue-card">
              <div class="revenue-card-header">
                <span class="revenue-card-title">Hóa đơn chờ</span>
                <div
                  class="revenue-card-icon"
                  style="
                    background: linear-gradient(
                      135deg,
                      #f39c12 0%,
                      #d68910 100%
                    );
                  "
                >
                  <i class="fas fa-file-invoice"></i>
                </div>
              </div>
              <div class="revenue-amount">23</div>
              <div class="revenue-change down">
                <i class="fas fa-arrow-down"></i>
                <span>-5 so với hôm qua</span>
              </div>
            </div>
          </div>

          <!-- Revenue Chart -->
          <div class="chart-container">
            <div class="chart-header">
              <h3 class="chart-title">Biểu đồ doanh thu 12 tháng</h3>
              <select class="btn btn-outline" style="padding: 8px 15px">
                <option>2025</option>
                <option>2024</option>
                <option>2023</option>
              </select>
            </div>
            <div class="chart-placeholder">
              <i class="fas fa-chart-area"></i> &nbsp; Biểu đồ doanh thu sẽ hiển
              thị ở đây
            </div>
          </div>

          <!-- Payment Methods -->
          <h3 style="margin-bottom: 15px; color: var(--primary)">
            Phương thức thanh toán
          </h3>
          <div class="payment-methods">
            <div class="payment-method-card">
              <div class="payment-method-icon">💳</div>
              <div class="payment-method-name">Thẻ tín dụng/ghi nợ</div>
              <div class="payment-method-count">245 giao dịch</div>
            </div>

            <div class="payment-method-card">
              <div class="payment-method-icon">💵</div>
              <div class="payment-method-name">Tiền mặt</div>
              <div class="payment-method-count">156 giao dịch</div>
            </div>

            <div class="payment-method-card">
              <div class="payment-method-icon">📱</div>
              <div class="payment-method-name">Ví điện tử</div>
              <div class="payment-method-count">189 giao dịch</div>
            </div>

            <div class="payment-method-card">
              <div class="payment-method-icon">🏦</div>
              <div class="payment-method-name">Chuyển khoản</div>
              <div class="payment-method-count">98 giao dịch</div>
            </div>
          </div>

          <!-- Invoice Table -->
          <h3
            style="margin-bottom: 15px; margin-top: 25px; color: var(--primary)"
          >
            Hóa đơn gần đây
          </h3>
          <div class="table-container">
            <table class="table">
              <thead>
                <tr>
                  <th>Mã HĐ</th>
                  <th>Khách hàng</th>
                  <th>Loại dịch vụ</th>
                  <th>Số tiền</th>
                  <th>Phương thức</th>
                  <th>Ngày thanh toán</th>
                  <th>Trạng thái</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>#HD001</td>
                  <td>Nguyễn Văn A</td>
                  <td>Gói Premium 6 tháng</td>
                  <td>2.000.000đ</td>
                  <td>Thẻ</td>
                  <td>10/02/2025</td>
                  <td><span class="badge badge-paid">Đã thanh toán</span></td>
                </tr>
                <tr>
                  <td>#HD002</td>
                  <td>Trần Thị B</td>
                  <td>Gói Standard 3 tháng</td>
                  <td>1.200.000đ</td>
                  <td>Tiền mặt</td>
                  <td>11/02/2025</td>
                  <td><span class="badge badge-paid">Đã thanh toán</span></td>
                </tr>
                <tr>
                  <td>#HD003</td>
                  <td>Lê Văn C</td>
                  <td>Personal Training</td>
                  <td>500.000đ</td>
                  <td>Ví điện tử</td>
                  <td>12/02/2025</td>
                  <td><span class="badge badge-pending">Chờ xác nhận</span></td>
                </tr>
                <tr>
                  <td>#HD004</td>
                  <td>Phạm Thị D</td>
                  <td>Gói VIP 12 tháng</td>
                  <td>3.500.000đ</td>
                  <td>Chuyển khoản</td>
                  <td>05/02/2025</td>
                  <td><span class="badge badge-overdue">Quá hạn</span></td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </main>
    </div>
  </body>
</html>
