<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/views/common/header.jsp" %>

<style>
    :root {
        --primary: #141a46;
        --primary-light: #1e2a5c;
        --accent: #ec8b5e;
        --accent-hover: #d67a4f;
        --text: #2c3e50;
        --text-light: #5a6c7d;
        --muted: #f8f9fa;
        --card: #ffffff;
        --shadow: rgba(0, 0, 0, 0.1);
        --shadow-hover: rgba(0, 0, 0, 0.15);
        --gradient-primary: linear-gradient(135deg, #141a46 0%, #1e2a5c 100%);
        --gradient-accent: linear-gradient(135deg, #ec8b5e 0%, #d67a4f 100%);
    }

    .cart-container {
        max-width: 1200px;
        margin: 40px auto;
        padding: 0 20px;
    }

    .cart-header {
        background: var(--gradient-primary);
        color: white;
        padding: 30px;
        border-radius: 15px;
        margin-bottom: 30px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .cart-header h1 {
        margin: 0;
        font-size: 2rem;
        font-weight: 700;
    }

    .cart-header .cart-actions {
        display: flex;
        gap: 15px;
    }

    .cart-table {
        background: var(--card);
        border-radius: 15px;
        box-shadow: 0 4px 15px var(--shadow);
        overflow: hidden;
        margin-bottom: 30px;
        transition: all 0.3s ease;
    }

    .cart-table:hover {
        box-shadow: 0 8px 25px var(--shadow-hover);
    }

    .cart-table table {
        width: 100%;
        border-collapse: collapse;
    }

    .cart-table th {
        background: var(--muted);
        padding: 15px;
        text-align: left;
        font-weight: 600;
        color: var(--text);
    }

    .cart-table td {
        padding: 15px;
        border-bottom: 1px solid var(--muted);
        vertical-align: middle;
        transition: background-color 0.3s ease;
    }

    .cart-table tbody tr:hover td {
        background-color: rgba(236, 139, 94, 0.05);
    }

    .product-info {
        display: flex;
        align-items: center;
        gap: 15px;
    }

    .product-image {
        width: 80px;
        height: 80px;
        border-radius: 10px;
        object-fit: cover;
        transition: transform 0.3s ease;
    }

    .product-image:hover {
        transform: scale(1.05);
    }

    .product-details h3 {
        margin: 0 0 5px 0;
        font-size: 1.1rem;
        color: var(--text);
    }

    .product-details p {
        margin: 0;
        color: var(--text-light);
        font-size: 0.9rem;
    }

    .quantity-control {
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .quantity-input {
        width: 60px;
        padding: 8px;
        border: 2px solid var(--muted);
        border-radius: 8px;
        text-align: center;
        transition: border-color 0.3s ease, box-shadow 0.3s ease;
    }

    .quantity-input:focus {
        outline: none;
        border-color: var(--accent);
        box-shadow: 0 0 0 3px rgba(236, 139, 94, 0.25);
    }

    .price {
        font-weight: 600;
        color: var(--accent);
    }

    .subtotal {
        font-weight: 700;
        color: var(--primary);
    }

    .cart-summary {
        background: var(--card);
        border-radius: 15px;
        box-shadow: 0 4px 15px var(--shadow);
        padding: 30px;
        transition: all 0.3s ease;
    }

    .cart-summary:hover {
        box-shadow: 0 8px 25px var(--shadow-hover);
        transform: translateY(-2px);
    }

    .summary-row {
        display: flex;
        justify-content: space-between;
        margin-bottom: 15px;
        padding-bottom: 15px;
        border-bottom: 1px solid var(--muted);
    }

    .summary-row:last-child {
        margin-bottom: 0;
        padding-bottom: 0;
        border-bottom: none;
    }

    .summary-label {
        color: var(--text-light);
        font-weight: 500;
    }

    .summary-value {
        color: var(--text);
        font-weight: 600;
    }

    .total-row {
        font-size: 1.2rem;
        color: var(--primary);
        font-weight: 700;
    }

    .btn {
        padding: 12px 24px;
        border-radius: 25px;
        font-weight: 600;
        text-decoration: none;
        transition: all 0.3s ease;
        display: inline-flex;
        align-items: center;
        gap: 8px;
        border: none;
        cursor: pointer;
        will-change: transform;
    }

    .btn-primary {
        background: var(--gradient-accent);
        color: white;
    }

    .btn-primary:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(236, 139, 94, 0.4);
    }

    .btn-outline {
        border: 2px solid var(--primary);
        color: var(--primary);
        background: transparent;
    }

    .btn-outline:hover {
        background: var(--primary);
        color: white;
        transform: translateY(-2px);
    }

    .btn-danger {
        background: #dc3545;
        color: white;
    }

    .btn-danger:hover {
        background: #c82333;
        transform: translateY(-2px);
    }

    .empty-cart {
        text-align: center;
        padding: 60px 20px;
        background: var(--muted);
        border-radius: 15px;
        margin-bottom: 30px;
        transition: all 0.3s ease;
    }

    .empty-cart:hover {
        transform: translateY(-5px);
        box-shadow: 0 8px 25px var(--shadow-hover);
    }

    .empty-cart i {
        font-size: 4rem;
        color: var(--text-light);
        margin-bottom: 20px;
        transition: all 0.3s ease;
    }

    .empty-cart:hover i {
        transform: scale(1.1);
        color: var(--accent);
    }

    .empty-cart h2 {
        color: var(--text);
        margin-bottom: 15px;
    }

    .empty-cart p {
        color: var(--text-light);
        margin-bottom: 30px;
    }

    /* Notification Messages */
    .notification-message {
        position: fixed;
        top: 20px;
        left: 50%;
        transform: translateX(-50%);
        z-index: 9999;
        max-width: 500px;
        width: 90%;
        border-radius: 10px;
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
        animation: slideDownFadeIn 0.5s ease-out, fadeOut 0.5s ease-in 4s forwards;
        pointer-events: auto;
        will-change: transform, opacity;
        display: block !important;
        visibility: visible !important;
    }

    .notification-content {
        display: flex;
        align-items: center;
        padding: 15px 20px;
        font-weight: 500;
    }

    .notification-content i {
        margin-right: 10px;
        font-size: 18px;
    }

    .success-message {
        background: linear-gradient(135deg, #d4edda 0%, #c3e6cb 100%);
        color: #155724;
        border-left: 4px solid #28a745;
    }

    .error-message {
        background: linear-gradient(135deg, #f8d7da 0%, #f5c6cb 100%);
        color: #721c24;
        border-left: 4px solid #dc3545;
    }

    /* Simple Animations */
    @keyframes slideDownFadeIn {
        from {
            transform: translateX(-50%) translateY(-100%);
            opacity: 0;
        }
        to {
            transform: translateX(-50%) translateY(0);
            opacity: 1;
        }
    }

    @keyframes fadeOut {
        from {
            opacity: 1;
        }
        to {
            opacity: 0;
        }
    }

    .notification-message.hide {
        animation: slideUpFadeOut 0.3s ease-in forwards;
    }

    @keyframes slideUpFadeOut {
        from {
            transform: translateX(-50%) translateY(0);
            opacity: 1;
        }
        to {
            transform: translateX(-50%) translateY(-100%);
            opacity: 0;
        }
    }

    .alert {
        padding: 15px 20px;
        border-radius: 10px;
        margin-bottom: 20px;
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .alert-success {
        background: #d4edda;
        color: #155724;
    }

    .alert-danger {
        background: #f8d7da;
        color: #721c24;
    }

    @media (max-width: 768px) {
        .cart-header {
            flex-direction: column;
            gap: 20px;
            text-align: center;
        }

        .cart-header .cart-actions {
            flex-direction: column;
            width: 100%;
        }

        .cart-table {
            display: block;
            overflow-x: auto;
        }

        .product-info {
            flex-direction: column;
            text-align: center;
        }

        .quantity-control {
            justify-content: center;
        }

        .btn {
            width: 100%;
            justify-content: center;
        }

        /* Mobile notification adjustments */
        .notification-message {
            top: 10px;
            width: 95%;
            max-width: none;
        }

        .notification-content {
            padding: 12px 15px;
            font-size: 14px;
        }

        .notification-content i {
            font-size: 16px;
        }
    }
</style>

<div class="cart-container">

    <!-- Success/Error Messages with CSS Animation -->
    <c:if test="${not empty message}">
        <div class="notification-message success-message">
            <div class="notification-content">
                <i class="fas fa-check-circle"></i>
                <span>${message}</span>
            </div>
        </div>
        <!-- Debug: Message displayed -->
    </c:if>
    
    <c:if test="${not empty error}">
        <div class="notification-message error-message">
            <div class="notification-content">
                <i class="fas fa-exclamation-circle"></i>
                <span>${error}</span>
            </div>
        </div>
        <!-- Debug: Error displayed -->
    </c:if>
    
    <!-- Debug: Check if messages exist -->
    <c:if test="${empty message && empty error}">
        <!-- Debug: No messages to display -->
    </c:if>

    <!-- Cart Header -->
    <div class="cart-header">
        <h1><i class="fas fa-shopping-cart me-3"></i>Giỏ hàng của bạn</h1>
        <div class="cart-actions">
            <a href="${pageContext.request.contextPath}/products" class="btn btn-outline">
                <i class="fas fa-arrow-left"></i>
                Tiếp tục mua sắm
            </a>
            <c:if test="${not empty cart}">
                <form action="${pageContext.request.contextPath}/cart/clear" method="post" style="display: inline;">
                    <button type="submit" class="btn btn-danger"
                            onclick="return confirm('Bạn có chắc muốn xóa tất cả sản phẩm khỏi giỏ hàng? Hành động này không thể hoàn tác.')">
                        <i class="fas fa-trash"></i>
                        Xóa giỏ hàng
                    </button>
                </form>
            </c:if>
        </div>
    </div>

    <!-- Cart Content -->
    <c:choose>
        <c:when test="${empty cart}">
            <div class="empty-cart">
                <i class="fas fa-shopping-cart"></i>
                <h2>Giỏ hàng trống</h2>
                <p>Hãy thêm sản phẩm vào giỏ hàng để tiếp tục.</p>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">
                    <i class="fas fa-shopping-bag"></i>
                    Xem sản phẩm
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="cart-table">
                <table>
                    <thead>
                        <tr>
                            <th>Sản phẩm</th>
                            <th>Số lượng</th>
                            <th>Đơn giá</th>
                            <th>Thành tiền</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${cart}">
                            <tr>
                                <td>
                                    <div class="product-info">
                                        <img src="${pageContext.request.contextPath}/images/products/${item.productId}.png"
                                             onerror="this.src='${pageContext.request.contextPath}/images/placeholder.png';"
                                             alt="${fn:escapeXml(item.productName)}"
                                             class="product-image"
                                             loading="lazy"/>
                                        <div class="product-details">
                                            <h3>${fn:escapeXml(item.productName)}</h3>
                                            <p>Đơn vị: ${fn:escapeXml(item.unit)}</p>
                                        </div>
                                    </div>
                                </td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/cart/update" 
                                          method="post" 
                                          class="quantity-control">
                                        <input type="hidden" name="productId" value="${item.productId}"/>
                                        <input type="number" 
                                               name="quantity" 
                                               value="${item.quantity}"
                                               min="1"
                                               class="quantity-input"
                                               required/>
                                        <button type="submit" class="btn btn-outline" title="Cập nhật số lượng">
                                            <i class="fas fa-sync-alt"></i>
                                        </button>
                                    </form>
                                </td>
                                <td class="price">
                                    <fmt:formatNumber value="${item.price}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                                </td>
                                <td class="subtotal">
                                    <fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                                </td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/cart/remove" method="post" style="display: inline;">
                                        <input type="hidden" name="productId" value="${item.productId}"/>
                                        <button type="submit" class="btn btn-danger"
                                                onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này?')">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- Cart Summary -->
            <div class="cart-summary">
                <div class="summary-row">
                    <span class="summary-label">Tổng sản phẩm:</span>
                    <span class="summary-value">${cart.size()} sản phẩm</span>
                </div>
                <div class="summary-row total-row">
                    <span class="summary-label">Tổng tiền:</span>
                    <span class="summary-value">
                        <fmt:formatNumber value="${total}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                    </span>
                </div>
                <div class="summary-row" style="margin-top: 20px;">
                    <c:choose>
                        <c:when test="${not empty cart && cart.size() > 0}">
                            <a href="${pageContext.request.contextPath}/checkout?type=cart" class="btn btn-primary" style="width: 100%;">
                                <i class="fas fa-credit-card me-2"></i>
                                Tiến hành thanh toán
                            </a>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-primary" style="width: 100%;" disabled>
                                <i class="fas fa-credit-card me-2"></i>
                                Giỏ hàng trống
                            </button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<!-- Clear session messages after displaying -->
<c:if test="${not empty message}">
    <c:remove var="message" scope="session"/>
</c:if>
<c:if test="${not empty error}">
    <c:remove var="error" scope="session"/>
</c:if>

<!-- No JavaScript needed - using pure JSP + CSS -->

<%@ include file="/views/common/footer.jsp" %>