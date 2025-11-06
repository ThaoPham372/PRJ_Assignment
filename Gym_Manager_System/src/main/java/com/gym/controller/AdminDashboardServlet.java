package com.gym.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.gym.model.User;
import com.gym.model.shop.Product;
import com.gym.service.AdminService;
import com.gym.service.UserService;
import com.gym.service.shop.ProductService;
import com.gym.service.shop.ProductServiceImpl;
import com.gym.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * AdminDashboardServlet - Servlet để điều hướng các trang admin
 * Xử lý authentication và authorization cho admin pages
 */
@WebServlet(name = "AdminDashboardServlet", urlPatterns = {
    "/admin/home",
    "/admin/dashboard",
    "/admin/profile",
    "/admin/account-management",
    "/admin/member-management",
    "/admin/service-schedule",
    "/admin/trainer-management",
    "/admin/order-management",
    "/admin/payment-finance",
    "/admin/reports",
    "/admin/products",
    "/admin/products/*",
    "/admin/users",
    "/admin/users/*"
})
public class AdminDashboardServlet extends HttpServlet {

    private transient AdminService adminService;
    private transient ProductService productService;
    private transient UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.adminService = new AdminService();
        // ✅ Tái sử dụng ProductService và UserService có sẵn
        this.productService = new ProductServiceImpl();
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check authentication - must be logged in
        if (!SessionUtil.isLoggedIn(request)) {
            System.out.println("[AdminDashboardServlet] User not logged in, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        // Check authorization - must be ADMIN
        if (!SessionUtil.isAdmin(request)) {
            System.out.println("[AdminDashboardServlet] User is not ADMIN, redirecting to member dashboard");
            response.sendRedirect(request.getContextPath() + "/member/dashboard");
            return;
        }

        // Get current user
        User currentUser = SessionUtil.getCurrentUser(request);
        if (currentUser == null) {
            System.out.println("[AdminDashboardServlet] Current user is null, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        // Get the request URI to determine which page to display
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = requestURI.substring(contextPath.length());

        // Set current user in request
        request.setAttribute("currentUser", currentUser);

        // Handle Product Management URLs
        if (path.startsWith("/admin/products")) {
            handleProductManagement(request, response, path);
            return;
        }
        
        // Handle User Management URLs
        if (path.startsWith("/admin/users")) {
            handleUserManagement(request, response, path);
            return;
        }

        // Determine which JSP to forward to
        String jspPath = determineJspPath(path, request);

        // Set page title attribute
        request.setAttribute("pageTitle", "Admin Dashboard - GymFit");

        // Forward to the appropriate JSP
        request.getRequestDispatcher(jspPath).forward(request, response);
    }

    /**
     * Determine JSP path based on request path and load necessary data
     */
    private String determineJspPath(String path, HttpServletRequest request) {
        String jspPath = "";

        switch (path) {
            case "/admin/home":
                jspPath = "/views/admin/admin_home.jsp";
                break;
            case "/admin/dashboard":
                loadDashboardData(request);
                jspPath = "/views/admin/dashboard.jsp";
                break;
            case "/admin/profile":
                loadProfileData(request);
                jspPath = "/views/admin/profile.jsp";
                break;
            case "/admin/account-management":
                loadAccountManagementData(request);
                jspPath = "/views/admin/account_management.jsp";
                break;
            case "/admin/member-management":
                loadMemberManagementData(request);
                jspPath = "/views/admin/member_management.jsp";
                break;
            case "/admin/service-schedule":
                jspPath = "/views/admin/service_schedule.jsp";
                break;
            case "/admin/trainer-management":
                jspPath = "/views/admin/trainer_management.jsp";
                break;
            case "/admin/order-management":
                jspPath = "/views/admin/order_management.jsp";
                break;
            case "/admin/payment-finance":
                jspPath = "/views/admin/payment_finance.jsp";
                break;
            case "/admin/reports":
                jspPath = "/views/admin/reports.jsp";
                break;
            default:
                jspPath = "/views/admin/admin_home.jsp";
                break;
        }

        return jspPath;
    }

    /**
     * Load data for dashboard page
     */
    private void loadDashboardData(HttpServletRequest request) {
        try {
            // Load admin list for dashboard
            List<com.gym.model.Admin> admins = adminService.getAll();
            request.setAttribute("admins", admins);
            
            // Load user list for statistics
            List<User> users = adminService.getUsers();
            request.setAttribute("totalUsers", users != null ? users.size() : 0);
            
            // You can add more statistics here (revenue, orders, etc.)
        } catch (Exception e) {
            System.err.println("[AdminDashboardServlet] Error loading dashboard data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load data for profile page
     */
    private void loadProfileData(HttpServletRequest request) {
        User currentUser = SessionUtil.getCurrentUser(request);
        if (currentUser != null) {
            try {
                // Load admin profile if exists
                com.gym.model.Admin admin = adminService.getAdminById(currentUser.getUserId());
                if (admin != null) {
                    request.setAttribute("admin", admin);
                }
            } catch (Exception e) {
                System.err.println("[AdminDashboardServlet] Error loading profile data: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Load data for account management page
     */
    private void loadAccountManagementData(HttpServletRequest request) {
        try {
            List<User> users = adminService.getUsers();
            request.setAttribute("users", users);
        } catch (Exception e) {
            System.err.println("[AdminDashboardServlet] Error loading account management data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load data for member management page
     */
    private void loadMemberManagementData(HttpServletRequest request) {
        try {
            List<User> users = adminService.getUsers();
            request.setAttribute("users", users);
            request.setAttribute("totalMembers", users != null ? users.size() : 0);
        } catch (Exception e) {
            System.err.println("[AdminDashboardServlet] Error loading member management data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check authentication and authorization for POST requests too
        if (!SessionUtil.isLoggedIn(request) || !SessionUtil.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        // Get the request URI
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = requestURI.substring(contextPath.length());

        // Handle Product Management POST
        if (path.startsWith("/admin/products")) {
            handleProductPost(request, response);
            return;
        }
        
        // Handle User Management POST
        if (path.startsWith("/admin/users")) {
            handleUserPost(request, response);
            return;
        }

        // Handle POST requests (for forms, updates, etc.)
        // For now, forward to doGet
        doGet(request, response);
    }

    // ==================== PRODUCT MANAGEMENT ====================
    
    private void handleProductManagement(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException {
        String action = path.substring("/admin/products".length());
        
        if (action.isEmpty() || action.equals("/")) {
            // List products with search and pagination
            String keyword = request.getParameter("keyword");
            String pageStr = request.getParameter("page");
            
            int page = 1;
            int pageSize = 20; // 20 products per page
            
            try {
                if (pageStr != null && !pageStr.isEmpty()) {
                    page = Integer.parseInt(pageStr);
                    if (page < 1) page = 1;
                }
            } catch (NumberFormatException e) {
                page = 1;
            }
            
            // Search products
            List<Product> products = productService.searchProductsAdmin(keyword, page, pageSize);
            int totalProducts = productService.countProductsAdmin(keyword);
            int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
            
            request.setAttribute("products", products);
            request.setAttribute("keyword", keyword != null ? keyword : "");
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalProducts", totalProducts);
            
            request.getRequestDispatcher("/views/admin/product-management.jsp").forward(request, response);
        } else if (action.equals("/add")) {
            // Show add form
            request.setAttribute("mode", "add");
            request.getRequestDispatcher("/views/admin/product-form.jsp").forward(request, response);
        } else if (action.startsWith("/edit")) {
            // Show edit form
            Long productId = Long.parseLong(request.getParameter("id"));
            Product product = productService.getProductById(productId);
            if (product == null) {
                request.getSession().setAttribute("error", "Không tìm thấy sản phẩm");
                response.sendRedirect(request.getContextPath() + "/admin/products");
                return;
            }
            request.setAttribute("mode", "edit");
            request.setAttribute("product", product);
            request.getRequestDispatcher("/views/admin/product-form.jsp").forward(request, response);
        }
    }
    
    private void handleProductPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            if ("create".equals(action)) {
                String productName = request.getParameter("productName");
                String productType = request.getParameter("productType");
                BigDecimal price = new BigDecimal(request.getParameter("price"));
                BigDecimal costPrice = request.getParameter("costPrice") != null && !request.getParameter("costPrice").isEmpty()
                        ? new BigDecimal(request.getParameter("costPrice")) : BigDecimal.ZERO;
                String description = request.getParameter("description");
                Integer stockQuantity = Integer.parseInt(request.getParameter("stockQuantity"));
                String unit = request.getParameter("unit");
                boolean active = request.getParameter("active") != null;
                
                boolean success = productService.createProduct(productName, productType, price, costPrice,
                        description, stockQuantity, unit, active);
                
                if (success) {
                    request.getSession().setAttribute("success", "Thêm sản phẩm thành công!");
                } else {
                    request.getSession().setAttribute("error", "Có lỗi khi thêm sản phẩm");
                }
            } else if ("update".equals(action)) {
                Long productId = Long.parseLong(request.getParameter("productId"));
                String productName = request.getParameter("productName");
                String productType = request.getParameter("productType");
                BigDecimal price = new BigDecimal(request.getParameter("price"));
                BigDecimal costPrice = request.getParameter("costPrice") != null && !request.getParameter("costPrice").isEmpty()
                        ? new BigDecimal(request.getParameter("costPrice")) : BigDecimal.ZERO;
                String description = request.getParameter("description");
                Integer stockQuantity = Integer.parseInt(request.getParameter("stockQuantity"));
                String unit = request.getParameter("unit");
                boolean active = request.getParameter("active") != null;
                
                boolean success = productService.updateProduct(productId, productName, productType, price,
                        costPrice, description, stockQuantity, unit, active);
                
                if (success) {
                    request.getSession().setAttribute("success", "Cập nhật sản phẩm thành công!");
                } else {
                    request.getSession().setAttribute("error", "Có lỗi khi cập nhật sản phẩm");
                }
            } else if ("delete".equals(action)) {
                Long productId = Long.parseLong(request.getParameter("productId"));
                boolean success = productService.deleteProduct(productId);
                
                if (success) {
                    request.getSession().setAttribute("success", "Xóa sản phẩm thành công!");
                } else {
                    request.getSession().setAttribute("error", "Có lỗi khi xóa sản phẩm");
                }
            }
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/products");
    }
    
    // ==================== USER MANAGEMENT ====================
    
    private void handleUserManagement(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException {
        String action = path.substring("/admin/users".length());
        
        if (action.isEmpty() || action.equals("/")) {
            // List users with search and pagination
            String keyword = request.getParameter("keyword");
            String pageStr = request.getParameter("page");
            
            int page = 1;
            int pageSize = 20; // 20 users per page
            
            try {
                if (pageStr != null && !pageStr.isEmpty()) {
                    page = Integer.parseInt(pageStr);
                    if (page < 1) page = 1;
                }
            } catch (NumberFormatException e) {
                page = 1;
            }
            
            // Search users
            List<User> users = userService.searchUsers(keyword, page, pageSize);
            int totalUsers = userService.countUsers(keyword);
            int totalPages = (int) Math.ceil((double) totalUsers / pageSize);
            
            request.setAttribute("users", users);
            request.setAttribute("keyword", keyword != null ? keyword : "");
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalUsers", totalUsers);
            
            request.getRequestDispatcher("/views/admin/user-management.jsp").forward(request, response);
        } else if (action.equals("/add")) {
            // Show add form
            request.setAttribute("mode", "add");
            request.getRequestDispatcher("/views/admin/user-form.jsp").forward(request, response);
        } else if (action.startsWith("/edit")) {
            // Show edit form
            Integer userId = Integer.parseInt(request.getParameter("id"));
            User user = userService.getUserById((long) userId);
            if (user == null) {
                request.getSession().setAttribute("error", "Không tìm thấy user");
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }
            request.setAttribute("mode", "edit");
            request.setAttribute("user", user);
            request.getRequestDispatcher("/views/admin/user-form.jsp").forward(request, response);
        }
    }
    
    private void handleUserPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            if ("create".equals(action)) {
                String username = request.getParameter("username");
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                String password = request.getParameter("password");
                String role = request.getParameter("role");
                String status = request.getParameter("status");
                
                boolean success = userService.createUser(username, name, email, phone, password, role, status);
                
                if (success) {
                    request.getSession().setAttribute("success", "Thêm user thành công!" + 
                        ("USER".equals(role) ? " (Student record tự động tạo)" : ""));
                } else {
                    request.getSession().setAttribute("error", "Có lỗi khi thêm user (username hoặc email đã tồn tại)");
                }
            } else if ("update".equals(action)) {
                Integer userId = Integer.parseInt(request.getParameter("userId"));
                String username = request.getParameter("username");
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                String role = request.getParameter("role");
                String status = request.getParameter("status");
                String newPassword = request.getParameter("newPassword");
                
                boolean success = userService.updateUserAdmin(userId, username, name, email, phone, role, status, newPassword);
                
                if (success) {
                    request.getSession().setAttribute("success", "Cập nhật user thành công!");
                } else {
                    request.getSession().setAttribute("error", "Có lỗi khi cập nhật user");
                }
            } else if ("delete".equals(action)) {
                Integer userId = Integer.parseInt(request.getParameter("userId"));
                boolean success = userService.deleteUser(userId);
                
                if (success) {
                    request.getSession().setAttribute("success", "Xóa user thành công!");
                } else {
                    request.getSession().setAttribute("error", "Có lỗi khi xóa user");
                }
            }
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }

    @Override
    public String getServletInfo() {
        return "Admin Dashboard Servlet for GymFit Management System";
    }
}
