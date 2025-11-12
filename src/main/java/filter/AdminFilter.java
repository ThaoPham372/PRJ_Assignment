package filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AdminFilter - Filter for admin-only pages
 * 
 * Protects all /admin/* URLs
 * Only users with ADMIN role can access
 * 
 * Pattern: Intercepting Filter Pattern
 * Follows MVC: Separates authorization logic from controllers
 * Follows OOP: Extends BaseRoleFilter for code reuse
 * 
 * Configured in web.xml to ensure proper filter ordering
 */
public class AdminFilter extends BaseRoleFilter {
    
    @Override
    public void init(jakarta.servlet.FilterConfig filterConfig) throws jakarta.servlet.ServletException {
        super.init(filterConfig);
        System.out.println("[AdminFilter] Initialized - Protecting admin pages");
    }
    
    @Override
    protected boolean hasRequiredRole(String userRole) {
        // Only ADMIN role can access
        return isRoleMatch(userRole, ROLE_ADMIN);
    }
    
    @Override
    protected String getUnauthorizedRedirectUrl(String userRole) {
        // Redirect based on user's actual role
        if (isRoleMatch(userRole, ROLE_TRAINER) || isRoleMatch(userRole, ROLE_PT)) {
            return "/pt/dashboard";
        } else if (isRoleMatch(userRole, ROLE_MEMBER) || isRoleMatch(userRole, ROLE_USER)) {
            return "/member/dashboard";
        }
        return URL_HOME;
    }
    
    @Override
    protected void handleUnauthorized(HttpServletRequest request, HttpServletResponse response, String userRole)
            throws IOException {
        String contextPath = request.getContextPath();
        String redirectUrl = getUnauthorizedRedirectUrl(userRole);
        
        System.out.println("[AdminFilter] Access denied for role: " + userRole + 
                          ", redirecting to: " + redirectUrl);
        
        response.sendRedirect(contextPath + redirectUrl);
    }
    
    @Override
    public void destroy() {
        System.out.println("[AdminFilter] Destroyed");
    }
}

