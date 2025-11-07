package com.gym.service;

import com.gym.dao.PasswordResetTokenDAO;
import com.gym.dao.UserDAO;
import com.gym.model.PasswordResetToken;
import com.gym.model.User;
import org.mindrot.jbcrypt.BCrypt;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * PasswordService - Unified service for password hashing, verification, and reset functionality
 * Combines PasswordService and PasswordResetService into one class
 */
public class PasswordService {

    private static final int BCRYPT_ROUNDS = 12;
    
    // DAOs and services for password reset functionality
    private PasswordResetTokenDAO tokenDAO;
    private UserDAO userDAO;
    private EmailService emailService;
    
    // Lazy initialization for reset functionality
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
     * Hash a plain text password using BCrypt
     * @param plainPassword The plain text password
     * @return The hashed password
     */
    public String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_ROUNDS));
    }

    /**
     * Verify a plain text password against a hashed password
     * @param plainPassword The plain text password to verify
     * @param hashedPassword The stored hashed password
     * @return true if password matches, false otherwise
     */
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            System.err.println("Error verifying password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Generate a random salt (for compatibility with older systems)
     * Note: BCrypt includes salt in the hash, so this is mainly for legacy support
     * @return A random salt string
     */
    public String generateSalt() {
        return BCrypt.gensalt(BCRYPT_ROUNDS);
    }

    /**
     * Validate password strength
     * @param password The password to validate
     * @return true if password meets requirements
     */
    public boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        // Check for at least one letter and one number
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasNumber = password.matches(".*\\d.*");
        
        return hasLetter && hasNumber;
    }

    /**
     * Get password validation message
     * @param password The password to validate
     * @return Validation message or null if valid
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
     * Request password reset - sends email with verification code
     * @return verification code if successful, null if failed
     */
    public String requestPasswordReset(String email) {
        initResetServices();
        
        try {
            System.out.println("[PasswordService] ===== Processing reset request =====");
            System.out.println("[PasswordService] Email: " + email);
            
            // 1. Check if user exists
            System.out.println("[PasswordService] Looking up user by email...");
            User user = userDAO.findByEmail(email);
            
            if (user == null) {
                System.err.println("[PasswordService] ❌ User not found for email: " + email);
                System.err.println("[PasswordService] Debug: Checking if email exists in DB...");
                
                // Debug: List all emails in DB (first 10)
                try {
                    java.util.List<User> allUsers = userDAO.findAll();
                    System.err.println("[PasswordService] Total users in DB: " + allUsers.size());
                    System.err.println("[PasswordService] Sample emails in DB:");
                    int count = 0;
                    for (User u : allUsers) {
                        if (count < 10 && u.getEmail() != null) {
                            System.err.println("  - " + u.getEmail() + " (user_id: " + u.getUserId() + ")");
                            count++;
                        }
                    }
                } catch (Exception e) {
                    System.err.println("[PasswordService] Error listing users: " + e.getMessage());
                }
                
                return null;
            }
            
            System.out.println("[PasswordService] ✅ User found: user_id=" + user.getUserId() + ", username=" + user.getUsername());
            
            // 2. Invalidate old tokens for this user
            tokenDAO.invalidateAllUserTokens(user.getUserId());
            
            // 3. Generate verification code
            String verificationCode = generateVerificationCode();
            
            // ✅ Ensure verification code is trimmed (should already be clean, but just in case)
            verificationCode = verificationCode.trim();
            
            // 4. Calculate expiry time (15 minutes from now)
            int expiryMinutes = emailService.getTokenExpiryMinutes();
            LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(expiryMinutes);
            
            // 5. Save token to database
            PasswordResetToken token = new PasswordResetToken(
                user.getUserId(),
                verificationCode,
                email,
                expiresAt
            );
            
            System.out.println("[PasswordService] Saving token to DB...");
            System.out.println("[PasswordService] Token details: userId=" + user.getUserId() + 
                             ", email=" + email + ", code=" + verificationCode + 
                             ", expiresAt=" + expiresAt);
            
            try {
                int savedId = tokenDAO.save(token);
                System.out.println("[PasswordService] save() returned: " + savedId);
                
                // ✅ Check if token was persisted by checking tokenId
                Long tokenId = token.getTokenId();
                System.out.println("[PasswordService] Token ID after save: " + tokenId);
                
                if (tokenId == null || tokenId <= 0) {
                    System.err.println("[PasswordService] ❌ Token ID not generated after save");
                    System.err.println("[PasswordService] Token state: userId=" + token.getUserId() + 
                                     ", email=" + token.getEmail() + ", token=" + token.getToken());
                    return null;
                }
                
                System.out.println("[PasswordService] ✅ Token saved successfully: tokenId=" + tokenId);
            } catch (Exception e) {
                System.err.println("[PasswordService] ❌ Exception saving token: " + e.getMessage());
                System.err.println("[PasswordService] Exception type: " + e.getClass().getName());
                e.printStackTrace();
                
                // Check if it's a constraint violation (duplicate token)
                if (e.getCause() != null) {
                    System.err.println("[PasswordService] Caused by: " + e.getCause().getMessage());
                }
                
                return null;
            }
            
            // 6. Send email
            boolean emailSent = emailService.sendPasswordResetEmail(email, verificationCode);
            if (!emailSent) {
                System.err.println("[PasswordService] Failed to send email");
                return null;
            }
            
            System.out.println("[PasswordService] ✅ Reset request successful for: " + email);
            return verificationCode; // Return for testing/debugging
            
        } catch (Exception e) {
            System.err.println("[PasswordService] Error requesting password reset: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Verify reset token
     * ✅ FIXED: Tìm token bằng token string thay vì chỉ bằng email
     */
    public boolean verifyResetToken(String email, String token) {
        initResetServices();
        
        try {
            System.out.println("[PasswordService] ===== Verifying Token =====");
            System.out.println("[PasswordService] Email: " + email);
            System.out.println("[PasswordService] Token input: '" + token + "' (length: " + (token != null ? token.length() : 0) + ")");
            
            // ✅ Trim token before searching
            if (token == null || token.trim().isEmpty()) {
                System.err.println("[PasswordService] ❌ Token is null or empty");
                return false;
            }
            
            String trimmedToken = token.trim();
            System.out.println("[PasswordService] Trimmed token: '" + trimmedToken + "' (length: " + trimmedToken.length() + ")");
            
            // ✅ FIX: Tìm token bằng token string (không phải chỉ email)
            Optional<PasswordResetToken> tokenOpt = tokenDAO.findByToken(trimmedToken);
            
            if (!tokenOpt.isPresent()) {
                System.err.println("[PasswordService] ❌ Token not found in DB: " + token);
                return false;
            }
            
            PasswordResetToken resetToken = tokenOpt.get();
            System.out.println("[PasswordService] Token found: tokenId=" + resetToken.getTokenId() + 
                             ", email=" + resetToken.getEmail() + 
                             ", expiresAt=" + resetToken.getExpiresAt() + 
                             ", used=" + resetToken.getUsed());
            
            // ✅ Normalize email for comparison (trim + lowercase)
            String normalizedEmail = email != null ? email.trim().toLowerCase() : "";
            String dbEmail = resetToken.getEmail() != null ? resetToken.getEmail().trim().toLowerCase() : "";
            
            System.out.println("[PasswordService] Comparing emails - input: '" + normalizedEmail + "', DB: '" + dbEmail + "'");
            
            // ✅ Check email matches (case-insensitive, trimmed)
            if (!dbEmail.equals(normalizedEmail)) {
                System.err.println("[PasswordService] ❌ Email mismatch: expected='" + normalizedEmail + 
                                 "', found='" + dbEmail + "'");
                return false;
            }
            
            // ✅ Check if token is still valid (not expired, not used)
            if (!resetToken.isValid()) {
                System.err.println("[PasswordService] ❌ Token invalid: expired=" + resetToken.isExpired() + 
                                 ", used=" + resetToken.getUsed());
                return false;
            }
            
            System.out.println("[PasswordService] ✅ Token verified successfully!");
            return true;
            
        } catch (Exception e) {
            System.err.println("[PasswordService] ❌ Error verifying token: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Reset password with verified token
     */
    public boolean resetPassword(String email, String token, String newPassword) {
        initResetServices();
        
        try {
            System.out.println("[PasswordService] Resetting password for: " + email);
            
            // 1. Verify token again
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
            user.setLastUpdate(LocalDateTime.now());
            
            boolean updated = userDAO.updateUser(user);
            if (!updated) {
                System.err.println("[PasswordService] Failed to update password");
                return false;
            }
            
            // 5. Mark token as used
            tokenDAO.markAsUsed(token);
            
            // 6. Invalidate all other tokens for this user
            tokenDAO.invalidateAllUserTokens(user.getUserId());
            
            System.out.println("[PasswordService] ✅ Password reset successful for: " + email);
            return true;
            
        } catch (Exception e) {
            System.err.println("[PasswordService] Error resetting password: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Check if user has pending reset request (for rate limiting)
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
