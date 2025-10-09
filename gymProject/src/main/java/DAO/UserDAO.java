package DAO;

import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * UserDAO handles database operations for User entity
 */
public class UserDAO {
    private static final Logger logger = Logger.getLogger(UserDAO.class.getName());

    /**
     * Authenticate user with username and password
     */
    public User authenticateUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND status = 'active'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password); // Direct password comparison (no hashing)
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = mapResultSetToUser(rs);
                    updateLastLogin(user.getId());
                    return user;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error authenticating user", e);
            throw e;
        }
        
        return null;
    }

    /**
     * Create a new user
     */
    public boolean createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, full_name, email, phone, date_of_birth, " +
                    "gender, address, role, package_type, emergency_contact_name, emergency_contact_phone, " +
                    "join_date, status, profile_image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword()); // Store password directly (no hashing for now)
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhone());
            stmt.setDate(6, user.getDateOfBirth() != null ? new java.sql.Date(user.getDateOfBirth().getTime()) : null);
            stmt.setString(7, user.getGender());
            stmt.setString(8, user.getAddress());
            stmt.setString(9, user.getRole());
            stmt.setString(10, user.getPackageType());
            stmt.setString(11, user.getEmergencyContactName());
            stmt.setString(12, user.getEmergencyContactPhone());
            stmt.setTimestamp(13, new java.sql.Timestamp(user.getJoinDate().getTime()));
            stmt.setString(14, user.getStatus());
            stmt.setString(15, user.getProfileImage());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating user", e);
            throw e;
        }
        
        return false;
    }

    /**
     * Check if username already exists
     */
    public boolean isUsernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error checking username existence", e);
            throw e;
        }
        
        return false;
    }

    /**
     * Check if email already exists
     */
    public boolean isEmailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error checking email existence", e);
            throw e;
        }
        
        return false;
    }

    /**
     * Get user by ID
     */
    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting user by ID", e);
            throw e;
        }
        
        return null;
    }

    /**
     * Get all users with pagination
     */
    public List<User> getAllUsers(int page, int pageSize) throws SQLException {
        String sql = "SELECT * FROM users ORDER BY join_date DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, page * pageSize);
            stmt.setInt(2, pageSize);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting all users", e);
            throw e;
        }
        
        return users;
    }

    /**
     * Update user information
     */
    public boolean updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET full_name = ?, email = ?, phone = ?, date_of_birth = ?, " +
                    "gender = ?, address = ?, package_type = ?, emergency_contact_name = ?, " +
                    "emergency_contact_phone = ?, status = ?, profile_image = ?, notes = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhone());
            stmt.setDate(4, user.getDateOfBirth() != null ? new java.sql.Date(user.getDateOfBirth().getTime()) : null);
            stmt.setString(5, user.getGender());
            stmt.setString(6, user.getAddress());
            stmt.setString(7, user.getPackageType());
            stmt.setString(8, user.getEmergencyContactName());
            stmt.setString(9, user.getEmergencyContactPhone());
            stmt.setString(10, user.getStatus());
            stmt.setString(11, user.getProfileImage());
            stmt.setString(12, user.getNotes());
            stmt.setInt(13, user.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating user", e);
            throw e;
        }
    }

    /**
     * Delete user (soft delete by setting status to inactive)
     */
    public boolean deleteUser(int id) throws SQLException {
        String sql = "UPDATE users SET status = 'inactive' WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting user", e);
            throw e;
        }
    }

    /**
     * Update last login timestamp
     */
    private void updateLastLogin(int userId) throws SQLException {
        String sql = "UPDATE users SET last_login = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating last login", e);
            throw e;
        }
    }

    /**
     * Hash password (simple implementation - in production use BCrypt or similar)
     */
    private String hashPassword(String password) {
        // Simple hash implementation - replace with proper hashing like BCrypt in production
        return String.valueOf(password.hashCode());
    }

    /**
     * Map ResultSet to User object
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        
        Date dateOfBirth = rs.getDate("date_of_birth");
        if (dateOfBirth != null) {
            user.setDateOfBirth(dateOfBirth);
        }
        
        user.setGender(rs.getString("gender"));
        user.setAddress(rs.getString("address"));
        user.setRole(rs.getString("role"));
        user.setPackageType(rs.getString("package_type"));
        user.setEmergencyContactName(rs.getString("emergency_contact_name"));
        user.setEmergencyContactPhone(rs.getString("emergency_contact_phone"));
        
        Timestamp joinDate = rs.getTimestamp("join_date");
        if (joinDate != null) {
            user.setJoinDate(joinDate);
        }
        
        Timestamp lastLogin = rs.getTimestamp("last_login");
        if (lastLogin != null) {
            user.setLastLogin(lastLogin);
        }
        
        user.setStatus(rs.getString("status"));
        user.setProfileImage(rs.getString("profile_image"));
        user.setNotes(rs.getString("notes"));
        
        return user;
    }

    /**
     * Get total count of users
     */
    public int getUserCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE status = 'active'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting user count", e);
            throw e;
        }
        
        return 0;
    }
}

