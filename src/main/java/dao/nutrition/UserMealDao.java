package dao.nutrition;

import dao.GenericDAO;
import model.DailyIntakeDTO;
import model.Food;
import model.UserMeal;
import jakarta.persistence.TypedQuery;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO for UserMeal entity using JPA
 * Extends GenericDAO for basic CRUD operations
 * Follows DAO pattern and OOP principles
 * Handles database operations for user_meals table
 */
public class UserMealDao extends GenericDAO<UserMeal> {
    
    // Constants for timezone operations
    private static final ZoneId VN_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");

    public UserMealDao() {
        super(UserMeal.class);
    }
    
    /**
     * Helper method to convert VN date to UTC date range
     * Reusable method following DRY principle
     * @param localDateVN date in Vietnam timezone
     * @return array with [startUTC, endUTC]
     */
    private LocalDateTime[] convertVNDateToUTCRange(LocalDate localDateVN) {
        ZonedDateTime startOfDayVN = localDateVN.atStartOfDay(VN_ZONE);
        ZonedDateTime endOfDayVN = localDateVN.plusDays(1).atStartOfDay(VN_ZONE);
        
        LocalDateTime startUTC = startOfDayVN.withZoneSameInstant(UTC_ZONE).toLocalDateTime();
        LocalDateTime endUTC = endOfDayVN.withZoneSameInstant(UTC_ZONE).toLocalDateTime();
        
        return new LocalDateTime[]{startUTC, endUTC};
    }

    /**
     * Insert a meal snapshot for a user
     * Gets nutrition info from foods table, inserts into user_meals
     * Reuses GenericDAO.save() for persistence
     * @param userId user ID
     * @param foodId food ID
     * @param servings number of servings
     * @param eatenAt time when the meal was eaten (OffsetDateTime with timezone)
     */
    public void insertSnapshot(Long userId, Long foodId, BigDecimal servings, OffsetDateTime eatenAt) {
        if (userId == null || foodId == null || servings == null || servings.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid parameters for insertSnapshot");
        }
        
        try {
            beginTransaction();
            
            // Get food info from foods table
            Food food = em.find(Food.class, foodId);
            if (food == null) {
                rollbackTransaction();
                throw new RuntimeException("Food not found: " + foodId);
            }
            
            if (!food.getIsActive()) {
                rollbackTransaction();
                throw new RuntimeException("Food is inactive: " + foodId);
            }
            
            // Get food nutrition info - handle null values
            BigDecimal snapCalories = food.getCalories() != null ? food.getCalories() : BigDecimal.ZERO;
            BigDecimal snapProteinG = food.getProteinG() != null ? food.getProteinG() : BigDecimal.ZERO;
            BigDecimal snapCarbsG = food.getCarbsG() != null ? food.getCarbsG() : BigDecimal.ZERO;
            BigDecimal snapFatG = food.getFatG() != null ? food.getFatG() : BigDecimal.ZERO;
            
            // Create UserMeal entity
            UserMeal meal = new UserMeal();
            meal.setUserId(userId);
            meal.setFoodId(foodId);
            meal.setServings(servings);
            meal.setEatenAt(eatenAt != null ? eatenAt.toLocalDateTime() : LocalDateTime.now());
            meal.setSnapCalories(snapCalories);
            meal.setSnapProteinG(snapProteinG);
            meal.setSnapCarbsG(snapCarbsG);
            meal.setSnapFatG(snapFatG);
            
            // NOTE: Total values (total_calories, total_protein_g, etc.) are GENERATED columns in MySQL
            // They are automatically calculated by MySQL from (servings * snap_*_g)
            // The @Column(insertable = false, updatable = false) annotation prevents JPA from trying to insert them
            
            // Reuse GenericDAO.save() for persistence
            em.persist(meal);
            em.flush(); // Force insert to happen immediately
            em.refresh(meal); // Refresh to get generated column values from database
            
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            System.err.println("[UserMealDao] Error inserting meal snapshot: " + e.getMessage());
            throw new RuntimeException("Failed to insert meal snapshot: " + e.getMessage(), e);
        }
    }

    /**
     * List meals of a specific day for a user
     * Reuses helper methods following DRY principle
     * @param userId user ID
     * @param localDateVN date in Vietnam timezone (Asia/Ho_Chi_Minh)
     * @return list of meals for that day
     */
    public List<UserMeal> listMealsOfDay(Long userId, LocalDate localDateVN) {
        if (userId == null || localDateVN == null) {
            return new ArrayList<>();
        }
        
        try {
            // Convert VN date to UTC date range
            LocalDateTime[] utcRange = convertVNDateToUTCRange(localDateVN);
            LocalDateTime startUTC = utcRange[0];
            LocalDateTime endUTC = utcRange[1];
            
            // Query meals and populate food names
            return queryMealsWithFoodNames(userId, startUTC, endUTC);
        } catch (Exception e) {
            System.err.println("[UserMealDao] Error listing meals of day: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Helper method to query meals with food names
     * Reusable method following DRY principle
     */
    private List<UserMeal> queryMealsWithFoodNames(Long userId, LocalDateTime startUTC, LocalDateTime endUTC) {
        TypedQuery<UserMeal> query = em.createQuery(
            "SELECT um FROM UserMeal um WHERE um.userId = :userId " +
            "AND um.eatenAt >= :startTime AND um.eatenAt < :endTime " +
            "ORDER BY um.eatenAt DESC",
            UserMeal.class
        );
        query.setParameter("userId", userId);
        query.setParameter("startTime", startUTC);
        query.setParameter("endTime", endUTC);
        
        List<UserMeal> meals = query.getResultList();
        
        // Fetch food names separately
        for (UserMeal meal : meals) {
            if (meal.getFoodId() != null) {
                Food food = em.find(Food.class, meal.getFoodId());
                if (food != null) {
                    meal.setFoodName(food.getName());
                }
            }
        }
        
        return meals;
    }

    /**
     * Sum total calories and protein for a specific day
     * Uses native SQL query for performance (aggregation)
     * @param userId user ID
     * @param localDateVN date in Vietnam timezone
     * @return DailyIntakeDTO with totals
     */
    public DailyIntakeDTO sumOfDay(Long userId, LocalDate localDateVN) {
        if (userId == null || localDateVN == null) {
            return new DailyIntakeDTO();
        }
        
        try {
            // Convert VN date to UTC date range
            LocalDateTime[] utcRange = convertVNDateToUTCRange(localDateVN);
            LocalDateTime startUTC = utcRange[0];
            LocalDateTime endUTC = utcRange[1];
            
            // Native SQL query for aggregation
            String sql = "SELECT " +
                        "SUM(total_calories) as total_calories, " +
                        "SUM(total_protein_g) as total_protein_g, " +
                        "SUM(total_carbs_g) as total_carbs_g, " +
                        "SUM(total_fat_g) as total_fat_g " +
                        "FROM user_meals " +
                        "WHERE user_id = ? " +
                        "AND eaten_at >= ? " +
                        "AND eaten_at < ?";
            
            jakarta.persistence.Query query = em.createNativeQuery(sql);
            query.setParameter(1, userId);
            query.setParameter(2, Timestamp.valueOf(startUTC));
            query.setParameter(3, Timestamp.valueOf(endUTC));
            
            try {
                Object[] result = (Object[]) query.getSingleResult();
                
                if (result != null && result.length >= 4) {
                    BigDecimal totalCalories = result[0] != null ? (BigDecimal) result[0] : BigDecimal.ZERO;
                    BigDecimal totalProtein = result[1] != null ? (BigDecimal) result[1] : BigDecimal.ZERO;
                    BigDecimal totalCarbs = result[2] != null ? (BigDecimal) result[2] : BigDecimal.ZERO;
                    BigDecimal totalFat = result[3] != null ? (BigDecimal) result[3] : BigDecimal.ZERO;
                    
                    return new DailyIntakeDTO(totalCalories, totalProtein, totalCarbs, totalFat);
                }
            } catch (jakarta.persistence.NoResultException e) {
                // No meals found for this day
            }
        } catch (Exception e) {
            System.err.println("[UserMealDao] Error summing day totals: " + e.getMessage());
        }
        
        return new DailyIntakeDTO(); // Return zero totals
    }

    /**
     * Get meal history for a date range
     * Reuses queryMealsWithFoodNames method following DRY principle
     * @param userId user ID
     * @param startDate start date (inclusive) in Vietnam timezone
     * @param endDate end date (exclusive) in Vietnam timezone
     * @return list of meals in the date range
     */
    public List<UserMeal> getMealHistory(Long userId, LocalDate startDate, LocalDate endDate) {
        if (userId == null || startDate == null || endDate == null) {
            return new ArrayList<>();
        }
        
        try {
            // Convert VN dates to UTC date range
            ZonedDateTime startOfDayVN = startDate.atStartOfDay(VN_ZONE);
            ZonedDateTime endOfDayVN = endDate.atStartOfDay(VN_ZONE);
            
            LocalDateTime startUTC = startOfDayVN.withZoneSameInstant(UTC_ZONE).toLocalDateTime();
            LocalDateTime endUTC = endOfDayVN.withZoneSameInstant(UTC_ZONE).toLocalDateTime();
            
            // Reuse helper method
            return queryMealsWithFoodNames(userId, startUTC, endUTC);
        } catch (Exception e) {
            System.err.println("[UserMealDao] Error getting meal history: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Get daily totals for a date range
     * Groups meals by date and sums nutrition values
     * @param userId user ID
     * @param startDate start date (inclusive) in Vietnam timezone
     * @param endDate end date (exclusive) in Vietnam timezone
     * @return map of date (LocalDate) to DailyIntakeDTO
     */
    public Map<LocalDate, DailyIntakeDTO> getDailyTotalsHistory(Long userId, LocalDate startDate, LocalDate endDate) {
        if (userId == null || startDate == null || endDate == null) {
            return new LinkedHashMap<>();
        }
        
        try {
            // Convert VN dates to UTC date range
            ZonedDateTime startOfDayVN = startDate.atStartOfDay(VN_ZONE);
            ZonedDateTime endOfDayVN = endDate.atStartOfDay(VN_ZONE);
            
            LocalDateTime startUTC = startOfDayVN.withZoneSameInstant(UTC_ZONE).toLocalDateTime();
            LocalDateTime endUTC = endOfDayVN.withZoneSameInstant(UTC_ZONE).toLocalDateTime();
            
            // Query all meals in the date range
            TypedQuery<UserMeal> query = em.createQuery(
                "SELECT um FROM UserMeal um WHERE um.userId = :userId " +
                "AND um.eatenAt >= :startTime AND um.eatenAt < :endTime " +
                "ORDER BY um.eatenAt DESC",
                UserMeal.class
            );
            query.setParameter("userId", userId);
            query.setParameter("startTime", startUTC);
            query.setParameter("endTime", endUTC);
            
            List<UserMeal> meals = query.getResultList();
            
            // Group by date in VN timezone
            Map<LocalDate, DailyIntakeDTO> dailyTotals = new LinkedHashMap<>();
            
            for (UserMeal meal : meals) {
                if (meal.getEatenAt() != null) {
                    // Use UserMeal helper method to get VN date
                    LocalDate localDate = meal.getEatenDateVN();
                    if (localDate == null) {
                        continue;
                    }
                    
                    BigDecimal totalCalories = meal.getTotalCalories() != null ? meal.getTotalCalories() : BigDecimal.ZERO;
                    BigDecimal totalProtein = meal.getTotalProteinG() != null ? meal.getTotalProteinG() : BigDecimal.ZERO;
                    BigDecimal totalCarbs = meal.getTotalCarbsG() != null ? meal.getTotalCarbsG() : BigDecimal.ZERO;
                    BigDecimal totalFat = meal.getTotalFatG() != null ? meal.getTotalFatG() : BigDecimal.ZERO;
                    
                    // Sum up values for each date
                    DailyIntakeDTO existingTotals = dailyTotals.get(localDate);
                    if (existingTotals == null) {
                        dailyTotals.put(localDate, new DailyIntakeDTO(totalCalories, totalProtein, totalCarbs, totalFat));
                    } else {
                        // Add to existing totals
                        existingTotals.setCaloriesKcal(existingTotals.getCaloriesKcal().add(totalCalories));
                        existingTotals.setProteinG(existingTotals.getProteinG().add(totalProtein));
                        existingTotals.setCarbsG(existingTotals.getCarbsG().add(totalCarbs));
                        existingTotals.setFatG(existingTotals.getFatG().add(totalFat));
                    }
                }
            }
            
            return dailyTotals;
        } catch (Exception e) {
            System.err.println("[UserMealDao] Error getting daily totals history: " + e.getMessage());
            return new LinkedHashMap<>();
        }
    }

    /**
     * Delete a meal entry by ID
     * Reuses GenericDAO.delete() for deletion
     * @param mealId meal ID
     * @param userId user ID (for security check)
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteMeal(Long mealId, Long userId) {
        if (mealId == null || userId == null) {
            return false;
        }
        
        try {
            // Find meal and verify it belongs to the user
            UserMeal meal = em.find(UserMeal.class, mealId);
            if (meal == null || !meal.getUserId().equals(userId)) {
                return false;
            }
            
            // Reuse GenericDAO.delete()
            delete(meal);
            return true;
        } catch (Exception e) {
            System.err.println("[UserMealDao] Error deleting meal: " + e.getMessage());
            return false;
        }
    }
}
