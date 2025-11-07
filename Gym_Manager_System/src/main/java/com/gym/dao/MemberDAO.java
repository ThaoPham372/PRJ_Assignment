package com.gym.dao;

import com.gym.model.Member;
import com.gym.model.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * MemberDAO - Data Access Object for members table using JPA
 * Extends GenericDAO for basic CRUD operations
 * Handles member-specific information (health metrics, emergency contacts)
 */
public class MemberDAO extends GenericDAO<Member> {

    public MemberDAO() {
        super(Member.class);
    }

    /**
     * Find member by user_id
     * NOTE: members table only has: user_id, weight, height, bmi, emergency_contact_*
     * Other info (full_name, email, phone...) should be fetched from user table via JOIN
     * Uses JPA find() which will load Member entity if it exists (JOINED inheritance)
     */
    public Optional<Member> findByUserId(int userId) {
        try {
            // Use EntityManager.find() to load Member entity
            // Since Member extends User with JOINED inheritance, 
            // Hibernate will automatically JOIN the members table
            Member member = em.find(Member.class, userId);
            return Optional.ofNullable(member);
        } catch (Exception e) {
            System.err.println("[MemberDAO] ERROR finding member by user_id: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Upsert member record (INSERT if not exists, UPDATE if exists)
     * Uses JPA merge which handles both insert and update
     * IMPORTANT: For JOINED inheritance:
     * - If userId is null: Creates new Member (JPA will create User record with DTYPE='MEMBER' and Member record)
     * - If userId exists: Updates existing Member or creates Member for existing User
     */
    public void upsert(Member member) {
        try {
            if (member == null) {
                throw new IllegalArgumentException("Member cannot be null");
            }
            
            beginTransaction();
            
            Integer userId = member.getUserId();
            
            if (userId != null && userId > 0) {
                // Check if Member already exists
                Member existing = em.find(Member.class, userId);
                
                if (existing != null) {
                    // Update existing member - merge changes from incoming member object
                    System.out.println("[MemberDAO] Found existing Member, updating fields...");
                    existing.setWeight(member.getWeight());
                    existing.setHeight(member.getHeight());
                    
                    // Calculate BMI if height and weight are provided
                    if (existing.getHeight() != null && existing.getWeight() != null) {
                        BigDecimal heightBD = BigDecimal.valueOf(existing.getHeight());
                        BigDecimal weightBD = BigDecimal.valueOf(existing.getWeight());
                        BigDecimal bmi = calculateBMI(heightBD, weightBD);
                        existing.setBmi(bmi != null ? bmi.floatValue() : null);
                    } else {
                        existing.setBmi(member.getBmi());
                    }
                    
                    // Gender is now in User table, not Member table - don't copy it here
                    existing.setEmergencyContactName(member.getEmergencyContactName());
                    existing.setEmergencyContactPhone(member.getEmergencyContactPhone());
                    existing.setEmergencyContactRelation(member.getEmergencyContactRelation());
                    existing.setEmergencyContactAddress(member.getEmergencyContactAddress());
                    
                    // Merge to persist changes (existing is already managed by EntityManager)
                    em.merge(existing);
                    System.out.println("[MemberDAO] Updated existing Member record with user_id: " + userId);
                } else {
                    // User exists but Member doesn't - use native SQL to insert into members table only
                    // This avoids JPA detached entity issues
                    System.out.println("[MemberDAO] Member not found, creating Member record for existing User...");
                    
                    // Verify User exists
                    User user = em.find(User.class, userId);
                    if (user == null) {
                        throw new RuntimeException("Cannot create Member: User with ID " + userId + " not found");
                    }
                    
                    // Calculate BMI if height and weight are provided
                    Float bmi = null;
                    if (member.getHeight() != null && member.getWeight() != null) {
                        BigDecimal heightBD = BigDecimal.valueOf(member.getHeight());
                        BigDecimal weightBD = BigDecimal.valueOf(member.getWeight());
                        BigDecimal bmiCalc = calculateBMI(heightBD, weightBD);
                        bmi = (bmiCalc != null) ? bmiCalc.floatValue() : null;
                    }
                    
                    // Insert directly into members table using native SQL
                    String sql = "INSERT INTO members (user_id, weight, height, bmi, " +
                                "emergency_contact_name, emergency_contact_phone, " +
                                "emergency_contact_relation, emergency_contact_address) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    
                    Query query = em.createNativeQuery(sql);
                    query.setParameter(1, userId);
                    query.setParameter(2, member.getWeight());
                    query.setParameter(3, member.getHeight());
                    query.setParameter(4, bmi);
                    query.setParameter(5, member.getEmergencyContactName());
                    query.setParameter(6, member.getEmergencyContactPhone());
                    query.setParameter(7, member.getEmergencyContactRelation());
                    query.setParameter(8, member.getEmergencyContactAddress());
                    
                    int rowsInserted = query.executeUpdate();
                    System.out.println("[MemberDAO] Created Member record for existing User with user_id: " + userId + 
                                     " (rows inserted: " + rowsInserted + ")");
                }
            } else {
                // userId is null - this is a new Member registration
                // JPA will create User record with DTYPE='MEMBER' and Member record with generated user_id
                System.out.println("[MemberDAO] ========================================");
                System.out.println("[MemberDAO] STEP 1: Persisting new Member (userId is NULL)");
                System.out.println("[MemberDAO]   - Member username: " + member.getUsername());
                System.out.println("[MemberDAO]   - Member email: " + member.getEmail());
                System.out.println("[MemberDAO]   - Member userId BEFORE persist: " + member.getUserId());
                
                // STEP 1: Persist Member - JPA will schedule INSERT into user and members tables
                em.persist(member);
                System.out.println("[MemberDAO] STEP 2: After persist() - userId is still: " + member.getUserId());
                
                // STEP 2: Flush to execute INSERT statement immediately
                // This triggers database INSERT and MySQL AUTO_INCREMENT generates user_id
                em.flush();
                System.out.println("[MemberDAO] STEP 3: After flush() - INSERT executed, userId now: " + member.getUserId());
                
                // STEP 3: Refresh to reload entity from database (get generated ID)
                // With GenerationType.IDENTITY, userId should be set after flush()
                // But refresh() ensures we have the latest state from DB
                em.refresh(member);
                userId = member.getUserId();
                System.out.println("[MemberDAO] STEP 4: After refresh() - userId from DB: " + userId);
                System.out.println("[MemberDAO] ========================================");
                
                if (userId != null) {
                    System.out.println("[MemberDAO] ✓ SUCCESS: Created new Member (and User) with generated user_id: " + userId);
                } else {
                    System.err.println("[MemberDAO] ✗ WARNING: Member persisted but userId is still null!");
                    System.err.println("[MemberDAO] This should not happen with GenerationType.IDENTITY");
                }
            }
            
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            System.err.println("[MemberDAO] ERROR in upsert: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save member profile: " + e.getMessage(), e);
        }
    }

    /**
     * Alternative upsert using ON DUPLICATE KEY UPDATE (requires PRIMARY KEY on user_id)
     * Uses native SQL for MySQL-specific behavior
     */
    public void upsertWithDuplicateKey(Member member) {
        try {
            String sql = "INSERT INTO members (user_id, " +
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
            query.setParameter(1, member.getUserId());
            query.setParameter(2, member.getWeight());
            query.setParameter(3, member.getHeight());
            query.setParameter(4, member.getBmi());
            query.setParameter(5, member.getEmergencyContactName());
            query.setParameter(6, member.getEmergencyContactPhone());
            query.setParameter(7, member.getEmergencyContactRelation());
            query.setParameter(8, member.getEmergencyContactAddress());
            query.executeUpdate();
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            System.err.println("[MemberDAO] ERROR in upsertWithDuplicateKey: " + e.getMessage());
            throw new RuntimeException("Failed to save member profile: " + e.getMessage(), e);
        }
    }

    /**
     * Create new member record
     * Uses GenericDAO save() method
     * NOTE: members table only has: user_id, weight, height, bmi, emergency_contact_*
     */
    public void create(Member member) {
        try {
            save(member);
        } catch (Exception e) {
            System.err.println("[MemberDAO] ERROR creating member: " + e.getMessage());
            throw new RuntimeException("Failed to create member profile: " + e.getMessage(), e);
        }
    }

    /**
     * Update existing member record
     * Uses GenericDAO update() method
     * NOTE: members table only has: user_id, weight, height, bmi, emergency_contact_*
     */
    public boolean updateMember(Member member) {
        try {
            int result = super.update(member);
            return result != -1;
        } catch (Exception e) {
            System.err.println("[MemberDAO] ERROR updating member: " + e.getMessage());
            throw new RuntimeException("Failed to update member profile: " + e.getMessage(), e);
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
     * Find all members
     */
    public java.util.List<Member> findAllMembers() {
        return findAll();
    }

    /**
     * Find members with weight/height data
     */
    public java.util.List<Member> findMembersWithHealthData() {
        try {
            TypedQuery<Member> query = em.createQuery(
                "SELECT m FROM Member m WHERE m.weight IS NOT NULL AND m.height IS NOT NULL",
                Member.class
            );
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("[MemberDAO] Error finding members with health data: " + e.getMessage());
            return List.of();
        }
    }
}


