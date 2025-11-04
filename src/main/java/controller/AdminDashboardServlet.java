package controller;

import com.google.gson.Gson;
import dao.UserDAO;
import model.Admin;
import model.Trainer;
import model.User;
import service.AdminService;
import service.PasswordService;
import service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * AdminDashboardServlet - Servlet để điều hướng các trang admin Đây là servlet
 * giả để test hiển thị dashboard admin
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
    "/admin/reports"
})
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the request URI to determine which page to display
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = requestURI.substring(contextPath.length());

        // Check if user is logged in and has ADMIN role
        HttpSession session = request.getSession(false);
//        if (session == null || session.getAttribute("isLoggedIn") == null) {
//            response.sendRedirect(request.getContextPath() + "/views/login.jsp");
//            return;
//        }

        // Check if user has ADMIN role
//        @SuppressWarnings("unchecked")
//        java.util.List<String> userRoles = (java.util.List<String>) session.getAttribute("userRoles");
//        if (userRoles == null || !userRoles.contains("ADMIN")) {
//            response.sendRedirect(request.getContextPath() + "/home");
//            return;
//        }
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "edit" -> {
                System.out.println("\n\nEDIT CALL");
                int id = Integer.parseInt(request.getParameter("id"));
                UserService userService = new UserService();
                User user = userService.getUserById(id);
                String json = new Gson().toJson(user);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                response.getWriter().write(json);
                return;
            }
        }

        String jspPath;

        switch (path) {
            case "/admin/home" ->
                jspPath = "/views/admin/admin_home.jsp";
            case "/admin/dashboard" ->
                jspPath = "/views/admin/dashboard.jsp";
            case "/admin/profile" ->
                jspPath = "/views/admin/profile.jsp";
            case "/admin/account-management" -> {
                getAccounts(request);
                jspPath = "/views/admin/account_management.jsp";
            }
            case "/admin/member-management" ->
                jspPath = "/views/admin/member_management.jsp";
            case "/admin/service-schedule" ->
                jspPath = "/views/admin/service_schedule.jsp";
            case "/admin/trainer-management" ->
                jspPath = "/views/admin/trainer_management.jsp";
            case "/admin/order-management" ->
                jspPath = "/views/admin/order_management.jsp";
            case "/admin/payment-finance" ->
                jspPath = "/views/admin/payment_finance.jsp";
            case "/admin/reports" ->
                jspPath = "/views/admin/reports.jsp";
            default ->
                jspPath = "/views/admin/admin_home.jsp";
        }

        // Set page title attribute
        request.setAttribute("pageTitle", "Admin Dashboard - GymFit");

        // Forward to the appropriate JSP
        request.getRequestDispatcher(jspPath).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle POST requests (for forms, updates, etc.)
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        switch (action) {
            case "addAdmin" ->
                addAdmin(request, response);
            case "addUser" ->
                addUser(request, response);
            case "addTrainer" ->
                addTrainer(request, response);
            case "addProduct" ->
                addProduct(request, response);
            case "addAccount" ->
                addAccount(request, response);

            case "updateAdmin" ->
                updateAdmin(request, response);
            case "updateAdminPassword" ->
                updateAdminPassword(request, response);
            case "updateUser" ->
                updateUser(request, response);
            case "updateTrainer" ->
                updateTrainer(request, response);
            case "updateProduct" ->
                updateProduct(request, response);
            case "updateAccount" ->
                updateAccount(request, response);

            case "deleteAdmin" ->
                deleteAdmin(request, response);
            case "deleteUser" ->
                deleteUser(request, response);
            case "deleteTrainer" ->
                deleteTrainer(request, response);
            case "deleteProduct" ->
                deleteProduct(request, response);
            case "deleteAccount" ->
                deleteAccount(request, response);
            default ->
                System.out.println("Action is null");
        }
    }

    @Override
    public String getServletInfo() {
        return "Admin Dashboard Servlet for GymFit Management System";
    }

    private void addAdmin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AdminService adminService = new AdminService();
        Admin admin = new Admin();
        this.enterInfoUser(request, admin);
        adminService.add(admin);
        response.sendRedirect(request.getContextPath() + "/admin/account-management");
    }

    private void addAccount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String role = request.getParameter("role");

        if (role.equalsIgnoreCase("admin")) {
            this.addAdmin(request, response);
        } else if (role.equalsIgnoreCase("trainer")) {
            this.addTrainer(request, response);
        } else {
            this.addUser(request, response);
        }
        response.sendRedirect(request.getContextPath() + "/admin/account-management");
    }

    private void addUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = new User();
        this.enterInfoUser(request, user);
        UserDAO userDAO = new UserDAO();
        userDAO.save(user);
        response.sendRedirect(request.getContextPath() + "/admin/account-management");
    }

    private void addTrainer(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void addProduct(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void updateAdmin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AdminService adminService = new AdminService();
        int id = Integer.parseInt(request.getParameter("id"));
        Admin admin = adminService.getAdminById(id);

        System.out.println("\nID: " + id + " - Admin found: " + admin);

        this.enterInfoUser(request, admin);
        adminService.update(admin);
        request.getSession().setAttribute("user", admin);
        request.setAttribute("message", "✅ Update successful!");
        request.getRequestDispatcher("/views/admin/profile.jsp").forward(request, response);
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void updateTrainer(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void deleteAdmin(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        AdminService adminService = new AdminService();
        Admin admin = adminService.getAdminById(id);
        adminService.delete(admin);
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void deleteTrainer(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void updateAdminPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PasswordService passwordService = new PasswordService();
        int id = Integer.parseInt(request.getParameter("id"));
        AdminService adminService = new AdminService();
        Admin admin = adminService.getAdminById(id);

        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        System.out.println("currentPassword: " + currentPassword);
        System.out.println("newPassword: " + newPassword);
        System.out.println("confirmPassword: " + confirmPassword);

        String userPassword = admin.getPassword();
        String hashNewP = passwordService.hashPassword(newPassword);

        boolean isCurrentPasswordCorrect = passwordService.verifyPassword(currentPassword, userPassword);

        if (!newPassword.equals(confirmPassword) || !isCurrentPasswordCorrect) {
            request.setAttribute("message",
                    "new password not equal confirm password or current password is not correctly!");
            request.getRequestDispatcher("/views/admin/profile.jsp").forward(request, response);
        } else {
            admin.setPassword(hashNewP);
            adminService.update(admin);
            System.out.println("Update password successfully!");
        }

        request.getRequestDispatcher("/views/admin/profile.jsp").forward(request, response);
    }

    private void getAccounts(HttpServletRequest request) {
        UserService userService = new UserService();
        List<User> users = userService.getAll();
        request.setAttribute("accounts", users);
    }

    private void deleteAccount(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("\n\n\nDELETE CALL:");
        int id = Integer.parseInt(request.getParameter("id"));
        System.out.println("ID: " + id);
        UserService userService = new UserService();
        User user = userService.getUserById(id);
        System.out.println("User: " + user);
        userService.delete(user);
        System.out.println("USER STATUS: " + user.getStatus());
        response.setStatus(HttpServletResponse.SC_OK);
        System.out.println("\n\n\n");
    }

    private void enterInfoUser(HttpServletRequest request, User obj) {
        PasswordService passwordService = new PasswordService();

        if (request.getParameter("name") != null) {
            obj.setName(request.getParameter("name").trim());
        }

        if (request.getParameter("email") != null) {
            obj.setEmail(request.getParameter("email").trim());
        }

        if (request.getParameter("phone") != null) {
            obj.setPhone(request.getParameter("phone").trim());
        }

        if (request.getParameter("birthday") != null && !request.getParameter("birthday").trim().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date dob = sdf.parse(request.getParameter("birthday").trim());
                obj.setDob(dob);
            } catch (ParseException e) {
                System.out.println("Error: Admin DB Servlet -> setAdmin()");
            }
        }

        if (request.getParameter("address") != null) {
            obj.setAddress(request.getParameter("address").trim());
        }

        if (request.getParameter("role") != null) {
            obj.setRole(request.getParameter("role").trim());
        }

        if (request.getParameter("username") != null) {
            obj.setUsername(request.getParameter("username").trim());
        }

        
        String passwordStr = request.getParameter("password");
        if (passwordStr != null && !passwordStr.isEmpty() && passwordService.isValidPassword(passwordStr)) {
            String passwordHash = passwordService.hashPassword(request.getParameter("password"));
            obj.setPassword(passwordHash);
        }

        if (request.getParameter("status") != null) {
            obj.setStatus(request.getParameter("status").trim());
        }

        if (request.getParameter("emailVerified") != null) {
            // admin.setEmailVerified(request.getParameter("emailVerified").equals("true"));
        }

        if (request.getParameter("failedLoginAttempts") != null) {
            try {
                obj.setFailedLoginAttempts(Integer.valueOf(request.getParameter("failedLoginAttempts")));
            } catch (NumberFormatException e) {
                obj.setFailedLoginAttempts(0);
            }
        }

        if (request.getParameter("createdDate") != null) {
            obj.setCreatedDate(parseDateTime(request.getParameter("createdDate")));
        }
        if (request.getParameter("lastLogin") != null) {
            obj.setLastLogin(parseDateTime(request.getParameter("lastLogin")));
        }
        if (request.getParameter("lockedUntil") != null) {
            obj.setLockedUntil(parseDateTime(request.getParameter("lockedUntil")));
        }
    }

    Date parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf;
            if (dateTimeStr.contains(":")) {
                // Nếu có 2 dấu ':' thì có giây
                sdf = dateTimeStr.chars().filter(ch -> ch == ':').count() == 2
                        ? new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                        : new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            } else {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
            }
            return sdf.parse(dateTimeStr);
        } catch (ParseException e) {
            return null;
        }
    }

    private void updateAccount(HttpServletRequest request, HttpServletResponse response){
        int id = Integer.parseInt(request.getParameter("id"));
        UserService userService = new UserService();
        User user = userService.getUserById(id);

        enterInfoUser(request, user);
        userService.update(user);
        
        try {
            response.sendRedirect(request.getContextPath() + "/admin/account-management");
        } catch (IOException ex) {
            Logger.getLogger(AdminDashboardServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
