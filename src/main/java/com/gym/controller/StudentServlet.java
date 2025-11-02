package com.gym.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.gym.model.Student;
import com.gym.service.StudentService;
import com.gym.service.StudentServiceImpl;

/**
 * StudentServlet - Handles student management page
 * URL: /pt/students
 */
@WebServlet(name = "StudentServlet", urlPatterns = { "/pt/students" })
public class StudentServlet extends HttpServlet {

  private StudentService studentService;

  @Override
  public void init() throws ServletException {
    super.init();
    this.studentService = new StudentServiceImpl();
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

    // Check if user has PT role (optional - uncomment if role checking is needed)
    /*
     * @SuppressWarnings("unchecked")
     * java.util.List<String> userRoles = (java.util.List<String>)
     * session.getAttribute("userRoles");
     * if (userRoles == null || !userRoles.contains("PT")) {
     * response.sendRedirect(request.getContextPath() + "/home");
     * return;
     * }
     */

    try {
      // Get search and filter parameters
      String searchTerm = request.getParameter("search");
      String packageFilter = request.getParameter("package");

      // Get students based on filters
      List<Student> students;
      if (searchTerm != null && !searchTerm.trim().isEmpty()) {
        students = studentService.searchStudents(searchTerm);
      } else if (packageFilter != null && !packageFilter.trim().isEmpty() && !packageFilter.equals("")) {
        //students = studentService.getStudentsByPackage(packageFilter);
      } else {
        students = studentService.getAllStudents();
      }

      // Forward to student management page
      request.getRequestDispatcher("/views/PT/student_management.jsp").forward(request, response);

    } catch (Exception e) {
      System.err.println("Error loading students: " + e.getMessage());
      e.printStackTrace();
      request.setAttribute("errorMessage", "Có lỗi xảy ra khi tải danh sách học viên: " + e.getMessage());
      request.getRequestDispatcher("/views/PT/student_management.jsp").forward(request, response);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // For now, POST redirects to GET
    doGet(request, response);
  }
}
