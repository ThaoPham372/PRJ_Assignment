package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * ErrorHandlerServlet handles application errors
 * Provides centralized error handling and logging
 */
@WebServlet(name = "ErrorHandlerServlet", urlPatterns = {"/error"})
public class ErrorHandlerServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ErrorHandlerServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleError(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleError(request, response);
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get error information from request attributes
        Throwable throwable = (Throwable) request.getAttribute("jakarta.servlet.error.exception");
        String requestUri = (String) request.getAttribute("jakarta.servlet.error.request_uri");
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        String servletName = (String) request.getAttribute("jakarta.servlet.error.servlet_name");
        
        // Log the error
        String errorMessage = "Error occurred: " + statusCode;
        if (throwable != null) {
            logger.severe(errorMessage + " - " + throwable.getMessage());
            logger.severe("Request URI: " + requestUri);
            logger.severe("Servlet Name: " + servletName);
        } else {
            logger.warning(errorMessage + " - No exception details available");
            logger.warning("Request URI: " + requestUri);
        }
        
        // Set error message for display
        if (statusCode != null) {
            switch (statusCode) {
                case 404:
                    request.setAttribute("error", "Trang không tồn tại hoặc đã bị xóa.");
                    break;
                case 403:
                    request.setAttribute("error", "Bạn không có quyền truy cập vào trang này.");
                    break;
                case 500:
                    request.setAttribute("error", throwable != null ? throwable.getMessage() : "Lỗi hệ thống không xác định.");
                    break;
                default:
                    request.setAttribute("error", "Đã xảy ra lỗi không xác định.");
                    break;
            }
        } else {
            request.setAttribute("error", "Đã xảy ra lỗi không xác định.");
        }
        
        // Forward to appropriate error page
        if (statusCode != null && statusCode == 404) {
            request.getRequestDispatcher("/views/error/404.jsp").forward(request, response);
        } else if (statusCode != null && statusCode == 403) {
            request.getRequestDispatcher("/views/error/403.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/views/error/500.jsp").forward(request, response);
        }
    }
}

