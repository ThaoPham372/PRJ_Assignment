
package com.gym.Validator;


import com.gym.model.User;
import com.gym.service.UserService;
import java.util.regex.Pattern;

/*
    Note: 
 */

public class InputValidator {

    // 1️⃣ Chuỗi hợp lệ (không null, không rỗng)
    public static boolean isValidString(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static boolean isValidUsername(String username) {
        boolean isValidForm = username != null && username.matches("^[a-zA-Z0-9._]{3,20}$");
        if (!isValidForm)
            return false;
        return !isExistUsername(username);
    }

    private static boolean isExistUsername(String username) {
        UserService userService = new UserService();
        User user = userService.getUserByUsername(username);
        return user != null;
    }

    public static boolean isValidPassword(String password) {
        return password != null &&
                password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }

    public static boolean isValidEmail(String email) {
        UserService userService = new UserService();
        User user = userService.getUserByEmail(email);
        if (user != null)
            return false;
        return email != null && Pattern
                .compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")
                .matcher(email)
                .matches();
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null)
            return false;
        return phone.matches("^(\\+84|0)[0-9]{9,10}$");
    }

}
