package com.gym.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gym.dao.UserDAO;
import com.gym.model.Schedule;
import com.gym.model.User;
import com.gym.service.ScheduleService;
import com.gym.service.ScheduleServiceImpl;

@WebServlet("/ScheduleServlet")
public class ScheduleServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private ScheduleService scheduleService = new ScheduleServiceImpl();

  // Lấy danh sách học viên cho dropdown
  private void provideUsersList(HttpServletRequest req) {
    UserDAO userDAO = new UserDAO();
    List<User> users = userDAO.getAllUsers();
    req.setAttribute("users", users);
  }

  // Build redirect URL preserving month/year parameters if present
  private String buildRedirectWithMonthYear(HttpServletRequest req, String baseUrl) {
    String month = req.getParameter("month");
    String year = req.getParameter("year");
    if (month != null && year != null && !month.isEmpty() && !year.isEmpty()) {
      return baseUrl + "&month=" + month + "&year=" + year;
    }
    return baseUrl;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String action = req.getParameter("action");
    String idStr = req.getParameter("id");
    try {
      if (action == null || action.equals("list")) {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        req.setAttribute("schedules", schedules);
        provideUsersList(req);
        provideCalendarAttributes(req, schedules);

        // Xử lý view parameter (calendar hoặc list)
        String viewParam = req.getParameter("view");
        if (viewParam != null) {
          req.setAttribute("defaultView", viewParam);
        }

        // Xử lý user filter parameter (để highlight trên calendar)
        String userParam = req.getParameter("user");
        if (userParam != null) {
          try {
            int userId = Integer.parseInt(userParam);
            req.setAttribute("highlightUserId", userId);
            // Lấy username để hiển thị indicator
            @SuppressWarnings("unchecked")
            List<User> users = (List<User>) req.getAttribute("users");
            if (users != null) {
              for (User u : users) {
                if ((int) u.getId() == userId) {
                  req.setAttribute("highlightUserName", u.getUsername());
                  break;
                }
              }
            }
          } catch (NumberFormatException e) {
            // Ignore invalid user parameter
          }
        }

        req.getRequestDispatcher("/views/PT/training_schedule.jsp").forward(req, resp);
      } else if (action.equals("edit")) {
        int id = Integer.parseInt(idStr);
        Schedule s = scheduleService.getScheduleById(id);
        req.setAttribute("editSchedule", s);
        List<Schedule> schedules = scheduleService.getAllSchedules();
        req.setAttribute("schedules", schedules);
        provideUsersList(req);
        provideCalendarAttributes(req, schedules);
        // Giữ nguyên ở tab Danh sách
        req.setAttribute("defaultView", "list");
        req.getRequestDispatcher("/views/PT/training_schedule.jsp").forward(req, resp);
      } else if (action.equals("delete")) {
        int id = Integer.parseInt(idStr);
        scheduleService.deleteSchedule(id);
        // Giữ nguyên ở tab Danh sách
        String redirectUrl = buildRedirectWithMonthYear(req, "ScheduleServlet?action=list&view=list");
        resp.sendRedirect(redirectUrl);
      } else if (action.equals("cancel") || action.equals("confirm") || action.equals("complete")
          || action.equals("reject")) {
        int id = Integer.parseInt(idStr);
        // Lấy thông tin schedule để có userId
        Schedule schedule = scheduleService.getScheduleById(id);
        String status = "pending";
        switch (action) {
          case "cancel":
            status = "cancelled";
            break;
          case "confirm":
            status = "confirmed";
            break;
          case "complete":
            status = "completed";
            break;
          case "reject":
            status = "rejected";
            break;
        }
        scheduleService.updateStatus(id, status);
        // Redirect về tab Lịch với user parameter
        String redirectUrl = buildRedirectWithMonthYear(req, "ScheduleServlet?action=list&view=calendar");
        if (schedule != null) {
          redirectUrl += "&user=" + schedule.getUserId();
        }
        resp.sendRedirect(redirectUrl);
      }
    } catch (Exception e) {
      req.setAttribute("error", e.getMessage());
      provideUsersList(req);
      try {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        req.setAttribute("schedules", schedules);
        provideCalendarAttributes(req, schedules);
      } catch (Exception ignore) {
      }
      req.getRequestDispatcher("/views/PT/training_schedule.jsp").forward(req, resp);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String action = req.getParameter("action");
    // Log để debug
    System.out.println("[ScheduleServlet] POST request - action: " + action);
    try {
      if (action == null) {
        resp.sendRedirect("ScheduleServlet");
        return;
      }
      String redirectUrl = "ScheduleServlet?action=list";
      if (action.equals("create")) {
        System.out.println("[ScheduleServlet] Creating new schedule...");
        Schedule s = parseScheduleFromRequest(req, null);
        s.setStatus("pending");
        scheduleService.createSchedule(s);
        // Redirect to month of created schedule
        java.sql.Date trainingDate = s.getTrainingDate();
        if (trainingDate != null) {
          LocalDate date = trainingDate.toLocalDate();
          redirectUrl += "&month=" + date.getMonthValue() + "&year=" + date.getYear();
        }
      } else if (action.equals("update")) {
        System.out.println("[ScheduleServlet] Updating schedule...");
        int id = Integer.parseInt(req.getParameter("id"));
        Schedule existing = scheduleService.getScheduleById(id);
        Schedule s = parseScheduleFromRequest(req, existing);
        s.setId(id);
        // Preserve old status - do not change status on edit form
        if (existing != null) {
          s.setStatus(existing.getStatus());
        }
        scheduleService.updateSchedule(s);
        System.out.println("[ScheduleServlet] Schedule updated successfully");
        // Redirect về tab Danh sách (không phải Lịch)
        redirectUrl = "ScheduleServlet?action=list&view=list";
        // Redirect to month of updated schedule
        java.sql.Date trainingDate = s.getTrainingDate();
        if (trainingDate != null) {
          LocalDate date = trainingDate.toLocalDate();
          redirectUrl += "&month=" + date.getMonthValue() + "&year=" + date.getYear();
        }
      }
      resp.sendRedirect(redirectUrl);
    } catch (Exception e) {
      // Log lỗi để debug
      System.err.println("[ScheduleServlet] Error: " + e.getMessage());
      e.printStackTrace();
      // Preserve form values and show modal again with error
      req.setAttribute("error", e.getMessage());
      req.setAttribute("showCreateModal", Boolean.TRUE);
      req.setAttribute("form_user_name", req.getParameter("user_name"));
      req.setAttribute("form_training_date", req.getParameter("training_date"));
      req.setAttribute("form_start_time", req.getParameter("start_time"));
      req.setAttribute("form_end_time", req.getParameter("end_time"));
      req.setAttribute("form_training_type", req.getParameter("training_type"));
      req.setAttribute("form_location", req.getParameter("location"));
      req.setAttribute("form_note", req.getParameter("note"));
      provideUsersList(req);
      try {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        req.setAttribute("schedules", schedules);
      } catch (Exception ignore) {
      }
      req.getRequestDispatcher("/views/PT/training_schedule.jsp").forward(req, resp);
    }
  }

  // Mapping dữ liệu form về POJO Schedule (create/update). existingSchedule có
  // thể null khi create
  private Schedule parseScheduleFromRequest(HttpServletRequest req, Schedule existingSchedule) throws ServletException {
    Schedule s = new Schedule();
    String inputUserName = req.getParameter("user_name");
    if (inputUserName == null || inputUserName.trim().isEmpty()) {
      throw new IllegalArgumentException("Học viên không tồn tại hoặc đã bị vô hiệu hóa");
    }
    UserDAO userDAO = new UserDAO();
    User matched = userDAO.getUserByNameAndStatus(inputUserName.trim(), "ACTIVE");
    if (matched == null) {
      throw new IllegalArgumentException("Học viên không tồn tại hoặc đã bị vô hiệu hóa");
    }
    s.setUserId((int) matched.getId());

    // trainer_id đã bị xóa khỏi database, không cần set nữa
    // Code cũ đã được comment để tương thích với Model class
    /*
     * if (existingSchedule != null) {
     * s.setTrainerId(existingSchedule.getTrainerId());
     * } else {
     * HttpSession session = req.getSession(false);
     * if (session == null || session.getAttribute("userId") == null) {
     * throw new IllegalArgumentException("Bạn cần đăng nhập để tạo buổi tập");
     * }
     * Long sessionUserId = (Long) session.getAttribute("userId");
     * s.setTrainerId(sessionUserId.intValue());
     * }
     */

    // Date: if missing on update, keep old
    String dateStr = req.getParameter("training_date");
    if (dateStr == null || dateStr.isBlank()) {
      if (existingSchedule != null) {
        s.setTrainingDate(existingSchedule.getTrainingDate());
      } else {
        throw new IllegalArgumentException("Ngày tập không hợp lệ");
      }
    } else {
      s.setTrainingDate(Date.valueOf(dateStr));
    }

    // Time fields: accept HH:mm or HH:mm:ss; if empty on update -> keep old
    String startStr = req.getParameter("start_time");
    String endStr = req.getParameter("end_time");
    s.setStartTime(parseTimeKeepingOld(startStr, existingSchedule == null ? null : existingSchedule.getStartTime()));
    s.setEndTime(parseTimeKeepingOld(endStr, existingSchedule == null ? null : existingSchedule.getEndTime()));

    s.setTrainingType(req.getParameter("training_type"));
    s.setLocation(req.getParameter("location"));
    s.setNote(req.getParameter("note"));
    return s;
  }

  private Time parseTimeKeepingOld(String raw, Time oldVal) {
    if (raw == null || raw.trim().isEmpty()) {
      return oldVal; // keep old on update
    }
    String v = raw.trim();
    // Normalize HH:mm to HH:mm:ss
    if (v.length() == 5) {
      v = v + ":00";
    }
    // If it's already HH:mm:ss, use directly; else attempt LocalTime parsing
    try {
      return Time.valueOf(v);
    } catch (IllegalArgumentException ex) {
      // try parse HH:mm using java.time
      try {
        java.time.LocalTime lt = java.time.LocalTime.parse(raw.length() == 5 ? raw : v.substring(0, 8));
        return Time.valueOf(lt);
      } catch (Exception e2) {
        throw ex; // rethrow original for clear message
      }
    }
  }

  // Build attributes for monthly calendar rendering in JSP
  private void provideCalendarAttributes(HttpServletRequest req, List<Schedule> schedules) {
    LocalDate now = LocalDate.now();
    int currentYear = now.getYear();
    int currentMonth = now.getMonthValue();

    // Get month/year from request, default to current
    int year = currentYear;
    int month = currentMonth;
    try {
      String yearParam = req.getParameter("year");
      String monthParam = req.getParameter("month");
      if (yearParam != null && !yearParam.isEmpty()) {
        year = Integer.parseInt(yearParam);
      }
      if (monthParam != null && !monthParam.isEmpty()) {
        month = Integer.parseInt(monthParam);
      }
    } catch (NumberFormatException e) {
      year = currentYear;
      month = currentMonth;
    }

    // Validate range: allow 3 months before and 3 months after current month
    YearMonth currentYM = YearMonth.of(currentYear, currentMonth);
    YearMonth requestedYM = YearMonth.of(year, month);
    int monthsDiff = (requestedYM.getYear() - currentYM.getYear()) * 12 +
        (requestedYM.getMonthValue() - currentYM.getMonthValue());
    if (monthsDiff < -3 || monthsDiff > 3) {
      year = currentYear;
      month = currentMonth;
    }

    YearMonth ym = YearMonth.of(year, month);
    int daysInMonth = ym.lengthOfMonth();
    LocalDate first = ym.atDay(1);
    // Monday=1 ... Sunday=7
    int firstDayOfWeekIso = first.getDayOfWeek().getValue();

    // Map userId -> userName from request users attribute (already provided)
    @SuppressWarnings("unchecked")
    List<User> users = (List<User>) req.getAttribute("users");
    Map<Integer, String> userIdToName = new HashMap<>();
    if (users != null) {
      for (User u : users)
        userIdToName.put((int) u.getId(), u.getUsername());
    }

    Map<String, List<Map<String, String>>> scheduleMap = new HashMap<>();
    for (Schedule s : schedules) {
      LocalDate d = s.getTrainingDate().toLocalDate();
      if (d.getYear() != year || d.getMonthValue() != month)
        continue; // only selected month
      String key = d.toString();
      List<Map<String, String>> list = scheduleMap.computeIfAbsent(key, k -> new ArrayList<>());
      Map<String, String> item = new HashMap<>();
      item.put("time", s.getStartTime().toString().substring(0, 5));
      item.put("userName", userIdToName.getOrDefault(s.getUserId(), ""));
      item.put("status", s.getStatus());
      item.put("userId", String.valueOf(s.getUserId()));
      list.add(item);
    }

    req.setAttribute("calendar_year", year);
    req.setAttribute("calendar_month", month);
    req.setAttribute("calendar_days", daysInMonth);
    req.setAttribute("calendar_firstIso", firstDayOfWeekIso);
    req.setAttribute("scheduleMap", scheduleMap);

    // For navigation buttons (prev/next month)
    YearMonth prevYM = ym.minusMonths(1);
    YearMonth nextYM = ym.plusMonths(1);
    req.setAttribute("prev_month", prevYM.getMonthValue());
    req.setAttribute("prev_year", prevYM.getYear());
    req.setAttribute("next_month", nextYM.getMonthValue());
    req.setAttribute("next_year", nextYM.getYear());
  }
}