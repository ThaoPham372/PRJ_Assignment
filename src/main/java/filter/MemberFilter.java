package filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * MemberFilter - Filter for member-only pages
 * 
 * Protects all /member/* URLs (except those already protected by AuthenticationFilter)
 * Only users with MEMBER or USER role can access
 * 
 * Note: This filter works in conjunction with AuthenticationFilter
 * AuthenticationFilter ensures user is logged in
 * MemberFilter ensures user has MEMBER/USER role
 * 
 * Pattern: Intercepting Filter Pattern
 * Follows MVC: Separates authorization logic from controllers
 * Follows OOP: Extends BaseRoleFilter for code reuse
 * 
 * Configured in web.xml to ensure proper filter ordering
 */
public class MemberFilter extends BaseRoleFilter {
    
    @Override
    public void init(jakarta.servlet.FilterConfig filterConfig) throws jakarta.servlet.ServletException {
        super.init(filterConfig);
        System.out.println("[MemberFilter] Initialized - Protecting member pages");
    }
    
    @Override
    protected boolean hasRequiredRole(String userRole) {
        // MEMBER or USER role can access
        return isRoleMatch(userRole, ROLE_MEMBER) || isRoleMatch(userRole, ROLE_USER);
    }
    
    @Override
    protected String getUnauthorizedRedirectUrl(String userRole) {
        // Redirect based on user's actual role
        if (isRoleMatch(userRole, ROLE_ADMIN)) {
            return "/admin/dashboard";
        } else if (isRoleMatch(userRole, ROLE_TRAINER) || isRoleMatch(userRole, ROLE_PT)) {
            return "/pt/dashboard";
        }
        return URL_HOME;
    }
    
    @Override
    protected void handleUnauthorized(HttpServletRequest request, HttpServletResponse response, String userRole)
            throws IOException {
        String contextPath = request.getContextPath();
        String redirectUrl = getUnauthorizedRedirectUrl(userRole);
        
        System.out.println("[MemberFilter] Access denied for role: " + userRole + 
                          ", redirecting to: " + redirectUrl);
        
        response.sendRedirect(contextPath + redirectUrl);
    }
    
    @Override
    public void destroy() {
        System.out.println("[MemberFilter] Destroyed");
    }
}

