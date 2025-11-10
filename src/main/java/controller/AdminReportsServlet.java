package controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ReportService;

import java.io.IOException;
import java.util.List;
import model.report.ChartData;
import model.report.ReportSummary;

@WebServlet( urlPatterns = "/admin/reports")
public class AdminReportsServlet extends HttpServlet {

    private final ReportService reportService = new ReportService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ReportSummary summary = reportService.getSummaryStats();
        request.setAttribute("summary", summary);

        List<ChartData> revenueData = reportService.getRevenueChartData();
        List<ChartData> packageData = reportService.getPackageRevenueChartData();

        request.setAttribute("revenueChartJson", gson.toJson(revenueData));
        request.setAttribute("packageChartJson", gson.toJson(packageData));

        request.getRequestDispatcher("/views/admin/reports.jsp").forward(request, response);
    }
}