<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Quản lý hội viên - GymFit</title>

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
        font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI',
          Roboto, sans-serif;
        color: var(--text);
        background: #f6f6f8;
        line-height: 1.6;
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

      .filter-group {
        display: flex;
        gap: 10px;
        flex-wrap: wrap;
      }

      .filter-select {
        padding: 10px 15px;
        border: 2px solid #e0e0e0;
        border-radius: 8px;
        font-size: 0.9rem;
      }

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
        font-size: 0.9rem;
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

      .badge-admin {
        background: #667eea;
        color: #fff;
      }

      .badge-user {
        background: #11998e;
        color: #fff;
      }

      .badge-pt {
        background: var(--accent);
        color: #fff;
      }

      .action-buttons {
        display: flex;
        gap: 8px;
      }

      .btn-icon {
        width: 35px;
        height: 35px;
        border-radius: 8px;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        transition: all 0.3s ease;
        border: none;
        color: #fff;
      }

      .btn-edit {
        background: #3498db;
      }

      .btn-edit:hover {
        background: #2980b9;
        transform: scale(1.1);
      }

      .btn-delete {
        background: #e74c3c;
      }

      .btn-delete:hover {
        background: #c0392b;
        transform: scale(1.1);
      }

      .modal {
        display: none;
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.5);
        z-index: 1000;
        align-items: center;
        justify-content: center;
      }

      .modal.active {
        display: flex;
      }

      .modal-content {
        background: #fff;
        border-radius: 15px;
        padding: 30px;
        max-width: 600px;
        width: 90%;
        max-height: 90vh;
        overflow-y: auto;
      }

      .modal-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;
      }

      .modal-title {
        font-size: 1.5rem;
        font-weight: 700;
        color: var(--primary);
      }

      .modal-close {
        background: none;
        border: none;
        font-size: 1.5rem;
        cursor: pointer;
        color: var(--text-light);
      }

      .form-group {
        margin-bottom: 15px;
      }

      .form-label {
        display: block;
        font-weight: 600;
        margin-bottom: 8px;
        color: var(--text);
      }

      .form-input {
        width: 100%;
        padding: 12px 15px;
        border: 2px solid #e0e0e0;
        border-radius: 8px;
        font-size: 1rem;
        transition: all 0.3s ease;
      }

      .form-input:focus {
        outline: none;
        border-color: var(--accent);
        box-shadow: 0 0 0 3px rgba(236, 139, 94, 0.1);
      }

      .btn-small {
        padding: 8px 16px;
        font-size: 0.85rem;
      }
      /* Stats Cards */
      .stats-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
        gap: 20px;
        margin-bottom: 25px;
      }

      .stat-card {
        background: #fff;
        padding: 20px;
        border-radius: 12px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        display: flex;
        align-items: center;
        gap: 15px;
      }

      .stat-icon {
        width: 50px;
        height: 50px;
        border-radius: 10px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 1.3rem;
        color: #fff;
      }

      .stat-icon.blue {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      .stat-icon.green {
        background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
      }

      .stat-icon.orange {
        background: linear-gradient(135deg, #ec8b5a 0%, #d67a4f 100%);
      }

      .stat-info h3 {
        font-size: 1.8rem;
        font-weight: 700;
        color: #141a49;
      }

      .stat-info p {
        font-size: 0.85rem;
        color: #5a6c7d;
      }

      /* Badge for membership status */
      .badge-active {
        background: #27ae60;
        color: #fff;
      }

      .badge-expired {
        background: #e74c3c;
        color: #fff;
      }

      .badge-expiring {
        background: #f39c12;
        color: #fff;
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
              href="${pageContext.request.contextPath}/admin/account-management"
              class="sidebar-menu-link"
            >
              <i class="fas fa-users-cog"></i>
              <span>Quản lý tài khoản</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/member_management.jsp"
              class="sidebar-menu-link active"
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
          <h1><i class="fas fa-users"></i> Quản lý hội viên</h1>
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
          <!-- Statistics -->
          <div class="stats-grid">
            <div class="stat-card">
              <div class="stat-icon blue">
                <i class="fas fa-users"></i>
              </div>
              <div class="stat-info">
                <h3>1,245</h3>
                <p>Tổng hội viên</p>
              </div>
            </div>

            <div class="stat-card">
              <div class="stat-icon green">
                <i class="fas fa-user-check"></i>
              </div>
              <div class="stat-info">
                <h3>1,102</h3>
                <p>Đang hoạt động</p>
              </div>
            </div>

            <div class="stat-card">
              <div class="stat-icon orange">
                <i class="fas fa-user-clock"></i>
              </div>
              <div class="stat-info">
                <h3>87</h3>
                <p>Sắp hết hạn</p>
              </div>
            </div>

            <div class="stat-card">
              <div
                class="stat-icon"
                style="
                  background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
                "
              >
                <i class="fas fa-user-times"></i>
              </div>
              <div class="stat-info">
                <h3>56</h3>
                <p>Đã hết hạn</p>
              </div>
            </div>
          </div>

          <!-- Actions Bar -->
          <div class="actions-bar">
            <div class="filter-group">
              <input
                type="text"
                class="filter-select"
                placeholder="Tìm kiếm theo tên, email..."
                style="width: 250px"
              />

              <select class="filter-select">
                <option value="all">Tất cả trạng thái</option>
                <option value="active">Đang hoạt động</option>
                <option value="expiring">Sắp hết hạn</option>
                <option value="expired">Đã hết hạn</option>
              </select>

              <select class="filter-select">
                <option value="all">Tất cả gói tập</option>
                <option value="basic">Gói Basic</option>
                <option value="standard">Gói Standard</option>
                <option value="premium">Gói Premium</option>
                <option value="vip">Gói VIP</option>
              </select>
            </div>

            <button class="btn" onclick="openAddMemberModal()">
              <i class="fas fa-user-plus"></i> Thêm hội viên mới
            </button>
          </div>

          <!-- Members Table -->
          <div class="table-container">
            <table class="table">
              <thead>
                <tr>
                  <th>Mã HV</th>
                  <th>Họ và tên</th>
                  <th>Email / SĐT</th>
                  <th>Gói tập</th>
                  <th>Ngày đăng ký</th>
                  <th>Ngày hết hạn</th>
                  <th>Trạng thái</th>
                  <th>Thao tác</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>#HV001</td>
                  <td>Nguyễn Văn A</td>
                  <td>nguyenvana@gmail.com<br /><small>0123456789</small></td>
                  <td><span class="badge badge-pt">Premium</span></td>
                  <td>01/01/2025</td>
                  <td>01/04/2025</td>
                  <td><span class="badge badge-active">Hoạt động</span></td>
                  <td>
                    <div class="action-buttons">
                      <button class="btn-icon btn-edit" title="Sửa thông tin">
                        <i class="fas fa-edit"></i>
                      </button>
                      <button
                        class="btn-icon"
                        style="background: #9b59b6"
                        title="Gia hạn"
                      >
                        <i class="fas fa-calendar-plus"></i>
                      </button>
                      <button class="btn-icon btn-delete" title="Xóa">
                        <i class="fas fa-trash"></i>
                      </button>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td>#HV002</td>
                  <td>Trần Thị B</td>
                  <td>tranthib@gmail.com<br /><small>0987654321</small></td>
                  <td><span class="badge badge-user">Standard</span></td>
                  <td>15/12/2024</td>
                  <td>15/03/2025</td>
                  <td><span class="badge badge-active">Hoạt động</span></td>
                  <td>
                    <div class="action-buttons">
                      <button class="btn-icon btn-edit" title="Sửa thông tin">
                        <i class="fas fa-edit"></i>
                      </button>
                      <button
                        class="btn-icon"
                        style="background: #9b59b6"
                        title="Gia hạn"
                      >
                        <i class="fas fa-calendar-plus"></i>
                      </button>
                      <button class="btn-icon btn-delete" title="Xóa">
                        <i class="fas fa-trash"></i>
                      </button>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td>#HV003</td>
                  <td>Lê Văn C</td>
                  <td>levanc@gmail.com<br /><small>0369852147</small></td>
                  <td><span class="badge badge-admin">VIP</span></td>
                  <td>10/10/2024</td>
                  <td>20/02/2025</td>
                  <td><span class="badge badge-expiring">Sắp hết hạn</span></td>
                  <td>
                    <div class="action-buttons">
                      <button class="btn-icon btn-edit" title="Sửa thông tin">
                        <i class="fas fa-edit"></i>
                      </button>
                      <button
                        class="btn-icon"
                        style="background: #9b59b6"
                        title="Gia hạn"
                      >
                        <i class="fas fa-calendar-plus"></i>
                      </button>
                      <button class="btn-icon btn-delete" title="Xóa">
                        <i class="fas fa-trash"></i>
                      </button>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td>#HV004</td>
                  <td>Phạm Thị D</td>
                  <td>phamthid@gmail.com<br /><small>0258963147</small></td>
                  <td><span class="badge badge-user">Basic</span></td>
                  <td>05/09/2024</td>
                  <td>05/01/2025</td>
                  <td><span class="badge badge-expired">Hết hạn</span></td>
                  <td>
                    <div class="action-buttons">
                      <button class="btn-icon btn-edit" title="Sửa thông tin">
                        <i class="fas fa-edit"></i>
                      </button>
                      <button
                        class="btn-icon"
                        style="background: #9b59b6"
                        title="Gia hạn"
                      >
                        <i class="fas fa-calendar-plus"></i>
                      </button>
                      <button class="btn-icon btn-delete" title="Xóa">
                        <i class="fas fa-trash"></i>
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </main>
    </div>

    <!-- Add Member Modal -->
    <div class="modal" id="addMemberModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3 class="modal-title">Thêm hội viên mới</h3>
          <button class="modal-close" onclick="closeModal('addMemberModal')">
            &times;
          </button>
        </div>
        <form action="#" method="post">
          <div class="form-group">
            <label class="form-label">Họ và tên</label>
            <input type="text" class="form-input" name="username" required />
          </div>

          <div class="form-group">
            <label class="form-label">Email</label>
            <input type="email" class="form-input" name="email" required />
          </div>

          <div class="form-group">
            <label class="form-label">Số điện thoại</label>
            <input type="tel" class="form-input" name="phone" required />
          </div>

          <div class="form-group">
            <label class="form-label">Gói tập</label>
            <select class="form-input" name="package" required>
              <option value="">-- Chọn gói tập --</option>
              <option value="basic">Gói Basic (1 tháng - 500K)</option>
              <option value="standard">Gói Standard (3 tháng - 1.2M)</option>
              <option value="premium">Gói Premium (6 tháng - 2M)</option>
              <option value="vip">Gói VIP (12 tháng - 3.5M)</option>
            </select>
          </div>

          <div class="form-group">
            <label class="form-label">Ngày bắt đầu</label>
            <input type="date" class="form-input" name="startDate" required />
          </div>

          <div
            style="
              display: flex;
              gap: 10px;
              justify-content: flex-end;
              margin-top: 20px;
            "
          >
            <button
              type="button"
              class="btn btn-outline"
              onclick="closeModal('addMemberModal')"
            >
              Hủy
            </button>
            <button type="submit" class="btn">Thêm hội viên</button>
          </div>
        </form>
      </div>
    </div>

    <script>
      function openAddMemberModal() {
        document.getElementById('addMemberModal').classList.add('active');
      }

      function closeModal(modalId) {
        document.getElementById(modalId).classList.remove('active');
      }

      // Close modal when clicking outside
      window.onclick = function (event) {
        if (event.target.classList.contains('modal')) {
          event.target.classList.remove('active');
        }
      };
    </script>
  </body>
</html>
