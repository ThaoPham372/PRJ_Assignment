package com.gym.service;

import com.gym.dao.MemberDAO;
import com.gym.model.Member;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * MemberService - Service layer for member operations
 * Handles business logic for member-specific information
 * Reusable service for all member-related operations
 */
public class MemberService {
    
    private final MemberDAO memberDAO;
    
    public MemberService() {
        this.memberDAO = new MemberDAO();
    }
    
    /**
     * Get member by user_id
     * Returns empty Member with userId set if not found
     * @param userId The user ID to find member for
     * @return Member object (empty with userId set if not found)
     */
    public Member getMemberByUserId(int userId) {
        Optional<Member> memberOpt = memberDAO.findByUserId(userId);
        if (memberOpt.isPresent()) {
            return memberOpt.get();
        } else {
            // Return empty member with userId set
            Member member = new Member();
            member.setUserIdAsInt(userId);
            return member;
        }
    }
    
    /**
     * Save member (upsert)
     * IMPORTANT: For new registrations, userId can be null - MemberDAO will handle creating new User+Member
     * For updates, userId must be valid
     * @param member The member to save
     */
    public void saveMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }
        
        // Allow userId to be null for new registrations (MemberDAO.upsert will handle it)
        // Only check if userId is explicitly set and invalid (negative)
        Integer userId = member.getUserId();
        if (userId != null && userId <= 0) {
            throw new IllegalArgumentException("Member userId must be positive if set");
        }
        
        // Calculate BMI if height and weight are provided
        // Convert Float to BigDecimal for calculation
        BigDecimal height = member.getHeightAsBigDecimal();
        BigDecimal weight = member.getWeightAsBigDecimal();
        
        if (height != null && weight != null) {
            BigDecimal bmi = memberDAO.calculateBMI(height, weight);
            member.setBmiFromBigDecimal(bmi);
            System.out.println("[MemberService] Calculated BMI: " + bmi + " for height=" + height + " weight=" + weight);
        }
        
        // Debug log before saving
        System.out.println("[MemberService] Saving member - User ID: " + (userId != null ? userId : "NULL (new registration)"));
        System.out.println("[MemberService] Username: " + member.getUsername());
        System.out.println("[MemberService] Name: " + member.getName());
        System.out.println("[MemberService] Email: " + member.getEmail());
        System.out.println("[MemberService] Height: " + member.getHeight() + ", Weight: " + member.getWeight());
        
        try {
            memberDAO.upsert(member);
            
            // Get userId after save (in case it was generated)
            Integer savedUserId = member.getUserId();
            System.out.println("[MemberService] Successfully saved member - User ID: " + (savedUserId != null ? savedUserId : "unknown"));
        } catch (Exception e) {
            System.err.println("[MemberService] ERROR saving member: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw to let caller handle
        }
    }
    
    /**
     * Update member from request parameters
     * NOTE: Only updates fields that exist in members table: weight, height, bmi, emergency_contact_*
     * Other fields (fullName, email, phone, address, avatarUrl, gender) should be updated in User table separately
     * @param member The member object to update
     * @param parameterMap Request parameter map
     */
    public void updateMemberFromRequest(Member member, java.util.Map<String, String[]> parameterMap) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }
        
        // NOTE: Basic info fields (fullName, email, phone, address, avatarUrl, gender) are NOT in members table
        // These should be updated in User table separately, not here
        // We'll only update fields that exist in members table: weight, height, bmi, emergency_contact_*
        
        // Update physical info (exists in members table)
        String heightStr = getParameter(parameterMap, "height");
        if (heightStr != null && !heightStr.trim().isEmpty()) {
            try {
                BigDecimal height = new BigDecimal(heightStr);
                member.setHeightFromBigDecimal(height);
            } catch (NumberFormatException e) {
                System.err.println("[MemberService] Invalid height format: " + heightStr);
            }
        }
        
        String weightStr = getParameter(parameterMap, "weight");
        if (weightStr != null && !weightStr.trim().isEmpty()) {
            try {
                BigDecimal weight = new BigDecimal(weightStr);
                member.setWeightFromBigDecimal(weight);
            } catch (NumberFormatException e) {
                System.err.println("[MemberService] Invalid weight format: " + weightStr);
            }
        }
        
        // NOTE: target_weight is NOT in members table (only weight, height, bmi, emergency_contact_*)
        // Removing targetWeight update since it doesn't exist in database
        
        // Update emergency contact
        String emergencyContactName = getParameter(parameterMap, "emergencyContactName");
        if (emergencyContactName != null) member.setEmergencyContactName(emergencyContactName);
        
        String emergencyContactPhone = getParameter(parameterMap, "emergencyContactPhone");
        if (emergencyContactPhone != null) member.setEmergencyContactPhone(emergencyContactPhone);
        
        String emergencyContactRelation = getParameter(parameterMap, "emergencyContactRelation");
        if (emergencyContactRelation != null) member.setEmergencyContactRelation(emergencyContactRelation);
        
        String emergencyContactAddress = getParameter(parameterMap, "emergencyContactAddress");
        if (emergencyContactAddress != null) member.setEmergencyContactAddress(emergencyContactAddress);
    }
    
    /**
     * Helper: Get parameter from map
     */
    private String getParameter(java.util.Map<String, String[]> parameterMap, String key) {
        String[] values = parameterMap.get(key);
        if (values != null && values.length > 0 && values[0] != null && !values[0].trim().isEmpty()) {
            return values[0].trim();
        }
        return null;
    }
    
    /**
     * Get BMI category based on BMI value
     * @param bmi The BMI value
     * @return BMI category string (Underweight, Normal, Overweight, Obese) or null if bmi is null
     */
    public String getBMICategory(BigDecimal bmi) {
        if (bmi == null) {
            return null;
        }
        
        double bmiValue = bmi.doubleValue();
        if (bmiValue < 18.5) {
            return "Underweight";
        } else if (bmiValue < 25) {
            return "Normal";
        } else if (bmiValue < 30) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    public List<Member> getAll() {
        return memberDAO.findAllMembers();
    }
}


