package controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Member;
import model.Membership;
import model.Package;
import service.MemberShipService;
import service.MemberShipService.ValidationResult;
import service.PackageService;

/**
 * MembershipServlet - Controller xử lý quản lý membership
 * 
 * Tuân thủ mô hình MVC và nguyên tắc Single Responsibility
 * 
 * Endpoints xử lý:
 * GET  /member/membership  - Hiển thị trang quản lý membership
 * POST /member/membership  - Xử lý đặt gói membership mới
 * 
 * Chức năng:
 * - Hiển thị tất cả gói thành viên của member (ACTIVE và INACTIVE)
 * - Hiển thị danh sách các gói tập (packages) có sẵn
 * - Đặt gói membership mới với validation
 * - Tự động cộng thời gian nếu đặt gói cùng loại
 * - Validation đầy đủ trước khi đặt gói
 */
@WebServlet(name = "MembershipServlet", urlPatterns = {
    "/member/membership"
})
public class MembershipServlet extends BaseMemberServlet {

    private MemberShipService membershipService;
    private PackageService packageService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.membershipService = new MemberShipService();
        this.packageService = new PackageService();
        System.out.println("[MembershipServlet] Initialized successfully");
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
                case "/member/membership":
                    showMembership(request, response, currentMember);
                    break;

                default:
                    response.sendRedirect(request.getContextPath() + "/member/dashboard");
                    break;
            }
        } catch (Exception e) {
            handleError(request, response, e, "Có lỗi xảy ra. Vui lòng thử lại.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
                case "/member/membership":
                    handlePurchaseMembership(request, response, currentMember);
                    break;

                default:
                    response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    break;
            }
        } catch (Exception e) {
            handleError(request, response, e, "Có lỗi xảy ra khi xử lý yêu cầu. Vui lòng thử lại.");
        }
    }

    // ==================== MEMBERSHIP HANDLERS ====================

    /**
     * Hiển thị trang Membership - Quản lý gói tập
     * Hiển thị TẤT CẢ gói thành viên của member (không chỉ ACTIVE)
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param member Member hiện tại
     * @throws ServletException
     * @throws IOException
     */
    private void showMembership(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        try {
            // Lấy TẤT CẢ memberships của member (ACTIVE và INACTIVE)
            List<Membership> allMemberships = membershipService.getMembershipsByMemberId(member.getId());
            
            // Lấy danh sách ACTIVE memberships để highlight
            List<Membership> activeMemberships = membershipService.getActiveMembershipsByMemberId(member.getId());
            
            // Lấy tất cả packages có sẵn (chỉ active packages)
            List<Package> allPackages = packageService.getAll();
            List<Package> activePackages = allPackages.stream()
                .filter(pkg -> pkg.getIsActive() != null && pkg.getIsActive())
                .collect(Collectors.toList());
            
            // Set attributes
            request.setAttribute("allMemberships", allMemberships); // Tất cả gói (để hiển thị lịch sử)
            request.setAttribute("activeMemberships", activeMemberships); // Chỉ gói ACTIVE
            request.setAttribute("packages", activePackages);
            
            // Set danh sách package IDs đang ACTIVE để highlight
            List<Integer> activePackageIds = activeMemberships.stream()
                .filter(m -> m.getPackageO() != null)
                .map(m -> m.getPackageO().getId())
                .collect(Collectors.toList());
            request.setAttribute("activePackageIds", activePackageIds);
            
            System.out.println("[MembershipServlet] Loading membership page for member: " + member.getId());
            System.out.println("[MembershipServlet] Total memberships: " + allMemberships.size());
            System.out.println("[MembershipServlet] Active memberships: " + activeMemberships.size());
            System.out.println("[MembershipServlet] Available packages: " + activePackages.size());
            
            request.getRequestDispatcher("/views/member/membership.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            handleError(request, response, e, "Có lỗi khi tải thông tin membership.");
        }
    }

    /**
     * Xử lý đặt gói membership mới
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param member Member hiện tại
     * @throws ServletException
     * @throws IOException
     */
    private void handlePurchaseMembership(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        try {
            // Lấy packageId từ request
            String packageIdStr = request.getParameter("packageId");
            if (packageIdStr == null || packageIdStr.trim().isEmpty()) {
                session.setAttribute("error", "Vui lòng chọn gói thành viên");
                response.sendRedirect(request.getContextPath() + "/member/membership");
                return;
            }

            int packageId;
            try {
                packageId = Integer.parseInt(packageIdStr);
            } catch (NumberFormatException e) {
                session.setAttribute("error", "Gói thành viên không hợp lệ");
                response.sendRedirect(request.getContextPath() + "/member/membership");
                return;
            }

            // Lấy package từ database
            Package packageToAdd = packageService.getById(packageId);
            if (packageToAdd == null) {
                session.setAttribute("error", "Không tìm thấy gói thành viên");
                response.sendRedirect(request.getContextPath() + "/member/membership");
                return;
            }

            // Validate trước khi đặt
            ValidationResult validation = membershipService.validateNewMembership(member.getId(), packageToAdd);
            if (!validation.isValid()) {
                String errorMessage = String.join(", ", validation.getErrors());
                session.setAttribute("error", errorMessage);
                response.sendRedirect(request.getContextPath() + "/member/membership");
                return;
            }

            // Kiểm tra xem đã có gói cùng loại chưa
            Membership existingMembership = membershipService.getActiveMembershipByMemberIdAndPackageId(
                member.getId(), packageId);

            // Tạo hoặc cộng thời gian vào membership
            Membership resultMembership;
            String successMessage;
            
            if (existingMembership != null) {
                // Cộng thời gian vào gói hiện có
                resultMembership = membershipService.createOrExtendMembership(member.getId(), packageToAdd);
                successMessage = "Đã cộng thêm " + packageToAdd.getDurationMonths() + 
                               " tháng vào gói " + packageToAdd.getName() + " của bạn!";
            } else {
                // Tạo gói mới
                resultMembership = membershipService.createOrExtendMembership(member.getId(), packageToAdd);
                successMessage = "Đặt gói " + packageToAdd.getName() + " thành công!";
            }

            // Set success message
            session.setAttribute("success", successMessage);
            
            System.out.println("[MembershipServlet] Membership created/extended successfully. ID: " + 
                             resultMembership.getId());

            // Redirect về trang membership để xem kết quả
            response.sendRedirect(request.getContextPath() + "/member/membership");
            
        } catch (IllegalArgumentException e) {
            // Validation error từ service
            session.setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/member/membership");
        } catch (Exception e) {
            System.err.println("[MembershipServlet] Error purchasing membership: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("error", "Có lỗi xảy ra khi đặt gói. Vui lòng thử lại sau.");
            response.sendRedirect(request.getContextPath() + "/member/membership");
        }
    }
}
