package com.gym.service;

import com.gym.dao.UserDAO;
import com.gym.model.User;
import com.gym.model.Student;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GoogleAuthService {

    private final UserDAO userDAO;
    private final StudentService studentService;
    private final String expectedClientId;

    public GoogleAuthService(String expectedClientId) {
        this.userDAO = new UserDAO();
        this.studentService = new StudentService();
        this.expectedClientId = expectedClientId;
    }

    public VerifyResult verifyTokenAndExtract(String credential) {
        BufferedReader reader = null;
        try {
            String encoded = URLEncoder.encode(credential, StandardCharsets.UTF_8);
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
                return VerifyResult.error("Token verification failed");
            }

            String aud = extractJson(body, "aud");
            String email = extractJson(body, "email");
            String name = extractJson(body, "name");
            String picture = extractJson(body, "picture");

            if (expectedClientId != null && !expectedClientId.isEmpty() && !expectedClientId.equals(aud)) {
                return VerifyResult.error("Audience mismatch");
            }
            if (email == null || email.isEmpty()) {
                return VerifyResult.error("Email not present in token");
            }

            GoogleUser user = new GoogleUser();
            user.email = email;
            user.name = name;
            user.picture = picture;
            return VerifyResult.success(user);
        } catch (Exception e) {
            return VerifyResult.error("Exception verifying token");
        } finally {
            try { if (reader != null) reader.close(); } catch (Exception ignored) {}
        }
    }

    public AuthResult loginWithGoogle(GoogleUser googleUser) {
        List<String> errors = new ArrayList<>();
        if (googleUser == null || googleUser.email == null) {
            errors.add("Invalid Google user");
            return AuthResult.failed(errors);
        }
        
        User existing = userDAO.findByEmail(googleUser.email);
        if (existing == null) {
            errors.add("Account not found. Please register first.");
            return AuthResult.failed(errors);
        }
        
        if (!"ACTIVE".equalsIgnoreCase(existing.getStatus())) {
            errors.add("Account is not active");
            return AuthResult.failed(errors);
        }
        
        // Update avatar from Google if user doesn't have one or wants to sync
        // Avatar is stored in User table
        if (googleUser.picture != null && !googleUser.picture.trim().isEmpty()) {
            try {
                // Update avatar in User table if user doesn't have one
                if (existing.getAvatarUrl() == null || existing.getAvatarUrl().trim().isEmpty()) {
                    userDAO.updateAvatarUrl(existing.getId(), googleUser.picture);
                    existing.setAvatarUrl(googleUser.picture);
                }
                // If user already has avatar, keep it (don't overwrite custom avatar)
            } catch (Exception e) {
                System.err.println("[GoogleAuthService] Error updating avatar: " + e.getMessage());
                // Continue with login even if avatar update fails
            }
        }
        
        // Get role from user.role column
        List<String> roles = new ArrayList<>();
        if (existing.getRole() != null && !existing.getRole().isEmpty()) {
            roles.add(existing.getRole());
        }
        return AuthResult.success(existing, roles);
    }

    public AuthResult registerWithGoogle(GoogleUser googleUser) {
        List<String> errors = new ArrayList<>();
        if (googleUser == null || googleUser.email == null) {
            errors.add("Invalid Google user");
            return AuthResult.failed(errors);
        }
        User existing = userDAO.findByEmail(googleUser.email);
        if (existing != null) {
            errors.add("Account already exists. Please login instead.");
            return AuthResult.failed(errors);
        }

        String baseUsername = googleUser.email.split("@")[0];
        String username = baseUsername;
        int suffix = 1;
        while (userDAO.existsByUsername(username)) {
            username = baseUsername + suffix;
            suffix++;
        }

        String salt = UUID.randomUUID().toString();
        String passwordHash = "GOOGLE_OAUTH_" + UUID.randomUUID();

        // Create user account - use Google name as full name
        String fullName = (googleUser.name != null && !googleUser.name.trim().isEmpty()) 
                         ? googleUser.name 
                         : username;
        long userId = userDAO.insertUser(username, fullName, googleUser.email, passwordHash, salt);
        if (userId <= 0) {
            errors.add("Failed to create account");
            return AuthResult.failed(errors);
        }
        
        // Update avatar from Google if available
        if (googleUser.picture != null && !googleUser.picture.trim().isEmpty()) {
            try {
                userDAO.updateAvatarUrl(userId, googleUser.picture);
            } catch (Exception e) {
                System.err.println("[GoogleAuthService] Error setting avatar: " + e.getMessage());
            }
        }
        
        // Set USER role for new Google registrations
        // Note: role will be set when we load and update the user below
        
        // Create Student profile
        // IMPORTANT: This must succeed for the system to work properly
        // NOTE: students table only stores: user_id, weight, height, bmi, emergency_contact_*
        // Avatar and other info (full_name, email, phone...) is stored in user table
        try {
            Student student = new Student();
            student.setUserId((int) userId);
            // Only set user_id - other fields will be NULL initially
            // User can update them later via Dashboard
            
            studentService.saveStudent(student);
        } catch (Exception e) {
            System.err.println("[GoogleAuthService] ERROR creating Student profile: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Load the created user
        User created = userDAO.getUserById(userId);
        if (created == null) {
            created = userDAO.findByEmail(googleUser.email);
            if (created == null) {
                errors.add("Failed to load created account");
                return AuthResult.failed(errors);
            }
        }
        
        // Set user.role to "USER" for Google registrations
        created.setRole("USER");
        try {
            userDAO.update(created);
            System.out.println("[GoogleAuthService] Successfully set role='USER' for user_id: " + created.getId());
        } catch (Exception e) {
            System.err.println("[GoogleAuthService] ERROR setting role: " + e.getMessage());
        }
        
        // Get role from user.role column
        List<String> roles = new ArrayList<>();
        if (created.getRole() != null && !created.getRole().isEmpty()) {
            roles.add(created.getRole());
        }
        
        return AuthResult.success(created, roles);
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

    public static class GoogleUser {
        public String email;
        public String name;
        public String picture;
    }

    public static class VerifyResult {
        public boolean success;
        public GoogleUser googleUser;
        public String error;

        public static VerifyResult success(GoogleUser user) {
            VerifyResult r = new VerifyResult();
            r.success = true;
            r.googleUser = user;
            return r;
        }

        public static VerifyResult error(String message) {
            VerifyResult r = new VerifyResult();
            r.success = false;
            r.error = message;
            return r;
        }
    }

    public static class AuthResult {
        public boolean success;
        public User user;
        public List<String> roles;
        public List<String> errors;

        public static AuthResult success(User user, List<String> roles) {
            AuthResult r = new AuthResult();
            r.success = true;
            r.user = user;
            r.roles = roles;
            return r;
        }

        public static AuthResult failed(List<String> errors) {
            AuthResult r = new AuthResult();
            r.success = false;
            r.errors = errors;
            return r;
        }
    }
}



