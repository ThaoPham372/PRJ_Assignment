package service;

import java.util.ArrayList;
import java.util.List;

import dao.UserDAO;
import model.User;

public class LoginService {

    private UserDAO userDAO;
    private PasswordService passwordService;

    public LoginService() {
        this.userDAO = new UserDAO();
        this.passwordService = new PasswordService();
    }

    public LoginResult authenticate(String username, String password) {
        List<String> errors = new ArrayList<>();

        if (!isValidInput(username, password, errors)) {
            return new LoginResult(false, null, errors);
        }

        try {
            User user = userDAO.findByUsernameOrEmail(username.trim());
            if (!isAvailableAccount(user, errors)) {
                return new LoginResult(false, null, errors);
            }

            return loginSuccessful(user);
        } catch (Exception e) {
            System.err.println("Unexpected error during login: " + e.getMessage());
            errors.add("Lỗi hệ thống. Vui lòng thử lại sau.");
            return new LoginResult(false, null, errors);
        }
    }

    private boolean isValidInput(String username, String password, List<String> errors) {
        if (username == null || username.trim().isEmpty()) {
            errors.add("Tên đăng nhập không được để trống");
            return false;
        }

        if (password == null || password.trim().isEmpty()) {
            errors.add("Mật khẩu không được để trống");
            return false;
        }
        return true;
    }

    private boolean isAvailableAccount(User user, List<String> errors) {

        if (user == null) {
            errors.add("Tên đăng nhập hoặc mật khẩu không đúng");
        }

        if (!"ACTIVE".equals(user.getStatus())) {
            errors.add("Tài khoản đã bị khóa hoặc chưa được kích hoạt");
            return false;
        }

        return true;
    }

    private LoginResult loginSuccessful(User user) {
        userDAO.updateLastLogin(user.getId());

        String role = user.getRole();

        return new LoginResult(true, user, role, null);
    }

    public static class LoginResult {

        private final boolean success;
        private final User user;
        private String roles;
        private final List<String> errors;

        public LoginResult(boolean success, User user, List<String> errors) {
            this.success = success;
            this.user = user;
            this.errors = errors;
        }

        public LoginResult(boolean success, User user, String roles, List<String> errors) {
            this.success = success;
            this.user = user;
            this.roles = roles;
            this.errors = errors;
        }

        public boolean isSuccess() {
            return success;
        }

        public User getUser() {
            return user;
        }

        public String getRole() {
            return roles;
        }

        public List<String> getErrors() {
            return errors;
        }
    }
}
