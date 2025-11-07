
package com.gym.util;

/*
    Note: 
 */

import com.gym.model.User;
import com.gym.service.PasswordService;
import jakarta.servlet.http.HttpServletRequest;

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
            String dob = request.getParameter("birthday").trim();
            obj.setDob(DateUtils.parseToLocalDate(dob));
        }

        if (request.getParameter("address") != null) {
            obj.setAddress(request.getParameter("address").trim());
        }

        if (request.getParameter("role") != null) {
            obj.setRole(request.getParameter("role").trim());
        }

        if (request.getParameter("username") != null) {
            obj.setUsername(request.getParameter("username").trim());
        }

        String passwordStr = request.getParameter("password");
        if (passwordStr != null && !passwordStr.isEmpty() && passwordService.isValidPassword(passwordStr)) {
            String passwordHash = passwordService.hashPassword(request.getParameter("password"));
            obj.setPassword(passwordHash);
        }

        if (request.getParameter("status") != null) {
            obj.setStatus(request.getParameter("status").trim());
        }

        if (request.getParameter("emailVerified") != null) {
            // admin.setEmailVerified(request.getParameter("emailVerified").equals("true"));
        }

        if (request.getParameter("failedLoginAttempts") != null) {
            try {
                obj.setFailedLoginAttempts(Integer.valueOf(request.getParameter("failedLoginAttempts")));
            } catch (NumberFormatException e) {
                obj.setFailedLoginAttempts(0);
            }
        }

        if (request.getParameter("createdDate") != null) {
            obj.setCreatedDate(DateUtils.parseDateInputToLocalDateTime(request.getParameter("createdDate")));
        }
        if (request.getParameter("lastLogin") != null) {
            obj.setLastLogin(DateUtils.parseDateInputToLocalDateTime(request.getParameter("lastLogin")));
        }
        if (request.getParameter("lockedUntil") != null) {
            obj.setLockedUntil(DateUtils.parseDateInputToLocalDateTime(request.getParameter("lockedUntil")));
        }
    }

}

