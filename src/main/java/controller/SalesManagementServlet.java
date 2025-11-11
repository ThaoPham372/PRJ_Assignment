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
import model.Product;
import model.ProductType;
import model.shop.Order;
import model.shop.OrderItem;
import model.shop.OrderStatus;
import service.OrderService;
import service.ProductService;
import dao.shop.OrderItemDao;
import dao.shop.OrderDao;

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

        // Load metrics from real data
        loadDashboardMetrics(req, products, orders);

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
     * Tính doanh thu trong tháng hiện tại từ các đơn hàng đã hoàn thành
     */
    private double getMonthlyRevenue(List<Order> orders) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        
        BigDecimal revenue = orders.stream()
                .filter(order -> order.getCreatedAt() != null && 
                        !order.getCreatedAt().isBefore(startOfMonth) &&
                        !order.getCreatedAt().isAfter(now) &&
                        order.getOrderStatus() == OrderStatus.COMPLETED &&
                        order.getTotalAmount() != null)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return revenue.doubleValue();
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
        double monthlyRevenue = getMonthlyRevenue(orders);
        int lowStockCount = getLowStockCount(products);

        req.setAttribute("productCount", productCount);
        req.setAttribute("monthlyOrderCount", monthlyOrderCount);
        req.setAttribute("monthlyRevenue", monthlyRevenue);
        req.setAttribute("lowStockCount", lowStockCount);
    }

    /**
     * Lấy tất cả sản phẩm (bao gồm cả inactive)
     */
    private List<Product> getProducts() {
        return productService.getAll();
    }

    /**
     * Load order items cho mỗi order
     */
    private void loadOrderItemsForOrders(List<Order> orders) {
        OrderItemDao orderItemDao = new OrderItemDao();
        for (Order order : orders) {
            if (order.getOrderId() != null) {
                List<OrderItem> items = orderItemDao.findByOrderId(order.getOrderId());
                if (items != null) {
                    order.setItems(items);
                }
            }
        }
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
     * Xác nhận đơn hàng - cập nhật trạng thái thành COMPLETED
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
            if (order.getOrderStatus() == OrderStatus.COMPLETED) {
                System.out.println("[SalesManagementServlet] Order already completed: " + orderId);
                return true; // Đã completed rồi, coi như thành công
            }
            
            // Sử dụng OrderDao.updateOrderStatus để đảm bảo update đúng cách
            OrderDao orderDao = new OrderDao();
            try {
                orderDao.updateOrderStatus(orderId, OrderStatus.COMPLETED);
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

