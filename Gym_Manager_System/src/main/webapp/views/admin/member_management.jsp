<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

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
              href="${pageContext.request.contextPath}/admin/users"
              class="sidebar-menu-link"
            >
              <i class="fas fa-users-cog"></i>
              <span>Quản lý tài khoản</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/admin/products"
              class="sidebar-menu-link"
            >
              <i class="fas fa-box"></i>
              <span>Quản lý sản phẩm</span>
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
                <h3><fmt:formatNumber value="${totalMembers != null ? totalMembers : 0}" groupingUsed="true"/></h3>
                <p>Tổng hội viên</p>
              </div>
            </div>

            <div class="stat-card">
              <div class="stat-icon green">
                <i class="fas fa-user-check"></i>
              </div>
              <div class="stat-info">
                <h3><fmt:formatNumber value="${totalActiveMembers != null ? totalActiveMembers : 0}" groupingUsed="true"/></h3>
                <p>Đang hoạt động</p>
              </div>
            </div>

            <div class="stat-card">
              <div
                class="stat-icon"
                style="
                  background: linear-gradient(135deg, #f39c12 0%, #e67e22 100%);
                "
              >
                <i class="fas fa-user-slash"></i>
              </div>
              <div class="stat-info">
                <h3><fmt:formatNumber value="${totalSuspendedMembers != null ? totalSuspendedMembers : 0}" groupingUsed="true"/></h3>
                <p>Tạm ngưng</p>
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
                id="searchInput"
              />

              <select class="filter-select" id="statusFilter">
                <option value="all">Tất cả trạng thái</option>
                <option value="ACTIVE">Đang hoạt động</option>
                <option value="SUSPENDED">Tạm ngưng (SUSPENDED)</option>
                <option value="EXPIRED">Tạm ngưng (EXPIRED)</option>
              </select>
            </div>
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
                <c:forEach var="membership" items="${memberships}">
                  <tr>
                    <td>#HV${membership.membershipId}</td>
                    <td>${membership.user != null ? membership.user.name : 'N/A'}</td>
                    <td>
                      ${membership.user != null ? membership.user.email : 'N/A'}
                      <c:if test="${membership.user != null && not empty membership.user.phone}">
                        <br /><small>${membership.user.phone}</small>
                      </c:if>
                    </td>
                    <td>
                      <span class="badge badge-pt">${membership.packageName != null ? membership.packageName : 'N/A'}</span>
                    </td>
                    <td>
                      <c:choose>
                        <c:when test="${membership.startDate != null}">
                          <%
                            com.gym.model.membership.Membership mem = (com.gym.model.membership.Membership) pageContext.getAttribute("membership");
                            if (mem != null && mem.getStartDate() != null) {
                              java.time.LocalDate startDate = mem.getStartDate();
                              out.print(String.format("%02d/%02d/%04d", startDate.getDayOfMonth(), startDate.getMonthValue(), startDate.getYear()));
                            }
                          %>
                        </c:when>
                        <c:otherwise>N/A</c:otherwise>
                      </c:choose>
                    </td>
                    <td>
                      <c:choose>
                        <c:when test="${membership.endDate != null}">
                          <%
                            com.gym.model.membership.Membership mem2 = (com.gym.model.membership.Membership) pageContext.getAttribute("membership");
                            if (mem2 != null && mem2.getEndDate() != null) {
                              java.time.LocalDate endDate = mem2.getEndDate();
                              out.print(String.format("%02d/%02d/%04d", endDate.getDayOfMonth(), endDate.getMonthValue(), endDate.getYear()));
                            }
                          %>
                        </c:when>
                        <c:otherwise>N/A</c:otherwise>
                      </c:choose>
                    </td>
                    <td>
                      <c:choose>
                        <c:when test="${membership.status == 'ACTIVE'}">
                          <span class="badge badge-active">Hoạt động</span>
                        </c:when>
                        <c:when test="${membership.status == 'SUSPENDED' || membership.status == 'EXPIRED'}">
                          <span class="badge badge-expired">Tạm ngưng</span>
                        </c:when>
                        <c:otherwise>
                          <span class="badge">${membership.status}</span>
                        </c:otherwise>
                      </c:choose>
                    </td>
                    <td>
                      <div class="action-buttons">
                        <button class="btn-icon btn-edit" title="Xem chi tiết">
                          <i class="fas fa-eye"></i>
                        </button>
                      </div>
                    </td>
                  </tr>
                </c:forEach>
                <c:if test="${empty memberships}">
                  <tr>
                    <td colspan="8" style="text-align: center; padding: 40px; color: #999;">
                      Chưa có hội viên nào
                    </td>
                  </tr>
                </c:if>
              </tbody>
            </table>
          </div>
        </div>
      </main>
    </div>

    <script>
      // Simple search and filter functionality
      document.addEventListener('DOMContentLoaded', function() {
        const searchInput = document.getElementById('searchInput');
        const statusFilter = document.getElementById('statusFilter');
        const tableRows = document.querySelectorAll('.table tbody tr');
        
        function filterTable() {
          const searchTerm = searchInput.value.toLowerCase();
          const statusValue = statusFilter.value;
          
          tableRows.forEach(row => {
            const text = row.textContent.toLowerCase();
            const statusBadge = row.querySelector('.badge');
            const rowStatus = statusBadge ? statusBadge.textContent.trim() : '';
            
            const matchesSearch = text.includes(searchTerm);
            const matchesStatus = statusValue === 'all' || 
              (statusValue === 'ACTIVE' && rowStatus === 'Hoạt động') ||
              (statusValue === 'SUSPENDED' && rowStatus === 'Tạm ngưng') ||
              (statusValue === 'EXPIRED' && rowStatus === 'Tạm ngưng');
            
            if (matchesSearch && matchesStatus) {
              row.style.display = '';
            } else {
              row.style.display = 'none';
            }
          });
        }
        
        if (searchInput) {
          searchInput.addEventListener('input', filterTable);
        }
        if (statusFilter) {
          statusFilter.addEventListener('change', filterTable);
        }
      });
    </script>
  </body>
</html>
