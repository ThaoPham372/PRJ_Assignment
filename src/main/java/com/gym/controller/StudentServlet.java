package com.gym.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.gym.dao.UserDAO;
import com.gym.model.Student;
import com.gym.model.User;
import com.gym.service.IStudentService;
import com.gym.service.StudentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * StudentServlet - Handles student management operations for PT
 * URL patterns:
 * - /pt/students/search (GET) - Search students
 * - /pt/students/view (GET) - View student detail
 * - /pt/students/update (POST) - Update student
 */
@WebServlet(name = "StudentServlet", urlPatterns = {
    "/pt/students/search",
    "/pt/students/view",
    "/pt/students/update"
})
public class StudentServlet extends HttpServlet {

  private IStudentService studentService;
  private UserDAO userDAO;

  @Override
  public void init() throws ServletException {
    super.init();
    this.studentService = new StudentService();
    this.userDAO = new UserDAO();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
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

    String path = request.getServletPath();
    String action = request.getParameter("action");

    try {
      if ("view".equals(action) || "/pt/students/view".equals(path)) {
        // View student detail
        handleViewStudent(request, response);
      } else {
        // Search/List students
        handleSearchStudents(request, response);
      }
    } catch (Exception e) {
      System.err.println("Error in StudentServlet: " + e.getMessage());
      e.printStackTrace();
      request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
      request.getRequestDispatcher("/views/PT/student_management.jsp").forward(request, response);
    }
  }

  private void handleSearchStudents(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession(false);

    // Check for success/error messages in session (from redirect)
    if (session != null) {
      String successMessage = (String) session.getAttribute("successMessage");
      if (successMessage != null) {
        request.setAttribute("successMessage", successMessage);
        session.removeAttribute("successMessage"); // Clear after use
      }
      String errorMessage = (String) session.getAttribute("errorMessage");
      if (errorMessage != null) {
        request.setAttribute("errorMessage", errorMessage);
        session.removeAttribute("errorMessage"); // Clear after use
      }
    }

    String keyword = request.getParameter("keyword");
    String trainingPackage = request.getParameter("package");

    List<Student> students;
    if ((keyword != null && !keyword.trim().isEmpty()) ||
        (trainingPackage != null && !trainingPackage.trim().isEmpty())) {
      students = studentService.searchStudents(keyword, trainingPackage);
    } else {
      students = studentService.getAllActiveStudents();
    }

    // Get statistics
    int totalStudents = studentService.getTotalStudentsCount();
    int activeStudents = studentService.getActiveStudentsCount();
    int achievedGoalCount = studentService.getAchievedGoalCount();

    // Set attributes for JSP
    request.setAttribute("students", students);
    request.setAttribute("totalStudents", totalStudents);
    request.setAttribute("activeStudents", activeStudents);
    request.setAttribute("achievedGoalCount", achievedGoalCount);
    request.setAttribute("searchTerm", keyword);
    request.setAttribute("packageFilter", trainingPackage);

    // Forward to JSP
    request.getRequestDispatcher("/views/PT/student_management.jsp").forward(request, response);
  }

  private void handleViewStudent(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String studentIdParam = request.getParameter("id");
    if (studentIdParam == null || studentIdParam.trim().isEmpty()) {
      // If client asks for JSON, return error JSON instead of forwarding
      boolean wantJson = "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))
          || "json".equalsIgnoreCase(request.getParameter("format"));
      if (wantJson) {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("{\"success\":false,\"message\":\"Không tìm thấy ID học viên\"}");
        return;
      } else {
        request.setAttribute("errorMessage", "Không tìm thấy ID học viên");
        handleSearchStudents(request, response);
        return;
      }
    }

    try {
      Integer studentId = Integer.parseInt(studentIdParam);
      Optional<Student> studentOpt = studentService.getStudentById(studentId);

      if (!studentOpt.isPresent()) {
        boolean wantJson = "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))
            || "json".equalsIgnoreCase(request.getParameter("format"));
        if (wantJson) {
          response.setContentType("application/json; charset=UTF-8");
          response.setStatus(HttpServletResponse.SC_NOT_FOUND);
          response.getWriter()
              .write("{\"success\":false,\"message\":\"Không tìm thấy học viên với ID: " + studentId + "\"}");
          return;
        }
        request.setAttribute("errorMessage", "Không tìm thấy học viên với ID: " + studentId);
        handleSearchStudents(request, response);
        return;
      }

      Student student = studentOpt.get();
      boolean wantJson = "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))
          || "json".equalsIgnoreCase(request.getParameter("format"));
      if (wantJson) {
        response.setContentType("application/json; charset=UTF-8");
        java.util.function.Function<String, String> esc = s -> s == null ? ""
            : s.replace("\\", "\\\\").replace("\"", "\\\"");
        String dob = student.getUser() != null && student.getUser().getDob() != null
            ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(student.getUser().getDob())
            : "";
        StringBuilder sb = new StringBuilder();
        sb.append("{\"success\":true,\"data\":{")
            .append("\"userId\":").append(student.getUserId()).append(",")
            .append("\"name\":\"").append(esc.apply(student.getUser() != null ? student.getUser().getName() : null))
            .append("\",")
            .append("\"phone\":\"").append(esc.apply(student.getUser() != null ? student.getUser().getPhone() : null))
            .append("\",")
            .append("\"email\":\"").append(esc.apply(student.getUser() != null ? student.getUser().getEmail() : null))
            .append("\",")
            .append("\"dob\":\"").append(dob).append("\",")
            .append("\"address\":\"")
            .append(esc.apply(student.getUser() != null ? student.getUser().getAddress() : null)).append("\",")
            .append("\"weight\":").append(student.getWeight() != null ? student.getWeight() : 0).append(",")
            .append("\"height\":").append(student.getHeight() != null ? student.getHeight() : 0).append(",")
            .append("\"bmi\":").append(student.getBmi() != null ? student.getBmi() : 0).append(",")
            .append("\"goal\":\"").append(esc.apply(student.getGoal())).append("\",")
            .append("\"trainingPackage\":\"").append(esc.apply(student.getTrainingPackage())).append("\",")
            .append("\"trainingDuration\":\"").append(esc.apply(student.getTrainingDuration())).append("\",")
            .append("\"trainingSessions\":")
            .append(student.getTrainingSessions() != null ? student.getTrainingSessions() : 0).append(",")
            .append("\"trainingProgress\":")
            .append(student.getTrainingProgress() != null ? student.getTrainingProgress() : 0).append(",")
            .append("\"ptNote\":\"").append(esc.apply(student.getPtNote())).append("\"}");
        sb.append("}");
        response.getWriter().write(sb.toString());
        return;
      } else {
        request.setAttribute("student", student);
        request.setAttribute("studentDetail", student); // For JSP modal
        // Also load search results for the page
        handleSearchStudents(request, response);
      }
    } catch (NumberFormatException e) {
      boolean wantJson = "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))
          || "json".equalsIgnoreCase(request.getParameter("format"));
      if (wantJson) {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("{\"success\":false,\"message\":\"ID học viên không hợp lệ\"}");
      } else {
        request.setAttribute("errorMessage", "ID học viên không hợp lệ");
        handleSearchStudents(request, response);
      }
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
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

    String path = request.getServletPath();
    if ("/pt/students/update".equals(path)) {
      try {
        handleUpdateStudent(request, response);
      } catch (Exception e) {
        System.err.println("Error updating student: " + e.getMessage());
        e.printStackTrace();
        request.setAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật: " + e.getMessage());
        handleSearchStudents(request, response);
      }
    } else {
      doGet(request, response);
    }
  }

  private void handleUpdateStudent(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession(false);
    boolean isAjax = "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))
        || request.getParameter("ajax") != null;

    // Helper method to send JSON error response
    java.util.function.BiConsumer<String, String> sendJsonError = (errorMsg, detail) -> {
      try {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("{\"success\":false,\"message\":\"" +
            errorMsg.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r") +
            "\",\"detail\":\""
            + (detail != null ? detail.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r") : "") + "\"}");
      } catch (IOException e) {
        System.err.println("Error sending JSON error response: " + e.getMessage());
      }
    };

    // Accept multiple param names for compatibility: studentId, user_id, userId
    String studentIdParam = request.getParameter("studentId");
    if (studentIdParam == null || studentIdParam.trim().isEmpty()) {
      studentIdParam = request.getParameter("user_id");
    }
    if (studentIdParam == null || studentIdParam.trim().isEmpty()) {
      studentIdParam = request.getParameter("userId");
    }
    if (studentIdParam == null || studentIdParam.trim().isEmpty()) {
      if (isAjax) {
        sendJsonError.accept("Không tìm thấy ID học viên", null);
        return;
      }
      request.setAttribute("errorMessage", "Không tìm thấy ID học viên");
      handleSearchStudents(request, response);
      return;
    }

    try {
      Integer studentId = Integer.parseInt(studentIdParam);
      Optional<Student> studentOpt = studentService.getStudentById(studentId);

      if (!studentOpt.isPresent()) {
        if (isAjax) {
          sendJsonError.accept("Không tìm thấy học viên với ID: " + studentId, null);
          return;
        }
        request.setAttribute("errorMessage", "Không tìm thấy học viên với ID: " + studentId);
        handleSearchStudents(request, response);
        return;
      }

      Student student = studentOpt.get();
      User user = student.getUser();

      if (user == null) {
        user = userDAO.findById(studentId.longValue());
        if (user == null) {
          if (isAjax) {
            sendJsonError.accept("Không tìm thấy thông tin user", null);
            return;
          }
          request.setAttribute("errorMessage", "Không tìm thấy thông tin user");
          handleSearchStudents(request, response);
          return;
        }
        student.setUser(user);
      }

      // Update User fields
      if (request.getParameter("name") != null) {
        user.setName(request.getParameter("name"));
      }
      if (request.getParameter("email") != null) {
        user.setEmail(request.getParameter("email"));
      }
      if (request.getParameter("phone") != null) {
        user.setPhone(request.getParameter("phone"));
      }
      if (request.getParameter("address") != null) {
        user.setAddress(request.getParameter("address"));
      }
      if (request.getParameter("dob") != null && !request.getParameter("dob").trim().isEmpty()) {
        try {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
          Date dob = sdf.parse(request.getParameter("dob"));
          user.setDob(dob);
        } catch (ParseException e) {
          System.err.println("Error parsing date: " + e.getMessage());
          if (isAjax) {
            sendJsonError.accept("Ngày sinh không hợp lệ", e.getMessage());
            return;
          }
        }
      }

      // Update Student fields
      if (request.getParameter("weight") != null && !request.getParameter("weight").trim().isEmpty()) {
        try {
          student.setWeight(Float.parseFloat(request.getParameter("weight")));
        } catch (NumberFormatException e) {
          System.err.println("Error parsing weight: " + e.getMessage());
          if (isAjax) {
            sendJsonError.accept("Cân nặng không hợp lệ", e.getMessage());
            return;
          }
        }
      }
      if (request.getParameter("height") != null && !request.getParameter("height").trim().isEmpty()) {
        try {
          student.setHeight(Float.parseFloat(request.getParameter("height")));
        } catch (NumberFormatException e) {
          System.err.println("Error parsing height: " + e.getMessage());
          if (isAjax) {
            sendJsonError.accept("Chiều cao không hợp lệ", e.getMessage());
            return;
          }
        }
      }
      if (request.getParameter("goal") != null) {
        student.setGoal(request.getParameter("goal"));
      }
      if (request.getParameter("trainingPackage") != null) {
        student.setTrainingPackage(request.getParameter("trainingPackage"));
      }
      if (request.getParameter("trainingDuration") != null) {
        student.setTrainingDuration(request.getParameter("trainingDuration"));
      }
      if (request.getParameter("trainingSessions") != null
          && !request.getParameter("trainingSessions").trim().isEmpty()) {
        try {
          student.setTrainingSessions(Integer.parseInt(request.getParameter("trainingSessions")));
        } catch (NumberFormatException e) {
          System.err.println("Error parsing trainingSessions: " + e.getMessage());
          if (isAjax) {
            sendJsonError.accept("Số buổi tập không hợp lệ", e.getMessage());
            return;
          }
        }
      }
      if (request.getParameter("trainingProgress") != null
          && !request.getParameter("trainingProgress").trim().isEmpty()) {
        try {
          student.setTrainingProgress(Integer.parseInt(request.getParameter("trainingProgress")));
        } catch (NumberFormatException e) {
          System.err.println("Error parsing trainingProgress: " + e.getMessage());
          if (isAjax) {
            sendJsonError.accept("Tiến độ không hợp lệ", e.getMessage());
            return;
          }
        }
      }
      if (request.getParameter("ptNote") != null) {
        student.setPtNote(request.getParameter("ptNote"));
      }
      if (request.getParameter("phoneNumber") != null) {
        student.setPhoneNumber(request.getParameter("phoneNumber"));
      }

      // Update emergency contact
      if (request.getParameter("emergencyContactName") != null) {
        student.setEmergencyContactName(request.getParameter("emergencyContactName"));
      }
      if (request.getParameter("emergencyContactPhone") != null) {
        student.setEmergencyContactPhone(request.getParameter("emergencyContactPhone"));
      }
      if (request.getParameter("emergencyContactRelation") != null) {
        student.setEmergencyContactRelation(request.getParameter("emergencyContactRelation"));
      }
      if (request.getParameter("emergencyContactAddress") != null) {
        student.setEmergencyContactAddress(request.getParameter("emergencyContactAddress"));
      }

      // Calculate BMI if weight and height are provided
      if (student.getWeight() != null && student.getHeight() != null && student.getHeight() > 0) {
        float heightInMeters = student.getHeight() / 100.0f;
        float bmi = student.getWeight() / (heightInMeters * heightInMeters);
        student.setBmi(bmi);
      }

      // Update lastUpdate timestamp
      user.setLastUpdate(new Date());

      // Save updates
      try {
        userDAO.updateUser(user);
        studentService.updateStudent(student);
      } catch (Exception e) {
        System.err.println("Error saving student update: " + e.getMessage());
        e.printStackTrace();
        if (isAjax) {
          sendJsonError.accept("Lỗi khi lưu dữ liệu: " + e.getMessage(), null);
          return;
        }
        throw e;
      }

      // Success message
      String successMessage = "Cập nhật học viên " + (user.getName() != null ? user.getName() : "")
          + " thành công!";

      // Support AJAX (no reload) update
      if (isAjax) {
        response.setContentType("application/json; charset=UTF-8");

        // Helper method to escape JSON strings properly
        java.util.function.Function<String, String> escapeJson = (String str) -> {
          if (str == null)
            return "";
          return str.replace("\\", "\\\\")
              .replace("\"", "\\\"")
              .replace("\n", "\\n")
              .replace("\r", "\\r")
              .replace("\t", "\\t");
        };

        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("\"success\":true,")
            .append("\"message\":\"").append(escapeJson.apply(successMessage))
            .append("\",")
            .append("\"data\":{")
            .append("\"userId\":").append(student.getUserId()).append(",")
            .append("\"name\":\"").append(escapeJson.apply(user.getName())).append("\",")
            .append("\"phone\":\"").append(escapeJson.apply(user.getPhone())).append("\",")
            .append("\"email\":\"").append(escapeJson.apply(user.getEmail())).append("\",")
            .append("\"dob\":\"")
            .append(user.getDob() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(user.getDob()) : "")
            .append("\",")
            .append("\"address\":\"").append(escapeJson.apply(user.getAddress())).append("\",")
            .append("\"weight\":").append(student.getWeight() != null ? student.getWeight() : 0).append(",")
            .append("\"height\":").append(student.getHeight() != null ? student.getHeight() : 0).append(",")
            .append("\"bmi\":").append(student.getBmi() != null ? student.getBmi() : 0).append(",")
            .append("\"goal\":\"").append(escapeJson.apply(student.getGoal())).append("\",")
            .append("\"trainingPackage\":\"").append(escapeJson.apply(student.getTrainingPackage())).append("\",")
            .append("\"trainingDuration\":\"").append(escapeJson.apply(student.getTrainingDuration())).append("\",")
            .append("\"trainingSessions\":")
            .append(student.getTrainingSessions() != null ? student.getTrainingSessions() : 0).append(",")
            .append("\"trainingProgress\":")
            .append(student.getTrainingProgress() != null ? student.getTrainingProgress() : 0).append(",")
            .append("\"ptNote\":\"").append(escapeJson.apply(student.getPtNote())).append("\"")
            .append("}")
            .append("}");
        response.getWriter().write(sb.toString());
        return;
      }

      // Default: redirect and show flash message
      session.setAttribute("successMessage", successMessage);
      response.sendRedirect(request.getContextPath() + "/pt/students/search");

    } catch (NumberFormatException e) {
      if (isAjax) {
        sendJsonError.accept("ID học viên không hợp lệ", e.getMessage());
        return;
      }
      request.setAttribute("errorMessage", "ID học viên không hợp lệ");
      handleSearchStudents(request, response);
    } catch (Exception e) {
      System.err.println("Unexpected error in handleUpdateStudent: " + e.getMessage());
      e.printStackTrace();
      if (isAjax) {
        sendJsonError.accept("Có lỗi xảy ra khi cập nhật", e.getMessage());
        return;
      }
      request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
      handleSearchStudents(request, response);
    }
  }
}
