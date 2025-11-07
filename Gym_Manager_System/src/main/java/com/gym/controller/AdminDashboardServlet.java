package com.gym.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.gym.model.User;
import com.gym.model.shop.Product;
import com.gym.service.AdminService;
import com.gym.service.PasswordService;
import com.gym.service.PaymentService;
import com.gym.service.PaymentServiceImpl;
import com.gym.service.UserService;
import com.gym.service.shop.ProductService;
import com.gym.service.shop.ProductServiceImpl;
import com.gym.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * AdminDashboardServlet - Servlet để điều hướng các trang admin
 * Xử lý authentication và authorization cho admin pages
 */
@MultipartConfig(
    maxFileSize = 10485760,      // 10 MB
    maxRequestSize = 10485760,   // 10 MB
    fileSizeThreshold = 524288   // 512 KB
)
@WebServlet(name = "AdminDashboardServlet", urlPatterns = {
    "/admin/home",
    "/admin/dashboard",
    "/admin/profile",
    "/admin/send-password-reset",
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
    private transient com.gym.service.membership.MembershipService membershipService;
    private transient PaymentService paymentService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.adminService = new AdminService();
        // ✅ Tái sử dụng ProductService và UserService có sẵn
        this.productService = new ProductServiceImpl();
        this.userService = new UserService();
        this.membershipService = new com.gym.service.membership.MembershipServiceImpl();
        this.paymentService = new PaymentServiceImpl();
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

        // Handle send password reset (redirect to forgot-password with email)
        if (path.equals("/admin/send-password-reset")) {
            String email = request.getParameter("email");
            if (email != null && !email.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/auth/forgot-password?email=" + java.net.URLEncoder.encode(email, "UTF-8"));
            } else {
                response.sendRedirect(request.getContextPath() + "/auth/forgot-password");
            }
            return;
        }

        // Handle Product Management URLs
        if (path.startsWith("/admin/products")) {
            handleProductManagement(request, response, path);
            return;
        }
        
        // Handle User Management URLs (also handles /admin/account-management)
        if (path.startsWith("/admin/users") || path.equals("/admin/account-management")) {
            if (path.equals("/admin/account-management")) {
                // Redirect to /admin/users for consistency
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }
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
                // This case is handled in doGet before determineJspPath
                // Redirect to /admin/users for consistency
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
            
            // Load active memberships count (status = ACTIVE and payment = PAID)
            long totalActiveMembers = membershipService.countActiveMemberships();
            request.setAttribute("totalActiveMembers", totalActiveMembers);
            
            // Load revenue for current month
            java.math.BigDecimal revenueThisMonth = paymentService.getRevenueThisMonth();
            request.setAttribute("revenueThisMonth", revenueThisMonth);
            
            // Format revenue for display
            String revenueDisplay = "0";
            if (revenueThisMonth != null && revenueThisMonth.compareTo(java.math.BigDecimal.ZERO) > 0) {
                if (revenueThisMonth.compareTo(java.math.BigDecimal.valueOf(1000000)) >= 0) {
                    // >= 1,000,000: display as "X.XM"
                    double millions = revenueThisMonth.divide(java.math.BigDecimal.valueOf(1000000), 1, java.math.RoundingMode.HALF_UP).doubleValue();
                    revenueDisplay = String.format("%.1fM", millions);
                } else if (revenueThisMonth.compareTo(java.math.BigDecimal.valueOf(1000)) >= 0) {
                    // >= 1,000: display as "X.XK"
                    double thousands = revenueThisMonth.divide(java.math.BigDecimal.valueOf(1000), 1, java.math.RoundingMode.HALF_UP).doubleValue();
                    revenueDisplay = String.format("%.1fK", thousands);
                } else {
                    // < 1,000: display as integer
                    revenueDisplay = revenueThisMonth.setScale(0, java.math.RoundingMode.HALF_UP).toString();
                }
            }
            request.setAttribute("revenueDisplay", revenueDisplay);
        } catch (Exception e) {
            System.err.println("[AdminDashboardServlet] Error loading dashboard data: " + e.getMessage());
            e.printStackTrace();
            // Set default values on error
            request.setAttribute("totalActiveMembers", 0L);
            request.setAttribute("revenueThisMonth", java.math.BigDecimal.ZERO);
            request.setAttribute("revenueDisplay", "0");
        }
    }

    /**
     * Load data for profile page
     * Uses UserService.getProfileData() to get profile data from UserDAO (same as member)
     */
    private void loadProfileData(HttpServletRequest request) {
        User currentUser = SessionUtil.getCurrentUser(request);
        if (currentUser != null) {
            try {
                // Use UserService.getProfileData() to get profile data (same as member)
                // This gets data from User table: name, email, phone, address, avatarUrl, dob, gender, etc.
                java.util.Map<String, Object> profileData = userService.getProfileData(currentUser.getUserId().longValue());
                if (profileData != null) {
                    request.setAttribute("profileData", profileData);
                    System.out.println("[AdminDashboardServlet] Loaded profile data for admin userId: " + currentUser.getUserId());
                } else {
                    System.err.println("[AdminDashboardServlet] Failed to load profile data");
                }
                
                // Get success/error messages from session and remove them after displaying
                jakarta.servlet.http.HttpSession session = request.getSession();
                String profileUpdateSuccess = (String) session.getAttribute("profileUpdateSuccess");
                String profileUpdateError = (String) session.getAttribute("profileUpdateError");
                String passwordChangeSuccess = (String) session.getAttribute("passwordChangeSuccess");
                String passwordChangeError = (String) session.getAttribute("passwordChangeError");
                
                if (profileUpdateSuccess != null) {
                    request.setAttribute("profileUpdateSuccess", profileUpdateSuccess);
                    session.removeAttribute("profileUpdateSuccess");
                }
                if (profileUpdateError != null) {
                    request.setAttribute("profileUpdateError", profileUpdateError);
                    session.removeAttribute("profileUpdateError");
                }
                if (passwordChangeSuccess != null) {
                    request.setAttribute("passwordChangeSuccess", passwordChangeSuccess);
                    session.removeAttribute("passwordChangeSuccess");
                }
                if (passwordChangeError != null) {
                    request.setAttribute("passwordChangeError", passwordChangeError);
                    session.removeAttribute("passwordChangeError");
                }
            } catch (Exception e) {
                System.err.println("[AdminDashboardServlet] Error loading profile data: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    /**
     * Load data for member management page
     */
    private void loadMemberManagementData(HttpServletRequest request) {
        try {
            // Load all memberships with status ACTIVE, EXPIRED, or SUSPENDED (regardless of payment)
            List<com.gym.model.membership.Membership> memberships = membershipService.getAllActiveAndSuspendedMemberships();
            request.setAttribute("memberships", memberships);
            
            // Load statistics
            // Đang hoạt động = ACTIVE
            long totalActive = membershipService.countActiveMembershipsForManagement();
            // Tạm ngưng = EXPIRED + SUSPENDED
            long totalSuspended = membershipService.countSuspendedMemberships();
            // Tổng số = ACTIVE + EXPIRED + SUSPENDED
            long totalMembers = membershipService.countAllMembershipsForManagement();
            
            request.setAttribute("totalActiveMembers", totalActive);
            request.setAttribute("totalSuspendedMembers", totalSuspended);
            request.setAttribute("totalMembers", totalMembers);
            
            System.out.println("[AdminDashboardServlet] Loaded " + memberships.size() + " memberships. Active: " + totalActive + ", Suspended/Expired: " + totalSuspended + ", Total: " + totalMembers);
        } catch (Exception e) {
            System.err.println("[AdminDashboardServlet] Error loading member management data: " + e.getMessage());
            e.printStackTrace();
            // Set default values on error
            request.setAttribute("memberships", new java.util.ArrayList<>());
            request.setAttribute("totalActiveMembers", 0L);
            request.setAttribute("totalSuspendedMembers", 0L);
            request.setAttribute("totalMembers", 0L);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("[AdminDashboardServlet] doPost - Request received");
        
        // Check authentication and authorization for POST requests too
        if (!SessionUtil.isLoggedIn(request) || !SessionUtil.isAdmin(request)) {
            System.out.println("[AdminDashboardServlet] doPost - Not logged in or not admin, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        // Get the request URI
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = requestURI.substring(contextPath.length());
        
        System.out.println("[AdminDashboardServlet] doPost - Path: " + path);
        System.out.println("[AdminDashboardServlet] doPost - Content-Type: " + request.getContentType());
        System.out.println("[AdminDashboardServlet] doPost - Method: " + request.getMethod());

        // Handle Product Management POST
        if (path.startsWith("/admin/products")) {
            System.out.println("[AdminDashboardServlet] doPost - Handling product POST");
            handleProductPost(request, response);
            return;
        }
        
        // Handle User Management POST
        if (path.startsWith("/admin/users")) {
            System.out.println("[AdminDashboardServlet] doPost - Handling user POST");
            handleUserPost(request, response);
            return;
        }

        // Handle profile POST requests
        if (path.equals("/admin/profile")) {
            System.out.println("[AdminDashboardServlet] doPost - Handling profile POST");
            handleProfilePost(request, response);
            return;
        }

        System.out.println("[AdminDashboardServlet] doPost - No handler found for path: " + path + ", forwarding to doGet");
        // Handle POST requests (for forms, updates, etc.)
        // For now, forward to doGet
        doGet(request, response);
    }
    
    // ==================== PROFILE MANAGEMENT ====================
    
    /**
     * Handle profile POST requests (update profile, change password)
     */
    private void handleProfilePost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("[AdminDashboardServlet] handleProfilePost - Starting");
        
        String action = request.getParameter("action");
        System.out.println("[AdminDashboardServlet] handleProfilePost - Action: " + action);
        
        User currentUser = SessionUtil.getCurrentUser(request);
        System.out.println("[AdminDashboardServlet] handleProfilePost - Current user: " + (currentUser != null ? currentUser.getUsername() : "NULL"));
        
        if (currentUser == null) {
            System.err.println("[AdminDashboardServlet] handleProfilePost - Current user is null, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        try {
            if ("updateAdmin".equals(action)) {
                // Check if it's multipart request
                String contentType = request.getContentType();
                boolean isMultipart = contentType != null && contentType.toLowerCase().startsWith("multipart/form-data");
                
                System.out.println("[AdminDashboardServlet] handleProfilePost - updateAdmin, isMultipart: " + isMultipart);
                
                // Update admin profile
                boolean success = false;
                try {
                    if (isMultipart) {
                        success = userService.updateProfileFromMultipartRequest(currentUser, request);
                    } else {
                        success = userService.updateProfileFromRequest(currentUser, request);
                    }
                    
                    System.out.println("[AdminDashboardServlet] handleProfilePost - Update result: " + success);
                    
                    if (success) {
                        // Reload user from database to get updated data
                        User updatedUser = userService.getUserById(currentUser.getUserId().longValue());
                        if (updatedUser != null) {
                            // Update session with new user data
                            jakarta.servlet.http.HttpSession session = request.getSession();
                            session.setAttribute("user", updatedUser);
                            System.out.println("[AdminDashboardServlet] handleProfilePost - Session updated with new user data");
                        }
                        request.getSession().setAttribute("profileUpdateSuccess", "Cập nhật thông tin thành công!");
                    } else {
                        request.getSession().setAttribute("profileUpdateError", "Không thể cập nhật thông tin. Vui lòng thử lại.");
                    }
                } catch (Exception e) {
                    System.err.println("[AdminDashboardServlet] handleProfilePost - Error updating profile: " + e.getMessage());
                    e.printStackTrace();
                    request.getSession().setAttribute("profileUpdateError", "Lỗi khi cập nhật thông tin: " + e.getMessage());
                }
                
                response.sendRedirect(request.getContextPath() + "/admin/profile");
                return;
                
            } else if ("updateAdminPassword".equals(action)) {
                // Change password
                String currentPassword = request.getParameter("currentPassword");
                String newPassword = request.getParameter("newPassword");
                String confirmPassword = request.getParameter("confirmPassword");
                
                // Validate
                if (currentPassword == null || currentPassword.trim().isEmpty()) {
                    request.getSession().setAttribute("passwordChangeError", "Vui lòng nhập mật khẩu hiện tại");
                    response.sendRedirect(request.getContextPath() + "/admin/profile");
                    return;
                }
                
                if (newPassword == null || newPassword.trim().isEmpty()) {
                    request.getSession().setAttribute("passwordChangeError", "Vui lòng nhập mật khẩu mới");
                    response.sendRedirect(request.getContextPath() + "/admin/profile");
                    return;
                }
                
                if (!newPassword.equals(confirmPassword)) {
                    request.getSession().setAttribute("passwordChangeError", "Mật khẩu mới và xác nhận không khớp");
                    response.sendRedirect(request.getContextPath() + "/admin/profile");
                    return;
                }
                
                // Verify current password
                PasswordService passwordService = new PasswordService();
                if (!passwordService.verifyPassword(currentPassword, currentUser.getPasswordHash())) {
                    request.getSession().setAttribute("passwordChangeError", "Mật khẩu hiện tại không đúng");
                    response.sendRedirect(request.getContextPath() + "/admin/profile");
                    return;
                }
                
                // Validate new password strength
                String passwordError = passwordService.getPasswordValidationMessage(newPassword);
                if (passwordError != null) {
                    request.getSession().setAttribute("passwordChangeError", passwordError);
                    response.sendRedirect(request.getContextPath() + "/admin/profile");
                    return;
                }
                
                // Update password
                String passwordHash = passwordService.hashPassword(newPassword);
                currentUser.setPassword(passwordHash);
                currentUser.setLastUpdate(java.time.LocalDateTime.now());
                
                boolean updated = userService.updateUser(currentUser);
                
                if (updated) {
                    request.getSession().setAttribute("passwordChangeSuccess", "Đổi mật khẩu thành công!");
                } else {
                    request.getSession().setAttribute("passwordChangeError", "Không thể đổi mật khẩu. Vui lòng thử lại.");
                }
                
                response.sendRedirect(request.getContextPath() + "/admin/profile");
            }
        } catch (Exception e) {
            System.err.println("[AdminDashboardServlet] Error handling profile POST: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("profileUpdateError", "Lỗi: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/profile");
            return;
        }
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
            // List users with search, role filter and pagination
            String keyword = request.getParameter("keyword");
            String role = request.getParameter("role");
            String pageStr = request.getParameter("page");
            
            // Debug logging
            System.out.println("[AdminDashboardServlet] Received parameters - keyword: " + keyword + ", role: " + role + ", page: " + pageStr);
            
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
            
            // Search users with role filter
            List<User> users = userService.searchUsers(keyword, role, page, pageSize);
            int totalUsers = userService.countUsers(keyword, role);
            int totalPages = (int) Math.ceil((double) totalUsers / pageSize);
            
            System.out.println("[AdminDashboardServlet] Found " + users.size() + " users, total: " + totalUsers);
            
            request.setAttribute("users", users);
            request.setAttribute("keyword", keyword != null ? keyword : "");
            request.setAttribute("role", role != null && !role.isEmpty() ? role : "");
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalUsers", totalUsers);
            
            request.getRequestDispatcher("/views/admin/account_management.jsp").forward(request, response);
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
                    String roleMessage = "";
                    if ("MEMBER".equalsIgnoreCase(role) || "USER".equalsIgnoreCase(role) || "STUDENT".equalsIgnoreCase(role)) {
                        roleMessage = " (Member record tự động tạo)";
                    } else if ("ADMIN".equalsIgnoreCase(role)) {
                        roleMessage = " (Admin record tự động tạo)";
                    } else if ("TRAINER".equalsIgnoreCase(role) || "PT".equalsIgnoreCase(role)) {
                        roleMessage = " (Trainer record tự động tạo)";
                    }
                    request.getSession().setAttribute("success", "Thêm user thành công!" + roleMessage);
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
                boolean success = userService.deleteUser(userId.longValue());
                
                if (success) {
                    request.getSession().setAttribute("success", "Xóa user thành công! (Soft delete - status=INACTIVE)");
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
