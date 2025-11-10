package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Admin;
import service.AdminService;
import service.PasswordService;

import java.io.IOException;

import Utils.FormUtils;

/*
    Note: 
 */
@WebServlet(urlPatterns = "/admin/profile")
public class AdminProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/admin/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "updateAdmin" ->
                updateAdmin(req, resp);
            case "updateAdminPassword" ->
                updateAdminPassword(req, resp);
            default -> {
                System.out.println("Action is null");
                req.getRequestDispatcher("/views/admin/profile.jsp").forward(req, resp);
            }
        }
    }

    private void updateAdmin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        AdminService adminService = new AdminService();
        Admin admin = adminService.getAdminById(id);


        FormUtils.getFormValue(request, admin);
        adminService.update(admin);
        request.getSession().setAttribute("user", admin);
        request.setAttribute("message", "> Update successful!");
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
}
