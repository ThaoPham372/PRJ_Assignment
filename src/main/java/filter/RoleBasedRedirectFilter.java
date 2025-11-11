package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * RoleBasedRedirectFilter - Filter để điều hướng user dựa trên role sau khi đăng nhập
 * 
 * Chức năng:
 * - Khi user đăng nhập thành công và được redirect đến /home
 * - Filter sẽ kiểm tra role và redirect đến dashboard tương ứng
 * - ADMIN → /admin/dashboard
 * - PT → /pt/dashboard  
 * - MEMBER → /member/dashboard
 * 
 * Pattern: Intercepting Filter Pattern
 * Tách biệt logic điều hướng khỏi servlet → Single Responsibility
 */
@WebFilter(filterName = "RoleBasedRedirectFilter", urlPatterns = {"/home"})
public class RoleBasedRedirectFilter implements Filter {
    
    // Role constants - Case-insensitive comparison
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_PT = "PT";
    private static final String ROLE_TRAINER = "TRAINER";
    private static final String ROLE_MEMBER = "MEMBER";
    private static final String ROLE_USER = "USER";
    
    // Dashboard URLs
    private static final String URL_ADMIN_DASHBOARD = "/admin/dashboard";
    private static final String URL_PT_DASHBOARD = "/pt/dashboard";
    private static final String URL_MEMBER_DASHBOARD = "/member/dashboard";
    private static final String URL_HOME = "/home";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("[RoleBasedRedirectFilter] Initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // Kiểm tra user đã đăng nhập chưa
        if (session != null && session.getAttribute("isLoggedIn") != null) {
            Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
            
            if (isLoggedIn) {
                // Extract and normalize role from session
                String role = extractRoleFromSession(session);
                
                if (role != null) {
                    System.out.println("[RoleBasedRedirectFilter] User logged in with role: " + role);
                    
                    // Điều hướng dựa trên role
                    String redirectUrl = getDashboardUrlByRole(role);
                    
                    if (redirectUrl != null && !redirectUrl.equals(URL_HOME)) {
                        System.out.println("[RoleBasedRedirectFilter] Redirecting to: " + redirectUrl);
                        httpResponse.sendRedirect(httpRequest.getContextPath() + redirectUrl);
                        return; // Dừng filter chain
                    }
                }
            }
        }

        // Nếu không redirect, tiếp tục filter chain bình thường
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("[RoleBasedRedirectFilter] Destroyed");
    }

    /**
     * Extract role from session and normalize it
     * Handles both List<String> and String types for backward compatibility
     * Also checks user.role as fallback
     * 
     * @param session HTTP session
     * @return Normalized role string (uppercase) or null if not found
     */
    private String extractRoleFromSession(HttpSession session) {
        Object userRolesObj = session.getAttribute("userRoles");
        String role = null;
        
        if (userRolesObj instanceof List) {
            // userRoles is a List<String>, extract the first role
            @SuppressWarnings("unchecked")
            List<String> rolesList = (List<String>) userRolesObj;
            if (!rolesList.isEmpty()) {
                role = rolesList.get(0);
            }
        } else if (userRolesObj instanceof String) {
            // Fallback for backward compatibility
            role = (String) userRolesObj;
        }
        
        // Fallback: Nếu không tìm thấy trong userRoles, lấy từ user.role
        if (role == null || role.trim().isEmpty()) {
            Object userObj = session.getAttribute("user");
            if (userObj != null && userObj instanceof model.User) {
                model.User user = (model.User) userObj;
                role = user.getRole();
            }
        }
        
        // Normalize role to uppercase for case-insensitive comparison
        return normalizeRole(role);
    }
    
    /**
     * Normalize role string for case-insensitive comparison
     * 
     * @param role Raw role string
     * @return Normalized role (trimmed and uppercase) or null if invalid
     */
    private String normalizeRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            return null;
        }
        return role.trim().toUpperCase();
    }
    
    /**
     * Xác định URL dashboard dựa trên role (case-insensitive)
     * 
     * @param role User's role (already normalized to uppercase)
     * @return Dashboard URL corresponding to the role
     */
    private String getDashboardUrlByRole(String role) {
        if (role == null) {
            return URL_HOME;
        }

        // Kiểm tra ADMIN trước (case-insensitive)
        if (isRoleMatch(role, ROLE_ADMIN)) {
            return URL_ADMIN_DASHBOARD;
        }
        
        // Kiểm tra PT/Trainer
        if (isRoleMatch(role, ROLE_PT) || isRoleMatch(role, ROLE_TRAINER)) {
            return URL_PT_DASHBOARD;
        }
        
        // Kiểm tra Member/User
        if (isRoleMatch(role, ROLE_MEMBER) || isRoleMatch(role, ROLE_USER)) {
            return URL_MEMBER_DASHBOARD;
        }
        
        // Default fallback
        return URL_HOME;
    }
    
    /**
     * Check if role matches target role (case-insensitive)
     * 
     * @param role Role to check
     * @param targetRole Target role constant
     * @return true if roles match
     */
    private boolean isRoleMatch(String role, String targetRole) {
        return role != null && role.equalsIgnoreCase(targetRole);
    }
}



