<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/views/common/header.jsp" %>

<style>
    :root {
        --primary: #141a46;
        --accent: #ec8b5e;
        --text: #2c3e50;
        --text-light: #5a6c7d;
        --card: #ffffff;
        --shadow: rgba(0, 0, 0, 0.1);
        --gradient-primary: linear-gradient(135deg, #141a46 0%, #1e2a5c 100%);
        --gradient-accent: linear-gradient(135deg, #ec8b5e 0%, #d67a4f 100%);
    }

    .checkout-container {
        max-width: 1200px;
        margin: 40px auto;
        padding: 0 20px;
    }

    .checkout-header {
        background: var(--gradient-primary);
        color: white;
        padding: 30px;
        border-radius: 15px;
        margin-bottom: 30px;
        text-align: center;
    }

    .checkout-content {
        display: grid;
        grid-template-columns: 1.5fr 1fr;
        gap: 30px;
    }

    .form-group {
        margin-bottom: 20px;
    }

    .form-label {
        display: block;
        font-weight: 600;
        color: var(--text);
        margin-bottom: 8px;
        font-size: 0.95rem;
    }

    .form-control {
        width: 100%;
        padding: 12px 15px;
        border: 2px solid #ddd;
        border-radius: 8px;
        font-size: 1rem;
        transition: all 0.3s;
        box-sizing: border-box;
    }

    .form-control:focus {
        outline: none;
        border-color: var(--accent);
        box-shadow: 0 0 0 3px rgba(236, 139, 94, 0.1);
    }

    .delivery-methods {
        margin-top: 20px;
    }

    .delivery-option {
        display: flex;
        align-items: center;
        padding: 15px;
        border: 2px solid #ddd;
        border-radius: 10px;
        margin-bottom: 10px;
        cursor: pointer;
        transition: all 0.3s;
    }

    .delivery-option:hover {
        border-color: var(--accent);
        background: rgba(236, 139, 94, 0.05);
    }

    .delivery-option input[type="radio"] {
        margin-right: 15px;
        cursor: pointer;
    }

    .delivery-option label {
        cursor: pointer;
        flex: 1;
        margin: 0;
    }

    .delivery-option.selected {
        border-color: var(--accent);
        background: rgba(236, 139, 94, 0.1);
    }

    .checkout-card {
        background: var(--card);
        border-radius: 15px;
        box-shadow: 0 4px 15px var(--shadow);
        padding: 30px;
    }

    .checkout-card h3 {
        color: var(--primary);
        margin-bottom: 20px;
        padding-bottom: 15px;
        border-bottom: 2px solid var(--accent);
    }

    .order-item {
        display: flex;
        align-items: center;
        gap: 15px;
        padding: 15px;
        border-bottom: 1px solid #eee;
    }

    .order-item img {
        width: 80px;
        height: 80px;
        object-fit: cover;
        border-radius: 8px;
    }

    .item-details {
        flex: 1;
    }

    .item-details h4 {
        margin: 0 0 5px 0;
        color: var(--text);
    }

    .item-quantity {
        color: var(--text-light);
        font-size: 0.9rem;
    }

    .item-price {
        font-weight: 700;
        color: var(--accent);
    }

    .summary-section {
        background: #f8f9fa;
        padding: 20px;
        border-radius: 10px;
        margin-top: 20px;
    }

    .summary-row {
        display: flex;
        justify-content: space-between;
        padding: 10px 0;
        border-bottom: 1px solid #ddd;
    }

    .summary-row.total {
        border-bottom: none;
        font-size: 1.3rem;
        font-weight: 700;
        color: var(--primary);
        margin-top: 10px;
        padding-top: 20px;
        border-top: 2px solid var(--accent);
    }

    .payment-methods {
        margin-top: 20px;
    }

    .payment-option {
        display: flex;
        align-items: center;
        padding: 15px;
        border: 2px solid #ddd;
        border-radius: 10px;
        margin-bottom: 10px;
        cursor: pointer;
        transition: all 0.3s;
    }

    .payment-option:hover {
        border-color: var(--accent);
        background: rgba(236, 139, 94, 0.05);
    }

    .payment-option input[type="radio"] {
        margin-right: 15px;
        cursor: pointer;
    }

    .payment-option label {
        cursor: pointer;
        flex: 1;
        margin: 0;
    }

    .btn-checkout {
        width: 100%;
        padding: 15px;
        background: var(--gradient-accent);
        color: white;
        border: none;
        border-radius: 10px;
        font-weight: 700;
        font-size: 1.1rem;
        cursor: pointer;
        margin-top: 20px;
        transition: all 0.3s;
    }

    .btn-checkout:hover {
        transform: translateY(-2px);
        box-shadow: 0 8px 20px rgba(236, 139, 94, 0.4);
    }
</style>

<div class="checkout-container">
    <div class="checkout-header">
        <h1><i class="fas fa-shopping-bag"></i> Xác nhận đơn hàng</h1>
        <p>Vui lòng kiểm tra lại thông tin đơn hàng và chọn phương thức thanh toán</p>
    </div>

    <c:if test="${not empty error}">
        <div style="background: #f8d7da; color: #721c24; padding: 15px; border-radius: 10px; margin-bottom: 20px;">
            <i class="fas fa-exclamation-circle"></i> ${error}
        </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/checkout" id="checkoutForm">
        <div class="checkout-content">
            <!-- Left: Delivery Information and Order Items -->
            <div>
                <!-- Delivery Information (hidden for membership) -->
                <c:if test="${not isMembershipCheckout}">
                    <div class="checkout-card">
                        <h3><i class="fas fa-shipping-fast"></i> Thông tin giao hàng</h3>
                        
                        <div class="form-group">
                            <label class="form-label">Họ và tên người nhận *</label>
                            <input type="text" name="deliveryName" class="form-control" 
                                   placeholder="Nhập họ và tên" required>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Số điện thoại *</label>
                            <input type="tel" name="deliveryPhone" class="form-control" 
                                   placeholder="Nhập số điện thoại" required>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Phương thức nhận hàng *</label>
                            <div class="delivery-methods">
                                <div class="delivery-option selected" onclick="selectDelivery('pickup')">
                                    <input type="radio" id="delivery_pickup" name="deliveryMethod" value="pickup" checked onchange="toggleAddressField()">
                                    <label for="delivery_pickup">Nhận trực tiếp tại cửa hàng</label>
                                </div>
                                <div class="delivery-option" onclick="selectDelivery('delivery')">
                                    <input type="radio" id="delivery_delivery" name="deliveryMethod" value="delivery" onchange="toggleAddressField()">
                                    <label for="delivery_delivery">Giao hàng online</label>
                                </div>
                            </div>
                        </div>

                        <div class="form-group" id="addressGroup" style="display: none;">
                            <label class="form-label">Địa chỉ giao hàng *</label>
                            <textarea name="deliveryAddress" class="form-control" rows="3" 
                                      placeholder="Nhập địa chỉ chi tiết"></textarea>
                        </div>
                    </div>
                </c:if>
                
                <!-- Customer Information for Membership -->
                <c:if test="${isMembershipCheckout}">
                    <div class="checkout-card">
                        <h3><i class="fas fa-user"></i> Thông tin khách hàng</h3>
                        
                        <div class="form-group">
                            <label class="form-label">Họ và tên *</label>
                            <input type="text" name="deliveryName" class="form-control" 
                                   placeholder="Nhập họ và tên" required>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Số điện thoại *</label>
                            <input type="tel" name="deliveryPhone" class="form-control" 
                                   placeholder="Nhập số điện thoại" required>
                        </div>
                    </div>
                </c:if>

                <!-- Order Items / Membership -->
                <div class="checkout-card" style="margin-top: 20px;">
                    <h3><i class="fas fa-list"></i> 
                        <c:choose>
                            <c:when test="${isMembershipCheckout}">Gói thành viên</c:when>
                            <c:otherwise>Sản phẩm đặt hàng</c:otherwise>
                        </c:choose>
                    </h3>
                    
                    <!-- Membership Display (membership checkout only) -->
                    <c:if test="${isMembershipCheckout and not empty membership}">
                        <div class="order-item">
                            <div style="width: 80px; height: 80px; background: linear-gradient(135deg, #141a46 0%, #1e2a5c 100%); border-radius: 10px; display: flex; align-items: center; justify-content: center; color: white; font-size: 2rem;">
                                <i class="fas fa-crown"></i>
                            </div>
                            <div class="item-details">
                                <h4>${fn:escapeXml(membership.displayName)}</h4>
                                <div class="item-quantity">
                                    Thời hạn: ${membership.durationMonths} ${membership.durationMonths == 1 ? 'tháng' : 'tháng'}
                                </div>
                                <c:if test="${not empty membership.description}">
                                    <p style="margin-top: 5px; color: #5a6c7d; font-size: 0.9rem;">${fn:escapeXml(membership.description)}</p>
                                </c:if>
                            </div>
                            <div class="item-price">
                                <fmt:formatNumber value="${membership.price}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                            </div>
                        </div>
                    </c:if>
                    
                    <!-- Product Items (cart checkout only) -->
                    <c:if test="${not isMembershipCheckout}">
                        <c:forEach var="item" items="${cart}">
                            <div class="order-item">
                                <img src="${pageContext.request.contextPath}/images/products/${item.productId}.png"
                                     onerror="this.src='${pageContext.request.contextPath}/images/placeholder.png';"
                                     alt="${fn:escapeXml(item.productName)}">
                                <div class="item-details">
                                    <h4>${fn:escapeXml(item.productName)}</h4>
                                    <div class="item-quantity">
                                        Số lượng: ${item.quantity} ${fn:escapeXml(item.unit)}
                                    </div>
                                </div>
                                <div class="item-price">
                                    <fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                                </div>
                            </div>
                        </c:forEach>
                        
                        <!-- Show message if cart is empty -->
                        <c:if test="${empty cart}">
                            <div style="text-align: center; padding: 40px; color: #999;">
                                <i class="fas fa-shopping-cart" style="font-size: 3rem; margin-bottom: 15px; opacity: 0.5;"></i>
                                <p>Không có sản phẩm nào trong giỏ hàng</p>
                            </div>
                        </c:if>
                    </c:if>
                    
                    <!-- Show message if membership checkout but no membership (shouldn't happen) -->
                    <c:if test="${isMembershipCheckout and empty membership}">
                        <div style="text-align: center; padding: 40px; color: #999;">
                            <i class="fas fa-exclamation-triangle" style="font-size: 3rem; margin-bottom: 15px; opacity: 0.5;"></i>
                            <p>Không tìm thấy gói thành viên</p>
                        </div>
                    </c:if>
                </div>
            </div>

            <!-- Right: Payment Method and Summary -->
            <div>
                <!-- Payment Method -->
                <div class="checkout-card">
                    <h3><i class="fas fa-credit-card"></i> Phương thức thanh toán</h3>
                    <div class="payment-methods">
                        <div class="payment-option" style="border-color: var(--accent); background: rgba(236, 139, 94, 0.05);">
                            <input type="radio" id="cash" name="paymentMethod" value="cash" checked>
                            <label for="cash">
                                <i class="fas fa-money-bill-wave"></i> Tiền mặt (Thanh toán khi nhận hàng)
                                <small style="display: block; color: #666; margin-top: 5px;">Thanh toán khi nhận hàng tại cửa hàng</small>
                            </label>
                        </div>
                        <div class="payment-option">
                            <input type="radio" id="momo" name="paymentMethod" value="momo">
                            <label for="momo">
                                <i class="fas fa-mobile-alt"></i> Chuyển khoản bằng MoMo
                                <small style="display: block; color: #666; margin-top: 5px;">Thanh toán nhanh và an toàn</small>
                            </label>
                        </div>
                    </div>

                    <div class="summary-section" style="margin-top: 25px;">
                        <div class="summary-row">
                            <span>Tạm tính:</span>
                            <span><fmt:formatNumber value="${total}" type="currency" currencySymbol="đ" maxFractionDigits="0"/></span>
                        </div>
                        <div class="summary-row">
                            <span>Giảm giá:</span>
                            <span>0 đ</span>
                        </div>
                        <div class="summary-row total">
                            <span>Tổng thanh toán:</span>
                            <span><fmt:formatNumber value="${total}" type="currency" currencySymbol="đ" maxFractionDigits="0"/></span>
                        </div>
                    </div>

                    <button type="submit" class="btn-checkout">
                        <i class="fas fa-check"></i> Xác nhận đặt hàng
                    </button>
                    <a href="${pageContext.request.contextPath}/cart" class="btn btn-outline" style="display: block; text-align: center; margin-top: 10px;">
                        <i class="fas fa-arrow-left"></i> Quay lại giỏ hàng
                    </a>
                </div>
            </div>
        </div>
    </form>

    <script>
        // Check if this is membership checkout
        <c:choose>
            <c:when test="${isMembershipCheckout}">
                const isMembershipCheckout = true;
                var membershipCheckoutActive = true;
            </c:when>
            <c:otherwise>
                const isMembershipCheckout = false;
                var membershipCheckoutActive = false;
            </c:otherwise>
        </c:choose>
        
        // If membership checkout, set default delivery method to pickup
        if (isMembershipCheckout) {
            document.addEventListener('DOMContentLoaded', function() {
                // Add hidden deliveryMethod field for membership
                const form = document.getElementById('checkoutForm');
                if (form) {
                    const hiddenDeliveryMethod = document.createElement('input');
                    hiddenDeliveryMethod.type = 'hidden';
                    hiddenDeliveryMethod.name = 'deliveryMethod';
                    hiddenDeliveryMethod.value = 'pickup';
                    form.appendChild(hiddenDeliveryMethod);
                }
            });
        }
        
        // Clear membership from session when user leaves checkout page without completing payment
        if (membershipCheckoutActive) {
            let formSubmitted = false;
            const checkoutForm = document.getElementById('checkoutForm');
            if (checkoutForm) {
                checkoutForm.addEventListener('submit', function() {
                    formSubmitted = true;
                });
            }
            
            // Clear membership when page is unloaded (user navigates away)
            window.addEventListener('beforeunload', function() {
                if (!formSubmitted) {
                    // Send request to clear membership from session
                    navigator.sendBeacon('${pageContext.request.contextPath}/checkout/clear-membership', '');
                }
            });
            
            // Also handle back button and navigation away
            window.addEventListener('pagehide', function() {
                if (!formSubmitted) {
                    navigator.sendBeacon('${pageContext.request.contextPath}/checkout/clear-membership', '');
                }
            });
        }
        
        function selectDelivery(method) {
            document.querySelectorAll('.delivery-option').forEach(opt => {
                opt.classList.remove('selected');
            });
            
            const radio = document.getElementById('delivery_' + method);
            if (radio) {
                radio.checked = true;
                radio.closest('.delivery-option').classList.add('selected');
                toggleAddressField();
            }
        }

        function toggleAddressField() {
            const deliveryMethod = document.querySelector('input[name="deliveryMethod"]:checked').value;
            const addressGroup = document.getElementById('addressGroup');
            const addressInput = document.querySelector('textarea[name="deliveryAddress"]');
            
            if (deliveryMethod === 'delivery') {
                addressGroup.style.display = 'block';
                addressInput.setAttribute('required', 'required');
            } else {
                addressGroup.style.display = 'none';
                addressInput.removeAttribute('required');
            }
        }

        // Form validation
        document.getElementById('checkoutForm').addEventListener('submit', function(e) {
            // Skip validation for membership checkout
            if (isMembershipCheckout) {
                return true;
            }
            
            const deliveryMethodRadio = document.querySelector('input[name="deliveryMethod"]:checked');
            if (!deliveryMethodRadio) {
                e.preventDefault();
                alert('Vui lòng chọn phương thức nhận hàng');
                return false;
            }
            
            const deliveryMethod = deliveryMethodRadio.value;
            const addressInput = document.querySelector('textarea[name="deliveryAddress"]');
            
            if (deliveryMethod === 'delivery' && (!addressInput || !addressInput.value || addressInput.value.trim() === '')) {
                e.preventDefault();
                alert('Vui lòng nhập địa chỉ giao hàng');
                if (addressInput) {
                    addressInput.focus();
                }
                return false;
            }
        });

        // Initialize on page load
        document.addEventListener('DOMContentLoaded', function() {
            // Only toggle address field if not membership checkout
            if (!isMembershipCheckout) {
                toggleAddressField();
            }
        });
    </script>
</div>

<%@ include file="/views/common/footer.jsp" %>

