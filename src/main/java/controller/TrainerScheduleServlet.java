package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Trainer;
import model.schedule.BookingStatus;
import model.schedule.ExceptionType;
import service.schedule.TrainerScheduleService;

/**
 * TrainerScheduleServlet
 * -----------------------------------------------------
 * Điều hướng toàn bộ tính năng quản lý lịch PT (Personal Trainer):
 * 
 * 1️⃣ Hiển thị lịch làm việc (GET)
 * 2️⃣ Cập nhật trạng thái booking (POST: action=update)
 * 3️⃣ Thêm / Xóa ngày nghỉ hoặc bận (POST: action=exception / delete-exception)
 * 4️⃣ Hiển thị lịch làm việc cố định (Fixed Weekly Schedule)
 * -----------------------------------------------------
 */
@WebServlet(urlPatterns = {
    "/pt/schedule",
    "/pt/update-booking",
    "/pt/add-exception",
    "/pt/delete-exception",
    "/pt/clear-message"
})
public class TrainerScheduleServlet extends HttpServlet {

  private TrainerScheduleService scheduleService;

  @Override
  public void init() throws ServletException {
    super.init();
    try {
      scheduleService = new TrainerScheduleService();
    } catch (Exception e) {
      System.err.println("Error initializing TrainerScheduleService: " + e.getMessage());
      e.printStackTrace();
      throw new ServletException("Failed to initialize TrainerScheduleService", e);
    }
  }

  /**
   * ==============================
   * 📅 Hiển thị lịch làm việc PT
   * ==============================
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    HttpSession session = req.getSession();
    Trainer trainer = (Trainer) session.getAttribute("user");

    if (trainer == null) {
      resp.sendRedirect(req.getContextPath() + "/login");
      return;
    }

    int trainerId = trainer.getId();

    // Các dữ liệu hiện tại
    req.setAttribute("bookings", scheduleService.getTrainerBookings(trainerId));
    req.setAttribute("exceptions", scheduleService.getTrainerExceptions(trainerId));
    req.setAttribute("schedules", scheduleService.getWeeklySchedule(trainerId));
    req.setAttribute("timeSlots", scheduleService.getActiveTimeSlots());

    // =============================
    // 🧩 Lịch làm việc cố định (Weekly Fixed Schedule)
    // =============================
    LocalDate today = LocalDate.now();
    LocalDate monday = today.with(java.time.DayOfWeek.MONDAY);

    // Lấy danh sách ca giờ cơ bản
    List<Object[]> timeSlots = scheduleService.getTimeSlotsBasic();

    // Lấy dữ liệu lịch cố định gộp với booking trong tuần
    List<Object[]> fixedRows = scheduleService.getWeeklyFixedSchedule(trainerId, monday);

    // Map để tra cứu nhanh theo khóa: DAY#SLOT
    Map<String, Object[]> fixedMap = new HashMap<>();
    for (Object[] row : fixedRows) {
      // row: [0]=dayOfWeek, [1]=slotId, [2]=isAvailable, [3]=maxBookings,
      // [4]=notes, [5]=total, [6]=confirmed, [7]=pending
      fixedMap.put((String) row[0] + "#" + row[1], row);
    }

    req.setAttribute("weekStart", monday);
    req.setAttribute("weekEnd", monday.plusDays(6));
    req.setAttribute("fixedMap", fixedMap);
    req.setAttribute("fixedTimeSlots", timeSlots);

    // =============================
    // Forward sang JSP
    // =============================
    req.getRequestDispatcher("/views/PT/training_schedule.jsp").forward(req, resp);
  }

  /**
   * ==============================
   * ⚙️ Xử lý các hành động POST
   * ==============================
   */
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String action = req.getParameter("action");

    try {
      if ("update".equals(action)) {
        // ✅ Cập nhật trạng thái booking
        int bookingId = Integer.parseInt(req.getParameter("bookingId"));
        BookingStatus status = BookingStatus.valueOf(req.getParameter("status"));
        scheduleService.updateBookingStatus(bookingId, status);

      } else if ("exception".equals(action)) {
        // ✅ Thêm ngày nghỉ/bận
        int trainerId = Integer.parseInt(req.getParameter("trainerId"));
        LocalDate date = LocalDate.parse(req.getParameter("date"));
        int slotId = Integer.parseInt(req.getParameter("slotId"));
        ExceptionType type = ExceptionType.valueOf(req.getParameter("type"));
        String reason = req.getParameter("reason");

        scheduleService.addException(trainerId, date, slotId, type, reason);

        HttpSession session = req.getSession();
        session.setAttribute("addSuccess", "Đăng ký ngày nghỉ/bận thành công!");
        resp.sendRedirect(req.getContextPath() + "/pt/schedule");
        return;

      } else if ("delete-exception".equals(action)) {
        // ✅ Xóa ngày nghỉ/bận
        int exceptionId = Integer.parseInt(req.getParameter("exceptionId"));
        scheduleService.deleteException(exceptionId);

        HttpSession session = req.getSession();
        session.setAttribute("deleteSuccess", "Xóa ngày nghỉ/bận thành công!");
        resp.sendRedirect(req.getContextPath() + "/pt/schedule");
        return;

      } else if ("clear-delete-success".equals(action)) {
        // ✅ Xóa thông báo thành công (AJAX)
        HttpSession session = req.getSession();
        session.removeAttribute("deleteSuccess");
        resp.setStatus(HttpServletResponse.SC_OK);
        return;

      } else if ("clear-add-success".equals(action)) {
        // ✅ Xóa thông báo thành công (AJAX)
        HttpSession session = req.getSession();
        session.removeAttribute("addSuccess");
        resp.setStatus(HttpServletResponse.SC_OK);
        return;
      }

      // Mặc định: quay lại trang schedule
      resp.sendRedirect(req.getContextPath() + "/pt/schedule");

    } catch (Exception e) {
      e.printStackTrace();
      req.setAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
      req.getRequestDispatcher("/views/PT/training_schedule.jsp").forward(req, resp);
    }
  }
}
