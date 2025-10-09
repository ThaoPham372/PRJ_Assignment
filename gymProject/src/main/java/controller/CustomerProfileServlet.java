package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * CustomerProfileServlet handles customer profile management
 * Allows customers to view and update their personal information
 */
@WebServlet(name = "CustomerProfileServlet", urlPatterns = {"/member/profile"})
public class CustomerProfileServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in and is a member
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String role = (String) session.getAttribute("role");
        if (!"member".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/views/error/403.jsp");
            return;
        }
        
        // Forward to profile page
        request.getRequestDispatcher("/views/member/profile.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        String memberId = (String) session.getAttribute("userId");
        
        if ("update".equals(action)) {
            // Handle profile update
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String dateOfBirth = request.getParameter("dateOfBirth");
            
            // TODO: Implement profile update logic
            request.setAttribute("message", "Cập nhật thông tin thành công!");
            
        } else if ("changePassword".equals(action)) {
            // Handle password change
            String currentPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");
            
            // TODO: Implement password change logic
            if (newPassword.equals(confirmPassword)) {
                request.setAttribute("message", "Đổi mật khẩu thành công!");
            } else {
                request.setAttribute("error", "Mật khẩu mới không khớp!");
            }
        }
        
        // Forward back to profile page
        request.getRequestDispatcher("/views/member/profile.jsp").forward(request, response);
    }
}
