package com.gym.service;

import com.gym.dao.StudentDAO;
import com.gym.model.Student;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * StudentProfileService - Service layer for student profile operations
 * Handles business logic for student-specific information
 */
public class StudentProfileService {
    
    private final StudentDAO studentDAO;
    
    public StudentProfileService() {
        this.studentDAO = new StudentDAO();
    }
    
    /**
     * Get student profile by user_id
     * Returns empty Student with userId set if not found
     */
    public Student getProfile(int userId) {
        Optional<Student> studentOpt = studentDAO.findByUserId(userId);
        if (studentOpt.isPresent()) {
            return studentOpt.get();
        } else {
            // Return empty student with userId set
            Student student = new Student();
            student.setUserId(userId);
            return student;
        }
    }
    
    /**
     * Save student profile (upsert)
     */
    public void saveProfile(Student student) {
        if (student == null || student.getUserId() <= 0) {
            throw new IllegalArgumentException("Student must have a valid userId");
        }
        
        // Calculate BMI if height and weight are provided
        if (student.getHeight() != null && student.getWeight() != null) {
            BigDecimal bmi = studentDAO.calculateBMI(student.getHeight(), student.getWeight());
            student.setBmi(bmi);
            System.out.println("[StudentProfileService] Calculated BMI: " + bmi + " for height=" + student.getHeight() + " weight=" + student.getWeight());
        }
        
        // Debug log before saving
        System.out.println("[StudentProfileService] Saving student profile - User ID: " + student.getUserId());
        System.out.println("[StudentProfileService] Height: " + student.getHeight() + ", Weight: " + student.getWeight());
        System.out.println("[StudentProfileService] Avatar URL: " + student.getAvatarUrl());
        System.out.println("[StudentProfileService] Full Name: " + student.getFullName());
        
        try {
            studentDAO.upsert(student);
            System.out.println("[StudentProfileService] Successfully saved student profile for user ID: " + student.getUserId());
        } catch (Exception e) {
            System.err.println("[StudentProfileService] ERROR saving student profile: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw to let caller handle
        }
    }
    
    /**
     * Update student profile from request parameters
     * NOTE: Only updates fields that exist in students table: weight, height, bmi, emergency_contact_*
     * Other fields (fullName, email, phone, address, avatarUrl, gender) should be updated in User table separately
     */
    public void updateProfileFromRequest(Student student, java.util.Map<String, String[]> parameterMap) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        
        // NOTE: Basic info fields (fullName, email, phone, address, avatarUrl, gender) are NOT in students table
        // These should be updated in User table separately, not here
        // We'll only update fields that exist in students table: weight, height, bmi, emergency_contact_*
        
        // Update physical info (exists in students table)
        String heightStr = getParameter(parameterMap, "height");
        if (heightStr != null && !heightStr.trim().isEmpty()) {
            try {
                student.setHeight(new BigDecimal(heightStr));
            } catch (NumberFormatException e) {
                System.err.println("[StudentProfileService] Invalid height format: " + heightStr);
            }
        }
        
        String weightStr = getParameter(parameterMap, "weight");
        if (weightStr != null && !weightStr.trim().isEmpty()) {
            try {
                student.setWeight(new BigDecimal(weightStr));
            } catch (NumberFormatException e) {
                System.err.println("[StudentProfileService] Invalid weight format: " + weightStr);
            }
        }
        
        // NOTE: target_weight is NOT in students table (only weight, height, bmi, emergency_contact_*)
        // Removing targetWeight update since it doesn't exist in database
        
        // Update emergency contact
        String emergencyContactName = getParameter(parameterMap, "emergencyContactName");
        if (emergencyContactName != null) student.setEmergencyContactName(emergencyContactName);
        
        String emergencyContactPhone = getParameter(parameterMap, "emergencyContactPhone");
        if (emergencyContactPhone != null) student.setEmergencyContactPhone(emergencyContactPhone);
        
        String emergencyContactRelation = getParameter(parameterMap, "emergencyContactRelation");
        if (emergencyContactRelation != null) student.setEmergencyContactRelation(emergencyContactRelation);
        
        String emergencyContactAddress = getParameter(parameterMap, "emergencyContactAddress");
        if (emergencyContactAddress != null) student.setEmergencyContactAddress(emergencyContactAddress);
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
     * Get BMI category
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
}
