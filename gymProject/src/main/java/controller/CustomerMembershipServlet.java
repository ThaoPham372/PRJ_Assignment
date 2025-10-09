package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * CustomerMembershipServlet handles customer membership management
 * Allows customers to view their membership status and upgrade options
 */
@WebServlet(name = "CustomerMembershipServlet", urlPatterns = {"/member/membership"})
public class CustomerMembershipServlet extends HttpServlet {
    
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
        
        String memberId = (String) session.getAttribute("userId");
        
        // TODO: Load membership information from database
        // For now, set mock data
        request.setAttribute("membershipType", "Premium");
        request.setAttribute("membershipStatus", "Active");
        request.setAttribute("expiryDate", "2024-12-31");
        request.setAttribute("remainingDays", 45);
        request.setAttribute("usedSessions", 12);
        request.setAttribute("totalSessions", 24);
        
        // Forward to membership page
        request.getRequestDispatcher("/views/member/membership.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        String memberId = (String) session.getAttribute("userId");
        
        if ("upgrade".equals(action)) {
            // Handle membership upgrade
            String newMembershipType = request.getParameter("membershipType");
            
            // TODO: Implement membership upgrade logic
            request.setAttribute("message", "Nâng cấp gói thành công!");
            
        } else if ("renew".equals(action)) {
            // Handle membership renewal
            String renewalPeriod = request.getParameter("renewalPeriod");
            
            // TODO: Implement membership renewal logic
            request.setAttribute("message", "Gia hạn thành công!");
            
        } else if ("freeze".equals(action)) {
            // Handle membership freeze
            String freezeReason = request.getParameter("freezeReason");
            
            // TODO: Implement membership freeze logic
            request.setAttribute("message", "Đã tạm ngưng gói thành công!");
        }
        
        // Redirect back to membership page
        response.sendRedirect(request.getContextPath() + "/member/membership");
    }
}
