package controller;

import DAO.IUserDAO;
import DAO.UserDAO;
import DAO.IMemberDAO;
import DAO.MemberDAO;
import model.User;
import model.Member;
import util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.Date;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * GoogleAuthController - Xử lý Google OAuth authentication
 */
@WebServlet(name = "GoogleAuthController", urlPatterns = {"/auth/google-login", "/auth/google-register"})
public class GoogleAuthController extends HttpServlet {
    
    private IUserDAO userDAO;
    private IMemberDAO memberDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.userDAO = new UserDAO();
        this.memberDAO = new MemberDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        
        String servletPath = request.getServletPath();
        PrintWriter out = response.getWriter();
        
        try {
            // Try to read credential from form first
            String credential = request.getParameter("credential");
            String json = null;
            if (credential == null || credential.isEmpty()) {
                // Read JSON request body
                StringBuilder jsonBuffer = new StringBuilder();
                String line;
                while ((line = request.getReader().readLine()) != null) {
                    jsonBuffer.append(line);
                }
                json = jsonBuffer.toString();
                // Simple JSON parsing (in production, use a proper JSON library like Jackson)
                credential = extractCredential(json);
            }
            System.out.println("[GoogleAuth] Raw body length=" + (json != null ? json.length() : 0));
            System.out.println("[GoogleAuth] Credential length=" + (credential != null ? credential.length() : 0));
            
            if (credential == null || credential.isEmpty()) {
                sendErrorResponse(out, "Missing Google credential");
                return;
            }
            
            // Decode Google JWT token (simplified - in production, use proper JWT library)
            GoogleUserInfo googleUser = decodeGoogleToken(credential);
            
            if (googleUser == null) {
                sendErrorResponse(out, "Invalid Google token");
                return;
            }
            
            // Check if user exists
            User existingUser = userDAO.getUserByEmail(googleUser.getEmail());
            
            if ("/auth/google-login".equals(servletPath)) {
                handleGoogleLogin(existingUser, googleUser, request, response, out);
            } else if ("/auth/google-register".equals(servletPath)) {
                handleGoogleRegister(existingUser, googleUser, request, response, out);
            } else {
                sendErrorResponse(out, "Invalid endpoint");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(out, "Server error: " + e.getMessage());
        }
    }
    
    private void handleGoogleLogin(User existingUser, GoogleUserInfo googleUser, 
                                 HttpServletRequest request, HttpServletResponse response, 
                                 PrintWriter out) throws IOException {
        
        if (existingUser == null) {
            sendErrorResponse(out, "Account not found. Please register first.");
            return;
        }
        
        if (!"active".equals(existingUser.getStatus())) {
            sendErrorResponse(out, "Account is not active");
            return;
        }
        
        // Create session
        if ("member".equals(existingUser.getRole())) {
            Member member = memberDAO.getMemberByUserId(existingUser.getUserId());
            if (member != null) {
                SessionUtil.createUserSession(request, existingUser, member.getMemberId(), member.getMemberCode());
            } else {
                SessionUtil.createUserSession(request, existingUser);
            }
        } else {
            SessionUtil.createUserSession(request, existingUser);
        }
        
        // Return success response
        String redirectUrl = getRedirectUrl(existingUser.getRole(), request.getContextPath());
        sendSuccessResponse(out, "Login successful", redirectUrl);
    }
    
    private void handleGoogleRegister(User existingUser, GoogleUserInfo googleUser, 
                                    HttpServletRequest request, HttpServletResponse response, 
                                    PrintWriter out) throws IOException {
        
        if (existingUser != null) {
            sendErrorResponse(out, "Account already exists. Please login instead.");
            return;
        }
        
        // Try stored procedure first for atomic register
        try {
            try (java.sql.Connection conn = DAO.DBConnection.getConnection()) {
                if (conn != null) {
                    try (java.sql.CallableStatement cs = conn.prepareCall("{call sp_register_member(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {
                        String username = generateUsername(googleUser.getEmail());
                        cs.setString(1, username);
                        cs.setString(2, "GOOGLE_OAUTH");
                        cs.setString(3, googleUser.getEmail());
                        cs.setString(4, googleUser.getName());
                        cs.setString(5, null);
                        cs.setDate(6, null);
                        cs.setString(7, null);
                        cs.registerOutParameter(8, java.sql.Types.INTEGER); // user_id
                        cs.registerOutParameter(9, java.sql.Types.INTEGER); // member_id
                        cs.registerOutParameter(10, java.sql.Types.VARCHAR); // member_code
                        cs.registerOutParameter(11, java.sql.Types.NVARCHAR); // error_message
                        cs.execute();
                        String spError = cs.getString(11);
                        if (spError == null || spError.trim().isEmpty()) {
                            // Build user and session from SP outputs
                            User spUser = userDAO.getUserByEmail(googleUser.getEmail());
                            if (spUser == null) {
                                spUser = new User();
                                spUser.setUserId(cs.getInt(8));
                                spUser.setUsername(username);
                                spUser.setEmail(googleUser.getEmail());
                                spUser.setFullName(googleUser.getName());
                                spUser.setRole("member");
                                spUser.setStatus("active");
                            }
                            // Create session
                            SessionUtil.createUserSession(request, spUser, cs.getInt(9), cs.getString(10));
                            String redirectUrl = getRedirectUrl("member", request.getContextPath());
                            sendSuccessResponse(out, "Registration successful", redirectUrl);
                            return;
                        }
                    } catch (Exception ignoreSp) {
                        // fall back below
                    }
                }
            }
        } catch (Exception ignored) {}
        
        // Fallback to manual DAO flow
        User newUser = new User();
        newUser.setUsername(generateUsername(googleUser.getEmail()));
        newUser.setPassword("GOOGLE_OAUTH");
        newUser.setEmail(googleUser.getEmail());
        newUser.setFullName(googleUser.getName());
        newUser.setAvatarUrl(googleUser.getPicture());
        newUser.setRole("member");
        newUser.setStatus("active");
        
        boolean userCreated = userDAO.createUser(newUser);
        if (!userCreated) {
            sendErrorResponse(out, "Failed to create account");
            return;
        }
        if (newUser.getUserId() > 0 && (newUser.getStatus() == null || !"active".equals(newUser.getStatus()))) {
            userDAO.setUserStatus(newUser.getUserId(), "active");
        }
        
        if (newUser.getUserId() <= 0) {
            User fetched = userDAO.getUserByEmail(newUser.getEmail());
            if (fetched != null) newUser.setUserId(fetched.getUserId());
        }
        
        // Create member record
        Member member = new Member();
        member.setUserId(newUser.getUserId());
        
        // Generate member code
        int lastNumber = memberDAO.getLastMemberNumber();
        String memberCode = "GYM" + java.time.Year.now().getValue() + 
                          String.format("%05d", lastNumber + 1);
        member.setMemberCode(memberCode);
        member.setRegistrationDate(new Date());
        member.setStatus("active");
        
        boolean memberCreated = memberDAO.createMember(member);
        
        Member finalMember = member;
        if (!memberCreated) {
            // Double-check if member actually exists (some drivers may return false despite success)
            Member existing = memberDAO.getMemberByUserId(newUser.getUserId());
            if (existing == null) {
                // Graceful fallback: proceed with user session even if member record not yet visible
                SessionUtil.createUserSession(request, newUser);
                String redirectUrl = getRedirectUrl(newUser.getRole(), request.getContextPath());
                sendSuccessResponse(out, "Registration successful", redirectUrl);
                return;
            } else {
                finalMember = existing;
            }
        }
        
        // Create session
        SessionUtil.createUserSession(request, newUser, finalMember.getMemberId(), finalMember.getMemberCode());
        
        // Return success response
        String redirectUrl = getRedirectUrl(newUser.getRole(), request.getContextPath());
        sendSuccessResponse(out, "Registration successful", redirectUrl);
    }
    
    private String extractCredential(String json) {
        if (json == null) return null;
        // Fast path
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
    
    private GoogleUserInfo decodeGoogleToken(String credential) {
        BufferedReader reader = null;
        try {
            // Verify token with Google tokeninfo endpoint (simple server-side validation)
            // Note: For production, prefer verifying JWT signature using Google's public keys
            String encoded = URLEncoder.encode(credential, java.nio.charset.StandardCharsets.UTF_8);
            URL url = new URL("https://oauth2.googleapis.com/tokeninfo?id_token=" + encoded);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            int status = conn.getResponseCode();
            reader = new BufferedReader(new InputStreamReader(
                    status >= 200 && status < 300 ? conn.getInputStream() : conn.getErrorStream(),
                    StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            String body = sb.toString();
            if (status < 200 || status >= 300) {
                System.err.println("[GoogleAuth] tokeninfo error: " + body);
                return null;
            }
            // Minimal JSON extraction without external libs
            String aud = extractJson(body, "aud");
            String email = extractJson(body, "email");
            String name = extractJson(body, "name");
            String picture = extractJson(body, "picture");

            // Validate audience matches our client id if available
            String clientId = util.ConfigUtil.get("google.client.id");
            if (clientId != null && !clientId.isEmpty() && !clientId.equals(aud)) {
                return null;
            }
            if (email == null) return null;

            GoogleUserInfo user = new GoogleUserInfo();
            user.setEmail(email);
            user.setName(name);
            user.setPicture(picture);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try { if (reader != null) reader.close(); } catch (Exception ignored) {}
        }
    }

    private String extractJson(String body, String key) {
        String pattern = "\"" + key + "\":";
        int idx = body.indexOf(pattern);
        if (idx < 0) return null;
        int start = body.indexOf('"', idx + pattern.length());
        if (start < 0) return null;
        int end = body.indexOf('"', start + 1);
        if (end < 0) return null;
        return body.substring(start + 1, end);
    }
    
    private String generateUsername(String email) {
        String baseUsername = email.split("@")[0];
        String username = baseUsername;
        int counter = 1;
        
        while (userDAO.isUsernameExists(username)) {
            username = baseUsername + counter;
            counter++;
        }
        
        return username;
    }
    
    private String getRedirectUrl(String role, String contextPath) {
        switch (role) {
            case "admin":
            case "manager":
                return contextPath + "/views/admin/dashboard.jsp";
            case "coach":
                return contextPath + "/views/coach/dashboard.jsp";
            case "member":
                return contextPath + "/views/member/dashboard.jsp";
            default:
                return contextPath + "/home.jsp";
        }
    }
    
    private void sendSuccessResponse(PrintWriter out, String message, String redirectUrl) {
        out.println("{");
        out.println("  \"success\": true,");
        out.println("  \"message\": \"" + message + "\",");
        out.println("  \"redirectUrl\": \"" + redirectUrl + "\"");
        out.println("}");
    }
    
    private void sendErrorResponse(PrintWriter out, String message) {
        out.println("{");
        out.println("  \"success\": false,");
        out.println("  \"message\": \"" + message + "\"");
        out.println("}");
    }
    
    // Inner class for Google user info
    private static class GoogleUserInfo {
        private String email;
        private String name;
        private String picture;
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getPicture() { return picture; }
        public void setPicture(String picture) { this.picture = picture; }
    }
}
