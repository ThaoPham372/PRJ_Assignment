package com.gym.controller;

import com.gym.service.PasswordService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Unified Servlet for Password Reset Flow
 * Handles both forgot password and reset password in one servlet
 * 
 * URLs:
 * - GET  /auth/forgot-password -> Show forgot password form
 * - POST /auth/forgot-password -> Send verification code
 * - GET  /auth/reset-password  -> Show reset password form (with token)
 * - POST /auth/reset-password  -> Reset password with token
 */
@WebServlet(name = "PasswordResetServlet", urlPatterns = {
    "/auth/forgot-password",
    "/auth/reset-password"
})
public class PasswordResetServlet extends HttpServlet {
    
    private PasswordService passwordService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.passwordService = new PasswordService();
        System.out.println("[PasswordResetServlet] Initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        if ("/auth/forgot-password".equals(path)) {
            // Show forgot password form
            showForgotPasswordForm(request, response);
        } else if ("/auth/reset-password".equals(path)) {
            // Show reset password form
            showResetPasswordForm(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        if ("/auth/forgot-password".equals(path)) {
            // Handle forgot password request
            handleForgotPassword(request, response);
        } else if ("/auth/reset-password".equals(path)) {
            // Handle reset password
            handleResetPassword(request, response);
        }
    }
    
    // ==================== FORGOT PASSWORD ====================
    
    /**
     * Show forgot password form
     */
    private void showForgotPasswordForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/auth/forgot-password.jsp").forward(request, response);
    }
    
    /**
     * Handle forgot password - send verification code
     */
    private void handleForgotPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập email");
            request.getRequestDispatcher("/views/auth/forgot-password.jsp").forward(request, response);
            return;
        }
        
        // Normalize email: trim + lowercase
        email = email.trim().toLowerCase();
        
        System.out.println("========================================");
        System.out.println("[PasswordResetServlet] ===== FORGOT PASSWORD REQUEST =====");
        System.out.println("[PasswordResetServlet] Original input: '" + request.getParameter("email") + "'");
        System.out.println("[PasswordResetServlet] Normalized email: '" + email + "'");
        
        // ✅ Check rate limiting - chỉ 30 giây thay vì 15 phút
        boolean hasPending = passwordService.hasPendingResetRequest(email);
        System.out.println("[PasswordResetServlet] Has pending request: " + hasPending);
        
        if (hasPending) {
            System.out.println("[PasswordResetServlet] ⚠️ Rate limiting: Request too soon");
            request.setAttribute("error", "Bạn đã yêu cầu đổi mật khẩu gần đây. Vui lòng đợi 30 giây trước khi gửi lại.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/views/auth/forgot-password.jsp").forward(request, response);
            return;
        }
        
        // Request password reset
        System.out.println("[PasswordResetServlet] Calling requestPasswordReset()...");
        String verificationCode = passwordService.requestPasswordReset(email);
        
        if (verificationCode == null) {
            System.out.println("[PasswordResetServlet] ❌ requestPasswordReset returned null");
            request.setAttribute("error", "Email không tồn tại trong hệ thống hoặc có lỗi khi gửi email. Vui lòng kiểm tra lại email và thử lại.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/views/auth/forgot-password.jsp").forward(request, response);
            return;
        }
        
        System.out.println("[PasswordResetServlet] ✅ Verification code generated: " + verificationCode);
        System.out.println("========================================");
        
        // Success - store email in session and redirect to reset form
        HttpSession session = request.getSession();
        session.setAttribute("resetEmail", email);
        session.setAttribute("successMessage", "Mã xác nhận đã được gửi đến email: " + email);
        
        response.sendRedirect(request.getContextPath() + "/auth/reset-password");
    }
    
    // ==================== RESET PASSWORD ====================
    
    /**
     * Show reset password form
     */
    private void showResetPasswordForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String resetEmail = (String) session.getAttribute("resetEmail");
        
        if (resetEmail == null || resetEmail.isEmpty()) {
            // No email in session - redirect to forgot password
            response.sendRedirect(request.getContextPath() + "/auth/forgot-password");
            return;
        }
        
        // Check for success message from forgot password
        String successMessage = (String) session.getAttribute("successMessage");
        if (successMessage != null) {
            request.setAttribute("success", successMessage);
            session.removeAttribute("successMessage");
        }
        
        request.setAttribute("email", resetEmail);
        request.getRequestDispatcher("/views/auth/reset-password.jsp").forward(request, response);
    }
    
    /**
     * Handle reset password - verify token and update password
     */
    private void handleResetPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("resetEmail");
        
        if (email == null || email.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/auth/forgot-password");
            return;
        }
        
        String token = request.getParameter("token");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validation
        if (token == null || token.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập mã xác nhận");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/views/auth/reset-password.jsp").forward(request, response);
            return;
        }
        
        if (newPassword == null || newPassword.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập mật khẩu mới");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/views/auth/reset-password.jsp").forward(request, response);
            return;
        }
        
        if (newPassword.length() < 6) {
            request.setAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/views/auth/reset-password.jsp").forward(request, response);
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/views/auth/reset-password.jsp").forward(request, response);
            return;
        }
        
        System.out.println("[PasswordResetServlet] Reset password attempt for: " + email);
        
        // Reset password
        boolean success = passwordService.resetPassword(email, token.trim(), newPassword);
        
        if (!success) {
            request.setAttribute("error", "Mã xác nhận không đúng hoặc đã hết hạn. Vui lòng thử lại.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/views/auth/reset-password.jsp").forward(request, response);
            return;
        }
        
        // Success - clear session and redirect to login with success message
        session.removeAttribute("resetEmail");
        session.setAttribute("loginSuccessMessage", "Đổi mật khẩu thành công! Vui lòng đăng nhập với mật khẩu mới.");
        
        response.sendRedirect(request.getContextPath() + "/login");
    }
}

