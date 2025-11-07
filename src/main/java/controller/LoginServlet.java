package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import service.LoginService;
import service.LoginService.LoginResult;

public class LoginServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/views/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");

        LoginService loginService = new LoginService();
        LoginResult result = loginService.authenticate(username, password);

        System.out.println("result: " + result.isSuccess());
        System.out.println("pass: " + password);
        System.out.println("passhash: " + result.getUser().getPassword());

        if (result.isSuccess()) {
            HttpSession session = request.getSession(true);

            session.setAttribute("user", result.getUser());
            session.setAttribute("userId", result.getUser().getId());
            session.setAttribute("userRoles", result.getRole());
            session.setAttribute("isLoggedIn", true);

            if ("on".equals(rememberMe)) {
                session.setMaxInactiveInterval(7 * 24 * 60 * 60); // 7 days
            } else {
                session.setMaxInactiveInterval(3000 * 60); // 30 minutes cần sửa lại 3000 -> 30
            }

            String redirectUrl = determineRedirectUrl(result.getRole());
            response.sendRedirect(request.getContextPath() + redirectUrl);

        } else {
            request.setAttribute("loginError", true);
            request.setAttribute("errors", result.getErrors());
            request.setAttribute("username", username); // Preserve username

            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }

    private String determineRedirectUrl(String roles) {
        if (roles == null || roles.isEmpty()) {
            return "/home"; // Default redirect
        }

        if (roles.equalsIgnoreCase("ADMIN")) {
            return "/admin/dashboard";
        }

        if (roles.equalsIgnoreCase("PT")) {
            return "/pt/dashboard";
        }

        return "/home";
    }
}
