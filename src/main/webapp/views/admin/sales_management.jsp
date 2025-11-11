<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
    uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib prefix="fmt"
                                                                 uri="http://java.sun.com/jsp/jstl/fmt" %>

    <!DOCTYPE html>
    <html lang="vi">
        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <title>Quản lý đơn hàng - GymFit</title>

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

                /* Stats Grid */
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
                    box-shadow: 0 2px 10px var(--shadow);
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

                .stat-info h3 {
                    font-size: 1.8rem;
                    font-weight: 700;
                    color: var(--primary);
                }

                .stat-info p {
                    font-size: 0.85rem;
                    color: #5a6c7d;
                }

                /* Product Grid */
                .products-grid {
                    display: grid;
                    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
                    gap: 20px;
                    margin-top: 20px;
                }

                .product-card {
                    background: #fff;
                    border-radius: 12px;
                    overflow: hidden;
                    box-shadow: 0 2px 10px var(--shadow);
                    transition: all 0.3s ease;
                }

                .product-card:hover {
                    transform: translateY(-5px);
                    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
                }

                .product-image {
                    width: 100%;
                    height: 200px;
                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    font-size: 3rem;
                    color: #fff;
                }

                .product-body {
                    padding: 15px;
                }

                .product-name {
                    font-size: 1.1rem;
                    font-weight: 700;
                    color: var(--primary);
                    margin-bottom: 8px;
                }

                .product-price {
                    font-size: 1.3rem;
                    color: var(--accent);
                    font-weight: 700;
                    margin-bottom: 8px;
                }

                .product-stock {
                    font-size: 0.9rem;
                    color: #5a6c7d;
                    margin-bottom: 15px;
                }

                .product-actions {
                    display: flex;
                    gap: 8px;
                }

                .btn-small {
                    padding: 8px 16px;
                    font-size: 0.85rem;
                    flex: 1;
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

                /* Table */
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

                .badge-success {
                    background: #27ae60;
                    color: #fff;
                }

                .badge-warning {
                    background: #f39c12;
                    color: #fff;
                }

                .badge-danger {
                    background: #e74c3c;
                    color: #fff;
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
                    .products-grid {
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
                                class="sidebar-menu-link"
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
                                class="sidebar-menu-link active"
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
                        <h1><i class="fas fa-box"></i> Quản lý đơn hàng & Sản phẩm</h1>
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
                        <!-- Stats -->
                        <div class="stats-grid">
                            <div class="stat-card">
                                <div
                                    class="stat-icon"
                                    style="
                                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                                    "
                                    >
                                    <i class="fas fa-box"></i>
                                </div>
                                <div class="stat-info">
                                    <h3>${productCount}</h3>
                                    <p>Sản phẩm</p>
                                </div>
                            </div>

                            <div class="stat-card">
                                <div
                                    class="stat-icon"
                                    style="
                                    background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
                                    "
                                    >
                                    <i class="fas fa-shopping-cart"></i>
                                </div>
                                <div class="stat-info">
                                    <h3>${monthlyOrderCount}</h3>
                                    <p>Đơn hàng tháng này</p>
                                </div>
                            </div>

                            <div class="stat-card">
                                <div class="stat-icon" style="background: var(--gradient-accent)">
                                    <i class="fas fa-dollar-sign"></i>
                                </div>
                                <div class="stat-info">
                                    <h3>
                                        <fmt:formatNumber value="${monthlyRevenue}" pattern="#,###" /> đ
                                    </h3>
                                    <p>Doanh thu</p>
                                </div>
                            </div>

                            <div class="stat-card">
                                <div
                                    class="stat-icon"
                                    style="
                                    background: linear-gradient(135deg, #f39c12 0%, #d68910 100%);
                                    "
                                    >
                                    <i class="fas fa-exclamation-triangle"></i>
                                </div>
                                <div class="stat-info">
                                    <h3>${lowStockCount}</h3>
                                    <p>Sắp hết hàng</p>
                                </div>
                            </div>
                        </div>

                        <!-- Messages -->
                        <c:if test="${not empty success}">
                            <div style="background: #d4edda; color: #155724; padding: 15px; border-radius: 8px; margin-bottom: 20px; border: 1px solid #c3e6cb;">
                                <i class="fas fa-check-circle"></i> ${success}
                            </div>
                        </c:if>
                        <c:if test="${not empty error}">
                            <div style="background: #f8d7da; color: #721c24; padding: 15px; border-radius: 8px; margin-bottom: 20px; border: 1px solid #f5c6cb;">
                                <i class="fas fa-exclamation-circle"></i> ${error}
                            </div>
                        </c:if>

                        <!-- Tabs -->
                        <div class="tabs">
                            <button class="tab ${activeTab == 'products' || empty activeTab ? 'active' : ''}" onclick="switchTab('products')">
                                <i class="fas fa-box"></i> Quản lý sản phẩm
                            </button>
                            <button class="tab ${activeTab == 'orders' ? 'active' : ''}" onclick="switchTab('orders')">
                                <i class="fas fa-shopping-cart"></i> Đơn hàng
                            </button>
                        </div>

                        <!-- Products Tab -->
                        <div id="products" class="tab-content ${activeTab == 'products' || empty activeTab ? 'active' : ''}">
                            <div style="margin-bottom: 15px">
                                <button class="btn" id="btn-addProduct">
                                    <i class="fas fa-plus"></i> Thêm sản phẩm mới
                                </button>
                            </div>

                            <c:if test="${empty products}">
                                <div style="text-align: center; padding: 40px; background: #fff; border-radius: 12px; box-shadow: 0 2px 10px var(--shadow);">
                                    <i class="fas fa-box" style="font-size: 3rem; color: #ccc; margin-bottom: 15px;"></i>
                                    <p style="color: #5a6c7d; font-size: 1.1rem;">Chưa có sản phẩm nào</p>
                                    <button class="btn" onclick="document.getElementById('btn-addProduct').click()" style="margin-top: 15px;">
                                        <i class="fas fa-plus"></i> Thêm sản phẩm đầu tiên
                                    </button>
                                </div>
                            </c:if>
                            <c:if test="${not empty products}">
                            <div class="products-grid">

                                <c:forEach var="product" items="${products}">
                                    <c:set var="isInactive" value="${product.active == false or empty product.active}" />
                                    <div class="product-card" style="<c:if test='${isInactive}'>opacity: 0.7; border: 2px dashed #ccc;</c:if>">

                                        <!-- IMAGE BOX -->
                                        <div class="product-image"
                                             style="
                                             <c:choose>
                                                 <c:when test='${product.productType == "SUPPLEMENT"}'>
                                                     background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                                                 </c:when>
                                                 <c:when test='${product.productType == "ACCESSORY"}'>
                                                     background: linear-gradient(135deg, #f39c12 0%, #d68910 100%);
                                                 </c:when>
                                                 <c:when test='${product.productType == "EQUIPMENT"}'>
                                                     background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                                                 </c:when>
                                                 <c:when test='${product.productType == "APPAREL"}'>
                                                     background: var(--gradient-accent);
                                                 </c:when>
                                                 <c:otherwise>
                                                     background: linear-gradient(135deg, #999 0%, #666 100%);
                                                 </c:otherwise>
                                             </c:choose>
                                             ">

                                            <c:choose>
                                                <c:when test="${product.productType == 'SUPPLEMENT'}">
                                                    <i class="fas fa-bottle-water"></i>
                                                </c:when>
                                                <c:when test="${product.productType == 'ACCESSORY'}">
                                                    <i class="fas fa-mitten"></i>
                                                </c:when>
                                                <c:when test="${product.productType == 'EQUIPMENT'}">
                                                    <i class="fas fa-dumbbell"></i>
                                                </c:when>
                                                <c:when test="${product.productType == 'APPAREL'}">
                                                    <i class="fas fa-tshirt"></i>
                                                </c:when>
                                                <c:otherwise>
                                                    <i class="fas fa-box"></i>
                                                </c:otherwise>
                                            </c:choose>

                                        </div>

                                        <!-- BODY -->
                                        <div class="product-body">
                                            <div class="product-name">
                                                ${product.productName}
                                                <c:if test="${isInactive}">
                                                    <span style="background: #e74c3c; color: white; padding: 2px 8px; border-radius: 4px; font-size: 0.7rem; margin-left: 8px;">INACTIVE</span>
                                                </c:if>
                                            </div>

                                            <div class="product-price">
                                                <fmt:formatNumber value="${product.price}" pattern="#,###" />đ
                                            </div>

                                            <!-- STOCK -->
                                            <div class="product-stock"
                                                 style="<c:if test='${product.stockQuantity lt 10}'>color:#e74c3c;</c:if>">

                                                 <c:choose>
                                                     <c:when test="${product.stockQuantity lt 10}">
                                                         <i class="fas fa-exclamation-triangle"></i>
                                                         Tồn kho: ${product.stockQuantity} (Sắp hết)
                                                     </c:when>
                                                     <c:otherwise>
                                                         <i class="fas fa-warehouse"></i>
                                                         Tồn kho: ${product.stockQuantity}
                                                     </c:otherwise>
                                                 </c:choose>
                                            </div>

                                            <!-- ACTION BUTTONS -->
                                            <div class="product-actions">
                                                <button class="btn btn-small" style="background:#3498db"
                                                        onclick="handleEditProduct('${product.productId}', '${product.productName}',
                                        '${product.productType}', '${product.price}', '${product.stockQuantity}')">
                                                    <i class="fas fa-edit"></i> Sửa
                                                </button>

                                                <c:choose>
                                                    <c:when test="${isInactive}">
                                                        <button class="btn btn-small" style="background:#27ae60"
                                                                onclick="handleActivateProduct('${product.productId}')">
                                                            <i class="fas fa-check-circle"></i> Kích hoạt
                                                        </button>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <button class="btn btn-small" style="background:#e74c3c"
                                                                onclick="handleDeleteProduct('${product.productId}')">
                                                            <i class="fas fa-trash"></i> Vô hiệu hóa
                                                        </button>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>

                            </div>
                            </c:if>
                        </div>

                        <!-- Orders Tab -->
                        <div id="orders" class="tab-content ${activeTab == 'orders' ? 'active' : ''}">
                            <c:if test="${empty orders}">
                                <div style="text-align: center; padding: 40px; background: #fff; border-radius: 12px; box-shadow: 0 2px 10px var(--shadow);">
                                    <i class="fas fa-shopping-cart" style="font-size: 3rem; color: #ccc; margin-bottom: 15px;"></i>
                                    <p style="color: #5a6c7d; font-size: 1.1rem;">Chưa có đơn hàng nào</p>
                                </div>
                            </c:if>
                            <c:if test="${not empty orders}">
                            <div class="table-container">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>Mã ĐH</th>
                                            <th>Khách hàng</th>
                                            <th>Số sản phẩm</th>
                                            <th>Tổng tiền</th>
                                            <th>Ngày đặt</th>
                                            <th>Trạng thái</th>
                                            <th>Thao tác</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="order" items="${orders}">
                                            <tr>
                                                <td>
                                                    <strong>
                                                        <c:choose>
                                                            <c:when test="${order.orderNumber != null && !order.orderNumber.isEmpty()}">
                                                                ${order.orderNumber}
                                                            </c:when>
                                                            <c:otherwise>
                                                                #${order.orderId}
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </strong>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${order.user != null && order.user.name != null}">
                                                            ${order.user.name}
                                                        </c:when>
                                                        <c:otherwise>
                                                            Khách hàng #${order.memberId}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${order.items != null && !order.items.isEmpty()}">
                                                            ${order.items.size()} sản phẩm
                                                        </c:when>
                                                        <c:otherwise>0 sản phẩm</c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <strong style="color: var(--accent);">
                                                        <c:choose>
                                                            <c:when test="${order.totalAmount != null}">
                                                                <fmt:formatNumber value="${order.totalAmount}" pattern="#,###" /> đ
                                                            </c:when>
                                                            <c:otherwise>0 đ</c:otherwise>
                                                        </c:choose>
                                                    </strong>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${order.createdAtAsDate != null}">
                                                            <fmt:formatDate value="${order.createdAtAsDate}" pattern="dd/MM/yyyy HH:mm" />
                                                        </c:when>
                                                        <c:otherwise>N/A</c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td id="order-status-${order.orderId}">
                                                    <c:choose>
                                                        <c:when test="${order.orderStatus.toString() == 'PENDING'}">
                                                            <span class="badge badge-danger">Chờ xác nhận</span>
                                                        </c:when>
                                                        <c:when test="${order.orderStatus.toString() == 'COMPLETED'}">
                                                            <span class="badge badge-success">Hoàn thành</span>
                                                        </c:when>
                                                        <c:when test="${order.orderStatus.toString() == 'CONFIRMED'}">
                                                            <span class="badge badge-warning">Đã xác nhận</span>
                                                        </c:when>
                                                        <c:when test="${order.orderStatus.toString() == 'SHIPPING'}">
                                                            <span class="badge badge-warning">Đang giao</span>
                                                        </c:when>
                                                        <c:when test="${order.orderStatus.toString() == 'CANCELLED'}">
                                                            <span class="badge" style="background: #95a5a6; color: #fff;">Đã hủy</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge badge-warning">${order.orderStatus}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td id="order-action-${order.orderId}">
                                                    <c:if test="${order.orderStatus.toString() != 'COMPLETED' && order.orderStatus.toString() != 'CANCELLED'}">
                                                        <button type="button"
                                                                class="btn btn-small confirm-order-btn" 
                                                                style="background: #27ae60; text-decoration: none; display: inline-flex; align-items: center; gap: 5px; border: none; cursor: pointer;"
                                                                data-order-id="${order.orderId}"
                                                                onclick="confirmOrder(${order.orderId}, this)">
                                                            <i class="fas fa-check"></i> Hoàn thành
                                                        </button>
                                                    </c:if>
                                                    <c:if test="${order.orderStatus.toString() == 'COMPLETED'}">
                                                        <span style="color: #27ae60; font-weight: 600;">
                                                            <i class="fas fa-check-circle"></i> Đã hoàn thành
                                                        </span>
                                                    </c:if>
                                                    <c:if test="${order.orderStatus.toString() == 'CANCELLED'}">
                                                        <span style="color: #95a5a6; font-weight: 600;">
                                                            <i class="fas fa-times-circle"></i> Đã hủy
                                                        </span>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            </c:if>
                        </div>
                    </div>
                </main>
            </div>

            <div class="modal" id="addProductModal">
                <div class="modal-content">
                    <div class="modal-header">
                        <h3 class="modal-title" id="formTitle">Thêm sản phẩm</h3>
                        <button class="modal-close" onclick="closeModal('addProductModal')">
                            &times;
                        </button>
                    </div>
                    <form action="${pageContext.request.contextPath}/admin/sales-management" method="post" id="productForm">
                        <input type="hidden" name="action" value="addProduct" />
                        <div class="form-group">
                            <label class="form-label">Tên sản phẩm</label>
                            <input type="text" class="form-input" name="name" required />
                        </div>
                        <div class="form-group">
                            <label class="form-label">Loại sản phẩm</label>
                            <select class="form-input" name="productType" required>
                                <option value="SUPPLEMENT" selected>Thực phẩm bổ sung</option>
                                <option value="EQUIPMENT">Thiết bị</option>
                                <option value="APPAREL">Trang phục</option>
                                <option value="ACCESSORY">Phụ kiện</option>
                                <option value="OTHER">Khác</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Giá sản phẩm</label>
                            <input type="number" class="form-input" name="price" min="0" max="2000000000" value="1000" required />
                        </div>

                        <div class="form-group">
                            <label class="form-label">Số lượng</label>
                            <input type="number" class="form-input" name="stockQuantity" min="0" max="2000000000" value="1" required/>
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
                            <button type="submit" class="btn">Thêm sản phẩm</button>
                        </div>
                    </form>
                </div>
                <script>
                    // Store context path in JavaScript variable
                    const contextPath = '${pageContext.request.contextPath}';
                    
                    const handleEditProduct = (
                            productId,
                            productName,
                            productType,
                            price,
                            stockQuantity
                            ) => {
                        console.log('Edit product with ID:', productId)
                        document.getElementById('formTitle').innerText = 'Sửa sản phẩm'
                        document.getElementById('addProductModal').classList.add('active')
                        const form = document.getElementById('productForm')
                        form.action =
                                contextPath +
                                `/admin/sales-management?action=editProduct&productId=` +
                                productId
                        form.method = 'post'
                        form.querySelector('input[name="name"]').value = productName
                        form.querySelector('select[name="productType"]').value = productType
                        form.querySelector('input[name="price"]').value = price
                        form.querySelector('input[name="stockQuantity"]').value = stockQuantity
                        form.querySelector('input[name="action"]').value = 'editProduct'
                        form.querySelector('button[type="submit"]').innerText = 'Lưu thay đổi'
                    }

                    const handleDeleteProduct = (productId) => {
                        if (confirm('Bạn có chắc chắn muốn vô hiệu hóa sản phẩm này không?')) {
                            window.location.href =
                                    contextPath +
                                    '/admin/sales-management?action=deleteProduct&productId=' +
                                    productId
                        }
                    }
                    
                    const handleActivateProduct = (productId) => {
                        if (confirm('Bạn có chắc chắn muốn kích hoạt lại sản phẩm này không?')) {
                            window.location.href =
                                    contextPath +
                                    '/admin/sales-management?action=activateProduct&productId=' +
                                    productId
                        }
                    }

                    document
                            .getElementById('btn-addProduct')
                            .addEventListener('click', function () {
                                console.log('add product button clicked!')

                                document.getElementById('addProductModal').classList.add('active')

                                const form = document.getElementById('productForm')
                            })

                    function switchTab(tabName) {
                        const tabs = document.querySelectorAll('.tab-content');
                        tabs.forEach((tab) => tab.classList.remove('active'));

                        const tabButtons = document.querySelectorAll('.tab');
                        tabButtons.forEach((btn) => btn.classList.remove('active'));

                        document.getElementById(tabName).classList.add('active');
                        if (event && event.target) {
                            event.target.closest('.tab').classList.add('active');
                        }
                        
                        // Update URL without reloading page
                        const url = new URL(window.location);
                        url.searchParams.set('tab', tabName);
                        window.history.pushState({}, '', url);
                    }
                    
                    function closeModal(modalId) {
                        document.getElementById(modalId).classList.remove('active');
                    }
                    
                    // Initialize tab on page load
                    document.addEventListener('DOMContentLoaded', function() {
                        const urlParams = new URLSearchParams(window.location.search);
                        const tab = urlParams.get('tab');
                        if (tab) {
                            switchTab(tab);
                        }
                    });
                    
                    // Function to confirm order via AJAX
                    function confirmOrder(orderId, buttonElement) {
                        if (!confirm('Bạn có chắc chắn muốn hoàn thành đơn hàng này?')) {
                            return;
                        }
                        
                        // Disable button and show loading
                        buttonElement.disabled = true;
                        const originalText = buttonElement.innerHTML;
                        buttonElement.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang xử lý...';
                        
                        // Make AJAX request
                        const url = contextPath + '/admin/sales-management?action=confirmOrder&orderId=' + orderId + '&ajax=true';
                        
                        console.log('Sending AJAX request to:', url);
                        
                        fetch(url, {
                            method: 'GET'
                        })
                        .then(response => {
                            console.log('Response status:', response.status);
                            if (!response.ok) {
                                throw new Error('HTTP error! status: ' + response.status);
                            }
                            return response.text();
                        })
                        .then(text => {
                            console.log('Response text:', text);
                            try {
                                const data = JSON.parse(text);
                                if (data.success) {
                                    // Update status badge
                                    const statusCell = document.getElementById('order-status-' + orderId);
                                    if (statusCell) {
                                        statusCell.innerHTML = '<span class="badge badge-success">Hoàn thành</span>';
                                    }
                                    
                                    // Update action cell
                                    const actionCell = document.getElementById('order-action-' + orderId);
                                    if (actionCell) {
                                        actionCell.innerHTML = '<span style="color: #27ae60; font-weight: 600;"><i class="fas fa-check-circle"></i> Đã hoàn thành</span>';
                                    }
                                    
                                    // Show success message
                                    showMessage('success', data.message);
                                } else {
                                    // Show error message
                                    showMessage('error', data.message);
                                    // Re-enable button
                                    buttonElement.disabled = false;
                                    buttonElement.innerHTML = originalText;
                                }
                            } catch (e) {
                                console.error('Error parsing JSON:', e);
                                console.error('Response text was:', text);
                                throw new Error('Invalid JSON response');
                            }
                        })
                        .catch(error => {
                            console.error('Error:', error);
                            showMessage('error', 'Lỗi khi xử lý yêu cầu. Vui lòng thử lại!');
                            // Re-enable button
                            buttonElement.disabled = false;
                            buttonElement.innerHTML = originalText;
                        });
                    }
                    
                    // Function to show messages
                    function showMessage(type, message) {
                        // Remove existing messages
                        const existingMessages = document.querySelectorAll('.message-alert');
                        existingMessages.forEach(msg => msg.remove());
                        
                        // Create message element
                        const messageDiv = document.createElement('div');
                        messageDiv.className = 'message-alert';
                        messageDiv.style.cssText = type === 'success' 
                            ? 'background: #d4edda; color: #155724; padding: 15px; border-radius: 8px; margin-bottom: 20px; border: 1px solid #c3e6cb;'
                            : 'background: #f8d7da; color: #721c24; padding: 15px; border-radius: 8px; margin-bottom: 20px; border: 1px solid #f5c6cb;';
                        messageDiv.innerHTML = '<i class="fas ' + (type === 'success' ? 'fa-check-circle' : 'fa-exclamation-circle') + '"></i> ' + message;
                        
                        // Insert message in the messages section or after stats grid
                        const contentArea = document.querySelector('.content-area');
                        if (!contentArea) {
                            console.error('Content area not found');
                            return;
                        }
                        
                        // Try to find messages section (after stats-grid, before tabs)
                        const statsGrid = document.querySelector('.stats-grid');
                        const tabs = document.querySelector('.tabs');
                        
                        if (tabs && tabs.parentNode === contentArea) {
                            // Insert before tabs
                            contentArea.insertBefore(messageDiv, tabs);
                        } else if (statsGrid && statsGrid.parentNode === contentArea) {
                            // Insert after stats grid
                            if (statsGrid.nextSibling) {
                                contentArea.insertBefore(messageDiv, statsGrid.nextSibling);
                            } else {
                                contentArea.appendChild(messageDiv);
                            }
                        } else {
                            // Fallback: insert at the beginning of content area
                            if (contentArea.firstChild) {
                                contentArea.insertBefore(messageDiv, contentArea.firstChild);
                            } else {
                                contentArea.appendChild(messageDiv);
                            }
                        }
                        
                        // Auto remove after 5 seconds
                        setTimeout(() => {
                            if (messageDiv && messageDiv.parentNode) {
                                messageDiv.remove();
                            }
                        }, 5000);
                    }
                </script>
        </body>
    </html>
