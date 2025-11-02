package com.gym.dao.membership;

import com.gym.model.membership.Membership;
import com.gym.util.DatabaseUtil;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for Membership entity
 */
public class MembershipDao {
    private static final Logger LOGGER = Logger.getLogger(MembershipDao.class.getName());

    /**
     * Find all active memberships ordered by display_order
     */
    public List<Membership> findAllActive() {
        List<Membership> memberships = new ArrayList<>();
        
        // Try without dbo prefix first (like other DAOs: products, foods)
        String sql = "SELECT membership_id, membership_name, display_name, description, price, " +
                    "duration_months, features, is_featured, is_active, display_order, " +
                    "image_path, created_at, updated_at " +
                    "FROM memberships " +
                    "WHERE is_active = 1 " +
                    "ORDER BY display_order ASC";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "[MembershipDao] Database connection is null");
                System.err.println("[MembershipDao] Database connection is null in findAllActive");
                return memberships;
            }
            
            // First check if table exists
            System.out.println("[MembershipDao] ===== Checking if memberships table exists =====");
            try (PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'memberships'")) {
                ResultSet rsCheck = checkStmt.executeQuery();
                if (rsCheck.next()) {
                    int tableExists = rsCheck.getInt(1);
                    System.out.println("[MembershipDao] Table 'memberships' exists: " + (tableExists > 0));
                    if (tableExists == 0) {
                        System.err.println("[MembershipDao] ERROR: Table 'memberships' does not exist in database!");
                        System.err.println("[MembershipDao] Please run the SQL script: memberships_table.sql");
                        return memberships;
                    }
                }
            }
            
            System.out.println("[MembershipDao] Executing query: " + sql);
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                int count = 0;
                while (rs.next()) {
                    count++;
                    try {
                        Membership membership = mapResultSetToMembership(rs);
                        memberships.add(membership);
                        System.out.println("[MembershipDao] Loaded membership: " + membership.getMembershipName() + 
                                          " (ID: " + membership.getMembershipId() + 
                                          ", Display: " + membership.getDisplayName() + ")");
                    } catch (Exception e) {
                        System.err.println("[MembershipDao] Error mapping membership row " + count + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                System.out.println("[MembershipDao] Total active memberships loaded: " + count);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding all active memberships", e);
            System.err.println("[MembershipDao] SQL Error: " + e.getMessage());
            System.err.println("[MembershipDao] SQL State: " + e.getSQLState());
            System.err.println("[MembershipDao] Error Code: " + e.getErrorCode());
            
            if (e.getMessage().contains("Invalid object name")) {
                System.err.println("[MembershipDao] ========================================");
                System.err.println("[MembershipDao] TABLE DOES NOT EXIST!");
                System.err.println("[MembershipDao] Please run the SQL script: memberships_table.sql");
                System.err.println("[MembershipDao] ========================================");
            }
            
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("[MembershipDao] Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("[MembershipDao] Returning " + memberships.size() + " memberships");
        return memberships;
    }

    /**
     * Find membership by ID
     */
    public Optional<Membership> findById(Long membershipId) {
        String sql = "SELECT membership_id, membership_name, display_name, description, price, " +
                    "duration_months, features, is_featured, is_active, display_order, " +
                    "image_path, created_at, updated_at " +
                    "FROM memberships " +
                    "WHERE membership_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Database connection is null");
                return Optional.empty();
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, membershipId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(mapResultSetToMembership(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding membership by ID: " + membershipId, e);
        }
        
        return Optional.empty();
    }

    /**
     * Helper method to map ResultSet to Membership object
     */
    private Membership mapResultSetToMembership(ResultSet rs) throws SQLException {
        Membership membership = new Membership();
        
        try {
            membership.setMembershipId(rs.getLong("membership_id"));
            membership.setMembershipName(rs.getString("membership_name"));
            membership.setDisplayName(rs.getString("display_name"));
            
            // Description can be null
            String description = rs.getString("description");
            membership.setDescription(description);
            
            membership.setPrice(rs.getBigDecimal("price"));
            membership.setDurationMonths(rs.getInt("duration_months"));
            
            // Features can be null
            String features = rs.getString("features");
            membership.setFeatures(features);
            
            // Handle BOOLEAN/TINYINT fields (MySQL boolean)
            membership.setIsFeatured(rs.getBoolean("is_featured"));
            membership.setIsActive(rs.getBoolean("is_active"));
            membership.setDisplayOrder(rs.getInt("display_order"));
            
            // Image path can be null
            String imagePath = rs.getString("image_path");
            membership.setImagePath(imagePath);
            
            Timestamp createdAt = rs.getTimestamp("created_at");
            if (createdAt != null) {
                membership.setCreatedAt(OffsetDateTime.ofInstant(createdAt.toInstant(), ZoneOffset.UTC));
            }
            
            Timestamp updatedAt = rs.getTimestamp("updated_at");
            if (updatedAt != null) {
                membership.setUpdatedAt(OffsetDateTime.ofInstant(updatedAt.toInstant(), ZoneOffset.UTC));
            }
        } catch (SQLException e) {
            System.err.println("[MembershipDao] Error mapping ResultSet to Membership: " + e.getMessage());
            throw e;
        }
        
        return membership;
    }
}

