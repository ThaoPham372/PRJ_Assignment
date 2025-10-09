package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * LogoutServlet handles user logout
 * Clears session and redirects to home page
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(LogoutServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processLogout(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processLogout(request, response);
    }

    private void processLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            String username = (String) session.getAttribute("username");
            logger.info("User " + username + " logged out");
            
            // Invalidate session
            session.invalidate();
        }

        // Redirect to home page
        response.sendRedirect(request.getContextPath() + "/home");
    }
}

