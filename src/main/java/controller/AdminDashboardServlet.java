package controller;

import dao.MemberDAO;
import dao.PaymentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {
        "/admin/home",
        "/admin/dashboard",
        "/admin/service-schedule",
        "/admin/order-management"
})
public class AdminDashboardServlet extends HttpServlet {

    private final MemberDAO memberDAO = new MemberDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String reqURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = reqURI.substring(contextPath.length());

        String jspPath;

        switch (path) {
            case "/admin/home" ->
                jspPath = "/views/admin/admin_home.jsp";
            case "/admin/dashboard" -> {
                jspPath = "/views/admin/dashboard.jsp";
                // Load dashboard statistics from database
                loadDashboardStatistics(req);
            }
            case "/admin/service-schedule" ->
                jspPath = "/views/admin/service_schedule.jsp";
            case "/admin/order-management" ->
                jspPath = "/views/admin/order_management.jsp";
//            case "/admin/reports" ->
//                jspPath = "/views/admin/reports.jsp";
            default ->
                jspPath = "/views/admin/admin_home.jsp";
        }

        // Set page title attribute
        req.setAttribute("pageTitle", "Admin Dashboard - GymFit");

        // Forward to the appropriate JSP
        req.getRequestDispatcher(jspPath).forward(req, resp);
    }

    /**
     * Load dashboard statistics from database
     */
    private void loadDashboardStatistics(HttpServletRequest req) {
        try {
            // Count total members with membership
            long totalMembersWithMembership = memberDAO.countMembersWithMembership();
            
            // Count total active members
            long totalActiveMembers = memberDAO.countActiveMembers();
            
            // Get revenue this month
            BigDecimal revenueThisMonth = paymentDAO.getRevenueThisMonth();
            
            // Format revenue for display
            String revenueFormatted = formatRevenue(revenueThisMonth);
            
            // Set attributes for JSP
            req.setAttribute("totalMembersWithMembership", totalMembersWithMembership);
            req.setAttribute("totalActiveMembers", totalActiveMembers);
            req.setAttribute("revenueThisMonth", revenueThisMonth);
            req.setAttribute("revenueFormatted", revenueFormatted);
        } catch (Exception e) {
            // Set default values on error
            req.setAttribute("totalMembersWithMembership", 0L);
            req.setAttribute("totalActiveMembers", 0L);
            req.setAttribute("revenueThisMonth", BigDecimal.ZERO);
            req.setAttribute("revenueFormatted", "0");
            System.err.println("Error loading dashboard statistics: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Format revenue for display (e.g., 456000000 -> 456M)
     */
    private String formatRevenue(BigDecimal revenue) {
        if (revenue == null || revenue.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        }
        
        double value = revenue.doubleValue();
        
        if (value >= 1_000_000_000) {
            return String.format("%.1fB", value / 1_000_000_000);
        } else if (value >= 1_000_000) {
            return String.format("%.0fM", value / 1_000_000);
        } else if (value >= 1_000) {
            return String.format("%.1fK", value / 1_000);
        } else {
            NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
            formatter.setMaximumFractionDigits(0);
            return formatter.format(value);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Handle POST reqs (for forms, updates, etc.)
    }

    @Override
    public String getServletInfo() {
        return "Admin Dashboard Servlet for GymFit Management System";
    }
}
