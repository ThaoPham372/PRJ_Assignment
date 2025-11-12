package service.nutrition;

import dao.nutrition.FoodDao;
import dao.nutrition.NutritionGoalDao;
import dao.nutrition.UserMealDao;
import model.DailyIntakeDTO;
import model.Food;
import model.NutritionGoal;
import model.UserMeal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of NutritionService
 * Handles business logic for nutrition tracking
 */
public class NutritionServiceImpl implements NutritionService {

    private final FoodDao foodDao;
    private final UserMealDao userMealDao;
    private final NutritionGoalDao nutritionGoalDao;
    private static final ZoneId VN_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    public NutritionServiceImpl() {
        this.foodDao = new FoodDao();
        this.userMealDao = new UserMealDao();
        this.nutritionGoalDao = new NutritionGoalDao();
    }

    @Override
    public List<Food> searchFoods(String keyword, int limit) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return Collections.emptyList();
        }
        
        String trimmedKeyword = keyword.trim();
        if (trimmedKeyword.length() < 1) {
            return Collections.emptyList();
        }
        
        return foodDao.searchActiveByName(trimmedKeyword, limit);
    }

    @Override
    public List<Food> getDefaultFoods(int limit) {
        if (limit <= 0) {
            return Collections.emptyList();
        }
        return foodDao.getDefaultFoods(limit);
    }

    @Override
    public void addMeal(int memberId, long foodId, BigDecimal servings) {
        // Validate inputs
        if (memberId <= 0) {
            throw new IllegalArgumentException("Invalid member ID");
        }
        
        if (foodId <= 0) {
            throw new IllegalArgumentException("Invalid food ID");
        }
        
        if (servings == null || servings.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Servings must be greater than 0");
        }
        
        // Check if food exists and is active
        var foodOpt = foodDao.findById(foodId);
        if (foodOpt.isEmpty()) {
            throw new IllegalArgumentException("Food not found or inactive: " + foodId);
        }
        
        // Get current time in Vietnam timezone, convert to UTC for storage
        ZonedDateTime nowVN = ZonedDateTime.now(VN_ZONE);
        OffsetDateTime eatenAt = nowVN.withZoneSameInstant(ZoneId.of("UTC")).toOffsetDateTime();
        
        // Insert meal snapshot
        userMealDao.insertSnapshot(memberId, foodId, servings, eatenAt);
    }

    @Override
    public List<UserMeal> todayMeals(int memberId) {
        if (memberId <= 0) {
            return Collections.emptyList();
        }
        
        // Get today's date in Vietnam timezone
        LocalDate todayVN = LocalDate.now(VN_ZONE);
        
        return userMealDao.listMealsOfDay(memberId, todayVN);
    }

    @Override
    public DailyIntakeDTO todayTotals(int memberId) {
        if (memberId <= 0) {
            return new DailyIntakeDTO();
        }
        
        // Get today's date in Vietnam timezone
        LocalDate todayVN = LocalDate.now(VN_ZONE);
        
        return userMealDao.sumOfDay(memberId, todayVN);
    }

    @Override
    public boolean deleteMeal(int memberId, long mealId) {
        if (memberId <= 0 || mealId <= 0) {
            return false;
        }
        
        return userMealDao.deleteMeal(mealId, memberId);
    }

    @Override
    public List<UserMeal> getMealHistory(int memberId, int days) {
        if (memberId <= 0 || days <= 0) {
            return Collections.emptyList();
        }
        
        LocalDate endDate = LocalDate.now(VN_ZONE).plusDays(1); // Exclusive
        LocalDate startDate = endDate.minusDays(days);
        
        return userMealDao.getMealHistory(memberId, startDate, endDate);
    }

    @Override
    public java.util.Map<java.time.LocalDate, DailyIntakeDTO> getDailyTotalsHistory(int memberId, int days) {
        if (memberId <= 0 || days <= 0) {
            return Collections.emptyMap();
        }
        
        LocalDate endDate = LocalDate.now(VN_ZONE).plusDays(1); // Exclusive
        LocalDate startDate = endDate.minusDays(days);
        
        return userMealDao.getDailyTotalsHistory(memberId, startDate, endDate);
    }

    @Override
    public Optional<NutritionGoal> getNutritionGoal(int memberId) {
        if (memberId <= 0) {
            return Optional.empty();
        }
        return nutritionGoalDao.findByMemberId(memberId);
    }
    
    @Override
    public List<UserMeal> getMealsByDate(int memberId, LocalDate date) {
        if (memberId <= 0 || date == null) {
            return Collections.emptyList();
        }
        
        // Date should already be in VN timezone format
        return userMealDao.listMealsOfDay(memberId, date);
    }
    
    @Override
    public DailyIntakeDTO getDailyTotalsByDate(int memberId, LocalDate date) {
        if (memberId <= 0 || date == null) {
            return new DailyIntakeDTO();
        }
        
        // Date should already be in VN timezone format
        return userMealDao.sumOfDay(memberId, date);
    }
    
    @Override
    public NutritionGoal calculateAndSaveNutritionGoal(int memberId, Float weight, Float height, Integer age, String gender, String goalType, String activityLevel) {
        if (memberId <= 0 || weight == null || weight <= 0 || height == null || height <= 0) {
            throw new IllegalArgumentException("Invalid member data for nutrition goal calculation");
        }
        
        // Calculate BMR using Mifflin-St Jeor Equation
        BigDecimal bmr = calculateBMR(weight, height, age, gender);
        
        // Get activity factor
        BigDecimal activityFactor = getActivityFactor(activityLevel);
        
        // Calculate TDEE (Total Daily Energy Expenditure)
        BigDecimal tdee = bmr.multiply(activityFactor);
        
        // Adjust TDEE based on goal type
        BigDecimal caloriesTarget = adjustCaloriesForGoal(tdee, goalType);
        
        // Calculate protein target (1.6-2.2g per kg body weight, depending on goal)
        BigDecimal proteinTarget = calculateProteinTarget(weight, goalType);
        
        // Create or update NutritionGoal
        NutritionGoal goal = new NutritionGoal();
        goal.setMemberId(memberId);
        goal.setGoalType(goalType);
        goal.setActivityFactor(activityFactor);
        goal.setDailyCaloriesTarget(caloriesTarget);
        goal.setDailyProteinTarget(proteinTarget);
        
        // Save or update
        nutritionGoalDao.saveOrUpdate(goal);
        
        return goal;
    }
    
    /**
     * Calculate BMR using Mifflin-St Jeor Equation
     * BMR = 10 * weight(kg) + 6.25 * height(cm) - 5 * age(years) + s
     * s = +5 for males, -161 for females
     */
    private BigDecimal calculateBMR(Float weight, Float height, Integer age, String gender) {
        BigDecimal weightBD = BigDecimal.valueOf(weight);
        BigDecimal heightBD = BigDecimal.valueOf(height);
        BigDecimal ageBD = age != null ? BigDecimal.valueOf(age) : BigDecimal.valueOf(30); // Default age 30 if not provided
        
        BigDecimal bmr = weightBD.multiply(new BigDecimal("10"))
                .add(heightBD.multiply(new BigDecimal("6.25")))
                .subtract(ageBD.multiply(new BigDecimal("5")));
        
        // Add gender factor
        if ("M".equalsIgnoreCase(gender) || "male".equalsIgnoreCase(gender)) {
            bmr = bmr.add(new BigDecimal("5"));
        } else {
            bmr = bmr.subtract(new BigDecimal("161"));
        }
        
        return bmr;
    }
    
    /**
     * Get activity factor based on activity level
     */
    private BigDecimal getActivityFactor(String activityLevel) {
        if (activityLevel == null || activityLevel.trim().isEmpty()) {
            return new BigDecimal("1.55"); // Default: moderate
        }
        
        switch (activityLevel.toLowerCase()) {
            case "sedentary":
                return new BigDecimal("1.2"); // Little to no exercise
            case "light":
                return new BigDecimal("1.375"); // Light exercise 1-3 days/week
            case "moderate":
                return new BigDecimal("1.55"); // Moderate exercise 3-5 days/week
            case "active":
                return new BigDecimal("1.725"); // Hard exercise 6-7 days/week
            case "very_active":
                return new BigDecimal("1.9"); // Very hard exercise, physical job
            default:
                return new BigDecimal("1.55"); // Default: moderate
        }
    }
    
    /**
     * Adjust calories based on weight goal
     */
    private BigDecimal adjustCaloriesForGoal(BigDecimal tdee, String goalType) {
        if (goalType == null || goalType.trim().isEmpty()) {
            return tdee; // Maintain weight
        }
        
        switch (goalType.toLowerCase()) {
            case "giam_can":
            case "lose_weight":
                // Deficit of 500 calories per day (lose ~0.5kg per week)
                return tdee.subtract(new BigDecimal("500"));
            case "tang_can":
            case "gain_weight":
            case "gain_muscle":
                // Surplus of 300-500 calories per day
                return tdee.add(new BigDecimal("400"));
            case "giu_dang":
            case "maintain":
            default:
                return tdee; // Maintain weight
        }
    }
    
    /**
     * Calculate protein target based on weight and goal
     */
    private BigDecimal calculateProteinTarget(Float weight, String goalType) {
        BigDecimal weightBD = BigDecimal.valueOf(weight);
        BigDecimal proteinPerKg;
        
        if (goalType != null) {
            switch (goalType.toLowerCase()) {
                case "giam_can":
                case "lose_weight":
                    proteinPerKg = new BigDecimal("2.0"); // Higher protein for weight loss
                    break;
                case "tang_can":
                case "gain_weight":
                case "gain_muscle":
                    proteinPerKg = new BigDecimal("2.2"); // Higher protein for muscle gain
                    break;
                case "giu_dang":
                case "maintain":
                default:
                    proteinPerKg = new BigDecimal("1.6"); // Standard protein
                    break;
            }
        } else {
            proteinPerKg = new BigDecimal("1.6"); // Default
        }
        
        return weightBD.multiply(proteinPerKg);
    }
}

