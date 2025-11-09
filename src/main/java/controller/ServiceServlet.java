package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.ProductService;
import java.io.IOException;
import java.util.List;

/**
 * ServiceServlet - Controller cho Services Main Page
 * Tuân thủ mô hình MVC và nguyên tắc OOP
 */
@WebServlet(name = "ServiceServlet", urlPatterns = {"/services", "/services/*"})
public class ServiceServlet extends HttpServlet {
    
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        super.init();
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Load products cho carousel
            loadProducts(request);
            
            // Xử lý messages từ session
            handleSessionMessages(request);
            
            // Forward đến services_main.jsp
            request.getRequestDispatcher("/views/Service_page/services_main.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("[ServiceServlet] Error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi khi tải trang dịch vụ");
            request.getRequestDispatcher("/views/Service_page/services_main.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Xử lý POST giống GET
        doGet(request, response);
    }

    /**
     * Load products từ database cho product carousel
     * Lấy active products (tối đa 20 để carousel có đủ để xoay vòng)
     */
    private void loadProducts(HttpServletRequest request) {
        try {
            // Lấy active products (không filter, không pagination - lấy tất cả active)
            List<model.Product> products = productService.getActiveProducts();
            
            // Giới hạn số lượng để tối ưu (carousel chỉ cần ~12-20 products)
            if (products.size() > 20) {
                products = products.subList(0, 20);
            }
            
            request.setAttribute("products", products);
        } catch (Exception e) {
            System.err.println("[ServiceServlet] Error loading products: " + e.getMessage());
            e.printStackTrace();
            // Set empty list nếu có lỗi
            request.setAttribute("products", List.of());
        }
    }

    /**
     * Xử lý messages từ session
     */
    private void handleSessionMessages(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String message = (String) session.getAttribute("message");
            String error = (String) session.getAttribute("error");
            
            if (message != null) {
                request.setAttribute("message", message);
                session.removeAttribute("message");
            }
            
            if (error != null) {
                request.setAttribute("error", error);
                session.removeAttribute("error");
            }
        }
    }
}
