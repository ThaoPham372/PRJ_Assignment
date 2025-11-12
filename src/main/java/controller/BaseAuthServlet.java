package controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.User;
import model.Member;
import Utils.ConfigManager;
import dao.MemberDAO;
import java.util.ArrayList;
import java.util.List;

/**
 * BaseAuthServlet - Lớp cơ sở chung cho các servlet xử lý authentication
 * 
 * Cung cấp các phương thức chung:
 * - Load Google Client ID từ ConfigManager
 * - Lấy client IP address
 * - Setup session cho user đã đăng nhập
 * 
 * Tuân thủ nguyên tắc DRY (Don't Repeat Yourself) và OOP
 */
public abstract class BaseAuthServlet extends HttpServlet {

    /**
     * Load Google Client ID từ ConfigManager và set vào request
     * Được sử dụng bởi cả LoginServlet và RegisterServlet
     * 
     * @param request HttpServletRequest
     */
    protected void loadGoogleClientId(HttpServletRequest request) {
        String googleClientId = ConfigManager.getInstance().getGoogleClientId();
        request.setAttribute("googleClientId", googleClientId);
    }

    /**
     * Lấy client IP address từ request
     * Hỗ trợ các header X-Forwarded-For và X-Real-IP (cho reverse proxy)
     * 
     * @param request HttpServletRequest
     * @return Client IP address
     */
    protected String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        return request.getRemoteAddr();
    }

    /**
     * Setup session cho user sau khi đăng nhập thành công
     * Tái sử dụng logic session setup cho cả login thường và Google login
     * Nếu user có role MEMBER, tự động lấy thông tin Member từ database và set vào session
     * 
     * @param session HttpSession
     * @param user User đã đăng nhập
     * @param role Role của user
     */
    protected void setupUserSession(HttpSession session, User user, String role) {
        // Convert role to List for consistency với GoogleAuthServlet
        List<String> roles = new ArrayList<>();
        if (role != null && !role.trim().isEmpty()) {
            roles.add(role);
        }

        session.setAttribute("user", user);
        session.setAttribute("userRoles", roles);
        session.setAttribute("isLoggedIn", true);
        session.setAttribute("userId", user.getId());
        
        // Nếu user có role MEMBER, lấy thông tin Member từ DB và set vào session
        // Điều này cho phép chatbot và các trang member hoạt động đúng
        if ("MEMBER".equalsIgnoreCase(role)) {
            try {
                MemberDAO memberDAO = new MemberDAO();
                // Member extends User và dùng @PrimaryKeyJoinColumn, nên member.id = user.id
                Member member = memberDAO.findById(user.getId());
                if (member != null) {
                    session.setAttribute("member", member);
                    System.out.println("[" + getClass().getSimpleName() + "] Member info loaded for user: " + user.getUsername() + " (memberId=" + member.getId() + ")");
                } else {
                    System.out.println("[" + getClass().getSimpleName() + "] Warning: User has MEMBER role but no Member record found for userId: " + user.getId());
                }
            } catch (Exception e) {
                System.err.println("[" + getClass().getSimpleName() + "] Error loading member info: " + e.getMessage());
            }
        }
    }

    /**
     * Setup session timeout dựa trên remember me option
     * 
     * @param session HttpSession
     * @param rememberMe "on" nếu user chọn remember me, null nếu không
     */
    protected void setupSessionTimeout(HttpSession session, String rememberMe) {
        if ("on".equals(rememberMe)) {
            session.setMaxInactiveInterval(7 * 24 * 60 * 60); // 7 days
            System.out.println("[" + getClass().getSimpleName() + "] Remember me enabled - session timeout: 7 days");
        } else {
            session.setMaxInactiveInterval(30 * 60); // 30 minutes
            System.out.println("[" + getClass().getSimpleName() + "] Session timeout: 30 minutes");
        }
    }
}

