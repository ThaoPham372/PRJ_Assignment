package controller;

import service.GoogleAuthService;
import service.GoogleAuthService.AuthResult;
import service.GoogleAuthService.VerifyResult;
import Utils.ConfigManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "GoogleAuthServlet", urlPatterns = {"/auth/google-login", "/auth/google-register"})
public class GoogleAuthServlet extends BaseAuthServlet {

    private GoogleAuthService googleAuthService;

    @Override
    public void init() throws ServletException {
        super.init();
        // Read Google Client ID from email.properties via ConfigManager
        String clientId = ConfigManager.getInstance().getGoogleClientId();
        if (clientId == null || clientId.trim().isEmpty()) {
            System.err.println("[GoogleAuthServlet] ERROR: google.client.id is missing in email.properties");
        } else {
            System.out.println("[GoogleAuthServlet] Google Client ID loaded successfully");
        }
        this.googleAuthService = new GoogleAuthService(clientId);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String servletPath = request.getServletPath();
        PrintWriter out = response.getWriter();

        try {
            String credential = request.getParameter("credential");
            String jsonBody = null;
            if (credential == null || credential.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                String line;
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                jsonBody = sb.toString();
                credential = extractCredential(jsonBody);
            }

            if (credential == null || credential.isEmpty()) {
                sendError(out, "Missing Google credential");
                return;
            }

            VerifyResult verify = googleAuthService.verifyTokenAndExtract(credential);
            if (!verify.success) {
                sendError(out, "Invalid Google token");
                return;
            }

            if ("/auth/google-login".equals(servletPath)) {
                handleLogin(request, out, verify);
            } else if ("/auth/google-register".equals(servletPath)) {
                handleRegister(request, out, verify);
            } else {
                sendError(out, "Invalid endpoint");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(out, "Server error: " + e.getMessage());
        }
    }

    private void handleLogin(HttpServletRequest request, PrintWriter out, VerifyResult verify) throws IOException {
        AuthResult result = googleAuthService.loginWithGoogle(verify.googleUser);
        if (!result.success) {
            sendError(out, result.errors != null && !result.errors.isEmpty() ? result.errors.get(0) : "Login failed");
            return;
        }
        HttpSession session = request.getSession(true);
        
        // Sử dụng setupUserSession từ BaseAuthServlet để đảm bảo setup session đầy đủ
        // Bao gồm: user, userRoles, isLoggedIn, userId, và member (nếu role là MEMBER)
        String role = extractRoleFromResult(result);
        setupUserSession(session, result.user, role);
        
        String redirectUrl = determineRedirectUrl(result.roles);
        sendSuccess(out, "Login successful", request.getContextPath() + redirectUrl);
    }

    private void handleRegister(HttpServletRequest request, PrintWriter out, VerifyResult verify) throws IOException {
        AuthResult result = googleAuthService.registerWithGoogle(verify.googleUser);
        if (!result.success) {
            sendError(out, result.errors != null && !result.errors.isEmpty() ? result.errors.get(0) : "Registration failed");
            return;
        }
        HttpSession session = request.getSession(true);
        
        // Sử dụng setupUserSession từ BaseAuthServlet để đảm bảo setup session đầy đủ
        // Bao gồm: user, userRoles, isLoggedIn, userId, và member (nếu role là MEMBER)
        String role = extractRoleFromResult(result);
        setupUserSession(session, result.user, role);
        
        String redirectUrl = determineRedirectUrl(result.roles);
        sendSuccess(out, "Registration successful", request.getContextPath() + redirectUrl);
    }

    // Token verification is handled by GoogleAuthService

    /**
     * Extract role từ AuthResult để sử dụng với setupUserSession
     * Lấy role đầu tiên từ roles list hoặc từ user.role
     */
    private String extractRoleFromResult(AuthResult result) {
        if (result.roles != null && !result.roles.isEmpty()) {
            return result.roles.get(0);
        }
        if (result.user != null && result.user.getRole() != null) {
            return result.user.getRole();
        }
        return null;
    }

    private String extractCredential(String json) {
        if (json == null) return null;
        int keyIdx = json.indexOf("\"credential\"");
        if (keyIdx >= 0) {
            int colon = json.indexOf(':', keyIdx);
            int firstQuote = json.indexOf('"', colon + 1);
            int secondQuote = json.indexOf('"', firstQuote + 1);
            if (colon > 0 && firstQuote > 0 && secondQuote > firstQuote) {
                return json.substring(firstQuote + 1, secondQuote);
            }
        }
        return null;
    }

    private String determineRedirectUrl(java.util.List<String> roles) {
        if (roles == null || roles.isEmpty()) {
            System.out.println("[GoogleAuthServlet] No roles found, redirecting to /member/dashboard");
            return "/member/dashboard";
        }
        if (roles.contains("ADMIN")) {
            System.out.println("[GoogleAuthServlet] User is ADMIN, redirecting to /admin/dashboard");
            return "/admin/dashboard";
        }
        if (roles.contains("PT")) {
            System.out.println("[GoogleAuthServlet] User is PT, redirecting to /pt/dashboard");
            return "/pt/dashboard";
        }
        // Default to member dashboard for USER, MEMBER, or any other role
        System.out.println("[GoogleAuthServlet] User roles: " + roles.toString() + ", redirecting to /member/dashboard");
        return "/member/dashboard";
    }

    private void sendSuccess(PrintWriter out, String message, String redirectUrl) {
        out.println("{");
        out.println("  \"success\": true,");
        out.println("  \"message\": \"" + message + "\",");
        out.println("  \"redirectUrl\": \"" + redirectUrl + "\"");
        out.println("}");
    }

    private void sendError(PrintWriter out, String message) {
        out.println("{");
        out.println("  \"success\": false,");
        out.println("  \"message\": \"" + message + "\"");
        out.println("}");
    }

}