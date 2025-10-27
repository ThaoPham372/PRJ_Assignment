package com.gym.service;

import org.mindrot.jbcrypt.BCrypt;

/**
 * PasswordService - Handles password hashing and verification using BCrypt
 */
public class PasswordService {

    private static final int BCRYPT_ROUNDS = 12;

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
}
