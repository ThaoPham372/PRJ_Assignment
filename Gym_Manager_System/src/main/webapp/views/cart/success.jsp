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
        --success: #28a745;
        --danger: #dc3545;
        --warning: #ffc107;
        --info: #17a2b8;
        --gradient-primary: linear-gradient(135deg, #141a46 0%, #1e2a5c 100%);
        --gradient-accent: linear-gradient(135deg, #ec8b5e 0%, #d67a4f 100%);
    }

    .success-page {
        background: linear-gradient(135deg, #f5f7fa 0%, #e9ecef 100%);
        min-height: 100vh;
        padding: 30px 0;
    }

    .success-container {
        max-width: 900px;
        margin: 0 auto;
        padding: 0 20px;
    }

    .success-header {
        background: var(--gradient-primary);
        color: white;
        padding: 40px;
        border-radius: 15px;
        margin-bottom: 30px;
        text-align: center;
        box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
    }

    .success-icon {
        font-size: 80px;
        margin-bottom: 20px;
        animation: bounce 1s ease-in-out;
    }

    @keyframes bounce {
        0%, 20%, 50%, 80%, 100% {
            transform: translateY(0);
        }
        40% {
            transform: translateY(-20px);
        }
        60% {
            transform: translateY(-10px);
        }
    }

    .success-title {
        color: white;
        font-size: 2.5rem;
        font-weight: 700;
        margin-bottom: 15px;
    }

    .success-subtitle {
        color: rgba(255, 255, 255, 0.9);
        font-size: 1.1rem;
        margin: 0;
    }

    .success-card {
        background: var(--card);
        border-radius: 15px;
        box-shadow: 0 4px 20px var(--shadow);
        padding: 30px;
        margin-bottom: 20px;
    }

    .card-title {
        color: var(--primary);
        font-size: 1.4rem;
        font-weight: 700;
        margin-bottom: 20px;
        padding-bottom: 15px;
        border-bottom: 3px solid var(--accent);
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .info-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        gap: 20px;
        margin-bottom: 20px;
    }

    .info-item {
        display: flex;
        flex-direction: column;
        padding: 15px;
        background: var(--muted);
        border-radius: 10px;
        border-left: 4px solid var(--accent);
    }

    .info-label {
        font-size: 0.85rem;
        color: var(--text-light);
        font-weight: 600;
        margin-bottom: 5px;
        text-transform: uppercase;
        letter-spacing: 0.5px;
    }

    .info-value {
        font-size: 1rem;
        color: var(--text);
        font-weight: 600;
    }

    .status-badge {
        display: inline-block;
        padding: 6px 14px;
        border-radius: 20px;
        font-size: 0.85rem;
        font-weight: 600;
        text-transform: uppercase;
        letter-spacing: 0.5px;
    }

    .status-badge.pending {
        background: #fff3cd;
        color: #856404;
    }

    .status-badge.confirmed {
        background: #d4edda;
        color: #155724;
    }

    .status-badge.paid {
        background: #d4edda;
        color: #155724;
    }

    .status-badge.failed {
        background: #f8d7da;
        color: #721c24;
    }

    .payment-item {
        background: var(--muted);
        border-radius: 10px;
        padding: 15px;
        margin-bottom: 10px;
        border-left: 4px solid var(--accent);
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .payment-info {
        flex: 1;
    }

    .payment-method-name {
        font-weight: 600;
        color: var(--text);
        margin-bottom: 5px;
    }

    .payment-date {
        font-size: 0.85rem;
        color: var(--text-light);
    }

    .payment-amount {
        font-size: 1.2rem;
        font-weight: 700;
        color: var(--primary);
    }

    .items-list {
        margin-top: 20px;
    }

    .item-row {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 12px 0;
        border-bottom: 1px solid var(--muted);
    }

    .item-row:last-child {
        border-bottom: none;
    }

    .item-info {
        flex: 1;
    }

    .item-name {
        font-weight: 600;
        color: var(--text);
        margin-bottom: 5px;
    }

    .item-details {
        font-size: 0.9rem;
        color: var(--text-light);
    }

    .item-price {
        font-weight: 600;
        color: var(--primary);
    }

    .total-section {
        background: var(--muted);
        border-radius: 10px;
        padding: 20px;
        margin-top: 20px;
    }

    .total-row {
        display: flex;
        justify-content: space-between;
        padding: 10px 0;
        border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    }

    .total-row:last-child {
        border-bottom: none;
        margin-top: 10px;
        padding-top: 15px;
        border-top: 2px solid var(--accent);
    }

    .total-label {
        font-weight: 600;
        color: var(--text);
    }

    .total-value {
        font-weight: 700;
        color: var(--primary);
        font-size: 1.3rem;
    }

    .notification-box {
        background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
        border-left: 4px solid var(--info);
        border-radius: 10px;
        padding: 20px;
        margin: 20px 0;
    }

    .notification-box p {
        margin: 0;
        color: var(--text);
        font-weight: 600;
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .action-buttons {
        display: flex;
        gap: 15px;
        margin-top: 30px;
        padding-top: 20px;
        border-top: 2px solid var(--muted);
        flex-wrap: wrap;
    }

    .btn {
        padding: 14px 28px;
        border-radius: 8px;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 8px;
        font-weight: 600;
        transition: all 0.3s ease;
        border: none;
        cursor: pointer;
        font-size: 1rem;
    }

    .btn-primary {
        background: var(--gradient-accent);
        color: white;
        box-shadow: 0 4px 15px rgba(236, 139, 94, 0.3);
    }

    .btn-primary:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(236, 139, 94, 0.4);
    }

    .btn-secondary {
        background: white;
        color: var(--primary);
        border: 2px solid var(--primary);
    }

    .btn-secondary:hover {
        background: var(--primary);
        color: white;
        transform: translateY(-2px);
    }

    .empty-state {
        text-align: center;
        padding: 60px 20px;
        color: var(--text-light);
    }

    .empty-state i {
        font-size: 5em;
        margin-bottom: 20px;
        opacity: 0.3;
    }

    @media (max-width: 768px) {
        .success-header {
            padding: 30px 20px;
        }

        .success-title {
            font-size: 2rem;
        }

        .info-grid {
            grid-template-columns: 1fr;
        }

        .action-buttons {
            flex-direction: column;
        }

        .btn {
            width: 100%;
            justify-content: center;
        }
    }
</style>

<div class="success-page">
    <div class="success-container">
        <!-- Back to Dashboard Button -->
        <div class="mb-4">
            <a href="${pageContext.request.contextPath}/member/dashboard" class="btn-back">
                <i class="fas fa-arrow-left"></i>
                <span>Quay lại Dashboard</span>
            </a>
        </div>

        <!-- Success Header -->
        <div class="success-header">
            <div class="success-icon">✅</div>
            <h1 class="success-title">Đặt Hàng Thành Công!</h1>
            <p class="success-subtitle">
                Cảm ơn bạn đã mua sắm tại cửa hàng của chúng tôi!
            </p>
        </div>

        <c:choose>
            <c:when test="${not empty order}">
                <!-- Order Information -->
                <div class="success-card">
                    <h2 class="card-title">
                        <i class="fas fa-receipt"></i>
                        Thông Tin Đơn Hàng
                    </h2>
                    
                    <div class="info-grid">
                        <div class="info-item">
                            <span class="info-label">Mã Đơn Hàng</span>
                            <span class="info-value">#${fn:escapeXml(order.orderNumber)}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">Ngày Đặt Hàng</span>
                            <span class="info-value">
                                <c:choose>
                                    <c:when test="${order.orderDate != null}">
                                        <fmt:formatDate value="${order.orderDateAsDate}" pattern="dd/MM/yyyy HH:mm" />
                                    </c:when>
                                    <c:otherwise>N/A</c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">Trạng Thái Đơn Hàng</span>
                            <span class="info-value">
                                <span class="status-badge ${fn:toLowerCase(order.orderStatus.code)}">
                                    ${order.orderStatus.displayName}
                                </span>
                            </span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">Họ Và Tên Người Nhận</span>
                            <span class="info-value">
                                ${fn:escapeXml(order.deliveryName != null ? order.deliveryName : 'N/A')}
                            </span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">Số Điện Thoại</span>
                            <span class="info-value">
                                ${fn:escapeXml(order.deliveryPhone != null ? order.deliveryPhone : 'N/A')}
                            </span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">Phương Thức Nhận Hàng</span>
                            <span class="info-value">
                                <c:choose>
                                    <c:when test="${order.deliveryMethod != null}">
                                        ${order.deliveryMethod.displayName}
                                    </c:when>
                                    <c:otherwise>N/A</c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                        <c:if test="${order.deliveryMethod != null && order.deliveryMethod.code == 'DELIVERY' && order.deliveryAddress != null}">
                            <div class="info-item" style="grid-column: 1 / -1;">
                                <span class="info-label">Địa Chỉ Giao Hàng</span>
                                <span class="info-value">${fn:escapeXml(order.deliveryAddress)}</span>
                            </div>
                        </c:if>
                    </div>
                </div>

                <!-- Payment Information -->
                <c:if test="${not empty payments}">
                    <div class="success-card">
                        <h2 class="card-title">
                            <i class="fas fa-credit-card"></i>
                            Thông Tin Thanh Toán
                        </h2>
                        
                        <c:forEach var="payment" items="${payments}">
                            <div class="payment-item">
                                <div class="payment-info">
                                    <div class="payment-method-name">
                                        <i class="fas fa-${payment.method.code == 'CASH' ? 'money-bill' : payment.method.code == 'MOMO' ? 'mobile-alt' : payment.method.code == 'VNPAY' ? 'credit-card' : 'university'}"></i>
                                        ${payment.method.displayName}
                                    </div>
                                    <div class="payment-date">
                                        <c:if test="${payment.paymentDate != null}">
                                            <fmt:formatDate value="${payment.paymentDateAsDate}" pattern="dd/MM/yyyy HH:mm" />
                                        </c:if>
                                        <c:if test="${payment.referenceId != null && !empty payment.referenceId}">
                                            <br><small>Mã GD: ${fn:escapeXml(payment.referenceId)}</small>
                                        </c:if>
                                    </div>
                                </div>
                                <div style="text-align: right;">
                                    <div class="payment-amount">
                                        <fmt:formatNumber value="${payment.amount}" type="number" maxFractionDigits="0" />đ
                                    </div>
                                    <div style="margin-top: 5px;">
                                        <span class="status-badge ${fn:toLowerCase(payment.status.code)}">
                                            ${payment.status.displayName}
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>

                <!-- Order Items -->
                <c:if test="${not empty order.items}">
                    <div class="success-card">
                        <h2 class="card-title">
                            <i class="fas fa-box"></i>
                            Sản Phẩm Đã Đặt
                        </h2>
                        
                        <div class="items-list">
                            <c:forEach var="item" items="${order.items}">
                                <div class="item-row">
                                    <div class="item-info">
                                        <div class="item-name">${fn:escapeXml(item.productName)}</div>
                                        <div class="item-details">
                                            Số lượng: ${item.quantity}
                                            <c:if test="${item.discountAmount != null && item.discountAmount > 0}">
                                                | Giảm: <fmt:formatNumber value="${item.discountAmount}" type="number" maxFractionDigits="0" />đ
                                            </c:if>
                                        </div>
                                    </div>
                                    <div class="item-price">
                                        <fmt:formatNumber value="${item.subtotal != null ? item.subtotal : (item.unitPrice * item.quantity - (item.discountAmount != null ? item.discountAmount : 0))}" type="number" maxFractionDigits="0" />đ
                                    </div>
                                </div>
                            </c:forEach>
                        </div>

                        <!-- Total Summary -->
                        <div class="total-section">
                            <div class="total-row">
                                <span class="total-label">Tổng Tiền Hàng</span>
                                <span class="total-value">
                                    <fmt:formatNumber value="${order.totalAmount}" type="number" maxFractionDigits="0" />đ
                                </span>
                            </div>
                            <c:if test="${order.discountAmount != null && order.discountAmount > 0}">
                                <div class="total-row">
                                    <span class="total-label">Giảm Giá</span>
                                    <span class="total-value" style="color: var(--danger);">
                                        -<fmt:formatNumber value="${order.discountAmount}" type="number" maxFractionDigits="0" />đ
                                    </span>
                                </div>
                            </c:if>
                            <div class="total-row">
                                <span class="total-label">Thành Tiền</span>
                                <span class="total-value" style="color: var(--accent); font-size: 1.5rem;">
                                    <fmt:formatNumber value="${order.totalAmount - (order.discountAmount != null ? order.discountAmount : 0)}" type="number" maxFractionDigits="0" />đ
                                </span>
                            </div>
                        </div>
                    </div>
                </c:if>

                <!-- Notification Box -->
                <div class="notification-box">
                    <p>
                        <i class="fas fa-envelope"></i>
                        <span>
                            <strong>📧 Email xác nhận đã được gửi đến hộp thư của bạn!</strong><br>
                            Đơn hàng của bạn đang được xử lý và sẽ được giao trong thời gian sớm nhất.
                        </span>
                    </p>
                </div>

                <!-- Action Buttons -->
                <div class="success-card">
                    <div class="action-buttons">
                        <a href="${pageContext.request.contextPath}/order/detail?orderId=${order.orderId}" class="btn btn-primary">
                            <i class="fas fa-receipt"></i>
                            Xem Chi Tiết Hóa Đơn
                        </a>
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-secondary">
                            <i class="fas fa-shopping-bag"></i>
                            Tiếp Tục Mua Sắm
                        </a>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <!-- Fallback if order is not loaded -->
                <div class="success-card">
                    <div class="empty-state">
                        <i class="fas fa-check-circle" style="color: var(--success);"></i>
                        <h2>Đơn Hàng Đã Được Tạo Thành Công!</h2>
                        <p>Mã đơn hàng: <strong>#${fn:escapeXml(param.orderNumber)}</strong></p>
                        <div class="action-buttons" style="justify-content: center; margin-top: 30px;">
                            <a href="${pageContext.request.contextPath}/order/list" class="btn btn-primary">
                                <i class="fas fa-list"></i>
                                Xem Danh Sách Đơn Hàng
                            </a>
                            <a href="${pageContext.request.contextPath}/products" class="btn btn-secondary">
                                <i class="fas fa-shopping-bag"></i>
                                Tiếp Tục Mua Sắm
                            </a>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<%@ include file="/views/common/footer.jsp" %>
