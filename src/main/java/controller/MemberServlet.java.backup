package controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Member;
import model.Membership;
import model.NutritionGoal;
import model.Package;
import model.User;
import service.MemberService;
import service.MembershipService;
import service.PackageService;
import service.PasswordService;
import service.nutrition.NutritionService;
import service.nutrition.NutritionServiceImpl;

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
        "/member/support",
        "/member/change-password"
})
public class MemberServlet extends HttpServlet {

    private MemberService memberService;
    private MembershipService membershipService;
    private PackageService packageService;
    private PasswordService passwordService;
    private NutritionService nutritionService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.memberService = new MemberService();
        this.membershipService = new MembershipService();
        this.packageService = new PackageService();
        this.passwordService = new PasswordService();
        this.nutritionService = new NutritionServiceImpl();
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

                case "/member/membership":
                    showMembership(request, response, currentMember);
                    break;

                // Các trang chưa implement - tạm thời forward đơn giản
                case "/member/schedule":

                case "/member/support":
                    request.getRequestDispatcher("/views" + path + ".jsp").forward(request, response);
                    break;

                case "/member/change-password":
                    // GET request không được phép - redirect về profile
                    response.sendRedirect(request.getContextPath() + "/member/profile");
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

                case "/member/body-goals":
                    updateBodyGoals(request, response, currentMember);
                    break;

                case "/member/change-password":
                    handleChangePassword(request, response, currentMember);
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
     * Optimized: Only reload if needed (not on every request to reduce DB calls)
     */
    private Member getCurrentMember(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return null;
        }

        // Check if we need to reload (e.g., after updates)
        // For now, reload to ensure fresh data, but this can be optimized
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
        if (member.getBmi() != null) {
            request.setAttribute("bmiCategory", calculateBMICategory(member.getBmi()));
        }
        request.getRequestDispatcher("/views/member/dashboard.jsp").forward(request, response);
    }

    /**
     * Hiển thị Profile - Thông tin cá nhân
     */
    private void showProfile(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        // Set BMI category nếu có BMI
        if (member.getBmi() != null) {
            request.setAttribute("bmiCategory", calculateBMICategory(member.getBmi()));
        }
        request.getRequestDispatcher("/views/member/profile.jsp").forward(request, response);
    }

    /**
     * Hiển thị form chỉnh sửa Profile
     */
    private void showProfileEdit(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/member/profile-edit.jsp").forward(request, response);
    }

    /**
     * Hiển thị Body Goals - Mục tiêu và chỉ số cơ thể
     */
    private void showBodyGoals(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        if (member.getBmi() != null) {
            request.setAttribute("bmiCategory", calculateBMICategory(member.getBmi()));
        }

        // Load nutrition goal if exists
        java.util.Optional<NutritionGoal> nutritionGoalOpt = nutritionService.getNutritionGoal(member.getId());
        if (nutritionGoalOpt.isPresent()) {
            NutritionGoal goal = nutritionGoalOpt.get();
            request.setAttribute("nutritionGoal", goal);

            // Map activity factor to activity level string for JSP
            String activityLevel = mapActivityFactorToLevel(goal.getActivityFactor());
            request.setAttribute("currentActivityLevel", activityLevel);
        }

        request.getRequestDispatcher("/views/member/body-goals.jsp").forward(request, response);
    }

    /**
     * Map activity factor (BigDecimal) to activity level string
     */
    private String mapActivityFactorToLevel(java.math.BigDecimal activityFactor) {
        if (activityFactor == null) {
            return "";
        }

        double value = activityFactor.doubleValue();
        if (value == 1.2) {
            return "sedentary";
        } else if (value == 1.375) {
            return "light";
        } else if (value == 1.55) {
            return "moderate";
        } else if (value == 1.725) {
            return "active";
        } else if (value == 1.9) {
            return "very_active";
        }
        return "";
    }

    /**
     * Hiển thị form chỉnh sửa Body Metrics
     */
    private void showBodyMetricsEdit(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/member/body-metrics-edit.jsp").forward(request, response);
    }

    /**
     * Hiển thị form chỉnh sửa Goals
     */
    private void showGoalsEdit(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/member/goals-edit.jsp").forward(request, response);
    }

    // ==================== UPDATE HANDLERS (POST) ====================

    /**
     * Cập nhật thông tin Profile
     */
    private void updateProfile(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        try {
            // Cập nhật thông tin User (kế thừa từ User)
            updateUserFields(member, request);

            // Cập nhật thông tin Member (emergency contact)
            updateMemberFields(member, request);

            // Lưu vào database
            int result = memberService.update(member);
            if (result > 0) {
                // Reload member từ database để có dữ liệu mới nhất
                Member updatedMember = memberService.getById(member.getId());
                HttpSession session = request.getSession();
                session.setAttribute("user", updatedMember);
                session.setAttribute("success", "Cập nhật thông tin thành công!");
                response.sendRedirect(request.getContextPath() + "/member/profile");
            } else {
                throw new Exception("Không thể cập nhật thông tin.");
            }
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("member", member);
            request.getRequestDispatcher("/views/member/profile-edit.jsp").forward(request, response);
        }
    }

    /**
     * Cập nhật các trường của User
     */
    private void updateUserFields(Member member, HttpServletRequest request) {
        String name = getParameter(request, "name");
        String phone = getParameter(request, "phone");
        String address = getParameter(request, "address");
        String gender = getParameter(request, "gender");
        String dobStr = getParameter(request, "dob");

        if (name != null)
            member.setName(name);
        if (phone != null)
            member.setPhone(phone);
        if (address != null)
            member.setAddress(address);
        if (gender != null)
            member.setGender(gender);

        if (dobStr != null && !dobStr.trim().isEmpty()) {
            try {
                member.setDob(java.sql.Date.valueOf(dobStr));
            } catch (IllegalArgumentException e) {
                System.err.println("[MemberServlet] Invalid date format: " + dobStr);
            }
        }
    }

    /**
     * Cập nhật các trường của Member
     */
    private void updateMemberFields(Member member, HttpServletRequest request) {
        // Emergency Contact
        String emergencyName = getParameter(request, "emergencyContactName");
        String emergencyPhone = getParameter(request, "emergencyContactPhone");
        String emergencyRelation = getParameter(request, "emergencyContactRelation");
        String emergencyAddress = getParameter(request, "emergencyContactAddress");

        if (emergencyName != null)
            member.setEmergencyContactName(emergencyName);
        if (emergencyPhone != null)
            member.setEmergencyContactPhone(emergencyPhone);
        if (emergencyRelation != null)
            member.setEmergencyContactRelation(emergencyRelation);
        if (emergencyAddress != null)
            member.setEmergencyContactAddress(emergencyAddress);

        // Physical Information
        String weightStr = getParameter(request, "weight");
        String heightStr = getParameter(request, "height");

        if (weightStr != null) {
            try {
                Float weight = parseFloat(weightStr, "Cân nặng", 0f, 500f);
                member.setWeight(weight);
            } catch (IllegalArgumentException e) {
                // Giữ nguyên giá trị cũ nếu parse lỗi
            }
        }

        if (heightStr != null) {
            try {
                Float height = parseFloat(heightStr, "Chiều cao", 0f, 300f);
                member.setHeight(height);
            } catch (IllegalArgumentException e) {
                // Giữ nguyên giá trị cũ nếu parse lỗi
            }
        }

        // Tính BMI nếu có đủ dữ liệu
        calculateBMI(member);
    }

    /**
     * Lấy parameter và trim, trả về null nếu rỗng
     */
    private String getParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return (value != null && !value.trim().isEmpty()) ? value.trim() : null;
    }

    /**
     * Cập nhật chỉ số cơ thể (Weight, Height, BMI)
     */
    private void updateBodyMetrics(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        try {
            String weightStr = getParameter(request, "weight");
            String heightStr = getParameter(request, "height");

            if (weightStr != null) {
                Float weight = parseFloat(weightStr, "Cân nặng", 0f, 500f);
                member.setWeight(weight);
            }

            if (heightStr != null) {
                Float height = parseFloat(heightStr, "Chiều cao", 0f, 300f);
                member.setHeight(height);
            }

            // Tính BMI nếu có đủ dữ liệu
            calculateBMI(member);

            int result = memberService.update(member);
            if (result > 0) {
                HttpSession session = request.getSession();
                session.setAttribute("user", member);
                session.setAttribute("success", "Cập nhật chỉ số cơ thể thành công!");
                response.sendRedirect(request.getContextPath() + "/member/body-goals");
            } else {
                throw new Exception("Không thể cập nhật chỉ số.");
            }
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("member", member);
            request.getRequestDispatcher("/views/member/body-metrics-edit.jsp").forward(request, response);
        }
    }

    /**
     * Parse Float với validation
     */
    private Float parseFloat(String value, String fieldName, Float min, Float max) throws IllegalArgumentException {
        try {
            Float num = Float.parseFloat(value);
            if (num <= min || num > max) {
                throw new IllegalArgumentException(fieldName + " không hợp lệ (phải từ " + min + "-" + max + ")");
            }
            return num;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " phải là số");
        }
    }

    /**
     * Tính BMI cho member
     */
    private void calculateBMI(Member member) {
        if (member.getWeight() != null && member.getHeight() != null &&
                member.getWeight() > 0 && member.getHeight() > 0) {
            float heightInMeters = member.getHeight() / 100.0f;
            float bmi = member.getWeight() / (heightInMeters * heightInMeters);
            member.setBmi(bmi);
        }
    }

    /**
     * Cập nhật mục tiêu tập luyện
     */
    private void updateGoals(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        try {
            String goal = getParameter(request, "goal");
            if (goal == null) {
                throw new IllegalArgumentException("Mục tiêu không được để trống");
            }
            member.setGoal(goal);

            int result = memberService.update(member);
            if (result > 0) {
                HttpSession session = request.getSession();
                session.setAttribute("user", member);
                session.setAttribute("success", "Cập nhật mục tiêu thành công!");
                response.sendRedirect(request.getContextPath() + "/member/body-goals");
            } else {
                throw new Exception("Không thể cập nhật mục tiêu.");
            }
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("member", member);
            request.getRequestDispatcher("/views/member/goals-edit.jsp").forward(request, response);
        }
    }

    /**
     * Cập nhật Body Goals - Chỉ số cơ thể và mục tiêu dinh dưỡng
     */
    private void updateBodyGoals(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        try {
            // Get weight and height
            String weightStr = getParameter(request, "weight");
            String heightStr = getParameter(request, "height");

            if (weightStr != null) {
                Float weight = parseFloat(weightStr, "Cân nặng", 20f, 300f);
                member.setWeight(weight);
            }

            if (heightStr != null) {
                Float height = parseFloat(heightStr, "Chiều cao", 100f, 250f);
                member.setHeight(height);
            }

            // Calculate BMI
            calculateBMI(member);

            // Get fitness goal and activity level
            String fitnessGoal = getParameter(request, "fitnessGoal");
            String activityLevel = getParameter(request, "activityLevel");

            if (fitnessGoal == null || fitnessGoal.trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng chọn mục tiêu tập luyện");
            }

            if (activityLevel == null || activityLevel.trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng chọn mức độ hoạt động");
            }

            // Update member goal
            member.setGoal(fitnessGoal);

            // Update member in database
            int result = memberService.update(member);
            if (result <= 0) {
                throw new Exception("Không thể cập nhật thông tin cơ thể.");
            }

            // Calculate and save nutrition goal
            if (member.getWeight() != null && member.getHeight() != null) {
                // Calculate age from DOB
                Integer age = null;
                if (member.getDob() != null) {
                    java.util.Date dobUtil = member.getDob();
                    java.sql.Date dobSql = new java.sql.Date(dobUtil.getTime());
                    java.time.LocalDate dob = dobSql.toLocalDate();
                    java.time.LocalDate now = java.time.LocalDate.now();
                    age = now.getYear() - dob.getYear();
                    if (now.getDayOfYear() < dob.getDayOfYear()) {
                        age--;
                    }
                }

                // Map fitness goal to nutrition goal type
                String goalType = mapFitnessGoalToNutritionGoal(fitnessGoal);

                // Calculate and save nutrition goal
                try {
                    nutritionService.calculateAndSaveNutritionGoal(
                            member.getId(),
                            member.getWeight(),
                            member.getHeight(),
                            age,
                            member.getGender(),
                            goalType,
                            activityLevel);
                } catch (Exception e) {
                    System.err.println("[MemberServlet] Error calculating nutrition goal: " + e.getMessage());
                    e.printStackTrace();
                    // Continue even if nutrition goal calculation fails
                }
            }

            // Update session with updated member (already updated in database)
            HttpSession session = request.getSession();
            session.setAttribute("user", member);
            session.setAttribute("success", "Cập nhật thông tin và mục tiêu dinh dưỡng thành công!");

            // Redirect - getCurrentMember will reload from DB in doGet
            // Ensure response is not committed before redirect
            if (!response.isCommitted()) {
                response.sendRedirect(request.getContextPath() + "/member/body-goals");
            } else {
                System.err.println("[MemberServlet] WARNING: Response already committed! Cannot redirect.");
            }
            return; // Important: return after redirect to prevent further execution

        } catch (IllegalArgumentException e) {
            HttpSession session = request.getSession();
            session.setAttribute("error", e.getMessage());
            if (!response.isCommitted()) {
                response.sendRedirect(request.getContextPath() + "/member/body-goals");
            } else {
                System.err.println(
                        "[MemberServlet] WARNING: Response already committed! Cannot redirect after validation error.");
            }
        } catch (Exception e) {
            System.err.println("[MemberServlet] Error updating body goals: " + e.getMessage());
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("error", "Có lỗi xảy ra khi cập nhật. Vui lòng thử lại.");
            if (!response.isCommitted()) {
                response.sendRedirect(request.getContextPath() + "/member/body-goals");
            } else {
                System.err.println("[MemberServlet] WARNING: Response already committed! Cannot redirect after error.");
            }
        }
    }

    /**
     * Map fitness goal to nutrition goal type
     */
    private String mapFitnessGoalToNutritionGoal(String fitnessGoal) {
        if (fitnessGoal == null) {
            return "giu_dang";
        }

        switch (fitnessGoal.toLowerCase()) {
            case "lose_weight":
                return "giam_can";
            case "gain_muscle":
            case "gain_weight":
                return "tang_can";
            case "maintain":
            case "improve_health":
            case "athletic_performance":
            default:
                return "giu_dang";
        }
    }

    // ==================== MEMBERSHIP HANDLERS ====================

    /**
     * Hiển thị trang Membership - Quản lý gói tập
     */
    private void showMembership(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        try {
            // Lấy membership hiện tại của member (ACTIVE)
            Membership currentMembership = getCurrentActiveMembership(member);

            // Lấy tất cả packages có sẵn (chỉ active packages)
            List<Package> allPackages = packageService.getAll();
            List<Package> activePackages = allPackages.stream()
                    .filter(pkg -> pkg.getIsActive() != null && pkg.getIsActive())
                    .collect(Collectors.toList());

            // Set attributes
            request.setAttribute("currentMembership", currentMembership);
            request.setAttribute("packages", activePackages);

            // Nếu có membership hiện tại, set thêm packageId để highlight
            if (currentMembership != null && currentMembership.getPackageO() != null) {
                request.setAttribute("currentPackageId", currentMembership.getPackageO().getId());
            }

            System.out.println("[MemberServlet] Loading membership page for member: " + member.getId());
            System.out.println("[MemberServlet] Current membership: "
                    + (currentMembership != null ? currentMembership.getId() : "None"));
            System.out.println("[MemberServlet] Available packages: " + activePackages.size());

            request.getRequestDispatcher("/views/member/membership.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            handleError(request, response, e, "Có lỗi khi tải thông tin membership.");
        }
    }

    /**
     * Lấy membership ACTIVE hiện tại của member
     */
    private Membership getCurrentActiveMembership(Member member) {
        try {
            List<Membership> allMemberships = membershipService.getAll();
            Date now = new Date();

            // Tìm membership ACTIVE và chưa hết hạn
            for (Membership m : allMemberships) {
                if (m.getMember() != null &&
                        m.getMember().getId().equals(member.getId()) &&
                        "ACTIVE".equalsIgnoreCase(m.getStatus()) &&
                        m.getEndDate() != null &&
                        m.getEndDate().after(now)) {
                    return m;
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("[MemberServlet] Error getting current membership: " + e.getMessage());
            return null;
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

    // ==================== PASSWORD CHANGE HANDLER ====================

    /**
     * Xử lý yêu cầu đổi mật khẩu từ profile
     * Tạo password reset token và chuyển hướng sang trang reset password
     */
    private void handleChangePassword(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        System.out.println("[MemberServlet] ===== CHANGE PASSWORD REQUEST =====");
        System.out.println("[MemberServlet] Method: " + request.getMethod());
        System.out.println("[MemberServlet] Path: " + request.getServletPath());
        System.out.println("[MemberServlet] Member ID: " + (member != null ? member.getId() : "NULL"));

        try {
            // Lấy email của member
            String email = member.getEmail();
            HttpSession session = request.getSession();
            System.out.println("[MemberServlet] Member email: " + email);

            if (email == null || email.trim().isEmpty()) {
                session.setAttribute("error", "Không tìm thấy email. Vui lòng liên hệ quản trị viên.");
                response.sendRedirect(request.getContextPath() + "/member/profile");
                return;
            }

            // Normalize email
            email = email.trim().toLowerCase();

            // Kiểm tra rate limiting (30 giây)
            boolean hasPending = passwordService.hasPendingResetRequest(email);
            if (hasPending) {
                session.setAttribute("error",
                        "Bạn đã yêu cầu đổi mật khẩu gần đây. Vui lòng đợi 30 giây trước khi gửi lại.");
                response.sendRedirect(request.getContextPath() + "/member/profile");
                return;
            }

            // Tạo password reset token và gửi email
            System.out.println("[MemberServlet] Calling passwordService.requestPasswordReset()...");
            String verificationCode = passwordService.requestPasswordReset(email);

            if (verificationCode == null) {
                System.err.println("[MemberServlet] ❌ Failed to generate verification code");
                session.setAttribute("error", "Có lỗi xảy ra khi gửi email. Vui lòng thử lại sau.");
                response.sendRedirect(request.getContextPath() + "/member/profile");
                return;
            }

            System.out.println("[MemberServlet] ✅ Verification code generated: " + verificationCode);

            // Lưu email vào session và chuyển hướng sang trang reset password
            session.setAttribute("resetEmail", email);
            session.setAttribute("successMessage", "Mã xác nhận đã được gửi đến email: " + email);

            String redirectUrl = request.getContextPath() + "/auth/reset-password";
            System.out.println("[MemberServlet] Redirecting to: " + redirectUrl);
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            System.err.println("[MemberServlet] Error handling change password: " + e.getMessage());
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("error", "Có lỗi xảy ra. Vui lòng thử lại sau.");
            response.sendRedirect(request.getContextPath() + "/member/profile");
        }
    }

    /**
     * Xử lý lỗi chung
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response,
            Exception e, String userMessage) throws ServletException, IOException {
        request.setAttribute("error", userMessage);
        request.getRequestDispatcher("/views/error/500.jsp").forward(request, response);
    }
}
