package controller;

import DAO.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * LoginServlet handles user authentication
 * Processes login requests and manages user sessions
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if ("admin".equals(user.getRole()) || "manager".equals(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else if ("employee".equals(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/employee/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/member/dashboard");
            }
            return;
        }

        // Forward to login page
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");

        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin đăng nhập.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }

        try {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.authenticateUser(username, password);

            if (user != null) {
                // Create session
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("role", user.getRole());

                // Handle remember me
                if ("on".equals(rememberMe)) {
                    Cookie usernameCookie = new Cookie("rememberedUsername", username);
                    Cookie passwordCookie = new Cookie("rememberedPassword", password);
                    usernameCookie.setMaxAge(30 * 24 * 60 * 60); // 30 days
                    passwordCookie.setMaxAge(30 * 24 * 60 * 60); // 30 days
                    response.addCookie(usernameCookie);
                    response.addCookie(passwordCookie);
                } else {
                    // Clear remember me cookies
                    Cookie usernameCookie = new Cookie("rememberedUsername", "");
                    Cookie passwordCookie = new Cookie("rememberedPassword", "");
                    usernameCookie.setMaxAge(0);
                    passwordCookie.setMaxAge(0);
                    response.addCookie(usernameCookie);
                    response.addCookie(passwordCookie);
                }

                logger.info("User " + username + " logged in successfully as " + user.getRole());

                // Redirect based on role
                if ("admin".equals(user.getRole()) || "manager".equals(user.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                } else if ("employee".equals(user.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/employee/dashboard");
                } else {
                    response.sendRedirect(request.getContextPath() + "/member/dashboard");
                }
            } else {
                request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
                request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error during login", e);
            request.setAttribute("error", "Lỗi hệ thống. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error during login", e);
            request.setAttribute("error", "Đã xảy ra lỗi. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }
}

