package controller;

import service.LoginService;
import service.LoginService.LoginResult;
import service.RegistrationService;
import service.RegistrationService.RegisterRequest;
import service.RegistrationService.RegisterResult;
import Utils.ConfigManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import java.io.IOException;

/**
 * AuthServlet - Unified authentication controller
 * Handles login, registration, and logout
 * 
 * URL Patterns:
 * - /auth, /login, /auth/login -> Login
 * - /register, /auth/register -> Register
 * - /logout -> Logout
 */
@WebServlet(name = "AuthServlet", urlPatterns = {
    "/auth",
    "/login",
    "/auth/login",
    "/register",
    "/auth/register",
    "/logout"
})
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
            if ("/register".equals(path)) {
                action = "register";
            } else if ("/logout".equals(path)) {
                action = "logout";
            } else {
                action = "login";
            }
        }

        // Load Google Client ID from ConfigManager (used by both login and register)
        String googleClientId = ConfigManager.getInstance().getGoogleClientId();
        request.setAttribute("googleClientId", googleClientId);
        
        switch (action) {
            case "register":
                request.getRequestDispatcher("/views/register.jsp").forward(request, response);
                break;
            
            case "logout":
                handleLogout(request, response);
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
            if ("/register".equals(path)) {
                action = "register";
            } else if ("/logout".equals(path)) {
                action = "logout";
            } else {
                action = "login";
            }
        }

        switch (action) {
            case "register":
                handleRegister(request, response);
                break;
            
            case "logout":
                handleLogout(request, response);
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
            String role = result.getRole();
            
            // Convert role to List for consistency with GoogleAuthServlet
            java.util.List<String> roles = new java.util.ArrayList<>();
            if (role != null && !role.trim().isEmpty()) {
                roles.add(role);
            }

            HttpSession session = request.getSession(true);
                      
            session.setAttribute("user", user);
            session.setAttribute("userRoles", roles);  // Changed from String to List
            session.setAttribute("isLoggedIn", true);
            session.setAttribute("userId", user.getId());
            
     
            if ("on".equals(rememberMe)) {
                session.setMaxInactiveInterval(7 * 24 * 60 * 60);
                System.out.println("[AuthServlet] Remember me enabled - session timeout: 7 days");
            } else {
                session.setMaxInactiveInterval(30 * 60);
                System.out.println("[AuthServlet] Session timeout: 30 minutes");
            }

        
            response.sendRedirect(request.getContextPath() + "/home");

        } else {
           
            request.setAttribute("loginError", true);
            request.setAttribute("errors", result.getErrors());
            request.setAttribute("username", username);
            // Load Google Client ID from ConfigManager and pass to JSP
            String googleClientId = ConfigManager.getInstance().getGoogleClientId();
            request.setAttribute("googleClientId", googleClientId);
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

        RegisterRequest registerRequest = new RegisterRequest(username, email, password, confirmPassword);
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

    /**
     * Handle logout - Invalidate session and redirect to home
     */
    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("[AuthServlet] Logout request received");
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            String username = "Unknown";
            User user = (User) session.getAttribute("user");
            if (user != null) {
                username = user.getUsername();
            }
            
  
            // Invalidate session - xóa tất cả attributes và dừng session
            session.invalidate();
            System.out.println("[AuthServlet] Session invalidated successfully");
        } else {
            System.out.println("[AuthServlet] No active session found");
        }
        
  
        response.sendRedirect(request.getContextPath() + "/home");
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


