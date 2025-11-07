package com.gym.controller;

import com.gym.model.shop.ProductType;
import com.gym.service.shop.ProductService;
import com.gym.service.shop.ProductServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductServlet", urlPatterns = {"/products/*"})
public class ProductServlet extends HttpServlet {
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
            // Get query parameters
            String q = request.getParameter("q");
            String typeStr = request.getParameter("type");
            String pageStr = request.getParameter("page");
            
            ProductType type = null;
            if (typeStr != null && !typeStr.trim().isEmpty()) {
                type = ProductType.fromCode(typeStr.trim());
            }
            
            int page = 1;
            int pageSize = 12;
            if (pageStr != null && !pageStr.trim().isEmpty()) {
                try {
                    page = Integer.parseInt(pageStr);
                    if (page < 1) page = 1;
                } catch (NumberFormatException e) {
                    // Use default page = 1
                }
            }
            
            // Call service
            List<com.gym.model.shop.Product> products = productService.search(q, type, page, pageSize);
            int totalCount = productService.count(q, type);
            int totalPages = (int) Math.ceil((double) totalCount / pageSize);
            
            // Set attributes
            request.setAttribute("products", products);
            request.setAttribute("page", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("searchKeyword", q);
            request.setAttribute("selectedType", typeStr);
            
            // Check for session messages
            HttpSession session = request.getSession();
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
            
            request.getRequestDispatcher("/views/Service_page/product.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("[ProductServlet] Error processing request: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi khi xử lý yêu cầu");
            request.getRequestDispatcher("/views/Service_page/product.jsp").forward(request, response);
        }
    }
}