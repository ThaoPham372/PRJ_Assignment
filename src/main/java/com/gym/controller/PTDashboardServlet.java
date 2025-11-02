package com.gym.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet for Personal Trainer Dashboard
 * Handles routing and authentication for PT users
 */
@WebServlet(name = "PTDashboardServlet", urlPatterns = {
    "/pt/dashboard",
    "/pt/home",
    "/pt/schedule",
    "/pt/chat",
    "/pt/reports"
})
public class PTDashboardServlet extends HttpServlet {

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
        forwardPath = "/views/PT/homePT.jsp";
        break;
      case "/pt/profile":
        forwardPath = "/views/PT/profile.jsp";
        break;
      case "/pt/schedule":
        // Đưa sang ScheduleServlet để lấy dữ liệu thật từ DB
        response.sendRedirect(request.getContextPath() + "/ScheduleServlet?action=list");
        return;
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
