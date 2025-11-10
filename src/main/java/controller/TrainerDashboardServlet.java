package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Trainer;
import model.User;
import service.PasswordService;
import service.TrainerService;

/**
 * TrainerDashboardServlet - Servlet để điều hướng các trang PT/Trainer
 * Xử lý các request từ Personal Trainer
 * 
 * Chức năng:
 * 1. Hiển thị các trang dashboard, profile, schedule, students, chat, reports
 * 2. Cập nhật thông tin profile trainer
 * 3. Đổi mật khẩu trainer
 */
@WebServlet(name = "TrainerDashboardServlet", urlPatterns = {
    "/pt/home",
    "/pt/dashboard",
    "/pt/profile",
    "/pt/update-profile", // Cập nhật thông tin trainer
    "/pt/change-password", // Đổi mật khẩu trainer
    "/pt/students",
    "/pt/chat",
    "/pt/reports"
})
public class TrainerDashboardServlet extends HttpServlet {

  private TrainerService trainerService;
  private PasswordService passwordService;

  @Override
  public void init() throws ServletException {
    super.init();
    this.trainerService = new TrainerService();
    this.passwordService = new PasswordService();
    System.out.println("[TrainerDashboardServlet] Initialized successfully");
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    // Check if user is logged in
    HttpSession session = req.getSession(false);
    if (!isAuthenticated(session)) {
      resp.sendRedirect(req.getContextPath() + "/login");
      return;
    }

    // Get trainer from session and reload from DB
    Trainer currentTrainer = getCurrentTrainer(session);
    if (currentTrainer == null) {
      resp.sendRedirect(req.getContextPath() + "/login");
      return;
    }

    // Set trainer vào request để JSP sử dụng
    req.setAttribute("trainer", currentTrainer);

    String reqURI = req.getRequestURI();
    String contextPath = req.getContextPath();
    String path = reqURI.substring(contextPath.length());

    String jspPath;

    switch (path) {
      case "/pt/home", "/pt/dashboard" ->
        jspPath = "/views/PT/homePT.jsp";
      case "/pt/profile" ->
        jspPath = "/views/PT/profile.jsp";
      case "/pt/students" ->
        jspPath = "/views/PT/students.jsp";
      case "/pt/chat" ->
        jspPath = "/views/PT/chat.jsp";
      case "/pt/reports" ->
        jspPath = "/views/PT/reports.jsp";
      default ->
        jspPath = "/views/PT/homePT.jsp";
    }

    // Set page title attribute
    req.setAttribute("pageTitle", "PT Dashboard - GymFit");

    // Forward to the appropriate JSP
    req.getRequestDispatcher(jspPath).forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    // Check if user is logged in
    HttpSession session = req.getSession(false);
    if (!isAuthenticated(session)) {
      resp.sendRedirect(req.getContextPath() + "/login");
      return;
    }

    // Get trainer from session
    Trainer currentTrainer = getCurrentTrainer(session);
    if (currentTrainer == null) {
      resp.sendRedirect(req.getContextPath() + "/login");
      return;
    }

    // Routing dựa trên path
    String path = req.getServletPath();

    try {
      switch (path) {
        case "/pt/update-profile":
          updateTrainerProfile(req, resp, currentTrainer);
          break;

        case "/pt/change-password":
          changeTrainerPassword(req, resp, currentTrainer);
          break;

        default:
          resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
          break;
      }
    } catch (Exception e) {
      handleError(req, resp, e, "Có lỗi xảy ra. Vui lòng thử lại.");
    }
  }

  // ==================== AUTHENTICATION & SESSION ====================

  /**
   * Kiểm tra user đã đăng nhập chưa
   */
  private boolean isAuthenticated(HttpSession session) {
    if (session == null) {
      return false;
    }
    Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
    return isLoggedIn != null && isLoggedIn;
  }

  /**
   * Lấy trainer hiện tại từ session và reload từ DB để có dữ liệu mới nhất
   */
  private Trainer getCurrentTrainer(HttpSession session) {
    User user = (User) session.getAttribute("user");
    if (user == null) {
      return null;
    }

    // Reload từ database để có dữ liệu mới nhất
    Trainer trainer = trainerService.getTrainerById(user.getId());

    // Cập nhật lại session
    if (trainer != null) {
      session.setAttribute("user", trainer);
    }

    return trainer;
  }

  // ==================== UPDATE HANDLERS (POST) ====================

  /**
   * Cập nhật thông tin Profile Trainer
   */
  private void updateTrainerProfile(HttpServletRequest req, HttpServletResponse resp, Trainer trainer)
      throws ServletException, IOException {
    try {
      // Cập nhật thông tin User (kế thừa từ User)
      updateUserFields(trainer, req);

      // Cập nhật thông tin Trainer (specific fields)
      updateTrainerFields(trainer, req);

      // Lưu vào database
      int result = trainerService.update(trainer);
      if (result > 0) {
        // Reload trainer từ database để có dữ liệu mới nhất
        Trainer updatedTrainer = trainerService.getTrainerById(trainer.getId());
        HttpSession session = req.getSession();
        session.setAttribute("user", updatedTrainer);
        session.setAttribute("successMessage", "Cập nhật thông tin thành công!");
        resp.sendRedirect(req.getContextPath() + "/pt/profile");
      } else {
        throw new Exception("Không thể cập nhật thông tin.");
      }
    } catch (Exception e) {
      HttpSession session = req.getSession();
      session.setAttribute("errorMessage", e.getMessage());
      resp.sendRedirect(req.getContextPath() + "/pt/profile");
    }
  }

  /**
   * Đổi mật khẩu Trainer
   */
  private void changeTrainerPassword(HttpServletRequest req, HttpServletResponse resp, Trainer trainer)
      throws ServletException, IOException {
    try {
      String currentPassword = getParameter(req, "currentPassword");
      String newPassword = getParameter(req, "newPassword");
      String confirmPassword = getParameter(req, "confirmPassword");

      // Validation - Kiểm tra các trường bắt buộc
      if (currentPassword == null || newPassword == null || confirmPassword == null) {
        throw new IllegalArgumentException("Vui lòng điền đầy đủ thông tin");
      }

      // Validation - Kiểm tra mật khẩu mới và xác nhận có khớp không
      if (!newPassword.equals(confirmPassword)) {
        throw new IllegalArgumentException("Mật khẩu mới và xác nhận mật khẩu không khớp");
      }

      // Validation - Kiểm tra độ dài mật khẩu mới
      if (newPassword.length() < 6) {
        throw new IllegalArgumentException("Mật khẩu mới phải có ít nhất 6 ký tự");
      }

      // Validation - Kiểm tra mật khẩu mới không trùng với mật khẩu hiện tại
      if (currentPassword.equals(newPassword)) {
        throw new IllegalArgumentException("Mật khẩu mới phải khác với mật khẩu hiện tại");
      }

      // Verify current password - Xác thực mật khẩu hiện tại
      boolean isCurrentPasswordCorrect = passwordService.verifyPassword(currentPassword, trainer.getPassword());
      if (!isCurrentPasswordCorrect) {
        throw new IllegalArgumentException("Mật khẩu hiện tại không đúng");
      }

      // Hash new password and update - Hash mật khẩu mới và cập nhật
      String hashedNewPassword = passwordService.hashPassword(newPassword);
      trainer.setPassword(hashedNewPassword);

      int result = trainerService.update(trainer);
      if (result > 0) {
        // Reload trainer từ database để có dữ liệu mới nhất
        Trainer updatedTrainer = trainerService.getTrainerById(trainer.getId());
        HttpSession session = req.getSession();
        session.setAttribute("user", updatedTrainer);
        session.setAttribute("passwordSuccessMessage", "Đổi mật khẩu thành công!");
        resp.sendRedirect(req.getContextPath() + "/pt/profile");
      } else {
        throw new Exception("Không thể đổi mật khẩu.");
      }
    } catch (IllegalArgumentException e) {
      // Validation errors - hiển thị message cho user
      HttpSession session = req.getSession();
      session.setAttribute("passwordErrorMessage", e.getMessage());
      resp.sendRedirect(req.getContextPath() + "/pt/profile");
    } catch (Exception e) {
      // System errors - log và hiển thị message chung
      System.err.println("[TrainerDashboardServlet] Error changing password: " + e.getMessage());
      e.printStackTrace();
      HttpSession session = req.getSession();
      session.setAttribute("passwordErrorMessage", "Có lỗi xảy ra khi đổi mật khẩu. Vui lòng thử lại.");
      resp.sendRedirect(req.getContextPath() + "/pt/profile");
    }
  }

  /**
   * Cập nhật các trường của User (base class)
   */
  private void updateUserFields(Trainer trainer, HttpServletRequest req) {
    String name = getParameter(req, "fullName");
    String phone = getParameter(req, "phoneNumber");
    String address = getParameter(req, "address");
    String gender = getParameter(req, "gender");
    String email = getParameter(req, "email");
    String dobStr = getParameter(req, "dateOfBirth");

    if (name != null)
      trainer.setName(name);
    if (phone != null)
      trainer.setPhone(phone);
    if (address != null)
      trainer.setAddress(address);
    if (gender != null)
      trainer.setGender(gender);
    if (email != null)
      trainer.setEmail(email);

    if (dobStr != null && !dobStr.trim().isEmpty()) {
      try {
        trainer.setDob(java.sql.Date.valueOf(dobStr));
      } catch (IllegalArgumentException e) {
        System.err.println("[TrainerDashboardServlet] Invalid date format: " + dobStr);
      }
    }
  }

  /**
   * Cập nhật các trường của Trainer (specific fields)
   */
  private void updateTrainerFields(Trainer trainer, HttpServletRequest req) {
    String specialization = getParameter(req, "specialization");
    String yearsOfExpStr = getParameter(req, "yearsOfExperience");
    String certificationLevel = getParameter(req, "certificationLevel");

    if (specialization != null) {
      trainer.setSpecialization(specialization);
    }

    if (yearsOfExpStr != null && !yearsOfExpStr.trim().isEmpty()) {
      try {
        Integer yearsOfExp = Integer.parseInt(yearsOfExpStr);
        if (yearsOfExp >= 0 && yearsOfExp <= 100) {
          trainer.setYearsOfExperience(yearsOfExp);
        }
      } catch (NumberFormatException e) {
        System.err.println("[TrainerDashboardServlet] Invalid years of experience: " + yearsOfExpStr);
      }
    }

    if (certificationLevel != null) {
      trainer.setCertificationLevel(certificationLevel);
    }
  }

  // ==================== UTILITY METHODS ====================

  /**
   * Lấy parameter và trim, trả về null nếu rỗng
   */
  private String getParameter(HttpServletRequest req, String name) {
    String value = req.getParameter(name);
    return (value != null && !value.trim().isEmpty()) ? value.trim() : null;
  }

  /**
   * Xử lý lỗi chung
   */
  private void handleError(HttpServletRequest req, HttpServletResponse resp,
      Exception e, String userMessage) throws ServletException, IOException {
    System.err.println("[TrainerDashboardServlet] Error: " + e.getMessage());
    e.printStackTrace();
    HttpSession session = req.getSession();
    session.setAttribute("errorMessage", userMessage);
    resp.sendRedirect(req.getContextPath() + "/pt/profile");
  }

  @Override
  public String getServletInfo() {
    return "Trainer Dashboard Servlet for GymFit Management System";
  }
}
