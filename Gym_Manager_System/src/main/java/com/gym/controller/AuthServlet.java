package com.gym.controller;

import com.gym.service.LoginService;
import com.gym.service.LoginService.LoginResult;
import com.gym.service.RegistrationService;
import com.gym.service.RegistrationService.RegisterRequest;
import com.gym.service.RegistrationService.RegisterResult;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.gym.model.User;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AuthServlet", urlPatterns = {"/auth"})
public class AuthServlet extends HttpServlet {

    private LoginService loginService;
    private RegistrationService registrationService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.loginService = new LoginService();
        this.registrationService = new RegistrationService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) {
            String path = request.getServletPath();
            if ("/register".equals(path)) action = "register"; else action = "login";
        }

        switch (action) {
            case "register":
                request.getRequestDispatcher("/views/register.jsp").forward(request, response);
                break;
            case "login":
            default:
                request.getRequestDispatcher("/views/login.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) {
            String path = request.getServletPath();
            if ("/register".equals(path)) action = "register"; else action = "login";
        }

        switch (action) {
            case "register":
                handleRegister(request, response);
                break;
            case "login":
            default:
                handleLogin(request, response);
                break;
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");

        System.out.println("[AuthServlet] Login attempt for username: " + username);
        
        LoginResult result = loginService.authenticate(username, password);
        if (result.isSuccess()) {
            User user = result.getUser();
            List<String> roles = result.getRoles();
            
            System.out.println("[AuthServlet] Login successful!");
            System.out.println("[AuthServlet] User ID: " + (user != null ? user.getId() : "NULL"));
            System.out.println("[AuthServlet] Username: " + (user != null ? user.getUsername() : "NULL"));
            System.out.println("[AuthServlet] Roles: " + (roles != null ? roles.toString() : "NULL"));
            
            HttpSession session = request.getSession(true);
            System.out.println("[AuthServlet] Session created: " + session.getId());
            System.out.println("[AuthServlet] Session isNew: " + session.isNew());
            
            session.setAttribute("user", user);
            session.setAttribute("userRoles", roles);
            session.setAttribute("isLoggedIn", true);
            
            System.out.println("[AuthServlet] Session attributes set:");
            System.out.println("[AuthServlet]   - user: " + (session.getAttribute("user") != null ? "SET" : "NULL"));
            System.out.println("[AuthServlet]   - userRoles: " + (session.getAttribute("userRoles") != null ? session.getAttribute("userRoles").toString() : "NULL"));
            System.out.println("[AuthServlet]   - isLoggedIn: " + session.getAttribute("isLoggedIn"));

            if ("on".equals(rememberMe)) {
                session.setMaxInactiveInterval(7 * 24 * 60 * 60);
                System.out.println("[AuthServlet] Remember me enabled - session timeout: 7 days");
            } else {
                session.setMaxInactiveInterval(30 * 60);
                System.out.println("[AuthServlet] Session timeout: 30 minutes");
            }

            String redirectUrl = determineRedirectUrl(roles);
            System.out.println("[AuthServlet] Redirecting to: " + redirectUrl);
            response.sendRedirect(request.getContextPath() + redirectUrl);
        } else {
            System.out.println("[AuthServlet] Login failed!");
            System.out.println("[AuthServlet] Errors: " + (result.getErrors() != null ? result.getErrors().toString() : "NULL"));
            request.setAttribute("loginError", true);
            request.setAttribute("errors", result.getErrors());
            request.setAttribute("username", username);
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        RegisterRequest registerRequest = new RegisterRequest(username, name, email, password, confirmPassword);
        registerRequest.setIpAddress(getClientIpAddress(request));
        registerRequest.setUserAgent(request.getHeader("User-Agent"));

        RegisterResult result = registrationService.register(registerRequest);
        if (result.isSuccess()) {
            request.setAttribute("registerSuccess", true);
            request.setAttribute("successMessage", result.getMessage());
            request.setAttribute("username", "");
            request.setAttribute("name", "");
            request.setAttribute("email", "");
        } else {
            request.setAttribute("registerSuccess", false);
            request.setAttribute("errors", result.getErrors());
            request.setAttribute("username", username);
            request.setAttribute("name", name);
            request.setAttribute("email", email);
        }

        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }

    private String determineRedirectUrl(List<String> roles) {
        if (roles == null || roles.isEmpty()) {
            System.out.println("[AuthServlet] No roles found, redirecting to /member/dashboard");
            return "/member/dashboard";
        }
        if (roles.contains("ADMIN")) {
            System.out.println("[AuthServlet] User is ADMIN, redirecting to /admin/dashboard");
            return "/admin/dashboard";
        }
        if (roles.contains("PT")) {
            System.out.println("[AuthServlet] User is PT, redirecting to /pt/dashboard");
            return "/pt/dashboard";
        }
        // Default to member dashboard for USER, MEMBER, or any other role
        System.out.println("[AuthServlet] User roles: " + roles.toString() + ", redirecting to /member/dashboard");
        return "/member/dashboard";
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
}


