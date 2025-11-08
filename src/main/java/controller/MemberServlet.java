package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Member;
import model.User;
import service.MemberService;

/**
 * MemberServlet - Controller xử lý các chức năng của Member
 * Tuân thủ mô hình MVC và nguyên tắc OOP
 * 
 * Chức năng chính:
 * 1. Dashboard - Hiển thị thông tin tổng quan
 * 2. Profile - Xem và cập nhật thông tin cá nhân
 * 3. Body Goals - Quản lý chỉ số cơ thể và mục tiêu
 */
@WebServlet(name = "MemberServlet", urlPatterns = {
    "/member/dashboard",
    "/member/profile",
    "/member/profile-edit",
    "/member/body-goals",
    "/member/body-metrics-edit",
    "/member/goals-edit",
    "/member/membership",
    "/member/schedule",
    "/member/nutrition",
    "/member/nutrition-history",
    "/member/support"
})
public class MemberServlet extends HttpServlet {

    private MemberService memberService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.memberService = new MemberService();
        System.out.println("[MemberServlet] Initialized successfully");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Kiểm tra authentication
        HttpSession session = request.getSession(false);
        if (!isAuthenticated(session)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Lấy member hiện tại
        Member currentMember = getCurrentMember(session);
        if (currentMember == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Set member vào request để JSP sử dụng
        request.setAttribute("member", currentMember);

        // Routing dựa trên path
        String path = request.getServletPath();
        
        try {
            switch (path) {
                case "/member/dashboard":
                    showDashboard(request, response, currentMember);
                    break;

                case "/member/profile":
                    showProfile(request, response, currentMember);
                    break;

                case "/member/profile-edit":
                    showProfileEdit(request, response, currentMember);
                    break;

                case "/member/body-goals":
                    showBodyGoals(request, response, currentMember);
                    break;

                case "/member/body-metrics-edit":
                    showBodyMetricsEdit(request, response, currentMember);
                    break;

                case "/member/goals-edit":
                    showGoalsEdit(request, response, currentMember);
                    break;

                // Các trang chưa implement - tạm thời forward đơn giản
                case "/member/membership":
                case "/member/schedule":
                case "/member/nutrition":
                case "/member/nutrition-history":
                case "/member/support":
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Kiểm tra authentication
        HttpSession session = request.getSession(false);
        if (!isAuthenticated(session)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Lấy member hiện tại
        Member currentMember = getCurrentMember(session);
        if (currentMember == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Routing dựa trên path
        String path = request.getServletPath();

        try {
            switch (path) {
                case "/member/profile":
                case "/member/profile-edit":
                    updateProfile(request, response, currentMember);
                    break;

                case "/member/body-metrics-edit":
                    updateBodyMetrics(request, response, currentMember);
                    break;

                case "/member/goals-edit":
                    updateGoals(request, response, currentMember);
                    break;

                default:
                    response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    break;
            }
        } catch (Exception e) {
            handleError(request, response, e, "Có lỗi xảy ra khi cập nhật. Vui lòng thử lại.");
        }
    }

    // ==================== AUTHENTICATION & SESSION ====================

    /**
     * Kiểm tra user đã đăng nhập chưa
     */
    private boolean isAuthenticated(HttpSession session) {
        if (session == null) {
            return false;
        }
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        return isLoggedIn != null && isLoggedIn;
    }

    /**
     * Lấy member hiện tại từ session và reload từ DB để có dữ liệu mới nhất
     */
    private Member getCurrentMember(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return null;
        }

        // Reload từ database để có dữ liệu mới nhất
        Member member = memberService.getById(user.getId());
        
        // Cập nhật lại session
        if (member != null) {
            session.setAttribute("user", member);
        }
        
        return member;
    }

    // ==================== VIEW HANDLERS (GET) ====================

    /**
     * Hiển thị Dashboard - Trang tổng quan
     */
    private void showDashboard(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        
        System.out.println("[MemberServlet] Loading dashboard for member ID: " + member.getId());
        
        // Chuẩn bị dữ liệu cho dashboard
        request.setAttribute("memberName", member.getName() != null ? member.getName() : member.getUsername());
        request.setAttribute("joinDate", member.getCreatedDate());
        
        // Thống kê cơ bản
        if (member.getBmi() != null) {
            request.setAttribute("bmi", member.getBmi());
            request.setAttribute("bmiCategory", calculateBMICategory(member.getBmi()));
        }
        
        if (member.getWeight() != null) {
            request.setAttribute("weight", member.getWeight());
        }
        
        if (member.getHeight() != null) {
            request.setAttribute("height", member.getHeight());
        }
        
        if (member.getGoal() != null && !member.getGoal().isEmpty()) {
            request.setAttribute("goal", member.getGoal());
        }
        
        // Forward đến dashboard JSP
        request.getRequestDispatcher("/views/member/dashboard.jsp").forward(request, response);
    }

    /**
     * Hiển thị Profile - Thông tin cá nhân
     */
    private void showProfile(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        
        System.out.println("[MemberServlet] Loading profile for member ID: " + member.getId());
        
        // Member đã được set trong doGet, chỉ cần forward
        request.getRequestDispatcher("/views/member/profile.jsp").forward(request, response);
    }

    /**
     * Hiển thị form chỉnh sửa Profile
     */
    private void showProfileEdit(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        
        System.out.println("[MemberServlet] Loading profile edit for member ID: " + member.getId());
        
        request.getRequestDispatcher("/views/member/profile-edit.jsp").forward(request, response);
    }

    /**
     * Hiển thị Body Goals - Mục tiêu và chỉ số cơ thể
     */
    private void showBodyGoals(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        
        System.out.println("[MemberServlet] Loading body goals for member ID: " + member.getId());
        
        // Chuẩn bị dữ liệu
        if (member.getBmi() != null) {
            request.setAttribute("bmi", member.getBmi());
            request.setAttribute("bmiCategory", calculateBMICategory(member.getBmi()));
        }
        
        request.getRequestDispatcher("/views/member/body-goals.jsp").forward(request, response);
    }

    /**
     * Hiển thị form chỉnh sửa Body Metrics
     */
    private void showBodyMetricsEdit(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        
        System.out.println("[MemberServlet] Loading body metrics edit for member ID: " + member.getId());
        
        request.getRequestDispatcher("/views/member/body-metrics-edit.jsp").forward(request, response);
    }

    /**
     * Hiển thị form chỉnh sửa Goals
     */
    private void showGoalsEdit(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        
        System.out.println("[MemberServlet] Loading goals edit for member ID: " + member.getId());
        
        request.getRequestDispatcher("/views/member/goals-edit.jsp").forward(request, response);
    }

    // ==================== UPDATE HANDLERS (POST) ====================

    /**
     * Cập nhật thông tin Profile
     */
    private void updateProfile(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        
        System.out.println("[MemberServlet] Updating profile for member ID: " + member.getId());
        
        try {
            // Lấy dữ liệu từ form
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String gender = request.getParameter("gender");
            String dobStr = request.getParameter("dob");
            
            // Emergency Contact
            String emergencyName = request.getParameter("emergencyContactName");
            String emergencyPhone = request.getParameter("emergencyContactPhone");
            String emergencyRelation = request.getParameter("emergencyContactRelation");
            String emergencyAddress = request.getParameter("emergencyContactAddress");

            // Cập nhật member object
            if (name != null && !name.trim().isEmpty()) {
                member.setName(name.trim());
            }
            
            if (phone != null && !phone.trim().isEmpty()) {
                member.setPhone(phone.trim());
            }
            
            if (address != null && !address.trim().isEmpty()) {
                member.setAddress(address.trim());
            }
            
            if (gender != null && !gender.trim().isEmpty()) {
                member.setGender(gender.trim());
            }
            
            // Parse date of birth
            if (dobStr != null && !dobStr.trim().isEmpty()) {
                try {
                    java.sql.Date dob = java.sql.Date.valueOf(dobStr);
                    member.setDob(dob);
                } catch (IllegalArgumentException e) {
                    System.err.println("[MemberServlet] Invalid date format: " + dobStr);
                }
            }
            
            // Update emergency contact
            if (emergencyName != null) {
                member.setEmergencyContactName(emergencyName.trim());
            }
            if (emergencyPhone != null) {
                member.setEmergencyContactPhone(emergencyPhone.trim());
            }
            if (emergencyRelation != null) {
                member.setEmergencyContactRelation(emergencyRelation.trim());
            }
            if (emergencyAddress != null) {
                member.setEmergencyContactAddress(emergencyAddress.trim());
            }

            // Lưu vào database
            int result = memberService.update(member);

            if (result > 0) {
                System.out.println("[MemberServlet] Profile updated successfully for member ID: " + member.getId());
                
                // Cập nhật session
                HttpSession session = request.getSession();
                session.setAttribute("user", member);
                session.setAttribute("successMessage", "Cập nhật thông tin thành công!");
                
                // Redirect về profile
                response.sendRedirect(request.getContextPath() + "/member/profile");
            } else {
                throw new Exception("Không thể cập nhật thông tin. Vui lòng thử lại.");
            }

        } catch (Exception e) {
            System.err.println("[MemberServlet] Error updating profile: " + e.getMessage());
            e.printStackTrace();
            
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.setAttribute("member", member);
            request.getRequestDispatcher("/views/member/profile-edit.jsp").forward(request, response);
        }
    }

    /**
     * Cập nhật chỉ số cơ thể (Weight, Height, BMI)
     */
    private void updateBodyMetrics(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        
        System.out.println("[MemberServlet] Updating body metrics for member ID: " + member.getId());
        
        try {
            // Lấy dữ liệu từ form
            String weightStr = request.getParameter("weight");
            String heightStr = request.getParameter("height");

            // Parse và validate
            Float weight = null;
            Float height = null;

            if (weightStr != null && !weightStr.trim().isEmpty()) {
                try {
                    weight = Float.parseFloat(weightStr.trim());
                    if (weight <= 0 || weight > 500) {
                        throw new IllegalArgumentException("Cân nặng không hợp lệ (phải từ 0-500 kg)");
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Cân nặng phải là số");
                }
            }

            if (heightStr != null && !heightStr.trim().isEmpty()) {
                try {
                    height = Float.parseFloat(heightStr.trim());
                    if (height <= 0 || height > 300) {
                        throw new IllegalArgumentException("Chiều cao không hợp lệ (phải từ 0-300 cm)");
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Chiều cao phải là số");
                }
            }

            // Cập nhật member
            if (weight != null) {
                member.setWeight(weight);
            }
            
            if (height != null) {
                member.setHeight(height);
            }

            // Tính BMI nếu có đủ dữ liệu
            if (member.getWeight() != null && member.getHeight() != null && 
                member.getWeight() > 0 && member.getHeight() > 0) {
                float heightInMeters = member.getHeight() / 100.0f; // Convert cm to m
                float bmi = member.getWeight() / (heightInMeters * heightInMeters);
                member.setBmi(bmi);
                System.out.println("[MemberServlet] Calculated BMI: " + bmi);
            }

            // Lưu vào database
            int result = memberService.update(member);

            if (result > 0) {
                System.out.println("[MemberServlet] Body metrics updated successfully for member ID: " + member.getId());
                
                // Cập nhật session
                HttpSession session = request.getSession();
                session.setAttribute("user", member);
                session.setAttribute("successMessage", "Cập nhật chỉ số cơ thể thành công!");
                
                // Redirect về body goals
                response.sendRedirect(request.getContextPath() + "/member/body-goals");
            } else {
                throw new Exception("Không thể cập nhật chỉ số. Vui lòng thử lại.");
            }

        } catch (Exception e) {
            System.err.println("[MemberServlet] Error updating body metrics: " + e.getMessage());
            e.printStackTrace();
            
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.setAttribute("member", member);
            request.getRequestDispatcher("/views/member/body-metrics-edit.jsp").forward(request, response);
        }
    }

    /**
     * Cập nhật mục tiêu tập luyện
     */
    private void updateGoals(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        
        System.out.println("[MemberServlet] Updating goals for member ID: " + member.getId());
        
        try {
            // Lấy dữ liệu từ form
            String goal = request.getParameter("goal");

            // Validate
            if (goal != null && !goal.trim().isEmpty()) {
                member.setGoal(goal.trim());
            } else {
                throw new IllegalArgumentException("Mục tiêu không được để trống");
            }

            // Lưu vào database
            int result = memberService.update(member);

            if (result > 0) {
                System.out.println("[MemberServlet] Goals updated successfully for member ID: " + member.getId());
                
                // Cập nhật session
                HttpSession session = request.getSession();
                session.setAttribute("user", member);
                session.setAttribute("successMessage", "Cập nhật mục tiêu thành công!");
                
                // Redirect về body goals
                response.sendRedirect(request.getContextPath() + "/member/body-goals");
            } else {
                throw new Exception("Không thể cập nhật mục tiêu. Vui lòng thử lại.");
            }

        } catch (Exception e) {
            System.err.println("[MemberServlet] Error updating goals: " + e.getMessage());
            e.printStackTrace();
            
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.setAttribute("member", member);
            request.getRequestDispatcher("/views/member/goals-edit.jsp").forward(request, response);
        }
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Tính phân loại BMI
     */
    private String calculateBMICategory(Float bmi) {
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

    /**
     * Xử lý lỗi chung
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response, 
                           Exception e, String userMessage) throws ServletException, IOException {
        System.err.println("[MemberServlet] Error: " + e.getMessage());
        e.printStackTrace();
        
        request.setAttribute("error", userMessage);
        request.getRequestDispatcher("/views/error/500.jsp").forward(request, response);
    }
}
