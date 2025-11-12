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
     * Add a meal for a member
     * @param memberId member ID
     * @param foodId food ID
     * @param servings number of servings
     */
    void addMeal(int memberId, long foodId, BigDecimal servings);
    
    /**
     * Get today's meals for a member (using Vietnam timezone)
     * @param memberId member ID
     * @return list of meals eaten today
     */
    List<UserMeal> todayMeals(int memberId);
    
    /**
     * Get today's nutrition totals for a member (using Vietnam timezone)
     * @param memberId member ID
     * @return daily intake totals
     */
    DailyIntakeDTO todayTotals(int memberId);
    
    /**
     * Delete a meal entry
     * @param memberId member ID
     * @param mealId meal ID
     * @return true if deleted successfully, false otherwise
     */
    boolean deleteMeal(int memberId, long mealId);
    
    /**
     * Get meal history for a date range
     * @param memberId member ID
     * @param days number of days to look back (e.g., 7 for last 7 days)
     * @return list of meals in the date range
     */
    List<UserMeal> getMealHistory(int memberId, int days);
    
    /**
     * Get daily totals history for a date range
     * @param memberId member ID
     * @param days number of days to look back
     * @return map of date to daily totals
     */
    java.util.Map<java.time.LocalDate, DailyIntakeDTO> getDailyTotalsHistory(int memberId, int days);
    
    /**
     * Get nutrition goal for a member
     * @param memberId member ID
     * @return Optional containing NutritionGoal if found, empty otherwise
     */
    java.util.Optional<model.NutritionGoal> getNutritionGoal(int memberId);
    
    /**
     * Get meals for a specific date
     * @param memberId member ID
     * @param date date to get meals for (in format yyyy-MM-dd or LocalDate)
     * @return list of meals for that date
     */
    List<UserMeal> getMealsByDate(int memberId, java.time.LocalDate date);
    
    /**
     * Get daily totals for a specific date
     * @param memberId member ID
     * @param date date to get totals for
     * @return daily intake totals
     */
    DailyIntakeDTO getDailyTotalsByDate(int memberId, java.time.LocalDate date);
    
    /**
     * Calculate and save nutrition goal based on member's body metrics and goals
     * @param memberId member ID
     * @param weight current weight in kg
     * @param height height in cm
     * @param age age in years
     * @param gender gender (M/F)
     * @param goalType weight goal type (giam_can, tang_can, giu_dang)
     * @param activityLevel activity level (sedentary, light, moderate, active, very_active)
     * @return NutritionGoal with calculated targets
     */
    model.NutritionGoal calculateAndSaveNutritionGoal(int memberId, Float weight, Float height, Integer age, String gender, String goalType, String activityLevel);
}

