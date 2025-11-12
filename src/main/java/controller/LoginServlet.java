package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import service.LoginService;
import service.LoginService.LoginResult;

/**
 * LoginServlet - Controller xử lý đăng nhập
 * 
 * Tuân thủ mô hình MVC và nguyên tắc Single Responsibility
 * 
 * Endpoints:
 * GET  /auth          - Hiển thị trang đăng nhập
 * GET  /login         - Hiển thị trang đăng nhập
 * GET  /auth/login    - Hiển thị trang đăng nhập
 * POST /auth          - Xử lý đăng nhập
 * POST /login         - Xử lý đăng nhập
 * POST /auth/login    - Xử lý đăng nhập
 * 
 * Chức năng:
 * - Hiển thị form đăng nhập
 * - Xác thực username/password
 * - Setup session sau khi đăng nhập thành công
 * - Hỗ trợ "Remember Me" để kéo dài session
 * - Load Google Client ID cho Google OAuth
 */
@WebServlet(name = "LoginServlet", urlPatterns = {
    "/auth",
    "/login",
    "/auth/login"
})
public class LoginServlet extends BaseAuthServlet {

    private LoginService loginService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.loginService = new LoginService();
        System.out.println("[LoginServlet] Initialized successfully");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Load Google Client ID cho Google OAuth button
        loadGoogleClientId(request);
        
        // Đọc cookie để tự động điền form
        loadRememberMeCookies(request);
        
        // Forward đến trang login
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleLogin(request, response);
    }

    /**
     * Xử lý đăng nhập
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");

        System.out.println("[LoginServlet] Login attempt for username: " + username);
        
        // Xác thực user
        LoginResult result = loginService.authenticate(username, password);
        
        if (result.isSuccess()) {
            User user = result.getUser();
            String role = result.getRole();
            
            // Tạo session mới
            HttpSession session = request.getSession(true);
            
            // Setup session attributes
            setupUserSession(session, user, role);
            
            // Setup session timeout dựa trên remember me
            setupSessionTimeout(session, rememberMe);
            
            // Xử lý cookie cho Remember Me
            handleRememberMeCookies(request, response, username, password, rememberMe);
            
            // Redirect về home sau khi đăng nhập thành công
            response.sendRedirect(request.getContextPath() + "/home");
            
        } else {
            // Đăng nhập thất bại - hiển thị lỗi
            request.setAttribute("loginError", true);
            request.setAttribute("errors", result.getErrors());
            request.setAttribute("username", username);
            
            // Load Google Client ID để hiển thị lại form
            loadGoogleClientId(request);
            
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }

    /**
     * Xử lý cookie cho tính năng Remember Me
     * - Nếu rememberMe được chọn: tạo cookie username và password (30 phút)
     * - Nếu rememberMe không được chọn: xóa cookie nếu có
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param username Username để lưu vào cookie
     * @param password Password để lưu vào cookie
     * @param rememberMe "on" nếu được chọn, null nếu không
     */
    private void handleRememberMeCookies(HttpServletRequest request, HttpServletResponse response, 
                                         String username, String password, String rememberMe) {
        // Lấy context path, nếu rỗng thì dùng "/"
        String contextPath = request.getContextPath();
        String cookiePath = contextPath.isEmpty() ? "/" : contextPath;
        
        if ("on".equals(rememberMe)) {
            // Tạo cookie cho username (30 phút = 30 * 60 giây)
            Cookie usernameCookie = new Cookie("rememberedUsername", username);
            usernameCookie.setMaxAge(30 * 60); // 30 phút
            usernameCookie.setPath(cookiePath);
            response.addCookie(usernameCookie);
            
            // Tạo cookie cho password (30 phút)
            Cookie passwordCookie = new Cookie("rememberedPassword", password);
            passwordCookie.setMaxAge(30 * 60); // 30 phút
            passwordCookie.setPath(cookiePath);
            response.addCookie(passwordCookie);
            
            System.out.println("[LoginServlet] Remember Me cookies created for username: " + username);
        } else {
            // Xóa cookie nếu rememberMe không được chọn
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("rememberedUsername".equals(cookie.getName()) || 
                        "rememberedPassword".equals(cookie.getName())) {
                        cookie.setMaxAge(0); // Xóa cookie
                        cookie.setPath(cookiePath); // Path phải khớp với cookie đã tạo
                        response.addCookie(cookie);
                    }
                }
            }
            System.out.println("[LoginServlet] Remember Me cookies removed");
        }
    }

    /**
     * Đọc cookie Remember Me và set vào request attributes để JSP có thể sử dụng
     * 
     * @param request HttpServletRequest
     */
    private void loadRememberMeCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String rememberedUsername = null;
            String rememberedPassword = null;
            
            for (Cookie cookie : cookies) {
                if ("rememberedUsername".equals(cookie.getName())) {
                    rememberedUsername = cookie.getValue();
                } else if ("rememberedPassword".equals(cookie.getName())) {
                    rememberedPassword = cookie.getValue();
                }
            }
            
            // Set vào request attributes để JSP sử dụng
            if (rememberedUsername != null) {
                request.setAttribute("rememberedUsername", rememberedUsername);
            }
            if (rememberedPassword != null) {
                request.setAttribute("rememberedPassword", rememberedPassword);
            }
            
            // Nếu cả hai cookie đều tồn tại, set flag để check checkbox
            if (rememberedUsername != null && rememberedPassword != null) {
                request.setAttribute("hasRememberMeCookies", true);
            }
        }
    }
}

