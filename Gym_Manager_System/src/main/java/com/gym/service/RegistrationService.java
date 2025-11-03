package com.gym.service;

import com.gym.dao.UserDAO;
import com.gym.dao.RoleDAO;
import com.gym.model.Student;
import com.gym.model.User;
import java.util.*;

/**
 * RegistrationService - Handles user registration business logic
 */
public class RegistrationService {

    private UserDAO userDAO;
    private RoleDAO roleDAO;
    private PasswordService passwordService;
    private StudentProfileService studentProfileService;

    public RegistrationService() {
        this.userDAO = new UserDAO();
        this.roleDAO = new RoleDAO();
        this.passwordService = new PasswordService();
        this.studentProfileService = new StudentProfileService();
    }

    /**
     * Register a new user
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

        // Get and trim name (full name)
        String name = (request.getName() != null) ? request.getName().trim() : null;
        System.out.println("[RegistrationService] Registering user:");
        System.out.println("  - Username: " + request.getUsername());
        System.out.println("  - Name (full name): " + name);
        System.out.println("  - Email: " + request.getEmail());

        // Create user in database
        long userId = userDAO.insertUser(
            request.getUsername(),
            name,  // Full name (trimmed)
            request.getEmail(),
            passwordHash,
            salt
        );

        if (userId == -1) {
            result.setSuccess(false);
            result.setErrors(Arrays.asList("Lỗi hệ thống khi tạo tài khoản"));
            return result;
        }
        
        // Verify user was created
        User createdUser = userDAO.getUserById(userId);
        if (createdUser == null) {
            createdUser = userDAO.findByEmail(request.getEmail());
            if (createdUser == null) {
                result.setSuccess(false);
                result.setErrors(Arrays.asList("Lỗi hệ thống: Tài khoản đã được tạo nhưng không thể tải thông tin. Vui lòng thử đăng nhập."));
                return result;
            }
        } else {
            System.out.println("[RegistrationService] User loaded successfully by ID:");
            System.out.println("  - Username: " + createdUser.getUsername());
            System.out.println("  - Name (full name): " + createdUser.getName());
            System.out.println("  - Email: " + createdUser.getEmail());
            if (name != null && !name.equals(createdUser.getName())) {
                System.err.println("[RegistrationService] WARNING: Name mismatch!");
                System.err.println("  - Expected name: '" + name + "'");
                System.err.println("  - Actual name in DB: '" + createdUser.getName() + "'");
            }
        }

        // Assign USER role
        long actualUserId = (createdUser != null) ? createdUser.getId() : userId;
        Long userRoleId = roleDAO.findRoleIdByName("USER");
        if (userRoleId == null) {
            try {
                roleDAO.createDefaultRoles();
                userRoleId = roleDAO.findRoleIdByName("USER");
            } catch (Exception e) {
                System.err.println("[RegistrationService] ERROR creating default roles: " + e.getMessage());
            }
        }
        if (userRoleId == null) {
            userRoleId = 2L; // Fallback
        }
        
        // Assign role
        boolean roleAssigned = roleDAO.assignUserRole(actualUserId, userRoleId);
        if (!roleAssigned && !roleDAO.userHasRole(actualUserId, "USER")) {
            result.setSuccess(false);
            result.setErrors(Arrays.asList("Lỗi hệ thống: Không thể gán quyền người dùng. Vui lòng liên hệ quản trị viên."));
            return result;
        }

        // Create Student record automatically when user registers
        // IMPORTANT: This must succeed for the system to work properly
        // NOTE: students table only stores: user_id, weight, height, bmi, emergency_contact_*
        // Other info (full_name, email, phone...) is stored in user table
        try {
            Student student = new Student();
            student.setUserId((int) actualUserId);
            // Only set user_id - other fields will be NULL initially
            // User can update them later via Dashboard
            
            studentProfileService.saveProfile(student);
        } catch (Exception e) {
            System.err.println("[RegistrationService] ERROR creating Student profile: " + e.getMessage());
            e.printStackTrace();
        }

        // Log registration
        userDAO.insertLoginHistory(actualUserId, request.getIpAddress(), request.getUserAgent());
        
        result.setSuccess(true);
        result.setMessage("Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.");
        result.setUserId(actualUserId);

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

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            errors.add("Tên đầy đủ không được để trống");
        } else if (request.getName().trim().length() < 2) {
            errors.add("Tên đầy đủ phải có ít nhất 2 ký tự");
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
        private String name;  // Full name
        private String email;
        private String password;
        private String confirmPassword;
        private String ipAddress;
        private String userAgent;

        // Constructors
        public RegisterRequest() {}

        public RegisterRequest(String username, String name, String email, String password, String confirmPassword) {
            this.username = username;
            this.name = name;
            this.email = email;
            this.password = password;
            this.confirmPassword = confirmPassword;
        }

        // Getters and Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getConfirmPassword() { return confirmPassword; }
        public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

        public String getIpAddress() { return ipAddress; }
        public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

        public String getUserAgent() { return userAgent; }
        public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
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
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public long getUserId() { return userId; }
        public void setUserId(long userId) { this.userId = userId; }

        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }
    }
}
