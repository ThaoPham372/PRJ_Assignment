package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

/**
 * LogoutServlet - Controller xử lý đăng xuất
 * 
 * Tuân thủ mô hình MVC và nguyên tắc Single Responsibility
 * 
 * Endpoints:
 * GET  /logout  - Xử lý đăng xuất (GET và POST đều được hỗ trợ)
 * POST /logout  - Xử lý đăng xuất
 * 
 * Chức năng:
 * - Invalidate session hiện tại
 * - Xóa tất cả session attributes
 * - Redirect về trang home
 * - Log thông tin logout để tracking
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {
    "/logout"
})
public class LogoutServlet extends BaseAuthServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("[LogoutServlet] Initialized successfully");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleLogout(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleLogout(request, response);
    }

    /**
     * Xử lý đăng xuất
     * Invalidate session và redirect về home
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("[LogoutServlet] Logout request received");
        
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            // Lấy thông tin user trước khi invalidate (để logging)
            String username = "Unknown";
            User user = (User) session.getAttribute("user");
            if (user != null) {
                username = user.getUsername();
            }
            
            // Invalidate session - xóa tất cả attributes và dừng session
            session.invalidate();
            System.out.println("[LogoutServlet] Session invalidated successfully for user: " + username);
        } else {
            System.out.println("[LogoutServlet] No active session found");
        }
        
        // Redirect về trang home
        response.sendRedirect(request.getContextPath() + "/home");
    }
}

