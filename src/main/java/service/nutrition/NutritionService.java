package service.nutrition;

import model.DailyIntakeDTO;
import model.Food;
import model.UserMeal;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for Nutrition functionality
 */
public interface NutritionService {
    
    /**
     * Search foods by keyword
     * @param keyword search keyword
     * @param limit maximum number of results
     * @return list of active foods matching the keyword
     */
    List<Food> searchFoods(String keyword, int limit);
    
    /**
     * Get default foods to display when no search keyword is provided
     * @param limit maximum number of results
     * @return list of active foods
     */
    List<Food> getDefaultFoods(int limit);
    
    /**
     * Add a meal for a user
     * @param userId user ID
     * @param foodId food ID
     * @param servings number of servings
     */
    void addMeal(long userId, long foodId, BigDecimal servings);
    
    /**
     * Get today's meals for a user (using Vietnam timezone)
     * @param userId user ID
     * @return list of meals eaten today
     */
    List<UserMeal> todayMeals(long userId);
    
    /**
     * Get today's nutrition totals for a user (using Vietnam timezone)
     * @param userId user ID
     * @return daily intake totals
     */
    DailyIntakeDTO todayTotals(long userId);
    
    /**
     * Delete a meal entry
     * @param userId user ID
     * @param mealId meal ID
     * @return true if deleted successfully, false otherwise
     */
    boolean deleteMeal(long userId, long mealId);
    
    /**
     * Get meal history for a date range
     * @param userId user ID
     * @param days number of days to look back (e.g., 7 for last 7 days)
     * @return list of meals in the date range
     */
    List<UserMeal> getMealHistory(long userId, int days);
    
    /**
     * Get daily totals history for a date range
     * @param userId user ID
     * @param days number of days to look back
     * @return map of date to daily totals
     */
    java.util.Map<java.time.LocalDate, DailyIntakeDTO> getDailyTotalsHistory(long userId, int days);
    
    /**
     * Get nutrition goal for a user
     * @param userId user ID
     * @return Optional containing NutritionGoal if found, empty otherwise
     */
    java.util.Optional<model.NutritionGoal> getNutritionGoal(long userId);
    
    /**
     * Get meals for a specific date
     * @param userId user ID
     * @param date date to get meals for (in format yyyy-MM-dd or LocalDate)
     * @return list of meals for that date
     */
    List<UserMeal> getMealsByDate(long userId, java.time.LocalDate date);
    
    /**
     * Get daily totals for a specific date
     * @param userId user ID
     * @param date date to get totals for
     * @return daily intake totals
     */
    DailyIntakeDTO getDailyTotalsByDate(long userId, java.time.LocalDate date);
    
    /**
     * Calculate and save nutrition goal based on user's body metrics and goals
     * @param userId user ID
     * @param weight current weight in kg
     * @param height height in cm
     * @param age age in years
     * @param gender gender (M/F)
     * @param goalType weight goal type (giam_can, tang_can, giu_dang)
     * @param activityLevel activity level (sedentary, light, moderate, active, very_active)
     * @return NutritionGoal with calculated targets
     */
    model.NutritionGoal calculateAndSaveNutritionGoal(long userId, Float weight, Float height, Integer age, String gender, String goalType, String activityLevel);
}

