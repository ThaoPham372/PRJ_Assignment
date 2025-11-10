package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

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
                String role = (String) session.getAttribute("userRoles");
                
                System.out.println("[RoleBasedRedirectFilter] User logged in with role: " + role);
                
                // Điều hướng dựa trên role
                String redirectUrl = getDashboardUrlByRole(role);
                
                if (redirectUrl != null && !redirectUrl.equals("/home")) {
                    System.out.println("[RoleBasedRedirectFilter] Redirecting to: " + redirectUrl);
                    httpResponse.sendRedirect(httpRequest.getContextPath() + redirectUrl);
                    return; // Dừng filter chain
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
     * Xác định URL dashboard dựa trên role
     * 
     * @param role User's role
     * @return Dashboard URL
     */
    private String getDashboardUrlByRole(String role) {
        if (role == null || role.isEmpty()) {
            return "/home"; // Guest user
        }

        switch (role.toUpperCase()) {
            case "ADMIN":
                return "/admin/dashboard";
            
            case "trainer":
            case "TRAINER":
                case "Trainer":
                return "/pt/home";
            
            case "MEMBER":
                return "/member/dashboard";
            
            default:
                return "/home";
        }
    }
}



