package com.gym.dao.nutrition;

import com.gym.dao.BaseDAO;
import com.gym.model.DailyIntakeDTO;
import com.gym.model.Food;
import com.gym.model.UserMeal;
import com.gym.util.JPAUtil;
import jakarta.persistence.EntityManager;
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
 * Handles database operations for user_meals table
 */
public class UserMealDao extends BaseDAO<UserMeal> {

    public UserMealDao() {
        super();
    }

    /**
     * Insert a meal snapshot for a user
     * Gets calories and protein_g from foods table, inserts into user_meals
     * @param userId user ID
     * @param foodId food ID
     * @param servings number of servings
     * @param eatenAt time when the meal was eaten (OffsetDateTime with timezone)
     */
    public void insertSnapshot(long userId, long foodId, BigDecimal servings, OffsetDateTime eatenAt) {
        System.out.println("[UserMealDao] insertSnapshot called - userId: " + userId + ", foodId: " + foodId + ", servings: " + servings);
        
        try {
            // Ensure EntityManager is open
            if (em == null || !em.isOpen()) {
                System.err.println("[UserMealDao] EntityManager is null or closed, creating new one");
                em = getEntityManagerFactory().createEntityManager();
            }
            
            // Check if transaction is already active
            if (em.getTransaction().isActive()) {
                System.err.println("[UserMealDao] WARNING: Transaction already active, rolling back");
                em.getTransaction().rollback();
            }
            
            beginTransaction();
            System.out.println("[UserMealDao] Transaction started");
            
            // Get food info from foods table
            Food food = em.find(Food.class, foodId);
            if (food == null) {
                rollbackTransaction();
                System.err.println("[UserMealDao] Food not found: " + foodId);
                throw new RuntimeException("Food not found: " + foodId);
            }
            
            if (!food.getIsActive()) {
                rollbackTransaction();
                System.err.println("[UserMealDao] Food is inactive: " + foodId);
                throw new RuntimeException("Food is inactive: " + foodId);
            }
            
            System.out.println("[UserMealDao] Food found: " + food.getName() + ", Active: " + food.getIsActive());
            
            // Get food nutrition info - handle null values
            BigDecimal snapCalories = food.getCalories() != null ? food.getCalories() : BigDecimal.ZERO;
            BigDecimal snapProteinG = food.getProteinG() != null ? food.getProteinG() : BigDecimal.ZERO;
            BigDecimal snapCarbsG = food.getCarbsG() != null ? food.getCarbsG() : BigDecimal.ZERO;
            BigDecimal snapFatG = food.getFatG() != null ? food.getFatG() : BigDecimal.ZERO;
            
            System.out.println("[UserMealDao] Food nutrition - Calories: " + snapCalories + ", Protein: " + snapProteinG);
            
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
            // DO NOT set these values - MySQL will calculate them automatically
            // The @Column(insertable = false, updatable = false) annotation prevents JPA from trying to insert them
            
            // Debug logging
            ZoneId vnZone = ZoneId.of("Asia/Ho_Chi_Minh");
            ZonedDateTime vnTime = eatenAt != null ? eatenAt.atZoneSameInstant(vnZone) : ZonedDateTime.now(vnZone);
            System.out.println("[UserMealDao] Inserting meal - VN time: " + vnTime + ", UTC time: " + eatenAt);
            System.out.println("[UserMealDao] VN date: " + vnTime.toLocalDate());
            System.out.println("[UserMealDao] Meal snap values - Calories: " + snapCalories + " * " + servings + " = " + servings.multiply(snapCalories));
            System.out.println("[UserMealDao] Meal snap values - Protein: " + snapProteinG + " * " + servings + " = " + servings.multiply(snapProteinG));
            
            em.persist(meal);
            System.out.println("[UserMealDao] Meal persisted, flushing...");
            em.flush(); // Force insert to happen immediately
            
            // Refresh entity to get generated column values from database
            em.refresh(meal);
            commitTransaction();
            
            System.out.println("[UserMealDao] Meal snapshot inserted successfully for user " + userId + ", meal ID: " + meal.getId());
            System.out.println("[UserMealDao] Generated totals (from DB) - Calories: " + meal.getTotalCalories() + ", Protein: " + meal.getTotalProteinG());
        } catch (Exception e) {
            rollbackTransaction();
            System.err.println("[UserMealDao] Error inserting meal snapshot: " + e.getMessage());
            System.err.println("[UserMealDao] Exception type: " + e.getClass().getName());
            e.printStackTrace();
            throw new RuntimeException("Failed to insert meal snapshot: " + e.getMessage(), e);
        }
    }

    /**
     * List meals of a specific day for a user
     * @param userId user ID
     * @param localDateVN date in Vietnam timezone (Asia/Ho_Chi_Minh)
     * @return list of meals for that day
     */
    public List<UserMeal> listMealsOfDay(long userId, LocalDate localDateVN) {
        EntityManager em = null;
        try {
            em = JPAUtil.createEntityManager();
            
            // Convert VN date to UTC date range for query
            ZoneId vnZone = ZoneId.of("Asia/Ho_Chi_Minh");
            ZonedDateTime startOfDayVN = localDateVN.atStartOfDay(vnZone);
            ZonedDateTime endOfDayVN = localDateVN.plusDays(1).atStartOfDay(vnZone);
            
            LocalDateTime startUTC = startOfDayVN.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
            LocalDateTime endUTC = endOfDayVN.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
            
            // Debug logging
            System.out.println("[UserMealDao] Querying meals for date (VN): " + localDateVN);
            System.out.println("[UserMealDao] UTC range: " + startUTC + " to " + endUTC);
            System.out.println("[UserMealDao] User ID: " + userId);
            
            // Use JPQL query to get UserMeal entities, then fetch food names separately
            List<UserMeal> meals = queryMealsWithFoodNames(em, userId, startUTC, endUTC);
            
            System.out.println("[UserMealDao] Total meals found: " + meals.size());
            if (!meals.isEmpty() && meals.get(0).getEatenAt() != null) {
                OffsetDateTime firstEatenAt = meals.get(0).getEatenAt().atOffset(ZoneOffset.UTC);
                ZonedDateTime mealVN = firstEatenAt.atZoneSameInstant(vnZone);
                System.out.println("[UserMealDao] First meal found - VN time: " + mealVN + ", VN date: " + mealVN.toLocalDate());
                System.out.println("[UserMealDao] First meal - Food: " + meals.get(0).getFoodName() + ", Servings: " + meals.get(0).getServings());
            }
            
            return meals;
        } catch (Exception e) {
            System.err.println("[UserMealDao] Error listing meals of day: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    /**
     * Helper method to query meals with food names
     */
    private List<UserMeal> queryMealsWithFoodNames(EntityManager em, long userId, LocalDateTime startUTC, LocalDateTime endUTC) {
        // Query UserMeal entities
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
     * @param userId user ID
     * @param localDateVN date in Vietnam timezone
     * @return DailyIntakeDTO with totals
     */
    public DailyIntakeDTO sumOfDay(long userId, LocalDate localDateVN) {
        EntityManager em = null;
        try {
            em = JPAUtil.createEntityManager();
            
            // Convert VN date to UTC date range for query
            ZoneId vnZone = ZoneId.of("Asia/Ho_Chi_Minh");
            ZonedDateTime startOfDayVN = localDateVN.atStartOfDay(vnZone);
            ZonedDateTime endOfDayVN = localDateVN.plusDays(1).atStartOfDay(vnZone);
            
            LocalDateTime startUTC = startOfDayVN.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
            LocalDateTime endUTC = endOfDayVN.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
            
            // Native SQL query using EntityManager
            String sql = "SELECT " +
                        "SUM(total_calories) as total_calories, " +
                        "SUM(total_protein_g) as total_protein_g, " +
                        "SUM(total_carbs_g) as total_carbs_g, " +
                        "SUM(total_fat_g) as total_fat_g " +
                        "FROM user_meals " +
                        "WHERE user_id = ? " +
                        "AND eaten_at >= ? " +
                        "AND eaten_at < ?";
            
            // Debug logging
            System.out.println("[UserMealDao] Querying totals for date (VN): " + localDateVN);
            System.out.println("[UserMealDao] UTC range: " + startUTC + " to " + endUTC);
            System.out.println("[UserMealDao] User ID: " + userId);
            
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
                    
                    System.out.println("[UserMealDao] Totals found - Calories: " + totalCalories + ", Protein: " + totalProtein);
                    
                    return new DailyIntakeDTO(totalCalories, totalProtein, totalCarbs, totalFat);
                } else {
                    System.out.println("[UserMealDao] No totals found for date (result array invalid)");
                }
            } catch (jakarta.persistence.NoResultException e) {
                System.out.println("[UserMealDao] No results found for date");
            }
        } catch (Exception e) {
            System.err.println("[UserMealDao] Error summing day totals: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        
        return new DailyIntakeDTO(); // Return zero totals
    }

    /**
     * Get meal history for a date range
     * @param userId user ID
     * @param startDate start date (inclusive) in Vietnam timezone
     * @param endDate end date (exclusive) in Vietnam timezone
     * @return list of meals in the date range
     */
    public List<UserMeal> getMealHistory(long userId, LocalDate startDate, LocalDate endDate) {
        EntityManager em = null;
        try {
            em = JPAUtil.createEntityManager();
            
            // Convert VN dates to UTC date range for query
            ZoneId vnZone = ZoneId.of("Asia/Ho_Chi_Minh");
            ZonedDateTime startOfDayVN = startDate.atStartOfDay(vnZone);
            ZonedDateTime endOfDayVN = endDate.atStartOfDay(vnZone);
            
            LocalDateTime startUTC = startOfDayVN.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
            LocalDateTime endUTC = endOfDayVN.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
            
            // Query UserMeal entities
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
        } catch (Exception e) {
            System.err.println("[UserMealDao] Error getting meal history: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Get daily totals for a date range
     * @param userId user ID
     * @param startDate start date (inclusive) in Vietnam timezone
     * @param endDate end date (exclusive) in Vietnam timezone
     * @return map of date (LocalDate) to DailyIntakeDTO
     */
    public Map<LocalDate, DailyIntakeDTO> getDailyTotalsHistory(long userId, LocalDate startDate, LocalDate endDate) {
        EntityManager em = null;
        try {
            em = JPAUtil.createEntityManager();
            
            // Convert VN dates to UTC date range for query
            ZoneId vnZone = ZoneId.of("Asia/Ho_Chi_Minh");
            ZonedDateTime startOfDayVN = startDate.atStartOfDay(vnZone);
            ZonedDateTime endOfDayVN = endDate.atStartOfDay(vnZone);
            
            LocalDateTime startUTC = startOfDayVN.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
            LocalDateTime endUTC = endOfDayVN.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
            
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
                    // Convert to VN timezone and get date
                    OffsetDateTime eatenAtOffset = meal.getEatenAt().atOffset(ZoneOffset.UTC);
                    LocalDate localDate = eatenAtOffset.atZoneSameInstant(vnZone).toLocalDate();
                    
                    BigDecimal totalCalories = meal.getTotalCalories();
                    BigDecimal totalProtein = meal.getTotalProteinG();
                    BigDecimal totalCarbs = meal.getTotalCarbsG();
                    BigDecimal totalFat = meal.getTotalFatG();
                    
                    // Sum up values for each date
                    DailyIntakeDTO existingTotals = dailyTotals.get(localDate);
                    if (existingTotals == null) {
                        dailyTotals.put(localDate, new DailyIntakeDTO(
                            totalCalories != null ? totalCalories : BigDecimal.ZERO,
                            totalProtein != null ? totalProtein : BigDecimal.ZERO,
                            totalCarbs != null ? totalCarbs : BigDecimal.ZERO,
                            totalFat != null ? totalFat : BigDecimal.ZERO
                        ));
                    } else {
                        // Add to existing totals
                        existingTotals.setCaloriesKcal(
                            existingTotals.getCaloriesKcal().add(totalCalories != null ? totalCalories : BigDecimal.ZERO)
                        );
                        existingTotals.setProteinG(
                            existingTotals.getProteinG().add(totalProtein != null ? totalProtein : BigDecimal.ZERO)
                        );
                        existingTotals.setCarbsG(
                            existingTotals.getCarbsG().add(totalCarbs != null ? totalCarbs : BigDecimal.ZERO)
                        );
                        existingTotals.setFatG(
                            existingTotals.getFatG().add(totalFat != null ? totalFat : BigDecimal.ZERO)
                        );
                    }
                }
            }
            
            return dailyTotals;
        } catch (Exception e) {
            System.err.println("[UserMealDao] Error getting daily totals history: " + e.getMessage());
            e.printStackTrace();
            return new LinkedHashMap<>();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Delete a meal entry by ID
     * @param mealId meal ID
     * @param userId user ID (for security check)
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteMeal(long mealId, long userId) {
        EntityManager em = null;
        try {
            em = JPAUtil.createEntityManager();
            em.getTransaction().begin();
            
            // Find meal and verify it belongs to the user
            UserMeal meal = em.find(UserMeal.class, mealId);
            if (meal == null || !meal.getUserId().equals(userId)) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                return false;
            }
            
            em.remove(meal);
            em.getTransaction().commit();
            
            return true;
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("[UserMealDao] Error deleting meal: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
