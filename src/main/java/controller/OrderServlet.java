package controller;

import dao.shop.OrderDao;
import dao.shop.OrderItemDao;
import model.shop.Order;
import model.shop.OrderItem;
import model.shop.OrderStatus;
import Utils.SessionUtil;
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
 * Follows MVC pattern - Controller layer
 * Handles order listing, detail view, success page, and cancel operations
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

        Integer userIdInt = SessionUtil.getUserId(request);
        if (userIdInt == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        Long userId = userIdInt.longValue();

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

        Integer userIdInt = SessionUtil.getUserId(request);
        if (userIdInt == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        Long userId = userIdInt.longValue();

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
                Optional<Order> orderOpt = orderDao.findByIdOptional(orderId.intValue());
                if (orderOpt.isEmpty()) {
                    request.getSession().setAttribute("error", "Không tìm thấy đơn hàng");
                } else {
                    Order order = orderOpt.get();
                    if (!order.getMemberId().equals(userId.intValue())) {
                        request.getSession().setAttribute("error", "Bạn không có quyền hủy đơn hàng này");
                    } else if (order.getOrderStatus() != OrderStatus.PENDING) {
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
            List<Order> orders = orderDao.findByMemberId(userId.intValue());
            System.out.println("[OrderServlet] showOrderList - Found " + (orders != null ? orders.size() : 0) + " orders");
            
            // Load order items and payments for each order
            service.shop.PaymentService paymentService = new service.shop.PaymentServiceImpl();
            java.util.Map<Integer, java.util.List<model.Payment>> paymentsMap = new java.util.HashMap<>();
            
            for (Order order : orders) {
                try {
                    System.out.println("[OrderServlet] showOrderList - Processing order: " + order.getOrderId() + ", status: " + order.getOrderStatus());
                    
                    // Load items - with null safety
                    List<OrderItem> items = orderItemDao.findByOrderId(order.getOrderId());
                    if (items == null) {
                        items = new java.util.ArrayList<>();
                    }
                    System.out.println("[OrderServlet] showOrderList - Found " + items.size() + " items for order " + order.getOrderId());
                    order.setItems(items);
                    
                    // Load payments for this order
                    List<model.Payment> payments = paymentService.findPaymentsByOrder(Long.valueOf(order.getOrderId()));
                    System.out.println("[OrderServlet] showOrderList - Found " + (payments != null ? payments.size() : 0) + " payments for order " + order.getOrderId());
                
                // For package orders: also find payments by membershipId
                // Check if order contains a package (packageId in order items)
                boolean hasPackage = false;
                Integer packageId = null;
                if (items != null && !items.isEmpty()) {
                    for (OrderItem item : items) {
                        if (item != null && item.getPackageId() != null) {
                            hasPackage = true;
                            packageId = item.getPackageId();
                            break;
                        }
                    }
                }
                
                // If order contains a package, find membership and its payments
                if (hasPackage && packageId != null) {
                    try {
                        // Find membership by packageId and memberId
                        dao.MembershipDAO membershipDao = new dao.MembershipDAO();
                        java.util.List<model.Membership> allMemberships = membershipDao.findAll();
                        
                        // Find the membership that matches this packageId and member
                        for (model.Membership membership : allMemberships) {
                            if (membership.getMember() != null && 
                                membership.getMember().getId().equals(order.getMemberId()) &&
                                membership.getPackageO() != null && 
                                membership.getPackageO().getId().equals(packageId)) {
                                // Find payments for this membership
                                List<model.Payment> membershipPayments = 
                                    paymentService.findPaymentsByMembership(Long.valueOf(membership.getId()));
                                
                                // Merge with existing payments (avoid duplicates)
                                for (model.Payment mp : membershipPayments) {
                                    boolean exists = false;
                                    for (model.Payment p : payments) {
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
                
                // Ensure payments is not null before putting into map
                if (payments == null) {
                    payments = new java.util.ArrayList<>();
                }
                paymentsMap.put(order.getOrderId(), payments);
                
                System.out.println("[OrderServlet] showOrderList - Successfully processed order: " + order.getOrderId());
                
                } catch (Exception e) {
                    System.err.println("[OrderServlet] ❌ ERROR processing order " + order.getOrderId() + ": " + e.getMessage());
                    e.printStackTrace();
                    // Ensure order still has empty items and payments to prevent JSP errors
                    if (order.getItems() == null) {
                        order.setItems(new java.util.ArrayList<>());
                    }
                    paymentsMap.put(order.getOrderId(), new java.util.ArrayList<>());
                    
                    // ⚠️ IMPORTANT: Continue with next order - DON'T BREAK THE LOOP
                    System.err.println("[OrderServlet] Continuing to next order...");
                }
            }
            
            System.out.println("[OrderServlet] ========================================");
            System.out.println("[OrderServlet] showOrderList - SUMMARY:");
            System.out.println("[OrderServlet]   Total orders loaded: " + orders.size());
            System.out.println("[OrderServlet]   Orders with payment data: " + paymentsMap.size());
            for (Order o : orders) {
                System.out.println("[OrderServlet]   - Order " + o.getOrderId() + ": " + 
                    (o.getItems() != null ? o.getItems().size() : 0) + " items");
            }
            System.out.println("[OrderServlet] ========================================");
            
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
            Optional<Order> orderOpt = orderDao.findByIdOptional(orderId.intValue());
            
            if (orderOpt.isEmpty()) {
                request.setAttribute("error", "Không tìm thấy đơn hàng");
                request.getRequestDispatcher("/views/cart/order-list.jsp").forward(request, response);
                return;
            }

            Order order = orderOpt.get();
            
            // Verify this order belongs to the current user
            if (!order.getMemberId().equals(userId.intValue())) {
                response.sendRedirect(request.getContextPath() + "/order/list");
                return;
            }

            // Load order items
            List<OrderItem> items = orderItemDao.findByOrderId(orderId.intValue());
            order.setItems(items);

            // Load payments for this order
            service.shop.PaymentService paymentService = new service.shop.PaymentServiceImpl();
            List<model.Payment> payments = paymentService.findPaymentsByOrder(orderId.longValue());
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
            Optional<Order> orderOpt = orderDao.findByIdOptional(orderId.intValue());
            
            if (orderOpt.isEmpty()) {
                request.setAttribute("error", "Không tìm thấy đơn hàng");
                response.sendRedirect(request.getContextPath() + "/order/list");
                return;
            }

            Order order = orderOpt.get();
            
            // Verify this order belongs to the current user
            if (!order.getMemberId().equals(userId.intValue())) {
                response.sendRedirect(request.getContextPath() + "/order/list");
                return;
            }

            // Load order items
            List<OrderItem> items = orderItemDao.findByOrderId(orderId.intValue());
            order.setItems(items);

            // Load payments for this order (for payment history display)
            service.shop.PaymentService paymentService = new service.shop.PaymentServiceImpl();
            List<model.Payment> payments = paymentService.findPaymentsByOrder(orderId.longValue());
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






