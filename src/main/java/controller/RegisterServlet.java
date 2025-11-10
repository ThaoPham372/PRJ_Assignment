package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.RegistrationService;
import service.RegistrationService.RegisterRequest;
import service.RegistrationService.RegisterResult;

/**
 * RegisterServlet - Controller xử lý đăng ký tài khoản
 * 
 * Tuân thủ mô hình MVC và nguyên tắc Single Responsibility
 * 
 * Endpoints:
 * GET  /register       - Hiển thị trang đăng ký
 * GET  /auth/register   - Hiển thị trang đăng ký
 * POST /register       - Xử lý đăng ký
 * POST /auth/register   - Xử lý đăng ký
 * 
 * Chức năng:
 * - Hiển thị form đăng ký
 * - Validate thông tin đăng ký
 * - Tạo tài khoản mới
 * - Load Google Client ID cho Google OAuth
 * - Lấy client IP và User-Agent để tracking
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {
    "/register",
    "/auth/register"
})
public class RegisterServlet extends BaseAuthServlet {

    private RegistrationService registrationService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.registrationService = new RegistrationService();
        System.out.println("[RegisterServlet] Initialized successfully");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Load Google Client ID cho Google OAuth button
        loadGoogleClientId(request);
        
        // Forward đến trang register
        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRegister(request, response);
    }

    /**
     * Xử lý đăng ký tài khoản
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy thông tin từ form
        String username = request.getParameter("username");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Tạo RegisterRequest
        RegisterRequest registerRequest = new RegisterRequest(username, email, password, confirmPassword);
        
        // Set thông tin tracking (tái sử dụng method từ BaseAuthServlet)
        registerRequest.setIpAddress(getClientIpAddress(request));
        registerRequest.setUserAgent(request.getHeader("User-Agent"));

        // Thực hiện đăng ký
        RegisterResult result = registrationService.register(registerRequest);
        
        if (result.isSuccess()) {
            // Đăng ký thành công
            request.setAttribute("registerSuccess", true);
            request.setAttribute("successMessage", result.getMessage());
            
            // Clear form data
            request.setAttribute("username", "");
            request.setAttribute("name", "");
            request.setAttribute("email", "");
        } else {
            // Đăng ký thất bại - hiển thị lỗi
            request.setAttribute("registerSuccess", false);
            request.setAttribute("errors", result.getErrors());
            
            // Giữ lại giá trị form để user không phải nhập lại
            request.setAttribute("username", username);
            request.setAttribute("name", name);
            request.setAttribute("email", email);
        }

        // Load Google Client ID để hiển thị lại form
        loadGoogleClientId(request);
        
        // Forward đến trang register với kết quả
        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }
}

