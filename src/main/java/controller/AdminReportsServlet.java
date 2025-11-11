package controller;

import com.google.gson.Gson;
import dao.PaymentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ReportService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import model.report.ChartData;
import model.report.ReportSummary;

@WebServlet( urlPatterns = "/admin/reports")
public class AdminReportsServlet extends HttpServlet {

    private final ReportService reportService = new ReportService();
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get summary stats from ReportService
        ReportSummary summary = reportService.getSummaryStats();
        request.setAttribute("summary", summary);

        // Get chart data
        List<ChartData> revenueData = reportService.getRevenueChartData();
        List<ChartData> packageData = reportService.getPackageRevenueChartData();
        List<ChartData> memberData = reportService.getMemberGrowthChartData();

        request.setAttribute("revenueChartJson", gson.toJson(revenueData));
        request.setAttribute("packageChartJson", gson.toJson(packageData));
        request.setAttribute("memberChartJson", gson.toJson(memberData));

        // Calculate revenue statistics from PaymentDAO
        BigDecimal revenueToday = paymentDAO.getRevenueToday();
        BigDecimal revenueYesterday = paymentDAO.getRevenueYesterday();
        BigDecimal revenueThisMonth = paymentDAO.getRevenueThisMonth();
        BigDecimal revenueLastMonth = paymentDAO.getRevenueLastMonth();
        BigDecimal revenueThisYear = paymentDAO.getRevenueThisYear();
        BigDecimal revenueLastYear = paymentDAO.getRevenueLastYear();

        // Calculate growth rates
        double todayGrowthRate = calculateGrowthRate(revenueToday, revenueYesterday);
        double monthGrowthRate = calculateGrowthRate(revenueThisMonth, revenueLastMonth);
        double yearGrowthRate = calculateGrowthRate(revenueThisYear, revenueLastYear);

        // Set revenue statistics as request attributes
        request.setAttribute("revenueToday", revenueToday);
        request.setAttribute("revenueYesterday", revenueYesterday);
        request.setAttribute("revenueThisMonth", revenueThisMonth);
        request.setAttribute("revenueLastMonth", revenueLastMonth);
        request.setAttribute("revenueThisYear", revenueThisYear);
        request.setAttribute("revenueLastYear", revenueLastYear);
        request.setAttribute("todayGrowthRate", todayGrowthRate);
        request.setAttribute("monthGrowthRate", monthGrowthRate);
        request.setAttribute("yearGrowthRate", yearGrowthRate);

        request.getRequestDispatcher("/views/admin/reports.jsp").forward(request, response);
    }

    /**
     * Calculate growth rate percentage between two values
     * @param current Current value
     * @param previous Previous value
     * @return Growth rate as double (percentage)
     */
    private double calculateGrowthRate(BigDecimal current, BigDecimal previous) {
        if (previous == null || previous.compareTo(BigDecimal.ZERO) == 0) {
            return current != null && current.compareTo(BigDecimal.ZERO) > 0 ? 100.0 : 0.0;
        }
        if (current == null) {
            return 0.0;
        }
        BigDecimal growth = current.subtract(previous)
                .divide(previous, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        return growth.setScale(1, RoundingMode.HALF_UP).doubleValue();
    }
}