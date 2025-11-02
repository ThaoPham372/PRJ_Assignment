package com.gym.controller;

import com.gym.dao.shop.OrderDao;
import com.gym.model.shop.PaymentStatus;
import com.gym.service.shop.MoMoPaymentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet to handle MoMo payment callbacks
 * - Return URL: User redirect after payment
 * - Notify URL: MoMo webhook to notify payment result
 */
@WebServlet(name = "MoMoCallbackServlet", urlPatterns = {"/order/momo/*"})
public class MoMoCallbackServlet extends HttpServlet {
    private OrderDao orderDao;
    private MoMoPaymentService momoService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.orderDao = new OrderDao();
        this.momoService = new MoMoPaymentService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            pathInfo = "";
        }
        
        // Handle /order/momo/return - User redirect after payment
        if (pathInfo.equals("/return")) {
            handleReturnUrl(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            pathInfo = "";
        }
        
        // Handle /order/momo/notify - MoMo webhook
        if (pathInfo.equals("/notify")) {
            handleNotifyUrl(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Handle return URL (user redirect)
     */
    private void handleReturnUrl(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String orderIdStr = request.getParameter("orderId");
        
        // MoMo returns these parameters
        String resultCode = request.getParameter("resultCode");
        
        if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?error=invalid_order");
            return;
        }

        try {
            Long orderId = Long.parseLong(orderIdStr);
            var orderOpt = orderDao.findById(orderId);
            
            if (orderOpt.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/cart?error=order_not_found");
                return;
            }
            
            var order = orderOpt.get();
            
            // If payment successful
            if ("0".equals(resultCode)) {
                // Update payment status to PAID
                orderDao.updatePayment(orderId, PaymentStatus.PAID, order.getPaymentMethod());
                response.sendRedirect(request.getContextPath() + "/order/success?orderId=" + orderId);
            } else {
                // Payment failed or cancelled
                orderDao.updatePayment(orderId, PaymentStatus.FAILED, order.getPaymentMethod());
                request.getSession().setAttribute("error", "Thanh toán thất bại hoặc đã bị hủy");
                response.sendRedirect(request.getContextPath() + "/cart?error=payment_failed");
            }
        } catch (Exception e) {
            System.err.println("[MoMoCallback] Error handling return URL: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/cart?error=payment_error");
        }
    }

    /**
     * Handle notify URL (MoMo webhook)
     */
    private void handleNotifyUrl(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get parameters from MoMo
            String orderId = request.getParameter("orderId");
            String requestId = request.getParameter("requestId");
            String amountStr = request.getParameter("amount");
            String orderInfo = request.getParameter("orderInfo");
            String orderStatusStr = request.getParameter("orderStatus");
            String responseTime = request.getParameter("responseTime");
            String message = request.getParameter("message");
            String extraData = request.getParameter("extraData");
            String transId = request.getParameter("transId");
            String signature = request.getParameter("signature");
            
            if (orderId == null || amountStr == null || orderStatusStr == null || signature == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Missing required parameters");
                return;
            }
            
            // Verify signature
            long amount = Long.parseLong(amountStr);
            int orderStatus = Integer.parseInt(orderStatusStr);
            
            boolean isValid = momoService.verifySignature(
                orderId, amount, orderInfo != null ? orderInfo : "",
                orderStatus, responseTime != null ? responseTime : "",
                requestId != null ? requestId : "",
                transId != null ? transId : "",
                message != null ? message : "",
                extraData != null ? extraData : "",
                signature
            );
            
            if (!isValid) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid signature");
                return;
            }
            
            // Find order by order number
            var orderOpt = orderDao.findByOrderNumber(orderId);
            if (orderOpt.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Order not found");
                return;
            }
            
            var order = orderOpt.get();
            
            // Update payment status based on MoMo response
            // orderStatus: 0 = Success, others = Failed/Cancelled
            if (orderStatus == 0) {
                orderDao.updatePayment(order.getOrderId(), PaymentStatus.PAID, order.getPaymentMethod());
            } else {
                orderDao.updatePayment(order.getOrderId(), PaymentStatus.FAILED, order.getPaymentMethod());
            }
            
            // Return success to MoMo
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":\"success\"}");
            
        } catch (Exception e) {
            System.err.println("[MoMoCallback] Error handling notify URL: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal server error");
        }
    }
}
