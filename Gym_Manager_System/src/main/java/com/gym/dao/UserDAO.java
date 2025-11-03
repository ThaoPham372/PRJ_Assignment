package com.gym.dao;

import com.gym.model.User;
import com.gym.util.DatabaseUtil;
import java.sql.*;

/**
 * UserDAO - Data Access Object for user table
 * Handles user authentication, registration, duplicate checks, and login history
 * Note: Student-specific information is handled by StudentDAO
 */
public class UserDAO {

    /**
     * Check if username already exists
     */
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM `user` WHERE username = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            // Check if error is due to missing username column
            String errorMsg = e.getMessage();
            if (errorMsg != null && (errorMsg.contains("Unknown column 'username'") || 
                errorMsg.contains("Unknown column 'user.username'") ||
                (e.getSQLState() != null && e.getSQLState().equals("42S22")))) {
                System.err.println("[UserDAO] ERROR: username column does not exist in database.");
                System.err.println("[UserDAO] Please run migration_add_username_column.sql to add the username column.");
                System.err.println("[UserDAO] Falling back to check 'name' column instead.");
                
                // Fallback: check name column (old schema)
                return existsByUsernameFallback(username);
            }
            
            System.err.println("[UserDAO] ERROR: Cannot query 'user' table.");
            System.err.println("[UserDAO] SQL Error: " + errorMsg);
            System.err.println("[UserDAO] SQL State: " + e.getSQLState());
            try {
                logDatabaseInfo();
            } catch (Exception ex) {
                // Ignore
            }
            e.printStackTrace();
            throw new RuntimeException("Database schema error: Failed to check username existence.", e);
        }
        
        return false;
    }
    
    /**
     * Fallback method to check username using name column (old schema)
     */
    private boolean existsByUsernameFallback(String username) {
        String sql = "SELECT COUNT(*) FROM `user` WHERE name = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] ERROR: Fallback check also failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Check if email already exists
     */
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM `user` WHERE email = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] ERROR: Cannot query 'user' table. Please ensure database uses NEW schema with 'user' table.");
            System.err.println("[UserDAO] SQL Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Database schema error: 'user' table not found. Please migrate database to new schema.", e);
        }
        
        return false;
    }

    /**
     * Insert new user and return generated ID
     * username is used for login, name is left NULL (can be set later in profile edit)
     */
    public long insertUser(String username, String email, String passwordHash, String salt) {
        return insertUser(username, null, email, passwordHash, salt); // name is NULL by default
    }
    
    /**
     * Insert new user with explicit name (full name) and return generated ID
     */
    public long insertUser(String username, String name, String email, String passwordHash, String salt) {
        String sql = "INSERT INTO `user` (username, name, email, password, status, createdDate) " +
                    "VALUES (?, ?, ?, ?, 'ACTIVE', NOW())";
        
        // Trim and prepare name value
        String nameValue = (name != null && !name.trim().isEmpty()) ? name.trim() : null;
        System.out.println("[UserDAO] insertUser called:");
        System.out.println("  - Username: " + username);
        System.out.println("  - Name (to save): " + nameValue);
        System.out.println("  - Email: " + email);
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, username); // username column for login
            if (nameValue != null) {
                stmt.setString(2, nameValue); // name column (full name)
                System.out.println("[UserDAO] Setting name parameter: '" + nameValue + "'");
            } else {
                stmt.setNull(2, Types.VARCHAR); // Set name to NULL if not provided
                System.out.println("[UserDAO] Setting name parameter: NULL");
            }
            stmt.setString(3, email);
            stmt.setString(4, passwordHash); // password column
            // Note: salt column may not exist in current schema, skipping
            
            int affectedRows = stmt.executeUpdate();
            System.out.println("[UserDAO] INSERT executed. Affected rows: " + affectedRows);
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    long userId = generatedKeys.getLong(1);
                    System.out.println("[UserDAO] User created successfully with ID: " + userId);
                    
                    // Verify that name was actually saved to database
                    try {
                        String verifySql = "SELECT name FROM `user` WHERE user_id = ?";
                        try (PreparedStatement verifyStmt = conn.prepareStatement(verifySql)) {
                            verifyStmt.setLong(1, userId);
                            ResultSet verifyRs = verifyStmt.executeQuery();
                            if (verifyRs.next()) {
                                String savedName = verifyRs.getString("name");
                                System.out.println("[UserDAO] Verification query - Saved name in DB: '" + savedName + "'");
                                if (nameValue != null && !nameValue.equals(savedName)) {
                                    System.err.println("[UserDAO] WARNING: Name mismatch! Expected: '" + nameValue + "', Actual: '" + savedName + "'");
                                } else if (nameValue == null && savedName != null && !savedName.trim().isEmpty()) {
                                    System.err.println("[UserDAO] WARNING: Expected NULL name but got: '" + savedName + "'");
                                } else {
                                    System.out.println("[UserDAO] ✓ Name verification successful!");
                                }
                            }
                        }
                    } catch (SQLException verifyEx) {
                        System.err.println("[UserDAO] WARNING: Could not verify name was saved: " + verifyEx.getMessage());
                        // Don't fail the insert if verification fails
                    }
                    
                    return userId;
                }
            }
        } catch (SQLException e) {
            // Check if error is due to missing username column
            String errorMsg = e.getMessage();
            System.err.println("[UserDAO] SQLException caught during insertUser:");
            System.err.println("  - Error message: " + errorMsg);
            System.err.println("  - SQL State: " + e.getSQLState());
            System.err.println("  - Error Code: " + e.getErrorCode());
            System.err.println("  - Attempted SQL: INSERT INTO `user` (username, name, email, password, status, createdDate) VALUES (?, ?, ?, ?, 'ACTIVE', NOW())");
            System.err.println("  - Parameters: username='" + username + "', name='" + nameValue + "', email='" + email + "'");
            
            if (errorMsg != null && (errorMsg.contains("Unknown column 'username'") || 
                errorMsg.contains("Unknown column 'user.username'") ||
                (e.getSQLState() != null && e.getSQLState().equals("42S22")))) {
                System.err.println("[UserDAO] ERROR: username column does not exist in database.");
                System.err.println("[UserDAO] Please run migration_add_username_column.sql to add the username column.");
                System.err.println("[UserDAO] Falling back to insert using 'name' column only.");
                
                // Fallback: insert using name column (old schema)
                return insertUserFallback(username, name, email, passwordHash, salt);
            }
            
            // Check if error is due to missing name column
            if (errorMsg != null && (errorMsg.contains("Unknown column 'name'") || 
                errorMsg.contains("Unknown column 'user.name'"))) {
                System.err.println("[UserDAO] ERROR: name column does not exist in database!");
                System.err.println("[UserDAO] This is critical - name column must exist. Please check database schema.");
            }
            
            System.err.println("[UserDAO] ERROR: Cannot insert into 'user' table.");
            try {
                logDatabaseInfo();
            } catch (Exception ex) {
                // Ignore if logDatabaseInfo fails
            }
            e.printStackTrace();
            throw new RuntimeException("Database error: Failed to insert user. " + errorMsg, e);
        }
        
        return -1; // Error
    }
    
    /**
     * Fallback method for old schema without username column
     * Uses name column for both username and name
     */
    private long insertUserFallback(String username, String name, String email, String passwordHash, String salt) {
        String sql = "INSERT INTO `user` (name, email, password, status, createdDate) " +
                    "VALUES (?, ?, ?, 'ACTIVE', NOW())";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Use username for name column (old schema compatibility)
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, passwordHash);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    long userId = generatedKeys.getLong(1);
                    System.out.println("[UserDAO] Successfully inserted user (fallback mode - using name column) with user_id: " + userId);
                    System.out.println("[UserDAO] WARNING: username column not found. Please run migration_add_username_column.sql.");
                    return userId;
                }
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] ERROR: Fallback insert also failed: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to insert user even with fallback method. " + e.getMessage(), e);
        }
        
        return -1;
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
        String sql = "SELECT user_id, username, name, email, password, status, " +
                    "createdDate, lastUpdate, lastLogin, failedLoginAttempts, lockedUntil, avatar_url " +
                    "FROM `user` WHERE username = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            // Check if error is due to missing username column
            String errorMsg = e.getMessage();
            if (errorMsg != null && (errorMsg.contains("Unknown column 'username'") || 
                errorMsg.contains("Unknown column 'user.username'") ||
                (e.getSQLState() != null && e.getSQLState().equals("42S22")))) {
                System.err.println("[UserDAO] WARNING: username column not found, using fallback with name column.");
                // Fallback: query using name column
                return findByUsernameFallback(username);
            }
            
            System.err.println("[UserDAO] ERROR: Cannot query 'user' table.");
            System.err.println("[UserDAO] SQL Error: " + errorMsg);
            e.printStackTrace();
            throw new RuntimeException("Database schema error: Failed to find user by username.", e);
        }
        
        return null;
    }
    
    /**
     * Fallback method to find user by name column (old schema)
     */
    private User findByUsernameFallback(String username) {
        String sql = "SELECT user_id, name, email, password, status, " +
                    "createdDate, lastUpdate, lastLogin, failedLoginAttempts, lockedUntil, avatar_url " +
                    "FROM `user` WHERE name = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] ERROR: Fallback query also failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Find user by email
     */
    public User findByEmail(String email) {
        String sql = "SELECT user_id, username, name, email, password, status, " +
                    "createdDate, lastUpdate, lastLogin, failedLoginAttempts, lockedUntil, avatar_url " +
                    "FROM `user` WHERE email = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            // Check if error is due to missing username column
            String errorMsg = e.getMessage();
            if (errorMsg != null && (errorMsg.contains("Unknown column 'username'") || 
                errorMsg.contains("Unknown column 'user.username'") ||
                (e.getSQLState() != null && e.getSQLState().equals("42S22")))) {
                System.err.println("[UserDAO] WARNING: username column not found, using fallback with name column.");
                // Fallback: query without username column
                return findByEmailFallback(email);
            }
            
            System.err.println("[UserDAO] ERROR: Cannot query 'user' table.");
            System.err.println("[UserDAO] SQL Error: " + errorMsg);
            e.printStackTrace();
            throw new RuntimeException("Database schema error: Failed to find user by email.", e);
        }
        return null;
    }
    
    /**
     * Fallback method to find user by email (old schema without username column)
     */
    private User findByEmailFallback(String email) {
        String sql = "SELECT user_id, name, email, password, status, " +
                    "createdDate, lastUpdate, lastLogin, failedLoginAttempts, lockedUntil, avatar_url " +
                    "FROM `user` WHERE email = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] ERROR: Fallback query also failed: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convenience: Find by username or email using a fresh connection
     */
    public User findByUsernameOrEmail(String usernameOrEmail) {
        String sql = "SELECT user_id, username, name, email, password, status, " +
                    "createdDate, lastUpdate, lastLogin, failedLoginAttempts, lockedUntil, avatar_url " +
                    "FROM `user` WHERE username = ? OR email = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usernameOrEmail);
            stmt.setString(2, usernameOrEmail);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            // Check if error is due to missing username column
            String errorMsg = e.getMessage();
            if (errorMsg != null && (errorMsg.contains("Unknown column 'username'") || 
                errorMsg.contains("Unknown column 'user.username'") ||
                (e.getSQLState() != null && e.getSQLState().equals("42S22")))) {
                // Fallback: query using name column
                return findByUsernameOrEmailFallback(usernameOrEmail);
            }
            
            System.err.println("[UserDAO] ERROR: Cannot query 'user' table.");
            System.err.println("[UserDAO] SQL Error: " + errorMsg);
            e.printStackTrace();
            throw new RuntimeException("Database schema error: Failed to find user.", e);
        }
        return null;
    }
    
    /**
     * Fallback method to find by name or email (old schema)
     */
    private User findByUsernameOrEmailFallback(String usernameOrEmail) {
        String sql = "SELECT user_id, name, email, password, status, " +
                    "createdDate, lastUpdate, lastLogin, failedLoginAttempts, lockedUntil, avatar_url " +
                    "FROM `user` WHERE name = ? OR email = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usernameOrEmail);
            stmt.setString(2, usernameOrEmail);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] ERROR: Fallback query also failed: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Find user by username or email for login
     */
    public User findByUsernameOrEmail(Connection conn, String usernameOrEmail) throws SQLException {
        String sql = "SELECT user_id, username, name, email, password, status, " +
                    "createdDate, lastUpdate, lastLogin, failedLoginAttempts, lockedUntil, avatar_url " +
                    "FROM `user` WHERE username = ? OR email = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usernameOrEmail);
            stmt.setString(2, usernameOrEmail);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            // Check if error is due to missing username column
            String errorMsg = e.getMessage();
            if (errorMsg != null && (errorMsg.contains("Unknown column 'username'") || 
                errorMsg.contains("Unknown column 'user.username'") ||
                (e.getSQLState() != null && e.getSQLState().equals("42S22")))) {
                System.err.println("[UserDAO] WARNING: username column not found, using fallback with name column.");
                // Fallback: query using name column
                String sqlFallback = "SELECT user_id, name, email, password, status, " +
                                   "createdDate, lastUpdate, lastLogin, failedLoginAttempts, lockedUntil, avatar_url " +
                                   "FROM `user` WHERE name = ? OR email = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sqlFallback)) {
                    stmt.setString(1, usernameOrEmail);
                    stmt.setString(2, usernameOrEmail);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        return mapResultSetToUser(rs);
                    }
                }
                // If still not found, return null (not an error)
                return null;
            }
            
            System.err.println("[UserDAO] ERROR: Cannot query 'user' table.");
            System.err.println("[UserDAO] SQL Error: " + errorMsg);
            throw e; // Re-throw to let caller handle
        }
        
        return null;
    }
    
    /**
     * Increment failed login attempts
     */
    public void incrementFailedLoginAttempts(Connection conn, long userId) throws SQLException {
        String sql = "UPDATE `user` SET failedLoginAttempts = failedLoginAttempts + 1 WHERE user_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Reset failed login attempts
     */
    public void resetFailedLoginAttempts(Connection conn, long userId) throws SQLException {
        String sql = "UPDATE `user` SET failedLoginAttempts = 0, lockedUntil = NULL WHERE user_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Lock account for specified minutes
     */
    public void lockAccount(Connection conn, long userId, int minutes) throws SQLException {
        String sql = "UPDATE `user` SET lockedUntil = DATE_ADD(NOW(), INTERVAL ? MINUTE) WHERE user_id = ?";
        
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
        String sql = "UPDATE `user` SET lastLogin = NOW() WHERE user_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Get user by ID
     */
    public User getUserById(long userId) {
        String sql = "SELECT user_id, username, name, email, password, status, " +
                    "createdDate, lastUpdate, lastLogin, failedLoginAttempts, lockedUntil, avatar_url " +
                    "FROM `user` WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            // Check if error is due to missing username column
            String errorMsg = e.getMessage();
            if (errorMsg != null && (errorMsg.contains("Unknown column 'username'") || 
                errorMsg.contains("Unknown column 'user.username'") ||
                (e.getSQLState() != null && e.getSQLState().equals("42S22")))) {
                System.err.println("[UserDAO] WARNING: username column not found, using fallback query.");
                // Fallback: query without username column
                return getUserByIdFallback(userId);
            }
            
            System.err.println("[UserDAO] ERROR: Cannot query 'user' table.");
            System.err.println("[UserDAO] SQL Error: " + errorMsg);
            e.printStackTrace();
            throw new RuntimeException("Database schema error: Failed to get user by ID.", e);
        }
        
        return null;
    }
    
    /**
     * Fallback method to get user by ID (old schema without username column)
     */
    private User getUserByIdFallback(long userId) {
        String sql = "SELECT user_id, name, email, password, status, " +
                    "createdDate, lastUpdate, lastLogin, failedLoginAttempts, lockedUntil, avatar_url " +
                    "FROM `user` WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] ERROR: Fallback query also failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Update user information
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE `user` SET " +
                    "username = ?, name = ?, email = ?, phone = ?, dob = ?, address = ?, status = ?, " +
                    "avatar_url = ?, " +
                    "lastUpdate = NOW() " +
                    "WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            int paramIndex = 1;
            stmt.setString(paramIndex++, user.getUsername());
            stmt.setString(paramIndex++, user.getName());
            stmt.setString(paramIndex++, user.getEmail());
            if (user.getPhone() != null) {
                stmt.setString(paramIndex++, user.getPhone());
            } else {
                stmt.setNull(paramIndex++, Types.VARCHAR);
            }
            if (user.getDob() != null) {
                stmt.setDate(paramIndex++, user.getDob());
            } else {
                stmt.setNull(paramIndex++, Types.DATE);
            }
            if (user.getAddress() != null) {
                stmt.setString(paramIndex++, user.getAddress());
            } else {
                stmt.setNull(paramIndex++, Types.VARCHAR);
            }
            stmt.setString(paramIndex++, user.getStatus());
            stmt.setString(paramIndex++, user.getAvatarUrl());
            
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
     * Update avatar URL for a user
     */
    public boolean updateAvatarUrl(long userId, String avatarUrl) {
        String sql = "UPDATE `user` SET avatar_url = ?, lastUpdate = NOW() WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, avatarUrl);
            stmt.setLong(2, userId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating avatar URL: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete user (soft delete by setting status to INACTIVE)
     */
    public boolean deleteUser(long userId) {
        String sql = "UPDATE `user` SET status = 'INACTIVE', lastUpdate = NOW() WHERE user_id = ?";
        
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
        String sql = "DELETE FROM `user` WHERE user_id = ?";
        
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
     * Helper method to map ResultSet to User object
     * Maps database columns (username, name, password, camelCase) to User model properties
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("user_id"));
        
        // Try to get username, fallback to name if username column doesn't exist
        try {
            user.setUsername(rs.getString("username")); // username for login
        } catch (SQLException e) {
            // Username column doesn't exist, use name as username (old schema)
            String name = rs.getString("name");
            user.setUsername(name);
        }
        
        // Get name (full name)
        user.setName(rs.getString("name")); // name is full name
        user.setEmail(rs.getString("email"));
        
        // Get phone, dob, address
        try {
            user.setPhone(rs.getString("phone"));
        } catch (SQLException e) {
            user.setPhone(null); // Column doesn't exist or null
        }
        try {
            user.setDob(rs.getDate("dob"));
        } catch (SQLException e) {
            user.setDob(null); // Column doesn't exist or null
        }
        try {
            user.setAddress(rs.getString("address"));
        } catch (SQLException e) {
            user.setAddress(null); // Column doesn't exist or null
        }
        
        user.setPasswordHash(rs.getString("password")); // Database has 'password', model uses 'passwordHash'
        
        // Salt column may not exist - set to empty string if missing
        try {
            user.setSalt(rs.getString("salt"));
            if (user.getSalt() == null) {
                user.setSalt(""); // Default empty salt if column doesn't exist
            }
        } catch (SQLException e) {
            user.setSalt(""); // Column doesn't exist, use empty string
        }
        
        user.setStatus(rs.getString("status"));
        
        // email_verified column may not exist - default to false
        try {
            user.setEmailVerified(rs.getBoolean("email_verified"));
        } catch (SQLException e) {
            user.setEmailVerified(false); // Column doesn't exist, default to false
        }
        
        user.setCreatedDate(rs.getTimestamp("createdDate")); // Database uses camelCase
        
        // avatar_url
        try {
            user.setAvatarUrl(rs.getString("avatar_url"));
        } catch (SQLException e) {
            user.setAvatarUrl(null); // Column doesn't exist or null
        }
        
        // lastUpdate (camelCase) instead of last_update
        try {
            user.setUpdatedDate(rs.getTimestamp("lastUpdate"));
        } catch (SQLException e) {
            // Try alternative column names
            try {
                user.setUpdatedDate(rs.getTimestamp("last_update"));
            } catch (SQLException e2) {
                // Column doesn't exist, skip
            }
        }
        
        user.setLastLogin(rs.getTimestamp("lastLogin")); // camelCase
        user.setFailedLoginAttempts(rs.getInt("failedLoginAttempts")); // camelCase
        
        Timestamp lockedUntil = rs.getTimestamp("lockedUntil"); // camelCase
        user.setLockedUntil(lockedUntil);
        
        return user;
    }
    
    /**
     * Helper method to log database connection info for debugging
     */
    private void logDatabaseInfo() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn != null) {
                String catalog = conn.getCatalog();
                String url = conn.getMetaData().getURL();
                System.err.println("[UserDAO] Current Database: " + catalog);
                System.err.println("[UserDAO] Connection URL: " + url);
                
                // Try to list all tables to see what exists
                try (java.sql.Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SHOW TABLES")) {
                    System.err.println("[UserDAO] Available tables in database:");
                    boolean foundUserTable = false;
                    while (rs.next()) {
                        String tableName = rs.getString(1);
                        System.err.println("[UserDAO]   - " + tableName);
                        if (tableName.equalsIgnoreCase("user")) {
                            foundUserTable = true;
                        }
                    }
                    if (!foundUserTable) {
                        System.err.println("[UserDAO] WARNING: 'user' table not found in database!");
                    } else {
                        System.err.println("[UserDAO] INFO: 'user' table exists in database.");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] Error getting database info: " + e.getMessage());
        }
    }
    
}
