package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * MemberDashboardServlet handles member dashboard requests
 */
@WebServlet(name = "MemberDashboardServlet", urlPatterns = {"/member/dashboard"})
public class MemberDashboardServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(MemberDashboardServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in and is member
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"member".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/views/error/403.jsp");
            return;
        }
        
        try {
            // Prepare mock data for member dashboard
            Map<String, Object> dashboardData = prepareMemberDashboardData(user);
            
            // Set attributes for JSP
            request.setAttribute("dashboardData", dashboardData);
            request.setAttribute("currentUser", user);
            
            // Create member dashboard JSP if doesn't exist, otherwise use simple dashboard
            request.getRequestDispatcher("/views/member/dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.severe("Error preparing member dashboard data: " + e.getMessage());
            request.setAttribute("error", "Lỗi khi tải dữ liệu dashboard.");
            request.getRequestDispatcher("/views/error/500.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    private Map<String, Object> prepareMemberDashboardData(User user) {
        Map<String, Object> data = new HashMap<>();
        
        // Member-specific information
        data.put("memberName", user.getFullName());
        data.put("packageType", user.getPackageType() != null ? user.getPackageType() : "Basic");
        data.put("joinDate", user.getJoinDate());
        data.put("lastLogin", user.getLastLogin());
        
        // Member statistics
        data.put("totalSessions", 45);
        data.put("sessionsThisMonth", 8);
        data.put("nextPaymentDate", "15/11/2024");
        data.put("remainingSessions", 12);
        
        // Member activities
        List<Map<String, String>> memberActivities = new ArrayList<>();
        
        Map<String, String> activity1 = new HashMap<>();
        activity1.put("type", "Workout");
        activity1.put("description", "Buổi tập tại khu tạ - 1 giờ");
        activity1.put("time", "2 ngày trước");
        memberActivities.add(activity1);
        
        Map<String, String> activity2 = new HashMap<>();
        activity2.put("type", "PT Session");
        activity2.put("description", "Buổi tập cùng huấn luyện viên");
        activity2.put("time", "5 ngày trước");
        memberActivities.add(activity2);
        
        Map<String, String> activity3 = new HashMap<>();
        activity3.put("type", "Payment");
        activity3.put("description", "Thanh toán phí tháng thành công");
        activity3.put("time", "1 tuần trước");
        memberActivities.add(activity3);
        
        data.put("recentActivities", memberActivities);
        
        // Upcoming sessions
        List<Map<String, String>> upcomingSessions = new ArrayList<>();
        
        Map<String, String> session1 = new HashMap<>();
        session1.put("date", "04/10/2024");
        session1.put("time", "18:00 - 19:00");
        session1.put("type", "PT Session");
        session1.put("coach", "Nguyễn Văn Nam");
        upcomingSessions.add(session1);
        
        Map<String, String> session2 = new HashMap<>();
        session2.put("date", "06/10/2024");
        session2.put("time", "19:00 - 20:00");
        session2.put("type", "Group Class");
        session2.put("coach", "Trần Thị Lan");
        upcomingSessions.add(session2);
        
        data.put("upcomingSessions", upcomingSessions);
        
        return data;
    }
}
