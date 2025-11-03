package com.gym.dao.membership;

import com.gym.model.membership.Package;
import com.gym.util.DatabaseUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for Package entity
 * Handles CRUD operations for packages table
 */
public class PackageDAO {
    private static final Logger LOGGER = Logger.getLogger(PackageDAO.class.getName());

    /**
     * Find all active packages (optionally filtered by gym_id)
     * @param gymId Optional gym ID to filter packages. If null, returns all active packages.
     * @return List of active packages
     */
    public List<Package> findAllActive(Integer gymId) {
        List<Package> packages = new ArrayList<>();
        
        // Query: Get all active packages (is_active = 1 or TRUE)
        // Also include packages where is_active might be NULL (treat as active)
        StringBuilder sql = new StringBuilder(
            "SELECT package_id, name, duration_months, price, description, " +
            "max_sessions, is_active, created_date, updated_date, gym_id " +
            "FROM packages " +
            "WHERE (is_active = 1 OR is_active IS NULL OR is_active = TRUE) "
        );
        
        if (gymId != null) {
            sql.append("AND (gym_id = ? OR gym_id IS NULL) ");
        }
        
        sql.append("ORDER BY created_date DESC");

        System.out.println("[PackageDAO] ===== findAllActive START =====");
        System.out.println("[PackageDAO] SQL: " + sql.toString());
        System.out.println("[PackageDAO] gymId: " + gymId);

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Database connection is null");
                System.err.println("[PackageDAO] ERROR: Database connection is null!");
                return packages;
            }
            
            System.out.println("[PackageDAO] Database connection OK");
            
            // First, check if table exists and count total records
            try (Statement checkStmt = conn.createStatement()) {
                ResultSet countRs = checkStmt.executeQuery("SELECT COUNT(*) as total FROM packages");
                if (countRs.next()) {
                    int totalRecords = countRs.getInt("total");
                    System.out.println("[PackageDAO] Total records in packages table: " + totalRecords);
                    
                    // Also check active count
                    ResultSet activeRs = checkStmt.executeQuery("SELECT COUNT(*) as active FROM packages WHERE is_active = 1 OR is_active IS NULL");
                    if (activeRs.next()) {
                        int activeCount = activeRs.getInt("active");
                        System.out.println("[PackageDAO] Active packages count: " + activeCount);
                    }
                }
            } catch (SQLException e) {
                System.err.println("[PackageDAO] ERROR checking table: " + e.getMessage());
                // Continue anyway
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
                if (gymId != null) {
                    stmt.setInt(1, gymId);
                    System.out.println("[PackageDAO] Set gymId parameter: " + gymId);
                }
                
                System.out.println("[PackageDAO] Executing query...");
                ResultSet rs = stmt.executeQuery();
                
                int count = 0;
                while (rs.next()) {
                    count++;
                    try {
                        Package pkg = mapResultSetToPackage(rs);
                        packages.add(pkg);
                        System.out.println("[PackageDAO] ✓ Loaded package #" + count + ": ID=" + pkg.getPackageId() + 
                                         ", Name='" + pkg.getName() + "', Price=" + pkg.getPrice() + 
                                         ", Active=" + pkg.getIsActive());
                    } catch (Exception e) {
                        System.err.println("[PackageDAO] ✗ ERROR mapping package row #" + count + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                
                System.out.println("[PackageDAO] Total packages loaded successfully: " + count);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding all active packages", e);
            System.err.println("[PackageDAO] SQL ERROR: " + e.getMessage());
            System.err.println("[PackageDAO] SQL State: " + e.getSQLState());
            System.err.println("[PackageDAO] Error Code: " + e.getErrorCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("[PackageDAO] Unexpected ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("[PackageDAO] Returning " + packages.size() + " packages");
        System.out.println("[PackageDAO] ===== findAllActive END =====");
        
        return packages;
    }

    /**
     * Find all active packages (no gym filter)
     */
    public List<Package> findAllActive() {
        return findAllActive(null);
    }

    /**
     * Find package by ID
     */
    public Optional<Package> findById(Long packageId) {
        String sql = "SELECT package_id, name, duration_months, price, description, " +
                    "max_sessions, is_active, created_date, updated_date, gym_id " +
                    "FROM packages WHERE package_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Database connection is null");
                return Optional.empty();
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, packageId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(mapResultSetToPackage(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding package by ID: " + packageId, e);
        }
        
        return Optional.empty();
    }

    /**
     * Create a new package
     */
    public Long create(Package pkg) {
        String sql = "INSERT INTO packages (name, duration_months, price, description, " +
                    "max_sessions, is_active, gym_id, created_date, updated_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Database connection is null");
                return null;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, pkg.getName());
                stmt.setInt(2, pkg.getDurationMonths());
                stmt.setBigDecimal(3, pkg.getPrice());
                setStringOrNull(stmt, 4, pkg.getDescription());
                setIntOrNull(stmt, 5, pkg.getMaxSessions());
                stmt.setBoolean(6, pkg.getIsActive() != null ? pkg.getIsActive() : true);
                setIntOrNull(stmt, 7, pkg.getGymId());
                
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
            LOGGER.log(Level.SEVERE, "Error creating package", e);
            throw new RuntimeException("Failed to create package: " + e.getMessage(), e);
        }
        
        return null;
    }

    /**
     * Update an existing package
     */
    public boolean update(Package pkg) {
        String sql = "UPDATE packages SET name = ?, duration_months = ?, price = ?, " +
                    "description = ?, max_sessions = ?, is_active = ?, gym_id = ?, " +
                    "updated_date = CURRENT_TIMESTAMP " +
                    "WHERE package_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Database connection is null");
                return false;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, pkg.getName());
                stmt.setInt(2, pkg.getDurationMonths());
                stmt.setBigDecimal(3, pkg.getPrice());
                setStringOrNull(stmt, 4, pkg.getDescription());
                setIntOrNull(stmt, 5, pkg.getMaxSessions());
                stmt.setBoolean(6, pkg.getIsActive() != null ? pkg.getIsActive() : true);
                setIntOrNull(stmt, 7, pkg.getGymId());
                stmt.setLong(8, pkg.getPackageId());
                
                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating package: " + pkg.getPackageId(), e);
            throw new RuntimeException("Failed to update package: " + e.getMessage(), e);
        }
    }

    /**
     * Soft disable a package (set is_active = 0 instead of deleting)
     */
    public boolean softDisable(Long packageId) {
        String sql = "UPDATE packages SET is_active = 0, updated_date = CURRENT_TIMESTAMP " +
                    "WHERE package_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Database connection is null");
                return false;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, packageId);
                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error soft disabling package: " + packageId, e);
            throw new RuntimeException("Failed to disable package: " + e.getMessage(), e);
        }
    }

    /**
     * Map ResultSet to Package object
     */
    private Package mapResultSetToPackage(ResultSet rs) throws SQLException {
        Package pkg = new Package();
        
        pkg.setPackageId(rs.getLong("package_id"));
        pkg.setName(rs.getString("name"));
        pkg.setDurationMonths(rs.getInt("duration_months"));
        pkg.setPrice(rs.getBigDecimal("price"));
        
        String description = rs.getString("description");
        pkg.setDescription(description);
        
        int maxSessions = rs.getInt("max_sessions");
        if (rs.wasNull()) {
            pkg.setMaxSessions(null);
        } else {
            pkg.setMaxSessions(maxSessions);
        }
        
        // Handle MySQL TINYINT(1) as boolean - check both getBoolean and getInt for compatibility
        try {
            pkg.setIsActive(rs.getBoolean("is_active"));
        } catch (SQLException e) {
            // If getBoolean fails, try getInt
            int isActiveInt = rs.getInt("is_active");
            pkg.setIsActive(isActiveInt == 1);
        }
        
        Timestamp createdDate = rs.getTimestamp("created_date");
        if (createdDate != null) {
            pkg.setCreatedDate(createdDate.toLocalDateTime());
        }
        
        Timestamp updatedDate = rs.getTimestamp("updated_date");
        if (updatedDate != null) {
            pkg.setUpdatedDate(updatedDate.toLocalDateTime());
        }
        
        int gymId = rs.getInt("gym_id");
        if (rs.wasNull()) {
            pkg.setGymId(null);
        } else {
            pkg.setGymId(gymId);
        }
        
        return pkg;
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

    /**
     * Helper: Set Integer or null
     */
    private void setIntOrNull(PreparedStatement stmt, int index, Integer value) throws SQLException {
        if (value != null) {
            stmt.setInt(index, value);
        } else {
            stmt.setNull(index, Types.INTEGER);
        }
    }
}

