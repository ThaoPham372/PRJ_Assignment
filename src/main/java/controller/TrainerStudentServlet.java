package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Trainer;
import service.TrainerStudentService;
import service.PackageService;
import java.io.IOException;
import java.util.List;

/**
 * TrainerStudentServlet
 * Handles requests for trainer student management
 * Routes:
 * - GET /pt/students - Display list of students
 * - GET /pt/students/search - Search and filter students
 * - GET /pt/students/detail - Get student detail
 */
@WebServlet(urlPatterns = {
    "/pt/students",
    "/pt/students/search",
    "/pt/students/detail"
})
public class TrainerStudentServlet extends HttpServlet {

    private TrainerStudentService studentService;
    private PackageService packageService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.studentService = new TrainerStudentService();
        this.packageService = new PackageService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        Trainer trainer = (Trainer) session.getAttribute("user");

        if (trainer == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int trainerId = trainer.getId();
        String path = req.getServletPath();

        try {
            if ("/pt/students/detail".equals(path)) {
                // Get student detail
                String memberIdParam = req.getParameter("id");
                if (memberIdParam != null) {
                    try {
                        Integer memberId = Integer.parseInt(memberIdParam);
                        Object[] studentDetail = studentService.getStudentDetail(memberId, trainerId);
                        req.setAttribute("studentDetail", studentDetail);
                        // Forward to detail page or return JSON
                        resp.setContentType("application/json");
                        resp.getWriter().write("{\"success\": true}");
                        return;
                    } catch (NumberFormatException e) {
                        req.setAttribute("error", "Invalid member ID");
                    }
                }
            } else {
                // Get search and filter parameters
                String keyword = req.getParameter("keyword");
                String packageFilter = req.getParameter("package");

                // Get students with or without filter
                List<Object[]> students;
                if ((keyword != null && !keyword.trim().isEmpty()) || 
                    (packageFilter != null && !packageFilter.trim().isEmpty())) {
                    students = studentService.getTrainerStudentsWithFilter(
                        trainerId, 
                        keyword != null ? keyword.trim() : null,
                        packageFilter != null && !packageFilter.trim().isEmpty() ? packageFilter : null
                    );
                } else {
                    students = studentService.getTrainerStudents(trainerId);
                }

                // Get statistics
                Object[] stats = studentService.getStudentStatistics(trainerId);
                Long totalStudents = stats != null && stats[0] != null ? ((Number) stats[0]).longValue() : 0L;
                Long activeStudents = stats != null && stats[1] != null ? ((Number) stats[1]).longValue() : 0L;
                Long achievedGoalCount = stats != null && stats[2] != null ? ((Number) stats[2]).longValue() : 0L;

                // Get packages for filter dropdown
                List<model.Package> packages = packageService.getAll();

                // Set attributes
                req.setAttribute("students", students);
                req.setAttribute("totalStudents", totalStudents);
                req.setAttribute("activeStudents", activeStudents);
                req.setAttribute("achievedGoalCount", achievedGoalCount);
                req.setAttribute("searchTerm", keyword);
                req.setAttribute("packageFilter", packageFilter);
                req.setAttribute("packages", packages);

                // Forward to JSP
                req.getRequestDispatcher("/views/PT/student_management.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
            req.getRequestDispatcher("/views/PT/student_management.jsp").forward(req, resp);
        }
    }

}

