package com.gym.controller;

import com.gym.service.LoginService;
import com.gym.service.LoginService.LoginResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class LoginServlet extends HttpServlet {
    
    private LoginService loginService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.loginService = new LoginService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Redirect GET requests to login page
        response.sendRedirect(request.getContextPath() + "/views/login.jsp");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get form parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");
        
        // Authenticate user
        LoginResult result = loginService.authenticate(username, password);
        
        if (result.isSuccess()) {
            // Login successful
            HttpSession session = request.getSession(true);
            
            // Store user info in session
            session.setAttribute("user", result.getUser());
            session.setAttribute("userRoles", result.getRoles());
            session.setAttribute("isLoggedIn", true);
            
            // Set session timeout based on remember me
            if ("on".equals(rememberMe)) {
                session.setMaxInactiveInterval(7 * 24 * 60 * 60); // 7 days
            } else {
                session.setMaxInactiveInterval(30 * 60); // 30 minutes
            }
            
            // Redirect based on user role
            String redirectUrl = determineRedirectUrl(result.getRoles());
            response.sendRedirect(request.getContextPath() + redirectUrl);
            
        } else {
            // Login failed - redirect back to login page with error
            request.setAttribute("loginError", true);
            request.setAttribute("errors", result.getErrors());
            request.setAttribute("username", username); // Preserve username
            
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }
    
    /**
     * Determine redirect URL based on user roles
     */
    private String determineRedirectUrl(List<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return "/home"; // Default redirect
        }
        
        // Check for admin role first
        if (roles.contains("ADMIN")) {
            return "/admin/dashboard";
        }
        
        // Check for PT role
        if (roles.contains("PT")) {
            return "/pt/dashboard";
        }
        
        // Default for regular users
        return "/home";
    }
}
