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

        // Check if this is a membership checkout (changed from membershipId to packageId)
        Object packageIdObj = request.getSession().getAttribute("packageId");
        Long packageId = null;
        if (packageIdObj != null) {
            packageId = (Long) packageIdObj;
        } else if (request.getParameter("packageId") != null) {
            try {
                packageId = Long.parseLong(request.getParameter("packageId"));
            } catch (NumberFormatException e) {
                // Ignore
            }
        }
        // Backward compatibility: also check for old membershipId parameter
        if (packageId == null) {
            Object membershipIdObj = request.getSession().getAttribute("membershipId");
            if (membershipIdObj != null) {
                packageId = (Long) membershipIdObj;
            } else if (request.getParameter("membershipId") != null) {
                try {
                    packageId = Long.parseLong(request.getParameter("membershipId"));
                } catch (NumberFormatException e) {
                    // Ignore
                }
            }
        }

        com.gym.model.shop.Order order = null;
        try {
            // Check if response is already committed
            if (response.isCommitted()) {
                System.err.println("[CheckoutServlet] WARNING: Response already committed before checkout processing!");
                return;
            }
            
            if (packageId != null) {
                // MEMBERSHIP CHECKOUT ONLY - Handle package checkout (no cart items)
                System.out.println("[CheckoutServlet] Processing package checkout for packageId: " + packageId);
                order = checkoutService.checkoutPackage(userId, packageId, paymentMethod, 
                        deliveryName.trim(), deliveryPhone.trim());
                
                // Clear packageId from session after successful checkout
                request.getSession().removeAttribute("packageId");
                request.getSession().removeAttribute("membershipId"); // Clear old key too
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
        // Check if this is a package checkout or cart checkout
        String type = request.getParameter("type");
        boolean isPackageCheckout = "membership".equals(type); // Keep "membership" for backward compatibility
        Object packageIdObj = request.getSession().getAttribute("packageId");
        // Backward compatibility: also check for old membershipId
        if (packageIdObj == null) {
            packageIdObj = request.getSession().getAttribute("membershipId");
        }
        
        // Check cart first - if cart has items and no explicit type=membership, it's cart checkout
        var cartItemsCheck = cartService.view(userId);
        boolean hasCartItems = cartItemsCheck != null && !cartItemsCheck.isEmpty();
        
        // Determine checkout type:
        // - If type=membership parameter exists, it's package checkout
        // - If no type parameter but has packageId in session AND no cart items, it's package checkout
        // - If has cart items and no explicit type=membership, it's cart checkout (clear package)
        boolean isPackageOnly = false;
        
        if (isPackageCheckout) {
            // Explicit package checkout from membership page
            isPackageOnly = true;
        } else if (hasCartItems) {
            // Has cart items - prioritize cart checkout, clear any package in session
            isPackageOnly = false;
            if (packageIdObj != null) {
                request.getSession().removeAttribute("packageId");
                request.getSession().removeAttribute("membershipId"); // Clear old key too
            }
        } else if (packageIdObj != null) {
            // No cart items but has package in session - package checkout
            isPackageOnly = true;
        }
        
        com.gym.model.membership.Package selectedPackage = null;
        java.util.List<com.gym.dto.CartItemDTO> cartItems = new java.util.ArrayList<>();
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        
        if (isPackageOnly) {
            // PACKAGE CHECKOUT ONLY - Load only package, ignore cart
            try {
                Long packageId = null;
                if (packageIdObj != null) {
                    packageId = (Long) packageIdObj;
                } else if (request.getParameter("packageId") != null) {
                    packageId = Long.parseLong(request.getParameter("packageId"));
                    request.getSession().setAttribute("packageId", packageId);
                } else if (request.getParameter("membershipId") != null) {
                    // Backward compatibility
                    packageId = Long.parseLong(request.getParameter("membershipId"));
                    request.getSession().setAttribute("packageId", packageId);
                }
                
                if (packageId != null) {
                    com.gym.service.membership.MembershipService membershipService = 
                        new com.gym.service.membership.MembershipServiceImpl();
                    java.util.Optional<com.gym.model.membership.Package> packageOpt = 
                        membershipService.getPackageById(packageId);
                    if (packageOpt.isPresent()) {
                        selectedPackage = packageOpt.get();
                        total = selectedPackage.getPrice();
                    } else {
                        // Package not found, clear session and redirect
                        request.getSession().removeAttribute("packageId");
                        request.getSession().removeAttribute("membershipId");
                        response.sendRedirect(request.getContextPath() + "/member/membership");
                        return;
                    }
                } else {
                    // No package ID, redirect to membership page
                    response.sendRedirect(request.getContextPath() + "/member/membership");
                    return;
                }
            } catch (Exception e) {
                System.err.println("[CheckoutServlet] Error loading package: " + e.getMessage());
                e.printStackTrace();
                request.getSession().removeAttribute("packageId");
                request.getSession().removeAttribute("membershipId");
                response.sendRedirect(request.getContextPath() + "/member/membership");
                return;
            }
        } else {
            // CART CHECKOUT ONLY - Load only cart items, ignore package
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
        request.setAttribute("selectedPackage", selectedPackage); // Changed from "membership" to "selectedPackage"
        request.setAttribute("isMembershipCheckout", isPackageOnly); // Keep name for backward compatibility
        request.getRequestDispatcher("/views/cart/checkout.jsp").forward(request, response);
    }
}

