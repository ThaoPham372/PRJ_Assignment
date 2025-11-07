package com.gym.controller;

import java.io.IOException;
import java.util.List;

import com.gym.model.Student;
import com.gym.service.IStudentService;
import com.gym.service.StudentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet for Personal Trainer Dashboard
 * Handles routing and authentication for PT users
 */
@WebServlet(name = "PTDashboardServlet", urlPatterns = {
    "/pt/dashboard",
    "/pt/home",
    "/pt/homePT.jsp",
    "/pt/profile",
    "/pt/schedule",
    "/pt/chat",
    "/pt/reports",
    "/pt/students"
})
public class PTDashboardServlet extends HttpServlet {

  private IStudentService studentService;

  @Override
  public void init() throws ServletException {
    super.init();
    this.studentService = new StudentService();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession(false);

    // Check if user is logged in and has PT role
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

    String path = request.getServletPath();
    String forwardPath = "";

    switch (path) {
      case "/pt/dashboard":
      case "/pt/home":
      case "/pt/homePT.jsp":
        forwardPath = "/views/PT/homePT.jsp";
        break;
      case "/pt/profile":
        forwardPath = "/views/PT/profile.jsp";
        break;
      case "/pt/schedule":
        forwardPath = "/views/PT/training_schedule.jsp";
        break;

      case "/pt/students":
        // Load student data before forwarding to JSP
        try {
          List<Student> students = studentService.getAllActiveStudents();
          int totalStudents = studentService.getTotalStudentsCount();
          int activeStudents = studentService.getActiveStudentsCount();
          int achievedGoalCount = studentService.getAchievedGoalCount();

          request.setAttribute("students", students);
          request.setAttribute("totalStudents", totalStudents);
          request.setAttribute("activeStudents", activeStudents);
          request.setAttribute("achievedGoalCount", achievedGoalCount);
        } catch (Exception e) {
          System.err.println("Error loading students in PTDashboardServlet: " + e.getMessage());
          e.printStackTrace();
        }
        forwardPath = "/views/PT/student_management.jsp";
        break;

      case "/pt/chat":
        forwardPath = "/views/PT/chat.jsp";
        break;
      case "/pt/reports":
        forwardPath = "/views/PT/reports.jsp";
        break;
      default:
        forwardPath = "/views/PT/homePT.jsp";
        break;
    }

    request.getRequestDispatcher(forwardPath).forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }
}
