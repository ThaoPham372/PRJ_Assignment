package com.gym.dao;

import com.gym.model.Student;
import com.gym.model.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * StudentDAO - Data Access Object for students table using JPA
 * Extends GenericDAO for basic CRUD operations
 * Handles student-specific information (health metrics, emergency contacts)
 */
public class StudentDAO extends GenericDAO<Student> {

    public StudentDAO() {
        super(Student.class);
    }

    /**
     * Find student by user_id
     * NOTE: students table only has: user_id, weight, height, bmi, emergency_contact_*
     * Other info (full_name, email, phone...) should be fetched from user table via JOIN
     * Uses JPA find() which will load Student entity if it exists (JOINED inheritance)
     */
    public Optional<Student> findByUserId(int userId) {
        try {
            // Use EntityManager.find() to load Student entity
            // Since Student extends User with JOINED inheritance, 
            // Hibernate will automatically JOIN the students table
            Student student = em.find(Student.class, userId);
            return Optional.ofNullable(student);
        } catch (Exception e) {
            System.err.println("[StudentDAO] ERROR finding student by user_id: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Upsert student record (INSERT if not exists, UPDATE if exists)
     * Uses JPA merge which handles both insert and update
     * IMPORTANT: For JOINED inheritance:
     * - If userId is null: Creates new Student (JPA will create User record with DTYPE='STUDENT' and Student record)
     * - If userId exists: Updates existing Student or creates Student for existing User
     */
    public void upsert(Student student) {
        try {
            if (student == null) {
                throw new IllegalArgumentException("Student cannot be null");
            }
            
            beginTransaction();
            
            Integer userId = student.getUserId();
            
            if (userId != null && userId > 0) {
                // Check if Student already exists
                Student existing = em.find(Student.class, userId);
                
                if (existing != null) {
                    // Update existing student - merge changes from incoming student object
                    System.out.println("[StudentDAO] Found existing Student, updating fields...");
                    existing.setWeight(student.getWeight());
                    existing.setHeight(student.getHeight());
                    
                    // Calculate BMI if height and weight are provided
                    if (existing.getHeight() != null && existing.getWeight() != null) {
                        BigDecimal heightBD = BigDecimal.valueOf(existing.getHeight());
                        BigDecimal weightBD = BigDecimal.valueOf(existing.getWeight());
                        BigDecimal bmi = calculateBMI(heightBD, weightBD);
                        existing.setBmi(bmi != null ? bmi.floatValue() : null);
                    } else {
                        existing.setBmi(student.getBmi());
                    }
                    
                    // Gender is now in User table, not Student table - don't copy it here
                    existing.setEmergencyContactName(student.getEmergencyContactName());
                    existing.setEmergencyContactPhone(student.getEmergencyContactPhone());
                    existing.setEmergencyContactRelation(student.getEmergencyContactRelation());
                    existing.setEmergencyContactAddress(student.getEmergencyContactAddress());
                    
                    // Merge to persist changes (existing is already managed by EntityManager)
                    em.merge(existing);
                    System.out.println("[StudentDAO] Updated existing Student record with user_id: " + userId);
                } else {
                    // User exists but Student doesn't - use native SQL to insert into students table only
                    // This avoids JPA detached entity issues
                    System.out.println("[StudentDAO] Student not found, creating Student record for existing User...");
                    
                    // Verify User exists
                    User user = em.find(User.class, userId);
                    if (user == null) {
                        throw new RuntimeException("Cannot create Student: User with ID " + userId + " not found");
                    }
                    
                    // Calculate BMI if height and weight are provided
                    Float bmi = null;
                    if (student.getHeight() != null && student.getWeight() != null) {
                        BigDecimal heightBD = BigDecimal.valueOf(student.getHeight());
                        BigDecimal weightBD = BigDecimal.valueOf(student.getWeight());
                        BigDecimal bmiCalc = calculateBMI(heightBD, weightBD);
                        bmi = (bmiCalc != null) ? bmiCalc.floatValue() : null;
                    }
                    
                    // Insert directly into students table using native SQL
                    String sql = "INSERT INTO students (user_id, weight, height, bmi, " +
                                "emergency_contact_name, emergency_contact_phone, " +
                                "emergency_contact_relation, emergency_contact_address) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    
                    Query query = em.createNativeQuery(sql);
                    query.setParameter(1, userId);
                    query.setParameter(2, student.getWeight());
                    query.setParameter(3, student.getHeight());
                    query.setParameter(4, bmi);
                    query.setParameter(5, student.getEmergencyContactName());
                    query.setParameter(6, student.getEmergencyContactPhone());
                    query.setParameter(7, student.getEmergencyContactRelation());
                    query.setParameter(8, student.getEmergencyContactAddress());
                    
                    int rowsInserted = query.executeUpdate();
                    System.out.println("[StudentDAO] Created Student record for existing User with user_id: " + userId + 
                                     " (rows inserted: " + rowsInserted + ")");
                }
            } else {
                // userId is null - this is a new Student registration
                // JPA will create User record with DTYPE='STUDENT' and Student record with generated user_id
                System.out.println("[StudentDAO] ========================================");
                System.out.println("[StudentDAO] STEP 1: Persisting new Student (userId is NULL)");
                System.out.println("[StudentDAO]   - Student username: " + student.getUsername());
                System.out.println("[StudentDAO]   - Student email: " + student.getEmail());
                System.out.println("[StudentDAO]   - Student userId BEFORE persist: " + student.getUserId());
                
                // STEP 1: Persist Student - JPA will schedule INSERT into user and students tables
                em.persist(student);
                System.out.println("[StudentDAO] STEP 2: After persist() - userId is still: " + student.getUserId());
                
                // STEP 2: Flush to execute INSERT statement immediately
                // This triggers database INSERT and MySQL AUTO_INCREMENT generates user_id
                em.flush();
                System.out.println("[StudentDAO] STEP 3: After flush() - INSERT executed, userId now: " + student.getUserId());
                
                // STEP 3: Refresh to reload entity from database (get generated ID)
                // With GenerationType.IDENTITY, userId should be set after flush()
                // But refresh() ensures we have the latest state from DB
                em.refresh(student);
                userId = student.getUserId();
                System.out.println("[StudentDAO] STEP 4: After refresh() - userId from DB: " + userId);
                System.out.println("[StudentDAO] ========================================");
                
                if (userId != null) {
                    System.out.println("[StudentDAO] ✓ SUCCESS: Created new Student (and User) with generated user_id: " + userId);
                } else {
                    System.err.println("[StudentDAO] ✗ WARNING: Student persisted but userId is still null!");
                    System.err.println("[StudentDAO] This should not happen with GenerationType.IDENTITY");
                }
            }
            
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            System.err.println("[StudentDAO] ERROR in upsert: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save student profile: " + e.getMessage(), e);
        }
    }

    /**
     * Alternative upsert using ON DUPLICATE KEY UPDATE (requires PRIMARY KEY on user_id)
     * Uses native SQL for MySQL-specific behavior
     */
    public void upsertWithDuplicateKey(Student student) {
        try {
            String sql = "INSERT INTO students (user_id, " +
                        "weight, height, bmi, " +
                        "emergency_contact_name, emergency_contact_phone, " +
                        "emergency_contact_relation, emergency_contact_address) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "weight = VALUES(weight), " +
                        "height = VALUES(height), " +
                        "bmi = VALUES(bmi), " +
                        "emergency_contact_name = VALUES(emergency_contact_name), " +
                        "emergency_contact_phone = VALUES(emergency_contact_phone), " +
                        "emergency_contact_relation = VALUES(emergency_contact_relation), " +
                        "emergency_contact_address = VALUES(emergency_contact_address)";

            beginTransaction();
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, student.getUserId());
            query.setParameter(2, student.getWeight());
            query.setParameter(3, student.getHeight());
            query.setParameter(4, student.getBmi());
            query.setParameter(5, student.getEmergencyContactName());
            query.setParameter(6, student.getEmergencyContactPhone());
            query.setParameter(7, student.getEmergencyContactRelation());
            query.setParameter(8, student.getEmergencyContactAddress());
            query.executeUpdate();
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            System.err.println("[StudentDAO] ERROR in upsertWithDuplicateKey: " + e.getMessage());
            throw new RuntimeException("Failed to save student profile: " + e.getMessage(), e);
        }
    }

    /**
     * Create new student record
     * Uses GenericDAO save() method
     * NOTE: students table only has: user_id, weight, height, bmi, emergency_contact_*
     */
    public void create(Student student) {
        try {
            save(student);
        } catch (Exception e) {
            System.err.println("[StudentDAO] ERROR creating student: " + e.getMessage());
            throw new RuntimeException("Failed to create student profile: " + e.getMessage(), e);
        }
    }

    /**
     * Update existing student record
     * Uses GenericDAO update() method
     * NOTE: students table only has: user_id, weight, height, bmi, emergency_contact_*
     */
    public boolean updateStudent(Student student) {
        try {
            int result = super.update(student);
            return result != -1;
        } catch (Exception e) {
            System.err.println("[StudentDAO] ERROR updating student: " + e.getMessage());
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
     * Find all students
     */
    public java.util.List<Student> findAllStudents() {
        return findAll();
    }

    /**
     * Find students with weight/height data
     */
    public java.util.List<Student> findStudentsWithHealthData() {
        try {
            TypedQuery<Student> query = em.createQuery(
                "SELECT s FROM Student s WHERE s.weight IS NOT NULL AND s.height IS NOT NULL",
                Student.class
            );
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("[StudentDAO] Error finding students with health data: " + e.getMessage());
            return List.of();
        }
    }
}
