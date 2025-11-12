package filter;

import Utils.SessionUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * BaseRoleFilter - Base class for role-based filters
 * 
 * Implements OOP principles:
 * - Template Method Pattern: Defines skeleton of filter logic
 * - Single Responsibility: Handles common role checking logic
 * - Open/Closed Principle: Extendable for different role requirements
 * 
 * Provides common functionality:
 * - Session validation
 * - Role extraction and normalization (case-insensitive)
 * - Redirect handling
 */
public abstract class BaseRoleFilter implements Filter {
    
    // Role constants - normalized to uppercase
    protected static final String ROLE_ADMIN = "ADMIN";
    protected static final String ROLE_TRAINER = "TRAINER";
    protected static final String ROLE_PT = "PT";
    protected static final String ROLE_MEMBER = "MEMBER";
    protected static final String ROLE_USER = "USER";
    
    // Redirect URLs
    protected static final String URL_LOGIN = "/auth/login";
    protected static final String URL_HOME = "/home";
    protected static final String URL_ACCESS_DENIED = "/views/error/403.jsp";
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Override in subclasses if needed
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Skip filter for static resources and auth pages
        String requestURI = httpRequest.getRequestURI();
        if (shouldSkipFilter(requestURI)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Check if user is logged in
        if (!SessionUtil.isLoggedIn(httpRequest)) {
            handleUnauthenticated(httpRequest, httpResponse);
            return;
        }
        
        // Get normalized role from session (case-insensitive)
        String userRole = getNormalizedRole(httpRequest);
        
        if (userRole == null) {
            handleInvalidRole(httpRequest, httpResponse);
            return;
        }
        
        // Check if user has required role (template method - implemented by subclasses)
        if (!hasRequiredRole(userRole)) {
            handleUnauthorized(httpRequest, httpResponse, userRole);
            return;
        }
        
        // User has required role, continue filter chain
        chain.doFilter(request, response);
    }
    
    /**
     * Template method - Subclasses must implement this to specify required role
     * 
     * @param userRole Normalized role from session (uppercase)
     * @return true if user has required role, false otherwise
     */
    protected abstract boolean hasRequiredRole(String userRole);
    
    /**
     * Get redirect URL when user doesn't have required role
     * Override in subclasses for custom redirect behavior
     * 
     * @param userRole Current user's role
     * @return Redirect URL
     */
    protected String getUnauthorizedRedirectUrl(String userRole) {
        return URL_ACCESS_DENIED;
    }
    
    /**
     * Handle unauthenticated users (not logged in)
     */
    protected void handleUnauthenticated(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String contextPath = request.getContextPath();
        response.sendRedirect(contextPath + URL_LOGIN);
    }
    
    /**
     * Handle users with invalid/null role
     */
    protected void handleInvalidRole(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String contextPath = request.getContextPath();
        response.sendRedirect(contextPath + URL_ACCESS_DENIED);
    }
    
    /**
     * Handle unauthorized users (wrong role)
     */
    protected void handleUnauthorized(HttpServletRequest request, HttpServletResponse response, String userRole)
            throws IOException {
        String contextPath = request.getContextPath();
        String redirectUrl = getUnauthorizedRedirectUrl(userRole);
        response.sendRedirect(contextPath + redirectUrl);
    }
    
    /**
     * Extract and normalize role from session (case-insensitive)
     * 
     * @param request HTTP request
     * @return Normalized role (uppercase) or null if not found
     */
    protected String getNormalizedRole(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        
        // Try to get role from SessionUtil first
        String role = SessionUtil.getUserRole(request);
        
        // Fallback: check userRoles list in session
        if (role == null || role.trim().isEmpty()) {
            java.util.List<String> roles = SessionUtil.getUserRoles(request);
            if (roles != null && !roles.isEmpty()) {
                role = roles.get(0);
            }
        }
        
        // Normalize to uppercase for case-insensitive comparison
        return normalizeRole(role);
    }
    
    /**
     * Check if filter should be skipped for this request
     * Skip static resources, auth pages, and error pages
     * 
     * @param requestURI Request URI
     * @return true if filter should be skipped
     */
    protected boolean shouldSkipFilter(String requestURI) {
        if (requestURI == null) {
            return false;
        }
        
        // Skip static resources
        if (requestURI.contains("/css/") || 
            requestURI.contains("/js/") || 
            requestURI.contains("/images/") ||
            requestURI.contains("/uploads/") ||
            requestURI.endsWith(".css") ||
            requestURI.endsWith(".js") ||
            requestURI.endsWith(".png") ||
            requestURI.endsWith(".jpg") ||
            requestURI.endsWith(".jpeg") ||
            requestURI.endsWith(".gif") ||
            requestURI.endsWith(".ico") ||
            requestURI.endsWith(".svg") ||
            requestURI.endsWith(".woff") ||
            requestURI.endsWith(".woff2") ||
            requestURI.endsWith(".ttf") ||
            requestURI.endsWith(".eot")) {
            return true;
        }
        
        // Skip auth pages
        if (requestURI.contains("/auth/") ||
            requestURI.contains("/login") ||
            requestURI.contains("/register") ||
            requestURI.contains("/logout")) {
            return true;
        }
        
        // Skip error pages
        if (requestURI.contains("/views/error/")) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Normalize role string for case-insensitive comparison
     * 
     * @param role Raw role string
     * @return Normalized role (trimmed and uppercase) or null if invalid
     */
    protected String normalizeRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            return null;
        }
        return role.trim().toUpperCase();
    }
    
    /**
     * Check if role matches target role (case-insensitive)
     * 
     * @param role Role to check (should be normalized)
     * @param targetRole Target role constant
     * @return true if roles match
     */
    protected boolean isRoleMatch(String role, String targetRole) {
        if (role == null || targetRole == null) {
            return false;
        }
        return role.equalsIgnoreCase(targetRole);
    }
    
    @Override
    public void destroy() {
        // Cleanup if needed
    }
}

