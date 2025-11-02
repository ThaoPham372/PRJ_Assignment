package com.gym.dao.membership;

import com.gym.model.membership.UserMembership;
import com.gym.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for UserMembership entity
 */
public class UserMembershipDao {
    private static final Logger LOGGER = Logger.getLogger(UserMembershipDao.class.getName());

    /**
     * Find active membership for a user
     */
    public Optional<UserMembership> findActiveByUserId(Long userId) {
        String sql = "SELECT um.user_membership_id, um.user_id, um.membership_id, " +
                    "um.start_date, um.expiry_date, um.status, um.auto_renew, um.order_id, " +
                    "um.created_at, um.updated_at, " +
                    "m.membership_name, m.display_name " +
                    "FROM user_memberships um " +
                    "INNER JOIN memberships m ON um.membership_id = m.membership_id " +
                    "WHERE um.user_id = ? AND um.status = 'active' " +
                    "AND um.expiry_date >= CURDATE() " +
                    "ORDER BY um.expiry_date DESC";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Database connection is null");
                return Optional.empty();
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(mapResultSetToUserMembership(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding active membership for user: " + userId, e);
        }
        
        return Optional.empty();
    }

    /**
     * Check if user has a specific membership (by membership_id)
     */
    public boolean hasMembership(Long userId, Long membershipId) {
        String sql = "SELECT COUNT(*) FROM user_memberships " +
                    "WHERE user_id = ? AND membership_id = ? AND status = 'active' " +
                    "AND expiry_date >= CURDATE()";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                return false;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, userId);
                stmt.setLong(2, membershipId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking membership for user: " + userId, e);
        }
        
        return false;
    }

    /**
     * Create a new user membership
     */
    public Long create(Long userId, Long membershipId, LocalDate startDate, LocalDate expiryDate, Long orderId) {
        String sql = "INSERT INTO user_memberships " +
                    "(user_id, membership_id, start_date, expiry_date, status, auto_renew, order_id, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, 'active', 0, ?, UTC_TIMESTAMP(), UTC_TIMESTAMP())";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Database connection is null");
                return null;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, userId);
                stmt.setLong(2, membershipId);
                stmt.setDate(3, java.sql.Date.valueOf(startDate));
                stmt.setDate(4, java.sql.Date.valueOf(expiryDate));
                stmt.setObject(5, orderId);
                
                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            return rs.getLong(1);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating user membership", e);
        }
        
        return null;
    }

    /**
     * Helper method to map ResultSet to UserMembership object
     */
    private UserMembership mapResultSetToUserMembership(ResultSet rs) throws SQLException {
        UserMembership userMembership = new UserMembership();
        userMembership.setUserMembershipId(rs.getLong("user_membership_id"));
        userMembership.setUserId(rs.getLong("user_id"));
        userMembership.setMembershipId(rs.getLong("membership_id"));
        
        Date startDate = rs.getDate("start_date");
        if (startDate != null) {
            userMembership.setStartDate(startDate.toLocalDate());
        }
        
        Date expiryDate = rs.getDate("expiry_date");
        if (expiryDate != null) {
            userMembership.setExpiryDate(expiryDate.toLocalDate());
        }
        
        userMembership.setStatus(rs.getString("status"));
        userMembership.setAutoRenew(rs.getBoolean("auto_renew"));
        
        Long orderId = rs.getLong("order_id");
        if (!rs.wasNull()) {
            userMembership.setOrderId(orderId);
        }
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            userMembership.setCreatedAt(OffsetDateTime.ofInstant(createdAt.toInstant(), ZoneOffset.UTC));
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            userMembership.setUpdatedAt(OffsetDateTime.ofInstant(updatedAt.toInstant(), ZoneOffset.UTC));
        }
        
        // Joined fields
        userMembership.setMembershipName(rs.getString("membership_name"));
        userMembership.setDisplayName(rs.getString("display_name"));
        
        return userMembership;
    }
}

