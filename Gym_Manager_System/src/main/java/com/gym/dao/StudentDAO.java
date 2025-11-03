package com.gym.dao;

import com.gym.model.Student;
import com.gym.util.DatabaseUtil;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Optional;

/**
 * StudentDAO - Data Access Object for students table
 * Handles student-specific information (health metrics, emergency contacts)
 */
public class StudentDAO {
    
    /**
     * Find student by user_id
     * NOTE: students table only has: user_id, weight, height, bmi, emergency_contact_*
     * Other info (full_name, email, phone...) should be fetched from user table via JOIN
     */
    public Optional<Student> findByUserId(int userId) {
        String sql = "SELECT user_id, weight, height, bmi, " +
                    "emergency_contact_name, emergency_contact_phone, " +
                    "emergency_contact_relation, emergency_contact_address " +
                    "FROM students WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO] ERROR finding student by user_id: " + e.getMessage());
            System.err.println("[StudentDAO] SQL State: " + e.getSQLState());
            System.err.println("[StudentDAO] User ID: " + userId);
            e.printStackTrace();
        }
        
        return Optional.empty();
    }
    
    /**
     * Upsert student record (INSERT if not exists, UPDATE if exists)
     * Uses ON DUPLICATE KEY UPDATE for MySQL
     * Note: This requires user_id to be PRIMARY KEY or have UNIQUE constraint in students table
     */
    public void upsert(Student student) {
        // First, check if record exists
        Optional<Student> existing = findByUserId(student.getUserId());
        
        if (existing.isPresent()) {
            // Update existing record
            update(student);
        } else {
            // Insert new record
            create(student);
        }
    }
    
    /**
     * Alternative upsert using ON DUPLICATE KEY UPDATE (requires PRIMARY KEY on user_id)
     * NOTE: Database only has basic columns: user_id, weight, height, bmi, emergency_contact_*
     */
    public void upsertWithDuplicateKey(Student student) {
        String sql = "INSERT INTO students (user_id, " +
                    "weight, height, bmi, " +
                    "emergency_contact_name, emergency_contact_phone, " +
                    "emergency_contact_relation, emergency_contact_address) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "weight = VALUES(weight), " +
                    "height = VALUES(height), " +
                    "bmi = VALUES(bmi), " +
                    "emergency_contact_name = VALUES(emergency_contact_name), " +
                    "emergency_contact_phone = VALUES(emergency_contact_phone), " +
                    "emergency_contact_relation = VALUES(emergency_contact_relation), " +
                    "emergency_contact_address = VALUES(emergency_contact_address)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            int paramIndex = 1;
            stmt.setInt(paramIndex++, student.getUserId());
            
            setBigDecimalOrNull(stmt, paramIndex++, student.getWeight());
            setBigDecimalOrNull(stmt, paramIndex++, student.getHeight());
            setBigDecimalOrNull(stmt, paramIndex++, student.getBmi());
            
            setStringOrNull(stmt, paramIndex++, student.getEmergencyContactName());
            setStringOrNull(stmt, paramIndex++, student.getEmergencyContactPhone());
            setStringOrNull(stmt, paramIndex++, student.getEmergencyContactRelation());
            setStringOrNull(stmt, paramIndex++, student.getEmergencyContactAddress());
            
            int affectedRows = stmt.executeUpdate();
            System.out.println("[StudentDAO] UpsertWithDuplicateKey affected rows: " + affectedRows);
            
        } catch (SQLException e) {
            System.err.println("[StudentDAO] ERROR in upsertWithDuplicateKey: " + e.getMessage());
            System.err.println("[StudentDAO] SQL State: " + e.getSQLState());
            System.err.println("[StudentDAO] Error Code: " + e.getErrorCode());
            System.err.println("[StudentDAO] User ID: " + student.getUserId());
            e.printStackTrace();
            throw new RuntimeException("Failed to save student profile: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create new student record
     * NOTE: students table only has: user_id, weight, height, bmi, emergency_contact_*
     * Other info (full_name, email, phone...) is stored in user table
     */
    public void create(Student student) {
        String sql = "INSERT INTO students (" +
                    "user_id, weight, height, bmi, " +
                    "emergency_contact_name, emergency_contact_phone, " +
                    "emergency_contact_relation, emergency_contact_address) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            int paramIndex = 1;
            stmt.setInt(paramIndex++, student.getUserId());
            
            // Physical info (only columns that exist in students table)
            setBigDecimalOrNull(stmt, paramIndex++, student.getWeight());
            setBigDecimalOrNull(stmt, paramIndex++, student.getHeight());
            setBigDecimalOrNull(stmt, paramIndex++, student.getBmi());
            
            // Emergency contact
            setStringOrNull(stmt, paramIndex++, student.getEmergencyContactName());
            setStringOrNull(stmt, paramIndex++, student.getEmergencyContactPhone());
            setStringOrNull(stmt, paramIndex++, student.getEmergencyContactRelation());
            setStringOrNull(stmt, paramIndex++, student.getEmergencyContactAddress());
            
            int affectedRows = stmt.executeUpdate();
            System.out.println("[StudentDAO] Create affected rows: " + affectedRows);
            
        } catch (SQLException e) {
            System.err.println("[StudentDAO] ERROR creating student: " + e.getMessage());
            System.err.println("[StudentDAO] SQL State: " + e.getSQLState());
            System.err.println("[StudentDAO] Error Code: " + e.getErrorCode());
            e.printStackTrace();
            throw new RuntimeException("Failed to create student profile: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update existing student record
     * NOTE: students table only has: user_id, weight, height, bmi, emergency_contact_*
     * Other info (full_name, email, phone...) should be updated in user table
     */
    public void update(Student student) {
        String sql = "UPDATE students SET " +
                    "weight = ?, height = ?, bmi = ?, " +
                    "emergency_contact_name = ?, emergency_contact_phone = ?, " +
                    "emergency_contact_relation = ?, emergency_contact_address = ? " +
                    "WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            int paramIndex = 1;
            // Physical info (only columns that exist in students table)
            setBigDecimalOrNull(stmt, paramIndex++, student.getWeight());
            setBigDecimalOrNull(stmt, paramIndex++, student.getHeight());
            setBigDecimalOrNull(stmt, paramIndex++, student.getBmi());
            
            // Emergency contact
            setStringOrNull(stmt, paramIndex++, student.getEmergencyContactName());
            setStringOrNull(stmt, paramIndex++, student.getEmergencyContactPhone());
            setStringOrNull(stmt, paramIndex++, student.getEmergencyContactRelation());
            setStringOrNull(stmt, paramIndex++, student.getEmergencyContactAddress());
            
            // WHERE clause
            stmt.setInt(paramIndex++, student.getUserId());
            
            int affectedRows = stmt.executeUpdate();
            System.out.println("[StudentDAO] Update affected rows: " + affectedRows);
            if (affectedRows == 0) {
                System.err.println("[StudentDAO] WARNING: No rows updated for user_id: " + student.getUserId());
            }
            
        } catch (SQLException e) {
            System.err.println("[StudentDAO] ERROR updating student: " + e.getMessage());
            System.err.println("[StudentDAO] SQL State: " + e.getSQLState());
            System.err.println("[StudentDAO] Error Code: " + e.getErrorCode());
            System.err.println("[StudentDAO] User ID: " + student.getUserId());
            e.printStackTrace();
            throw new RuntimeException("Failed to update student profile: " + e.getMessage(), e);
        }
    }
    
    /**
     * Calculate BMI from height (in cm) and weight (in kg)
     * BMI = weight (kg) / (height (m))^2
     */
    public BigDecimal calculateBMI(BigDecimal height, BigDecimal weight) {
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
     * Helper method to map ResultSet to Student object
     * NOTE: students table only has: user_id, weight, height, bmi, emergency_contact_*
     * Other fields (full_name, email, phone...) should be fetched from user table via JOIN
     */
    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        
        student.setUserId(rs.getInt("user_id"));
        
        // Handle null values for BigDecimal/FLOAT
        try {
            double weight = rs.getDouble("weight");
            student.setWeight(rs.wasNull() ? null : BigDecimal.valueOf(weight));
        } catch (SQLException e) {
            student.setWeight(null);
        }
        
        try {
            double height = rs.getDouble("height");
            student.setHeight(rs.wasNull() ? null : BigDecimal.valueOf(height));
        } catch (SQLException e) {
            student.setHeight(null);
        }
        
        try {
            double bmi = rs.getDouble("bmi");
            student.setBmi(rs.wasNull() ? null : BigDecimal.valueOf(bmi));
        } catch (SQLException e) {
            student.setBmi(null);
        }
        
        // Emergency contact
        student.setEmergencyContactName(rs.getString("emergency_contact_name"));
        student.setEmergencyContactPhone(rs.getString("emergency_contact_phone"));
        student.setEmergencyContactRelation(rs.getString("emergency_contact_relation"));
        student.setEmergencyContactAddress(rs.getString("emergency_contact_address"));
        
        // Other fields (full_name, email, phone...) are NOT in students table
        // These should be fetched from user table via JOIN in the service layer
        student.setFullName(null);
        student.setGender(null);
        student.setPhone(null);
        student.setEmail(null);
        student.setAddress(null);
        student.setAvatarUrl(null);
        student.setCurrentWeight(0);
        student.setTrainingPackage(null);
        student.setTrainingMonths(0);
        student.setSessionCount(0);
        student.setProgress(0);
        student.setPtNote(null);
        student.setActive(true); // Default
        
        return student;
    }
    
    /**
     * Helper: Set BigDecimal or null (converts to FLOAT for database)
     * NOTE: Database uses FLOAT type, not DECIMAL
     */
    private void setBigDecimalOrNull(PreparedStatement stmt, int index, BigDecimal value) throws SQLException {
        if (value != null) {
            stmt.setFloat(index, value.floatValue());
        } else {
            stmt.setNull(index, Types.FLOAT);
        }
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
     * Helper: Set Boolean with default false if null
     */
    private void setBooleanOrDefault(PreparedStatement stmt, int index, boolean value) throws SQLException {
        stmt.setBoolean(index, value);
    }
}
