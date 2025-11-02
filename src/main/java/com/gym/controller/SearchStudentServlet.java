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
 * SearchStudentServlet - Handles student search functionality
 * URL: /pt/students/search
 */
@WebServlet(name = "SearchStudentServlet", urlPatterns = { "/pt/students/search" })
public class SearchStudentServlet extends HttpServlet {

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

    try {
      // Get search keyword from request parameter
      String keyword = request.getParameter("keyword");
      if (keyword == null) {
        keyword = request.getParameter("search"); // Fallback for compatibility
      }

      // Get package filter if any
      String packageFilter = request.getParameter("package");

      // Get students based on search keyword
      List<Student> students;
      if (keyword != null && !keyword.trim().isEmpty()) {
        // Perform search
        students = studentService.searchStudents(keyword.trim());
      } else if (packageFilter != null && !packageFilter.trim().isEmpty() && !packageFilter.equals("")) {
        // Filter by package if no keyword
        //students = studentService.getStudentsByPackage(packageFilter);
      } else {
        // No search keyword, return all students
        students = studentService.getAllStudents();
      }

      // Get statistics (based on all students, not filtered results)

      // Set attributes for JSP
    

      // Forward to student management page
      request.getRequestDispatcher("/views/PT/student_management.jsp").forward(request, response);

    } catch (Exception e) {
      System.err.println("Error searching students: " + e.getMessage());
      e.printStackTrace();
      request.setAttribute("errorMessage", "Có lỗi xảy ra khi tìm kiếm học viên: " + e.getMessage());
      request.getRequestDispatcher("/views/PT/student_management.jsp").forward(request, response);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // POST redirects to GET
    doGet(request, response);
  }
}
