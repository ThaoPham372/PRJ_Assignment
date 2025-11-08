package service;

import dao.PasswordResetTokenDAO;
import dao.UserDAO;
import model.PasswordResetToken;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * PasswordService - Service xử lý password hashing, verification và reset
 * 
 * Chức năng:
 * - Hash password bằng BCrypt
 * - Verify password
 * - Validate password strength
 * - Password reset với verification code qua email
 */
public class PasswordService {

    private static final int BCRYPT_ROUNDS = 12;
    
    // Lazy initialization cho password reset functionality
    private PasswordResetTokenDAO tokenDAO;
    private UserDAO userDAO;
    private EmailService emailService;
    
    private void initResetServices() {
        if (tokenDAO == null) {
            this.tokenDAO = new PasswordResetTokenDAO();
        }
        if (userDAO == null) {
            this.userDAO = new UserDAO();
        }
        if (emailService == null) {
            this.emailService = new EmailService();
        }
    }

    // ==================== PASSWORD HASHING & VERIFICATION ====================

    /**
     * Hash password bằng BCrypt
     * 
     * @param plainPassword Mật khẩu plain text
     * @return Mật khẩu đã được hash
     */
    public String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_ROUNDS));
    }

    /**
     * Verify password
     * 
     * @param plainPassword Mật khẩu plain text cần verify
     * @param hashedPassword Mật khẩu đã hash trong database
     * @return true nếu password khớp, false nếu không
     */
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            System.err.println("[PasswordService] Error verifying password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validate password strength
     * 
     * @param password Mật khẩu cần validate
     * @return true nếu password đạt yêu cầu
     */
    public boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasNumber = password.matches(".*\\d.*");
        
        return hasLetter && hasNumber;
    }

    /**
     * Lấy message validation cho password
     * 
     * @param password Mật khẩu cần validate
     * @return Message lỗi hoặc null nếu hợp lệ
     */
    public String getPasswordValidationMessage(String password) {
        if (password == null || password.length() < 8) {
            return "Mật khẩu phải có ít nhất 8 ký tự";
        }
        
        if (!password.matches(".*[a-zA-Z].*")) {
            return "Mật khẩu phải chứa ít nhất một chữ cái";
        }
        
        if (!password.matches(".*\\d.*")) {
            return "Mật khẩu phải chứa ít nhất một số";
        }
        
        return null; // Valid password
    }

    // ==================== PASSWORD RESET FUNCTIONALITY ====================

    /**
     * Generate 6-digit verification code
     */
    private String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000); // 6 digits: 100000-999999
        return String.valueOf(code);
    }
    
    /**
     * Request password reset - gửi email với verification code
     * 
     * @param email Email của user
     * @return Verification code nếu thành công, null nếu thất bại
     */
    public String requestPasswordReset(String email) {
        initResetServices();
        
        try {
            // 1. Kiểm tra user tồn tại
            User user = userDAO.findByEmail(email);
            if (user == null) {
                System.err.println("[PasswordService] User not found for email: " + email);
                return null;
            }
            
            // 2. Invalidate old tokens
            tokenDAO.invalidateAllUserTokens(user.getId());
            
            // 3. Generate verification code
            String verificationCode = generateVerificationCode().trim();
            
            // 4. Calculate expiry time
            int expiryMinutes = emailService.getTokenExpiryMinutes();
            LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(expiryMinutes);
            
            // 5. Save token to database
            PasswordResetToken token = new PasswordResetToken(
                user.getId(),
                verificationCode,
                email,
                expiresAt
            );
            
            try {
                tokenDAO.save(token);
                if (token.getTokenId() == null || token.getTokenId() <= 0) {
                    System.err.println("[PasswordService] Token ID not generated after save");
                    return null;
                }
            } catch (Exception e) {
                System.err.println("[PasswordService] Exception saving token: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
            
            // 6. Send email
            boolean emailSent = emailService.sendPasswordResetEmail(email, verificationCode);
            if (!emailSent) {
                System.err.println("[PasswordService] Failed to send email");
                return null;
            }
            
            return verificationCode;
            
        } catch (Exception e) {
            System.err.println("[PasswordService] Error requesting password reset: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Verify reset token
     * 
     * @param email Email của user
     * @param token Verification code
     * @return true nếu token hợp lệ
     */
    public boolean verifyResetToken(String email, String token) {
        initResetServices();
        
        try {
            if (token == null || token.trim().isEmpty()) {
                return false;
            }
            
            String trimmedToken = token.trim();
            
            // Tìm token trong database
            Optional<PasswordResetToken> tokenOpt = tokenDAO.findByToken(trimmedToken);
            
            if (!tokenOpt.isPresent()) {
                return false;
            }
            
            PasswordResetToken resetToken = tokenOpt.get();
            
            // Kiểm tra email khớp (case-insensitive)
            String normalizedEmail = email != null ? email.trim().toLowerCase() : "";
            String dbEmail = resetToken.getEmail() != null ? resetToken.getEmail().trim().toLowerCase() : "";
            
            if (!dbEmail.equals(normalizedEmail)) {
                return false;
            }
            
            // Kiểm tra token còn hợp lệ (chưa expired, chưa used)
            if (!resetToken.isValid()) {
                return false;
            }
            
            return true;
            
        } catch (Exception e) {
            System.err.println("[PasswordService] Error verifying token: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Reset password với verified token
     * 
     * @param email Email của user
     * @param token Verification code
     * @param newPassword Mật khẩu mới
     * @return true nếu thành công
     */
    public boolean resetPassword(String email, String token, String newPassword) {
        initResetServices();
        
        try {
            // 1. Verify token
            if (!verifyResetToken(email, token)) {
                return false;
            }
            
            // 2. Get user
            User user = userDAO.findByEmail(email);
            if (user == null) {
                System.err.println("[PasswordService] User not found: " + email);
                return false;
            }
            
            // 3. Hash new password
            String passwordHash = hashPassword(newPassword);
            
            // 4. Update user password
            user.setPassword(passwordHash);
            if (userDAO.update(user) < -1) {
                System.err.println("[PasswordService] Failed to update password");
                return false;
            }
            
            // 5. Mark token as used
            tokenDAO.markAsUsed(token);
            
            // 6. Invalidate all other tokens for this user
            tokenDAO.invalidateAllUserTokens(user.getId());
            
            return true;
            
        } catch (Exception e) {
            System.err.println("[PasswordService] Error resetting password: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Kiểm tra user có pending reset request không (để rate limiting)
     * 
     * @param email Email của user
     * @return true nếu có pending request
     */
    public boolean hasPendingResetRequest(String email) {
        initResetServices();
        
        try {
            Optional<PasswordResetToken> tokenOpt = tokenDAO.findValidTokenByEmail(email);
            return tokenOpt.isPresent();
        } catch (Exception e) {
            return false;
        }
    }
}
