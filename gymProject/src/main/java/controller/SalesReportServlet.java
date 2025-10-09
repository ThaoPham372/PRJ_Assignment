package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * SalesReportServlet handles sales reporting and analytics
 * Generates revenue reports and statistics
 */
@WebServlet(name = "SalesReportServlet", urlPatterns = {"/admin/sales-report"})
public class SalesReportServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(SalesReportServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("role");
        if (!"admin".equals(role) && !"manager".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/views/error/403.jsp");
            return;
        }

        try {
            // Get report parameters
            String fromDate = request.getParameter("fromDate");
            String toDate = request.getParameter("toDate");
            String reportType = request.getParameter("reportType");

            // Set default values if not provided
            if (fromDate == null || fromDate.trim().isEmpty()) {
                fromDate = "2024-09-01";
            }
            if (toDate == null || toDate.trim().isEmpty()) {
                toDate = "2024-09-30";
            }
            if (reportType == null || reportType.trim().isEmpty()) {
                reportType = "monthly";
            }

            // Mock revenue data (in real app, this would come from database)
            request.setAttribute("totalRevenue", "450000000");
            request.setAttribute("membershipRevenue", "350000000");
            request.setAttribute("ptRevenue", "75000000");
            request.setAttribute("otherRevenue", "25000000");
            
            // Mock package revenue breakdown
            request.setAttribute("basicRevenue", "120000000");
            request.setAttribute("premiumRevenue", "180000000");
            request.setAttribute("vipRevenue", "150000000");
            
            // Mock chart data
            request.setAttribute("revenueMonths", "T1,T2,T3,T4,T5,T6,T7,T8,T9");
            request.setAttribute("revenueValues", "380M,395M,410M,425M,440M,455M,420M,435M,450M");
            
            // Set request parameters
            request.setAttribute("fromDate", fromDate);
            request.setAttribute("toDate", toDate);
            request.setAttribute("reportType", reportType);

            // Forward to sales report page
            request.getRequestDispatcher("/views/admin/sales-report.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.severe("Error in sales report: " + e.getMessage());
            request.setAttribute("error", "Đã xảy ra lỗi. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/views/error/500.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("role");
        if (!"admin".equals(role) && !"manager".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/views/error/403.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/admin/sales-report");
            return;
        }

        try {
            switch (action) {
                case "generate_report":
                    handleGenerateReport(request, response);
                    break;
                case "export_report":
                    handleExportReport(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/admin/sales-report");
            }

        } catch (Exception e) {
            logger.severe("Error in sales report POST: " + e.getMessage());
            request.setAttribute("error", "Đã xảy ra lỗi. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/views/error/500.jsp").forward(request, response);
        }
    }

    private void handleGenerateReport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");
        String reportType = request.getParameter("reportType");

        // Validate parameters
        if (fromDate == null || fromDate.trim().isEmpty() ||
            toDate == null || toDate.trim().isEmpty()) {
            
            request.setAttribute("error", "Vui lòng chọn khoảng thời gian báo cáo.");
            doGet(request, response);
            return;
        }

        // In real app, generate report from database
        logger.info("Generating sales report from " + fromDate + " to " + toDate + " (" + reportType + ")");
        
        // Redirect with parameters
        response.sendRedirect(request.getContextPath() + "/admin/sales-report?fromDate=" + fromDate + 
                            "&toDate=" + toDate + "&reportType=" + reportType + "&success=generated");
    }

    private void handleExportReport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");
        String format = request.getParameter("format"); // excel, pdf, csv

        if (format == null || format.trim().isEmpty()) {
            format = "excel";
        }

        // In real app, generate and download report file
        logger.info("Exporting sales report from " + fromDate + " to " + toDate + " in " + format + " format");
        
        // For now, just redirect back with success message
        response.sendRedirect(request.getContextPath() + "/admin/sales-report?fromDate=" + fromDate + 
                            "&toDate=" + toDate + "&success=exported&format=" + format);
    }
}

