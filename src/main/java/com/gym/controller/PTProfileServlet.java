package com.gym.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.gym.dao.PTCertificateDAO;
import com.gym.dao.PTProfileDAO;
import com.gym.model.PTProfile;
import com.gym.model.User;

/**
 * PTProfileServlet - Handles PT profile page display
 * URL: /pt/profile
 */
@WebServlet(name = "PTProfileServlet", urlPatterns = { "/pt/profile" })
public class PTProfileServlet extends HttpServlet {

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
      request.setAttribute("errorMessage", "Không tìm thấy thông tin người dùng!");
      request.getRequestDispatcher("/views/PT/profile.jsp").forward(request, response);
      return;
    }

    try {
      // Get PT profile data
      PTProfile ptProfile = ptProfileDAO.findByUserId(userId);

      // If no profile exists, create a default one
      if (ptProfile == null) {
        System.out.println("DEBUG: No PT profile found for user_id = " + userId + ", creating default profile");
        User user = (User) session.getAttribute("user");
        ptProfile = new PTProfile();
        ptProfile.setUserId(userId);
        ptProfile.setFullName(user != null ? user.getUsername() : "Personal Trainer");
        ptProfile.setEmail(user != null ? user.getEmail() : "");
        ptProfile.setExperienceYears(0);
        ptProfile.setStudentsTrained(0);
        ptProfile.setAverageRating(0.0);
        ptProfile.setSessionsThisMonth(0);

        // Create the profile in database
        Long profileId = ptProfileDAO.createPTProfile(ptProfile);
        if (profileId > 0) {
          ptProfile.setId(profileId);
          System.out.println("DEBUG: Created new PT profile with ID = " + profileId);
        } else {
          System.err.println("ERROR: Failed to create PT profile");
        }
      } else {
        System.out.println("DEBUG: Found existing PT profile with ID = " + ptProfile.getId());
      }

      // Get certificates for this profile
      java.util.List<com.gym.model.PTCertificate> certificates = ptCertificateDAO.findByPtProfileId(ptProfile.getId());

      // Set profile data for display
      request.setAttribute("ptProfile", ptProfile);
      request.setAttribute("certificates", certificates);

      // Check for messages from other servlets (like AddCertificateServlet,
      // ChangePasswordServlet)
      String successMessage = (String) request.getAttribute("successMessage");
      String errorMessage = (String) request.getAttribute("errorMessage");
      String passwordSuccessMessage = (String) request.getAttribute("passwordSuccessMessage");
      String passwordErrorMessage = (String) request.getAttribute("passwordErrorMessage");
      Boolean showAddCertificateModal = (Boolean) request.getAttribute("showAddCertificateModal");

      if (successMessage != null) {
        request.setAttribute("successMessage", successMessage);
      }
      if (errorMessage != null) {
        request.setAttribute("errorMessage", errorMessage);
      }
      if (passwordSuccessMessage != null) {
        request.setAttribute("passwordSuccessMessage", passwordSuccessMessage);
      }
      if (passwordErrorMessage != null) {
        request.setAttribute("passwordErrorMessage", passwordErrorMessage);
      }
      if (showAddCertificateModal != null) {
        request.setAttribute("showAddCertificateModal", showAddCertificateModal);
      }

      // Forward to profile page
      request.getRequestDispatcher("/views/PT/profile.jsp").forward(request, response);

    } catch (Exception e) {
      System.err.println("Error loading PT profile: " + e.getMessage());
      e.printStackTrace();
      request.setAttribute("errorMessage", "Có lỗi xảy ra khi tải thông tin hồ sơ: " + e.getMessage());
      request.getRequestDispatcher("/views/PT/profile.jsp").forward(request, response);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }
}
