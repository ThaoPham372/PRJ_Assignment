package com.gym.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.gym.dao.IScheduleDAO;
import com.gym.dao.ScheduleDAO;
import com.gym.model.Schedule;
import com.gym.model.Trainer;
import com.gym.service.ITrainerService;
import com.gym.service.TrainerServiceImpl;
import com.gym.util.ReportExportUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * TrainerReportServlet - Servlet điều hướng đến trang báo cáo trainer
 * Chỉ điều hướng và gọi service, không chứa logic xử lý
 */
@WebServlet(name = "TrainerReportServlet", urlPatterns = { "/trainer/report" })
public class TrainerReportServlet extends HttpServlet {

  private ITrainerService trainerService;
  private IScheduleDAO scheduleDAO;

  @Override
  public void init() throws ServletException {
    super.init();
    this.trainerService = new TrainerServiceImpl();
    this.scheduleDAO = new ScheduleDAO();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");

    // Check if export request
    String exportType = request.getParameter("export");
    if (exportType != null && (exportType.equals("excel") || exportType.equals("pdf"))) {
      handleExport(request, response, exportType);
      return;
    }

    response.setContentType("text/html; charset=UTF-8");

    HttpSession session = request.getSession(false);

    // Check authentication
    if (session == null || session.getAttribute("isLoggedIn") == null) {
      response.sendRedirect(request.getContextPath() + "/views/login.jsp");
      return;
    }

    // Check PT role
    @SuppressWarnings("unchecked")
    java.util.List<String> userRoles = (java.util.List<String>) session.getAttribute("userRoles");
    if (userRoles == null || !userRoles.contains("PT")) {
      response.sendRedirect(request.getContextPath() + "/home");
      return;
    }

    try {
      // Lấy tham số từ request
      String fromDateParam = request.getParameter("fromDate");
      String toDateParam = request.getParameter("toDate");
      String reportType = request.getParameter("reportType");
      String packageName = request.getParameter("packageName"); // Filter theo gói tập
      String trainingType = request.getParameter("trainingType"); // Filter theo loại hình tập

      // Parse dates
      LocalDate fromDate = null;
      LocalDate toDate = null;
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

      if (fromDateParam != null && !fromDateParam.trim().isEmpty()) {
        try {
          fromDate = LocalDate.parse(fromDateParam, formatter);
        } catch (DateTimeParseException e) {
          System.err.println("Error parsing fromDate: " + e.getMessage());
        }
      }

      if (toDateParam != null && !toDateParam.trim().isEmpty()) {
        try {
          toDate = LocalDate.parse(toDateParam, formatter);
        } catch (DateTimeParseException e) {
          System.err.println("Error parsing toDate: " + e.getMessage());
        }
      }

      // Set default dates if not provided (current month)
      if (fromDate == null || toDate == null) {
        LocalDate now = LocalDate.now();
        fromDate = now.withDayOfMonth(1);
        toDate = now;
      }

      // Lấy trainerId từ session (giả sử có userId trong session)
      Integer trainerId = null;
      Object userIdObj = session.getAttribute("userId");
      if (userIdObj != null) {
        if (userIdObj instanceof Integer) {
          trainerId = (Integer) userIdObj;
        } else if (userIdObj instanceof Long) {
          trainerId = ((Long) userIdObj).intValue();
        }
      }

      // Nếu không có trainerId, lấy từ trainer đầu tiên (fallback)
      if (trainerId == null) {
        Trainer firstTrainer = trainerService.getTrainerById(1); // Fallback
        if (firstTrainer != null) {
          trainerId = firstTrainer.getUserId();
        }
      }

      // Gọi service để lấy thống kê
      if (trainerId != null) {
        // Sử dụng filter nếu có
        int completedSessions = (packageName != null && !packageName.trim().isEmpty())
            || (trainingType != null && !trainingType.trim().isEmpty())
                ? trainerService.countCompletedSessionsWithFilter(trainerId, fromDate, toDate, packageName,
                    trainingType)
                : trainerService.countCompletedSessions(trainerId, fromDate, toDate);
        int cancelledSessions = (packageName != null && !packageName.trim().isEmpty())
            || (trainingType != null && !trainingType.trim().isEmpty())
                ? trainerService.countCancelledSessionsWithFilter(trainerId, fromDate, toDate, packageName,
                    trainingType)
                : trainerService.countCancelledSessions(trainerId, fromDate, toDate);
        float completionRate = trainerService.calculateCompletionRate(trainerId, fromDate, toDate);
        float averageRating = trainerService.calculateAverageRating(trainerId, fromDate, toDate);
        Map<String, Long> trainingTypeDistribution = trainerService.getTrainingTypeDistribution(trainerId, fromDate,
            toDate);
        Map<java.time.YearMonth, Long> monthlySessionCount = trainerService.getMonthlySessionCount(trainerId);
        Map<Integer, Float> weeklyCompletionRate = trainerService.getWeeklyCompletionRate(trainerId);

        // Lấy đánh giá theo tháng trong năm hiện tại
        Map<java.time.YearMonth, Float> monthlyAverageRating = trainerService.getMonthlyAverageRating(trainerId,
            LocalDate.now().getYear());

        // Lấy performance trends
        List<com.gym.dto.PerformanceTrendDTO> weeklyTrends = trainerService.getWeeklyPerformanceTrend(trainerId, 8);
        List<com.gym.dto.PerformanceTrendDTO> monthlyTrends = trainerService.getMonthlyPerformanceTrend(trainerId, 6);

        // Lấy danh hiệu
        List<com.gym.dto.TrainerAwardDTO> awards = trainerService.getTrainerAwards(trainerId);

        // Lấy trainer info
        Trainer trainer = trainerService.getTrainerById(trainerId);
        int totalStudents = trainer != null && trainer.getStudentsCount() != null ? trainer.getStudentsCount() : 0;

        // Lấy recent sessions (10 sessions gần nhất của trainer)
        List<Schedule> recentSessions = null;
        try {
          // Sử dụng native query để lấy recent sessions theo trainer_id
          // Tạm thời lấy từ date range và filter sau, hoặc có thể thêm method mới vào DAO
          LocalDate recentStart = LocalDate.now().minusMonths(1);
          List<Schedule> allRecent = scheduleDAO.findByDateRange(recentStart, LocalDate.now());

          // Filter theo trainer_id nếu có (giả sử Schedule có trainerId field sau này)
          // Hoặc có thể thêm method findByTrainerId vào DAO
          recentSessions = allRecent.stream()
              .limit(10)
              .collect(Collectors.toList());
        } catch (Exception e) {
          System.err.println("Error getting recent sessions: " + e.getMessage());
          recentSessions = new java.util.ArrayList<>();
        }

        // Lưu kết quả vào request attributes
        request.setAttribute("trainerId", trainerId);
        request.setAttribute("completedSessions", completedSessions);
        request.setAttribute("cancelledSessions", cancelledSessions);
        request.setAttribute("completionRate", completionRate);
        request.setAttribute("averageRating", averageRating);
        request.setAttribute("totalStudents", totalStudents);
        request.setAttribute("trainingTypeDistribution", trainingTypeDistribution);
        request.setAttribute("monthlySessionCount", monthlySessionCount);
        request.setAttribute("weeklyCompletionRate", weeklyCompletionRate);
        request.setAttribute("recentSessions", recentSessions);
        request.setAttribute("monthlyAverageRating", monthlyAverageRating);
        request.setAttribute("weeklyTrends", weeklyTrends);
        request.setAttribute("monthlyTrends", monthlyTrends);
        request.setAttribute("awards", awards);
      }

      // Lưu các attributes khác
      List<Trainer> trainerStats = trainerService.getTrainerStatistics(fromDate, toDate);
      request.setAttribute("trainerStats", trainerStats);
      request.setAttribute("fromDate", fromDate);
      request.setAttribute("toDate", toDate);
      request.setAttribute("reportType", reportType);
      request.setAttribute("packageName", packageName);
      request.setAttribute("trainingType", trainingType);

      // Forward tới JSP
      request.getRequestDispatcher("/views/PT/reports.jsp").forward(request, response);

    } catch (Exception e) {
      System.err.println("Error in TrainerReportServlet: " + e.getMessage());
      e.printStackTrace();
      request.setAttribute("errorMessage", "Có lỗi xảy ra khi tải báo cáo: " + e.getMessage());
      request.getRequestDispatcher("/views/PT/reports.jsp").forward(request, response);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // POST requests cũng xử lý giống GET
    doGet(request, response);
  }

  /**
   * Xử lý export báo cáo ra PDF hoặc Excel
   */
  private void handleExport(HttpServletRequest request, HttpServletResponse response, String exportType)
      throws ServletException, IOException {
    HttpSession session = request.getSession(false);

    // Check authentication
    if (session == null || session.getAttribute("isLoggedIn") == null) {
      response.sendRedirect(request.getContextPath() + "/views/login.jsp");
      return;
    }

    try {
      // Lấy trainerId từ session
      Integer trainerId = null;
      Object userIdObj = session.getAttribute("userId");
      if (userIdObj != null) {
        if (userIdObj instanceof Integer) {
          trainerId = (Integer) userIdObj;
        } else if (userIdObj instanceof Long) {
          trainerId = ((Long) userIdObj).intValue();
        }
      }

      if (trainerId == null) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Trainer ID not found");
        return;
      }

      // Parse dates
      String fromDateParam = request.getParameter("fromDate");
      String toDateParam = request.getParameter("toDate");
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

      LocalDate fromDate = null;
      LocalDate toDate = null;

      if (fromDateParam != null && !fromDateParam.trim().isEmpty()) {
        fromDate = LocalDate.parse(fromDateParam, formatter);
      }
      if (toDateParam != null && !toDateParam.trim().isEmpty()) {
        toDate = LocalDate.parse(toDateParam, formatter);
      }

      if (fromDate == null || toDate == null) {
        LocalDate now = LocalDate.now();
        fromDate = now.withDayOfMonth(1);
        toDate = now;
      }

      // Lấy dữ liệu
      Trainer trainer = trainerService.getTrainerById(trainerId);
      int completedSessions = trainerService.countCompletedSessions(trainerId, fromDate, toDate);
      int cancelledSessions = trainerService.countCancelledSessions(trainerId, fromDate, toDate);
      float completionRate = trainerService.calculateCompletionRate(trainerId, fromDate, toDate);
      float averageRating = trainerService.calculateAverageRating(trainerId, fromDate, toDate);
      Trainer trainerInfo = trainerService.getTrainerById(trainerId);
      int totalStudents = trainerInfo != null && trainerInfo.getStudentsCount() != null
          ? trainerInfo.getStudentsCount()
          : 0;

      // Lấy recent sessions
      LocalDate recentStart = LocalDate.now().minusMonths(1);
      List<Schedule> recentSessions = scheduleDAO.findByDateRange(recentStart, LocalDate.now()).stream()
          .limit(10)
          .collect(Collectors.toList());

      Map<java.time.YearMonth, Long> monthlyData = trainerService.getMonthlySessionCount(trainerId);
      Map<String, Long> trainingTypeData = trainerService.getTrainingTypeDistribution(trainerId, fromDate, toDate);

      // Set response headers
      String fileName = "trainer_report_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
      if (exportType.equals("excel")) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".xlsx\"");
      } else if (exportType.equals("pdf")) {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".pdf\"");
      }

      OutputStream outputStream = response.getOutputStream();

      if (exportType.equals("excel")) {
        ReportExportUtil.exportToExcel(trainer, completedSessions, cancelledSessions, completionRate, averageRating,
            totalStudents, recentSessions, monthlyData, trainingTypeData, outputStream);
      } else if (exportType.equals("pdf")) {
        ReportExportUtil.exportToPDF(trainer, completedSessions, cancelledSessions, completionRate, averageRating,
            totalStudents, recentSessions, monthlyData, trainingTypeData, outputStream);
      }

      outputStream.flush();
      outputStream.close();

    } catch (Exception e) {
      System.err.println("Error exporting report: " + e.getMessage());
      e.printStackTrace();
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error exporting report");
    }
  }
}
