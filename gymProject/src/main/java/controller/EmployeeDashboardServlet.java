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
 * EmployeeDashboardServlet handles employee dashboard requests
 */
@WebServlet(name = "EmployeeDashboardServlet", urlPatterns = {"/employee/dashboard"})
public class EmployeeDashboardServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(EmployeeDashboardServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in and is employee
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"employee".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/views/error/403.jsp");
            return;
        }
        
        try {
            // Prepare mock data for employee dashboard
            Map<String, Object> dashboardData = prepareEmployeeDashboardData();
            
            // Set attributes for JSP
            request.setAttribute("dashboardData", dashboardData);
            request.setAttribute("currentUser", user);
            
            // Create employee dashboard JSP if doesn't exist, otherwise use admin dashboard
            request.getRequestDispatcher("/views/admin/dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.severe("Error preparing employee dashboard data: " + e.getMessage());
            request.setAttribute("error", "Lỗi khi tải dữ liệu dashboard.");
            request.getRequestDispatcher("/views/error/500.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    private Map<String, Object> prepareEmployeeDashboardData() {
        Map<String, Object> data = new HashMap<>();
        
        // Employee-specific statistics (limited access)
        data.put("totalMembers", 120);
        data.put("activeMembers", 100);
        data.put("monthlyRevenue", 35000000);
        data.put("newMembersThisMonth", 15);
        
        // Employee tasks and activities
        List<Map<String, String>> employeeTasks = new ArrayList<>();
        
        Map<String, String> task1 = new HashMap<>();
        task1.put("type", "Check-in");
        task1.put("description", "Xử lý check-in cho thành viên mới");
        task1.put("time", "1 giờ trước");
        employeeTasks.add(task1);
        
        Map<String, String> task2 = new HashMap<>();
        task2.put("type", "Equipment");
        task2.put("description", "Kiểm tra thiết bị trong khu tạ");
        task2.put("time", "3 giờ trước");
        employeeTasks.add(task2);
        
        Map<String, String> task3 = new HashMap<>();
        task3.put("type", "Payment");
        task3.put("description", "Thu phí tháng cho 5 thành viên");
        task3.put("time", "5 giờ trước");
        employeeTasks.add(task3);
        
        data.put("recentActivities", employeeTasks);
        
        // Employee permissions
        data.put("canManageMembers", true);
        data.put("canManageEquipment", true);
        data.put("canProcessPayments", true);
        data.put("canViewReports", false);
        data.put("canManageCoaches", true);
        
        return data;
    }
}
