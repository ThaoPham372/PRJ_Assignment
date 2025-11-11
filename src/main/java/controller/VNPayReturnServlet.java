package controller;

import service.shop.PaymentService;
import service.shop.PaymentServiceImpl;
import service.shop.VNPayService;
import model.shop.PaymentStatus;
import dao.shop.OrderDao;
import dao.shop.OrderItemDao;
import model.shop.Order;
import model.shop.OrderItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * VNPay Return Servlet
 * Handles VNPay payment callback after user completes payment
 * Follows MVC pattern - Controller layer delegates to Service layer
 * 
 * Endpoint: /vnpay-return
 */
@WebServlet(name = "VNPayReturnServlet", urlPatterns = {"/vnpay-return"})
public class VNPayReturnServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(VNPayReturnServlet.class.getName());
    
    private VNPayService vnPayService;
    private PaymentService paymentService;
    private OrderDao orderDao;
    private OrderItemDao orderItemDao;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.vnPayService = new VNPayService();
        this.paymentService = new PaymentServiceImpl();
        this.orderDao = new OrderDao();
        this.orderItemDao = new OrderItemDao();
        LOGGER.info("[VNPayReturnServlet] Initialized successfully");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processVNPayReturn(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processVNPayReturn(request, response);
    }
    
    /**
     * Process VNPay return callback
     * Validates signature and updates payment status
     */
    private void processVNPayReturn(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOGGER.info("[VNPayReturnServlet] Processing VNPay return callback");
        
        try {
            // Collect all VNPay parameters
            // IMPORTANT: VNPay sends parameters in URL, they may be URL encoded
            // We need to collect them as-is and let VNPayService handle signature verification
            Map<String, String> vnpParams = new HashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            
            // Collect all parameters - VNPay may send duplicate parameters, use first occurrence
            // IMPORTANT: Servlet container automatically URL-decodes parameters
            // But we need to preserve the exact values as VNPay sends them
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                String paramValue = request.getParameter(paramName);
                // Store parameter as-is (Servlet container already URL-decoded it)
                // If duplicate, only keep first occurrence
                if (!vnpParams.containsKey(paramName)) {
                    // Trim whitespace from parameter value
                    if (paramValue != null) {
                        paramValue = paramValue.trim();
                    }
                    vnpParams.put(paramName, paramValue);
                    LOGGER.info("[VNPayReturnServlet] Parameter: " + paramName + " = [" + paramValue + "]");
                } else {
                    LOGGER.warning("[VNPayReturnServlet] Duplicate parameter ignored: " + paramName);
                }
            }
            
            LOGGER.info("[VNPayReturnServlet] Total parameters received: " + vnpParams.size());
            
            // Log full query string for debugging
            String queryString = request.getQueryString();
            if (queryString != null) {
                LOGGER.info("[VNPayReturnServlet] Full Query String: " + queryString);
            }
            
            // Verify signature
            boolean isValidSignature = vnPayService.verifySignature(vnpParams);
            
            if (!isValidSignature) {
                LOGGER.warning("[VNPayReturnServlet] Invalid signature - possible tampering");
                request.getSession().setAttribute("error", "Xác thực thanh toán thất bại. Vui lòng liên hệ hỗ trợ.");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }
            
            // Get response code and transaction info
            String vnp_ResponseCode = vnpParams.get("vnp_ResponseCode");
            String vnp_TxnRef = vnpParams.get("vnp_TxnRef"); // Order ID
            String vnp_TransactionNo = vnpParams.get("vnp_TransactionNo"); // VNPay transaction ID
            
            LOGGER.info("[VNPayReturnServlet] Response Code: " + vnp_ResponseCode);
            LOGGER.info("[VNPayReturnServlet] Order ID (TxnRef): " + vnp_TxnRef);
            LOGGER.info("[VNPayReturnServlet] VNPay Transaction No: " + vnp_TransactionNo);
            
            // Parse order ID
            Integer orderId = null;
            try {
                orderId = Integer.parseInt(vnp_TxnRef);
            } catch (NumberFormatException e) {
                LOGGER.warning("[VNPayReturnServlet] Invalid order ID: " + vnp_TxnRef);
                request.getSession().setAttribute("error", "Mã đơn hàng không hợp lệ.");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }
            
            // Find payment by order ID
            List<model.Payment> payments = paymentService.findPaymentsByOrder(orderId.longValue());
            if (payments == null || payments.isEmpty()) {
                LOGGER.warning("[VNPayReturnServlet] No payment found for order: " + orderId);
                request.getSession().setAttribute("error", "Không tìm thấy thông tin thanh toán cho đơn hàng này.");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }
            
            // Get the most recent payment (should be only one for VNPay)
            model.Payment payment = payments.get(0);
            
            // Check response code
            // vnp_ResponseCode = "00" means success
            if ("00".equals(vnp_ResponseCode)) {
                // Payment successful
                LOGGER.info("[VNPayReturnServlet] Payment successful for order: " + orderId);
                
                // Update payment status to PAID
                boolean updated = paymentService.updatePaymentStatus(
                    payment.getPaymentId(), 
                    PaymentStatus.PAID,
                    vnp_TransactionNo // Use VNPay transaction ID as reference
                );
                
                if (updated) {
                    LOGGER.info("[VNPayReturnServlet] Payment status updated to PAID successfully");
                    payment.setStatus(PaymentStatus.PAID);
                    if (vnp_TransactionNo != null && !vnp_TransactionNo.isBlank()) {
                        payment.setExternalRef(vnp_TransactionNo);
                    }
                } else {
                    LOGGER.warning("[VNPayReturnServlet] Failed to update payment status");
                }

                // Load order details to display invoice
                Order order = loadOrderWithItems(orderId);
                request.setAttribute("order", order);
                request.setAttribute("payments", payments);
                request.setAttribute("successMessage", "Thanh toán thành công! Đơn hàng của bạn đã được xác nhận.");
                
                // Forward to invoice/success page directly so user sees the bill even without login
                request.getRequestDispatcher("/views/cart/success.jsp").forward(request, response);
                return;
                
            } else {
                // Payment failed or cancelled
                String responseMessage = getResponseMessage(vnp_ResponseCode);
                LOGGER.warning("[VNPayReturnServlet] Payment failed: " + responseMessage);
                
                // Update payment status to FAILED if not already PAID
                if (payment.getStatus() != PaymentStatus.PAID) {
                    boolean failedUpdated = paymentService.updatePaymentStatus(payment.getPaymentId(), PaymentStatus.FAILED);
                    if (failedUpdated) {
                        payment.setStatus(PaymentStatus.FAILED);
                    }
                }
                
                request.setAttribute("errorMessage", "Thanh toán không thành công: " + responseMessage);
                request.getRequestDispatcher("/views/cart/checkout.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            LOGGER.severe("[VNPayReturnServlet] Error processing VNPay return: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi xử lý kết quả thanh toán. Vui lòng liên hệ hỗ trợ.");
            request.getRequestDispatcher("/views/cart/checkout.jsp").forward(request, response);
        }
    }

    /**
     * Load order and attach items for invoice display
     */
    private Order loadOrderWithItems(Integer orderId) {
        try {
            Optional<Order> orderOpt = orderDao.findByIdOptional(orderId);
            if (orderOpt.isEmpty()) {
                return null;
            }
            Order order = orderOpt.get();
            List<OrderItem> items = orderItemDao.findByOrderId(orderId);
            order.setItems(items);
            return order;
        } catch (Exception ex) {
            LOGGER.warning("[VNPayReturnServlet] Unable to load order details for invoice: " + ex.getMessage());
            return null;
        }
    }
    
    /**
     * Get human-readable response message from VNPay response code
     * @param responseCode VNPay response code
     * @return Response message
     */
    private String getResponseMessage(String responseCode) {
        if (responseCode == null) {
            return "Không xác định";
        }
        
        switch (responseCode) {
            case "00":
                return "Giao dịch thành công";
            case "07":
                return "Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường).";
            case "09":
                return "Thẻ/Tài khoản chưa đăng ký dịch vụ InternetBanking";
            case "10":
                return "Xác thực thông tin thẻ/tài khoản không đúng quá 3 lần";
            case "11":
                return "Đã hết hạn chờ thanh toán. Xin vui lòng thực hiện lại giao dịch.";
            case "12":
                return "Thẻ/Tài khoản bị khóa";
            case "13":
                return "Nhập sai mật khẩu xác thực giao dịch (OTP). Xin vui lòng thực hiện lại giao dịch.";
            case "51":
                return "Tài khoản không đủ số dư để thực hiện giao dịch.";
            case "65":
                return "Tài khoản đã vượt quá hạn mức giao dịch trong ngày.";
            case "75":
                return "Ngân hàng thanh toán đang bảo trì.";
            case "79":
                return "Nhập sai mật khẩu thanh toán quá số lần quy định. Xin vui lòng thực hiện lại giao dịch.";
            case "99":
                return "Lỗi không xác định";
            default:
                return "Lỗi không xác định (Mã: " + responseCode + ")";
        }
    }
}

