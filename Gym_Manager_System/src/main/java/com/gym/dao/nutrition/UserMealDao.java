package com.gym.dao.nutrition;

import com.gym.model.DailyIntakeDTO;
import com.gym.model.UserMeal;
import com.gym.util.DatabaseUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for UserMeal entity
 * Handles database operations for user_meals table
 */
public class UserMealDao {

    /**
     * Insert a meal snapshot for a user
     * Gets calories and protein_g from foods table, inserts into user_meals
     * @param userId user ID
     * @param foodId food ID
     * @param servings number of servings
     * @param eatenAt time when the meal was eaten (OffsetDateTime with timezone)
     */
    public void insertSnapshot(long userId, long foodId, BigDecimal servings, OffsetDateTime eatenAt) {
        // First, get food info from foods table
        String getFoodSql = "SELECT calories, protein_g, carbs_g, fat_g FROM foods WHERE id = ? AND is_active = 1";
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                System.err.println("Error: Database connection is null in insertSnapshot");
                throw new SQLException("Database connection is null");
            }
            
            // Get food nutrition info
            BigDecimal snapCalories = null;
            BigDecimal snapProteinG = null;
            BigDecimal snapCarbsG = null;
            BigDecimal snapFatG = null;
            
            try (PreparedStatement getFoodStmt = conn.prepareStatement(getFoodSql)) {
                getFoodStmt.setLong(1, foodId);
                ResultSet rs = getFoodStmt.executeQuery();
                
                if (rs.next()) {
                    snapCalories = rs.getBigDecimal("calories");
                    snapProteinG = rs.getBigDecimal("protein_g");
                    snapCarbsG = rs.getBigDecimal("carbs_g");
                    snapFatG = rs.getBigDecimal("fat_g");
                } else {
                    throw new SQLException("Food not found or inactive: " + foodId);
                }
            }
            
            // Insert meal snapshot
            String insertSql = "INSERT INTO user_meals (user_id, food_id, servings, eaten_at, " +
                             "snap_calories, snap_protein_g, snap_carbs_g, snap_fat_g) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setLong(1, userId);
                insertStmt.setLong(2, foodId);
                insertStmt.setBigDecimal(3, servings);
                
                // Convert OffsetDateTime to MySQL DATETIME
                // MySQL can handle Timestamp directly
                insertStmt.setTimestamp(4, Timestamp.from(eatenAt.toInstant()));
                
                insertStmt.setBigDecimal(5, snapCalories);
                insertStmt.setBigDecimal(6, snapProteinG);
                insertStmt.setBigDecimal(7, snapCarbsG);
                insertStmt.setBigDecimal(8, snapFatG);
                
                // Debug logging
                ZoneId vnZone = ZoneId.of("Asia/Ho_Chi_Minh");
                java.time.ZonedDateTime vnTime = eatenAt.atZoneSameInstant(vnZone);
                System.out.println("[UserMealDao] Inserting meal - VN time: " + vnTime + ", UTC time: " + eatenAt);
                System.out.println("[UserMealDao] VN date: " + vnTime.toLocalDate());
                
                int affectedRows = insertStmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Failed to insert meal snapshot");
                }
                
                System.out.println("[UserMealDao] Meal snapshot inserted successfully for user " + userId);
            }
        } catch (SQLException e) {
            System.err.println("Error inserting meal snapshot: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to insert meal snapshot", e);
        }
    }

    /**
     * List meals of a specific day for a user
     * @param userId user ID
     * @param localDateVN date in Vietnam timezone (Asia/Ho_Chi_Minh)
     * @return list of meals for that day
     */
    public List<UserMeal> listMealsOfDay(long userId, LocalDate localDateVN) {
        List<UserMeal> meals = new ArrayList<>();
        
        // Convert VN date to UTC date range for query
        ZoneId vnZone = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime startOfDayVN = localDateVN.atStartOfDay(vnZone);
        ZonedDateTime endOfDayVN = localDateVN.plusDays(1).atStartOfDay(vnZone);
        
        OffsetDateTime startUTC = startOfDayVN.withZoneSameInstant(ZoneId.of("UTC")).toOffsetDateTime();
        OffsetDateTime endUTC = endOfDayVN.withZoneSameInstant(ZoneId.of("UTC")).toOffsetDateTime();
        
        String sql = "SELECT um.id, um.user_id, um.food_id, um.meal_type, um.eaten_at, " +
                    "um.servings, um.snap_calories, um.snap_protein_g, um.snap_carbs_g, um.snap_fat_g, " +
                    "um.total_calories, um.total_protein_g, um.total_carbs_g, um.total_fat_g, " +
                    "f.name as food_name " +
                    "FROM user_meals um " +
                    "JOIN foods f ON um.food_id = f.id " +
                    "WHERE um.user_id = ? AND um.eaten_at >= ? AND um.eaten_at < ? " +
                    "ORDER BY um.eaten_at DESC";
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                System.err.println("Error: Database connection is null in listMealsOfDay");
                return meals;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, userId);
                // MySQL can handle Timestamp directly
                stmt.setTimestamp(2, Timestamp.from(startUTC.toInstant()));
                stmt.setTimestamp(3, Timestamp.from(endUTC.toInstant()));
                
                // Debug logging
                System.out.println("[UserMealDao] Querying meals for date (VN): " + localDateVN);
                System.out.println("[UserMealDao] UTC range: " + startUTC + " to " + endUTC);
                System.out.println("[UserMealDao] User ID: " + userId);
                
                ResultSet rs = stmt.executeQuery();
                int count = 0;
                while (rs.next()) {
                    count++;
                    UserMeal meal = mapResultSetToUserMeal(rs);
                    meals.add(meal);
                    
                    // Debug: log first meal
                    if (count == 1 && meal.getEatenAt() != null) {
                        java.time.ZonedDateTime mealVN = meal.getEatenAt().atZoneSameInstant(vnZone);
                        System.out.println("[UserMealDao] First meal found - VN time: " + mealVN + ", VN date: " + mealVN.toLocalDate());
                        System.out.println("[UserMealDao] First meal - Food: " + meal.getFoodName() + ", Servings: " + meal.getServings());
                    }
                }
                System.out.println("[UserMealDao] Total meals found: " + count);
            }
        } catch (SQLException e) {
            System.err.println("Error listing meals of day: " + e.getMessage());
            e.printStackTrace();
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
        // Convert VN date to UTC date range for query
        ZoneId vnZone = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime startOfDayVN = localDateVN.atStartOfDay(vnZone);
        ZonedDateTime endOfDayVN = localDateVN.plusDays(1).atStartOfDay(vnZone);
        
        OffsetDateTime startUTC = startOfDayVN.withZoneSameInstant(ZoneId.of("UTC")).toOffsetDateTime();
        OffsetDateTime endUTC = endOfDayVN.withZoneSameInstant(ZoneId.of("UTC")).toOffsetDateTime();
        
        // Query using UTC range - MySQL DATETIME comparison
        String sql = "SELECT " +
                    "SUM(total_calories) as total_calories, " +
                    "SUM(total_protein_g) as total_protein_g, " +
                    "SUM(total_carbs_g) as total_carbs_g, " +
                    "SUM(total_fat_g) as total_fat_g " +
                    "FROM user_meals " +
                    "WHERE user_id = ? " +
                    "AND eaten_at >= ? " +
                    "AND eaten_at < ?";
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                System.err.println("Error: Database connection is null in sumOfDay");
                return new DailyIntakeDTO(); // Return zero totals
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, userId);
                // MySQL can handle Timestamp directly
                stmt.setTimestamp(2, Timestamp.from(startUTC.toInstant()));
                stmt.setTimestamp(3, Timestamp.from(endUTC.toInstant()));
                
                // Debug logging
                System.out.println("[UserMealDao] Querying totals for date (VN): " + localDateVN);
                System.out.println("[UserMealDao] UTC range: " + startUTC + " to " + endUTC);
                System.out.println("[UserMealDao] User ID: " + userId);
                
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    BigDecimal totalCalories = rs.getBigDecimal("total_calories");
                    BigDecimal totalProtein = rs.getBigDecimal("total_protein_g");
                    BigDecimal totalCarbs = rs.getBigDecimal("total_carbs_g");
                    BigDecimal totalFat = rs.getBigDecimal("total_fat_g");
                    
                    System.out.println("[UserMealDao] Totals found - Calories: " + totalCalories + ", Protein: " + totalProtein);
                    
                    return new DailyIntakeDTO(
                        totalCalories != null ? totalCalories : BigDecimal.ZERO,
                        totalProtein != null ? totalProtein : BigDecimal.ZERO,
                        totalCarbs != null ? totalCarbs : BigDecimal.ZERO,
                        totalFat != null ? totalFat : BigDecimal.ZERO
                    );
                } else {
                    System.out.println("[UserMealDao] No totals found for date");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error summing day totals: " + e.getMessage());
            e.printStackTrace();
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
        List<UserMeal> meals = new ArrayList<>();
        
        // Convert VN dates to UTC date range for query
        ZoneId vnZone = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime startOfDayVN = startDate.atStartOfDay(vnZone);
        ZonedDateTime endOfDayVN = endDate.atStartOfDay(vnZone);
        
        OffsetDateTime startUTC = startOfDayVN.withZoneSameInstant(ZoneId.of("UTC")).toOffsetDateTime();
        OffsetDateTime endUTC = endOfDayVN.withZoneSameInstant(ZoneId.of("UTC")).toOffsetDateTime();
        
        String sql = "SELECT um.id, um.user_id, um.food_id, um.meal_type, um.eaten_at, " +
                    "um.servings, um.snap_calories, um.snap_protein_g, um.snap_carbs_g, um.snap_fat_g, " +
                    "um.total_calories, um.total_protein_g, um.total_carbs_g, um.total_fat_g, " +
                    "f.name as food_name " +
                    "FROM user_meals um " +
                    "JOIN foods f ON um.food_id = f.id " +
                    "WHERE um.user_id = ? AND um.eaten_at >= ? AND um.eaten_at < ? " +
                    "ORDER BY um.eaten_at DESC";
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                System.err.println("Error: Database connection is null in getMealHistory");
                return meals;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, userId);
                stmt.setTimestamp(2, Timestamp.from(startUTC.toInstant()));
                stmt.setTimestamp(3, Timestamp.from(endUTC.toInstant()));
                
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    meals.add(mapResultSetToUserMeal(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting meal history: " + e.getMessage());
            e.printStackTrace();
        }
        
        return meals;
    }

    /**
     * Get daily totals for a date range
     * @param userId user ID
     * @param startDate start date (inclusive) in Vietnam timezone
     * @param endDate end date (exclusive) in Vietnam timezone
     * @return map of date (LocalDate) to DailyIntakeDTO
     */
    public java.util.Map<LocalDate, DailyIntakeDTO> getDailyTotalsHistory(long userId, LocalDate startDate, LocalDate endDate) {
        java.util.Map<LocalDate, DailyIntakeDTO> dailyTotals = new java.util.LinkedHashMap<>();
        
        // Convert VN dates to UTC date range for query
        ZoneId vnZone = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime startOfDayVN = startDate.atStartOfDay(vnZone);
        ZonedDateTime endOfDayVN = endDate.atStartOfDay(vnZone);
        
        OffsetDateTime startUTC = startOfDayVN.withZoneSameInstant(ZoneId.of("UTC")).toOffsetDateTime();
        OffsetDateTime endUTC = endOfDayVN.withZoneSameInstant(ZoneId.of("UTC")).toOffsetDateTime();
        
        // Convert each meal's eaten_at from UTC to VN timezone and group by date
        // We'll do the grouping in Java instead of SQL for better compatibility
        String sql = "SELECT um.eaten_at, " +
                    "um.total_calories, um.total_protein_g, um.total_carbs_g, um.total_fat_g " +
                    "FROM user_meals um " +
                    "WHERE um.user_id = ? AND um.eaten_at >= ? AND um.eaten_at < ? " +
                    "ORDER BY um.eaten_at DESC";
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                System.err.println("Error: Database connection is null in getDailyTotalsHistory");
                return dailyTotals;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, userId);
                stmt.setTimestamp(2, Timestamp.from(startUTC.toInstant()));
                stmt.setTimestamp(3, Timestamp.from(endUTC.toInstant()));
                
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    // Get eaten_at as OffsetDateTime
                    Object eatenAtObj = rs.getObject("eaten_at");
                    OffsetDateTime eatenAt = null;
                    
                    if (eatenAtObj instanceof OffsetDateTime) {
                        eatenAt = (OffsetDateTime) eatenAtObj;
                    } else if (eatenAtObj instanceof Timestamp) {
                        Timestamp ts = (Timestamp) eatenAtObj;
                        eatenAt = OffsetDateTime.ofInstant(ts.toInstant(), java.time.ZoneId.of("UTC"));
                    }
                    
                    if (eatenAt != null) {
                        // Convert to VN timezone and get date
                        LocalDate localDate = eatenAt.atZoneSameInstant(vnZone).toLocalDate();
                        
                        BigDecimal totalCalories = rs.getBigDecimal("total_calories");
                        BigDecimal totalProtein = rs.getBigDecimal("total_protein_g");
                        BigDecimal totalCarbs = rs.getBigDecimal("total_carbs_g");
                        BigDecimal totalFat = rs.getBigDecimal("total_fat_g");
                        
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
            }
        } catch (SQLException e) {
            System.err.println("Error getting daily totals history: " + e.getMessage());
            e.printStackTrace();
        }
        
        return dailyTotals;
    }

    /**
     * Delete a meal entry by ID
     * @param mealId meal ID
     * @param userId user ID (for security check)
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteMeal(long mealId, long userId) {
        String sql = "DELETE FROM user_meals WHERE id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                System.err.println("Error: Database connection is null in deleteMeal");
                return false;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, mealId);
                stmt.setLong(2, userId);
                
                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting meal: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Helper method to map ResultSet to UserMeal object
     */
    private UserMeal mapResultSetToUserMeal(ResultSet rs) throws SQLException {
        UserMeal meal = new UserMeal();
        meal.setId(rs.getLong("id"));
        meal.setUserId(rs.getLong("user_id"));
        meal.setFoodId(rs.getLong("food_id"));
        meal.setFoodName(rs.getString("food_name"));
        meal.setMealType(rs.getString("meal_type"));
        
        // Handle DATETIME to OffsetDateTime
        // MySQL JDBC driver supports getTimestamp for DATETIME
        try {
            Object eatenAtObj = rs.getObject("eaten_at");
            if (eatenAtObj instanceof OffsetDateTime) {
                meal.setEatenAt((OffsetDateTime) eatenAtObj);
            } else if (eatenAtObj instanceof Timestamp) {
                Timestamp ts = (Timestamp) eatenAtObj;
                meal.setEatenAt(OffsetDateTime.ofInstant(ts.toInstant(), java.time.ZoneId.of("UTC")));
            }
        } catch (Exception e) {
            // Fallback: try Timestamp
            Timestamp eatenAtTimestamp = rs.getTimestamp("eaten_at");
            if (eatenAtTimestamp != null) {
                meal.setEatenAt(OffsetDateTime.ofInstant(eatenAtTimestamp.toInstant(), 
                    java.time.ZoneId.of("UTC")));
            }
        }
        
        meal.setServings(rs.getBigDecimal("servings"));
        meal.setSnapCalories(rs.getBigDecimal("snap_calories"));
        meal.setSnapProteinG(rs.getBigDecimal("snap_protein_g"));
        meal.setSnapCarbsG(rs.getBigDecimal("snap_carbs_g"));
        meal.setSnapFatG(rs.getBigDecimal("snap_fat_g"));
        meal.setTotalCalories(rs.getBigDecimal("total_calories"));
        meal.setTotalProteinG(rs.getBigDecimal("total_protein_g"));
        meal.setTotalCarbsG(rs.getBigDecimal("total_carbs_g"));
        meal.setTotalFatG(rs.getBigDecimal("total_fat_g"));
        
        return meal;
    }
}

