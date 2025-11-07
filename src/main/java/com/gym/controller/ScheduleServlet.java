package com.gym.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import com.gym.dao.IScheduleDAO;
import com.gym.dao.IStudentDAO;
import com.gym.dao.IUserDAO;
import com.gym.dao.ScheduleDAO;
import com.gym.dao.StudentDAO;
import com.gym.dao.UserDAOImpl;
import com.gym.model.Schedule;
import com.gym.model.ScheduleStatus;
import com.gym.service.IScheduleService;
import com.gym.service.ScheduleService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ScheduleServlet", urlPatterns = { "/ScheduleServlet" })
public class ScheduleServlet extends HttpServlet {

  private IScheduleService scheduleService;

  @Override
  public void init() throws ServletException {
    // Manual wiring (no Spring)
    IScheduleDAO scheduleDAO = new ScheduleDAO();
    IStudentDAO studentDAO = new StudentDAO();
    this.scheduleService = new ScheduleService(scheduleDAO, studentDAO);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    String action = request.getParameter("action");
    if (action == null || action.isEmpty())
      action = "calendar";

    try {
      switch (action) {
        case "list":
          handleList(request, response);
          break;
        case "calendar":
          handleCalendar(request, response);
          break;
        case "updateStatus":
          handleUpdateStatus(request, response);
          break;
        case "confirm":
          handleQuickStatus(request, response, ScheduleStatus.confirmed);
          break;
        case "reject":
          handleQuickStatus(request, response, ScheduleStatus.rejected);
          break;
        case "cancel":
          handleQuickStatus(request, response, ScheduleStatus.cancelled);
          break;
        case "complete":
          handleQuickStatus(request, response, ScheduleStatus.completed);
          break;
        case "edit":
          handleEditOpen(request, response);
          break;
        case "delete":
          handleDelete(request, response);
          break;
        default:
          handleCalendar(request, response);
      }
    } catch (IllegalArgumentException ex) {
      // Business error -> show message
      request.setAttribute("error", ex.getMessage());
      forward(request, response, "/views/PT/training_schedule.jsp");
    } catch (Exception ex) {
      // Unexpected error -> clear message
      request.setAttribute("error", "Failed to process request: " + ex.getMessage());
      forward(request, response, "/views/PT/training_schedule.jsp");
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    String action = request.getParameter("action");
    if ("create".equals(action)) {
      handleCreate(request, response);
      return;
    }
    if ("update".equals(action)) {
      handleUpdate(request, response);
      return;
    }
    doGet(request, response);
  }

  private void handleList(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession(false);
    Long userId = session != null ? (Long) session.getAttribute("userId") : null;
    if (userId == null) {
      userId = 1L; // Fallback for dev
    }

    YearMonth now = YearMonth.from(LocalDate.now());
    int year = parseIntOrDefault(request.getParameter("year"), now.getYear());
    int month = parseIntOrDefault(request.getParameter("month"), now.getMonthValue());

    // Lấy danh sách lịch tập theo tháng của PT
    List<Schedule> schedules = scheduleService.findByTrainerAndMonth(userId.intValue(), year, month);

    // Build map schedulesByDate từ danh sách đã lọc theo trainer
    Map<String, List<Schedule>> schedulesByDate = buildSchedulesByDateMap(schedules, year, month);
    YearMonth ym = YearMonth.of(year, month);
    int daysInMonth = ym.lengthOfMonth();
    int firstDayOffset = (ym.atDay(1).getDayOfWeek().getValue() + 6) % 7; // Monday=0

    request.setAttribute("year", year);
    request.setAttribute("month", month);
    request.setAttribute("daysInMonth", daysInMonth);
    request.setAttribute("firstDayOffset", firstDayOffset);
    request.setAttribute("schedulesByDate", schedulesByDate);
    request.setAttribute("monthsWindow", scheduleService.getMonthsWindowAroundNow());
    request.setAttribute("schedules", schedules);

    // Check for highlight student name parameter
    String highlightStudentName = request.getParameter("highlightStudentName");
    if (highlightStudentName != null && !highlightStudentName.trim().isEmpty()) {
      request.setAttribute("highlightStudentName", highlightStudentName);
    }

    // Check for success message from session
    if (session != null) {
      String successMessage = (String) session.getAttribute("successMessage");
      if (successMessage != null && !successMessage.trim().isEmpty()) {
        request.setAttribute("successMessage", successMessage);
        // Tự động mở tab list khi có success message
        request.setAttribute("defaultView", "list");
        // Xóa message khỏi session sau khi đã lấy
        session.removeAttribute("successMessage");
      }
    }

    forward(request, response, "/views/PT/training_schedule.jsp");
  }

  private void handleCalendar(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession(false);
    Long userId = session != null ? (Long) session.getAttribute("userId") : null;
    if (userId == null) {
      userId = 1L; // Fallback for dev
    }

    YearMonth now = YearMonth.from(LocalDate.now());
    int year = parseIntOrDefault(request.getParameter("year"), now.getYear());
    int month = parseIntOrDefault(request.getParameter("month"), now.getMonthValue());

    // Lấy danh sách lịch tập theo tháng của PT
    List<Schedule> schedules = scheduleService.findByTrainerAndMonth(userId.intValue(), year, month);
    // Build map schedulesByDate từ danh sách đã lọc theo trainer
    Map<String, List<Schedule>> schedulesByDate = buildSchedulesByDateMap(schedules, year, month);

    YearMonth ym = YearMonth.of(year, month);
    int daysInMonth = ym.lengthOfMonth();
    int firstDayOffset = (ym.atDay(1).getDayOfWeek().getValue() + 6) % 7; // Monday=0

    request.setAttribute("year", year);
    request.setAttribute("month", month);
    request.setAttribute("daysInMonth", daysInMonth);
    request.setAttribute("firstDayOffset", firstDayOffset);
    request.setAttribute("schedulesByDate", schedulesByDate);
    request.setAttribute("monthsWindow", scheduleService.getMonthsWindowAroundNow());
    request.setAttribute("schedules", schedules);

    // Check for highlight student name parameter
    String highlightStudentName = request.getParameter("highlightStudentName");
    if (highlightStudentName != null && !highlightStudentName.trim().isEmpty()) {
      request.setAttribute("highlightStudentName", highlightStudentName);
    }

    // Check for success message from session (nếu có)
    if (session != null) {
      String successMessage = (String) session.getAttribute("successMessage");
      if (successMessage != null && !successMessage.trim().isEmpty()) {
        request.setAttribute("successMessage", successMessage);
        // Tự động mở tab list khi có success message
        request.setAttribute("defaultView", "list");
        // Xóa message khỏi session sau khi đã lấy
        session.removeAttribute("successMessage");
      }
    }

    forward(request, response, "/views/PT/training_schedule.jsp");
  }

  private void handleUpdateStatus(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    int id = Integer.parseInt(request.getParameter("id"));
    String statusStr = request.getParameter("status");
    ScheduleStatus status = ScheduleStatus.valueOf(statusStr);
    scheduleService.updateStatus(id, status);
    response.sendRedirect(request.getContextPath() + "/ScheduleServlet?action=calendar");
  }

  private void handleQuickStatus(HttpServletRequest request, HttpServletResponse response, ScheduleStatus status)
      throws IOException {
    int id = Integer.parseInt(request.getParameter("id"));
    Schedule s = scheduleService.getById(id);
    scheduleService.updateStatus(id, status);
    if (s != null && s.getTrainingDate() != null) {
      int year = s.getTrainingDate().getYear();
      int month = s.getTrainingDate().getMonthValue();
      response.sendRedirect(request.getContextPath()
          + "/ScheduleServlet?action=calendar&year=" + year + "&month=" + month
          + "&highlightDate=" + s.getTrainingDate().toString());
    } else {
      response.sendRedirect(request.getContextPath() + "/ScheduleServlet?action=calendar");
    }
  }

  private void handleDelete(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    int id = Integer.parseInt(request.getParameter("id"));
    scheduleService.delete(id);
    response.sendRedirect(request.getContextPath() + "/ScheduleServlet?action=list");
  }

  private void handleCreate(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String studentInput = request.getParameter("studentUsername");
    String dateStr = request.getParameter("trainingDate");
    String startStr = request.getParameter("startTime");
    String endStr = request.getParameter("endTime");
    String trainingType = request.getParameter("trainingType");
    String location = request.getParameter("location");
    String note = request.getParameter("note");

    try {
      // Bước 1: Tìm User theo username hoặc name, với role = 'User' và status =
      // 'ACTIVE'
      IUserDAO userDAO = new UserDAOImpl();
      java.util.Optional<com.gym.model.User> userOpt = userDAO.findByUsernameOrNameAndRoleAndStatus(studentInput);

      if (!userOpt.isPresent()) {
        request.setAttribute("errorMessage", "Học viên không tồn tại hoặc đã bị vô hiệu hóa");
        handleCalendar(request, response);
        return;
      }

      com.gym.model.User user = userOpt.get();

      // Bước 2: Tìm Student theo userId
      IStudentDAO studentDAO = new StudentDAO();
      java.util.Optional<com.gym.model.Student> studentOpt = studentDAO.findByUserId(user.getId());

      if (!studentOpt.isPresent()) {
        request.setAttribute("errorMessage", "Không tìm thấy thông tin học viên");
        handleCalendar(request, response);
        return;
      }

      com.gym.model.Student student = studentOpt.get();

      // Bước 3: Parse date và time với formatter rõ ràng
      // LocalDate và LocalTime không có timezone, nên parse trực tiếp từ string
      // Đảm bảo format từ form: yyyy-MM-dd cho date, HH:mm cho time
      if (dateStr == null || dateStr.trim().isEmpty()) {
        throw new IllegalArgumentException("Ngày tập là bắt buộc");
      }
      if (startStr == null || startStr.trim().isEmpty()) {
        throw new IllegalArgumentException("Giờ bắt đầu là bắt buộc");
      }
      if (endStr == null || endStr.trim().isEmpty()) {
        throw new IllegalArgumentException("Giờ kết thúc là bắt buộc");
      }

      LocalDate trainingDate = LocalDate.parse(dateStr.trim()); // yyyy-MM-dd (ISO format)
      // Parse time với format HH:mm (input type="time" gửi format này, không có
      // timezone)
      java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");
      LocalTime startTime = LocalTime.parse(startStr.trim(), timeFormatter);
      LocalTime endTime = LocalTime.parse(endStr.trim(), timeFormatter);

      // Validate: endTime phải sau startTime
      if (!endTime.isAfter(startTime)) {
        throw new IllegalArgumentException("Giờ kết thúc phải sau giờ bắt đầu");
      }

      // Bước 4: Tạo Schedule và lưu
      Schedule s = new Schedule();
      s.setStudent(student);
      s.setTrainingDate(trainingDate);
      s.setStartTime(startTime);
      s.setEndTime(endTime);
      s.setTrainingType(trainingType);
      s.setLocation(location);
      s.setNote(note);
      s.setStatus(com.gym.model.ScheduleStatus.pending);

      scheduleService.create(s);

      // Redirect về action=list với year/month của trainingDate để hiển thị đúng
      // tháng
      int year = trainingDate.getYear();
      int month = trainingDate.getMonthValue();
      response.sendRedirect(
          request.getContextPath() + "/ScheduleServlet?action=calendar&year=" + year + "&month=" + month
              + "&highlightDate=" + trainingDate.toString());
    } catch (IllegalArgumentException ex) {
      request.setAttribute("errorMessage", ex.getMessage());
      handleCalendar(request, response);
    } catch (Exception ex) {
      request.setAttribute("errorMessage", "Có lỗi xảy ra: " + ex.getMessage());
      handleCalendar(request, response);
    }
  }

  private void handleEditOpen(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    int id = Integer.parseInt(request.getParameter("id"));
    Schedule s = scheduleService.getById(id);
    if (s == null) {
      request.setAttribute("errorMessage", "Không tìm thấy buổi tập");
      // Kiểm tra xem đang ở tab nào
      String sourceView = request.getParameter("sourceView");
      if ("list".equals(sourceView)) {
        handleList(request, response);
      } else {
        handleCalendar(request, response);
      }
      return;
    }
    request.setAttribute("editSchedule", s);
    request.setAttribute("openEditModal", Boolean.TRUE);

    // Lưu sourceView để biết đang ở tab nào
    String sourceView = request.getParameter("sourceView");
    if (sourceView == null) {
      // Mặc định là calendar nếu không có sourceView
      sourceView = "calendar";
    }
    request.setAttribute("sourceView", sourceView);

    HttpSession session = request.getSession(false);
    Long userId = session != null ? (Long) session.getAttribute("userId") : null;
    if (userId == null) {
      userId = 1L;
    }
    YearMonth now = YearMonth.from(LocalDate.now());
    int year = parseIntOrDefault(request.getParameter("year"),
        s.getTrainingDate() != null ? s.getTrainingDate().getYear() : now.getYear());
    int month = parseIntOrDefault(request.getParameter("month"),
        s.getTrainingDate() != null ? s.getTrainingDate().getMonthValue() : now.getMonthValue());
    List<Schedule> schedules = scheduleService.findByTrainerAndMonth(userId.intValue(), year, month);
    Map<String, List<Schedule>> schedulesByDate = buildSchedulesByDateMap(schedules, year, month);
    YearMonth ym = YearMonth.of(year, month);
    int daysInMonth = ym.lengthOfMonth();
    int firstDayOffset = (ym.atDay(1).getDayOfWeek().getValue() + 6) % 7;
    request.setAttribute("year", year);
    request.setAttribute("month", month);
    request.setAttribute("daysInMonth", daysInMonth);
    request.setAttribute("firstDayOffset", firstDayOffset);
    request.setAttribute("schedulesByDate", schedulesByDate);
    request.setAttribute("monthsWindow", scheduleService.getMonthsWindowAroundNow());
    request.setAttribute("schedules", schedules);
    forward(request, response, "/views/PT/training_schedule.jsp");
  }

  private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String scheduleIdStr = request.getParameter("scheduleId");
    String sourceView = request.getParameter("sourceView");
    if (sourceView == null) {
      sourceView = "calendar"; // Mặc định
    }

    if (scheduleIdStr == null || scheduleIdStr.trim().isEmpty()) {
      request.setAttribute("errorMessage", "Không tìm thấy buổi tập cần cập nhật");
      if ("list".equals(sourceView)) {
        handleList(request, response);
      } else {
        handleCalendar(request, response);
      }
      return;
    }
    int scheduleId = Integer.parseInt(scheduleIdStr);
    Schedule existing = scheduleService.getById(scheduleId);
    if (existing == null) {
      request.setAttribute("errorMessage", "Không tìm thấy buổi tập");
      if ("list".equals(sourceView)) {
        handleList(request, response);
      } else {
        handleCalendar(request, response);
      }
      return;
    }

    String studentInput = request.getParameter("studentUsername");
    String dateStr = request.getParameter("trainingDate");
    String startStr = request.getParameter("startTime");
    String endStr = request.getParameter("endTime");
    String trainingType = request.getParameter("trainingType");
    String location = request.getParameter("location");
    String note = request.getParameter("note");

    try {
      // Tìm lại student nếu username thay đổi
      if (studentInput != null && !studentInput.trim().isEmpty()
          && !studentInput.equals(existing.getStudent().getUser().getName())) {
        IUserDAO userDAO = new UserDAOImpl();
        java.util.Optional<com.gym.model.User> userOpt = userDAO
            .findByUsernameOrNameAndRoleAndStatus(studentInput.trim());
        if (!userOpt.isPresent()) {
          request.setAttribute("errorMessage", "Học viên không tồn tại hoặc đã bị vô hiệu hóa");
          request.setAttribute("editSchedule", existing);
          request.setAttribute("openEditModal", Boolean.TRUE);
          request.setAttribute("sourceView", sourceView);
          if ("list".equals(sourceView)) {
            handleList(request, response);
          } else {
            handleCalendar(request, response);
          }
          return;
        }
        IStudentDAO studentDAO = new StudentDAO();
        java.util.Optional<com.gym.model.Student> studentOpt = studentDAO.findByUserId(userOpt.get().getId());
        if (!studentOpt.isPresent()) {
          request.setAttribute("errorMessage", "Không tìm thấy thông tin học viên");
          request.setAttribute("editSchedule", existing);
          request.setAttribute("openEditModal", Boolean.TRUE);
          request.setAttribute("sourceView", sourceView);
          if ("list".equals(sourceView)) {
            handleList(request, response);
          } else {
            handleCalendar(request, response);
          }
          return;
        }
        existing.setStudent(studentOpt.get());
      }

      // Update fields
      if (dateStr != null && !dateStr.trim().isEmpty()) {
        existing.setTrainingDate(LocalDate.parse(dateStr.trim()));
      }
      if (startStr != null && !startStr.trim().isEmpty()) {
        java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");
        existing.setStartTime(LocalTime.parse(startStr.trim(), timeFormatter));
      }
      if (endStr != null && !endStr.trim().isEmpty()) {
        java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");
        existing.setEndTime(LocalTime.parse(endStr.trim(), timeFormatter));
      }
      if (endStr != null && startStr != null && !endStr.trim().isEmpty() && !startStr.trim().isEmpty()) {
        java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");
        LocalTime startTime = LocalTime.parse(startStr.trim(), timeFormatter);
        LocalTime endTime = LocalTime.parse(endStr.trim(), timeFormatter);
        if (!endTime.isAfter(startTime)) {
          throw new IllegalArgumentException("Giờ kết thúc phải sau giờ bắt đầu");
        }
      }
      existing.setTrainingType(trainingType);
      existing.setLocation(location);
      existing.setNote(note);

      scheduleService.update(existing);

      // Redirect về đúng tab dựa vào sourceView
      LocalDate trainingDate = existing.getTrainingDate();
      int year = trainingDate.getYear();
      int month = trainingDate.getMonthValue();

      if ("list".equals(sourceView)) {
        // Lưu thông báo thành công vào session để tránh lỗi encoding
        HttpSession session = request.getSession(true);
        String studentName = existing.getStudent().getUser().getName();
        String successMsg = "Cập nhật thông tin " + studentName + " thành công";
        session.setAttribute("successMessage", successMsg);
        // Redirect về tab Danh sách
        response
            .sendRedirect(request.getContextPath() + "/ScheduleServlet?action=list&year=" + year + "&month=" + month);
      } else {
        // Redirect về tab Lịch
        response
            .sendRedirect(request.getContextPath() + "/ScheduleServlet?action=calendar&year=" + year + "&month=" + month
                + "&highlightDate=" + trainingDate.toString());
      }
    } catch (IllegalArgumentException ex) {
      request.setAttribute("errorMessage", ex.getMessage());
      request.setAttribute("editSchedule", existing);
      request.setAttribute("openEditModal", Boolean.TRUE);
      request.setAttribute("sourceView", sourceView);
      if ("list".equals(sourceView)) {
        handleList(request, response);
      } else {
        handleCalendar(request, response);
      }
    } catch (Exception ex) {
      request.setAttribute("errorMessage", "Có lỗi xảy ra: " + ex.getMessage());
      request.setAttribute("editSchedule", existing);
      request.setAttribute("openEditModal", Boolean.TRUE);
      request.setAttribute("sourceView", sourceView);
      if ("list".equals(sourceView)) {
        handleList(request, response);
      } else {
        handleCalendar(request, response);
      }
    }
  }

  private void forward(HttpServletRequest request, HttpServletResponse response, String path)
      throws ServletException, IOException {
    request.getRequestDispatcher(path).forward(request, response);
  }

  private int parseIntOrDefault(String value, int def) {
    try {
      return value == null ? def : Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return def;
    }
  }

  private Map<String, List<Schedule>> buildSchedulesByDateMap(List<Schedule> schedules, int year, int month) {
    java.time.YearMonth ym = java.time.YearMonth.of(year, month);
    java.time.LocalDate start = ym.atDay(1);
    java.time.LocalDate end = ym.atEndOfMonth();

    Map<String, List<Schedule>> byDate = new java.util.LinkedHashMap<>();
    // Khởi tạo tất cả các ngày trong tháng
    for (java.time.LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
      byDate.put(d.toString(), new java.util.ArrayList<>());
    }
    // Gán schedules vào đúng ngày
    for (Schedule s : schedules) {
      if (s.getTrainingDate() != null) {
        String key = s.getTrainingDate().toString();
        if (byDate.containsKey(key)) {
          byDate.get(key).add(s);
        }
      }
    }
    return byDate;
  }
}
