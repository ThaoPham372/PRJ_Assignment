<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thanh toán thành công</title>
    <style>
        body { 
            font-family: Arial, sans-serif; 
            margin: 20px;
            background-color: #f5f5f5;
        }
        .success-container {
            max-width: 600px;
            margin: 50px auto;
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            text-align: center;
        }
        .success-icon {
            font-size: 60px;
            color: #4CAF50;
            margin-bottom: 20px;
        }
        .success-title {
            color: #4CAF50;
            font-size: 28px;
            margin-bottom: 20px;
        }
        .order-info {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin: 20px 0;
            text-align: left;
        }
        .order-info h3 {
            color: #333;
            margin-bottom: 15px;
        }
        .info-row {
            display: flex;
            justify-content: space-between;
            margin: 10px 0;
            padding: 8px 0;
            border-bottom: 1px solid #eee;
        }
        .info-label {
            font-weight: bold;
            color: #666;
        }
        .info-value {
            color: #333;
        }
        .total-row {
            background-color: #e8f5e8;
            font-weight: bold;
            font-size: 18px;
            color: #4CAF50;
            margin-top: 15px;
            padding: 15px;
            border-radius: 5px;
        }
        .action-buttons {
            margin-top: 30px;
        }
        .btn {
            display: inline-block;
            padding: 12px 24px;
            margin: 0 10px;
            text-decoration: none;
            border-radius: 6px;
            font-weight: bold;
            transition: background-color 0.3s;
        }
        .btn-primary {
            background-color: #4CAF50;
            color: white;
        }
        .btn-primary:hover {
            background-color: #45a049;
        }
        .btn-secondary {
            background-color: #2196F3;
            color: white;
        }
        .btn-secondary:hover {
            background-color: #1976D2;
        }
    </style>
</head>
<body>
    <div class="success-container">
        <div class="success-icon">✅</div>
        <h1 class="success-title">Đặt hàng thành công!</h1>
        <p style="color: #666; font-size: 16px; margin-bottom: 30px;">
            Cảm ơn bạn đã mua sắm tại cửa hàng của chúng tôi!
        </p>
        
        <c:choose>
            <c:when test="${not empty order}">
                <div class="order-info">
                    <h3>📋 Thông tin đơn hàng</h3>
                    <div class="info-row">
                        <span class="info-label">Mã đơn hàng:</span>
                        <span class="info-value">#${order.orderNumber}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Ngày đặt hàng:</span>
                        <span class="info-value">
                            <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/>
                        </span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Họ và tên người nhận:</span>
                        <span class="info-value">${fn:escapeXml(order.deliveryName)}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Số điện thoại:</span>
                        <span class="info-value">${fn:escapeXml(order.deliveryPhone)}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Phương thức nhận hàng:</span>
                        <span class="info-value">${order.deliveryMethod.displayName}</span>
                    </div>
                    <c:if test="${order.deliveryMethod.code == 'delivery' && not empty order.deliveryAddress}">
                        <div class="info-row">
                            <span class="info-label">Địa chỉ giao hàng:</span>
                            <span class="info-value">${fn:escapeXml(order.deliveryAddress)}</span>
                        </div>
                    </c:if>
                    <div class="info-row">
                        <span class="info-label">Phương thức thanh toán:</span>
                        <span class="info-value">${order.paymentMethod.displayName}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Trạng thái thanh toán:</span>
                        <span class="info-value">
                            <c:choose>
                                <c:when test="${order.paymentStatus.code == 'pending'}">
                                    <span style="color: #ffc107; font-weight: bold;">${order.paymentStatus.displayName}</span>
                                </c:when>
                                <c:when test="${order.paymentStatus.code == 'paid'}">
                                    <span style="color: #28a745; font-weight: bold;">${order.paymentStatus.displayName}</span>
                                </c:when>
                                <c:otherwise>
                                    ${order.paymentStatus.displayName}
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Trạng thái đơn hàng:</span>
                        <span class="info-value">${order.orderStatus.displayName}</span>
                    </div>
                    <c:if test="${not empty order.items}">
                        <div style="margin-top: 20px;">
                            <h4 style="margin-bottom: 10px;">Sản phẩm:</h4>
                            <c:forEach var="item" items="${order.items}">
                                <div style="padding: 8px 0; border-bottom: 1px solid #eee;">
                                    ${fn:escapeXml(item.productName)} - 
                                    SL: ${item.quantity} - 
                                    <fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                                </div>
                            </c:forEach>
                        </div>
                    </c:if>
                    <div class="total-row">
                        <span class="info-label">Tổng tiền thanh toán:</span>
                        <span class="info-value">
                            <fmt:formatNumber value="${order.finalAmount}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                        </span>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="order-info">
                    <h3>📋 Thông tin đơn hàng</h3>
                    <p style="color: #666; text-align: center;">
                        Đơn hàng của bạn đã được tạo thành công!<br>
                        Mã đơn hàng: <strong>#${param.orderNumber}</strong>
                    </p>
                </div>
            </c:otherwise>
        </c:choose>
        
        <div style="background-color: #e3f2fd; padding: 15px; border-radius: 8px; margin: 20px 0;">
            <p style="margin: 0; color: #1976d2;">
                <strong>📧 Email xác nhận đã được gửi đến hộp thư của bạn!</strong><br>
                Đơn hàng của bạn đang được xử lý và sẽ được giao trong thời gian sớm nhất.
            </p>
        </div>
        
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/products" class="btn btn-secondary">🛍️ Tiếp tục mua sắm</a>
            <a href="${pageContext.request.contextPath}/member/orders" class="btn btn-primary">📋 Xem đơn hàng</a>
        </div>
    </div>
</body>
</html>