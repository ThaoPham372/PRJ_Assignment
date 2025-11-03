package com.gym.dao.membership;

import com.gym.model.membership.Membership;
import com.gym.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for Membership entity
 * Handles CRUD operations for memberships table
 */
public class MembershipDAO {
    private static final Logger LOGGER = Logger.getLogger(MembershipDAO.class.getName());

    /**
     * Create a new membership
     * @param userId User ID
     * @param packageId Package ID
     * @param startDate Start date
     * @param endDate End date (calculated: startDate + duration_months)
     * @param notes Optional notes
     * @return Generated membership_id
     */
    public Long createMembership(Integer userId, Long packageId, LocalDate startDate, 
                                 LocalDate endDate, String notes) {
        String sql = "INSERT INTO memberships (user_id, package_id, start_date, end_date, " +
                    "status, notes, created_date, updated_date) " +
                    "VALUES (?, ?, ?, ?, 'ACTIVE', ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Database connection is null");
                return null;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, userId);
                stmt.setLong(2, packageId);
                stmt.setDate(3, java.sql.Date.valueOf(startDate));
                stmt.setDate(4, java.sql.Date.valueOf(endDate));
                setStringOrNull(stmt, 5, notes);
                
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
            LOGGER.log(Level.SEVERE, "Error creating membership", e);
            throw new RuntimeException("Failed to create membership: " + e.getMessage(), e);
        }
        
        return null;
    }

    /**
     * Find active membership for a user
     * Returns membership with status = 'ACTIVE' and end_date >= CURDATE()
     * @param userId User ID
     * @return Optional Membership
     */
    public Optional<Membership> findActiveByUser(Integer userId) {
        String sql = "SELECT m.membership_id, m.user_id, m.package_id, m.start_date, " +
                    "m.end_date, m.status, m.notes, m.created_date, m.updated_date, " +
                    "p.name as package_name, p.duration_months as package_duration_months, " +
                    "p.price as package_price " +
                    "FROM memberships m " +
                    "INNER JOIN packages p ON m.package_id = p.package_id " +
                    "WHERE m.user_id = ? AND m.status = 'ACTIVE' " +
                    "AND m.end_date >= CURDATE() " +
                    "ORDER BY m.end_date DESC " +
                    "LIMIT 1";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Database connection is null");
                return Optional.empty();
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(mapResultSetToMembership(rs, true));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding active membership for user: " + userId, e);
        }
        
        return Optional.empty();
    }

    /**
     * Expire a membership (set status = 'EXPIRED')
     */
    public boolean expireMembership(Long membershipId) {
        String sql = "UPDATE memberships SET status = 'EXPIRED', updated_date = CURRENT_TIMESTAMP " +
                    "WHERE membership_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Database connection is null");
                return false;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, membershipId);
                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error expiring membership: " + membershipId, e);
            throw new RuntimeException("Failed to expire membership: " + e.getMessage(), e);
        }
    }

    /**
     * List all memberships for a user (for history)
     */
    public List<Membership> listByUser(Integer userId) {
        List<Membership> memberships = new ArrayList<>();
        
        String sql = "SELECT m.membership_id, m.user_id, m.package_id, m.start_date, " +
                    "m.end_date, m.status, m.notes, m.created_date, m.updated_date, " +
                    "p.name as package_name, p.duration_months as package_duration_months, " +
                    "p.price as package_price " +
                    "FROM memberships m " +
                    "INNER JOIN packages p ON m.package_id = p.package_id " +
                    "WHERE m.user_id = ? " +
                    "ORDER BY m.created_date DESC";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Database connection is null");
                return memberships;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    memberships.add(mapResultSetToMembership(rs, true));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error listing memberships for user: " + userId, e);
        }
        
        return memberships;
    }

    /**
     * Find all expired memberships for a user (for auto-expire logic)
     */
    public List<Membership> findExpiredByUser(Integer userId) {
        List<Membership> memberships = new ArrayList<>();
        
        String sql = "SELECT m.membership_id, m.user_id, m.package_id, m.start_date, " +
                    "m.end_date, m.status, m.notes, m.created_date, m.updated_date, " +
                    "p.name as package_name, p.duration_months as package_duration_months, " +
                    "p.price as package_price " +
                    "FROM memberships m " +
                    "INNER JOIN packages p ON m.package_id = p.package_id " +
                    "WHERE m.user_id = ? AND m.status = 'ACTIVE' " +
                    "AND m.end_date < CURDATE()";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                return memberships;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    memberships.add(mapResultSetToMembership(rs, true));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding expired memberships for user: " + userId, e);
        }
        
        return memberships;
    }

    /**
     * Map ResultSet to Membership object
     */
    private Membership mapResultSetToMembership(ResultSet rs, boolean includePackageInfo) throws SQLException {
        Membership membership = new Membership();
        
        membership.setMembershipId(rs.getLong("membership_id"));
        membership.setUserId(rs.getInt("user_id"));
        membership.setPackageId(rs.getLong("package_id"));
        
        Date startDate = rs.getDate("start_date");
        if (startDate != null) {
            membership.setStartDate(startDate.toLocalDate());
        }
        
        Date endDate = rs.getDate("end_date");
        if (endDate != null) {
            membership.setEndDate(endDate.toLocalDate());
        }
        
        membership.setStatus(rs.getString("status"));
        
        String notes = rs.getString("notes");
        membership.setNotes(notes);
        
        Timestamp createdDate = rs.getTimestamp("created_date");
        if (createdDate != null) {
            membership.setCreatedDate(createdDate.toLocalDateTime());
        }
        
        Timestamp updatedDate = rs.getTimestamp("updated_date");
        if (updatedDate != null) {
            membership.setUpdatedDate(updatedDate.toLocalDateTime());
        }
        
        // Joined fields from packages (if included)
        if (includePackageInfo) {
            try {
                membership.setPackageName(rs.getString("package_name"));
                int durationMonths = rs.getInt("package_duration_months");
                if (!rs.wasNull()) {
                    membership.setPackageDurationMonths(durationMonths);
                }
                java.math.BigDecimal price = rs.getBigDecimal("package_price");
                membership.setPackagePrice(price);
            } catch (SQLException e) {
                // Columns may not exist if join was not performed
                // This is OK, just skip
            }
        }
        
        return membership;
    }

    /**
     * Helper: Set String or null
     */
    private void setStringOrNull(PreparedStatement stmt, int index, String value) throws SQLException {
        if (value != null && !value.trim().isEmpty()) {
            stmt.setString(index, value);
        } else {
            stmt.setNull(index, Types.VARCHAR);
        }
    }
}
