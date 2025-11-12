package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Member;

/**
 * MemberScheduleServlet - Handles member schedule pages
 */
@WebServlet({
    "/member/schedule",
    "/member/my-bookings"
})
public class MemberScheduleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if member is logged in
        Member member = (Member) request.getSession().getAttribute("member");
        if (member == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String pathInfo = request.getServletPath();
        
        switch (pathInfo) {
            case "/member/schedule":
                request.getRequestDispatcher("/views/member/schedule_new.jsp").forward(request, response);
                break;
            case "/member/my-bookings":
                request.getRequestDispatcher("/views/member/my_bookings.jsp").forward(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
