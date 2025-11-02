package com.gym.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gym.dao.RoleDAO;
import com.gym.dao.UserDAO;
import com.gym.model.User;

/**
 * RegistrationService - Handles user registration business logic
 */
public class RegistrationService {

    private UserDAO userDAO;
    private RoleDAO roleDAO;
    private PasswordService passwordService;

    public RegistrationService() {
        this.userDAO = new UserDAO();
        this.roleDAO = new RoleDAO();
        this.passwordService = new PasswordService();
    }

    /**
     * Register a new user
     * 
     * @param request Registration request data
     * @return Registration result
     */
    public RegisterResult register(RegisterRequest request) {
        RegisterResult result = new RegisterResult();
        List<String> errors = new ArrayList<>();

        // Validate input
        validateInput(request, errors);

        if (!errors.isEmpty()) {
            result.setSuccess(false);
            result.setErrors(errors);
            return result;
        }

        // Check for duplicates
        if (userDAO.existsByUsername(request.getUsername())) {
            errors.add("Tên đăng nhập đã tồn tại");
        }

        if (userDAO.existsByEmail(request.getEmail())) {
            errors.add("Email đã được sử dụng");
        }

        if (!errors.isEmpty()) {
            result.setSuccess(false);
            result.setErrors(errors);
            return result;
        }

        // Hash password
        String passwordHash = passwordService.hashPassword(request.getPassword());
        String salt = passwordService.generateSalt();

        // Create user in database
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordHash);
        long userId = userDAO.insertUser(user);

        if (userId == -1) {
            result.setSuccess(false);
            result.setErrors(Arrays.asList("Lỗi hệ thống khi tạo tài khoản"));
            return result;
        }

        // Assign role based on user selection
        String roleName = request.getRoleType() != null ? request.getRoleType() : "USER";
        Long userRoleId = roleDAO.findRoleIdByName(roleName);
        if (userRoleId == null) {
            // Create default roles if they don't exist
            roleDAO.createDefaultRoles();
            userRoleId = roleDAO.findRoleIdByName(roleName);
        }

        if (userRoleId != null) {
            boolean roleAssigned = roleDAO.assignUserRole(userId, userRoleId);
            if (!roleAssigned) {
                System.err.println("Warning: Failed to assign " + roleName + " role to user " + userId);
            }
        }

        // Log registration
        

        // Success
        result.setSuccess(true);
        result.setMessage("Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.");
        result.setUserId(userId);

        return result;
    }

    /**
     * Validate registration input
     */
    private void validateInput(RegisterRequest request, List<String> errors) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            errors.add("Tên đăng nhập không được để trống");
        } else if (request.getUsername().length() < 3) {
            errors.add("Tên đăng nhập phải có ít nhất 3 ký tự");
        } else if (!request.getUsername().matches("^[a-zA-Z0-9_]+$")) {
            errors.add("Tên đăng nhập chỉ được chứa chữ cái, số và dấu gạch dưới");
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            errors.add("Email không được để trống");
        } else if (!isValidEmail(request.getEmail())) {
            errors.add("Email không hợp lệ");
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            errors.add("Mật khẩu không được để trống");
        } else {
            String passwordError = passwordService.getPasswordValidationMessage(request.getPassword());
            if (passwordError != null) {
                errors.add(passwordError);
            }
        }

        if (request.getConfirmPassword() == null || request.getConfirmPassword().trim().isEmpty()) {
            errors.add("Xác nhận mật khẩu không được để trống");
        } else if (!request.getPassword().equals(request.getConfirmPassword())) {
            errors.add("Mật khẩu và xác nhận mật khẩu không khớp");
        }
    }

    /**
     * Simple email validation
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }

    /**
     * Registration request DTO
     */
    public static class RegisterRequest {
        private String username;
        private String email;
        private String password;
        private String confirmPassword;
        private String roleType;
        private String ipAddress;
        private String userAgent;

        // Constructors
        public RegisterRequest() {
        }

        public RegisterRequest(String username, String email, String password, String confirmPassword) {
            this.username = username;
            this.email = email;
            this.password = password;
            this.confirmPassword = confirmPassword;
        }

        // Getters and Setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getConfirmPassword() {
            return confirmPassword;
        }

        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public String getUserAgent() {
            return userAgent;
        }

        public void setUserAgent(String userAgent) {
            this.userAgent = userAgent;
        }

        public String getRoleType() {
            return roleType;
        }

        public void setRoleType(String roleType) {
            this.roleType = roleType;
        }
    }

    /**
     * Registration result DTO
     */
    public static class RegisterResult {
        private boolean success;
        private String message;
        private long userId;
        private List<String> errors;

        public RegisterResult() {
            this.errors = new ArrayList<>();
        }

        // Getters and Setters
        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public List<String> getErrors() {
            return errors;
        }

        public void setErrors(List<String> errors) {
            this.errors = errors;
        }
    }
}
