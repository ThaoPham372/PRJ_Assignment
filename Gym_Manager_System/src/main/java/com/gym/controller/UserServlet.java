package com.gym.controller;

import com.gym.model.User;
import com.gym.service.UserService;
import com.gym.service.nutrition.NutritionService;
import com.gym.service.nutrition.NutritionServiceImpl;
import com.gym.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * UserServlet - Controller for Member functionality
 * Follows MVC pattern: Only delegates to Service layer, no business logic here
 */
@WebServlet(name = "MemberServlet", urlPatterns = {"/member/*"})
@MultipartConfig(maxFileSize = 5242880, maxRequestSize = 10485760) // 5MB max file, 10MB max request
public class UserServlet extends HttpServlet {

    private transient UserService userService;
    private transient NutritionService nutritionService;
    private transient com.gym.service.membership.MembershipService membershipService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = new UserService();
        this.nutritionService = new NutritionServiceImpl();
        this.membershipService = new com.gym.service.membership.MembershipServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Debug session info
        jakarta.servlet.http.HttpSession session = request.getSession(false);
        System.out.println("[MemberServlet] doGet - Session: " + (session != null ? session.getId() : "NULL"));
        System.out.println("[MemberServlet] doGet - isLoggedIn: " + SessionUtil.isLoggedIn(request));
        
        User currentUser = SessionUtil.getCurrentUser(request);
        System.out.println("[MemberServlet] doGet - Current User: " + (currentUser != null ? currentUser.getUsername() + " (ID: " + currentUser.getId() + ")" : "NULL"));
        
        List<String> roles = SessionUtil.getUserRoles(request);
        System.out.println("[MemberServlet] doGet - User Roles: " + (roles != null ? roles.toString() : "NULL"));
        
        // Check authentication - must be logged in
        if (!SessionUtil.isLoggedIn(request)) {
            System.out.println("[MemberServlet] doGet - Authentication failed, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        // If user is admin or PT, redirect to their dashboard
        if (SessionUtil.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }
        
        if (SessionUtil.isPersonalTrainer(request)) {
            response.sendRedirect(request.getContextPath() + "/pt/dashboard");
            return;
        }
        
        // If logged in user (even without specific role), allow access to member area
        
        String path = request.getPathInfo();
        String action = path == null || "/".equals(path) ? "dashboard" : path.substring(1);
        
        System.out.println("[MemberServlet] GET action: " + action);
        
        // Handle nested paths first (more specific paths)
        if (action != null && action.startsWith("nutrition/history")) {
            System.out.println("[MemberServlet] Routing to nutrition/history");
            showNutritionHistory(request, response);
        } else if (action != null && action.equals("nutrition")) {
            showNutrition(request, response);
        } else if (action != null && action.equals("membership")) {
            showMembership(request, response);
        } else if (action != null && action.startsWith("profile/edit")) {
            showProfileEdit(request, response);
        } else {
            switch (action) {
                case "dashboard":
                    showDashboard(request, response);
                    break;
                case "profile":
                    showProfile(request, response);
                    break;
            case "body-metrics":
                showBodyMetricsEdit(request, response);
                break;
            case "goals":
                showGoalsEdit(request, response);
                break;
            case "body-goals":
                showBodyGoals(request, response);
                break;
                case "workout":
                    showWorkout(request, response);
                    break;
                case "schedule":
                    showSchedule(request, response);
                    break;
                case "membership":
                    showMembership(request, response);
                    break;
                case "support":
                    showSupport(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found: " + action);
                    break;
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check authentication - must be logged in
        if (!SessionUtil.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        String path = request.getPathInfo();
        String action = path == null || "/".equals(path) ? "" : path.substring(1);
        
        System.out.println("[MemberServlet] POST action: " + action);
        
        switch (action) {
            case "profile/update":
                updateProfile(request, response);
                break;
            case "body-metrics/update":
                updateBodyMetrics(request, response);
                break;
            case "goals/update":
                updateGoals(request, response);
                break;
            case "body-goals/update":
                updateBodyGoals(request, response);
                break;
            case "workout/log":
                logWorkout(request, response);
                break;
            case "nutrition/addMeal":
                addMeal(request, response);
                break;
            case "nutrition/deleteMeal":
                deleteMeal(request, response);
                break;
            case "membership/buyNow":
                Long userId = SessionUtil.getUserId(request);
                if (userId == null) {
                    response.sendRedirect(request.getContextPath() + "/auth/login");
                    return;
                }
                buyMembershipNow(request, response, userId);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Action not found: " + action);
                break;
        }
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        Long userId = SessionUtil.getUserId(request);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        // Get dashboard data from service
        Map<String, Object> dashboardData = userService.getDashboardData(userId);
        if (dashboardData == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        request.setAttribute("dashboardData", dashboardData);
        request.getRequestDispatcher("/views/member/dashboard.jsp").forward(request, response);
    }

    private void showProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Long userId = SessionUtil.getUserId(request);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/member/dashboard");
            return;
        }
        
        // Check for success/error messages from session
        String successMsg = (String) request.getSession().getAttribute("profileUpdateSuccess");
        String errorMsg = (String) request.getSession().getAttribute("profileUpdateError");
        if (successMsg != null) {
            request.setAttribute("success", successMsg);
            request.getSession().removeAttribute("profileUpdateSuccess");
        }
        if (errorMsg != null) {
            request.setAttribute("error", errorMsg);
            request.getSession().removeAttribute("profileUpdateError");
        }
        
        // Get profile data from service
        Map<String, Object> profileData = userService.getProfileData(userId);
        if (profileData == null) {
            response.sendRedirect(request.getContextPath() + "/member/dashboard");
            return;
        }
        
        request.setAttribute("profileData", profileData);
        request.getRequestDispatcher("/views/member/profile.jsp").forward(request, response);
    }
    
    private void showProfileEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Long userId = SessionUtil.getUserId(request);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/member/profile");
            return;
        }
        
        // Get profile data from service
        Map<String, Object> profileData = userService.getProfileData(userId);
        if (profileData == null) {
            response.sendRedirect(request.getContextPath() + "/member/profile");
            return;
        }
        
        request.setAttribute("profileData", profileData);
        request.getRequestDispatcher("/views/member/profile-edit.jsp").forward(request, response);
    }
    
    private void updateProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Long userId = SessionUtil.getUserId(request);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/member/profile");
            return;
        }
        
        // Get user from service
        User user = userService.getUserById(userId);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/member/profile");
            return;
        }
        
        // Handle multipart request - check if it's multipart
        String contentType = request.getContentType();
        boolean isMultipart = contentType != null && contentType.toLowerCase().startsWith("multipart/form-data");
        
        // Update via service with multipart support
        boolean updated;
        if (isMultipart) {
            // Create a wrapper to handle multipart parameters
            updated = userService.updateProfileFromMultipartRequest(user, request);
        } else {
            updated = userService.updateProfileFromRequest(user, request);
        }
        
        if (updated) {
            request.getSession().setAttribute("profileUpdateSuccess", "Cập nhật thông tin thành công!");
        } else {
            request.getSession().setAttribute("profileUpdateError", "Không thể cập nhật thông tin. Vui lòng thử lại.");
        }
        
        response.sendRedirect(request.getContextPath() + "/member/profile");
    }
    
    private void showBodyMetricsEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(request) || !SessionUtil.isMember(request)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        Long userId = SessionUtil.getUserId(request);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/member/dashboard");
            return;
        }

        // Check for success/error messages
        String successMsg = (String) request.getSession().getAttribute("bodyMetricsUpdateSuccess");
        String errorMsg = (String) request.getSession().getAttribute("bodyMetricsUpdateError");
        if (successMsg != null) {
            request.setAttribute("success", successMsg);
            request.getSession().removeAttribute("bodyMetricsUpdateSuccess");
        }
        if (errorMsg != null) {
            request.setAttribute("error", errorMsg);
            request.getSession().removeAttribute("bodyMetricsUpdateError");
        }

        // Get profile data from service (contains body metrics)
        Map<String, Object> profileData = userService.getProfileData(userId);
        if (profileData == null) {
            response.sendRedirect(request.getContextPath() + "/member/dashboard");
            return;
        }

        request.setAttribute("profileData", profileData);
        request.getRequestDispatcher("/views/member/body-metrics-edit.jsp").forward(request, response);
    }
    
    private void showGoalsEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        Long userId = SessionUtil.getUserId(request);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/member/dashboard");
            return;
        }

        // Check for success/error messages
        String successMsg = (String) request.getSession().getAttribute("goalsUpdateSuccess");
        String errorMsg = (String) request.getSession().getAttribute("goalsUpdateError");
        if (successMsg != null) {
            request.setAttribute("success", successMsg);
            request.getSession().removeAttribute("goalsUpdateSuccess");
        }
        if (errorMsg != null) {
            request.setAttribute("error", errorMsg);
            request.getSession().removeAttribute("goalsUpdateError");
        }

        // Get profile data from service
        Map<String, Object> profileData = userService.getProfileData(userId);
        if (profileData == null) {
            response.sendRedirect(request.getContextPath() + "/member/dashboard");
            return;
        }

        request.setAttribute("profileData", profileData);
        request.getRequestDispatcher("/views/member/goals-edit.jsp").forward(request, response);
    }
    
    private void showBodyGoals(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        Long userId = SessionUtil.getUserId(request);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/member/dashboard");
            return;
        }

        // Check for success/error messages
        String successMsg = (String) request.getSession().getAttribute("bodyGoalsUpdateSuccess");
        String errorMsg = (String) request.getSession().getAttribute("bodyGoalsUpdateError");
        if (successMsg != null) {
            request.setAttribute("success", successMsg);
            request.getSession().removeAttribute("bodyGoalsUpdateSuccess");
        }
        if (errorMsg != null) {
            request.setAttribute("error", errorMsg);
            request.getSession().removeAttribute("bodyGoalsUpdateError");
        }

        // Get profile data from service
        Map<String, Object> profileData = userService.getProfileData(userId);
        if (profileData == null) {
            response.sendRedirect(request.getContextPath() + "/member/dashboard");
            return;
        }
        
        // Get nutrition goal
        java.util.Optional<com.gym.model.NutritionGoal> goalOpt = userService.getNutritionGoal(userId);
        com.gym.model.NutritionGoal nutritionGoal = goalOpt.orElse(new com.gym.model.NutritionGoal());

        request.setAttribute("profileData", profileData);
        request.setAttribute("nutritionGoal", nutritionGoal);
        request.getRequestDispatcher("/views/member/body-goals.jsp").forward(request, response);
    }
    
    private void updateBodyMetrics(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        Long userId = SessionUtil.getUserId(request);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/member/body-metrics");
            return;
        }

        // Get user from service
        User user = userService.getUserById(userId);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/member/body-metrics");
            return;
        }

        // Update via service
        boolean updated = userService.updateBodyMetricsFromRequest(user, request);

        if (updated) {
            request.getSession().setAttribute("bodyMetricsUpdateSuccess", "Cập nhật chỉ số cơ thể thành công!");
        } else {
            request.getSession().setAttribute("bodyMetricsUpdateError", "Không thể cập nhật chỉ số cơ thể. Vui lòng thử lại.");
        }
        
        response.sendRedirect(request.getContextPath() + "/member/body-metrics");
    }
    
    private void updateGoals(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        Long userId = SessionUtil.getUserId(request);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/member/goals");
            return;
        }

        // Get user from service
        User user = userService.getUserById(userId);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/member/goals");
            return;
        }

        // Update via service
        boolean updated = userService.updateGoalsFromRequest(user, request);
        
        if (updated) {
            request.getSession().setAttribute("goalsUpdateSuccess", "Cập nhật mục tiêu thành công!");
        } else {
            request.getSession().setAttribute("goalsUpdateError", "Không thể cập nhật mục tiêu. Vui lòng thử lại.");
        }
        
        response.sendRedirect(request.getContextPath() + "/member/goals");
    }
    
    private void updateBodyGoals(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        Long userId = SessionUtil.getUserId(request);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/member/body-goals");
            return;
        }

        // Get user from service
        User user = userService.getUserById(userId);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/member/body-goals");
            return;
        }

        // Update body metrics
        boolean metricsUpdated = userService.updateBodyMetricsFromRequest(user, request);
        
        // Update goals (this also recalculates calories based on updated metrics)
        boolean goalsUpdated = userService.updateGoalsFromRequest(user, request);

        if (metricsUpdated && goalsUpdated) {
            request.getSession().setAttribute("bodyGoalsUpdateSuccess", "Cập nhật chỉ số và mục tiêu thành công!");
        } else {
            request.getSession().setAttribute("bodyGoalsUpdateError", "Không thể cập nhật. Vui lòng thử lại.");
        }
        
        response.sendRedirect(request.getContextPath() + "/member/body-goals");
    }
    
    // ========== PLACEHOLDER METHODS FOR OTHER PAGES ==========
    private void showWorkout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/member/workout.jsp").forward(request, response);
    }
    
    private void showNutrition(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        Long userId = SessionUtil.getUserId(request);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/member/dashboard");
            return;
        }
        
        // Get search keyword from request
        String keyword = request.getParameter("q");
        if (keyword == null) {
            keyword = "";
        }
        
        // Search foods if keyword is provided, otherwise show default foods
        java.util.List<com.gym.model.Food> foods = java.util.Collections.emptyList();
        if (!keyword.trim().isEmpty()) {
            // Search foods by keyword
            foods = nutritionService.searchFoods(keyword.trim(), 20);
            request.setAttribute("searchKeyword", keyword.trim());
        } else {
            // Show default foods when no search keyword
            foods = nutritionService.getDefaultFoods(15);
            request.setAttribute("searchKeyword", "");
        }
        
        // Get today's meals and totals
        java.util.List<com.gym.model.UserMeal> todayMeals = nutritionService.todayMeals(userId);
        com.gym.model.DailyIntakeDTO totals = nutritionService.todayTotals(userId);
        
        // Get nutrition goal (for target calories and protein)
        java.util.Optional<com.gym.model.NutritionGoal> goalOpt = nutritionService.getNutritionGoal(userId);
        com.gym.model.NutritionGoal nutritionGoal = goalOpt.orElse(null);
        
        // Set default targets if no goal is set
        java.math.BigDecimal targetCalories = java.math.BigDecimal.valueOf(2000);
        java.math.BigDecimal targetProtein = java.math.BigDecimal.valueOf(150);
        
        if (nutritionGoal != null) {
            if (nutritionGoal.getDailyCaloriesTarget() != null) {
                targetCalories = nutritionGoal.getDailyCaloriesTarget();
            }
            if (nutritionGoal.getDailyProteinTarget() != null) {
                targetProtein = nutritionGoal.getDailyProteinTarget();
            }
        }
        
        // Calculate percentages for progress bars
        double caloriesPercent = 0.0;
        double proteinPercent = 0.0;
        
        if (totals.getCaloriesKcal() != null && targetCalories != null && targetCalories.compareTo(java.math.BigDecimal.ZERO) > 0) {
            caloriesPercent = totals.getCaloriesKcal().divide(targetCalories, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(java.math.BigDecimal.valueOf(100)).doubleValue();
            if (caloriesPercent > 100) caloriesPercent = 100;
        }
        
        if (totals.getProteinG() != null && targetProtein != null && targetProtein.compareTo(java.math.BigDecimal.ZERO) > 0) {
            proteinPercent = totals.getProteinG().divide(targetProtein, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(java.math.BigDecimal.valueOf(100)).doubleValue();
            if (proteinPercent > 100) proteinPercent = 100;
        }
        
        // Set attributes for JSP
        request.setAttribute("foods", foods);
        request.setAttribute("todayMeals", todayMeals);
        request.setAttribute("totals", totals);
        request.setAttribute("searchKeyword", keyword);
        request.setAttribute("targetCalories", targetCalories);
        request.setAttribute("targetProtein", targetProtein);
        request.setAttribute("caloriesPercent", caloriesPercent);
        request.setAttribute("proteinPercent", proteinPercent);
        
        request.getRequestDispatcher("/views/member/nutrition.jsp").forward(request, response);
    }
    
    private void addMeal(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        Long userId = SessionUtil.getUserId(request);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/member/nutrition");
            return;
        }
        
        try {
            // Get parameters from form
            String foodIdStr = request.getParameter("foodId");
            String servingsStr = request.getParameter("servings");
            
            if (foodIdStr == null || servingsStr == null) {
                request.getSession().setAttribute("nutritionError", "Vui lòng chọn món ăn và nhập khẩu phần");
                response.sendRedirect(request.getContextPath() + "/member/nutrition");
                return;
            }
            
            long foodId = Long.parseLong(foodIdStr);
            java.math.BigDecimal servings = new java.math.BigDecimal(servingsStr);
            
            // Validate servings
            if (servings.compareTo(java.math.BigDecimal.ZERO) <= 0) {
                request.getSession().setAttribute("nutritionError", "Khẩu phần phải lớn hơn 0");
                response.sendRedirect(request.getContextPath() + "/member/nutrition");
                return;
            }
            
            // Add meal via service
            nutritionService.addMeal(userId, foodId, servings);
            
            // Success - redirect with PRG pattern
            request.getSession().setAttribute("nutritionSuccess", "Thêm món ăn thành công!");
            response.sendRedirect(request.getContextPath() + "/member/nutrition");
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("nutritionError", "Dữ liệu không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/member/nutrition");
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("nutritionError", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/member/nutrition");
        } catch (Exception e) {
            System.err.println("Error adding meal: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("nutritionError", "Lỗi hệ thống khi thêm món ăn");
            response.sendRedirect(request.getContextPath() + "/member/nutrition");
        }
    }
    
    private void deleteMeal(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        Long userId = SessionUtil.getUserId(request);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/member/nutrition");
            return;
        }
        
        try {
            String mealIdStr = request.getParameter("mealId");
            if (mealIdStr == null) {
                request.getSession().setAttribute("nutritionError", "Không tìm thấy món ăn để xóa");
                response.sendRedirect(request.getContextPath() + "/member/nutrition");
                return;
            }
            
            long mealId = Long.parseLong(mealIdStr);
            
            boolean deleted = nutritionService.deleteMeal(userId, mealId);
            if (deleted) {
                request.getSession().setAttribute("nutritionSuccess", "Đã xóa món ăn thành công!");
            } else {
                request.getSession().setAttribute("nutritionError", "Không thể xóa món ăn. Món ăn không tồn tại hoặc không thuộc về bạn.");
            }
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("nutritionError", "Dữ liệu không hợp lệ");
        } catch (Exception e) {
            System.err.println("Error deleting meal: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("nutritionError", "Lỗi hệ thống khi xóa món ăn");
        }
        
        response.sendRedirect(request.getContextPath() + "/member/nutrition");
    }
    
    private void showNutritionHistory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("[MemberServlet] showNutritionHistory called");
        
        if (!SessionUtil.isLoggedIn(request)) {
            System.out.println("[MemberServlet] Not logged in, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        Long userId = SessionUtil.getUserId(request);
        if (userId == null) {
            System.out.println("[MemberServlet] User ID is null, redirecting to dashboard");
            response.sendRedirect(request.getContextPath() + "/member/dashboard");
            return;
        }
        
        System.out.println("[MemberServlet] User ID: " + userId);
        
        // Get date from request parameter (format: yyyy-MM-dd)
        String dateParam = request.getParameter("date");
        java.time.LocalDate selectedDate;
        
        if (dateParam != null && !dateParam.trim().isEmpty()) {
            try {
                selectedDate = java.time.LocalDate.parse(dateParam.trim());
                System.out.println("[MemberServlet] Selected date: " + selectedDate);
            } catch (java.time.format.DateTimeParseException e) {
                System.out.println("[MemberServlet] Invalid date format, using today: " + e.getMessage());
                selectedDate = java.time.LocalDate.now(java.time.ZoneId.of("Asia/Ho_Chi_Minh"));
            }
        } else {
            // Default to today if no date specified
            selectedDate = java.time.LocalDate.now(java.time.ZoneId.of("Asia/Ho_Chi_Minh"));
            System.out.println("[MemberServlet] No date specified, using today: " + selectedDate);
        }
        
        // Get meals and totals for the selected date
        java.util.List<com.gym.model.UserMeal> meals = nutritionService.getMealsByDate(userId, selectedDate);
        com.gym.model.DailyIntakeDTO totals = nutritionService.getDailyTotalsByDate(userId, selectedDate);
        
        System.out.println("[MemberServlet] Found " + meals.size() + " meals for date " + selectedDate);
        System.out.println("[MemberServlet] Totals - Calories: " + totals.getCaloriesKcal() + ", Protein: " + totals.getProteinG());
        
        // Set attributes for JSP
        request.setAttribute("meals", meals);
        request.setAttribute("totals", totals);
        request.setAttribute("selectedDate", selectedDate);
        request.setAttribute("selectedDateStr", selectedDate.toString());
        
        // Set today's date for max date picker
        java.time.LocalDate today = java.time.LocalDate.now(java.time.ZoneId.of("Asia/Ho_Chi_Minh"));
        request.setAttribute("todayStr", today.toString());
        
        request.getRequestDispatcher("/views/member/nutrition-history.jsp").forward(request, response);
    }
    
    private void showSchedule(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/member/schedule.jsp").forward(request, response);
    }
    
    private void showMembership(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        Long userId = SessionUtil.getUserId(request);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        // Get all active memberships
        System.out.println("[MemberServlet] ===== START: Loading memberships =====");
        java.util.List<com.gym.model.membership.Membership> memberships = membershipService.getAllActiveMemberships();
        System.out.println("[MemberServlet] Service returned " + memberships.size() + " active memberships");
        
        if (memberships != null) {
            System.out.println("[MemberServlet] Memberships list is NOT NULL");
            if (memberships.isEmpty()) {
                System.out.println("[MemberServlet] WARNING: Memberships list is EMPTY!");
            } else {
                System.out.println("[MemberServlet] Memberships list contains " + memberships.size() + " items:");
                for (int i = 0; i < memberships.size(); i++) {
                    com.gym.model.membership.Membership m = memberships.get(i);
                    System.out.println("[MemberServlet]   [" + i + "] ID=" + m.getMembershipId() + 
                                      ", Name=" + m.getMembershipName() + 
                                      ", DisplayName=" + m.getDisplayName());
                }
            }
        } else {
            System.out.println("[MemberServlet] ERROR: Memberships list is NULL!");
        }
        
        // Get user's current active membership
        java.util.Optional<com.gym.model.membership.UserMembership> currentMembership = 
            membershipService.getUserActiveMembership(userId);
        System.out.println("[MemberServlet] User " + userId + " has active membership: " + currentMembership.isPresent());
        
        // Get user's membership ID if exists (to mark "Gói của bạn")
        Long currentMembershipId = currentMembership.map(m -> m.getMembershipId()).orElse(null);
        
        System.out.println("[MemberServlet] Setting request attributes:");
        System.out.println("[MemberServlet]   - memberships: " + (memberships != null ? memberships.size() + " items" : "NULL"));
        System.out.println("[MemberServlet]   - currentMembership: " + (currentMembership.isPresent() ? "Present" : "Empty"));
        System.out.println("[MemberServlet]   - currentMembershipId: " + currentMembershipId);
        
        request.setAttribute("memberships", memberships);
        request.setAttribute("currentMembership", currentMembership.orElse(null));
        request.setAttribute("currentMembershipId", currentMembershipId);
        
        System.out.println("[MemberServlet] Forwarding to membership.jsp");
        System.out.println("[MemberServlet] ===== END: Loading memberships =====");
        
        request.getRequestDispatcher("/views/member/membership.jsp").forward(request, response);
    }
    
    private void buyMembershipNow(HttpServletRequest request, HttpServletResponse response, Long userId)
            throws ServletException, IOException {
        try {
            String membershipIdStr = request.getParameter("membershipId");
            if (membershipIdStr == null) {
                request.getSession().setAttribute("error", "Không tìm thấy gói thành viên");
                response.sendRedirect(request.getContextPath() + "/member/membership");
                return;
            }
            
            Long membershipId = Long.parseLong(membershipIdStr);
            
            // Redirect to checkout with membership
            request.getSession().setAttribute("membershipId", membershipId);
            response.sendRedirect(request.getContextPath() + "/checkout?type=membership");
            
        } catch (Exception e) {
            System.err.println("Error buying membership: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi khi mua gói thành viên");
            response.sendRedirect(request.getContextPath() + "/member/membership");
        }
    }
    
    private void showSupport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/member/support.jsp").forward(request, response);
    }
    
    private void logWorkout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Implement workout logging via service
        response.sendRedirect(request.getContextPath() + "/member/workout");
    }
}
