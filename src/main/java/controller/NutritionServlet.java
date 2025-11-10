package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.NutritionGoal;
import model.User;
import service.nutrition.NutritionService;
import service.nutrition.NutritionServiceImpl;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * NutritionServlet - Controller xử lý các chức năng dinh dưỡng của Member
 * Tuân thủ mô hình MVC và nguyên tắc OOP
 * 
 * Chức năng chính:
 * 1. Hiển thị trang dinh dưỡng hôm nay
 * 2. Tìm kiếm và thêm món ăn
 * 3. Xóa món ăn
 * 4. Xem lịch sử dinh dưỡng
 */
@WebServlet(name = "NutritionServlet", urlPatterns = {
    "/member/nutrition",
    "/member/nutrition/*"
})
public class NutritionServlet extends HttpServlet {

    private NutritionService nutritionService;
    private static final int DEFAULT_FOOD_LIMIT = 20;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void init() throws ServletException {
        super.init();
        this.nutritionService = new NutritionServiceImpl();
        System.out.println("[NutritionServlet] Initialized successfully");
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

        // Lấy user hiện tại
        User currentUser = getCurrentUser(session);
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Long userId = Long.valueOf(currentUser.getId());
        String path = request.getPathInfo();
        
        try {
            if (path == null || path.equals("/")) {
                // GET /member/nutrition - Hiển thị trang dinh dưỡng hôm nay
                showNutritionPage(request, response, userId);
            } else if (path.equals("/history")) {
                // GET /member/nutrition/history - Hiển thị lịch sử dinh dưỡng
                showNutritionHistory(request, response, userId);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            handleError(request, response, e, "Có lỗi xảy ra khi tải trang dinh dưỡng.");
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

        // Lấy user hiện tại
        User currentUser = getCurrentUser(session);
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Long userId = Long.valueOf(currentUser.getId());
        String path = request.getPathInfo();
        
        try {
            if (path == null || path.equals("/")) {
                // POST /member/nutrition - Xử lý form (redirect to GET)
                response.sendRedirect(request.getContextPath() + "/member/nutrition");
            } else if (path.equals("/addMeal")) {
                // POST /member/nutrition/addMeal - Thêm món ăn
                addMeal(request, response, userId);
            } else if (path.equals("/deleteMeal")) {
                // POST /member/nutrition/deleteMeal - Xóa món ăn
                deleteMeal(request, response, userId);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            handleError(request, response, e, "Có lỗi xảy ra khi xử lý yêu cầu.");
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
     * Lấy user hiện tại từ session
     */
    private User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    // ==================== VIEW HANDLERS (GET) ====================

    /**
     * Hiển thị trang dinh dưỡng hôm nay
     * Loads foods, today's meals, totals, and nutrition goals
     */
    private void showNutritionPage(HttpServletRequest request, HttpServletResponse response, Long userId)
            throws ServletException, IOException {
        
        try {
            // Lấy search keyword từ request
            String searchKeyword = request.getParameter("q");
            
            // Load foods (search or default)
            List<model.Food> foods;
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                foods = nutritionService.searchFoods(searchKeyword.trim(), DEFAULT_FOOD_LIMIT);
                request.setAttribute("searchKeyword", searchKeyword.trim());
            } else {
                foods = nutritionService.getDefaultFoods(DEFAULT_FOOD_LIMIT);
            }
            request.setAttribute("foods", foods);
            
            // Load today's meals
            List<model.UserMeal> todayMeals = nutritionService.todayMeals(userId);
            request.setAttribute("todayMeals", todayMeals);
            
            // Load today's totals
            model.DailyIntakeDTO totals = nutritionService.todayTotals(userId);
            // Ensure totals is never null
            if (totals == null) {
                totals = new model.DailyIntakeDTO();
            }
            request.setAttribute("totals", totals);
            
            // Load nutrition goals for progress calculation
            @SuppressWarnings("unchecked")
            Optional<NutritionGoal> goalOpt = (Optional<NutritionGoal>) (Optional<?>) nutritionService.getNutritionGoal(userId);
            if (goalOpt.isPresent()) {
                NutritionGoal goal = goalOpt.get();
                BigDecimal targetCalories = goal.getDailyCaloriesTarget();
                BigDecimal targetProtein = goal.getDailyProteinTarget();
                
                if (targetCalories != null && targetCalories.compareTo(BigDecimal.ZERO) > 0) {
                    request.setAttribute("targetCalories", targetCalories);
                    // Calculate progress percentage - safe null handling
                    BigDecimal currentCalories = totals.getCaloriesKcal();
                    if (currentCalories != null) {
                        BigDecimal caloriesPercent = currentCalories
                            .divide(targetCalories, 2, RoundingMode.HALF_UP)
                            .multiply(new BigDecimal("100"));
                        request.setAttribute("caloriesPercent", caloriesPercent);
                    } else {
                        request.setAttribute("caloriesPercent", BigDecimal.ZERO);
                    }
                }
                
                if (targetProtein != null && targetProtein.compareTo(BigDecimal.ZERO) > 0) {
                    request.setAttribute("targetProtein", targetProtein);
                    // Calculate progress percentage - safe null handling
                    BigDecimal currentProtein = totals.getProteinG();
                    if (currentProtein != null) {
                        BigDecimal proteinPercent = currentProtein
                            .divide(targetProtein, 2, RoundingMode.HALF_UP)
                            .multiply(new BigDecimal("100"));
                        request.setAttribute("proteinPercent", proteinPercent);
                    } else {
                        request.setAttribute("proteinPercent", BigDecimal.ZERO);
                    }
                }
            }
            
            // Forward to nutrition.jsp
            request.getRequestDispatcher("/views/member/nutrition.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("[NutritionServlet] Error showing nutrition page: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Hiển thị lịch sử dinh dưỡng
     * Loads meals and totals for a specific date
     */
    private void showNutritionHistory(HttpServletRequest request, HttpServletResponse response, Long userId)
            throws ServletException, IOException {
        
        try {
            // Get date parameter
            String dateStr = request.getParameter("date");
            LocalDate selectedDate;
            
            if (dateStr != null && !dateStr.trim().isEmpty()) {
                try {
                    selectedDate = LocalDate.parse(dateStr.trim(), DATE_FORMATTER);
                } catch (Exception e) {
                    // Invalid date format, use today
                    selectedDate = LocalDate.now();
                }
            } else {
                // No date provided, use today
                selectedDate = LocalDate.now();
            }
            
            // Set today's date string for date picker
            LocalDate today = LocalDate.now();
            String todayStr = today.format(DATE_FORMATTER);
            request.setAttribute("todayStr", todayStr);
            
            // Set selected date string
            String selectedDateStr = selectedDate.format(DATE_FORMATTER);
            request.setAttribute("selectedDateStr", selectedDateStr);
            request.setAttribute("selectedDate", selectedDate);
            
            // Load meals for selected date
            List<model.UserMeal> meals = nutritionService.getMealsByDate(userId, selectedDate);
            request.setAttribute("meals", meals);
            
            // Load totals for selected date
            model.DailyIntakeDTO totals = nutritionService.getDailyTotalsByDate(userId, selectedDate);
            request.setAttribute("totals", totals);
            
            // Forward to nutrition-history.jsp
            request.getRequestDispatcher("/views/member/nutrition-history.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("[NutritionServlet] Error showing nutrition history: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // ==================== ACTION HANDLERS (POST) ====================

    /**
     * Thêm món ăn vào hôm nay
     * Validates input and calls service layer
     */
    private void addMeal(HttpServletRequest request, HttpServletResponse response, Long userId)
            throws ServletException, IOException {
        
        try {
            // Get parameters
            String foodIdStr = request.getParameter("foodId");
            String servingsStr = request.getParameter("servings");
            
            // Validate parameters
            if (foodIdStr == null || foodIdStr.trim().isEmpty()) {
                setSessionMessage(request, "nutritionError", "Vui lòng chọn một món ăn");
                response.sendRedirect(request.getContextPath() + "/member/nutrition");
                return;
            }
            
            if (servingsStr == null || servingsStr.trim().isEmpty()) {
                setSessionMessage(request, "nutritionError", "Vui lòng nhập số khẩu phần");
                response.sendRedirect(request.getContextPath() + "/member/nutrition");
                return;
            }
            
            // Parse parameters
            Long foodId;
            BigDecimal servings;
            
            try {
                foodId = Long.parseLong(foodIdStr.trim());
            } catch (NumberFormatException e) {
                setSessionMessage(request, "nutritionError", "ID món ăn không hợp lệ");
                response.sendRedirect(request.getContextPath() + "/member/nutrition");
                return;
            }
            
            try {
                servings = new BigDecimal(servingsStr.trim());
                if (servings.compareTo(BigDecimal.ZERO) <= 0) {
                    setSessionMessage(request, "nutritionError", "Khẩu phần phải lớn hơn 0");
                    response.sendRedirect(request.getContextPath() + "/member/nutrition");
                    return;
                }
            } catch (NumberFormatException e) {
                setSessionMessage(request, "nutritionError", "Khẩu phần không hợp lệ");
                response.sendRedirect(request.getContextPath() + "/member/nutrition");
                return;
            }
            
            // Call service to add meal
            nutritionService.addMeal(userId, foodId, servings);
            
            // Set success message
            setSessionMessage(request, "nutritionSuccess", "Đã thêm món ăn thành công!");
            
            // Redirect to nutrition page
            response.sendRedirect(request.getContextPath() + "/member/nutrition");
            
        } catch (IllegalArgumentException e) {
            setSessionMessage(request, "nutritionError", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/member/nutrition");
        } catch (Exception e) {
            System.err.println("[NutritionServlet] Error adding meal: " + e.getMessage());
            e.printStackTrace();
            setSessionMessage(request, "nutritionError", "Có lỗi xảy ra khi thêm món ăn. Vui lòng thử lại.");
            response.sendRedirect(request.getContextPath() + "/member/nutrition");
        }
    }

    /**
     * Xóa món ăn
     * Validates input and calls service layer
     */
    private void deleteMeal(HttpServletRequest request, HttpServletResponse response, Long userId)
            throws ServletException, IOException {
        
        try {
            // Get meal ID parameter
            String mealIdStr = request.getParameter("mealId");
            
            if (mealIdStr == null || mealIdStr.trim().isEmpty()) {
                setSessionMessage(request, "nutritionError", "ID món ăn không hợp lệ");
                response.sendRedirect(request.getContextPath() + "/member/nutrition");
                return;
            }
            
            // Parse meal ID
            Long mealId;
            try {
                mealId = Long.parseLong(mealIdStr.trim());
            } catch (NumberFormatException e) {
                setSessionMessage(request, "nutritionError", "ID món ăn không hợp lệ");
                response.sendRedirect(request.getContextPath() + "/member/nutrition");
                return;
            }
            
            // Call service to delete meal
            boolean deleted = nutritionService.deleteMeal(userId, mealId);
            
            if (deleted) {
                setSessionMessage(request, "nutritionSuccess", "Đã xóa món ăn thành công!");
            } else {
                setSessionMessage(request, "nutritionError", "Không thể xóa món ăn. Món ăn không tồn tại hoặc không thuộc về bạn.");
            }
            
            // Redirect to nutrition page
            response.sendRedirect(request.getContextPath() + "/member/nutrition");
            
        } catch (Exception e) {
            System.err.println("[NutritionServlet] Error deleting meal: " + e.getMessage());
            e.printStackTrace();
            setSessionMessage(request, "nutritionError", "Có lỗi xảy ra khi xóa món ăn. Vui lòng thử lại.");
            response.sendRedirect(request.getContextPath() + "/member/nutrition");
        }
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Set message in session for display in JSP
     */
    private void setSessionMessage(HttpServletRequest request, String attributeName, String message) {
        HttpSession session = request.getSession();
        session.setAttribute(attributeName, message);
    }

    /**
     * Handle errors and forward to error page or redirect with error message
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response, Exception e, String userMessage)
            throws ServletException, IOException {
        System.err.println("[NutritionServlet] Error: " + e.getMessage());
        e.printStackTrace();
        
        HttpSession session = request.getSession();
        session.setAttribute("nutritionError", userMessage);
        
        // Redirect to nutrition page
        response.sendRedirect(request.getContextPath() + "/member/nutrition");
    }
}

