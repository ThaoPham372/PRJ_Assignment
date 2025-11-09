
package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import model.Product;
import service.ProductService;

/*
    Note: 
 */
@WebServlet(name = "SalesManagementServlet", urlPatterns = "/admin/sales-management")
public class SalesManagementServlet extends HttpServlet{
    
    ProductService productService = new ProductService();
//    OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Product> products = productService.getAll();
        System.out.println("\nsize:" + products.size());
        req.setAttribute("products", products);
        req.getRequestDispatcher("/views/admin/sales_management.jsp").forward(req, resp);
    }
    
}
