package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import dao.MemberDAO;
import dao.PaymentDAO;
import dao.schedule.PTBookingDAO;
import dao.schedule.TrainerScheduleDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.schedule.BookingStatus;
import model.schedule.CancelledBy;
import model.schedule.PTBooking;
import model.schedule.TrainerSchedule;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {
        "/admin/home",
        "/admin/dashboard",
        "/admin/service-schedule",
        "/admin/order-management"
})
public class AdminDashboardServlet extends HttpServlet {

    private final MemberDAO memberDAO = new MemberDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final PTBookingDAO ptBookingDAO = new PTBookingDAO();
    private final TrainerScheduleDAO trainerScheduleDAO = new TrainerScheduleDAO();

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
            case "/admin/service-schedule" -> {
                jspPath = "/views/admin/service_schedule.jsp";
                // Load schedule data from database
                loadScheduleData(req);
            }
            case "/admin/order-management" ->
                jspPath = "/views/admin/order_management.jsp";
            case "/admin/payment-finance" ->
                jspPath = "/views/admin/payment_finance.jsp";
            // case "/admin/reports" ->
            //     jspPath = "/views/admin/reports.jsp";
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

    /**
     * Load schedule data from database (bookings and trainer schedules)
     */
    private void loadScheduleData(HttpServletRequest req) {
        try {
            // Load all PT bookings with full details
            List<PTBooking> allBookings = ptBookingDAO.findAllWithDetails();
            req.setAttribute("allBookings", allBookings);

            // Load all trainer schedules
            List<TrainerSchedule> allSchedules = trainerScheduleDAO.findAll();
            req.setAttribute("allSchedules", allSchedules);

        } catch (Exception e) {
            // Set empty lists on error
            req.setAttribute("allBookings", List.of());
            req.setAttribute("allSchedules", List.of());
            System.err.println("Error loading schedule data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String reqURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = reqURI.substring(contextPath.length());

        // Handle booking actions for service-schedule
        if (path.equals("/admin/service-schedule")) {
            String action = req.getParameter("action");
            if ("confirm".equals(action)) {
                handleConfirmBooking(req, resp);
                return;
            } else if ("cancel".equals(action)) {
                handleCancelBooking(req, resp);
                return;
            }
        }

        // Default: invalid action
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
    }

    /**
     * Handle confirm booking action
     */
    private void handleConfirmBooking(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String bookingIdStr = req.getParameter("bookingId");
            if (bookingIdStr == null || bookingIdStr.trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Booking ID is required");
                return;
            }

            Integer bookingId = Integer.parseInt(bookingIdStr);
            ptBookingDAO.updateStatus(bookingId, BookingStatus.CONFIRMED);

            // Redirect back to service-schedule page
            resp.sendRedirect(req.getContextPath() + "/admin/service-schedule?success=confirmed");
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid booking ID");
        } catch (Exception e) {
            System.err.println("Error confirming booking: " + e.getMessage());
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to confirm booking");
        }
    }

    /**
     * Handle cancel booking action
     */
    private void handleCancelBooking(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String bookingIdStr = req.getParameter("bookingId");
            if (bookingIdStr == null || bookingIdStr.trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Booking ID is required");
                return;
            }

            Integer bookingId = Integer.parseInt(bookingIdStr);
            String reason = req.getParameter("reason");
            if (reason == null) {
                reason = "Cancelled by admin";
            }

            // Use cancelBooking method which sets status to CANCELLED and updates
            // timestamps
            ptBookingDAO.cancelBooking(bookingId, reason, CancelledBy.ADMIN);

            // Redirect back to service-schedule page
            resp.sendRedirect(req.getContextPath() + "/admin/service-schedule?success=cancelled");
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid booking ID");
        } catch (Exception e) {
            System.err.println("Error cancelling booking: " + e.getMessage());
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to cancel booking");
        }
    }

    @Override
    public String getServletInfo() {
        return "Admin Dashboard Servlet for GymFit Management System";
    }
}
