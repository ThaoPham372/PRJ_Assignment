package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import model.Product;
import model.ProductType;
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

    ProductService productService = new ProductService();
    OrderItemService orderItemService = new OrderItemService();

    // TODO: Tách products và orderItems
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null)
            action = "";

        List<Product> products = getProducts();
        List<OrderItem> orderItems = orderItemService.getAll();

        loadDashboardMetrics(req, products);

        switch (action) {
            case "deleteProduct" -> {
                int productId = Integer.parseInt(req.getParameter("productId"));
                handleDeleteProduct(products, productId);
            }
            case "confirmOrder" -> {
                int orderId = Integer.parseInt(req.getParameter("orderId"));
                System.out.println("\n\n\n" + orderId);
                handleConfirmOrder(orderItems, orderId);
            }
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

        resp.sendRedirect(req.getContextPath() + "/admin/sales-management");
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

    private int getMonthlyOrderCount(List<Product> products) {
        return 666;
    }

    private double getMonthlyRevenue(List<Product> products) {
        return 666d;
    }

    private int getLowStockCount(List<Product> products) {
        return 666;
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

    private void loadDashboardMetrics(HttpServletRequest req, List<Product> products) {
        int productCount = products.size(); // Số lượng sản phẩm
        int monthlyOrderCount = getMonthlyOrderCount(products); // Số đơn hàng tháng này
        double monthlyRevenue = getMonthlyRevenue(products); // Doanh thu tháng này
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

    private void handleConfirmOrder(List<OrderItem> orderItems, int orderId) {
        for (OrderItem o : orderItems) {
            if (o.getOrder().getOrderId() == orderId) {
                o.getOrder().setOrderStatus(OrderStatus.COMPLETED);
                System.out.println("OD status : " + o.getOrder().getOrderStatus());
                new OrderService().update(o.getOrder());
                break;
            }
        }
    }
}
