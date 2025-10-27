package com.gym.dao;

import com.gym.model.User;
import com.gym.util.DatabaseUtil;
import java.sql.*;

/**
 * UserDAO - Data Access Object for users table
 * Handles user registration, duplicate checks, and login history
 */
public class UserDAO {

    /**
     * Check if username already exists
     */
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking username existence: " + e.getMessage());
        }
        
        return false;
    }

    /**
     * Check if email already exists
     */
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
        }
        
        return false;
    }

    /**
     * Insert new user and return generated ID
     */
    public long insertUser(String username, String email, String passwordHash, String salt) {
        String sql = "INSERT INTO users (username, email, password_hash, salt, status, email_verified, created_date) " +
                    "VALUES (?, ?, ?, ?, 'ACTIVE', false, NOW())";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, passwordHash);
            stmt.setString(4, salt);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting user: " + e.getMessage());
        }
        
        return -1; // Error
    }

    /**
     * Insert login history record for registration
     */
    public void insertLoginHistory(long userId, String ipAddress, String userAgent) {
        String sql = "INSERT INTO login_history (user_id, login_time, ip_address, user_agent, login_successful, failure_reason) " +
                    "VALUES (?, NOW(), ?, ?, true, 'registration_success')";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            stmt.setString(2, ipAddress);
            stmt.setString(3, userAgent);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting login history: " + e.getMessage());
        }
    }

    /**
     * Find user by username for login
     */
    public User findByUsername(String username) {
        String sql = "SELECT id, username, email, password_hash, salt, status, email_verified, created_date, last_login, failed_login_attempts, locked_until " +
                    "FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by username: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Find user by username or email for login
     */
    public User findByUsernameOrEmail(Connection conn, String usernameOrEmail) throws SQLException {
        String sql = "SELECT id, username, email, password_hash, salt, status, email_verified, created_date, last_login, failed_login_attempts, locked_until " +
                    "FROM users WHERE username = ? OR email = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usernameOrEmail);
            stmt.setString(2, usernameOrEmail);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        }
        
        return null;
    }
    
    /**
     * Increment failed login attempts
     */
    public void incrementFailedLoginAttempts(Connection conn, long userId) throws SQLException {
        String sql = "UPDATE users SET failed_login_attempts = failed_login_attempts + 1 WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Reset failed login attempts
     */
    public void resetFailedLoginAttempts(Connection conn, long userId) throws SQLException {
        String sql = "UPDATE users SET failed_login_attempts = 0, locked_until = NULL WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Lock account for specified minutes
     */
    public void lockAccount(Connection conn, long userId, int minutes) throws SQLException {
        String sql = "UPDATE users SET locked_until = DATE_ADD(NOW(), INTERVAL ? MINUTE) WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, minutes);
            stmt.setLong(2, userId);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Update last login time
     */
    public void updateLastLogin(Connection conn, long userId) throws SQLException {
        String sql = "UPDATE users SET last_login = NOW() WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Helper method to map ResultSet to User object
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setSalt(rs.getString("salt"));
        user.setStatus(rs.getString("status"));
        user.setEmailVerified(rs.getBoolean("email_verified"));
        user.setCreatedDate(rs.getTimestamp("created_date"));
        user.setLastLogin(rs.getTimestamp("last_login"));
        user.setFailedLoginAttempts(rs.getInt("failed_login_attempts"));
        user.setLockedUntil(rs.getTimestamp("locked_until"));
        return user;
    }
}
