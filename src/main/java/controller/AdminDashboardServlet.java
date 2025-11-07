package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AdminDashboardServlet - Servlet để điều hướng các trang admin Đây là servlet
 * giả để test hiển thị dashboard admin
 */
@WebServlet(name = "AdminDashboardServlet", urlPatterns = {
        "/admin/home",
        "/admin/dashboard",
        "/admin/service-schedule",
        "/admin/trainer-management",
        "/admin/order-management",
        "/admin/payment-finance",
        "/admin/reports"
})
public class AdminDashboardServlet extends HttpServlet {

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
            case "/admin/dashboard" ->
                jspPath = "/views/admin/dashboard.jsp";

            case "/admin/service-schedule" ->
                jspPath = "/views/admin/service_schedule.jsp";
            case "/admin/trainer-management" ->
                jspPath = "/views/admin/trainer_management.jsp";
            case "/admin/order-management" ->
                jspPath = "/views/admin/order_management.jsp";
            case "/admin/payment-finance" ->
                jspPath = "/views/admin/payment_finance.jsp";
            case "/admin/reports" ->
                jspPath = "/views/admin/reports.jsp";
            default ->
                jspPath = "/views/admin/admin_home.jsp";
        }

        // Set page title attribute
        req.setAttribute("pageTitle", "Admin Dashboard - GymFit");

        // Forward to the appropriate JSP
        req.getRequestDispatcher(jspPath).forward(req, resp);
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
