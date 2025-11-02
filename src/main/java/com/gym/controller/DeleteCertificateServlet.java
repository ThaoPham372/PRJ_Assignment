package com.gym.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.gym.dao.PTCertificateDAO;

/**
 * DeleteCertificateServlet - Handles certificate deletion
 * URL: /delete-certificate
 */
@WebServlet(name = "DeleteCertificateServlet", urlPatterns = { "/delete-certificate" })
public class DeleteCertificateServlet extends HttpServlet {

  private PTCertificateDAO ptCertificateDAO;

  @Override
  public void init() throws ServletException {
    super.init();
    this.ptCertificateDAO = new PTCertificateDAO();
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    // Set UTF-8 encoding
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html; charset=UTF-8");

    HttpSession session = request.getSession(false);

    // Check if user is logged in
    if (session == null || session.getAttribute("isLoggedIn") == null) {
      response.getWriter().write("error:not_logged_in");
      return;
    }

    // Check if user has PT role
    @SuppressWarnings("unchecked")
    java.util.List<String> userRoles = (java.util.List<String>) session.getAttribute("userRoles");
    if (userRoles == null || !userRoles.contains("PT")) {
      response.getWriter().write("error:not_pt_user");
      return;
    }

    try {
      // Get certificate ID
      String certificateIdStr = request.getParameter("certificateId");
      if (certificateIdStr == null || certificateIdStr.trim().isEmpty()) {
        response.getWriter().write("error:no_certificate_id");
        return;
      }

      Long certificateId = Long.parseLong(certificateIdStr);
      System.out.println("DEBUG: Deleting certificate ID: " + certificateId);

      // Delete certificate
      boolean deleted = ptCertificateDAO.deleteCertificate(certificateId);

      if (deleted) {
        System.out.println("DEBUG: Certificate deleted successfully");
        response.getWriter().write("success:certificate_deleted");
      } else {
        System.out.println("ERROR: Failed to delete certificate");
        response.getWriter().write("error:delete_failed");
      }

    } catch (NumberFormatException e) {
      System.err.println("ERROR: Invalid certificate ID format");
      response.getWriter().write("error:invalid_certificate_id");
    } catch (Exception e) {
      System.err.println("ERROR: Exception during certificate deletion: " + e.getMessage());
      e.printStackTrace();
      response.getWriter().write("error:deletion_failed");
    }
  }
}

