package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * CustomerScheduleServlet handles customer schedule management
 * Allows customers to view and book training sessions
 */
@WebServlet(name = "CustomerScheduleServlet", urlPatterns = {"/member/schedule"})
public class CustomerScheduleServlet extends HttpServlet {
    
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
        
        // Forward to schedule page
        request.getRequestDispatcher("/views/member/schedule.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("book".equals(action)) {
            // Handle booking a session
            String sessionId = request.getParameter("sessionId");
            String memberId = (String) request.getSession().getAttribute("userId");
            
            // TODO: Implement booking logic
            request.setAttribute("message", "Đã đăng ký thành công!");
        } else if ("cancel".equals(action)) {
            // Handle canceling a session
            String bookingId = request.getParameter("bookingId");
            
            // TODO: Implement cancel logic
            request.setAttribute("message", "Đã hủy đăng ký thành công!");
        }
        
        // Redirect back to schedule page
        response.sendRedirect(request.getContextPath() + "/member/schedule");
    }
}
