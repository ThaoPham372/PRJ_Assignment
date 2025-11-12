package service;

import dao.MemberDAO;
import dao.UserDAO;
import model.User;
import model.Member;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * GoogleAuthService - Service xử lý đăng nhập và đăng ký qua Google OAuth
 * 
 * Chức năng:
 * - Verify Google ID token
 * - Đăng nhập với Google account (nếu đã có account)
 * - Đăng ký user mới từ Google account
 */
public class GoogleAuthService {

    private final MemberDAO memberDAO;
    private final UserDAO userDAO;
    private final String expectedClientId;

    public GoogleAuthService(String expectedClientId) {
        this.memberDAO = new MemberDAO();
        this.userDAO = new UserDAO();
        this.expectedClientId = expectedClientId;
    }

    /**
     * Verify Google ID token và extract thông tin user
     */
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
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String body = sb.toString();
            
            if (status < 200 || status >= 300) {
                return VerifyResult.error("Token verification failed");
            }

            // Extract thông tin từ JSON response
            String aud = extractJson(body, "aud");
            String email = extractJson(body, "email");
            String name = extractJson(body, "name");
            String picture = extractJson(body, "picture");

            // Validate audience (client ID)
            if (expectedClientId != null && !expectedClientId.isEmpty() && !expectedClientId.equals(aud)) {
                return VerifyResult.error("Audience mismatch");
            }
            
            // Validate email
            if (email == null || email.isEmpty()) {
                return VerifyResult.error("Email not present in token");
            }

            // Tạo GoogleUser object
            GoogleUser user = new GoogleUser();
            user.email = email;
            user.name = name;
            user.picture = picture;
            
            return VerifyResult.success(user);
        } catch (Exception e) {
            System.err.println("[GoogleAuthService] Error verifying token: " + e.getMessage());
            e.printStackTrace();
            return VerifyResult.error("Exception verifying token: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception ignored) {}
        }
    }

    /**
     * Đăng nhập với Google account (nếu account đã tồn tại)
     * 
     * @param googleUser Thông tin user từ Google
     * @return AuthResult chứa User và roles nếu thành công
     */
    public AuthResult loginWithGoogle(GoogleUser googleUser) {
        List<String> errors = new ArrayList<>();
        
        // Validation
        if (googleUser == null || googleUser.email == null) {
            errors.add("Invalid Google user");
            return AuthResult.failed(errors);
        }
        
        // Tìm user theo email
        User existing = userDAO.findByEmail(googleUser.email);
        if (existing == null) {
            errors.add("Account not found. Please register first.");
            return AuthResult.failed(errors);
        }
        
        // Kiểm tra status
        if (!"ACTIVE".equalsIgnoreCase(existing.getStatus())) {
            errors.add("Account is not active");
            return AuthResult.failed(errors);
        }
        
        // Tạo roles list
        List<String> roles = new ArrayList<>();
        if (existing.getRole() != null && !existing.getRole().isEmpty()) {
            roles.add(existing.getRole());
        }
        
        return AuthResult.success(existing, roles);
    }

    /**
     * Đăng ký user mới từ Google account
     * 
     * @param googleUser Thông tin user từ Google
     * @return AuthResult chứa User và roles nếu thành công
     */
    public AuthResult registerWithGoogle(GoogleUser googleUser) {
        List<String> errors = new ArrayList<>();
        
        // Validation
        if (googleUser == null || googleUser.email == null) {
            errors.add("Invalid Google user");
            return AuthResult.failed(errors);
        }
        
        // Kiểm tra email đã tồn tại chưa
        User existing = userDAO.findByEmail(googleUser.email);
        if (existing != null) {
            errors.add("Tài Khoản đã tồn tại. Hãy đăng nhập.");
            return AuthResult.failed(errors);
        }

        // Tạo username từ email (nếu trùng thì thêm số)
        String baseUsername = googleUser.email.split("@")[0];
        String username = baseUsername;
        int suffix = 1;
        while (userDAO.existsByUsername(username)) {
            username = baseUsername + suffix;
            suffix++;
        }

        // Tạo password hash (random cho Google OAuth)
        String passwordHash = "GOOGLE_OAUTH_" + UUID.randomUUID().toString();

        // Tạo user account
        // Sử dụng Google name làm họ tên, nếu không có thì để trống
        String fullName = (googleUser.name != null && !googleUser.name.trim().isEmpty()) 
                         ? googleUser.name 
                         : "";
        
        // Tạo Member entity
        Member member = new Member();
        member.setUsername(username);  // username để đăng nhập
        member.setName(fullName);      // name là họ và tên thật
        member.setEmail(googleUser.email.toLowerCase());
        member.setPassword(passwordHash);
        member.setRole("MEMBER");
        member.setStatus("ACTIVE");
        member.setCreatedDate(new Date());
        
        // Lưu user vào database
        int userId = memberDAO.save(member);
        if (userId <= 0) {
            errors.add("Failed to create account");
            return AuthResult.failed(errors);
        }
        
     
        // Load lại user đã tạo
        User created = userDAO.findById(userId);
        if (created == null) {
            // Fallback: tìm theo email
            created = userDAO.findByEmail(googleUser.email);
            if (created == null) {
                errors.add("Failed to load created account");
                return AuthResult.failed(errors);
            }
        }
        
        // Tạo roles list
        List<String> roles = new ArrayList<>();
        if (created.getRole() != null && !created.getRole().isEmpty()) {
            roles.add(created.getRole());
        }
        
        return AuthResult.success(created, roles);
    }

    /**
     * Extract giá trị từ JSON string
     */
    private String extractJson(String body, String key) {
        String pattern = "\"" + key + "\":";
        int idx = body.indexOf(pattern);
        if (idx < 0) {
            return null;
        }
        int start = body.indexOf('"', idx + pattern.length());
        if (start < 0) {
            return null;
        }
        int end = body.indexOf('"', start + 1);
        if (end < 0) {
            return null;
        }
        return body.substring(start + 1, end);
    }

    // ==================== Inner Classes ====================

    /**
     * GoogleUser - Chứa thông tin user từ Google
     */
    public static class GoogleUser {
        public String email;
        public String name;
        public String picture;
    }

    /**
     * VerifyResult - Kết quả verify token
     */
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

    /**
     * AuthResult - Kết quả đăng ký
     */
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