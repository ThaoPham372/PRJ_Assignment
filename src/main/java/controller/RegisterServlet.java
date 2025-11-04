package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import service.RegistrationService;
import service.RegistrationService.RegisterRequest;
import service.RegistrationService.RegisterResult;

/**
 * RegisterServlet - Handles user registration requests
 * GET: Show registration form
 * POST: Process registration
 */
@WebServlet(name = "RegisterServlet", urlPatterns = { "/register" })
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
        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RegisterRequest registerRequest = createRegisterRequest(request);
        RegisterResult result = registrationService.register(registerRequest);

        if (result.isSuccess()) {
            request.setAttribute("registerSuccess", true);
            request.setAttribute("successMessage", result.getMessage());

            request.setAttribute("username", "");
            request.setAttribute("email", "");
        } else {
            request.setAttribute("registerSuccess", false);
            request.setAttribute("errors", result.getErrors());

            request.setAttribute("username", registerRequest.getUsername());
            request.setAttribute("email", registerRequest.getEmail());
        }

        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }

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

    private RegisterRequest createRegisterRequest(HttpServletRequest request) {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String role = request.getParameter("role"); 

        RegisterRequest registerRequest = new RegisterRequest(username, email, password, confirmPassword);
        
        registerRequest.setRole(role); 
        registerRequest.setIpAddress(getClientIpAddress(request));
        registerRequest.setUserAgent(request.getHeader("User-Agent"));
        
        return registerRequest;
    }
}
