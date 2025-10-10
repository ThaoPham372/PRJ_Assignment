package util;

import model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Utility class cho session management
 */
public class SessionUtil {
    
    // Session attribute keys
    public static final String USER_ID = "userId";
    public static final String USERNAME = "username";
    public static final String FULL_NAME = "fullName";
    public static final String EMAIL = "email";
    public static final String ROLE = "role";
    public static final String AVATAR_URL = "avatarUrl";
    public static final String MEMBER_ID = "memberId";
    public static final String MEMBER_CODE = "memberCode";
    
    // Session timeout (30 minutes)
    private static final int SESSION_TIMEOUT = 30 * 60;
    
    /**
     * Create session cho user sau khi login
     */
    public static void createUserSession(HttpServletRequest request, User user) {
        HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(SESSION_TIMEOUT);
        
        session.setAttribute(USER_ID, user.getUserId());
        session.setAttribute(USERNAME, user.getUsername());
        session.setAttribute(FULL_NAME, user.getFullName());
        session.setAttribute(EMAIL, user.getEmail());
        session.setAttribute(ROLE, user.getRole());
        session.setAttribute(AVATAR_URL, user.getAvatarUrl());
    }
    
    /**
     * Create session cho user với member info
     */
    public static void createUserSession(HttpServletRequest request, User user, int memberId, String memberCode) {
        createUserSession(request, user);
        HttpSession session = request.getSession(false);
        session.setAttribute(MEMBER_ID, memberId);
        session.setAttribute(MEMBER_CODE, memberCode);
    }
    
    /**
     * Invalidate session (logout)
     */
    public static void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
    
    /**
     * Check if user is logged in
     */
    public static boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(USER_ID) != null;
    }
    
    /**
     * Get logged in user ID
     */
    public static Integer getUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (Integer) session.getAttribute(USER_ID);
        }
        return null;
    }
    
    /**
     * Get logged in username
     */
    public static String getUsername(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (String) session.getAttribute(USERNAME);
        }
        return null;
    }
    
    /**
     * Get logged in user role
     */
    public static String getRole(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (String) session.getAttribute(ROLE);
        }
        return null;
    }
    
    /**
     * Get member ID
     */
    public static Integer getMemberId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (Integer) session.getAttribute(MEMBER_ID);
        }
        return null;
    }
    
    /**
     * Check if user has role
     */
    public static boolean hasRole(HttpServletRequest request, String role) {
        String userRole = getRole(request);
        return userRole != null && userRole.equals(role);
    }
    
    /**
     * Check if user is admin
     */
    public static boolean isAdmin(HttpServletRequest request) {
        return hasRole(request, "admin");
    }
    
    /**
     * Check if user is member
     */
    public static boolean isMember(HttpServletRequest request) {
        return hasRole(request, "member");
    }
    
    /**
     * Check if user is coach
     */
    public static boolean isCoach(HttpServletRequest request) {
        return hasRole(request, "coach");
    }
    
    /**
     * Update session attribute
     */
    public static void updateSessionAttribute(HttpServletRequest request, String key, Object value) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute(key, value);
        }
    }
    
    /**
     * Get session attribute
     */
    public static Object getSessionAttribute(HttpServletRequest request, String key) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return session.getAttribute(key);
        }
        return null;
    }
}

