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
 * AdminDashboardServlet handles admin dashboard requests
 */
@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(AdminDashboardServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in and is admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"admin".equals(user.getRole()) && !"manager".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/views/error/403.jsp");
            return;
        }
        
        try {
            // Prepare mock data for dashboard
            Map<String, Object> dashboardData = prepareDashboardData();
            
            // Set attributes for JSP
            request.setAttribute("dashboardData", dashboardData);
            request.setAttribute("currentUser", user);
            
            // Forward to dashboard JSP
            request.getRequestDispatcher("/views/admin/dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.severe("Error preparing dashboard data: " + e.getMessage());
            request.setAttribute("error", "Lỗi khi tải dữ liệu dashboard.");
            request.getRequestDispatcher("/views/error/500.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    private Map<String, Object> prepareDashboardData() {
        Map<String, Object> data = new HashMap<>();
        
        // Mock statistics
        data.put("totalMembers", 150);
        data.put("activeMembers", 120);
        data.put("monthlyRevenue", 45000000);
        data.put("newMembersThisMonth", 25);
        
        // Mock recent activities
        List<Map<String, String>> recentActivities = new ArrayList<>();
        
        Map<String, String> activity1 = new HashMap<>();
        activity1.put("type", "New Member");
        activity1.put("description", "Nguyễn Văn A đã đăng ký gói Premium");
        activity1.put("time", "2 giờ trước");
        recentActivities.add(activity1);
        
        Map<String, String> activity2 = new HashMap<>();
        activity2.put("type", "Payment");
        activity2.put("description", "Trần Thị B đã thanh toán phí tháng");
        activity2.put("time", "4 giờ trước");
        recentActivities.add(activity2);
        
        Map<String, String> activity3 = new HashMap<>();
        activity3.put("type", "Equipment");
        activity3.put("description", "Máy chạy bộ #3 cần bảo trì");
        activity3.put("time", "6 giờ trước");
        recentActivities.add(activity3);
        
        data.put("recentActivities", recentActivities);
        
        // Mock membership packages
        List<Map<String, Object>> packages = new ArrayList<>();
        
        Map<String, Object> package1 = new HashMap<>();
        package1.put("name", "Basic");
        package1.put("price", 300000);
        package1.put("members", 80);
        packages.add(package1);
        
        Map<String, Object> package2 = new HashMap<>();
        package2.put("name", "Premium");
        package2.put("price", 500000);
        package2.put("members", 60);
        packages.add(package2);
        
        Map<String, Object> package3 = new HashMap<>();
        package3.put("name", "VIP");
        package3.put("price", 800000);
        package3.put("members", 30);
        packages.add(package3);
        
        data.put("packages", packages);
        
        return data;
    }
}