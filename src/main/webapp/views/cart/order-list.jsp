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
        align-items: center;
        justify-content: flex-end;
    }

    .status-badge {
        padding: 8px 16px;
        border-radius: 25px;
        font-size: 0.9rem;
        font-weight: 600;
        white-space: nowrap;
        display: inline-flex;
        align-items: center;
        gap: 6px;
    }
    
    .status-badge i {
        font-size: 0.95rem;
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

    .status-cancelled,
    .status-CANCELLED {
        background: #f8d7da;
        color: #721c24;
    }
    
    .status-completed,
    .status-COMPLETED {
        background: #d4edda;
        color: #155724;
    }
    
    .status-PENDING {
        background: #fff3cd;
        color: #856404;
    }
    
    .status-PROCESSING {
        background: #cfe2ff;
        color: #084298;
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
    
    .btn-outline[style*="border-color: #dc3545"]:hover {
        background: #dc3545 !important;
        color: white !important;
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
    
    .header-actions {
        display: flex;
        gap: 10px;
    }
    
    /* Responsive */
    @media (max-width: 768px) {
        .orders-header {
            flex-direction: column;
            gap: 20px;
        }
        
        .header-actions {
            width: 100%;
            flex-direction: column;
        }
        
        .header-actions .btn {
            width: 100%;
            text-align: center;
            justify-content: center;
        }
        
        .order-header {
            flex-direction: column;
            gap: 15px;
        }
        
        .order-status-badges {
            justify-content: flex-start;
        }
        
        .order-actions {
            flex-direction: column;
        }
        
        .order-actions .btn {
            width: 100%;
            text-align: center;
            justify-content: center;
        }
    }
</style>

<div class="orders-container">
    <!-- Success/Error Messages -->
    <c:if test="${not empty sessionScope.success}">
        <div style="background: #d4edda; color: #155724; padding: 15px; border-radius: 10px; margin-bottom: 20px; border-left: 4px solid #28a745;">
            <i class="fas fa-check-circle"></i> ${sessionScope.success}
        </div>
        <c:remove var="success" scope="session" />
    </c:if>
    
    <c:if test="${not empty sessionScope.error}">
        <div style="background: #f8d7da; color: #721c24; padding: 15px; border-radius: 10px; margin-bottom: 20px; border-left: 4px solid #dc3545;">
            <i class="fas fa-exclamation-circle"></i> ${sessionScope.error}
        </div>
        <c:remove var="error" scope="session" />
    </c:if>

    <div class="orders-header">
        <div>
            <h1><i class="fas fa-receipt"></i> Danh sách đơn hàng</h1>
            <c:if test="${not empty orders}">
                <p style="margin: 10px 0 0 0; opacity: 0.9; font-size: 0.95rem;">
                    Tổng cộng: <strong>${fn:length(orders)}</strong> đơn hàng
                </p>
            </c:if>
        </div>
        <div class="header-actions">
            <a href="${pageContext.request.contextPath}/member/dashboard" class="btn btn-outline" style="color: white; border-color: white;">
                <i class="fas fa-arrow-left"></i> Quay lại Dashboard
            </a>
            <a href="${pageContext.request.contextPath}/services" class="btn btn-outline" style="color: white; border-color: white;">
                <i class="fas fa-shopping-bag"></i> Tiếp tục mua sắm
            </a>
        </div>
    </div>

    <c:choose>
        <c:when test="${not empty orders and fn:length(orders) > 0}">
            <!-- Debug: Total orders = ${fn:length(orders)} -->
            <div class="orders-list">
                <c:forEach var="order" items="${orders}" varStatus="status">
                    <!-- Debug: Rendering order ${status.index + 1} - ${order.orderNumber} -->
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
                                            <c:catch var="dateError">
                                                <fmt:formatDate value="${order.orderDateAsDate}" pattern="dd/MM/yyyy HH:mm" />
                                            </c:catch>
                                            <c:if test="${not empty dateError}">
                                                ${order.orderDate}
                                            </c:if>
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
                                <!-- Unified Status Badge - Show only ONE badge -->
                                <c:choose>
                                    <%-- Priority 1: If order is CANCELLED, show Cancelled badge --%>
                                    <c:when test="${order.orderStatus.code == 'CANCELLED' or order.orderStatus.code == 'cancelled'}">
                                        <span class="status-badge status-CANCELLED">
                                            <i class="fas fa-ban"></i>
                                            ${order.orderStatus.displayName}
                                        </span>
                                    </c:when>
                                    
                                    <%-- Priority 2: If order is CONFIRMED, show "Đã hoàn thành" --%>
                                    <c:when test="${order.orderStatus.code == 'CONFIRMED' or order.orderStatus.code == 'confirmed'}">
                                        <span class="status-badge status-COMPLETED">
                                            <i class="fas fa-check-circle"></i>
                                            Đã hoàn thành
                                        </span>
                                    </c:when>
                                    
                                    <%-- Priority 3: If order is COMPLETED, show Completed badge --%>
                                    <c:when test="${order.orderStatus.code == 'COMPLETED' or order.orderStatus.code == 'completed' or order.orderStatus.code == 'DELIVERED' or order.orderStatus.code == 'delivered'}">
                                        <span class="status-badge status-COMPLETED">
                                            <i class="fas fa-check-circle"></i>
                                            ${order.orderStatus.displayName}
                                        </span>
                                    </c:when>
                                    
                                    <%-- Priority 4: Check payment status for PENDING/PROCESSING orders --%>
                                    <c:otherwise>
                                        <c:set var="orderPayments" value="${paymentsMap[order.orderId]}" />
                                        <c:set var="hasPaid" value="false" />
                                        <c:if test="${not empty orderPayments}">
                                            <c:forEach items="${orderPayments}" var="payment">
                                                <c:if test="${payment.status.code == 'paid'}">
                                                    <c:set var="hasPaid" value="true" />
                                                </c:if>
                                            </c:forEach>
                                        </c:if>
                                        
                                        <c:choose>
                                            <%-- If NOT paid yet, show "Chờ thanh toán" --%>
                                            <c:when test="${!hasPaid}">
                                                <span class="status-badge status-pending">
                                                    <i class="fas fa-clock"></i>
                                                    Chờ thanh toán
                                                </span>
                                            </c:when>
                                            
                                            <%-- If paid, show Order Status (PENDING/PROCESSING) --%>
                                            <c:otherwise>
                                                <span class="status-badge status-${order.orderStatus.code}">
                                                    <i class="fas fa-${order.orderStatus.code == 'PROCESSING' || order.orderStatus.code == 'processing' ? 'spinner' : order.orderStatus.code == 'shipped' ? 'truck' : 'clock'}"></i>
                                                    ${order.orderStatus.displayName}
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <c:if test="${not empty order.items}">
                            <div class="order-items">
                                <c:catch var="itemsError">
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
                                                <c:catch var="priceError">
                                                    <fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                                                </c:catch>
                                                <c:if test="${not empty priceError}">
                                                    ${item.subtotal}đ
                                                </c:if>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:catch>
                                <c:if test="${not empty itemsError}">
                                    <p style="color: #dc3545; font-size: 0.9rem;">Lỗi load sản phẩm</p>
                                </c:if>
                            </div>
                        </c:if>

                        <div class="order-summary">
                            <div class="summary-row">
                                <span class="summary-label">Tổng tiền hàng:</span>
                                <span class="summary-value">
                                    <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
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
                                    <fmt:formatNumber value="${order.finalAmount}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                                </span>
                            </div>
                        </div>

                        <div class="order-actions">
                            <c:if test="${order.orderStatus.code == 'PENDING' || order.orderStatus.code == 'pending'}">
                                <button type="button" 
                                        onclick="if(confirm('Bạn có chắc muốn hủy đơn hàng #${fn:escapeXml(order.orderNumber)}?')) { 
                                            document.getElementById('cancelForm${order.orderId}').submit(); 
                                        }"
                                        class="btn btn-outline" 
                                        style="border-color: #dc3545; color: #dc3545;">
                                    <i class="fas fa-times-circle"></i> Hủy đơn
                                </button>
                                <form id="cancelForm${order.orderId}" 
                                      action="${pageContext.request.contextPath}/order/cancel" 
                                      method="post" 
                                      style="display: none;">
                                    <input type="hidden" name="orderId" value="${order.orderId}">
                                </form>
                            </c:if>
                            <a href="${pageContext.request.contextPath}/order/detail?orderId=${order.orderId}" 
                               class="btn btn-primary">
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

<script>
// Debug: Check how many order cards are rendered
document.addEventListener('DOMContentLoaded', function() {
    const orderCards = document.querySelectorAll('.order-card');
    console.log('========================================');
    console.log('🔍 ORDER LIST DEBUG INFO:');
    console.log('Total order cards in DOM:', orderCards.length);
    console.log('Expected orders: ${fn:length(orders)}');
    
    if (orderCards.length < ${fn:length(orders)}) {
        console.error('❌ MISMATCH: Expected ${fn:length(orders)} orders but only ' + orderCards.length + ' cards rendered!');
        console.error('This indicates a JSP rendering issue or exception during loop');
    } else {
        console.log('✅ All orders rendered successfully');
    }
    
    // Log each order card
    orderCards.forEach((card, index) => {
        const orderNumber = card.querySelector('.order-number');
        console.log(`Order ${index + 1}:`, orderNumber ? orderNumber.textContent.trim() : 'Unknown');
    });
    console.log('========================================');
});
</script>

