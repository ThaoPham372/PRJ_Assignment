package com.gym.controller;

import com.gym.service.RegistrationService;
import com.gym.service.RegistrationService.RegisterRequest;
import com.gym.service.RegistrationService.RegisterResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * RegisterServlet - Handles user registration requests
 * GET: Show registration form
 * POST: Process registration
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private RegistrationService registrationService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.registrationService = new RegistrationService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Forward to registration JSP
        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get form parameters
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Create registration request
        RegisterRequest registerRequest = new RegisterRequest(username, email, password, confirmPassword);
        
        // Set IP address and user agent for audit
        registerRequest.setIpAddress(getClientIpAddress(request));
        registerRequest.setUserAgent(request.getHeader("User-Agent"));

        // Process registration
        RegisterResult result = registrationService.register(registerRequest);

        if (result.isSuccess()) {
            // Registration successful
            request.setAttribute("registerSuccess", true);
            request.setAttribute("successMessage", result.getMessage());
            
            // Clear form data
            request.setAttribute("username", "");
            request.setAttribute("email", "");
        } else {
            // Registration failed
            request.setAttribute("registerSuccess", false);
            request.setAttribute("errors", result.getErrors());
            
            // Keep form data for re-display
            request.setAttribute("username", username);
            request.setAttribute("email", email);
        }

        // Forward back to registration JSP
        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }

    /**
     * Get client IP address
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}
