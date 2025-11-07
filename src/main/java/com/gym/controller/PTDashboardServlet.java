package com.gym.controller;

import java.io.IOException;
import java.util.List;

import com.gym.model.Member;
import com.gym.service.MemberService;

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

  private MemberService memberService;

  @Override
  public void init() throws ServletException {
    super.init();
    this.memberService = new MemberService();
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
        // Load member data before forwarding to JSP
        try {
          // Note: MemberService doesn't have getAllActiveMembers method
          // Using empty list for now - can be extended later
          List<Member> members = new java.util.ArrayList<>();
          int totalMembers = 0;
          int activeMembers = 0;
          int achievedGoalCount = 0;

          request.setAttribute("members", members);
          request.setAttribute("students", members); // Keep for backward compatibility
          request.setAttribute("totalMembers", totalMembers);
          request.setAttribute("totalStudents", totalMembers); // Keep for backward compatibility
          request.setAttribute("activeMembers", activeMembers);
          request.setAttribute("activeStudents", activeMembers); // Keep for backward compatibility
          request.setAttribute("achievedGoalCount", achievedGoalCount);
        } catch (Exception e) {
          System.err.println("Error loading members in PTDashboardServlet: " + e.getMessage());
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

