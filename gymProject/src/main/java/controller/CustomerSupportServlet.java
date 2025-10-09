package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * CustomerSupportServlet handles customer support requests
 * Allows customers to submit tickets and view support history
 */
@WebServlet(name = "CustomerSupportServlet", urlPatterns = {"/member/support"})
public class CustomerSupportServlet extends HttpServlet {
    
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
        
        // TODO: Load support tickets from database
        // For now, set mock data
        request.setAttribute("openTickets", 2);
        request.setAttribute("resolvedTickets", 8);
        request.setAttribute("totalTickets", 10);
        
        // Forward to support page
        request.getRequestDispatcher("/views/member/support.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        String memberId = (String) session.getAttribute("userId");
        
        if ("submitTicket".equals(action)) {
            // Handle submitting a support ticket
            String category = request.getParameter("category");
            String subject = request.getParameter("subject");
            String description = request.getParameter("description");
            String priority = request.getParameter("priority");
            
            // TODO: Implement ticket submission logic
            request.setAttribute("message", "Đã gửi yêu cầu hỗ trợ thành công!");
            
        } else if ("updateTicket".equals(action)) {
            // Handle updating a support ticket
            String ticketId = request.getParameter("ticketId");
            String updateMessage = request.getParameter("updateMessage");
            
            // TODO: Implement ticket update logic
            request.setAttribute("message", "Đã cập nhật yêu cầu hỗ trợ thành công!");
            
        } else if ("closeTicket".equals(action)) {
            // Handle closing a support ticket
            String ticketId = request.getParameter("ticketId");
            
            // TODO: Implement ticket closing logic
            request.setAttribute("message", "Đã đóng yêu cầu hỗ trợ thành công!");
        }
        
        // Redirect back to support page
        response.sendRedirect(request.getContextPath() + "/member/support");
    }
}
