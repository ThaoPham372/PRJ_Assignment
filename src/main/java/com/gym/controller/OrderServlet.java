package com.gym.controller;

import com.gym.dao.shop.OrderDao;
import com.gym.dao.shop.OrderItemDao;
import com.gym.model.shop.Order;
import com.gym.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Servlet for order-related operations (list orders, view order details, success page)
 */
@WebServlet(name = "OrderServlet", urlPatterns = {"/order/*"})
public class OrderServlet extends HttpServlet {
    private OrderDao orderDao;
    private OrderItemDao orderItemDao;

    @Override
    public void init() throws ServletException {
        super.init();
        this.orderDao = new OrderDao();
        this.orderItemDao = new OrderItemDao();
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
        String action = pathInfo == null || pathInfo.equals("/") ? "list" : pathInfo.substring(1);

        switch (action) {
            case "list":
                showOrderList(request, response, userId);
                break;
            case "success":
                showSuccess(request, response, userId);
                break;
            case "detail":
                showOrderDetail(request, response, userId);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/order/list");
                break;
        }
    }

    /**
     * Show list of all orders for the user
     */
    private void showOrderList(HttpServletRequest request, HttpServletResponse response, Long userId)
            throws ServletException, IOException {
        try {
            List<Order> orders = orderDao.findByUserId(userId);
            
            // Load order items and payments for each order
            com.gym.service.PaymentService paymentService = new com.gym.service.PaymentServiceImpl();
            java.util.Map<Long, java.util.List<com.gym.model.Payment>> paymentsMap = new java.util.HashMap<>();
            
            for (Order order : orders) {
                List<com.gym.model.shop.OrderItem> items = orderItemDao.findByOrderId(order.getOrderId());
                order.setItems(items);
                
                // Load payments for this order
                List<com.gym.model.Payment> payments = paymentService.findPaymentsByOrder(order.getOrderId());
                paymentsMap.put(order.getOrderId(), payments);
            }
            
            request.setAttribute("orders", orders);
            request.setAttribute("paymentsMap", paymentsMap);
            request.getRequestDispatcher("/views/cart/order-list.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("[OrderServlet] Error loading order list: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Không thể tải danh sách đơn hàng: " + e.getMessage());
            request.getRequestDispatcher("/views/cart/order-list.jsp").forward(request, response);
        }
    }

    /**
     * Show order success page after checkout
     */
    private void showSuccess(HttpServletRequest request, HttpServletResponse response, Long userId)
            throws ServletException, IOException {
        String orderIdStr = request.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
            // If no orderId, redirect to order list
            response.sendRedirect(request.getContextPath() + "/order/list");
            return;
        }

        try {
            Long orderId = Long.parseLong(orderIdStr);
            Optional<Order> orderOpt = orderDao.findById(orderId);
            
            if (orderOpt.isEmpty()) {
                request.setAttribute("error", "Không tìm thấy đơn hàng");
                request.getRequestDispatcher("/views/cart/order-list.jsp").forward(request, response);
                return;
            }

            Order order = orderOpt.get();
            
            // Verify this order belongs to the current user
            if (!order.getUserId().equals(userId)) {
                response.sendRedirect(request.getContextPath() + "/order/list");
                return;
            }

            // Load order items
            List<com.gym.model.shop.OrderItem> items = orderItemDao.findByOrderId(orderId);
            order.setItems(items);

            // Load payments for this order
            com.gym.service.PaymentService paymentService = new com.gym.service.PaymentServiceImpl();
            List<com.gym.model.Payment> payments = paymentService.findPaymentsByOrder(orderId);
            request.setAttribute("payments", payments);

            request.setAttribute("order", order);
            request.getRequestDispatcher("/views/cart/success.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/order/list");
        } catch (Exception e) {
            System.err.println("[OrderServlet] Error loading order success: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Không thể tải thông tin đơn hàng: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/order/list");
        }
    }

    /**
     * Show order detail page
     */
    private void showOrderDetail(HttpServletRequest request, HttpServletResponse response, Long userId)
            throws ServletException, IOException {
        String orderIdStr = request.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/order/list");
            return;
        }

        try {
            Long orderId = Long.parseLong(orderIdStr);
            Optional<Order> orderOpt = orderDao.findById(orderId);
            
            if (orderOpt.isEmpty()) {
                request.setAttribute("error", "Không tìm thấy đơn hàng");
                response.sendRedirect(request.getContextPath() + "/order/list");
                return;
            }

            Order order = orderOpt.get();
            
            // Verify this order belongs to the current user
            if (!order.getUserId().equals(userId)) {
                response.sendRedirect(request.getContextPath() + "/order/list");
                return;
            }

            // Load order items
            List<com.gym.model.shop.OrderItem> items = orderItemDao.findByOrderId(orderId);
            order.setItems(items);

            // Load payments for this order (for payment history display)
            com.gym.service.PaymentService paymentService = new com.gym.service.PaymentServiceImpl();
            List<com.gym.model.Payment> payments = paymentService.findPaymentsByOrder(orderId);
            request.setAttribute("payments", payments);

            request.setAttribute("order", order);
            request.getRequestDispatcher("/views/cart/order-detail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/order/list");
        } catch (Exception e) {
            System.err.println("[OrderServlet] Error loading order detail: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Không thể tải thông tin đơn hàng: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/order/list");
        }
    }
}

