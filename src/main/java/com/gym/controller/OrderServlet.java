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

        String pathInfo = request.getPathInfo();
        String action = pathInfo == null || pathInfo.equals("/") ? "" : pathInfo.substring(1);

        if ("cancel".equals(action)) {
            handleCancelOrder(request, response, userId);
        } else {
            response.sendRedirect(request.getContextPath() + "/order/list");
        }
    }
    
    /**
     * Handle cancel order request
     * Only allows cancellation if order status is PENDING
     */
    private void handleCancelOrder(HttpServletRequest request, HttpServletResponse response, Long userId)
            throws ServletException, IOException {
        String orderIdStr = request.getParameter("orderId");
        String cancellationReason = request.getParameter("cancellationReason");
        
        if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
            request.getSession().setAttribute("error", "Không tìm thấy đơn hàng");
            response.sendRedirect(request.getContextPath() + "/order/list");
            return;
        }

        try {
            Long orderId = Long.parseLong(orderIdStr);
            
            // Cancel order using OrderDao
            boolean cancelled = orderDao.cancelOrder(orderId, userId, cancellationReason);
            
            if (cancelled) {
                request.getSession().setAttribute("success", "Đã hủy đơn hàng thành công");
            } else {
                // Check if order exists and belongs to user
                Optional<Order> orderOpt = orderDao.findById(orderId);
                if (orderOpt.isEmpty()) {
                    request.getSession().setAttribute("error", "Không tìm thấy đơn hàng");
                } else {
                    Order order = orderOpt.get();
                    if (!order.getUserId().equals(userId)) {
                        request.getSession().setAttribute("error", "Bạn không có quyền hủy đơn hàng này");
                    } else if (order.getOrderStatus() != com.gym.model.shop.OrderStatus.PENDING) {
                        request.getSession().setAttribute("error", "Chỉ có thể hủy đơn hàng đang chờ xác nhận");
                    } else {
                        request.getSession().setAttribute("error", "Không thể hủy đơn hàng. Vui lòng thử lại.");
                    }
                }
            }
            
            response.sendRedirect(request.getContextPath() + "/order/list");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Mã đơn hàng không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/order/list");
        } catch (Exception e) {
            System.err.println("[OrderServlet] Error cancelling order: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi khi hủy đơn hàng: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/order/list");
        }
    }

    /**
     * Show list of all orders for the user
     */
    private void showOrderList(HttpServletRequest request, HttpServletResponse response, Long userId)
            throws ServletException, IOException {
        try {
            System.out.println("[OrderServlet] showOrderList - Loading orders for userId: " + userId);
            List<Order> orders = orderDao.findByUserId(userId);
            System.out.println("[OrderServlet] showOrderList - Found " + (orders != null ? orders.size() : 0) + " orders");
            
            // Load order items and payments for each order
            com.gym.service.PaymentService paymentService = new com.gym.service.PaymentServiceImpl();
            java.util.Map<Long, java.util.List<com.gym.model.Payment>> paymentsMap = new java.util.HashMap<>();
            
            for (Order order : orders) {
                try {
                    System.out.println("[OrderServlet] showOrderList - Processing order: " + order.getOrderId() + ", status: " + order.getOrderStatus());
                    List<com.gym.model.shop.OrderItem> items = orderItemDao.findByOrderId(order.getOrderId());
                    System.out.println("[OrderServlet] showOrderList - Found " + (items != null ? items.size() : 0) + " items for order " + order.getOrderId());
                    order.setItems(items);
                    
                    // Load payments for this order
                    List<com.gym.model.Payment> payments = paymentService.findPaymentsByOrder(order.getOrderId());
                    System.out.println("[OrderServlet] showOrderList - Found " + (payments != null ? payments.size() : 0) + " payments for order " + order.getOrderId());
                
                // For package orders: also find payments by membershipId
                // Check if order contains a package (packageId in order items)
                boolean hasPackage = false;
                Long packageId = null;
                for (com.gym.model.shop.OrderItem item : items) {
                    if (item.getPackageId() != null) {
                        hasPackage = true;
                        packageId = item.getPackageId();
                        break;
                    }
                }
                
                // If order contains a package, find membership and its payments
                if (hasPackage && packageId != null) {
                    try {
                        // Find membership by packageId and userId
                        com.gym.dao.membership.MembershipDao membershipDao = new com.gym.dao.membership.MembershipDao();
                        java.util.List<com.gym.model.membership.Membership> memberships = 
                            membershipDao.listByUser(order.getUserId().intValue());
                        
                        // Find the membership that matches this packageId
                        for (com.gym.model.membership.Membership membership : memberships) {
                            if (membership.getPackageId().equals(packageId)) {
                                // Find payments for this membership
                                List<com.gym.model.Payment> membershipPayments = 
                                    paymentService.findPaymentsByMembership(membership.getMembershipId());
                                
                                // Merge with existing payments (avoid duplicates)
                                for (com.gym.model.Payment mp : membershipPayments) {
                                    boolean exists = false;
                                    for (com.gym.model.Payment p : payments) {
                                        if (p.getPaymentId().equals(mp.getPaymentId())) {
                                            exists = true;
                                            break;
                                        }
                                    }
                                    if (!exists) {
                                        payments.add(mp);
                                    }
                                }
                                break;
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("[OrderServlet] Error finding membership payments for order: " + order.getOrderId() + ", " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                
                paymentsMap.put(order.getOrderId(), payments);
                } catch (Exception e) {
                    System.err.println("[OrderServlet] Error processing order " + order.getOrderId() + ": " + e.getMessage());
                    e.printStackTrace();
                    // Continue with next order even if this one fails
                    paymentsMap.put(order.getOrderId(), new java.util.ArrayList<>());
                }
            }
            
            System.out.println("[OrderServlet] showOrderList - Setting orders attribute: " + (orders != null ? orders.size() : 0) + " orders");
            System.out.println("[OrderServlet] showOrderList - Setting paymentsMap attribute: " + paymentsMap.size() + " entries");
            
            request.setAttribute("orders", orders);
            request.setAttribute("paymentsMap", paymentsMap);
            
            System.out.println("[OrderServlet] showOrderList - Forwarding to order-list.jsp");
            request.getRequestDispatcher("/views/cart/order-list.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("[OrderServlet] Error loading order list: " + e.getMessage());
            e.printStackTrace();
            
            // Set empty lists to prevent JSP errors
            request.setAttribute("orders", new java.util.ArrayList<>());
            request.setAttribute("paymentsMap", new java.util.HashMap<>());
            request.setAttribute("error", "Không thể tải danh sách đơn hàng: " + e.getMessage());
            
            try {
                request.getRequestDispatcher("/views/cart/order-list.jsp").forward(request, response);
            } catch (Exception ex) {
                System.err.println("[OrderServlet] Error forwarding to JSP: " + ex.getMessage());
                ex.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tải trang danh sách đơn hàng");
            }
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

