package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class cho password hashing và verification
 * Sử dụng SHA-256 với salt
 */
public class PasswordUtil {
    
    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;
    
    /**
     * Hash password với salt
     * @param password Plain text password
     * @return Hashed password with salt (format: salt:hash)
     */
    public static String hashPassword(String password) {
        try {
            // Generate random salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);
            
            // Hash password with salt
            String hash = hashWithSalt(password, salt);
            
            // Return salt:hash format
            return Base64.getEncoder().encodeToString(salt) + ":" + hash;
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    /**
     * Verify password
     * @param password Plain text password to check
     * @param storedPassword Stored password (format: salt:hash)
     * @return true nếu password đúng
     */
    public static boolean verifyPassword(String password, String storedPassword) {
        try {
            // Split salt and hash
            String[] parts = storedPassword.split(":");
            if (parts.length != 2) {
                return false;
            }
            
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            String storedHash = parts[1];
            
            // Hash input password with same salt
            String hash = hashWithSalt(password, salt);
            
            // Compare hashes
            return hash.equals(storedHash);
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Hash password with given salt
     */
    private static String hashWithSalt(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }
    
    /**
     * Generate random password
     * @param length Length của password
     * @return Random password
     */
    public static String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            password.append(chars.charAt(index));
        }
        
        return password.toString();
    }
    
    /**
     * Validate password strength
     * @param password Password cần check
     * @return true nếu password đủ mạnh
     */
    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }
        
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
    
    /**
     * Get password strength message
     */
    public static String getPasswordStrengthMessage(String password) {
        if (password == null || password.length() < 8) {
            return "Password phải có ít nhất 8 ký tự";
        }
        
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
        
        if (!hasUpper) return "Password phải có ít nhất 1 chữ hoa";
        if (!hasLower) return "Password phải có ít nhất 1 chữ thường";
        if (!hasDigit) return "Password phải có ít nhất 1 số";
        if (!hasSpecial) return "Password phải có ít nhất 1 ký tự đặc biệt";
        
        return "Password hợp lệ";
    }
}

