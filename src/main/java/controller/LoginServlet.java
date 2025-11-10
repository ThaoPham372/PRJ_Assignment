package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
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
}

