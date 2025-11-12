package controller;

import java.io.IOException;

import Utils.FormUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Admin;
import service.AdminService;
import service.PasswordService;

/*
    Note: 
 */
@WebServlet(urlPatterns = "/admin/profile")
public class AdminProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Check if user is logged in
        if (req.getSession().getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        // Get message from session (set during redirect) and move to request
        String message = (String) req.getSession().getAttribute("message");
        if (message != null) {
            req.setAttribute("message", message);
            req.getSession().removeAttribute("message");
        }
        
        // Get error from session (set during redirect) and move to request
        String error = (String) req.getSession().getAttribute("error");
        if (error != null) {
            req.setAttribute("error", error);
            req.getSession().removeAttribute("error");
        }
        
        req.getRequestDispatcher("/views/admin/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Check if user is logged in
        if (req.getSession().getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        String action = req.getParameter("action");
        if (action == null || action.isEmpty()) {
            req.setAttribute("error", "Action parameter is required");
            req.getRequestDispatcher("/views/admin/profile.jsp").forward(req, resp);
            return;
        }
        
        switch (action) {
            case "updateAdmin" ->
                updateAdmin(req, resp);
            case "updateAdminPassword" ->
                updateAdminPassword(req, resp);
            case "changePassword" ->
                handleChangePassword(req, resp);
            default -> {
                req.setAttribute("error", "Invalid action: " + action);
                req.getRequestDispatcher("/views/admin/profile.jsp").forward(req, resp);
            }
        }
    }

    private void updateAdmin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                request.setAttribute("error", "User ID is required");
                request.getRequestDispatcher("/views/admin/profile.jsp").forward(request, response);
                return;
            }
            
            int id = Integer.parseInt(idParam);
            AdminService adminService = new AdminService();
            Admin admin = adminService.getAdminById(id);
            
            if (admin == null) {
                request.setAttribute("error", "Admin not found");
                request.getRequestDispatcher("/views/admin/profile.jsp").forward(request, response);
                return;
            }

            FormUtils.getFormValue(request, admin);
            adminService.update(admin);
            request.getSession().setAttribute("user", admin);
            request.getSession().setAttribute("message", "Cập nhật thông tin thành công!");
            response.sendRedirect(request.getContextPath() + "/admin/profile");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid user ID format");
            request.getRequestDispatcher("/views/admin/profile.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/views/admin/profile.jsp").forward(request, response);
        }
    }

    private void updateAdminPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                request.setAttribute("error", "User ID is required");
                request.getRequestDispatcher("/views/admin/profile.jsp").forward(request, response);
                return;
            }
            
            int id = Integer.parseInt(idParam);
            PasswordService passwordService = new PasswordService();
            AdminService adminService = new AdminService();
            Admin admin = adminService.getAdminById(id);
            
            if (admin == null) {
                request.setAttribute("error", "Admin not found");
                request.getRequestDispatcher("/views/admin/profile.jsp").forward(request, response);
                return;
            }

            String currentPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");

            if (currentPassword == null || newPassword == null || confirmPassword == null) {
                request.setAttribute("error", "Tất cả các trường mật khẩu đều bắt buộc");
                request.getRequestDispatcher("/views/admin/profile.jsp").forward(request, response);
                return;
            }

            String userPassword = admin.getPassword();
            boolean isCurrentPasswordCorrect = passwordService.verifyPassword(currentPassword, userPassword);

            if (!newPassword.equals(confirmPassword)) {
                request.setAttribute("error", "Mật khẩu mới và xác nhận mật khẩu không khớp!");
                request.getRequestDispatcher("/views/admin/profile.jsp").forward(request, response);
                return;
            }
            
            if (!isCurrentPasswordCorrect) {
                request.setAttribute("error", "Mật khẩu hiện tại không đúng!");
                request.getRequestDispatcher("/views/admin/profile.jsp").forward(request, response);
                return;
            }

            String hashNewP = passwordService.hashPassword(newPassword);
            admin.setPassword(hashNewP);
            adminService.update(admin);
            request.getSession().setAttribute("user", admin);
            request.getSession().setAttribute("message", "Đổi mật khẩu thành công!");
            response.sendRedirect(request.getContextPath() + "/admin/profile");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid user ID format");
            request.getRequestDispatcher("/views/admin/profile.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/views/admin/profile.jsp").forward(request, response);
        }
    }

    /**
     * Xử lý yêu cầu đổi mật khẩu từ profile
     * Tạo password reset token và chuyển hướng sang trang reset password
     * Giống như chức năng của member
     */
    private void handleChangePassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("[AdminProfileServlet] ===== CHANGE PASSWORD REQUEST =====");
        
        try {
            // Lấy admin từ session
            Object userObj = request.getSession().getAttribute("user");
            if (userObj == null || !(userObj instanceof Admin)) {
                request.getSession().setAttribute("error", "Bạn cần đăng nhập để đổi mật khẩu.");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            Admin admin = (Admin) userObj;
            String email = admin.getEmail();
            HttpSession session = request.getSession();
            
            System.out.println("[AdminProfileServlet] Admin email: " + email);
            
            if (email == null || email.trim().isEmpty()) {
                session.setAttribute("error", "Không tìm thấy email. Vui lòng liên hệ quản trị viên.");
                response.sendRedirect(request.getContextPath() + "/admin/profile");
                return;
            }

            // Normalize email
            email = email.trim().toLowerCase();

            // Kiểm tra rate limiting (30 giây)
            PasswordService passwordService = new PasswordService();
            boolean hasPending = passwordService.hasPendingResetRequest(email);
            if (hasPending) {
                session.setAttribute("error", "Bạn đã yêu cầu đổi mật khẩu gần đây. Vui lòng đợi 30 giây trước khi gửi lại.");
                response.sendRedirect(request.getContextPath() + "/admin/profile");
                return;
            }

            // Tạo password reset token và gửi email
            System.out.println("[AdminProfileServlet] Calling passwordService.requestPasswordReset()...");
            String verificationCode = passwordService.requestPasswordReset(email);
            
            if (verificationCode == null) {
                System.err.println("[AdminProfileServlet] ❌ Failed to generate verification code");
                session.setAttribute("error", "Có lỗi xảy ra khi gửi email. Vui lòng thử lại sau.");
                response.sendRedirect(request.getContextPath() + "/admin/profile");
                return;
            }

            System.out.println("[AdminProfileServlet] ✅ Verification code generated: " + verificationCode);
            
            // Lưu email vào session và chuyển hướng sang trang reset password
            session.setAttribute("resetEmail", email);
            session.setAttribute("successMessage", "Mã xác nhận đã được gửi đến email: " + email);
            
            String redirectUrl = request.getContextPath() + "/auth/reset-password";
            System.out.println("[AdminProfileServlet] Redirecting to: " + redirectUrl);
            response.sendRedirect(redirectUrl);
            
        } catch (Exception e) {
            System.err.println("[AdminProfileServlet] Error handling change password: " + e.getMessage());
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("error", "Có lỗi xảy ra. Vui lòng thử lại sau.");
            response.sendRedirect(request.getContextPath() + "/admin/profile");
        }
    }
}
