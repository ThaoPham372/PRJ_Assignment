package controller;

import DAO.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * RegisterServlet handles user registration
 * Processes new member registration requests
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(RegisterServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to register page
        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get form parameters
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String dateOfBirthStr = request.getParameter("dateOfBirth");
        String gender = request.getParameter("gender");
        String address = request.getParameter("address");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String packageType = request.getParameter("packageType");
        String emergencyName = request.getParameter("emergencyName");
        String emergencyPhone = request.getParameter("emergencyPhone");
        String agreeTerms = request.getParameter("agreeTerms");

        // Validation
        StringBuilder errors = new StringBuilder();

        if (fullName == null || fullName.trim().isEmpty()) {
            errors.append("Họ và tên không được để trống.<br>");
        }

        if (email == null || email.trim().isEmpty() || !isValidEmail(email)) {
            errors.append("Email không hợp lệ.<br>");
        }

        if (phone == null || phone.trim().isEmpty()) {
            errors.append("Số điện thoại không được để trống.<br>");
        }

        if (username == null || username.trim().isEmpty()) {
            errors.append("Tên đăng nhập không được để trống.<br>");
        }

        if (password == null || password.trim().isEmpty() || password.length() < 6) {
            errors.append("Mật khẩu phải có ít nhất 6 ký tự.<br>");
        }

        if (!password.equals(confirmPassword)) {
            errors.append("Mật khẩu xác nhận không khớp.<br>");
        }

        if (packageType == null || packageType.trim().isEmpty()) {
            errors.append("Vui lòng chọn gói membership.<br>");
        }

        if (agreeTerms == null || !agreeTerms.equals("on")) {
            errors.append("Vui lòng đồng ý với điều khoản và điều kiện.<br>");
        }

        if (errors.length() > 0) {
            request.setAttribute("error", errors.toString());
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        try {
            UserDAO userDAO = new UserDAO();

            // Check if username or email already exists
            if (userDAO.isUsernameExists(username)) {
                request.setAttribute("error", "Tên đăng nhập đã được sử dụng.");
                request.getRequestDispatcher("/views/register.jsp").forward(request, response);
                return;
            }

            if (userDAO.isEmailExists(email)) {
                request.setAttribute("error", "Email đã được sử dụng.");
                request.getRequestDispatcher("/views/register.jsp").forward(request, response);
                return;
            }

            // Parse date of birth
            Date dateOfBirth = null;
            if (dateOfBirthStr != null && !dateOfBirthStr.trim().isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    dateOfBirth = sdf.parse(dateOfBirthStr);
                } catch (ParseException e) {
                    logger.warning("Invalid date format: " + dateOfBirthStr);
                }
            }

            // Create new user
            User newUser = new User();
            newUser.setFullName(fullName.trim());
            newUser.setEmail(email.trim());
            newUser.setPhone(phone.trim());
            newUser.setDateOfBirth(dateOfBirth);
            newUser.setGender(gender);
            newUser.setAddress(address);
            newUser.setUsername(username.trim());
            newUser.setPassword(password); // Password will be hashed in DAO
            newUser.setRole("member");
            newUser.setPackageType(packageType);
            newUser.setEmergencyContactName(emergencyName);
            newUser.setEmergencyContactPhone(emergencyPhone);
            newUser.setJoinDate(new Date());
            newUser.setStatus("active");

            // Save user to database
            boolean success = userDAO.createUser(newUser);

            if (success) {
                logger.info("New user registered: " + username);
                request.setAttribute("success", "Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.");
                request.setAttribute("registeredUsername", username);
                request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi đăng ký. Vui lòng thử lại.");
                request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error during registration", e);
            request.setAttribute("error", "Lỗi hệ thống. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error during registration", e);
            request.setAttribute("error", "Đã xảy ra lỗi. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }
}

