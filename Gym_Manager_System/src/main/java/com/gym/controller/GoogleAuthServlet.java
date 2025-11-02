package com.gym.controller;

import com.gym.model.User;
import com.gym.service.GoogleAuthService;
import com.gym.service.GoogleAuthService.AuthResult;
import com.gym.service.GoogleAuthService.VerifyResult;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "GoogleAuthServlet", urlPatterns = {"/auth/google-login", "/auth/google-register"})
public class GoogleAuthServlet extends HttpServlet {

    private GoogleAuthService googleAuthService;

    @Override
    public void init() throws ServletException {
        super.init();
        String clientId = getServletContext().getInitParameter("google.client.id");
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
        session.setAttribute("user", result.user);
        session.setAttribute("userRoles", result.roles);
        session.setAttribute("isLoggedIn", true);
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
        session.setAttribute("user", result.user);
        session.setAttribute("userRoles", result.roles);
        session.setAttribute("isLoggedIn", true);
        String redirectUrl = determineRedirectUrl(result.roles);
        sendSuccess(out, "Registration successful", request.getContextPath() + redirectUrl);
    }

    // Token verification is handled by GoogleAuthService

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
        if (roles == null || roles.isEmpty()) return "/home";
        if (roles.contains("ADMIN")) return "/admin/dashboard";
        if (roles.contains("PT")) return "/pt/dashboard";
        return "/home";
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


