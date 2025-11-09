package controller;

import service.AdvisoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * AdvisoryServlet - Handles advisory requests
 * Follows MVC pattern: Controller → Service → DAO
 * GET: Display advisory form
 * POST: Process form submission and save to database
 */
@WebServlet(name = "AdvisoryServlet", urlPatterns = { "/advisory" })
public class AdvisoryServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AdvisoryServlet.class.getName());
    private AdvisoryService advisoryService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.advisoryService = new AdvisoryService();
    }

    /**
     * GET: Display advisory form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/advisory/advisory_form.jsp").forward(request, response);
    }

    /**
     * POST: Process form submission
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Get form data
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String address = request.getParameter("address");

        // Keep form data for re-display if error
        request.setAttribute("fullName", fullName);
        request.setAttribute("phone", phone);
        request.setAttribute("email", email);
        request.setAttribute("address", address);

        try {
            // Use Service to create request (Service handles validation)
            advisoryService.createRequest(fullName, phone, email, address);

            // Success: show success message and clear form
            request.setAttribute("successMessage",
                    "Cảm ơn bạn đã gửi yêu cầu tư vấn! Chúng tôi sẽ liên hệ với bạn sớm nhất có thể.");

            // Clear form data
            request.removeAttribute("fullName");
            request.removeAttribute("phone");
            request.removeAttribute("email");
            request.removeAttribute("address");

        } catch (IllegalArgumentException e) {
            // Validation error: show error message
            request.setAttribute("errorMessage", e.getMessage());
            LOGGER.warning("Validation error: " + e.getMessage());

        } catch (Exception e) {
            // System error: show generic error message
            request.setAttribute("errorMessage",
                    "Đã có lỗi xảy ra khi gửi yêu cầu. Vui lòng thử lại sau!");
            LOGGER.severe("Error processing advisory request: " + e.getMessage());
            e.printStackTrace();
        }

        // Forward back to form
        request.getRequestDispatcher("/views/advisory/advisory_form.jsp").forward(request, response);
    }
}
