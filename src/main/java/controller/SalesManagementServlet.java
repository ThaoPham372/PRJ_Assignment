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
import service.OrderItemService;
import service.OrderService;
import service.ProductService;

/*
    Note: 
 */
@WebServlet(name = "SalesManagementServlet", urlPatterns = "/admin/sales-management")
public class SalesManagementServlet extends HttpServlet {

    private final ProductService productService;
    private final OrderItemService orderItemService;
    private final OrderService orderService;

    public SalesManagementServlet() {
        this.productService = new ProductService();
        this.orderItemService = new OrderItemService();
        this.orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null)
            action = "";

        List<Product> products = getProducts();
        List<OrderItem> orderItems = orderItemService.getAll();
        List<Order> orders = orderService.getAll();

        // Load metrics from real data
        loadDashboardMetrics(req, products, orders);

        switch (action) {
            case "deleteProduct" -> {
                int productId = Integer.parseInt(req.getParameter("productId"));
                handleDeleteProduct(products, productId);
            }
            case "confirmOrder" -> {
                int orderId = Integer.parseInt(req.getParameter("orderId"));
                boolean success = handleConfirmOrder(orderId);
                // Redirect to keep on orders tab with success message
                if (success) {
                    resp.sendRedirect(req.getContextPath() + "/admin/sales-management?tab=orders&success=Xác nhận đơn hàng thành công!");
                } else {
                    resp.sendRedirect(req.getContextPath() + "/admin/sales-management?tab=orders&error=Không tìm thấy đơn hàng!");
                }
                return;
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

        req.setAttribute("orderItems", orderItems);
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

    private void handleDeleteProduct(List<Product> products, int id) {
        for (Product p : products) {
            if (p.getId() == id) {
                p.setActive(false);
                productService.update(p);
                break;
            }
        }
    }

    private List<Product> getProductsActive(List<Product> products) {
        products.removeIf(p -> !p.getActive());
        return products;
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

    private List<Product> getProducts() {
        List<Product> products = productService.getAll();
        products = getProductsActive(products);
        return products;
    }

    /**
     * Xác nhận đơn hàng - cập nhật trạng thái thành COMPLETED
     * @return true nếu thành công, false nếu không tìm thấy đơn hàng
     */
    private boolean handleConfirmOrder(int orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            order.setOrderStatus(OrderStatus.COMPLETED);
            orderService.update(order);
            return true;
        }
        return false;
    }
}

