
package Utils;

import jakarta.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.User;
import service.PasswordService;

/*
    Note: 
 */
public class FormUtils {

    public static void getFormValue(HttpServletRequest request, User obj) {
        PasswordService passwordService = new PasswordService();

        if (request.getParameter("name") != null) {
            obj.setName(request.getParameter("name").trim());
        }

        if (request.getParameter("email") != null) {
            obj.setEmail(request.getParameter("email").trim());
        }

        if (request.getParameter("phone") != null) {
            obj.setPhone(request.getParameter("phone").trim());
        }

        if (request.getParameter("birthday") != null && !request.getParameter("birthday").trim().isEmpty()) {
            try {
                Date dob = new SimpleDateFormat("yyyy-MM-dd")
                        .parse(request.getParameter("birthday").trim());
                obj.setDob(dob);
            } catch (Exception e) {
                System.out.println("Error: Admin DB Servlet -> setAdmin()");
            }
        }

        if (request.getParameter("address") != null) {
            obj.setAddress(request.getParameter("address").trim());
        }

        if (request.getParameter("role") != null) {
            String role = request.getParameter("role").trim();
            // Chuẩn hóa role: admin -> Admin, member -> Member, trainer -> Trainer
            if (role.equalsIgnoreCase("admin")) {
                obj.setRole("Admin");
            } else if (role.equalsIgnoreCase("member")) {
                obj.setRole("Member");
            } else if (role.equalsIgnoreCase("trainer")) {
                obj.setRole("Trainer");
            } else {
                obj.setRole(role);
            }
        }

        if (request.getParameter("username") != null) {
            obj.setUsername(request.getParameter("username").trim());
        }

        String passwordStr = request.getParameter("password");
        if (passwordStr != null && !passwordStr.isEmpty()) {
            // Luôn hash mật khẩu nếu có (giống như RegistrationService)
            // Validation nên được xử lý ở tầng servlet/service, không phải ở FormUtils
            String passwordHash = passwordService.hashPassword(passwordStr);
            obj.setPassword(passwordHash);
        }

        if (request.getParameter("status") != null) {
            String status = request.getParameter("status").trim().toUpperCase();
            obj.setStatus(status);
        }

        if (request.getParameter("emailVerified") != null) {
            // admin.setEmailVerified(request.getParameter("emailVerified").equals("true"));
        }

        if (request.getParameter("createdDate") != null) {
            obj.setCreatedDate(DateUtils.parseToDate(request.getParameter("createdDate")));
        }
        if (request.getParameter("lastLogin") != null) {
            obj.setLastLogin(DateUtils.parseToDate(request.getParameter("lastLogin")));
        }
    }

}
