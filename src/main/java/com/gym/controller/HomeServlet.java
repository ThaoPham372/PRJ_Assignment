package com.gym.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HomeServlet handles requests for the home page
 * Displays gym information, membership packages, coaches, and contact form
 */
@WebServlet(name = "HomeServlet", urlPatterns = { "/home", "/HomeServlet" })
public class HomeServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    // Forward đến home.jsp
    request.getRequestDispatcher("/home.jsp").forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // Forward đến home.jsp
    request.getRequestDispatcher("/home.jsp").forward(request, response);
  }
}