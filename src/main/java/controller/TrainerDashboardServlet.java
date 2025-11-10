package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

/**
 * TrainerDashboardServlet - Servlet để điều hướng các trang PT/Trainer
 * Xử lý các request từ Personal Trainer
 */
@WebServlet(name = "TrainerDashboardServlet", urlPatterns = {
    "/pt/home",
    "/pt/dashboard",
    "/pt/profile",
    "/pt/schedule",
    "/pt/students",
    "/pt/chat",
    "/pt/reports"
})
public class TrainerDashboardServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    // Check if user is logged in
    HttpSession session = req.getSession(false);
    if (session == null || session.getAttribute("user") == null) {
      resp.sendRedirect(req.getContextPath() + "/login");
      return;
    }

    // Get user from session
    User user = (User) session.getAttribute("user");

    // Optional: Check if user has PT/Trainer role
    // Uncomment if you have role checking in place
    // if (!hasTrainerRole(user)) {
    // resp.sendRedirect(req.getContextPath() + "/home");
    // return;
    // }

    String reqURI = req.getRequestURI();
    String contextPath = req.getContextPath();
    String path = reqURI.substring(contextPath.length());

    String jspPath;

    switch (path) {
      case "/pt/home", "/pt/dashboard" ->
        jspPath = "/views/PT/homePT.jsp";
      case "/pt/profile" ->
        jspPath = "/views/PT/profile.jsp";
      case "/pt/schedule" ->
        jspPath = "/views/PT/schedule.jsp";
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
    // Handle POST requests (for forms, updates, etc.)
    doGet(req, resp);
  }

  /**
   * Check if user has Trainer role
   * Uncomment and modify this method based on your role checking logic
   */
  // private boolean hasTrainerRole(User user) {
  // if (user.getRoles() != null) {
  // return user.getRoles().stream()
  // .anyMatch(role -> "PT".equals(role) || "TRAINER".equals(role));
  // }
  // return false;
  // }

  @Override
  public String getServletInfo() {
    return "Trainer Dashboard Servlet for GymFit Management System";
  }
}
