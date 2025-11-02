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

import com.gym.dao.PTProfileDAO;
import com.gym.model.PTProfile;

/**
 * UploadAvatarServlet - Handles avatar upload for PT profiles
 * URL: /upload-avatar
 */
@WebServlet(name = "UploadAvatarServlet", urlPatterns = { "/upload-avatar" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 5, // 5MB
    maxRequestSize = 1024 * 1024 * 10 // 10MB
)
public class UploadAvatarServlet extends HttpServlet {

  private PTProfileDAO ptProfileDAO;
  private static final String UPLOAD_DIR = "uploads/avatars";

  @Override
  public void init() throws ServletException {
    super.init();
    this.ptProfileDAO = new PTProfileDAO();

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

    Long userId = (Long) session.getAttribute("userId");
    if (userId == null) {
      response.getWriter().write("error:no_user_id");
      return;
    }

    try {
      // Get uploaded file
      Part filePart = request.getPart("avatar");
      if (filePart == null || filePart.getSize() == 0) {
        response.getWriter().write("error:no_file");
        return;
      }

      // Validate file type
      String contentType = filePart.getContentType();
      if (!contentType.startsWith("image/")) {
        response.getWriter().write("error:invalid_file_type");
        return;
      }

      // Generate unique filename
      String originalFileName = getFileName(filePart);
      String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
      String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

      // Get upload path
      String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
      Path filePath = Paths.get(uploadPath + File.separator + uniqueFileName);

      // Save file
      Files.copy(filePart.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

      System.out.println("DEBUG: Avatar uploaded successfully: " + uniqueFileName);

      // Update database
      PTProfile profile = ptProfileDAO.findByUserId(userId);
      if (profile != null) {
        // Delete old avatar if exists
        if (profile.getAvatar() != null && !profile.getAvatar().isEmpty()) {
          String oldFilePath = uploadPath + File.separator + profile.getAvatar();
          File oldFile = new File(oldFilePath);
          if (oldFile.exists()) {
            oldFile.delete();
          }
        }

        // Update profile with new avatar
        profile.setAvatar(uniqueFileName);
        boolean updated = ptProfileDAO.updatePTProfile(profile);

        if (updated) {
          System.out.println("DEBUG: Avatar updated in database successfully");
          response.getWriter().write("success:avatar_uploaded");
        } else {
          System.out.println("ERROR: Failed to update avatar in database");
          response.getWriter().write("error:database_update_failed");
        }
      } else {
        System.out.println("ERROR: PT profile not found for user_id: " + userId);
        response.getWriter().write("error:profile_not_found");
      }

    } catch (Exception e) {
      System.err.println("ERROR: Exception during avatar upload: " + e.getMessage());
      e.printStackTrace();
      response.getWriter().write("error:upload_failed");
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

