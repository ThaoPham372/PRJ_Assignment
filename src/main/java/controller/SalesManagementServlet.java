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
import service.OrderService;
import service.ProductService;

/*
    Note: 
 */
@WebServlet(name = "SalesManagementServlet", urlPatterns = "/admin/sales-management")
public class SalesManagementServlet extends HttpServlet {

    ProductService productService = new ProductService();
    OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null)
            action = "";

        List<Product> products = productService.getAll();

        int productCount = products.size(); // Số lượng sản phẩm
        int monthlyOrderCount = getMonthlyOrderCount(products); // Số đơn hàng tháng này
        double monthlyRevenue = getMonthlyRevenue(products); // Doanh thu tháng này
        int lowStockCount = getLowStockCount(products);

        switch (action) {
            case "deleteProduct" -> {
                int id = Integer.parseInt(req.getParameter("productId"));
                handleDeleteProduct(products, id);
            }
        }

        products = getProductsActive(products);

        req.setAttribute("productCount", productCount);
        req.setAttribute("monthlyOrderCount", monthlyOrderCount);
        req.setAttribute("monthlyRevenue", monthlyRevenue);
        req.setAttribute("lowStockCount", lowStockCount);
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

}
