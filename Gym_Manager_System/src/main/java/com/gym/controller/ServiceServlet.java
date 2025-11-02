package com.gym.controller;

import com.gym.model.shop.Product;
import com.gym.service.shop.ProductService;
import com.gym.service.shop.ProductServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ServiceServlet handles requests for the services main page
 * Loads products from database to display in the product carousel
 */
@WebServlet(name = "ServiceServlet", urlPatterns = { "/services", "/services/*" })
public class ServiceServlet extends HttpServlet {
    
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.productService = new ProductServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Load products for the carousel
            loadProducts(request);
            
            // Check for session messages
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
            
            // Forward đến services_main.jsp
            request.getRequestDispatcher("/views/Service_page/services_main.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("[ServiceServlet] Error processing request: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi khi tải trang dịch vụ");
            request.getRequestDispatcher("/views/Service_page/services_main.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle POST requests the same way as GET
        doGet(request, response);
    }

    /**
     * Load products from database for the product carousel
     * Gets active products ordered by creation date (newest first)
     */
    private void loadProducts(HttpServletRequest request) {
        try {
            // Get active products for the carousel (we'll limit in JSP for carousel display)
            // Get a reasonable number for the carousel (e.g., 12 products to cycle through)
            List<Product> products = productService.search(null, null, 1, 20);
            
            if (products == null) {
                products = new ArrayList<>();
            }
            
            // Debug logging
            System.out.println("[ServiceServlet] Loaded " + products.size() + " products");
            if (!products.isEmpty()) {
                System.out.println("[ServiceServlet] First product: " + products.get(0).getProductName() + " (ID: " + products.get(0).getProductId() + ")");
            }
            
            request.setAttribute("products", products);
        } catch (Exception e) {
            System.err.println("[ServiceServlet] Error loading products: " + e.getMessage());
            e.printStackTrace();
            // Set empty list on error
            request.setAttribute("products", new ArrayList<Product>());
        }
    }
}
