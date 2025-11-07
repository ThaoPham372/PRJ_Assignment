<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Quản lý tài khoản - GymFit</title>

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

      /* Reuse sidebar and layout styles from dashboard */
      .admin-container {
        display: flex;
        min-height: 100vh;
      }

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

      .btn-danger {
        background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
      }

      .btn-success {
        background: linear-gradient(135deg, #27ae60 0%, #229954 100%);
      }

      .content-area {
        padding: 30px 40px;
      }

      /* Filter and Actions Bar */
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
        align-items: center;
        flex-wrap: wrap;
      }

      .filter-select {
        padding: 10px 15px;
        border: 2px solid #e0e0e0;
        border-radius: 8px;
        font-size: 0.9rem;
        cursor: pointer;
        transition: all 0.3s ease;
        background: #fff;
      }

      .filter-select:focus {
        outline: none;
        border-color: var(--accent);
      }

      /* Table Styles */
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

      .table tbody tr:last-child td {
        border-bottom: none;
      }

      /* Badge */
      .badge {
        display: inline-block;
        padding: 5px 12px;
        border-radius: 20px;
        font-size: 0.8rem;
        font-weight: 600;
        text-transform: uppercase;
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

      /* Action Buttons */
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

      .btn-permissions {
        background: #f39c12;
      }

      .btn-permissions:hover {
        background: #d68910;
        transform: scale(1.1);
      }

      /* Modal */
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

        .content-area {
          padding: 20px;
        }

        .table-container {
          overflow-x: auto;
        }

        .actions-bar {
          flex-direction: column;
          align-items: stretch;
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
              class="sidebar-menu-link"
            >
              <i class="fas fa-user-circle"></i>
              <span>Profile của Admin</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/account_management.jsp"
              class="sidebar-menu-link active"
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
          <h1>
            <i class="fas fa-users-cog"></i> Quản lý tài khoản & Phân quyền
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

        <!-- Content Area -->
        <div class="content-area">
          <!-- Actions Bar -->
          <div class="actions-bar">
            <div class="filter-group">
              <select class="filter-select" id="roleFilter">
                <option value="all">Tất cả vai trò</option>
                <option value="admin">Admin</option>
                <option value="user">User</option>
                <option value="pt">PT (Personal Trainer)</option>
              </select>

              <select class="filter-select" id="statusFilter">
                <option value="all">Tất cả trạng thái</option>
                <option value="active">Đang hoạt động</option>
                <option value="inactive">Ngưng hoạt động</option>
              </select>
            </div>

            <button class="btn" onclick="openAddModal()">
              <i class="fas fa-plus"></i> Thêm tài khoản mới
            </button>
          </div>

          <!-- Table -->
          <div class="table-container">
            <table class="table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Họ và tên</th>
                  <th>Email</th>
                  <th>Username</th>
                  <th>Vai trò</th>
                  <th>Trạng thái</th>
                  <th>Thao tác</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>#001</td>
                  <td>Nguyễn Văn Admin</td>
                  <td>admin@gymfit.vn</td>
                  <td>admin</td>
                  <td><span class="badge badge-admin">Admin</span></td>
                  <td><span class="badge badge-user">Hoạt động</span></td>
                  <td>
                    <div class="action-buttons">
                      <button
                        class="btn-icon btn-edit"
                        onclick="openEditModal(1)"
                        title="Sửa"
                      >
                        <i class="fas fa-edit"></i>
                      </button>
                      <button
                        class="btn-icon btn-permissions"
                        onclick="openPermissionsModal(1)"
                        title="Phân quyền"
                      >
                        <i class="fas fa-key"></i>
                      </button>
                      <button
                        class="btn-icon btn-delete"
                        onclick="deleteAccount(1)"
                        title="Xóa"
                      >
                        <i class="fas fa-trash"></i>
                      </button>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td>#002</td>
                  <td>Trần Thị User</td>
                  <td>user@gymfit.vn</td>
                  <td>user01</td>
                  <td><span class="badge badge-user">User</span></td>
                  <td><span class="badge badge-user">Hoạt động</span></td>
                  <td>
                    <div class="action-buttons">
                      <button
                        class="btn-icon btn-edit"
                        onclick="openEditModal(2)"
                        title="Sửa"
                      >
                        <i class="fas fa-edit"></i>
                      </button>
                      <button
                        class="btn-icon btn-permissions"
                        onclick="openPermissionsModal(2)"
                        title="Phân quyền"
                      >
                        <i class="fas fa-key"></i>
                      </button>
                      <button
                        class="btn-icon btn-delete"
                        onclick="deleteAccount(2)"
                        title="Xóa"
                      >
                        <i class="fas fa-trash"></i>
                      </button>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td>#003</td>
                  <td>Lê Văn Trainer</td>
                  <td>trainer@gymfit.vn</td>
                  <td>trainer01</td>
                  <td><span class="badge badge-pt">PT</span></td>
                  <td><span class="badge badge-user">Hoạt động</span></td>
                  <td>
                    <div class="action-buttons">
                      <button
                        class="btn-icon btn-edit"
                        onclick="openEditModal(3)"
                        title="Sửa"
                      >
                        <i class="fas fa-edit"></i>
                      </button>
                      <button
                        class="btn-icon btn-permissions"
                        onclick="openPermissionsModal(3)"
                        title="Phân quyền"
                      >
                        <i class="fas fa-key"></i>
                      </button>
                      <button
                        class="btn-icon btn-delete"
                        onclick="deleteAccount(3)"
                        title="Xóa"
                      >
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

    <!-- Add/Edit Account Modal -->
    <div class="modal" id="accountModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3 class="modal-title" id="modalTitle">Thêm tài khoản mới</h3>
          <button class="modal-close" onclick="closeModal('accountModal')">
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
            <label class="form-label">Username</label>
            <input type="text" class="form-input" name="username" required />
          </div>

          <div class="form-group">
            <label class="form-label">Mật khẩu</label>
            <input
              type="password"
              class="form-input"
              name="password"
              required
            />
          </div>

          <div class="form-group">
            <label class="form-label">Vai trò</label>
            <select class="form-input" name="role" required>
              <option value="">-- Chọn vai trò --</option>
              <option value="admin">Admin</option>
              <option value="user">User</option>
              <option value="pt">PT (Personal Trainer)</option>
            </select>
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
              onclick="closeModal('accountModal')"
            >
              Hủy
            </button>
            <button type="submit" class="btn">Lưu</button>
          </div>
        </form>
      </div>
    </div>

    <!-- Permissions Modal -->
    <div class="modal" id="permissionsModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3 class="modal-title">Set quyền cho tài khoản</h3>
          <button class="modal-close" onclick="closeModal('permissionsModal')">
            &times;
          </button>
        </div>
        <form action="#" method="post">
          <div class="form-group">
            <label class="form-label"
              >Tài khoản: <strong>Nguyễn Văn A</strong></label
            >
          </div>

          <div class="form-group">
            <label class="form-label">Vai trò mới</label>
            <select class="form-input" name="newRole" required>
              <option value="admin">Admin</option>
              <option value="user">User</option>
              <option value="pt">PT (Personal Trainer)</option>
            </select>
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
              onclick="closeModal('permissionsModal')"
            >
              Hủy
            </button>
            <button type="submit" class="btn">Cập nhật quyền</button>
          </div>
        </form>
      </div>
    </div>

    <script>
      function openAddModal() {
        document.getElementById('accountModal').classList.add('active');
        document.getElementById('modalTitle').textContent =
          'Thêm tài khoản mới';
      }

      function openEditModal(id) {
        document.getElementById('accountModal').classList.add('active');
        document.getElementById('modalTitle').textContent =
          'Chỉnh sửa tài khoản';
        // Load data for editing
        console.log('Edit account:', id);
      }

      function openPermissionsModal(id) {
        document.getElementById('permissionsModal').classList.add('active');
        console.log('Set permissions for account:', id);
      }

      function closeModal(modalId) {
        document.getElementById(modalId).classList.remove('active');
      }

      function deleteAccount(id) {
        if (confirm('Bạn có chắc chắn muốn xóa tài khoản này?')) {
          console.log('Delete account:', id);
          alert('Xóa tài khoản thành công!');
        }
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
