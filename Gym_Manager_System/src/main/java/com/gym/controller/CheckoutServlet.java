package com.gym.controller;

import com.gym.model.shop.DeliveryMethod;
import com.gym.model.shop.PaymentMethod;
import com.gym.service.shop.CartService;
import com.gym.service.shop.CartServiceImpl;
import com.gym.service.shop.CheckoutService;
import com.gym.service.shop.CheckoutServiceImpl;
import com.gym.service.shop.EmptyCartException;
import com.gym.service.shop.InsufficientStockException;
import com.gym.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet for checkout operations
 */
@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout/*"})
public class CheckoutServlet extends HttpServlet {
    private CheckoutService checkoutService;
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.checkoutService = new CheckoutServiceImpl();
        this.cartService = new CartServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        Long userId = SessionUtil.getUserId(request);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        String pathInfo = request.getPathInfo();
        String action = pathInfo == null || pathInfo.equals("/") ? "confirm" : pathInfo.substring(1);

        // Handle /checkout (confirmation page)
        if ("confirm".equals(action)) {
            showCheckout(request, response, userId);
            return;
        }
        
        // Handle /checkout/clear-membership (clear membership from session)
        if ("clear-membership".equals(action)) {
            request.getSession().removeAttribute("membershipId");
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/plain");
            response.getWriter().write("OK");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        Long userId = SessionUtil.getUserId(request);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        // Get delivery information
        String deliveryName = request.getParameter("deliveryName");
        String deliveryAddress = request.getParameter("deliveryAddress");
        String deliveryPhone = request.getParameter("deliveryPhone");
        String deliveryMethodStr = request.getParameter("deliveryMethod");
        String paymentMethodStr = request.getParameter("paymentMethod");
        
        // Validation
        if (deliveryName == null || deliveryName.trim().isEmpty() ||
            deliveryPhone == null || deliveryPhone.trim().isEmpty() ||
            deliveryMethodStr == null || deliveryMethodStr.trim().isEmpty() ||
            paymentMethodStr == null || paymentMethodStr.trim().isEmpty()) {
            request.getSession().setAttribute("error", "Vui lòng điền đầy đủ thông tin");
            response.sendRedirect(request.getContextPath() + "/checkout");
            return;
        }
        
        DeliveryMethod deliveryMethod = DeliveryMethod.fromCode(deliveryMethodStr);
        
        // If delivery method is DELIVERY, address is required
        if (deliveryMethod == DeliveryMethod.DELIVERY && 
            (deliveryAddress == null || deliveryAddress.trim().isEmpty())) {
            request.getSession().setAttribute("error", "Vui lòng nhập địa chỉ giao hàng");
            response.sendRedirect(request.getContextPath() + "/checkout");
            return;
        }
        
        PaymentMethod paymentMethod = PaymentMethod.fromCode(paymentMethodStr);

        // Check if this is a membership checkout
        Object membershipIdObj = request.getSession().getAttribute("membershipId");
        Long membershipId = null;
        if (membershipIdObj != null) {
            membershipId = (Long) membershipIdObj;
        } else if (request.getParameter("membershipId") != null) {
            try {
                membershipId = Long.parseLong(request.getParameter("membershipId"));
            } catch (NumberFormatException e) {
                // Ignore
            }
        }

        com.gym.model.shop.Order order = null;
        try {
            // Check if response is already committed
            if (response.isCommitted()) {
                System.err.println("[CheckoutServlet] WARNING: Response already committed before checkout processing!");
                return;
            }
            
            if (membershipId != null) {
                // MEMBERSHIP CHECKOUT ONLY - Handle membership checkout (no cart items)
                System.out.println("[CheckoutServlet] Processing membership checkout for membershipId: " + membershipId);
                order = checkoutService.checkoutMembership(userId, membershipId, paymentMethod, 
                        deliveryName.trim(), deliveryPhone.trim());
                
                // Clear membership from session after successful checkout
                request.getSession().removeAttribute("membershipId");
            } else {
                // CART CHECKOUT ONLY - Handle regular product checkout (no membership)
                System.out.println("[CheckoutServlet] Processing cart checkout");
                order = checkoutService.checkout(userId, paymentMethod, 
                        deliveryName.trim(), deliveryAddress != null ? deliveryAddress.trim() : null,
                        deliveryPhone.trim(), deliveryMethod);
            }
            
            // Verify order was created successfully
            if (order == null || order.getOrderId() == null) {
                throw new RuntimeException("Không thể tạo đơn hàng. Vui lòng thử lại.");
            }
            
            System.out.println("[CheckoutServlet] Order created successfully with ID: " + order.getOrderId());
            
            // If payment method is MoMo, redirect to MoMo payment gateway
            if (paymentMethod == PaymentMethod.MOMO) {
                try {
                    // Get base URL for callback
                    String baseUrl = request.getScheme() + "://" + request.getServerName() +
                        (request.getServerPort() != 80 && request.getServerPort() != 443 ? 
                         ":" + request.getServerPort() : "") +
                        request.getContextPath();
                    
                    String momoPaymentUrl = checkoutService.processMoMoPayment(order.getOrderId(), baseUrl);
                    if (momoPaymentUrl != null && !momoPaymentUrl.trim().isEmpty()) {
                        System.out.println("[CheckoutServlet] Redirecting to MoMo payment URL");
                        response.sendRedirect(momoPaymentUrl);
                        return;
                    } else {
                        // If MoMo fails but order is created, still redirect to success page
                        System.err.println("[CheckoutServlet] Warning: MoMo payment URL creation failed, but order was created. Redirecting to success page.");
                        String successUrl = request.getContextPath() + "/order/success?orderId=" + order.getOrderId();
                        System.out.println("[CheckoutServlet] Redirecting to success page: " + successUrl);
                        response.sendRedirect(successUrl);
                        return;
                    }
                } catch (Exception e) {
                    // If MoMo payment processing fails but order is already created, redirect to success page
                    System.err.println("[CheckoutServlet] Error processing MoMo payment: " + e.getMessage());
                    e.printStackTrace();
                    if (order != null && order.getOrderId() != null) {
                        // Order was created, redirect to success page
                        String successUrl = request.getContextPath() + "/order/success?orderId=" + order.getOrderId();
                        System.out.println("[CheckoutServlet] Redirecting to success page after MoMo error: " + successUrl);
                        if (!response.isCommitted()) {
                            response.sendRedirect(successUrl);
                        }
                    } else {
                        request.getSession().setAttribute("error", "Không thể kết nối đến MoMo. Vui lòng thử lại sau.");
                        if (!response.isCommitted()) {
                            response.sendRedirect(request.getContextPath() + "/checkout");
                        }
                    }
                    return;
                }
            }
            
            // For cash payment (pickup/delivery), go to success page
            // Payment status is already PENDING (chưa thanh toán)
            String successUrl = request.getContextPath() + "/order/success?orderId=" + order.getOrderId();
            System.out.println("[CheckoutServlet] Checkout successful! Redirecting to success page: " + successUrl);
            
            // Ensure response is not committed before redirect
            if (!response.isCommitted()) {
                response.sendRedirect(successUrl);
            } else {
                System.err.println("[CheckoutServlet] ERROR: Cannot redirect - response already committed!");
            }
            
        } catch (EmptyCartException e) {
            System.err.println("[CheckoutServlet] EmptyCartException: " + e.getMessage());
            if (!response.isCommitted()) {
                response.sendRedirect(request.getContextPath() + "/cart?error=empty");
            }
        } catch (InsufficientStockException e) {
            System.err.println("[CheckoutServlet] InsufficientStockException: " + e.getMessage());
            if (!response.isCommitted()) {
                response.sendRedirect(request.getContextPath() + "/cart?error=stock");
            }
        } catch (Exception e) {
            System.err.println("[CheckoutServlet] Error during checkout: " + e.getMessage());
            e.printStackTrace();
            
            // If order was created but exception occurred after, redirect to success page
            if (order != null && order.getOrderId() != null) {
                System.err.println("[CheckoutServlet] Order was created with ID: " + order.getOrderId() + ", redirecting to success page despite exception.");
                String successUrl = request.getContextPath() + "/order/success?orderId=" + order.getOrderId();
                if (!response.isCommitted()) {
                    response.sendRedirect(successUrl);
                }
            } else {
                // Order was not created, show error
                request.getSession().setAttribute("error", "Thanh toán thất bại: " + e.getMessage());
                if (!response.isCommitted()) {
                    response.sendRedirect(request.getContextPath() + "/cart");
                }
            }
        }
    }

    private void showCheckout(HttpServletRequest request, HttpServletResponse response, Long userId)
            throws ServletException, IOException {
        // Check if this is a membership checkout or cart checkout
        String type = request.getParameter("type");
        boolean isMembershipCheckout = "membership".equals(type);
        Object membershipIdObj = request.getSession().getAttribute("membershipId");
        
        // Check cart first - if cart has items and no explicit type=membership, it's cart checkout
        var cartItemsCheck = cartService.view(userId);
        boolean hasCartItems = cartItemsCheck != null && !cartItemsCheck.isEmpty();
        
        // Determine checkout type:
        // - If type=membership parameter exists, it's membership checkout
        // - If no type parameter but has membershipId in session AND no cart items, it's membership checkout
        // - If has cart items and no explicit type=membership, it's cart checkout (clear membership)
        boolean isMembershipOnly = false;
        
        if (isMembershipCheckout) {
            // Explicit membership checkout from membership page
            isMembershipOnly = true;
        } else if (hasCartItems) {
            // Has cart items - prioritize cart checkout, clear any membership in session
            isMembershipOnly = false;
            if (membershipIdObj != null) {
                request.getSession().removeAttribute("membershipId");
            }
        } else if (membershipIdObj != null) {
            // No cart items but has membership in session - membership checkout
            isMembershipOnly = true;
        }
        
        com.gym.model.membership.Membership membership = null;
        java.util.List<com.gym.model.shop.CartItem> cartItems = new java.util.ArrayList<>();
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        
        if (isMembershipOnly) {
            // MEMBERSHIP CHECKOUT ONLY - Load only membership, ignore cart
            try {
                Long membershipId = null;
                if (membershipIdObj != null) {
                    membershipId = (Long) membershipIdObj;
                } else if (request.getParameter("membershipId") != null) {
                    membershipId = Long.parseLong(request.getParameter("membershipId"));
                    // Store in session for persistence
                    request.getSession().setAttribute("membershipId", membershipId);
                }
                
                if (membershipId != null) {
                    com.gym.service.membership.MembershipService membershipService = 
                        new com.gym.service.membership.MembershipServiceImpl();
                    java.util.Optional<com.gym.model.membership.Membership> membershipOpt = 
                        membershipService.getMembershipById(membershipId);
                    if (membershipOpt.isPresent()) {
                        membership = membershipOpt.get();
                        total = membership.getPrice();
                    } else {
                        // Membership not found, clear session and redirect
                        request.getSession().removeAttribute("membershipId");
                        response.sendRedirect(request.getContextPath() + "/member/membership");
                        return;
                    }
                } else {
                    // No membership ID, redirect to membership page
                    response.sendRedirect(request.getContextPath() + "/member/membership");
                    return;
                }
            } catch (Exception e) {
                System.err.println("[CheckoutServlet] Error loading membership: " + e.getMessage());
                e.printStackTrace();
                request.getSession().removeAttribute("membershipId");
                response.sendRedirect(request.getContextPath() + "/member/membership");
                return;
            }
        } else {
            // CART CHECKOUT ONLY - Load only cart items, ignore membership
            cartItems = cartService.view(userId);
            total = cartService.calculateTotal(cartItems);
            
            // If cart is empty, redirect
            if (cartItems.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/cart?error=empty");
                return;
            }
        }

        // Set attributes
        request.setAttribute("cart", cartItems);
        request.setAttribute("total", total);
        request.setAttribute("membership", membership);
        request.setAttribute("isMembershipCheckout", isMembershipOnly);
        request.getRequestDispatcher("/views/cart/checkout.jsp").forward(request, response);
    }
}

