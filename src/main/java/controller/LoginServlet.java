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
        
        // Đọc cookies để pre-fill form (nếu có)
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
            
            // Đọc cookies để pre-fill form (nếu có)
            loadRememberMeCookies(request);
            
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }

    /**
     * Xử lý cookie cho Remember Me
     * Nếu rememberMe = "on", tạo cookie với thời gian sống 30 phút
     * Nếu rememberMe = null, xóa cookie (nếu có)
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param username Username
     * @param password Password
     * @param rememberMe "on" nếu được chọn, null nếu không
     */
    private void handleRememberMeCookies(HttpServletRequest request, HttpServletResponse response, 
                                         String username, String password, String rememberMe) {
        // Cookie names
        String USERNAME_COOKIE = "rememberedUsername";
        String PASSWORD_COOKIE = "rememberedPassword";
        
        // Set cookie path - use "/" for entire application
        String cookiePath = "/";
        String contextPath = request.getContextPath();
        if (contextPath != null && !contextPath.isEmpty() && !contextPath.equals("/")) {
            cookiePath = contextPath;
        }
        
        if ("on".equals(rememberMe)) {
            // Tạo cookie cho username (30 phút = 30 * 60 giây)
            Cookie usernameCookie = new Cookie(USERNAME_COOKIE, username);
            usernameCookie.setMaxAge(30 * 60); // 30 phút
            usernameCookie.setPath(cookiePath);
            response.addCookie(usernameCookie);
            
            // Tạo cookie cho password (30 phút)
            Cookie passwordCookie = new Cookie(PASSWORD_COOKIE, password);
            passwordCookie.setMaxAge(30 * 60); // 30 phút
            passwordCookie.setPath(cookiePath);
            response.addCookie(passwordCookie);
            
            System.out.println("[LoginServlet] Remember me cookies created for user: " + username);
        } else {
            // Xóa cookie nếu không chọn remember me
            Cookie usernameCookie = new Cookie(USERNAME_COOKIE, "");
            usernameCookie.setMaxAge(0);
            usernameCookie.setPath(cookiePath);
            response.addCookie(usernameCookie);
            
            Cookie passwordCookie = new Cookie(PASSWORD_COOKIE, "");
            passwordCookie.setMaxAge(0);
            passwordCookie.setPath(cookiePath);
            response.addCookie(passwordCookie);
            
            System.out.println("[LoginServlet] Remember me cookies deleted");
        }
    }

    /**
     * Đọc cookies và set vào request attributes để JSP có thể sử dụng
     * 
     * @param request HttpServletRequest
     */
    private void loadRememberMeCookies(HttpServletRequest request) {
        String USERNAME_COOKIE = "rememberedUsername";
        String PASSWORD_COOKIE = "rememberedPassword";
        
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (USERNAME_COOKIE.equals(cookie.getName())) {
                    String rememberedUsername = cookie.getValue();
                    if (rememberedUsername != null && !rememberedUsername.trim().isEmpty()) {
                        request.setAttribute("rememberedUsername", rememberedUsername);
                        request.setAttribute("rememberMeChecked", true);
                    }
                } else if (PASSWORD_COOKIE.equals(cookie.getName())) {
                    String rememberedPassword = cookie.getValue();
                    if (rememberedPassword != null && !rememberedPassword.trim().isEmpty()) {
                        request.setAttribute("rememberedPassword", rememberedPassword);
                    }
                }
            }
        }
    }
}

