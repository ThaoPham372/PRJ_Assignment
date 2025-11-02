package com.gym.dao.nutrition;

import com.gym.model.Food;
import com.gym.util.DatabaseUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO for Food entity
 * Handles database operations for foods table
 */
public class FoodDao {

    /**
     * Search active foods by name (case-insensitive LIKE search)
     * @param keyword search keyword
     * @param limit maximum number of results
     * @return list of active foods matching the keyword
     */
    public List<Food> searchActiveByName(String keyword, int limit) {
        List<Food> foods = new ArrayList<>();
        String sql = "SELECT id, name, serving_label, calories, protein_g, carbs_g, fat_g, " +
                    "is_active, created_at, updated_at " +
                    "FROM foods " +
                    "WHERE is_active = 1 AND name LIKE ? " +
                    "ORDER BY name " +
                    "LIMIT ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                System.err.println("Error: Database connection is null in searchActiveByName");
                return foods;
            }
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, "%" + keyword + "%");
                stmt.setInt(2, limit);
                
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    foods.add(mapResultSetToFood(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching foods: " + e.getMessage());
            e.printStackTrace();
        }
        
        return foods;
    }

    /**
     * Find food by ID
     * @param id food ID
     * @return Optional containing Food if found, empty otherwise
     */
    public Optional<Food> findById(long id) {
        String sql = "SELECT id, name, serving_label, calories, protein_g, carbs_g, fat_g, " +
                    "is_active, created_at, updated_at " +
                    "FROM foods " +
                    "WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                System.err.println("Error: Database connection is null in findById");
                return Optional.empty();
            }
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, id);
                
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(mapResultSetToFood(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding food by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return Optional.empty();
    }

    /**
     * Get default foods to display (popular/recent foods)
     * @param limit maximum number of results
     * @return list of active foods
     */
    public List<Food> getDefaultFoods(int limit) {
        List<Food> foods = new ArrayList<>();
        // Get active foods ordered by name (you can change to order by created_at DESC for recent, or add popularity field)
        String sql = "SELECT id, name, serving_label, calories, protein_g, carbs_g, fat_g, " +
                    "is_active, created_at, updated_at " +
                    "FROM foods " +
                    "WHERE is_active = 1 " +
                    "ORDER BY name " +
                    "LIMIT ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                System.err.println("Error: Database connection is null in getDefaultFoods");
                return foods;
            }
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, limit);
                
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    foods.add(mapResultSetToFood(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting default foods: " + e.getMessage());
            e.printStackTrace();
        }
        
        return foods;
    }

    /**
     * Helper method to map ResultSet to Food object
     */
    private Food mapResultSetToFood(ResultSet rs) throws SQLException {
        Food food = new Food();
        food.setId(rs.getLong("id"));
        food.setName(rs.getString("name"));
        food.setServingLabel(rs.getString("serving_label"));
        food.setCalories(rs.getBigDecimal("calories"));
        food.setProteinG(rs.getBigDecimal("protein_g"));
        food.setCarbsG(rs.getBigDecimal("carbs_g"));
        food.setFatG(rs.getBigDecimal("fat_g"));
        food.setIsActive(rs.getBoolean("is_active"));
        
        // Handle DATETIME2(3) to OffsetDateTime
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            food.setCreatedAt(OffsetDateTime.ofInstant(createdAt.toInstant(), 
                java.time.ZoneId.systemDefault()));
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            food.setUpdatedAt(OffsetDateTime.ofInstant(updatedAt.toInstant(), 
                java.time.ZoneId.systemDefault()));
        }
        
        return food;
    }
}

