package Utils;

import model.User;
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
    public static Integer getUserId(HttpServletRequest request) {
        User user = getCurrentUser(request);
        return user != null ? user.getId() : null;
    }
    
    /**
     * Get user roles from session
     * Note: roles are stored as a list in session, populated from user.role column
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
     * Get user role directly from User object (single role from user.role column)
     * @return role string like "USER", "ADMIN", "PT", etc. or null
     */
    public static String getUserRole(HttpServletRequest request) {
        User user = getCurrentUser(request);
        return user != null ? user.getRole() : null;
    }
    
    /**
     * Check if user has specific role
     * Checks both session roles list and user.role column
     */
    public static boolean hasRole(HttpServletRequest request, String roleName) {
        // Check session roles list first (for backward compatibility)
        List<String> roles = getUserRoles(request);
        if (roles != null && roles.contains(roleName)) {
            return true;
        }
        
        // Fallback to user.role column
        String userRole = getUserRole(request);
        return userRole != null && userRole.equalsIgnoreCase(roleName);
    }
    
    /**
     * Check if user is a member
     * Accepts both "USER" and "MEMBER" roles
     */
    public static boolean isMember(HttpServletRequest request) {
        return hasRole(request, "MEMBER") || hasRole(request, "member") ;
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
    public static boolean isTrainer(HttpServletRequest request) {
        return hasRole(request, "TRAINER") || hasRole(request, "trainer") || hasRole(request, "PT");
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

