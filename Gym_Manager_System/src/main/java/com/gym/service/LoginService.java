package com.gym.service;

import com.gym.dao.UserDAO;
import com.gym.model.User;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class LoginService {
    
    private UserDAO userDAO;
    private PasswordService passwordService;
    
    public LoginService() {
        this.userDAO = new UserDAO();
        this.passwordService = new PasswordService();
    }
    
    /**
     * Authenticate user with username and password
     * @param username username or email
     * @param password plain text password
     * @return LoginResult containing success status, user info, and error messages
     */
    public LoginResult authenticate(String username, String password) {
        List<String> errors = new ArrayList<>();
        
        // Validate input
        if (username == null || username.trim().isEmpty()) {
            errors.add("Tên đăng nhập không được để trống");
        }
        
        if (password == null || password.trim().isEmpty()) {
            errors.add("Mật khẩu không được để trống");
        }
        
        if (!errors.isEmpty()) {
            return new LoginResult(false, null, errors);
        }
        
        try {
            // Find user by username or email (using JPA EntityManager)
            User user = userDAO.findByUsernameOrEmail(username.trim());
            
            if (user == null) {
                errors.add("Tên đăng nhập hoặc mật khẩu không đúng");
                return new LoginResult(false, null, errors);
            }
            
            // Check if account is active
            if (!"ACTIVE".equals(user.getStatus())) {
                errors.add("Tài khoản đã bị khóa hoặc chưa được kích hoạt");
                return new LoginResult(false, null, errors);
            }
            
            // Check if account is locked due to failed attempts
            if (user.getLockedUntil() != null) {
                java.time.LocalDateTime lockedUntil = user.getLockedUntil();
                if (lockedUntil.isAfter(java.time.LocalDateTime.now())) {
                    errors.add("Tài khoản đã bị khóa do đăng nhập sai quá nhiều lần. Vui lòng thử lại sau.");
                    return new LoginResult(false, null, errors);
                }
            }
            
            // Verify password
            if (!passwordService.verifyPassword(password, user.getPasswordHash())) {
                // Increment failed login attempts (using JPA)
                EntityManager em = userDAO.getEntityManager();
                userDAO.incrementFailedLoginAttempts(em, user.getId());
                
                // Refresh user to get updated failed attempts count
                user = userDAO.getUserById(user.getId());
                
                // Lock account if too many failed attempts
                if (user.getFailedLoginAttempts() != null && user.getFailedLoginAttempts() >= 5) {
                    userDAO.lockAccount(em, user.getId(), 30); // Lock for 30 minutes
                    errors.add("Tài khoản đã bị khóa do đăng nhập sai quá nhiều lần. Vui lòng thử lại sau 30 phút.");
                } else {
                    errors.add("Tên đăng nhập hoặc mật khẩu không đúng");
                }
                
                return new LoginResult(false, null, errors);
            }
            
            // Login successful - reset failed attempts and update last login
            EntityManager em = userDAO.getEntityManager();
            userDAO.resetFailedLoginAttempts(em, user.getId());
            userDAO.updateLastLogin(em, user.getId());
            
            // Get user role from user.role column
            List<String> roles = new ArrayList<>();
            if (user.getRole() != null && !user.getRole().isEmpty()) {
                roles.add(user.getRole());
            }
            
            return new LoginResult(true, user, roles, null);
            
        } catch (Exception e) {
            System.err.println("Unexpected error during login: " + e.getMessage());
            e.printStackTrace();
            errors.add("Lỗi hệ thống. Vui lòng thử lại sau.");
            return new LoginResult(false, null, errors);
        }
    }
    
    /**
     * Result class for login operation
     */
    public static class LoginResult {
        private boolean success;
        private User user;
        private List<String> roles;
        private List<String> errors;
        
        public LoginResult(boolean success, User user, List<String> errors) {
            this.success = success;
            this.user = user;
            this.errors = errors;
        }
        
        public LoginResult(boolean success, User user, List<String> roles, List<String> errors) {
            this.success = success;
            this.user = user;
            this.roles = roles;
            this.errors = errors;
        }
        
        public boolean isSuccess() { return success; }
        public User getUser() { return user; }
        public List<String> getRoles() { return roles; }
        public List<String> getErrors() { return errors; }
    }
}
