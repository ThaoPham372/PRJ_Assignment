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

    .order-detail-page {
        background: linear-gradient(135deg, #f5f7fa 0%, #e9ecef 100%);
        min-height: 100vh;
        padding: 30px 0;
    }

    .order-detail-container {
        max-width: 1200px;
        margin: 0 auto;
        padding: 0 20px;
    }

    .detail-header {
        background: var(--gradient-primary);
        color: white;
        padding: 30px;
        border-radius: 15px;
        margin-bottom: 30px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .detail-header h1 {
        margin: 0;
        font-size: 2rem;
        font-weight: 700;
    }

    .btn-back {
        background: rgba(255, 255, 255, 0.2);
        color: white;
        padding: 10px 20px;
        border-radius: 8px;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 8px;
        transition: all 0.3s ease;
        border: 1px solid rgba(255, 255, 255, 0.3);
    }

    .btn-back:hover {
        background: rgba(255, 255, 255, 0.3);
        transform: translateX(-3px);
    }

    .detail-card {
        background: var(--card);
        border-radius: 15px;
        box-shadow: 0 4px 20px var(--shadow);
        padding: 30px;
        margin-bottom: 20px;
    }

    .detail-card-title {
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
        grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
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

    .status-badge.preparing {
        background: #cce5ff;
        color: #004085;
    }

    .status-badge.ready {
        background: #d1ecf1;
        color: #0c5460;
    }

    .status-badge.completed {
        background: #d4edda;
        color: #155724;
    }

    .status-badge.cancelled {
        background: #f8d7da;
        color: #721c24;
    }

    .status-badge.paid {
        background: #d4edda;
        color: #155724;
    }

    .status-badge.failed {
        background: #f8d7da;
        color: #721c24;
    }

    .items-table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
    }

    .items-table thead {
        background: var(--gradient-primary);
        color: white;
    }

    .items-table th {
        padding: 15px;
        text-align: left;
        font-weight: 600;
        text-transform: uppercase;
        font-size: 0.85rem;
        letter-spacing: 0.5px;
    }

    .items-table td {
        padding: 15px;
        border-bottom: 1px solid var(--muted);
    }

    .items-table tbody tr:hover {
        background: var(--muted);
    }

    .items-table tbody tr:last-child td {
        border-bottom: none;
    }

    .item-name {
        font-weight: 600;
        color: var(--text);
    }

    .item-price {
        color: var(--primary);
        font-weight: 600;
    }

    .summary-section {
        background: var(--muted);
        border-radius: 10px;
        padding: 20px;
        margin-top: 20px;
    }

    .summary-row {
        display: flex;
        justify-content: space-between;
        padding: 10px 0;
        border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    }

    .summary-row:last-child {
        border-bottom: none;
        margin-top: 10px;
        padding-top: 15px;
        border-top: 2px solid var(--accent);
    }

    .summary-label {
        font-weight: 600;
        color: var(--text);
    }

    .summary-value {
        font-weight: 700;
        color: var(--primary);
    }

    .summary-row.total .summary-value {
        font-size: 1.3rem;
        color: var(--accent);
    }

    .payment-section {
        margin-top: 20px;
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

    .action-buttons {
        display: flex;
        gap: 15px;
        margin-top: 30px;
        padding-top: 20px;
        border-top: 2px solid var(--muted);
    }

    .btn {
        padding: 12px 24px;
        border-radius: 8px;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 8px;
        font-weight: 600;
        transition: all 0.3s ease;
        border: none;
        cursor: pointer;
    }

    .btn-primary {
        background: var(--gradient-accent);
        color: white;
    }

    .btn-primary:hover {
        transform: translateY(-2px);
        box-shadow: 0 5px 15px rgba(236, 139, 94, 0.4);
    }

    .btn-outline {
        background: white;
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
        color: var(--text-light);
    }

    .empty-state i {
        font-size: 5em;
        margin-bottom: 20px;
        opacity: 0.3;
    }

    @media (max-width: 768px) {
        .detail-header {
            flex-direction: column;
            align-items: flex-start;
            gap: 15px;
        }

        .info-grid {
            grid-template-columns: 1fr;
        }

        .items-table {
            font-size: 0.9rem;
        }

        .items-table th,
        .items-table td {
            padding: 10px 8px;
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

<div class="order-detail-page">
    <div class="order-detail-container">
        <c:choose>
            <c:when test="${order != null}">
                <!-- Header -->
                <div class="detail-header">
                    <h1>
                        <i class="fas fa-receipt"></i>
                        Chi Tiết Đơn Hàng #${fn:escapeXml(order.orderNumber)}
                    </h1>
                    <a href="${pageContext.request.contextPath}/order/list" class="btn-back">
                        <i class="fas fa-arrow-left"></i>
                        Quay Lại
                    </a>
                </div>

                <!-- Order Information -->
                <div class="detail-card">
                    <h2 class="detail-card-title">
                        <i class="fas fa-info-circle"></i>
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
                            <span class="info-label">Tổng Tiền</span>
                            <span class="info-value">
                                <fmt:formatNumber value="${order.totalAmount}" type="number" maxFractionDigits="0" />đ
                            </span>
                        </div>
                    </div>
                </div>

                <!-- Delivery Information -->
                <div class="detail-card">
                    <h2 class="detail-card-title">
                        <i class="fas fa-truck"></i>
                        Thông Tin Giao Hàng
                    </h2>
                    
                    <div class="info-grid">
                        <div class="info-item">
                            <span class="info-label">Họ Và Tên Người Nhận</span>
                            <span class="info-value">${fn:escapeXml(order.deliveryName != null ? order.deliveryName : 'N/A')}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">Số Điện Thoại</span>
                            <span class="info-value">${fn:escapeXml(order.deliveryPhone != null ? order.deliveryPhone : 'N/A')}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">Hình Thức Giao Hàng</span>
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
                        <c:if test="${order.notes != null && !empty order.notes}">
                            <div class="info-item" style="grid-column: 1 / -1;">
                                <span class="info-label">Ghi Chú</span>
                                <span class="info-value">${fn:escapeXml(order.notes)}</span>
                            </div>
                        </c:if>
                    </div>
                </div>

                <!-- Order Items -->
                <div class="detail-card">
                    <h2 class="detail-card-title">
                        <i class="fas fa-box"></i>
                        Chi Tiết Sản Phẩm
                    </h2>
                    
                    <c:choose>
                        <c:when test="${not empty order.items}">
                            <table class="items-table">
                                <thead>
                                    <tr>
                                        <th>Sản Phẩm</th>
                                        <th>Đơn Giá</th>
                                        <th>Số Lượng</th>
                                        <th>Giảm Giá</th>
                                        <th>Thành Tiền</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${order.items}">
                                        <tr>
                                            <td>
                                                <div class="item-name">${fn:escapeXml(item.productName)}</div>
                                                <c:if test="${item.productId != null && item.productId > 0}">
                                                    <small style="color: var(--text-light);">ID: ${item.productId}</small>
                                                </c:if>
                                                <c:if test="${item.notes != null && !empty item.notes}">
                                                    <br><small style="color: var(--text-light); font-style: italic;">${fn:escapeXml(item.notes)}</small>
                                                </c:if>
                                            </td>
                                            <td class="item-price">
                                                <fmt:formatNumber value="${item.unitPrice}" type="number" maxFractionDigits="0" />đ
                                            </td>
                                            <td>${item.quantity}</td>
                                            <td>
                                                <c:if test="${item.discountAmount != null && item.discountAmount > 0}">
                                                    <span style="color: var(--danger);">
                                                        -<fmt:formatNumber value="${item.discountAmount}" type="number" maxFractionDigits="0" />đ
                                                    </span>
                                                    <c:if test="${item.discountPercent != null && item.discountPercent > 0}">
                                                        <br><small>(${item.discountPercent}%)</small>
                                                    </c:if>
                                                </c:if>
                                                <c:if test="${item.discountAmount == null || item.discountAmount == 0}">
                                                    <span style="color: var(--text-light);">-</span>
                                                </c:if>
                                            </td>
                                            <td class="item-price">
                                                <fmt:formatNumber value="${item.subtotal != null ? item.subtotal : (item.unitPrice * item.quantity - (item.discountAmount != null ? item.discountAmount : 0))}" type="number" maxFractionDigits="0" />đ
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>

                            <!-- Summary -->
                            <div class="summary-section">
                                <div class="summary-row">
                                    <span class="summary-label">Tổng Tiền Hàng</span>
                                    <span class="summary-value">
                                        <fmt:formatNumber value="${order.totalAmount}" type="number" maxFractionDigits="0" />đ
                                    </span>
                                </div>
                                <c:if test="${order.discountAmount != null && order.discountAmount > 0}">
                                    <div class="summary-row">
                                        <span class="summary-label">Giảm Giá</span>
                                        <span class="summary-value" style="color: var(--danger);">
                                            -<fmt:formatNumber value="${order.discountAmount}" type="number" maxFractionDigits="0" />đ
                                        </span>
                                    </div>
                                </c:if>
                                <div class="summary-row total">
                                    <span class="summary-label">Thành Tiền</span>
                                    <span class="summary-value">
                                        <fmt:formatNumber value="${order.totalAmount - (order.discountAmount != null ? order.discountAmount : 0)}" type="number" maxFractionDigits="0" />đ
                                    </span>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <i class="fas fa-box-open"></i>
                                <p>Không có sản phẩm nào trong đơn hàng này</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Payment Information -->
                <c:if test="${not empty payments}">
                    <div class="detail-card payment-section">
                        <h2 class="detail-card-title">
                            <i class="fas fa-credit-card"></i>
                            Lịch Sử Thanh Toán
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

                <!-- Action Buttons -->
                <div class="detail-card">
                    <div class="action-buttons">
                        <a href="${pageContext.request.contextPath}/order/list" class="btn btn-outline">
                            <i class="fas fa-list"></i>
                            Danh Sách Đơn Hàng
                        </a>
                        <c:if test="${order.orderStatus.code == 'PENDING' || order.orderStatus.code == 'pending'}">
                            <a href="${pageContext.request.contextPath}/cart" class="btn btn-primary">
                                <i class="fas fa-shopping-cart"></i>
                                Tiếp Tục Mua Sắm
                            </a>
                        </c:if>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="detail-card">
                    <div class="empty-state">
                        <i class="fas fa-exclamation-triangle"></i>
                        <h2>Không Tìm Thấy Đơn Hàng</h2>
                        <p>Đơn hàng bạn đang tìm kiếm không tồn tại hoặc đã bị xóa.</p>
                        <a href="${pageContext.request.contextPath}/order/list" class="btn btn-primary" style="margin-top: 20px;">
                            <i class="fas fa-arrow-left"></i>
                            Quay Lại Danh Sách
                        </a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<%@ include file="/views/common/footer.jsp" %>

