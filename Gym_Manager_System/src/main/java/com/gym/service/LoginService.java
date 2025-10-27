package com.gym.service;

import com.gym.dao.UserDAO;
import com.gym.dao.RoleDAO;
import com.gym.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoginService {
    
    private UserDAO userDAO;
    private RoleDAO roleDAO;
    private PasswordService passwordService;
    
    public LoginService() {
        this.userDAO = new UserDAO();
        this.roleDAO = new RoleDAO();
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
        
        try (Connection conn = com.gym.util.DatabaseUtil.getConnection()) {
            // Find user by username or email
            User user = userDAO.findByUsernameOrEmail(conn, username.trim());
            
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
            if (user.getLockedUntil() != null && 
                user.getLockedUntil().getTime() > System.currentTimeMillis()) {
                errors.add("Tài khoản đã bị khóa do đăng nhập sai quá nhiều lần. Vui lòng thử lại sau.");
                return new LoginResult(false, null, errors);
            }
            
            // Verify password
            if (!passwordService.verifyPassword(password, user.getPasswordHash())) {
                // Increment failed login attempts
                userDAO.incrementFailedLoginAttempts(conn, user.getId());
                
                // Lock account if too many failed attempts
                if (user.getFailedLoginAttempts() + 1 >= 5) {
                    userDAO.lockAccount(conn, user.getId(), 30); // Lock for 30 minutes
                    errors.add("Tài khoản đã bị khóa do đăng nhập sai quá nhiều lần. Vui lòng thử lại sau 30 phút.");
                } else {
                    errors.add("Tên đăng nhập hoặc mật khẩu không đúng");
                }
                
                return new LoginResult(false, null, errors);
            }
            
            // Login successful - reset failed attempts and update last login
            userDAO.resetFailedLoginAttempts(conn, user.getId());
            userDAO.updateLastLogin(conn, user.getId());
            
            // Get user roles
            List<String> roles = roleDAO.getUserRoles(conn, user.getId());
            
            return new LoginResult(true, user, roles, null);
            
        } catch (SQLException e) {
            System.err.println("Database error during login: " + e.getMessage());
            errors.add("Lỗi hệ thống. Vui lòng thử lại sau.");
            return new LoginResult(false, null, errors);
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
