package controller;

import model.shop.DeliveryMethod;
import model.shop.PaymentMethod;
import service.shop.CartService;
import service.shop.CartServiceImpl;
import service.shop.CheckoutService;
import service.shop.CheckoutServiceImpl;
import exception.EmptyCartException;
import exception.InsufficientStockException;
import Utils.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.shop.Order;

import java.io.IOException;

/**
 * Servlet for checkout operations
 * Follows MVC pattern - Controller layer
 * Handles checkout page display and order processing
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

        Integer userIdInt = SessionUtil.getUserId(request);
        if (userIdInt == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        Long userId = userIdInt.longValue();

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
            request.getSession().removeAttribute("packageId");
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

        Integer userIdInt = SessionUtil.getUserId(request);
        if (userIdInt == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        Long userId = userIdInt.longValue();

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

        Order order = null;
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
            
            // If payment method is VNPay, redirect to VNPay payment gateway
            if (paymentMethod == PaymentMethod.VNPAY) {
                try {
                    // Get client IP address
                    String ipAddress = getClientIpAddress(request);
                    
                    // Get order amount in VND (convert from BigDecimal to Long)
                    Long amount = order.getTotalAmount().longValue();
                    
                    // Build order info
                    String orderInfo = "Thanh toan don hang #" + order.getOrderId();
                    
                    // Build base URL dynamically from request (includes context path)
                    // IMPORTANT: VNPay requires public URL (not localhost)
                    // Get ngrok URL from configuration
                    String ngrokBaseUrl = getNgrokBaseUrl(request);
                    String baseUrl;
                    
                    if (ngrokBaseUrl != null && !ngrokBaseUrl.isEmpty()) {
                        // Use ngrok URL from config (already includes context path if configured)
                        // Example: https://xxx.ngrok-free.dev/Gym_Management_Systems
                        baseUrl = ngrokBaseUrl;
                        System.out.println("[CheckoutServlet] Using ngrok URL from config: " + baseUrl);
                    } else {
                        // Fallback: Try to detect if running through ngrok by checking headers
                        String forwardedHost = request.getHeader("X-Forwarded-Host");
                        if (forwardedHost != null && !forwardedHost.isEmpty() && forwardedHost.contains("ngrok")) {
                            // Running through ngrok but not configured - use forwarded host
                            String scheme = request.getHeader("X-Forwarded-Proto");
                            if (scheme == null) scheme = "https";
                            String contextPath = request.getContextPath();
                            baseUrl = scheme + "://" + forwardedHost + contextPath;
                            System.out.println("[CheckoutServlet] Using ngrok URL from headers: " + baseUrl);
                        } else {
                            // Not using ngrok - this will fail with VNPay!
                            String contextPath = request.getContextPath();
                            baseUrl = request.getScheme() + "://" + request.getServerName() +
                                (request.getServerPort() != 80 && request.getServerPort() != 443 ? 
                                 ":" + request.getServerPort() : "") +
                                contextPath;
                            System.err.println("[CheckoutServlet] WARNING: Using localhost URL - VNPay will reject this!");
                            System.err.println("[CheckoutServlet] Please configure vnp_ReturnUrl in email.properties with ngrok URL");
                        }
                    }
                    
                    System.out.println("[CheckoutServlet] Base URL: " + baseUrl);
                    System.out.println("[CheckoutServlet] Context Path: " + request.getContextPath());
                    
                    String vnpayPaymentUrl = checkoutService.processVNPayPayment(
                        order.getOrderId(), amount, orderInfo, ipAddress, baseUrl);
                    
                    if (vnpayPaymentUrl != null && !vnpayPaymentUrl.trim().isEmpty()) {
                        System.out.println("[CheckoutServlet] Redirecting to VNPay payment URL");
                        response.sendRedirect(vnpayPaymentUrl);
                        return;
                    } else {
                        System.err.println("[CheckoutServlet] Warning: VNPay payment URL creation failed");
                        request.getSession().setAttribute("error", "Không thể kết nối đến VNPay. Vui lòng thử lại sau.");
                        if (!response.isCommitted()) {
                            response.sendRedirect(request.getContextPath() + "/checkout");
                        }
                        return;
                    }
                } catch (Exception e) {
                    System.err.println("[CheckoutServlet] Error processing VNPay payment: " + e.getMessage());
                    e.printStackTrace();
                    request.getSession().setAttribute("error", "Không thể kết nối đến VNPay. Vui lòng thử lại sau.");
                    if (!response.isCommitted()) {
                        response.sendRedirect(request.getContextPath() + "/checkout");
                    }
                    return;
                }
            }
            
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
        
        model.Package selectedPackage = null;
        java.util.List<dto.CartItemDTO> cartItems = new java.util.ArrayList<>();
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
                    // Load package using DAO
                    dao.PackageDAO packageDao = new dao.PackageDAO();
                    model.Package pkg = packageDao.findById(packageId.intValue());
                    
                    if (pkg != null) {
                        selectedPackage = pkg;
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
    
    /**
     * Get ngrok base URL from configuration or request
     * @param request HttpServletRequest
     * @return Ngrok base URL or null
     */
    private String getNgrokBaseUrl(HttpServletRequest request) {
        // Try to get from ConfigManager first
        try {
            Utils.ConfigManager config = Utils.ConfigManager.getInstance();
            String returnUrl = config.getProperty("vnp_ReturnUrl");
            if (returnUrl != null && !returnUrl.trim().isEmpty()) {
                // Extract base URL from return URL
                // Example: https://xxx.ngrok-free.dev/Gym_Management_Systems/vnpay-return
                // Extract: https://xxx.ngrok-free.dev/Gym_Management_Systems
                int vnpayIndex = returnUrl.indexOf("/vnpay-return");
                if (vnpayIndex > 0) {
                    String fullPath = returnUrl.substring(0, vnpayIndex);
                    // fullPath now contains: https://xxx.ngrok-free.dev/Gym_Management_Systems
                    // We want to return the full path including context path
                    return fullPath;
                }
            }
        } catch (Exception e) {
            System.err.println("[CheckoutServlet] Error getting ngrok URL from config: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get client IP address from request
     * @param request HttpServletRequest
     * @return IP address
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}

