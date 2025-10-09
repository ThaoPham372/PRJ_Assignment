package controller;

import model.Coach;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * CoachManagementServlet handles coach/trainer management operations
 * CRUD operations for gym coaches
 */
@WebServlet(name = "CoachManagementServlet", urlPatterns = {"/admin/coaches"})
public class CoachManagementServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(CoachManagementServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("role");
        if (!"admin".equals(role) && !"manager".equals(role) && !"employee".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/views/error/403.jsp");
            return;
        }

        try {
            // Mock data for coaches (in real app, this would come from database)
            List<Coach> coaches = getMockCoaches();
            
            // Set statistics
            request.setAttribute("totalCoaches", coaches.size());
            request.setAttribute("activeCoaches", coaches.stream().mapToInt(c -> "active".equals(c.getStatus()) ? 1 : 0).sum());
            request.setAttribute("ptSessions", "145");
            request.setAttribute("avgRating", "4.8");

            // Set request attributes
            request.setAttribute("coaches", coaches);

            // Forward to coaches page
            request.getRequestDispatcher("/views/admin/coaches.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.severe("Error in coach management: " + e.getMessage());
            request.setAttribute("error", "Đã xảy ra lỗi. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/views/error/500.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("role");
        if (!"admin".equals(role) && !"manager".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/views/error/403.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/admin/coaches");
            return;
        }

        try {
            switch (action) {
                case "add":
                    handleAddCoach(request, response);
                    break;
                case "update":
                    handleUpdateCoach(request, response);
                    break;
                case "delete":
                    handleDeleteCoach(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/admin/coaches");
            }

        } catch (Exception e) {
            logger.severe("Error in coach management POST: " + e.getMessage());
            request.setAttribute("error", "Đã xảy ra lỗi. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/views/error/500.jsp").forward(request, response);
        }
    }

    private void handleAddCoach(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String specialization = request.getParameter("specialization");
        String experienceYearsStr = request.getParameter("experienceYears");
        String salaryStr = request.getParameter("salary");
        String certifications = request.getParameter("certifications");
        String description = request.getParameter("description");

        // Validation
        if (fullName == null || fullName.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            phone == null || phone.trim().isEmpty() ||
            specialization == null || specialization.trim().isEmpty()) {
            
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin bắt buộc.");
            doGet(request, response);
            return;
        }

        // Parse numeric values
        int experienceYears = 0;
        double salary = 0.0;
        
        try {
            if (experienceYearsStr != null && !experienceYearsStr.trim().isEmpty()) {
                experienceYears = Integer.parseInt(experienceYearsStr);
            }
            if (salaryStr != null && !salaryStr.trim().isEmpty()) {
                salary = Double.parseDouble(salaryStr);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Số năm kinh nghiệm và mức lương phải là số hợp lệ.");
            doGet(request, response);
            return;
        }

        // In real app, save to database
        logger.info("New coach added by admin: " + fullName);
        
        // Redirect with success message
        response.sendRedirect(request.getContextPath() + "/admin/coaches?success=added");
    }

    private void handleUpdateCoach(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/coaches");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            
            // In real app, update coach in database
            logger.info("Coach updated by admin: ID " + id);
            
            response.sendRedirect(request.getContextPath() + "/admin/coaches?success=updated");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/coaches");
        }
    }

    private void handleDeleteCoach(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/coaches");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            
            // In real app, delete coach from database
            logger.info("Coach deleted by admin: ID " + id);
            
            response.sendRedirect(request.getContextPath() + "/admin/coaches?success=deleted");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/coaches");
        }
    }

    private List<Coach> getMockCoaches() {
        List<Coach> coaches = new ArrayList<>();
        
        // Mock coach data
        String[] names = {
            "Nguyễn Văn Nam", "Trần Thị Lan", "Lê Hoàng Minh", "Phạm Thu Hà",
            "Vũ Đình Khoa", "Hoàng Thị Mai", "Đặng Văn Tùng", "Bùi Thị Nga"
        };
        
        String[] specializations = {
            "Chuyên gia Bodybuilding", "Yoga & Pilates Instructor", "Strength & Conditioning",
            "Nutritionist & Fitness", "Cardio & Weight Loss", "Functional Training",
            "CrossFit & HIIT", "Rehabilitation & Recovery"
        };
        
        String[] emails = {
            "nam.nguyen@gym.com", "lan.tran@gym.com", "minh.le@gym.com", "ha.pham@gym.com",
            "khoa.vu@gym.com", "mai.hoang@gym.com", "tung.dang@gym.com", "nga.bui@gym.com"
        };

        for (int i = 0; i < names.length; i++) {
            Coach coach = new Coach();
            coach.setId(i + 1);
            coach.setFullName(names[i]);
            coach.setEmail(emails[i]);
            coach.setPhone("090" + String.format("%08d", 1000000 + i * 100000));
            coach.setSpecialization(specializations[i]);
            coach.setExperienceYears(2 + (i % 5));
            coach.setSalary(15000000 + (i * 2000000));
            coach.setRating(4 + (i % 2));
            coach.setTotalClients(15 + (i * 3));
            coach.setSessionsPerWeek(8 + (i % 15));
            coach.setStatus(i % 4 != 0 ? "active" : "busy");
            coach.setCertifications("Certification " + (i + 1));
            coach.setDescription("Experienced trainer with " + coach.getExperienceYears() + " years of experience.");
            
            coaches.add(coach);
        }
        
        return coaches;
    }
}
