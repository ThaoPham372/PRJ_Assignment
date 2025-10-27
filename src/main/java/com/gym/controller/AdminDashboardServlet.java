package com.gym.controller;

import com.gym.model.Admin;
import com.gym.service.AdminService;
import com.gym.service.PasswordService;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

        // Check if user is logged in (in production, you would check session and role)
        HttpSession session = request.getSession(false);

        // For demo purposes, we'll just forward to the JSP pages
        String jspPath = "";

        switch (path) {
            case "/admin/home":
                jspPath = "/views/admin/admin_home.jsp";
                break;
            case "/admin/dashboard":
                jspPath = "/views/admin/dashboard.jsp";
                break;
            case "/admin/profile":
                jspPath = "/views/admin/profile.jsp";
                break;
            case "/admin/account-management":
                jspPath = "/views/admin/account_management.jsp";
                break;
            case "/admin/member-management":
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
            case "addAdmin":
                addAdmin(request, response);
                break;
            case "addUser":
                addUser(request, response);
                break;
            case "addTrainer":
                addTrainer(request, response);
                break;
            case "addProduct":
                addProduct(request, response);
                break;

            case "updateAdmin":
                updateAdmin(request, response);
                break;
            case "updateUser":
                updateUser(request, response);
                break;
            case "updateTrainer":
                updateTrainer(request, response);
                break;
            case "updateProduct":
                updateProduct(request, response);
                break;

            case "deleteAdmin":
                deleteAdmin(request, response);
                break;
            case "deleteUser":
                deleteUser(request, response);
                break;
            case "deleteTrainer":
                deleteTrainer(request, response);
                break;
            case "deleteProduct":
                deleteProduct(request, response);
                break;
            default:
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

        try {
            Admin admin = new Admin();
            this.setAdmin(request, admin);
            boolean success = adminService.addAdmin(admin);

            if (success) {
                request.setAttribute("message", "Admin created successfully!");
            } else {
                request.setAttribute("error", "Failed to create admin. Maybe email or name already exists.");
            }

            // Quay lại trang quản lý tài khoản
            request.getRequestDispatcher("/views/admin/account_management.jsp")
                    .forward(request, response);

        } catch (IOException | ServletException e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("/views/admin/account_management.jsp")
                    .forward(request, response);
        }
    }
    
    private void setAdmin(HttpServletRequest request, Admin admin) {
        PasswordService passwordService = new PasswordService();

        if (request.getParameter("name") != null) {
            admin.setName(request.getParameter("name").trim());
        }

        if (request.getParameter("email") != null) {
            admin.setEmail(request.getParameter("email").trim());
        }

        if (request.getParameter("phone") != null) {
            admin.setPhone(request.getParameter("phone").trim());
        }

        if (request.getParameter("password") != null && !request.getParameter("password").isEmpty()) {
            String passwordHash = passwordService.hashPassword(request.getParameter("password"));
            admin.setPasswordHash(passwordHash);
        }

        if (request.getParameter("note") != null) {
            admin.setNote(request.getParameter("note").trim());
        }

        if (request.getParameter("status") != null) {
            admin.setStatus(request.getParameter("status").trim());
        }

        if (request.getParameter("emailVerified") != null) {
            admin.setEmailVerified(request.getParameter("emailVerified").equals("true"));
        }

        if (request.getParameter("failedLoginAttempts") != null) {
            try {
                admin.setFailedLoginAttempts(Integer.valueOf(request.getParameter("failedLoginAttempts")));
            } catch (NumberFormatException e) {
                admin.setFailedLoginAttempts(0);
            }
        }

        if (request.getParameter("createdDate") != null) {
            admin.setCreatedDate(parseDateTime(request.getParameter("createdDate")));
        }
        if (request.getParameter("lastLogin") != null) {
            admin.setLastLogin(parseDateTime(request.getParameter("lastLogin")));
        }
        if (request.getParameter("lockedUntil") != null) {
            admin.setLockedUntil(parseDateTime(request.getParameter("lockedUntil")));
        }
    }
    
    private void addUser(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void addTrainer(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void addProduct(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void updateAdmin(HttpServletRequest request, HttpServletResponse response) {
        AdminService adminService = new AdminService();
        int id = Integer.parseInt(request.getParameter("id"));
        Admin admin = adminService.getAdminById(id);
        this.setAdmin(request, admin);
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

    private void updateUser(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void updateTrainer(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void deleteAdmin(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        AdminService adminService = new AdminService();
        Admin admin = adminService.getAdminById(id);
        adminService.deleteAdmin(admin);
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void deleteTrainer(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


}
