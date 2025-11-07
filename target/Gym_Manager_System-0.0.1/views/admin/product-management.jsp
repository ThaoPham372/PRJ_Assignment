<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Quản lý sản phẩm - GymFit</title>

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
        font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
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
        padding: 30px 40px;
    }

    .page-header {
        background: linear-gradient(135deg, #141a46 0%, #1e2a5c 100%);
        color: white;
        padding: 30px;
        border-radius: 15px;
        margin-bottom: 30px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    
    .btn {
        padding: 10px 20px;
        border-radius: 8px;
        text-decoration: none;
        font-weight: 600;
        transition: all 0.3s;
        border: none;
        cursor: pointer;
        display: inline-block;
    }
    
    .btn-primary {
        background: #ec8b5e;
        color: white;
    }
    
    .btn-primary:hover {
        background: #d67a4f;
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(236, 139, 94, 0.4);
    }
    
    .btn-danger {
        background: #dc3545;
        color: white;
    }
    
    .btn-danger:hover {
        background: #c82333;
        transform: translateY(-2px);
    }
    
    .btn-warning {
        background: #ffc107;
        color: #000;
    }
    
    .btn-warning:hover {
        background: #e0a800;
        transform: translateY(-2px);
    }
    
    .btn-sm {
        padding: 6px 12px;
        font-size: 0.85rem;
    }
    
    table {
        width: 100%;
        background: white;
        border-radius: 10px;
        overflow: hidden;
        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    }
    
    th {
        background: #141a46;
        color: white;
        padding: 15px;
        text-align: left;
    }
    
    td {
        padding: 15px;
        border-bottom: 1px solid #eee;
    }
    
    tr:hover {
        background: #f8f9fa;
    }
    
    .badge {
        padding: 5px 10px;
        border-radius: 5px;
        font-size: 0.85rem;
    }
    
    .badge-success { background: #d4edda; color: #155724; }
    .badge-danger { background: #f8d7da; color: #721c24; }
    
    .alert {
        padding: 15px;
        border-radius: 8px;
        margin-bottom: 20px;
    }
    
    .alert-success { background: #d4edda; color: #155724; }
    .alert-danger { background: #f8d7da; color: #721c24; }
    
    .search-box {
        background: white;
        padding: 20px;
        border-radius: 10px;
        margin-bottom: 20px;
        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    }
    
    .search-box form {
        display: flex;
        gap: 10px;
        align-items: center;
    }
    
    .search-box input[type="text"] {
        flex: 1;
        padding: 10px 15px;
        border: 2px solid #ddd;
        border-radius: 8px;
        font-size: 1rem;
    }
    
    .search-box input[type="text"]:focus {
        outline: none;
        border-color: #ec8b5e;
    }
    
    .pagination {
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 10px;
        margin-top: 20px;
        padding: 20px;
    }
    
    .pagination a, .pagination span {
        padding: 8px 12px;
        border: 1px solid #ddd;
        border-radius: 5px;
        text-decoration: none;
        color: #141a46;
        transition: all 0.3s;
    }
    
    .pagination a:hover {
        background: #ec8b5e;
        color: white;
        border-color: #ec8b5e;
    }
    
    .pagination .active {
        background: #141a46;
        color: white;
        border-color: #141a46;
        font-weight: bold;
    }
    
    .stats-info {
        text-align: center;
        color: #666;
        margin-top: 10px;
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
              class="sidebar-menu-link active"
            >
              <i class="fas fa-box"></i>
              <span>Quản lý sản phẩm</span>
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
    <div class="main-content">
        <div class="page-header">
        <h1><i class="fas fa-box"></i> Quản Lý Sản Phẩm</h1>
        <a href="${pageContext.request.contextPath}/admin/products/add" class="btn btn-primary">
            <i class="fas fa-plus"></i> Thêm Sản Phẩm
        </a>
    </div>
    
    <!-- Search Box -->
    <div class="search-box">
        <form method="get" action="${pageContext.request.contextPath}/admin/products">
            <i class="fas fa-search" style="color: #999;"></i>
            <input type="text" name="keyword" placeholder="Tìm kiếm sản phẩm..." 
                   value="${fn:escapeXml(keyword)}"/>
            <button type="submit" class="btn btn-primary">
                <i class="fas fa-search"></i> Tìm kiếm
            </button>
            <c:if test="${not empty keyword}">
                <a href="${pageContext.request.contextPath}/admin/products" class="btn" 
                   style="background: #6c757d; color: white;">
                    <i class="fas fa-times"></i> Xóa lọc
                </a>
            </c:if>
        </form>
        <div class="stats-info">
            Tổng số: <strong>${totalProducts != null ? totalProducts : 0}</strong> sản phẩm
            <c:if test="${not empty keyword}">
                - Kết quả tìm kiếm cho: "<strong>${fn:escapeXml(keyword)}</strong>"
            </c:if>
        </div>
    </div>
    
    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success">
            <i class="fas fa-check-circle"></i> ${sessionScope.success}
        </div>
        <c:remove var="success" scope="session"/>
    </c:if>
    
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger">
            <i class="fas fa-exclamation-circle"></i> ${sessionScope.error}
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>
    
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Tên Sản Phẩm</th>
                <th>Loại</th>
                <th>Giá</th>
                <th>Giá Vốn</th>
                <th>Tồn Kho</th>
                <th>Đơn Vị</th>
                <th>Trạng Thái</th>
                <th>Thao Tác</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="product" items="${products}">
                <tr>
                    <td>${product.productId}</td>
                    <td>${fn:escapeXml(product.productName)}</td>
                    <td>${product.productType.displayName}</td>
                    <td><fmt:formatNumber value="${product.price}" type="currency" currencySymbol="đ" maxFractionDigits="0"/></td>
                    <td><fmt:formatNumber value="${product.costPrice}" type="currency" currencySymbol="đ" maxFractionDigits="0"/></td>
                    <td>${product.stockQuantity}</td>
                    <td>${fn:escapeXml(product.unit)}</td>
                    <td>
                        <c:choose>
                            <c:when test="${product.active}">
                                <span class="badge badge-success">Hoạt động</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-danger">Ngưng</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/admin/products/edit?id=${product.productId}" 
                           class="btn btn-warning btn-sm">
                            <i class="fas fa-edit"></i>
                        </a>
                        <form method="post" action="${pageContext.request.contextPath}/admin/products" style="display:inline;" 
                              onsubmit="return confirm('Xóa sản phẩm này? (Soft delete - set active=false)')">
                            <input type="hidden" name="action" value="delete"/>
                            <input type="hidden" name="productId" value="${product.productId}"/>
                            <button type="submit" class="btn btn-danger btn-sm">
                                <i class="fas fa-trash"></i>
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty products}">
                <tr>
                    <td colspan="9" style="text-align: center; padding: 40px; color: #999;">
                        Chưa có sản phẩm nào
                    </td>
                </tr>
            </c:if>
        </tbody>
    </table>
    
    <!-- Pagination -->
    <c:if test="${totalPages > 1}">
        <div class="pagination">
            <!-- Previous -->
            <c:if test="${currentPage > 1}">
                <a href="?page=${currentPage - 1}<c:if test='${not empty keyword}'>&keyword=${fn:escapeXml(keyword)}</c:if>">
                    <i class="fas fa-chevron-left"></i> Trước
                </a>
            </c:if>
            
            <!-- Page numbers -->
            <c:set var="startPage" value="${currentPage - 2 > 0 ? currentPage - 2 : 1}"/>
            <c:set var="endPage" value="${startPage + 4 <= totalPages ? startPage + 4 : totalPages}"/>
            <c:if test="${endPage - startPage < 4}">
                <c:set var="startPage" value="${endPage - 4 > 0 ? endPage - 4 : 1}"/>
            </c:if>
            
            <c:if test="${startPage > 1}">
                <a href="?page=1<c:if test='${not empty keyword}'>&keyword=${fn:escapeXml(keyword)}</c:if>">1</a>
                <c:if test="${startPage > 2}">
                    <span>...</span>
                </c:if>
            </c:if>
            
            <c:forEach var="i" begin="${startPage}" end="${endPage}">
                <c:choose>
                    <c:when test="${i == currentPage}">
                        <span class="active">${i}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="?page=${i}<c:if test='${not empty keyword}'>&keyword=${fn:escapeXml(keyword)}</c:if>">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            
            <c:if test="${endPage < totalPages}">
                <c:if test="${endPage < totalPages - 1}">
                    <span>...</span>
                </c:if>
                <a href="?page=${totalPages}<c:if test='${not empty keyword}'>&keyword=${fn:escapeXml(keyword)}</c:if>">${totalPages}</a>
            </c:if>
            
            <!-- Next -->
            <c:if test="${currentPage < totalPages}">
                <a href="?page=${currentPage + 1}<c:if test='${not empty keyword}'>&keyword=${fn:escapeXml(keyword)}</c:if>">
                    Sau <i class="fas fa-chevron-right"></i>
                </a>
            </c:if>
        </div>
    </c:if>
    </div>
    <!-- End Main Content -->
</div>
<!-- End Admin Container -->

</body>
</html>

