package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ProductType;
import service.ProductService;
import java.io.IOException;
import java.util.List;

/**
 * ProductServlet - Controller cho Product
 * Tuân thủ mô hình MVC và nguyên tắc OOP
 */
@WebServlet(name = "ProductServlet", urlPatterns = {"/products"})
public class ProductServlet extends HttpServlet {
    
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
            // Lấy parameters
            String keyword = getParameter(request, "q");
            String typeStr = getParameter(request, "type");
            String pageStr = getParameter(request, "page");
            
            // Parse ProductType
            ProductType type = null;
            if (typeStr != null && !typeStr.trim().isEmpty()) {
                type = ProductType.fromCode(typeStr.trim());
            }
            
            // Parse page
            int page = parsePage(pageStr);
            int pageSize = 12;
            
            // Lấy dữ liệu từ service
            List<model.Product> products = productService.search(keyword, type, page, pageSize);
            int totalCount = productService.count(keyword, type);
            int totalPages = (int) Math.ceil((double) totalCount / pageSize);
            
            // Set attributes cho JSP
            request.setAttribute("products", products);
            request.setAttribute("page", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("searchKeyword", keyword);
            request.setAttribute("selectedType", typeStr);
            
            // Lấy messages từ session
            handleSessionMessages(request);
            
            // Forward đến JSP
            request.getRequestDispatcher("/views/Service_page/product.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("[ProductServlet] Error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi khi xử lý yêu cầu");
            request.getRequestDispatcher("/views/Service_page/product.jsp").forward(request, response);
        }
    }
    
    /**
     * Lấy parameter và trim, trả về null nếu rỗng
     */
    private String getParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return (value != null && !value.trim().isEmpty()) ? value.trim() : null;
    }
    
    /**
     * Parse page number từ string
     */
    private int parsePage(String pageStr) {
        if (pageStr == null || pageStr.trim().isEmpty()) {
            return 1;
        }
        try {
            int page = Integer.parseInt(pageStr);
            return page < 1 ? 1 : page;
        } catch (NumberFormatException e) {
            return 1;
        }
    }
    
    /**
     * Xử lý messages từ session
     */
    private void handleSessionMessages(HttpServletRequest request) {
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
    }
}
