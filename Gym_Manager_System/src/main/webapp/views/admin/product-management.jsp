<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/views/common/header.jsp" %>

<style>
    .admin-container {
        max-width: 1400px;
        margin: 40px auto;
        padding: 0 20px;
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
    }
    
    .btn-danger {
        background: #dc3545;
        color: white;
    }
    
    .btn-warning {
        background: #ffc107;
        color: #000;
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

<div class="admin-container">
    <div class="mb-4">
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn-back">
            <i class="fas fa-arrow-left"></i>
            <span>Quay lại Dashboard</span>
        </a>
    </div>

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

<%@ include file="/views/common/footer.jsp" %>

