package com.gym.util;

import com.gym.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 * SessionUtil - Utility class for managing user sessions
 */
public class SessionUtil {
    
    /**
     * Check if user is logged in
     */
    public static boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        return isLoggedIn != null && isLoggedIn;
    }
    
    /**
     * Get current user from session
     */
    public static User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (User) session.getAttribute("user");
    }
    
    /**
     * Get user ID from session
     */
    public static Long getUserId(HttpServletRequest request) {
        User user = getCurrentUser(request);
        return user != null ? user.getId() : null;
    }
    
    /**
     * Get user roles from session
     */
    @SuppressWarnings("unchecked")
    public static List<String> getUserRoles(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (List<String>) session.getAttribute("userRoles");
    }
    
    /**
     * Check if user has specific role
     */
    public static boolean hasRole(HttpServletRequest request, String roleName) {
        List<String> roles = getUserRoles(request);
        if (roles == null) {
            return false;
        }
        return roles.contains(roleName);
    }
    
    /**
     * Check if user is a member
     * Accepts both "USER" and "MEMBER" roles
     */
    public static boolean isMember(HttpServletRequest request) {
        return hasRole(request, "MEMBER") || hasRole(request, "member") 
            || hasRole(request, "USER") || hasRole(request, "user");
    }
    
    /**
     * Check if user is an admin
     */
    public static boolean isAdmin(HttpServletRequest request) {
        return hasRole(request, "ADMIN") || hasRole(request, "admin");
    }
    
    /**
     * Check if user is a personal trainer
     */
    public static boolean isPersonalTrainer(HttpServletRequest request) {
        return hasRole(request, "PERSONAL_TRAINER") || hasRole(request, "personal_trainer") || hasRole(request, "PT");
    }
    
    /**
     * Set user in session
     */
    public static void setUser(HttpServletRequest request, User user, List<String> roles) {
        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);
        session.setAttribute("userRoles", roles);
        session.setAttribute("isLoggedIn", true);
    }
    
    /**
     * Clear session (logout)
     */
    public static void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}

