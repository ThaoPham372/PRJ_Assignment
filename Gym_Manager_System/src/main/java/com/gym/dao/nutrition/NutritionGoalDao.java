package com.gym.dao.nutrition;

import com.gym.model.NutritionGoal;
import com.gym.util.DatabaseUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * DAO for Nutrition Goal entity
 * Handles database operations for user_nutrition_goals table
 */
public class NutritionGoalDao {

    /**
     * Get nutrition goal for a user
     * @param userId user ID
     * @return Optional containing NutritionGoal if found, empty otherwise
     */
    public Optional<NutritionGoal> findByUserId(long userId) {
        String sql = "SELECT user_id, goal_type, activity_factor, daily_calories_target, " +
                    "daily_protein_target, updated_at " +
                    "FROM user_nutrition_goals WHERE user_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                System.err.println("Error: Database connection is null in findByUserId");
                return Optional.empty();
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, userId);
                
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(mapResultSetToGoal(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding nutrition goal: " + e.getMessage());
            e.printStackTrace();
        }
        
        return Optional.empty();
    }

    /**
     * Save or update nutrition goal (UPSERT)
     * @param goal NutritionGoal to save
     * @return true if successful, false otherwise
     */
    public boolean saveOrUpdate(NutritionGoal goal) {
        // Use INSERT ... ON DUPLICATE KEY UPDATE (UPSERT) for MySQL
        String sql = "INSERT INTO user_nutrition_goals " +
                    "(user_id, goal_type, activity_factor, daily_calories_target, " +
                    "daily_protein_target, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, NOW()) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "goal_type = ?, activity_factor = ?, " +
                    "daily_calories_target = ?, daily_protein_target = ?, " +
                    "updated_at = NOW()";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                System.err.println("Error: Database connection is null in saveOrUpdate");
                return false;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                int paramIndex = 1;
                
                // For INSERT
                stmt.setLong(paramIndex++, goal.getUserId());
                stmt.setString(paramIndex++, goal.getGoalType());
                stmt.setBigDecimal(paramIndex++, goal.getActivityFactor());
                if (goal.getDailyCaloriesTarget() != null) {
                    stmt.setBigDecimal(paramIndex++, goal.getDailyCaloriesTarget());
                } else {
                    stmt.setNull(paramIndex++, Types.DECIMAL);
                }
                if (goal.getDailyProteinTarget() != null) {
                    stmt.setBigDecimal(paramIndex++, goal.getDailyProteinTarget());
                } else {
                    stmt.setNull(paramIndex++, Types.DECIMAL);
                }
                
                // For ON DUPLICATE KEY UPDATE
                stmt.setString(paramIndex++, goal.getGoalType());
                stmt.setBigDecimal(paramIndex++, goal.getActivityFactor());
                if (goal.getDailyCaloriesTarget() != null) {
                    stmt.setBigDecimal(paramIndex++, goal.getDailyCaloriesTarget());
                } else {
                    stmt.setNull(paramIndex++, Types.DECIMAL);
                }
                if (goal.getDailyProteinTarget() != null) {
                    stmt.setBigDecimal(paramIndex++, goal.getDailyProteinTarget());
                } else {
                    stmt.setNull(paramIndex++, Types.DECIMAL);
                }
                
                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error saving nutrition goal: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Helper method to map ResultSet to NutritionGoal object
     */
    private NutritionGoal mapResultSetToGoal(ResultSet rs) throws SQLException {
        NutritionGoal goal = new NutritionGoal();
        goal.setUserId(rs.getLong("user_id"));
        goal.setGoalType(rs.getString("goal_type"));
        goal.setActivityFactor(rs.getBigDecimal("activity_factor"));
        goal.setDailyCaloriesTarget(rs.getBigDecimal("daily_calories_target"));
        goal.setDailyProteinTarget(rs.getBigDecimal("daily_protein_target"));
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            goal.setUpdatedAt(OffsetDateTime.ofInstant(updatedAt.toInstant(), 
                java.time.ZoneId.of("UTC")));
        }
        
        return goal;
    }
}



