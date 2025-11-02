package com.gym.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * AdminDashboardServlet - Servlet để điều hướng các trang admin
 * Đây là servlet giả để test hiển thị dashboard admin
 */
@WebServlet(name = "AdminDashboardServlet", urlPatterns = {
    "/admin/home",
    "/admin/dashboard",
    "/admin/profile",
    "/admin/account-management",
    "/admin/member-management",
    "/admin/service-schedule",
    "/admin/trainer-management",
    "/admin/order-management",
    "/admin/payment-finance",
    "/admin/reports"
})
public class AdminDashboardServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    // Get the request URI to determine which page to display
    String requestURI = request.getRequestURI();
    String contextPath = request.getContextPath();
    String path = requestURI.substring(contextPath.length());

    // Check if user is logged in and has ADMIN role
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("isLoggedIn") == null) {
      response.sendRedirect(request.getContextPath() + "/views/login.jsp");
      return;
    }

    // Check if user has ADMIN role
    @SuppressWarnings("unchecked")
    java.util.List<String> userRoles = (java.util.List<String>) session.getAttribute("userRoles");
    if (userRoles == null || !userRoles.contains("ADMIN")) {
      response.sendRedirect(request.getContextPath() + "/home");
      return;
    }
    String jspPath = "";

    switch (path) {
      case "/admin/home":
        jspPath = "/views/admin/admin_home.jsp";
        break;
      case "/admin/dashboard":
        jspPath = "/views/admin/dashboard.jsp";
        break;
      case "/admin/profile":
        jspPath = "/views/admin/profile.jsp";
        break;
      case "/admin/account-management":
        jspPath = "/views/admin/account_management.jsp";
        break;
      case "/admin/member-management":
        jspPath = "/views/admin/member_management.jsp";
        break;
      case "/admin/service-schedule":
        jspPath = "/views/admin/service_schedule.jsp";
        break;
      case "/admin/trainer-management":
        jspPath = "/views/admin/trainer_management.jsp";
        break;
      case "/admin/order-management":
        jspPath = "/views/admin/order_management.jsp";
        break;
      case "/admin/payment-finance":
        jspPath = "/views/admin/payment_finance.jsp";
        break;
      case "/admin/reports":
        jspPath = "/views/admin/reports.jsp";
        break;
      default:
        jspPath = "/views/admin/admin_home.jsp";
        break;
    }

    // Set page title attribute
    request.setAttribute("pageTitle", "Admin Dashboard - GymFit");

    // Forward to the appropriate JSP
    request.getRequestDispatcher(jspPath).forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // Handle POST requests (for forms, updates, etc.)
    doGet(request, response);
  }

  @Override
  public String getServletInfo() {
    return "Admin Dashboard Servlet for GymFit Management System";
  }
}
