package filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AuthenticationFilter - Filter for pages that require user authentication
 * 
 * Use cases:
 * - Shopping cart operations (requires login)
 * - Checkout process (requires login)
 * - Order management (requires login)
 * - Member-specific pages
 * 
 * Pattern: Intercepting Filter Pattern
 * Follows MVC: Separates authentication logic from controllers
 * 
 * Configured in web.xml to ensure proper filter ordering
 */
public class AuthenticationFilter extends BaseRoleFilter {
    
    @Override
    public void init(jakarta.servlet.FilterConfig filterConfig) throws jakarta.servlet.ServletException {
        super.init(filterConfig);
        System.out.println("[AuthenticationFilter] Initialized - Protecting authenticated pages");
    }
    
    @Override
    protected boolean hasRequiredRole(String userRole) {
        // Any authenticated user can access these pages
        // Role-specific access is handled by MemberFilter, AdminFilter, etc.
        return userRole != null;
    }
    
    @Override
    protected void handleUnauthenticated(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String contextPath = request.getContextPath();
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        
        // Store the original URL for redirect after login
        String redirectUrl = requestURI;
        if (queryString != null) {
            redirectUrl += "?" + queryString;
        }
        
        // Redirect to login with return URL
        response.sendRedirect(contextPath + URL_LOGIN + "?redirect=" + 
            java.net.URLEncoder.encode(redirectUrl, "UTF-8"));
    }
    
    @Override
    public void destroy() {
        System.out.println("[AuthenticationFilter] Destroyed");
    }
}

