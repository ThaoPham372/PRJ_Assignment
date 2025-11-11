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
import service.MemberService;
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
    "/pt/students/detail",
    "/pt/students/update"
})
public class TrainerStudentServlet extends HttpServlet {

    private TrainerStudentService studentService;
    private PackageService packageService;
    private MemberService memberService;

    private static String toJsonStr(Object v) {
        if (v == null) return "null";
        String s = String.valueOf(v).replace("\\", "\\\\").replace("\"", "\\\"");
        return "\"" + s + "\"";
    }
    private static String toJsonNum(Object v) {
        if (v == null) return "null";
        if (v instanceof Number) return v.toString();
        try { return String.valueOf(new java.math.BigDecimal(String.valueOf(v))); } catch (Exception e) { return "null"; }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        this.studentService = new TrainerStudentService();
        this.packageService = new PackageService();
        this.memberService = new MemberService();
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
                // Trả về JSON thông tin chi tiết học viên
                String memberIdParam = req.getParameter("id");
                resp.setContentType("application/json;charset=UTF-8");
                if (memberIdParam != null) {
                    try {
                        Integer memberId = Integer.parseInt(memberIdParam);
                        Object[] d = studentService.getStudentDetail(memberId, trainerId);
                        if (d == null) {
                            // Fallback: lấy trực tiếp Member để vẫn hiển thị thông tin cơ bản
                            var m = memberService.getById(Integer.parseInt(memberIdParam));
                            if (m != null) {
                                String json = String.format(
                                    "{\"success\":true," +
                                    "\"memberId\":%s," +
                                    "\"name\":%s," +
                                    "\"phone\":%s," +
                                    "\"email\":%s," +
                                    "\"gender\":%s," +
                                    "\"dob\":%s," +
                                    "\"address\":%s," +
                                    "\"weight\":%s," +
                                    "\"height\":%s," +
                                    "\"bmi\":%s," +
                                    "\"goal\":%s," +
                                    "\"ptNote\":%s}",
                                    toJsonNum(m.getId()),
                                    toJsonStr(m.getName()),
                                    toJsonStr(m.getPhone()),
                                    toJsonStr(m.getEmail()),
                                    toJsonStr(m.getGender()),
                                    toJsonStr(m.getDob()),
                                    toJsonStr(m.getAddress()),
                                    toJsonNum(m.getWeight()),
                                    toJsonNum(m.getHeight()),
                                    toJsonNum(m.getBmi()),
                                    toJsonStr(m.getGoal()),
                                    toJsonStr(m.getPtNote())
                                );
                                resp.getWriter().write(json);
                                return;
                            } else {
                                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                                resp.getWriter().write("{\"success\":false,\"message\":\"Không tìm thấy học viên\"}");
                                return;
                            }
                        }
                        // Map indices theo TrainerStudentDAO.getStudentDetail
                        String json = String.format(
                            "{\"success\":true," +
                            "\"memberId\":%s," +
                            "\"name\":%s," +
                            "\"phone\":%s," +
                            "\"email\":%s," +
                            "\"gender\":%s," +
                            "\"dob\":%s," +
                            "\"address\":%s," +
                            "\"weight\":%s," +
                            "\"height\":%s," +
                            "\"bmi\":%s," +
                            "\"goal\":%s," +
                            "\"ptNote\":%s}",
                            toJsonNum(d[0]), toJsonStr(d[1]), toJsonStr(d[2]), toJsonStr(d[3]), toJsonStr(d[4]),
                            toJsonStr(d[5]), toJsonStr(d[6]), toJsonNum(d[7]), toJsonNum(d[8]), toJsonNum(d[9]),
                            toJsonStr(d[10]), toJsonStr(d[11])
                        );
                        resp.getWriter().write(json);
                        return;
                    } catch (NumberFormatException e) {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        resp.getWriter().write("{\"success\":false,\"message\":\"Member ID không hợp lệ\"}");
                        return;
                    }
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"success\":false,\"message\":\"Thiếu tham số id\"}");
                    return;
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/pt/students/update".equals(path)) {
            handleUpdateStudent(req, resp);
            return;
        }
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void handleUpdateStudent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        try {
            int memberId = Integer.parseInt(req.getParameter("memberId"));
            String weightStr = req.getParameter("weight");
            String heightStr = req.getParameter("height");
            String bmiStr = req.getParameter("bmi");
            String goal = req.getParameter("goal");
            String ptNote = req.getParameter("ptNote");

            var member = memberService.getById(memberId);
            if (member == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"success\":false,\"message\":\"Member không tồn tại\"}");
                return;
            }

            if (weightStr != null && !weightStr.isBlank()) {
                try { member.setWeight(Float.parseFloat(weightStr)); } catch (NumberFormatException ignored) {}
            }
            if (heightStr != null && !heightStr.isBlank()) {
                try { member.setHeight(Float.parseFloat(heightStr)); } catch (NumberFormatException ignored) {}
            }
            if (bmiStr != null && !bmiStr.isBlank()) {
                try { member.setBmi(Float.parseFloat(bmiStr)); } catch (NumberFormatException ignored) {}
            }
            if (goal != null) {
                member.setGoal(goal.trim());
            }
            if (ptNote != null) {
                member.setPtNote(ptNote.trim());
            }

            memberService.update(member);

            resp.getWriter().write("{\"success\":true}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"success\":false,\"message\":\"" + e.getMessage().replace("\"","\\\"") + "\"}");
        }
    }
}

