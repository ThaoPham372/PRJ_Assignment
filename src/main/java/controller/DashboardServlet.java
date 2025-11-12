package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Member;
import model.Membership;
import model.Package;
import model.DailyIntakeDTO;
import dao.PaymentDAO;
import service.MembershipService;
import service.nutrition.NutritionService;
import service.nutrition.NutritionServiceImpl;

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
 * - Hiển thị BMI category, calories hôm nay, tổng tiền đã chi, gói đang sở hữu
 * - Forward các trang schedule và support
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {
    "/member/dashboard",
    "/member/schedule",
    "/member/support"
})
public class DashboardServlet extends BaseMemberServlet {

    private final PaymentDAO paymentDAO;
    private final MembershipService membershipService;
    private final NutritionService nutritionService;

    public DashboardServlet() {
        this.paymentDAO = new PaymentDAO();
        this.membershipService = new MembershipService();
        this.nutritionService = new NutritionServiceImpl();
    }

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
        try {
            // 1. Tính và set BMI category nếu có BMI
            if (member.getBmi() != null) {
                request.setAttribute("bmiCategory", calculateBMICategory(member.getBmi()));
            }
            
            // 2. Lấy tổng calories đã nạp trong ngày từ NutritionService
            DailyIntakeDTO todayTotals = nutritionService.todayTotals(member.getId().intValue());
            BigDecimal todayCalories = todayTotals != null && todayTotals.getCaloriesKcal() != null 
                    ? todayTotals.getCaloriesKcal() 
                    : BigDecimal.ZERO;
            request.setAttribute("todayCalories", todayCalories);
            
            // 3. Lấy tổng số tiền đã chi tiêu (tất cả payment đã PAID của user)
            BigDecimal totalSpent = paymentDAO.getTotalSpentByMember(member.getId());
            request.setAttribute("totalSpent", totalSpent);
            
            // 4. Lấy gói đang sở hữu (active membership)
            List<Membership> activeMemberships = membershipService.getActiveMembershipsByMemberId(member.getId());
            String currentPackageName = "Chưa có gói";
            if (activeMemberships != null && !activeMemberships.isEmpty()) {
                // Lấy gói đầu tiên (hoặc có thể hiển thị tất cả)
                Membership firstMembership = activeMemberships.get(0);
                Package packageO = firstMembership.getPackageO();
                if (packageO != null && packageO.getName() != null) {
                    currentPackageName = packageO.getName();
                    // Nếu có nhiều gói, có thể hiển thị thêm
                    if (activeMemberships.size() > 1) {
                        currentPackageName += " (+" + (activeMemberships.size() - 1) + " gói khác)";
                    }
                }
            }
            request.setAttribute("currentPackageName", currentPackageName);
            
        } catch (Exception e) {
            System.err.println("[DashboardServlet] Error loading dashboard data: " + e.getMessage());
            e.printStackTrace();
            // Set default values on error
            request.setAttribute("todayCalories", BigDecimal.ZERO);
            request.setAttribute("totalSpent", BigDecimal.ZERO);
            request.setAttribute("currentPackageName", "Chưa có gói");
        }
        
        // Forward đến JSP dashboard
        request.getRequestDispatcher("/views/member/dashboard.jsp").forward(request, response);
    }
}

