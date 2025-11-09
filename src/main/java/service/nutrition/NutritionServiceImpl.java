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
    public void addMeal(long userId, long foodId, BigDecimal servings) {
        // Validate inputs
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
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
        userMealDao.insertSnapshot(userId, foodId, servings, eatenAt);
    }

    @Override
    public List<UserMeal> todayMeals(long userId) {
        if (userId <= 0) {
            return Collections.emptyList();
        }
        
        // Get today's date in Vietnam timezone
        LocalDate todayVN = LocalDate.now(VN_ZONE);
        
        return userMealDao.listMealsOfDay(userId, todayVN);
    }

    @Override
    public DailyIntakeDTO todayTotals(long userId) {
        if (userId <= 0) {
            return new DailyIntakeDTO();
        }
        
        // Get today's date in Vietnam timezone
        LocalDate todayVN = LocalDate.now(VN_ZONE);
        
        return userMealDao.sumOfDay(userId, todayVN);
    }

    @Override
    public boolean deleteMeal(long userId, long mealId) {
        if (userId <= 0 || mealId <= 0) {
            return false;
        }
        
        return userMealDao.deleteMeal(mealId, userId);
    }

    @Override
    public List<UserMeal> getMealHistory(long userId, int days) {
        if (userId <= 0 || days <= 0) {
            return Collections.emptyList();
        }
        
        LocalDate endDate = LocalDate.now(VN_ZONE).plusDays(1); // Exclusive
        LocalDate startDate = endDate.minusDays(days);
        
        return userMealDao.getMealHistory(userId, startDate, endDate);
    }

    @Override
    public java.util.Map<java.time.LocalDate, DailyIntakeDTO> getDailyTotalsHistory(long userId, int days) {
        if (userId <= 0 || days <= 0) {
            return Collections.emptyMap();
        }
        
        LocalDate endDate = LocalDate.now(VN_ZONE).plusDays(1); // Exclusive
        LocalDate startDate = endDate.minusDays(days);
        
        return userMealDao.getDailyTotalsHistory(userId, startDate, endDate);
    }

    @Override
    public Optional<NutritionGoal> getNutritionGoal(long userId) {
        if (userId <= 0) {
            return Optional.empty();
        }
        return nutritionGoalDao.findByUserId(userId);
    }
    
    @Override
    public List<UserMeal> getMealsByDate(long userId, LocalDate date) {
        if (userId <= 0 || date == null) {
            return Collections.emptyList();
        }
        
        // Date should already be in VN timezone format
        return userMealDao.listMealsOfDay(userId, date);
    }
    
    @Override
    public DailyIntakeDTO getDailyTotalsByDate(long userId, LocalDate date) {
        if (userId <= 0 || date == null) {
            return new DailyIntakeDTO();
        }
        
        // Date should already be in VN timezone format
        return userMealDao.sumOfDay(userId, date);
    }
}

