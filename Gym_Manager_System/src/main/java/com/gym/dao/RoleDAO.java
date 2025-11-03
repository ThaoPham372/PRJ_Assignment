package com.gym.dao;

import com.gym.util.DatabaseUtil;
import java.sql.*;

/**
 * RoleDAO - Data Access Object for roles and user_roles tables
 * Handles role management and user role assignments
 */
public class RoleDAO {

    /**
     * Find role ID by role name
     */
    public Long findRoleIdByName(String roleName) {
        String sql = "SELECT id FROM roles WHERE role_name = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, roleName);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getLong("id");
            }
        } catch (SQLException e) {
            System.err.println("Error finding role by name: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Assign role to user
     * Uses INSERT IGNORE to avoid duplicate key errors if role already assigned
     */
    public boolean assignUserRole(long userId, long roleId) {
        // Check if role already assigned
        if (userHasRoleId(userId, roleId)) {
            return true;
        }
        
        String sql = "INSERT IGNORE INTO user_roles (user_id, role_id, assigned_date) VALUES (?, ?, NOW())";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            stmt.setLong(2, roleId);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return true;
            }
            // Check again in case it was inserted by another thread
            return userHasRoleId(userId, roleId);
        } catch (SQLException e) {
            String errorMsg = e.getMessage();
            if (errorMsg != null && (errorMsg.contains("Duplicate entry") || errorMsg.contains("UNIQUE constraint"))) {
                return true;
            }
            System.err.println("[RoleDAO] Error assigning user role: " + errorMsg);
            return false;
        }
    }
    
    /**
     * Check if user has a specific role by role ID
     */
    private boolean userHasRoleId(long userId, long roleId) {
        String sql = "SELECT COUNT(*) FROM user_roles WHERE user_id = ? AND role_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            stmt.setLong(2, roleId);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking user role: " + e.getMessage());
        }
        
        return false;
    }

    /**
     * Check if user has specific role
     */
    public boolean userHasRole(long userId, String roleName) {
        String sql = "SELECT COUNT(*) FROM user_roles ur " +
                    "JOIN roles r ON ur.role_id = r.id " +
                    "WHERE ur.user_id = ? AND r.role_name = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            stmt.setString(2, roleName);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking user role: " + e.getMessage());
        }
        
        return false;
    }

    /**
     * Get all roles for a user
     */
    public java.util.List<String> getUserRoles(long userId) {
        java.util.List<String> roles = new java.util.ArrayList<>();
        String sql = "SELECT r.role_name FROM user_roles ur " +
                    "JOIN roles r ON ur.role_id = r.id " +
                    "WHERE ur.user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                roles.add(rs.getString("role_name"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting user roles: " + e.getMessage());
        }
        
        return roles;
    }
    
    /**
     * Get all roles for a user using existing connection
     */
    public java.util.List<String> getUserRoles(Connection conn, long userId) throws SQLException {
        java.util.List<String> roles = new java.util.ArrayList<>();
        String sql = "SELECT r.role_name FROM user_roles ur " +
                    "JOIN roles r ON ur.role_id = r.id " +
                    "WHERE ur.user_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                roles.add(rs.getString("role_name"));
            }
        }
        
        return roles;
    }

    /**
     * Create default roles if they don't exist
     * @throws RuntimeException if roles cannot be created
     */
    public void createDefaultRoles() {
        String[] defaultRoles = {
            "INSERT IGNORE INTO roles (role_name, display_name, description, is_system_role) VALUES ('ADMIN', 'Administrator', 'Full system access', true)",
            "INSERT IGNORE INTO roles (role_name, display_name, description, is_system_role) VALUES ('USER', 'Member', 'Gym member access', true)"
        };
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            conn.setAutoCommit(false); // Use transaction
            
            try {
                for (String sql : defaultRoles) {
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.executeUpdate();
                        // Extract role name from SQL for logging
                        String roleName = sql.substring(sql.indexOf("VALUES") + 7, sql.indexOf(",", sql.indexOf("VALUES") + 7)).replace("'", "");
                        System.out.println("[RoleDAO] Created/verified default role: " + roleName);
                    }
                }
                conn.commit();
                System.out.println("[RoleDAO] Successfully created/verified all default roles");
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Failed to create default roles", e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("[RoleDAO] Error creating default roles: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to database or create default roles", e);
        }
    }
}
