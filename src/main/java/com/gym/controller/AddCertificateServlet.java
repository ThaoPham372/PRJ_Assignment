package com.gym.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.gym.dao.PTCertificateDAO;
import com.gym.dao.PTProfileDAO;
import com.gym.model.PTCertificate;
import com.gym.model.PTProfile;

/**
 * AddCertificateServlet - Handles certificate addition for PT profiles
 * URL: /add-certificate
 */
@WebServlet(name = "AddCertificateServlet", urlPatterns = { "/add-certificate" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10, // 10MB
    maxRequestSize = 1024 * 1024 * 20 // 20MB
)
public class AddCertificateServlet extends HttpServlet {

  private PTProfileDAO ptProfileDAO;
  private PTCertificateDAO ptCertificateDAO;
  private static final String UPLOAD_DIR = "uploads/certificates";

  @Override
  public void init() throws ServletException {
    super.init();
    this.ptProfileDAO = new PTProfileDAO();
    this.ptCertificateDAO = new PTCertificateDAO();

    // Create upload directory if it doesn't exist
    String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
    File uploadDir = new File(uploadPath);
    if (!uploadDir.exists()) {
      uploadDir.mkdirs();
    }
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
      // Get form parameters
      String certificateName = request.getParameter("certificateName");
      String certificateType = request.getParameter("certificateType");
      String issuingOrganization = request.getParameter("issuingOrganization");
      String certificateNumber = request.getParameter("certificateNumber");
      String issueDate = request.getParameter("issueDate");
      String expiryDate = request.getParameter("expiryDate");

      System.out.println("DEBUG: Adding certificate for user_id: " + userId);
      System.out.println("DEBUG: Certificate name: " + certificateName);

      // Validate required fields
      if (certificateName == null || certificateName.trim().isEmpty()) {
        request.setAttribute("errorMessage", "Tên chứng chỉ không được để trống!");
        request.setAttribute("showAddCertificateModal", true);
        request.getRequestDispatcher("/pt/profile").forward(request, response);
        return;
      }

      if (certificateType == null || certificateType.trim().isEmpty()) {
        request.setAttribute("errorMessage", "Loại chứng chỉ không được để trống!");
        request.setAttribute("showAddCertificateModal", true);
        request.getRequestDispatcher("/pt/profile").forward(request, response);
        return;
      }

      // Get PT profile
      PTProfile profile = ptProfileDAO.findByUserId(userId);
      if (profile == null) {
        request.setAttribute("errorMessage", "Không tìm thấy hồ sơ PT!");
        request.setAttribute("showAddCertificateModal", true);
        request.getRequestDispatcher("/pt/profile").forward(request, response);
        return;
      }

      // Handle file upload
      String certificateImage = null;
      Part filePart = request.getPart("certificateImage");
      if (filePart != null && filePart.getSize() > 0) {
        // Validate file type
        String contentType = filePart.getContentType();
        if (!contentType.startsWith("image/")) {
          request.setAttribute("errorMessage", "File ảnh không đúng định dạng!");
          request.setAttribute("showAddCertificateModal", true);
          request.getRequestDispatcher("/pt/profile").forward(request, response);
          return;
        }

        // Generate unique filename
        String originalFileName = getFileName(filePart);
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        // Save file
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
        Path filePath = Paths.get(uploadPath + File.separator + uniqueFileName);
        Files.copy(filePart.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        certificateImage = uniqueFileName;
        System.out.println("DEBUG: Certificate image uploaded: " + uniqueFileName);
      }

      // Create certificate object
      PTCertificate certificate = new PTCertificate();
      certificate.setPtProfileId(profile.getId());
      certificate.setCertificateName(certificateName);
      certificate.setCertificateType(certificateType);
      certificate.setIssuingOrganization(issuingOrganization);
      certificate.setCertificateNumber(certificateNumber);
      certificate.setIssueDate(issueDate);
      certificate.setExpiryDate(expiryDate);
      certificate.setCertificateImage(certificateImage);
      certificate.setStatus("ACTIVE");

      // Save to database
      Long certificateId = ptCertificateDAO.createCertificate(certificate);

      if (certificateId > 0) {
        System.out.println("DEBUG: Certificate created successfully with ID: " + certificateId);
        request.setAttribute("successMessage", "Thêm chứng chỉ thành công!");
      } else {
        System.out.println("ERROR: Failed to create certificate");
        request.setAttribute("errorMessage", "Có lỗi xảy ra khi thêm chứng chỉ!");
        request.setAttribute("showAddCertificateModal", true);
      }

      // Forward back to profile page
      request.getRequestDispatcher("/pt/profile").forward(request, response);

    } catch (Exception e) {
      System.err.println("ERROR: Exception during certificate creation: " + e.getMessage());
      e.printStackTrace();
      request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
      request.setAttribute("showAddCertificateModal", true);
      request.getRequestDispatcher("/pt/profile").forward(request, response);
    }
  }

  /**
   * Extract filename from Part
   */
  private String getFileName(Part part) {
    String contentDisposition = part.getHeader("content-disposition");
    String[] tokens = contentDisposition.split(";");
    for (String token : tokens) {
      if (token.trim().startsWith("filename")) {
        return token.substring(token.indexOf("=") + 2, token.length() - 1);
      }
    }
    return "unknown";
  }
}
