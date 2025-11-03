package com.gym.model;

import java.math.BigDecimal;

/**
 * Student entity representing extended student-specific information.
 * Links to User entity via user_id (1-1 relationship).
 */
public class Student {
    
    private int userId;  // FK to user.user_id (PK in students table)
    private BigDecimal weight;  // DECIMAL(5,2)
    private BigDecimal height;  // DECIMAL(5,2)
    private BigDecimal bmi;     // DECIMAL(4,2) - may be generated column in DB
    private BigDecimal targetWeight;  // DECIMAL(5,2)
    
    // Emergency contact fields
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelation;
    private String emergencyContactAddress;
    
    // Additional student fields (from schema)
    private String fullName;
    private String gender;
    private String phone;
    private String email;
    private String address;
    private String avatarUrl;
    private int currentWeight;  // if different from weight
    private String trainingPackage;
    private int trainingMonths;
    private int sessionCount;
    private int progress;
    private String ptNote;
    private boolean isActive;
    
    // Getters & Setters
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public BigDecimal getWeight() {
        return weight;
    }
    
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
    
    public BigDecimal getHeight() {
        return height;
    }
    
    public void setHeight(BigDecimal height) {
        this.height = height;
    }
    
    public BigDecimal getBmi() {
        return bmi;
    }
    
    public void setBmi(BigDecimal bmi) {
        this.bmi = bmi;
    }
    
    public BigDecimal getTargetWeight() {
        return targetWeight;
    }
    
    public void setTargetWeight(BigDecimal targetWeight) {
        this.targetWeight = targetWeight;
    }
    
    public String getEmergencyContactName() {
        return emergencyContactName;
    }
    
    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }
    
    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }
    
    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }
    
    public String getEmergencyContactRelation() {
        return emergencyContactRelation;
    }
    
    public void setEmergencyContactRelation(String emergencyContactRelation) {
        this.emergencyContactRelation = emergencyContactRelation;
    }
    
    public String getEmergencyContactAddress() {
        return emergencyContactAddress;
    }
    
    public void setEmergencyContactAddress(String emergencyContactAddress) {
        this.emergencyContactAddress = emergencyContactAddress;
    }
    
    // Additional getters/setters for schema compatibility
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getAvatarUrl() {
        return avatarUrl;
    }
    
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
    public int getCurrentWeight() {
        return currentWeight;
    }
    
    public void setCurrentWeight(int currentWeight) {
        this.currentWeight = currentWeight;
    }
    
    public String getTrainingPackage() {
        return trainingPackage;
    }
    
    public void setTrainingPackage(String trainingPackage) {
        this.trainingPackage = trainingPackage;
    }
    
    public int getTrainingMonths() {
        return trainingMonths;
    }
    
    public void setTrainingMonths(int trainingMonths) {
        this.trainingMonths = trainingMonths;
    }
    
    public int getSessionCount() {
        return sessionCount;
    }
    
    public void setSessionCount(int sessionCount) {
        this.sessionCount = sessionCount;
    }
    
    public int getProgress() {
        return progress;
    }
    
    public void setProgress(int progress) {
        this.progress = progress;
    }
    
    public String getPtNote() {
        return ptNote;
    }
    
    public void setPtNote(String ptNote) {
        this.ptNote = ptNote;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
}


