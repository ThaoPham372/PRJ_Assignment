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

    .orders-container {
        max-width: 1200px;
        margin: 40px auto;
        padding: 0 20px;
    }

    .orders-header {
        background: var(--gradient-primary);
        color: white;
        padding: 30px;
        border-radius: 15px;
        margin-bottom: 30px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .orders-header h1 {
        margin: 0;
        font-size: 2rem;
        font-weight: 700;
    }

    .orders-list {
        display: flex;
        flex-direction: column;
        gap: 20px;
    }

    .order-card {
        background: var(--card);
        border-radius: 15px;
        box-shadow: 0 2px 10px var(--shadow);
        padding: 25px;
        transition: all 0.3s ease;
    }

    .order-card:hover {
        box-shadow: 0 4px 20px var(--shadow-hover);
        transform: translateY(-2px);
    }

    .order-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        margin-bottom: 20px;
        padding-bottom: 15px;
        border-bottom: 2px solid var(--muted);
    }

    .order-info-left {
        flex: 1;
    }

    .order-number {
        font-size: 1.3rem;
        font-weight: 700;
        color: var(--primary);
        margin-bottom: 8px;
    }

    .order-date {
        color: var(--text-light);
        font-size: 0.9rem;
    }

    .order-status-badges {
        display: flex;
        flex-direction: column;
        gap: 8px;
        align-items: flex-end;
    }

    .status-badge {
        padding: 6px 12px;
        border-radius: 20px;
        font-size: 0.85rem;
        font-weight: 600;
        white-space: nowrap;
    }

    .status-pending {
        background: #fff3cd;
        color: #856404;
    }

    .status-paid {
        background: #d4edda;
        color: #155724;
    }

    .status-processing {
        background: #cfe2ff;
        color: #084298;
    }

    .status-shipped {
        background: #d1ecf1;
        color: #0c5460;
    }

    .status-delivered {
        background: #d4edda;
        color: #155724;
    }

    .status-cancelled {
        background: #f8d7da;
        color: #721c24;
    }

    .status-completed {
        background: #d4edda;
        color: #155724;
    }

    .order-items {
        margin: 15px 0;
    }

    .order-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 10px 0;
        border-bottom: 1px solid var(--muted);
    }

    .order-item:last-child {
        border-bottom: none;
    }

    .item-info {
        flex: 1;
        display: flex;
        align-items: center;
        gap: 15px;
    }

    .item-name {
        font-weight: 600;
        color: var(--text);
    }

    .item-quantity {
        color: var(--text-light);
        font-size: 0.9rem;
    }

    .item-price {
        font-weight: 600;
        color: var(--primary);
    }

    .order-summary {
        margin-top: 20px;
        padding-top: 20px;
        border-top: 2px solid var(--muted);
    }

    .summary-row {
        display: flex;
        justify-content: space-between;
        margin: 8px 0;
        color: var(--text);
    }

    .summary-label {
        font-weight: 600;
    }

    .summary-value {
        font-weight: 700;
    }

    .total-amount {
        font-size: 1.2rem;
        color: var(--primary);
        margin-top: 10px;
        padding-top: 10px;
        border-top: 2px solid var(--primary);
    }

    .order-actions {
        margin-top: 20px;
        display: flex;
        gap: 10px;
        justify-content: flex-end;
    }

    .btn {
        padding: 10px 20px;
        border-radius: 8px;
        text-decoration: none;
        font-weight: 600;
        transition: all 0.3s ease;
        border: none;
        cursor: pointer;
        display: inline-block;
    }

    .btn-primary {
        background: var(--gradient-primary);
        color: white;
    }

    .btn-primary:hover {
        opacity: 0.9;
        transform: translateY(-2px);
    }

    .btn-outline {
        background: transparent;
        color: var(--primary);
        border: 2px solid var(--primary);
    }

    .btn-outline:hover {
        background: var(--primary);
        color: white;
    }

    .empty-state {
        text-align: center;
        padding: 60px 20px;
        background: var(--card);
        border-radius: 15px;
        box-shadow: 0 2px 10px var(--shadow);
    }

    .empty-state-icon {
        font-size: 4rem;
        color: var(--text-light);
        margin-bottom: 20px;
    }

    .empty-state h2 {
        color: var(--text);
        margin-bottom: 10px;
    }

    .empty-state p {
        color: var(--text-light);
        margin-bottom: 30px;
    }

    .payment-method {
        display: inline-flex;
        align-items: center;
        gap: 5px;
        color: var(--text-light);
        font-size: 0.9rem;
    }
</style>

<div class="orders-container">
    <!-- Back to Dashboard Button -->
    <div class="mb-4">
        <a href="${pageContext.request.contextPath}/member/dashboard" class="btn-back">
            <i class="fas fa-arrow-left"></i>
            <span>Quay lại Dashboard</span>
        </a>
    </div>

    <div class="orders-header">
        <h1><i class="fas fa-receipt"></i> Danh sách đơn hàng</h1>
        <a href="${pageContext.request.contextPath}/services" class="btn btn-outline" style="color: white; border-color: white;">
            <i class="fas fa-shopping-bag"></i> Tiếp tục mua sắm
        </a>
    </div>

    <!-- Success/Error Messages -->
    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success alert-dismissible fade show" role="alert" style="margin-bottom: 20px; background: #d4edda; color: #155724; padding: 15px; border-radius: 8px; border: 1px solid #c3e6cb;">
            <i class="fas fa-check-circle"></i> ${fn:escapeXml(sessionScope.success)}
            <button type="button" class="btn-close" onclick="this.parentElement.remove()" style="float: right; background: none; border: none; font-size: 1.2rem; cursor: pointer;">&times;</button>
        </div>
        <c:remove var="success" scope="session" />
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert" style="margin-bottom: 20px; background: #f8d7da; color: #721c24; padding: 15px; border-radius: 8px; border: 1px solid #f5c6cb;">
            <i class="fas fa-exclamation-circle"></i> ${fn:escapeXml(sessionScope.error)}
            <button type="button" class="btn-close" onclick="this.parentElement.remove()" style="float: right; background: none; border: none; font-size: 1.2rem; cursor: pointer;">&times;</button>
        </div>
        <c:remove var="error" scope="session" />
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert" style="margin-bottom: 20px; background: #f8d7da; color: #721c24; padding: 15px; border-radius: 8px; border: 1px solid #f5c6cb;">
            <i class="fas fa-exclamation-circle"></i> ${fn:escapeXml(error)}
        </div>
    </c:if>

    <!-- Debug Info (remove in production) -->
    <c:if test="${empty orders}">
        <div style="background: #fff3cd; padding: 15px; border-radius: 8px; margin-bottom: 20px; color: #856404;">
            <strong>Debug:</strong> Orders list is empty or null. 
            <c:if test="${orders == null}">Orders is NULL</c:if>
            <c:if test="${orders != null}">Orders size: ${fn:length(orders)}</c:if>
        </div>
    </c:if>

    <c:choose>
        <c:when test="${not empty orders and fn:length(orders) > 0}">
            <div class="orders-list">
                <c:forEach var="order" items="${orders}">
                    <div class="order-card">
                        <div class="order-header">
                            <div class="order-info-left">
                                <div class="order-number">
                                    <i class="fas fa-hashtag"></i> ${fn:escapeXml(order.orderNumber)}
                                </div>
                                <div class="order-date">
                                    <i class="far fa-calendar"></i> 
                                    <c:choose>
                                        <c:when test="${order.orderDate != null}">
                                            <fmt:formatDate value="${order.orderDateAsDate}" pattern="dd/MM/yyyy HH:mm" />
                                        </c:when>
                                        <c:otherwise>
                                            N/A
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="payment-method" style="margin-top: 8px;">
                                    <i class="fas fa-credit-card"></i> 
                                    <c:set var="orderPayments" value="${paymentsMap[order.orderId]}" />
                                    <c:choose>
                                        <c:when test="${not empty orderPayments}">
                                            <c:forEach items="${orderPayments}" var="payment" varStatus="loop">
                                                ${payment.method.displayName}<c:if test="${!loop.last}">, </c:if>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            N/A
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="order-status-badges">
                                <c:choose>
                                    <c:when test="${order.orderStatus != null}">
                                        <span class="status-badge status-${fn:toLowerCase(order.orderStatus.code)}">
                                            <i class="fas fa-${order.orderStatus.code == 'COMPLETED' ? 'check-circle' : order.orderStatus.code == 'CANCELLED' ? 'times-circle' : 'clock'}"></i>
                                            ${order.orderStatus.displayName}
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-badge status-pending">
                                            <i class="fas fa-clock"></i>
                                            Chờ thanh toán
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <c:if test="${not empty order.items}">
                            <div class="order-items">
                                <c:forEach var="item" items="${order.items}">
                                    <div class="order-item">
                                        <div class="item-info">
                                            <span class="item-name">
                                                ${fn:escapeXml(item.productName != null ? item.productName : 'Gói thành viên')}
                                            </span>
                                            <span class="item-quantity">
                                                x ${item.quantity}
                                            </span>
                                        </div>
                                        <div class="item-price">
                                            <fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:if>

                        <div class="order-summary">
                            <div class="summary-row">
                                <span class="summary-label">Tổng tiền hàng:</span>
                                <span class="summary-value">
                                    <fmt:formatNumber value="${order.totalAmount != null ? order.totalAmount : 0}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                                </span>
                            </div>
                            <c:if test="${order.discountAmount != null and order.discountAmount > 0}">
                                <div class="summary-row">
                                    <span class="summary-label">Giảm giá:</span>
                                    <span class="summary-value" style="color: #dc3545;">
                                        -<fmt:formatNumber value="${order.discountAmount}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                                    </span>
                                </div>
                            </c:if>
                            <div class="summary-row total-amount">
                                <span class="summary-label">Thành tiền:</span>
                                <span class="summary-value">
                                    <fmt:formatNumber value="${(order.totalAmount != null ? order.totalAmount : 0) - (order.discountAmount != null ? order.discountAmount : 0)}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                                </span>
                            </div>
                        </div>

                        <div class="order-actions">
                            <a href="${pageContext.request.contextPath}/order/detail?orderId=${order.orderId}" class="btn btn-outline">
                                <i class="fas fa-eye"></i> Xem chi tiết
                            </a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="empty-state">
                <div class="empty-state-icon">
                    <i class="fas fa-inbox"></i>
                </div>
                <h2>Chưa có đơn hàng nào</h2>
                <p>Bạn chưa có đơn hàng nào. Hãy bắt đầu mua sắm ngay!</p>
                <a href="${pageContext.request.contextPath}/services" class="btn btn-primary">
                    <i class="fas fa-shopping-bag"></i> Mua sắm ngay
                </a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/views/common/footer.jsp" %>


