package controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * SecurityFilter provides authentication and authorization
 * Protects admin and member areas
 */
@WebFilter(urlPatterns = {"/admin/*", "/member/*", "/employee/*"})
public class SecurityFilter implements Filter {
    private static final Logger logger = Logger.getLogger(SecurityFilter.class.getName());
    
    // Public URLs that don't require authentication
    private static final List<String> PUBLIC_URLS = Arrays.asList(
        "/login", "/register", "/home", "/", "/index.jsp", "/views/login.jsp", 
        "/views/register.jsp", "/views/home.jsp", "/views/error/", "/views/common/",
        "/css/", "/js/", "/images/", "/assets/"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("SecurityFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());
        
        logger.info("SecurityFilter checking: " + path);
        
        // Allow public URLs
        if (isPublicURL(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Check session
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            logger.info("No valid session found, redirecting to login");
            httpResponse.sendRedirect(contextPath + "/login");
            return;
        }
        
        String role = (String) session.getAttribute("role");
        if (role == null) {
            logger.info("No role found in session, redirecting to login");
            httpResponse.sendRedirect(contextPath + "/login");
            return;
        }
        
        // Check authorization based on URL path and role
        if (!isAuthorized(path, role)) {
            logger.info("Access denied for role " + role + " to " + path);
            httpResponse.sendRedirect(contextPath + "/views/error/403.jsp");
            return;
        }
        
        // Continue with the request
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.info("SecurityFilter destroyed");
    }

    private boolean isPublicURL(String path) {
        return PUBLIC_URLS.stream().anyMatch(path::startsWith);
    }

    private boolean isAuthorized(String path, String role) {
        // Admin and Manager can access everything
        if ("admin".equals(role) || "manager".equals(role)) {
            return true;
        }
        
        // Employee can access admin areas but with limited permissions
        if ("employee".equals(role)) {
            if (path.startsWith("/admin/")) {
                // Employee can access members, equipment, and POS
                return path.startsWith("/admin/members") || 
                       path.startsWith("/admin/equipment") || 
                       path.startsWith("/admin/pos") ||
                       path.startsWith("/admin/coaches");
            }
            return true;
        }
        
        // Member can only access member areas
        if ("member".equals(role)) {
            return path.startsWith("/member/") || path.startsWith("/views/member/");
        }
        
        return false;
    }
}

