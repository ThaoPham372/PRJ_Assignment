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
                            <i class="fas fa-home"></i>
                            <span>Trang chủ</span>
                        </a>
                    </li>
                    <li class="sidebar-menu-item">
                        <a
                            href="${pageContext.request.contextPath}/admin/profile"
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
                            href="${pageContext.request.contextPath}/admin/membership-management"
                            class="sidebar-menu-link active"
                            >
                            <i class="fas fa-users"></i>
                            <span>Quản lý hội viên</span>
                        </a>
                    </li>
                    <li class="sidebar-menu-item">
                        <a
                            href="${pageContext.request.contextPath}/admin/service-schedule"
                            class="sidebar-menu-link"
                            >
                            <i class="fas fa-calendar-alt"></i>
                            <span>Dịch vụ & Lịch tập</span>
                        </a>
                    </li>
                    <li class="sidebar-menu-item">
                        <a
                            href="${pageContext.request.contextPath}/admin/trainer-management"
                            class="sidebar-menu-link"
                            >
                            <i class="fas fa-chalkboard-teacher"></i>
                            <span>Quản lý PT</span>
                        </a>
                    </li>
                    <li class="sidebar-menu-item">
                        <a
                            href="${pageContext.request.contextPath}/admin/sales-management"
                            class="sidebar-menu-link"
                            >
                            <i class="fas fa-box"></i>
                            <span>Quản lý đơn hàng</span>
                        </a>
                    </li>
                    <li class="sidebar-menu-item">
                        <a
                            href="${pageContext.request.contextPath}/admin/payment-finance"
                            class="sidebar-menu-link"
                            >
                            <i class="fas fa-money-bill-wave"></i>
                            <span>Thanh toán & Tài chính</span>
                        </a>
                    </li>
                    <li class="sidebar-menu-item">
                        <a
                            href="${pageContext.request.contextPath}/admin/reports"
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
                            href="${pageContext.request.contextPath}/admin/dashboard"
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
                                <h3>${numberMemberships}</h3>
                                <p>Tổng hội viên</p>
                            </div>
                        </div>

                        <div class="stat-card">
                            <div class="stat-icon green">
                                <i class="fas fa-user-check"></i>
                            </div>
                            <div class="stat-info">
                                <h3>${numberMembershipsActive}</h3>
                                <p>Đang hoạt động</p>
                            </div>
                        </div>

                        <div class="stat-card">
                            <div class="stat-icon orange">
                                <i class="fas fa-user-clock"></i>
                            </div>
                            <div class="stat-info">
                                <h3>${numberMembershipsExpiringSoon}</h3>
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
                                <h3>${numberMembershipsExpired}</h3>
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
                                name="keyword"
                                />

                            <select class="filter-select" name="status">
                                <option value="all" ${status == 'all' ? 'selected' : ''}>Tất cả trạng thái</option>
                                <option value="active" ${status == 'active' ? 'selected' : ''}>Đang hoạt động</option>
                                <option value="expiring" ${status == 'expiring' ? 'selected' : ''}>Sắp hết hạn</option>
                                <option value="expired" ${status == 'expired' ? 'selected' : ''}>Đã hết hạn</option>
                            </select>

                            <select class="filter-select" name="packageType">
                                <option value="all" ${packageType == 'all' ? 'selected' : ''}>Tất cả gói tập</option>
                                <c:if test="${not empty packages}">
                                    <c:forEach var="packageO" items="${packages}">
                                        <c:if test="${packageO != null}">
                                            <option value="${packageO.name}" ${packageType == packageO.name ? 'selected' : ''}>${packageO.name}</option>
                                        </c:if>
                                    </c:forEach>
                                </c:if>
                            </select>
                        </div>

                        <button class="btn" onclick="openAddMembershipModal()">
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
                                    <th>Ngày bắt đầu</th>
                                    <th>Ngày hết hạn</th>
                                    <th>Trạng thái</th>
                                    <th>Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>

                                <c:if test="${empty memberships}">
                                    <tr>
                                        <td colspan="8" style="text-align: center; padding: 20px;">
                                            Không có hội viên nào.
                                        </td>
                                    </tr>
                                </c:if>

                                <c:if test="${not empty memberships}">
                                    <c:forEach var="membership" items="${memberships}">
                                        <tr>
                                            <c:choose>
                                                <c:when test="${membership.id != null}">
                                                    <input type="hidden" id="${membership.id}"/>
                                                </c:when>
                                            <c:otherwise></c:otherwise>
                                            </c:choose>
                                        <td><c:choose><c:when test="${membership.member != null}">${membership.member.id}</c:when><c:otherwise></c:otherwise></c:choose></td>
                                        <td><c:choose><c:when test="${membership.member != null}">${membership.member.name}</c:when><c:otherwise></c:otherwise></c:choose></td>
                                        <td><c:choose><c:when test="${membership.member != null}">${membership.member.email}</c:when><c:otherwise></c:otherwise></c:choose></td>
                                                <td>
                                                    <span class="badge badge-pt">
                                                <c:choose>
                                                    <c:when test="${membership.packageO != null}">${membership.packageO.name}</c:when>
                                                    <c:otherwise></c:otherwise>
                                                </c:choose>
                                            </span>
                                        </td>
                                        <td><c:choose><c:when test="${membership.createdDate != null}"><fmt:formatDate value="${membership.createdDate}" pattern="dd/MM/yyyy" /></c:when><c:otherwise></c:otherwise></c:choose></td>
                                        <td><c:choose><c:when test="${membership.startDate != null}"><fmt:formatDate value="${membership.startDate}" pattern="dd/MM/yyyy" /></c:when><c:otherwise></c:otherwise></c:choose></td>
                                        <td><c:choose><c:when test="${membership.endDate != null}"><fmt:formatDate value="${membership.endDate}" pattern="dd/MM/yyyy" /></c:when><c:otherwise></c:otherwise></c:choose></td>
                                        <td><c:choose><c:when test="${membership.status != null}"><span class="badge badge-active">${membership.status}</span></c:when><c:otherwise></c:otherwise></c:choose></td>
                                                <td>
                                                    <div class="action-buttons">
                                                        <button id="btn-edit" class="btn-icon btn-edit" title="Sửa thông tin" 
                                                                onclick="openEditMembershipModal('${membership.id}', '${membership.member.username}', '${membership.member.name}', '${membership.packageO.id}', '${membership.status}', '${membership.startDate}')" >
                                                            <i class="fas fa-edit"></i>
                                                        </button>
                                                        <button
                                                            class="btn-icon"
                                                            style="background: #9b59b6"
                                                            title="Gia hạn"
                                                            >
                                                            <i class="fas fa-calendar-plus"></i>
                                                        </button>
                                                        <button class="btn-icon btn-delete" title="Xóa" name="deleteMembership" onclick="handleClickDelete(${membership.id})">
                                                            <i class="fas fa-trash"></i>
                                                        </button>
                                                    </div>
                                                </td>
                                                </tr>
                                    </c:forEach>
                                </c:if>

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
                <form action="#" method="post" id="membershipForm">
                    <input type="hidden" name="action" value="addMembership" />
                    <div class="form-group">
                        <label class="form-label">Username</label>
                        <input type="text" class="form-input" name="username" required />
                    </div>
                    <div class="form-group">
                        <label class="form-label">Gói tập</label>

                        <select class="form-input" name="package_id" required>
                            <option value="">-- Chọn gói tập --</option>
                            <c:if test="${not empty packages}">
                                <c:forEach var="packageO" items="${packages}">
                                    <c:if test="${packageO != null}">
                                        <option value="${packageO.id}">${packageO.name} - ${packageO.durationMonths} tháng</option>
                                    </c:if>
                                </c:forEach>
                            </c:if>
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
        
        <div class="modal" id="editMembership">
            <div class="modal-content">
                <div class="modal-header">
                    <h3 class="modal-title">Chỉnh sửa hội viên</h3>
                    <button class="modal-close" onclick="closeModal('editMembership')">
                        &times;
                    </button>
                </div>
                <form action="#" method="post" id="editFrom">
                    <input type="hidden" name="action" value="editMembership" />
                    <input type="hidden" name="id" value="-1" />
                    <div class="form-group">
                        <label class="form-label">Username</label>
                        <input type="text" class="form-input" name="username" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-label">Tên</label>
                        <input type="text" class="form-input" name="name" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-label">Gói tập</label>

                        <select class="form-input" name="package_id" required>
                            <option value="">-- Chọn gói tập --</option>
                            <c:if test="${not empty packages}">
                                <c:forEach var="packageO" items="${packages}">
                                    <c:if test="${packageO != null}">
                                        <option value="${packageO.id}">${packageO.name} - ${packageO.durationMonths} tháng</option>
                                    </c:if>
                                </c:forEach>
                            </c:if>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label class="form-label">Trạng thái</label>

                        <select class="form-input" name="status" required>
                            <option value="active">ACTIVE</option>
                            <option value="inactive">INACTIVE</option>
                            <option value="suspended">SUSPENDED</option>
                            <option value="expired">EXPIRED</option>
                            <option value="cancelled">CANCELLED</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Ngày bắt đầu</label>
                        <input type="date" class="form-input" name="startDate" value="<fmt:formatDate value='' pattern='yyyy-MM-dd'/>" required />
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
                            onclick="closeModal('editMembership')"
                            >
                            Hủy
                        </button>
                        <button type="submit" class="btn">edit</button>
                    </div>
                </form>
            </div>
        </div>

        <script>
            console.log("JS Loaded ✅");

            const contextPath = '${pageContext.request.contextPath}';

            handleClickDelete = function(membershipId) {
                deleteMembership(membershipId);
            }

            document.querySelectorAll('button[name="deleteMembership"]').forEach(button => {
                button.addEventListener('click', function () {
                    const membershipId = this.closest('tr').querySelector('td:first-child').textContent.trim();
                    deleteMembership(membershipId);
                });
            });

            function openAddMembershipModal() {
                document.getElementById('addMemberModal').classList.add('active');
                const form = document.querySelector('#membershipForm');
                form.action = `${contextPath}/admin/membership-management`;
                form.method = 'post';
                form.querySelector('input[name="action"]').value = 'addMembership';
            }
            
            function formatDateForInput(dateString) {
                if (!dateString) return "";

                // Ví dụ chuỗi: "Sat Nov 01 00:00:00 ICT 2025"
                const parts = dateString.split(" ");
                if (parts.length < 6) return "";

                const monthMap = {
                    Jan: "01", Feb: "02", Mar: "03", Apr: "04",
                    May: "05", Jun: "06", Jul: "07", Aug: "08",
                    Sep: "09", Oct: "10", Nov: "11", Dec: "12"
                };

                const day = parts[2];
                const month = monthMap[parts[1]];
                const year = parts[5];
                
                let result = year + "-" + month + "-" + day;
                return result;
            }
         
            function openEditMembershipModal(id, username, name, packageId, status, startDate) {
                document.getElementById('editMembership').classList.add('active');
                const form = document.querySelector('#editFrom');
                form.action = `${contextPath}/admin/membership-management`;
                form.method = 'post';
                form.querySelector('input[name="action"]').value = 'editMembership';
                form.querySelector('input[name="id"]').value = id;
                form.querySelector('input[name="username"]').value = username;
                form.querySelector('input[name="name"]').value = name;
                form.querySelector('select[name="package_id"]').value = packageId;
                form.querySelector('select[name="status"]').value = status.toLowerCase();
                console.log(startDate);
                console.log(formatDateForInput(startDate));
                form.querySelector('input[name="startDate"]').value = formatDateForInput(startDate);
                
                
            }

            function closeModal(modalId) {
                document.getElementById(modalId).classList.remove('active');
            }

            function deleteMembership(membershipId) {
                if (confirm('Bạn có chắc chắn muốn xóa hội viên này không?')) {
                    // Redirect to servlet with delete action
                    window.location.href = `${contextPath}/admin/membership-management?action=deleteMembership&membershipId=` + membershipId;
                }
            }

            document.querySelectorAll('.filter-select').forEach(select => {
                select.addEventListener('change', () => {
                    let status = document.querySelector('select[name="status"]').value;
                    let packageType = document.querySelector('select[name="packageType"]').value;
                    let keyword = document.querySelector('input[name="keyword"]').value.trim();
                    let query = '?action=filterMemberships&';
                    if (status && status !== 'all') {
                        query += `status=` + status;
                    }
                    if (packageType && packageType !== 'all') {
                        query += `packageType=` + packageType;
                    }
                    if (keyword) {
                        query += `keyword=` + keyword;
                    }
                    // Remove trailing '&' or '?' if exists
                    if (query.endsWith('&') || query.endsWith('?')) {
                        query = query.slice(0, -1);
                    }
                    console.log(`${contextPath}/admin/membership-management` + query);

                    window.location.href = `${contextPath}/admin/membership-management` + query;
                });
            });

            // Close modal when clicking outside
            window.onclick = function (event) {
                if (event.target.classList.contains('modal')) {
                    event.target.classList.remove('active');
                }
            };
        </script>
    </body>
</html>
