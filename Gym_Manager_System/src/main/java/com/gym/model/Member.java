package com.gym.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Member entity representing extended member-specific information.
 * Inherits from User using JOINED inheritance strategy.
 * Links to User entity via user_id (shared primary key).
 */
@Entity
@Table(name = "members")
@DiscriminatorValue("MEMBER")
@PrimaryKeyJoinColumn(name = "user_id")
public class Member extends User {

    @Column(name = "weight")
    private Float weight;

    @Column(name = "height")
    private Float height;

    @Column(name = "bmi")
    private Float bmi;

    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone", length = 20)
    private String emergencyContactPhone;

    @Column(name = "emergency_contact_relation", length = 50)
    private String emergencyContactRelation;

    @Column(name = "emergency_contact_address", length = 255)
    private String emergencyContactAddress;

    // Gender is now in User table, not in Member table
    // Use getGender() and setGender() from parent User class

    // Getters & Setters
    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Float getBmi() {
        return bmi;
    }

    public void setBmi(Float bmi) {
        this.bmi = bmi;
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

    // Backward compatibility methods for existing code
    // These methods help maintain compatibility with existing DAO/Service code
    public int getUserIdAsInt() {
        Integer id = super.getUserId();
        return id != null ? id : 0;
    }

    public void setUserIdAsInt(int userId) {
        super.setUserId(userId);
    }

    public BigDecimal getWeightAsBigDecimal() {
        return weight != null ? BigDecimal.valueOf(weight) : null;
    }

    public void setWeightFromBigDecimal(BigDecimal weight) {
        this.weight = weight != null ? weight.floatValue() : null;
    }

    public BigDecimal getHeightAsBigDecimal() {
        return height != null ? BigDecimal.valueOf(height) : null;
    }

    public void setHeightFromBigDecimal(BigDecimal height) {
        this.height = height != null ? height.floatValue() : null;
    }

    public BigDecimal getBmiAsBigDecimal() {
        return bmi != null ? BigDecimal.valueOf(bmi) : null;
    }

    public void setBmiFromBigDecimal(BigDecimal bmi) {
        this.bmi = bmi != null ? bmi.floatValue() : null;
    }

    // Additional fields that might be in views/queries but not in members table
    // These are kept for backward compatibility but should not be persisted
    @Transient
    private BigDecimal targetWeight;

    @Transient
    private String fullName;

    @Transient
    private String phone;

    @Transient
    private String email;

    @Transient
    private String address;

    @Transient
    private String avatarUrl;

    @Transient
    private Integer currentWeight;

    @Transient
    private String trainingPackage;

    @Transient
    private Integer trainingMonths;

    @Transient
    private Integer sessionCount;

    @Transient
    private Integer progress;

    @Transient
    private String ptNote;

    @Transient
    private Boolean isActive;

    // Getters/setters for transient fields (backward compatibility)
    public BigDecimal getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(BigDecimal targetWeight) {
        this.targetWeight = targetWeight;
    }

    public String getFullName() {
        return fullName != null ? fullName : (super.getName() != null ? super.getName() : null);
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
        if (fullName != null && super.getName() == null) {
            super.setName(fullName);
        }
    }

    // Gender getter/setter removed - now using parent User.getGender() and User.setGender()
    // Gender is stored in User table, not Member table

    public String getPhone() {
        return phone != null ? phone : super.getPhone();
    }

    public void setPhone(String phone) {
        this.phone = phone;
        if (phone != null && super.getPhone() == null) {
            super.setPhone(phone);
        }
    }

    public String getEmail() {
        return email != null ? email : super.getEmail();
    }

    public void setEmail(String email) {
        this.email = email;
        if (email != null && super.getEmail() == null) {
            super.setEmail(email);
        }
    }

    public String getAddress() {
        return address != null ? address : super.getAddress();
    }

    public void setAddress(String address) {
        this.address = address;
        if (address != null && super.getAddress() == null) {
            super.setAddress(address);
        }
    }

    public String getAvatarUrl() {
        return avatarUrl != null ? avatarUrl : super.getAvatarUrl();
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        if (avatarUrl != null && super.getAvatarUrl() == null) {
            super.setAvatarUrl(avatarUrl);
        }
    }

    public Integer getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(Integer currentWeight) {
        this.currentWeight = currentWeight;
    }

    public String getTrainingPackage() {
        return trainingPackage;
    }

    public void setTrainingPackage(String trainingPackage) {
        this.trainingPackage = trainingPackage;
    }

    public Integer getTrainingMonths() {
        return trainingMonths;
    }

    public void setTrainingMonths(Integer trainingMonths) {
        this.trainingMonths = trainingMonths;
    }

    public Integer getSessionCount() {
        return sessionCount;
    }

    public void setSessionCount(Integer sessionCount) {
        this.sessionCount = sessionCount;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getPtNote() {
        return ptNote;
    }

    public void setPtNote(String ptNote) {
        this.ptNote = ptNote;
    }

    public boolean isActive() {
        return isActive != null ? isActive : ("ACTIVE".equals(super.getStatus()));
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

