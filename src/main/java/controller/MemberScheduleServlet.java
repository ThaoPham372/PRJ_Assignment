package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
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
public class MemberScheduleServlet extends BaseMemberServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Kiểm tra authentication sử dụng method từ BaseMemberServlet
        // Method này sẽ kiểm tra isLoggedIn, lấy user từ session, 
        // reload member từ DB nếu cần, và redirect về login nếu chưa đăng nhập
        Member member = requireAuthentication(request, response);
        if (member == null) {
            return; // Đã redirect trong requireAuthentication
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
