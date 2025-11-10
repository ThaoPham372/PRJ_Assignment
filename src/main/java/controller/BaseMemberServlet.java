package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Member;
import model.User;
import service.MemberService;

/**
 * BaseMemberServlet - Lớp cơ sở chung cho các servlet xử lý member
 * 
 * Cung cấp các phương thức chung:
 * - Authentication và session management
 * - Lấy member hiện tại từ session
 * - Xử lý lỗi chung
 * 
 * Tuân thủ nguyên tắc DRY (Don't Repeat Yourself) và OOP
 */
public abstract class BaseMemberServlet extends HttpServlet {

    protected MemberService memberService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.memberService = new MemberService();
    }

    /**
     * Kiểm tra user đã đăng nhập chưa
     * 
     * @param session HttpSession hiện tại
     * @return true nếu đã đăng nhập, false nếu chưa
     */
    protected boolean isAuthenticated(HttpSession session) {
        if (session == null) {
            return false;
        }
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        return isLoggedIn != null && isLoggedIn;
    }

    /**
     * Lấy member hiện tại từ session và reload từ DB để có dữ liệu mới nhất
     * 
     * @param session HttpSession hiện tại
     * @return Member object hoặc null nếu không tìm thấy
     */
    protected Member getCurrentMember(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return null;
        }

        // Reload từ DB để đảm bảo dữ liệu mới nhất
        Member member = memberService.getById(user.getId());
        
        // Cập nhật lại session
        if (member != null) {
            session.setAttribute("user", member);
        }
        
        return member;
    }

    /**
     * Kiểm tra authentication và redirect nếu chưa đăng nhập
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return Member object nếu đã authenticated, null nếu chưa (đã redirect)
     * @throws IOException
     */
    protected Member requireAuthentication(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        HttpSession session = request.getSession(false);
        
        if (!isAuthenticated(session)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }

        Member currentMember = getCurrentMember(session);
        if (currentMember == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }

        // Set member vào request để JSP sử dụng
        request.setAttribute("member", currentMember);
        
        return currentMember;
    }

    /**
     * Xử lý lỗi chung và forward đến trang error
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param e Exception đã xảy ra
     * @param userMessage Thông báo lỗi cho user
     * @throws ServletException
     * @throws IOException
     */
    protected void handleError(HttpServletRequest request, HttpServletResponse response, 
                               Exception e, String userMessage) throws ServletException, IOException {
        System.err.println("[" + getClass().getSimpleName() + "] Error: " + e.getMessage());
        e.printStackTrace();
        request.setAttribute("error", userMessage);
        request.getRequestDispatcher("/views/error/500.jsp").forward(request, response);
    }

    /**
     * Tính phân loại BMI
     * 
     * @param bmi Giá trị BMI
     * @return Phân loại BMI dạng String
     */
    protected String calculateBMICategory(Float bmi) {
        if (bmi == null) {
            return "Chưa xác định";
        }
        if (bmi < 18.5) {
            return "Thiếu cân";
        } else if (bmi < 25) {
            return "Bình thường";
        } else if (bmi < 30) {
            return "Thừa cân";
        } else {
            return "Béo phì";
        }
    }
}

