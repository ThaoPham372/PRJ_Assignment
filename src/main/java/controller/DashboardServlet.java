package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Member;

/**
 * DashboardServlet - Controller xử lý Dashboard và các trang liên quan
 * 
 * Tuân thủ mô hình MVC và nguyên tắc Single Responsibility
 * 
 * Endpoints xử lý:
 * GET  /member/dashboard  - Hiển thị trang dashboard tổng quan
 * GET  /member/schedule   - Hiển thị trang lịch tập (forward đơn giản)
 * GET  /member/support    - Hiển thị trang hỗ trợ (forward đơn giản)
 * 
 * Chức năng:
 * - Load và hiển thị thông tin tổng quan của member
 * - Hiển thị BMI category
 * - Forward các trang schedule và support
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {
    "/member/dashboard",
    "/member/schedule",
    "/member/support"
})
public class DashboardServlet extends BaseMemberServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("[DashboardServlet] Initialized successfully");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Kiểm tra authentication
        Member currentMember = requireAuthentication(request, response);
        if (currentMember == null) {
            return; // Đã redirect trong requireAuthentication
        }

        // Routing dựa trên path
        String path = request.getServletPath();
        
        try {
            switch (path) {
                case "/member/dashboard":
                    showDashboard(request, response, currentMember);
                    break;

                case "/member/schedule":
                case "/member/support":
                    // Forward đơn giản đến JSP tương ứng
                    request.getRequestDispatcher("/views" + path + ".jsp").forward(request, response);
                    break;

                default:
                    response.sendRedirect(request.getContextPath() + "/member/dashboard");
                    break;
            }
        } catch (Exception e) {
            handleError(request, response, e, "Có lỗi xảy ra. Vui lòng thử lại.");
        }
    }

    /**
     * Hiển thị Dashboard - Trang tổng quan
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param member Member hiện tại
     * @throws ServletException
     * @throws IOException
     */
    private void showDashboard(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        // Tính và set BMI category nếu có BMI
        if (member.getBmi() != null) {
            request.setAttribute("bmiCategory", calculateBMICategory(member.getBmi()));
        }
        
        // Forward đến JSP dashboard
        request.getRequestDispatcher("/views/member/dashboard.jsp").forward(request, response);
    }
}

