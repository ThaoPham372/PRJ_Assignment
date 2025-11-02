package com.gym.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.gym.dao.UserDAO;
import com.gym.model.User;
import com.gym.service.PasswordService;

/**
 * ChangePasswordServlet - Handles password change requests
 * URL: /change-password
 */
@WebServlet(name = "ChangePasswordServlet", urlPatterns = { "/change-password" })
public class ChangePasswordServlet extends HttpServlet {

  private UserDAO userDAO;
  private PasswordService passwordService;

  @Override
  public void init() throws ServletException {
    super.init();
    this.userDAO = new UserDAO();
    this.passwordService = new PasswordService();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // Redirect GET requests to PT profile page
    response.sendRedirect(request.getContextPath() + "/pt/profile");
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    // Set UTF-8 encoding for request and response
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html; charset=UTF-8");

    HttpSession session = request.getSession(false);

    // Check if user is logged in
    if (session == null || session.getAttribute("isLoggedIn") == null) {
      response.sendRedirect(request.getContextPath() + "/views/login.jsp");
      return;
    }

    // Check if user has PT role
    @SuppressWarnings("unchecked")
    java.util.List<String> userRoles = (java.util.List<String>) session.getAttribute("userRoles");
    if (userRoles == null || !userRoles.contains("PT")) {
      response.sendRedirect(request.getContextPath() + "/home");
      return;
    }

    Long userId = (Long) session.getAttribute("userId");
    if (userId == null) {
      System.err.println("ERROR: userId not found in session for password change");
      request.setAttribute("errorMessage", "Không tìm thấy thông tin người dùng!");
      request.getRequestDispatcher("/pt/profile").forward(request, response);
      return;
    }

    System.out.println("DEBUG: Processing password change for user_id = " + userId);

    try {
      // Get form parameters
      String currentPassword = request.getParameter("currentPassword");
      String newPassword = request.getParameter("newPassword");
      String confirmPassword = request.getParameter("confirmPassword");

      System.out.println("DEBUG: Password change parameters received:");
      System.out.println("  - currentPassword: " + (currentPassword != null ? "[PROVIDED]" : "null"));
      System.out.println("  - newPassword: " + (newPassword != null ? "[PROVIDED]" : "null"));
      System.out.println("  - confirmPassword: " + (confirmPassword != null ? "[PROVIDED]" : "null"));

      // Validate input
      String validationError = validatePasswordInput(currentPassword, newPassword, confirmPassword);
      if (validationError != null) {
        request.setAttribute("passwordErrorMessage", validationError);
        request.getRequestDispatcher("/pt/profile").forward(request, response);
        return;
      }

      // Get user from database to verify current password
      User sessionUser = (User) session.getAttribute("user");
      if (sessionUser == null) {
        System.err.println("ERROR: User object not found in session");
        request.setAttribute("passwordErrorMessage", "Không tìm thấy thông tin người dùng trong session!");
        request.getRequestDispatcher("/pt/profile").forward(request, response);
        return;
      }

      User user = userDAO.findByUsername(sessionUser.getUsername());
      if (user == null) {
        System.err.println("ERROR: User not found in database for password change");
        request.setAttribute("passwordErrorMessage", "Không tìm thấy thông tin người dùng trong database!");
        request.getRequestDispatcher("/pt/profile").forward(request, response);
        return;
      }

      System.out.println("DEBUG: User found - ID: " + user.getId() + ", Username: " + user.getUsername());

      // Verify current password
      boolean currentPasswordValid = passwordService.verifyPasswordWithSalt(
          currentPassword,
          user.getPassword(),
          user.getName());

      System.out.println("DEBUG: Current password verification result: " + currentPasswordValid);

      if (!currentPasswordValid) {
        request.setAttribute("passwordErrorMessage", "Mật khẩu hiện tại không đúng!");
        request.getRequestDispatcher("/pt/profile").forward(request, response);
        return;
      }

      // Hash new password
      String newPasswordHash = passwordService.hashPassword(newPassword);
      String newSalt = passwordService.generateSalt();

      System.out.println("DEBUG: New password hashed successfully");

      // Update password in database
      boolean passwordUpdated = updateUserPassword(userId, newPasswordHash, newSalt);

      System.out.println("DEBUG: Password update result: " + passwordUpdated);

      if (passwordUpdated) {
        request.setAttribute("passwordSuccessMessage", "Đổi mật khẩu thành công!");
        System.out.println("DEBUG: Password change successful for user_id: " + userId);
      } else {
        request.setAttribute("passwordErrorMessage", "Có lỗi xảy ra khi đổi mật khẩu!");
        System.err.println("ERROR: Password update failed for user_id: " + userId);
      }

      // Forward to PTProfileServlet to load data and display message
      request.getRequestDispatcher("/pt/profile").forward(request, response);

    } catch (Exception e) {
      System.err.println("ERROR: Exception during password change for user_id: " + userId);
      System.err.println("Exception: " + e.getMessage());
      e.printStackTrace();
      request.setAttribute("passwordErrorMessage", "Có lỗi xảy ra: " + e.getMessage());
      request.getRequestDispatcher("/pt/profile").forward(request, response);
    }
  }

  /**
   * Validate password input parameters
   */
  private String validatePasswordInput(String currentPassword, String newPassword, String confirmPassword) {
    if (currentPassword == null || currentPassword.trim().isEmpty()) {
      return "Mật khẩu hiện tại không được để trống";
    }

    if (newPassword == null || newPassword.trim().isEmpty()) {
      return "Mật khẩu mới không được để trống";
    }

    if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
      return "Xác nhận mật khẩu không được để trống";
    }

    if (!newPassword.equals(confirmPassword)) {
      return "Mật khẩu mới và xác nhận mật khẩu không khớp";
    }

    if (currentPassword.equals(newPassword)) {
      return "Mật khẩu mới phải khác mật khẩu hiện tại";
    }

    // Validate password strength
    String validationMessage = passwordService.getPasswordValidationMessage(newPassword);
    if (validationMessage != null) {
      return validationMessage;
    }

    return null; // Valid input
  }

  /**
   * Update user password in database
   */
  private boolean updateUserPassword(Long userId, String newPasswordHash, String newSalt) {
    String sql = "UPDATE users SET password_hash = ?, salt = ?, updated_date = NOW() WHERE id = ?";

    try (java.sql.Connection conn = com.gym.util.DatabaseUtil.getConnection();
        java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, newPasswordHash);
      stmt.setString(2, newSalt);
      stmt.setLong(3, userId);

      int affectedRows = stmt.executeUpdate();
      System.out.println("DEBUG: Password UPDATE query affected " + affectedRows + " rows");

      if (affectedRows == 0) {
        System.err.println("WARNING: No rows were updated for password change, user_id = " + userId);
        return false;
      }

      if (affectedRows > 1) {
        System.err.println("WARNING: Multiple rows updated for password change, user_id = " + userId);
      }

      return affectedRows > 0;

    } catch (java.sql.SQLException e) {
      System.err.println("ERROR: Failed to update password for user_id = " + userId);
      System.err.println("SQL Error: " + e.getMessage());
      System.err.println("SQL State: " + e.getSQLState());
      System.err.println("Error Code: " + e.getErrorCode());
      e.printStackTrace();
    }

    return false;
  }
}
