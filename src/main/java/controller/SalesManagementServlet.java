package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import model.Product;
import model.ProductType;
import model.shop.Order;
import model.shop.OrderItem;
import model.shop.OrderStatus;
import model.shop.PaymentStatus;
import service.OrderService;
import service.ProductService;
import service.shop.PaymentService;
import service.shop.PaymentServiceImpl;
import dao.shop.OrderItemDao;
import dao.shop.OrderDao;
import dao.PaymentDAO;

/*
    Note: 
 */
@WebServlet(name = "SalesManagementServlet", urlPatterns = "/admin/sales-management")
public class SalesManagementServlet extends HttpServlet {

    private final ProductService productService;
    private final OrderService orderService;

    public SalesManagementServlet() {
        this.productService = new ProductService();
        this.orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null)
            action = "";

        List<Product> products = getProducts();
        List<Order> orders = orderService.getAll();
        
        // Load order items for each order
        loadOrderItemsForOrders(orders);
        
        // Sắp xếp orders theo thời gian giảm dần (mới nhất trước)
        orders = sortOrdersByDateDescending(orders);
        
        // Lọc orders theo trạng thái nếu có filter parameter
        String statusFilter = req.getParameter("statusFilter");
        if (statusFilter != null && !statusFilter.isEmpty()) {
            orders = filterOrdersByStatus(orders, statusFilter);
        }

        // Load metrics from real data (sử dụng tất cả orders, không filter)
        List<Order> allOrders = orderService.getAll();
        loadOrderItemsForOrders(allOrders);
        loadDashboardMetrics(req, products, allOrders);

        switch (action) {
            case "deleteProduct" -> {
                int productId = Integer.parseInt(req.getParameter("productId"));
                handleDeleteProduct(products, productId);
                // Redirect back to products tab
                resp.sendRedirect(req.getContextPath() + "/admin/sales-management?tab=products&success=Sản phẩm đã được vô hiệu hóa!");
                return;
            }
            case "activateProduct" -> {
                int productId = Integer.parseInt(req.getParameter("productId"));
                handleActivateProduct(productId);
                // Redirect back to products tab
                resp.sendRedirect(req.getContextPath() + "/admin/sales-management?tab=products&success=Sản phẩm đã được kích hoạt thành công!");
                return;
            }
            case "confirmOrder" -> {
                // Check if this is an AJAX request
                String ajax = req.getParameter("ajax");
                if ("true".equals(ajax)) {
                    // Handle AJAX request - return JSON
                    handleConfirmOrderAjax(req, resp);
                    return;
                } else {
                    // Handle regular request - redirect
                    try {
                        int orderId = Integer.parseInt(req.getParameter("orderId"));
                        boolean success = handleConfirmOrder(orderId);
                        // Redirect to keep on orders tab with success message
                        if (success) {
                            String successMsg = java.net.URLEncoder.encode("Xác nhận đơn hàng thành công!", "UTF-8");
                            resp.sendRedirect(req.getContextPath() + "/admin/sales-management?tab=orders&success=" + successMsg);
                        } else {
                            String errorMsg = java.net.URLEncoder.encode("Không thể xác nhận đơn hàng. Vui lòng thử lại!", "UTF-8");
                            resp.sendRedirect(req.getContextPath() + "/admin/sales-management?tab=orders&error=" + errorMsg);
                        }
                    } catch (NumberFormatException e) {
                        String errorMsg = java.net.URLEncoder.encode("Mã đơn hàng không hợp lệ!", "UTF-8");
                        resp.sendRedirect(req.getContextPath() + "/admin/sales-management?tab=orders&error=" + errorMsg);
                    } catch (Exception e) {
                        System.err.println("[SalesManagementServlet] Error in confirmOrder action: " + e.getMessage());
                        e.printStackTrace();
                        String errorMsg = java.net.URLEncoder.encode("Lỗi khi xác nhận đơn hàng: " + e.getMessage(), "UTF-8");
                        resp.sendRedirect(req.getContextPath() + "/admin/sales-management?tab=orders&error=" + errorMsg);
                    }
                    return;
                }
            }
        }

        // Set active tab from request parameter
        String activeTab = req.getParameter("tab");
        if (activeTab == null || activeTab.isEmpty()) {
            activeTab = "products";
        }
        req.setAttribute("activeTab", activeTab);
        
        // Set status filter parameter
        if (statusFilter != null && !statusFilter.isEmpty()) {
            req.setAttribute("statusFilter", statusFilter);
        }
        
        // Get messages from request parameters (for redirect messages)
        String success = req.getParameter("success");
        String error = req.getParameter("error");
        if (success != null && !success.isEmpty()) {
            req.setAttribute("success", success);
        }
        if (error != null && !error.isEmpty()) {
            req.setAttribute("error", error);
        }

        req.setAttribute("orders", orders);
        req.setAttribute("products", products);

        req.getRequestDispatcher("/views/admin/sales_management.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("\nSales ManagementServlet Post called");
        System.out.println("parameters' name: " + req.getParameterNames());

        String action = req.getParameter("action");
        if (action == null)
            action = "";
        switch (action) {
            case "addProduct" -> {
                handleAddProduct(req, resp);
            }
            case "editProduct" -> {
                handleEditProduct(req, resp);
            }
            default -> {
                System.out.println("Action: ???");
            }
        }

        // Redirect with active tab parameter
        String activeTab = req.getParameter("activeTab");
        if (activeTab == null || activeTab.isEmpty()) {
            activeTab = "products";
        }
        resp.sendRedirect(req.getContextPath() + "/admin/sales-management?tab=" + activeTab);
    }

    private void handleEditProduct(HttpServletRequest req, HttpServletResponse resp) {
        int id = Integer.parseInt(req.getParameter("productId"));
        String name = req.getParameter("name");
        ProductType productType = ProductType.fromCode(req.getParameter("productType"));
        BigDecimal price = new BigDecimal(req.getParameter("price"));
        int stockQuantity = Integer.parseInt(req.getParameter("stockQuantity"));

        Product product = productService.getById(id);
        if (product != null) {
            product.setProductName(name);
            product.setProductType(productType);
            product.setPrice(price);
            product.setStockQuantity(stockQuantity);
            productService.update(product);
        }
        productService.update(product);
    }

    /**
     * Tính số đơn hàng trong tháng hiện tại
     */
    private int getMonthlyOrderCount(List<Order> orders) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        
        return (int) orders.stream()
                .filter(order -> order.getCreatedAt() != null && 
                        !order.getCreatedAt().isBefore(startOfMonth) &&
                        !order.getCreatedAt().isAfter(now))
                .count();
    }

    /**
     * Tính doanh thu hôm nay từ các payment có status = PAID
     * Sử dụng timezone +7 (Asia/Ho_Chi_Minh) và tính từ paidAt
     */
    private double getTodayRevenue() {
        try {
            PaymentDAO paymentDAO = new PaymentDAO();
            BigDecimal revenue = paymentDAO.getRevenueToday();
            return revenue.doubleValue();
        } catch (Exception e) {
            System.err.println("[SalesManagementServlet] Error getting today revenue: " + e.getMessage());
            e.printStackTrace();
            return 0.0;
        }
    }

    /**
     * Đếm số sản phẩm sắp hết hàng (stock < 10)
     */
    private int getLowStockCount(List<Product> products) {
        return (int) products.stream()
                .filter(p -> p.getStockQuantity() != null && p.getStockQuantity() < 10)
                .count();
    }

    private void handleAddProduct(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter("name");
        ProductType productType = ProductType.fromCode(req.getParameter("productType"));
        BigDecimal price = new BigDecimal(req.getParameter("price"));
        int stockQuantity = Integer.parseInt(req.getParameter("stockQuantity"));
        Product product = new Product(name, productType, price, stockQuantity);
        productService.add(product);
    }

    /**
     * Xóa sản phẩm (set active = false)
     */
    private void handleDeleteProduct(List<Product> products, int id) {
        Product product = productService.getById(id);
        if (product != null) {
            product.setActive(false);
            productService.update(product);
        }
    }

    /**
     * Kích hoạt lại sản phẩm (set active = true)
     */
    private void handleActivateProduct(int productId) {
        Product product = productService.getById(productId);
        if (product != null) {
            product.setActive(true);
            productService.update(product);
        }
    }

    /**
     * Load dashboard metrics từ dữ liệu thật
     */
    private void loadDashboardMetrics(HttpServletRequest req, List<Product> products, List<Order> orders) {
        int productCount = products.size();
        int monthlyOrderCount = getMonthlyOrderCount(orders);
        double todayRevenue = getTodayRevenue();
        int lowStockCount = getLowStockCount(products);

        req.setAttribute("productCount", productCount);
        req.setAttribute("monthlyOrderCount", monthlyOrderCount);
        req.setAttribute("todayRevenue", todayRevenue);
        req.setAttribute("lowStockCount", lowStockCount);
    }

    /**
     * Lấy tất cả sản phẩm (bao gồm cả inactive)
     */
    private List<Product> getProducts() {
        return productService.getAll();
    }

    /**
     * Load order items cho mỗi order - Tối ưu bằng batch loading để tránh N+1 query problem
     */
    private void loadOrderItemsForOrders(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return;
        }
        
        OrderItemDao orderItemDao = new OrderItemDao();
        
        // Lấy danh sách order IDs
        List<Integer> orderIds = orders.stream()
                .filter(order -> order.getOrderId() != null)
                .map(Order::getOrderId)
                .distinct()
                .collect(java.util.stream.Collectors.toList());
        
        if (orderIds.isEmpty()) {
            return;
        }
        
        // Load tất cả order items trong một query duy nhất (batch loading)
        List<OrderItem> allItems = orderItemDao.findByOrderIds(orderIds);
        
        // Group items by orderId để map vào từng order
        Map<Integer, List<OrderItem>> itemsByOrderId = allItems.stream()
                .collect(java.util.stream.Collectors.groupingBy(OrderItem::getOrderId));
        
        // Map items vào từng order
        for (Order order : orders) {
            if (order.getOrderId() != null) {
                List<OrderItem> items = itemsByOrderId.get(order.getOrderId());
                if (items != null) {
                    order.setItems(items);
                } else {
                    order.setItems(new java.util.ArrayList<>());
                }
            }
        }
    }

    /**
     * Sắp xếp orders theo thời gian giảm dần (mới nhất trước)
     * Sử dụng createdAt nếu có, nếu không thì dùng orderDate
     */
    private List<Order> sortOrdersByDateDescending(List<Order> orders) {
        return orders.stream()
                .sorted(Comparator
                        .comparing((Order o) -> {
                            if (o.getCreatedAt() != null) {
                                return o.getCreatedAt();
                            } else if (o.getOrderDate() != null) {
                                return o.getOrderDate();
                            } else {
                                return LocalDateTime.MIN;
                            }
                        })
                        .reversed()) // Giảm dần (mới nhất trước)
                .collect(Collectors.toList());
    }

    /**
     * Lọc orders theo trạng thái
     * @param orders Danh sách orders cần lọc
     * @param statusFilter "pending" cho chưa xác nhận, "confirmed" cho đã xác nhận, "all" cho tất cả
     * @return Danh sách orders đã được lọc
     */
    private List<Order> filterOrdersByStatus(List<Order> orders, String statusFilter) {
        if (statusFilter == null || statusFilter.isEmpty() || "all".equalsIgnoreCase(statusFilter)) {
            return orders;
        }
        
        return orders.stream()
                .filter(order -> {
                    if ("pending".equalsIgnoreCase(statusFilter)) {
                        // Chưa xác nhận: PENDING, PREPARING, READY, PROCESSING
                        return order.getOrderStatus() == OrderStatus.PENDING || 
                               order.getOrderStatus() == OrderStatus.PREPARING ||
                               order.getOrderStatus() == OrderStatus.READY ||
                               order.getOrderStatus() == OrderStatus.PROCESSING;
                    } else if ("confirmed".equalsIgnoreCase(statusFilter)) {
                        // Đã xác nhận: CONFIRMED, COMPLETED, SHIPPED, DELIVERED
                        return order.getOrderStatus() == OrderStatus.CONFIRMED || 
                               order.getOrderStatus() == OrderStatus.COMPLETED ||
                               order.getOrderStatus() == OrderStatus.SHIPPED ||
                               order.getOrderStatus() == OrderStatus.DELIVERED;
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    /**
     * Xử lý AJAX request để xác nhận đơn hàng
     */
    private void handleConfirmOrderAjax(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        
        // Enable CORS if needed
        resp.setHeader("Access-Control-Allow-Origin", "*");
        
        try {
            String orderIdParam = req.getParameter("orderId");
            System.out.println("[SalesManagementServlet] AJAX confirmOrder - orderId: " + orderIdParam);
            
            if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
                resp.getWriter().write("{\"success\": false, \"message\": \"Mã đơn hàng không được để trống!\"}");
                return;
            }
            
            int orderId = Integer.parseInt(orderIdParam);
            boolean success = handleConfirmOrder(orderId);
            
            System.out.println("[SalesManagementServlet] AJAX confirmOrder - result: " + success);
            
            if (success) {
                // Return success JSON
                String jsonResponse = "{\"success\": true, \"message\": \"Đơn hàng đã được hoàn thành thành công!\"}";
                resp.getWriter().write(jsonResponse);
                System.out.println("[SalesManagementServlet] AJAX response: " + jsonResponse);
            } else {
                // Return error JSON
                String jsonResponse = "{\"success\": false, \"message\": \"Không thể hoàn thành đơn hàng. Vui lòng thử lại!\"}";
                resp.getWriter().write(jsonResponse);
                System.out.println("[SalesManagementServlet] AJAX response: " + jsonResponse);
            }
        } catch (NumberFormatException e) {
            System.err.println("[SalesManagementServlet] NumberFormatException: " + e.getMessage());
            resp.getWriter().write("{\"success\": false, \"message\": \"Mã đơn hàng không hợp lệ!\"}");
        } catch (Exception e) {
            System.err.println("[SalesManagementServlet] Error in confirmOrder AJAX: " + e.getMessage());
            e.printStackTrace();
            String errorMessage = e.getMessage() != null ? e.getMessage().replace("\"", "\\\"") : "Unknown error";
            resp.getWriter().write("{\"success\": false, \"message\": \"Lỗi khi xử lý: " + errorMessage + "\"}");
        } finally {
            resp.getWriter().flush();
        }
    }

    /**
     * Xác nhận đơn hàng - cập nhật payment status thành PAID và order status thành CONFIRMED
     * @return true nếu thành công, false nếu không tìm thấy đơn hàng hoặc có lỗi
     */
    private boolean handleConfirmOrder(int orderId) {
        try {
            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                System.err.println("[SalesManagementServlet] Order not found: " + orderId);
                return false;
            }
            
            // Kiểm tra trạng thái hiện tại
            if (order.getOrderStatus() == OrderStatus.CONFIRMED) {
                System.out.println("[SalesManagementServlet] Order already confirmed: " + orderId);
                return true; // Đã confirmed rồi, coi như thành công
            }
            
            // Lấy tất cả payments của order
            PaymentService paymentService = new PaymentServiceImpl();
            List<model.Payment> payments = paymentService.findPaymentsByOrder((long) orderId);
            
            // Update payment status thành PAID cho tất cả payments của order
            if (payments != null && !payments.isEmpty()) {
                for (model.Payment payment : payments) {
                    if (payment.getStatus() != PaymentStatus.PAID) {
                        boolean paymentUpdated = paymentService.updatePaymentStatus(
                            payment.getPaymentId(), 
                            PaymentStatus.PAID
                        );
                        if (paymentUpdated) {
                            System.out.println("[SalesManagementServlet] Payment updated to PAID: paymentId=" + payment.getPaymentId());
                        } else {
                            System.err.println("[SalesManagementServlet] Failed to update payment: paymentId=" + payment.getPaymentId());
                        }
                    }
                }
            } else {
                System.out.println("[SalesManagementServlet] No payments found for order: " + orderId);
            }
            
            // Update order status thành CONFIRMED
            OrderDao orderDao = new OrderDao();
            try {
                orderDao.updateOrderStatus(orderId, OrderStatus.CONFIRMED);
                System.out.println("[SalesManagementServlet] Order confirmed successfully: " + orderId);
                return true;
            } catch (RuntimeException e) {
                System.err.println("[SalesManagementServlet] Failed to update order status: " + orderId);
                System.err.println("[SalesManagementServlet] Error: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            System.err.println("[SalesManagementServlet] Error confirming order: " + orderId);
            e.printStackTrace();
            return false;
        }
    }
}

