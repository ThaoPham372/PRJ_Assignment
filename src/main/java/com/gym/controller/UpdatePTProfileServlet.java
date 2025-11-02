package com.gym.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.gym.dao.PTCertificateDAO;
import com.gym.dao.PTProfileDAO;
import com.gym.model.PTProfile;

/**
 * UpdatePTProfileServlet - Handles PT profile updates
 * URL: /update-pt-profile
 */
@WebServlet(name = "UpdatePTProfileServlet", urlPatterns = { "/update-pt-profile" })
public class UpdatePTProfileServlet extends HttpServlet {

  private PTProfileDAO ptProfileDAO;
  private PTCertificateDAO ptCertificateDAO;

  @Override
  public void init() throws ServletException {
    super.init();
    this.ptProfileDAO = new PTProfileDAO();
    this.ptCertificateDAO = new PTCertificateDAO();
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
    List<String> userRoles = (List<String>) session.getAttribute("userRoles");
    if (userRoles == null || !userRoles.contains("PT")) {
      response.sendRedirect(request.getContextPath() + "/home");
      return;
    }

    Long userId = (Long) session.getAttribute("userId");
    if (userId == null) {
      System.err.println("ERROR: userId not found in session");
      request.setAttribute("errorMessage", "Không tìm thấy thông tin người dùng!");
      request.getRequestDispatcher("/views/PT/profile.jsp").forward(request, response);
      return;
    }

    System.out.println("DEBUG: Processing PT profile update for user_id = " + userId);

    try {
      // Get form parameters
      String fullName = request.getParameter("fullName");
      String email = request.getParameter("email");
      String phoneNumber = request.getParameter("phoneNumber");
      String dateOfBirth = request.getParameter("dateOfBirth");
      String gender = request.getParameter("gender");
      String specialization = request.getParameter("specialization");
      String address = request.getParameter("address");

      System.out.println("DEBUG: Form parameters received:");
      System.out.println("  - fullName: " + fullName);
      System.out.println("  - email: " + email);
      System.out.println("  - phoneNumber: " + phoneNumber);
      System.out.println("  - dateOfBirth: " + dateOfBirth);
      System.out.println("  - gender: " + gender);
      System.out.println("  - specialization: " + specialization);
      System.out.println("  - address: " + address);

      // Validate required fields
      List<String> errors = validateInput(fullName, email);
      if (!errors.isEmpty()) {
        request.setAttribute("errorMessage", String.join(", ", errors));
        request.getRequestDispatcher("/views/PT/profile.jsp").forward(request, response);
        return;
      }

      // Check if PT profile exists
      PTProfile existingProfile = ptProfileDAO.findByUserId(userId);
      System.out.println("DEBUG: Existing profile found: " + (existingProfile != null));

      PTProfile profile;
      if (existingProfile != null) {
        // Update existing profile
        System.out.println("DEBUG: Updating existing profile with ID: " + existingProfile.getId());
        profile = existingProfile;
        profile.setFullName(fullName);
        profile.setEmail(email);
        profile.setPhoneNumber(phoneNumber);
        profile.setDateOfBirth(dateOfBirth);
        profile.setGender(gender);
        profile.setSpecialization(specialization);
        profile.setAddress(address);

        boolean updated = ptProfileDAO.updatePTProfile(profile);
        System.out.println("DEBUG: Update result: " + updated);

        if (updated) {
          request.setAttribute("successMessage", "Cập nhật thông tin thành công!");
          System.out.println("DEBUG: Profile update successful");
        } else {
          request.setAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật thông tin!");
          System.err.println("ERROR: Profile update failed");
        }
      } else {
        // Create new profile
        System.out.println("DEBUG: Creating new profile for user_id: " + userId);
        profile = new PTProfile();
        profile.setUserId(userId);
        profile.setFullName(fullName);
        profile.setEmail(email);
        profile.setPhoneNumber(phoneNumber);
        profile.setDateOfBirth(dateOfBirth);
        profile.setGender(gender);
        profile.setSpecialization(specialization);
        profile.setAddress(address);
        profile.setExperienceYears(0);
        profile.setStudentsTrained(0);
        profile.setAverageRating(0.0);
        profile.setSessionsThisMonth(0);

        Long profileId = ptProfileDAO.createPTProfile(profile);
        System.out.println("DEBUG: Create result - Profile ID: " + profileId);

        if (profileId > 0) {
          request.setAttribute("successMessage", "Tạo hồ sơ thành công!");
          System.out.println("DEBUG: Profile creation successful");
        } else {
          request.setAttribute("errorMessage", "Có lỗi xảy ra khi tạo hồ sơ!");
          System.err.println("ERROR: Profile creation failed");
        }
      }

      // Set profile data for display
      request.setAttribute("ptProfile", profile);

      // Get certificates for this profile
      java.util.List<com.gym.model.PTCertificate> certificates = ptCertificateDAO.findByPtProfileId(profile.getId());
      request.setAttribute("certificates", certificates);

      // Forward back to profile page
      request.getRequestDispatcher("/views/PT/profile.jsp").forward(request, response);

    } catch (Exception e) {
      System.err.println("Error updating PT profile: " + e.getMessage());
      e.printStackTrace();
      request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
      request.getRequestDispatcher("/views/PT/profile.jsp").forward(request, response);
    }
  }

  /**
   * Validate input parameters
   */
  private List<String> validateInput(String fullName, String email) {
    List<String> errors = new ArrayList<>();

    if (fullName == null || fullName.trim().isEmpty()) {
      errors.add("Họ và tên không được để trống");
    }

    if (email == null || email.trim().isEmpty()) {
      errors.add("Email không được để trống");
    } else if (!isValidEmail(email)) {
      errors.add("Email không đúng định dạng");
    }

    return errors;
  }

  /**
   * Simple email validation
   */
  private boolean isValidEmail(String email) {
    return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
  }
}
