package com.gym.dao;

import com.gym.model.User;
import com.gym.util.DatabaseUtil;
import java.math.BigDecimal;
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
        return insertUser(username, email, passwordHash, salt, null);
    }
    
    public long insertUser(String username, String email, String passwordHash, String salt, String avatarUrl) {
        String sql = "INSERT INTO users (username, email, password_hash, salt, status, email_verified, created_date, avatar_url) " +
                    "VALUES (?, ?, ?, ?, 'ACTIVE', false, NOW(), ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, passwordHash);
            stmt.setString(4, salt);
            if (avatarUrl != null && !avatarUrl.trim().isEmpty()) {
                stmt.setString(5, avatarUrl);
            } else {
                stmt.setNull(5, Types.VARCHAR);
            }
            
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
        String sql = "SELECT id, username, email, password_hash, salt, status, email_verified, " +
                    "created_date, updated_date, last_login, failed_login_attempts, locked_until, " +
                    "gender, address, avatar_url, height, weight, bmi, " +
                    "emergency_contact_name, emergency_contact_phone, emergency_contact_relation, emergency_contact_address " +
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
     * Find user by email
     */
    public User findByEmail(String email) {
        String sql = "SELECT id, username, email, password_hash, salt, status, email_verified, " +
                    "created_date, updated_date, last_login, failed_login_attempts, locked_until, " +
                    "gender, address, avatar_url, height, weight, bmi, " +
                    "emergency_contact_name, emergency_contact_phone, emergency_contact_relation, emergency_contact_address " +
                    "FROM users WHERE email = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by email: " + e.getMessage());
        }
        return null;
    }

    /**
     * Convenience: Find by username or email using a fresh connection
     */
    public User findByUsernameOrEmail(String usernameOrEmail) {
        String sql = "SELECT id, username, email, password_hash, salt, status, email_verified, " +
                    "created_date, updated_date, last_login, failed_login_attempts, locked_until, " +
                    "gender, address, avatar_url, height, weight, bmi, " +
                    "emergency_contact_name, emergency_contact_phone, emergency_contact_relation, emergency_contact_address " +
                    "FROM users WHERE username = ? OR email = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usernameOrEmail);
            stmt.setString(2, usernameOrEmail);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by username or email: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Find user by username or email for login
     */
    public User findByUsernameOrEmail(Connection conn, String usernameOrEmail) throws SQLException {
        String sql = "SELECT id, username, email, password_hash, salt, status, email_verified, " +
                    "created_date, updated_date, last_login, failed_login_attempts, locked_until, " +
                    "gender, address, avatar_url, height, weight, bmi, " +
                    "emergency_contact_name, emergency_contact_phone, emergency_contact_relation, emergency_contact_address " +
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
     * Get user by ID
     */
    public User getUserById(long userId) {
        String sql = "SELECT id, username, email, password_hash, salt, status, email_verified, " +
                    "created_date, updated_date, last_login, failed_login_attempts, locked_until, " +
                    "gender, address, avatar_url, height, weight, bmi, " +
                    "emergency_contact_name, emergency_contact_phone, emergency_contact_relation, emergency_contact_address " +
                    "FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Update user information
     */
    public boolean updateUser(User user) {
        // Calculate BMI if height and weight are provided
        BigDecimal bmi = calculateBMI(user.getHeight(), user.getWeight());
        
        String sql = "UPDATE users SET " +
                    "username = ?, email = ?, status = ?, " +
                    "updated_date = NOW(), " +
                    "gender = ?, address = ?, avatar_url = ?, " +
                    "height = ?, weight = ?, bmi = ?, " +
                    "emergency_contact_name = ?, emergency_contact_phone = ?, " +
                    "emergency_contact_relation = ?, emergency_contact_address = ? " +
                    "WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            int paramIndex = 1;
            stmt.setString(paramIndex++, user.getUsername());
            stmt.setString(paramIndex++, user.getEmail());
            stmt.setString(paramIndex++, user.getStatus());
            
            // New fields
            stmt.setString(paramIndex++, user.getGender());
            stmt.setString(paramIndex++, user.getAddress());
            stmt.setString(paramIndex++, user.getAvatarUrl());
            
            if (user.getHeight() != null) {
                stmt.setBigDecimal(paramIndex++, user.getHeight());
            } else {
                stmt.setNull(paramIndex++, java.sql.Types.DECIMAL);
            }
            
            if (user.getWeight() != null) {
                stmt.setBigDecimal(paramIndex++, user.getWeight());
            } else {
                stmt.setNull(paramIndex++, java.sql.Types.DECIMAL);
            }
            
            if (bmi != null) {
                stmt.setBigDecimal(paramIndex++, bmi);
            } else {
                stmt.setNull(paramIndex++, java.sql.Types.DECIMAL);
            }
            
            stmt.setString(paramIndex++, user.getEmergencyContactName());
            stmt.setString(paramIndex++, user.getEmergencyContactPhone());
            stmt.setString(paramIndex++, user.getEmergencyContactRelation());
            stmt.setString(paramIndex++, user.getEmergencyContactAddress());
            
            stmt.setLong(paramIndex++, user.getId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete user (soft delete by setting status to INACTIVE)
     */
    public boolean deleteUser(long userId) {
        String sql = "UPDATE users SET status = 'INACTIVE', updated_date = NOW() WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Hard delete user (permanent removal from database)
     */
    public boolean hardDeleteUser(long userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error hard deleting user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Calculate BMI from height (in cm) and weight (in kg)
     * BMI = weight (kg) / (height (m))^2
     */
    private BigDecimal calculateBMI(BigDecimal height, BigDecimal weight) {
        if (height == null || weight == null || height.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        
        // Convert height from cm to meters
        BigDecimal heightInMeters = height.divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
        
        // Calculate BMI: weight / (height^2)
        BigDecimal heightSquared = heightInMeters.multiply(heightInMeters);
        BigDecimal bmi = weight.divide(heightSquared, 2, java.math.RoundingMode.HALF_UP);
        
        return bmi;
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
        
        // updated_date might not exist in older schema
        try {
            user.setUpdatedDate(rs.getTimestamp("updated_date"));
        } catch (SQLException e) {
            // Column doesn't exist, skip
        }
        
        user.setLastLogin(rs.getTimestamp("last_login"));
        user.setFailedLoginAttempts(rs.getInt("failed_login_attempts"));
        
        Timestamp lockedUntil = rs.getTimestamp("locked_until");
        user.setLockedUntil(lockedUntil);
        
        // New fields - handle null values gracefully
        try {
            user.setGender(rs.getString("gender"));
            user.setAddress(rs.getString("address"));
            user.setAvatarUrl(rs.getString("avatar_url"));
            
            java.math.BigDecimal height = rs.getBigDecimal("height");
            user.setHeight(height);
            
            java.math.BigDecimal weight = rs.getBigDecimal("weight");
            user.setWeight(weight);
            
            java.math.BigDecimal bmi = rs.getBigDecimal("bmi");
            user.setBmi(bmi);
            
            user.setEmergencyContactName(rs.getString("emergency_contact_name"));
            user.setEmergencyContactPhone(rs.getString("emergency_contact_phone"));
            user.setEmergencyContactRelation(rs.getString("emergency_contact_relation"));
            user.setEmergencyContactAddress(rs.getString("emergency_contact_address"));
        } catch (SQLException e) {
            // These columns might not exist in the database yet
            // Just continue without setting them
            System.out.println("Warning: Some user fields not found in database: " + e.getMessage());
        }
        
        return user;
    }
}
