package com.gym.controller;

import java.io.IOException;

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
    "/pt/profile",
    "/pt/schedule",
    "/pt/students",
    "/pt/chat",
    "/pt/reports"
})
public class PTDashboardServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession(false);

    // Check if user is logged in and is a PT
    // TODO: Implement authentication logic
    // For now, we'll just forward to the appropriate page

    String path = request.getServletPath();
    String forwardPath = "";

    switch (path) {
      case "/pt/dashboard":
      case "/pt/home":
        forwardPath = "/views/PT/homePT.jsp";
        break;
      case "/pt/profile":
        forwardPath = "/views/PT/profile.jsp";
        break;
      case "/pt/schedule":
        forwardPath = "/views/PT/training_schedule.jsp";
        break;
      case "/pt/students":
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

