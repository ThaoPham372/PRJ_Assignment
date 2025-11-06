package com.gym.service;

import com.gym.dao.StudentDAO;
import com.gym.model.Student;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * StudentService - Service layer for student operations
 * Handles business logic for student-specific information
 * Reusable service for all student-related operations
 */
public class StudentService {
    
    private final StudentDAO studentDAO;
    
    public StudentService() {
        this.studentDAO = new StudentDAO();
    }
    
    /**
     * Get student by user_id
     * Returns empty Student with userId set if not found
     * @param userId The user ID to find student for
     * @return Student object (empty with userId set if not found)
     */
    public Student getStudentByUserId(int userId) {
        Optional<Student> studentOpt = studentDAO.findByUserId(userId);
        if (studentOpt.isPresent()) {
            return studentOpt.get();
        } else {
            // Return empty student with userId set
            Student student = new Student();
            student.setUserIdAsInt(userId);
            return student;
        }
    }
    
    /**
     * Save student (upsert)
     * IMPORTANT: For new registrations, userId can be null - StudentDAO will handle creating new User+Student
     * For updates, userId must be valid
     * @param student The student to save
     */
    public void saveStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        
        // Allow userId to be null for new registrations (StudentDAO.upsert will handle it)
        // Only check if userId is explicitly set and invalid (negative)
        Integer userId = student.getUserId();
        if (userId != null && userId <= 0) {
            throw new IllegalArgumentException("Student userId must be positive if set");
        }
        
        // Calculate BMI if height and weight are provided
        // Convert Float to BigDecimal for calculation
        BigDecimal height = student.getHeightAsBigDecimal();
        BigDecimal weight = student.getWeightAsBigDecimal();
        
        if (height != null && weight != null) {
            BigDecimal bmi = studentDAO.calculateBMI(height, weight);
            student.setBmiFromBigDecimal(bmi);
            System.out.println("[StudentService] Calculated BMI: " + bmi + " for height=" + height + " weight=" + weight);
        }
        
        // Debug log before saving
        System.out.println("[StudentService] Saving student - User ID: " + (userId != null ? userId : "NULL (new registration)"));
        System.out.println("[StudentService] Username: " + student.getUsername());
        System.out.println("[StudentService] Name: " + student.getName());
        System.out.println("[StudentService] Email: " + student.getEmail());
        System.out.println("[StudentService] Height: " + student.getHeight() + ", Weight: " + student.getWeight());
        
        try {
            studentDAO.upsert(student);
            
            // Get userId after save (in case it was generated)
            Integer savedUserId = student.getUserId();
            System.out.println("[StudentService] Successfully saved student - User ID: " + (savedUserId != null ? savedUserId : "unknown"));
        } catch (Exception e) {
            System.err.println("[StudentService] ERROR saving student: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw to let caller handle
        }
    }
    
    /**
     * Update student from request parameters
     * NOTE: Only updates fields that exist in students table: weight, height, bmi, emergency_contact_*
     * Other fields (fullName, email, phone, address, avatarUrl, gender) should be updated in User table separately
     * @param student The student object to update
     * @param parameterMap Request parameter map
     */
    public void updateStudentFromRequest(Student student, java.util.Map<String, String[]> parameterMap) {
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
                BigDecimal height = new BigDecimal(heightStr);
                student.setHeightFromBigDecimal(height);
            } catch (NumberFormatException e) {
                System.err.println("[StudentService] Invalid height format: " + heightStr);
            }
        }
        
        String weightStr = getParameter(parameterMap, "weight");
        if (weightStr != null && !weightStr.trim().isEmpty()) {
            try {
                BigDecimal weight = new BigDecimal(weightStr);
                student.setWeightFromBigDecimal(weight);
            } catch (NumberFormatException e) {
                System.err.println("[StudentService] Invalid weight format: " + weightStr);
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
}

