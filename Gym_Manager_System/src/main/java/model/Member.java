package model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Member model - Đại diện cho thành viên phòng gym
 */
public class Member {
    private int memberId;
    private int userId; // Foreign key to User
    
    // Thông tin cơ bản
    private String memberCode; // Mã thành viên (VD: GYM2024001)
    private Date registrationDate;
    private String status; // active, inactive, suspended, expired
    
    // Thông tin thể chất
    private Double height; // cm
    private Double weight; // kg
    private Double bmi; // Body Mass Index
    private String bloodType; // A, B, AB, O
    private String medicalConditions; // Tình trạng sức khỏe đặc biệt
    private String allergies; // Dị ứng
    private String injuries; // Chấn thương
    
    // Mục tiêu tập luyện
    private String fitnessGoal; // lose_weight, gain_weight, maintain, build_muscle, improve_health
    private Double targetWeight;
    private String activityLevel; // sedentary, light, moderate, active, very_active
    
    // Membership Package
    private Integer membershipPackageId; // Foreign key to MembershipPackage
    private Date packageStartDate;
    private Date packageEndDate;
    private Integer remainingDays;
    
    // Thông tin người thân khẩn cấp
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelation;
    private String emergencyContactAddress;
    
    // Thông tin huấn luyện
    private Integer assignedCoachId; // Foreign key to Coach
    private String preferredTrainingTime; // morning, afternoon, evening
    private String trainingFrequency; // Số lần/tuần
    
    // Thống kê
    private Integer totalWorkoutSessions;
    private Integer currentStreak; // Số ngày tập liên tiếp
    private Integer longestStreak;
    private BigDecimal totalCaloriesBurned;
    private Date lastWorkoutDate;
    
    // Ghi chú
    private String notes;
  
    // Metadata
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;

    // Constructors
    public Member() {
        this.registrationDate = new Date();
        this.status = "active";
        this.totalWorkoutSessions = 0;
        this.currentStreak = 0;
        this.longestStreak = 0;
        this.totalCaloriesBurned = BigDecimal.ZERO;
    }

    public Member(int userId, String memberCode) {
        this();
        this.userId = userId;
        this.memberCode = memberCode;
    }

    // Getters and Setters
    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
        calculateBMI();
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
        calculateBMI();
    }

    public Double getBmi() {
        return bmi;
    }

    public void setBmi(Double bmi) {
        this.bmi = bmi;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getMedicalConditions() {
        return medicalConditions;
    }

    public void setMedicalConditions(String medicalConditions) {
        this.medicalConditions = medicalConditions;
    }

    public String getFitnessGoal() {
        return fitnessGoal;
    }

    public void setFitnessGoal(String fitnessGoal) {
        this.fitnessGoal = fitnessGoal;
    }

    public Double getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(Double targetWeight) {
        this.targetWeight = targetWeight;
    }

    public String getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    public Integer getMembershipPackageId() {
        return membershipPackageId;
    }

    public void setMembershipPackageId(Integer membershipPackageId) {
        this.membershipPackageId = membershipPackageId;
    }

    public Date getPackageStartDate() {
        return packageStartDate;
    }

    public void setPackageStartDate(Date packageStartDate) {
        this.packageStartDate = packageStartDate;
    }

    public Date getPackageEndDate() {
        return packageEndDate;
    }

    public void setPackageEndDate(Date packageEndDate) {
        this.packageEndDate = packageEndDate;
    }

    public Integer getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(Integer remainingDays) {
        this.remainingDays = remainingDays;
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

    public Integer getAssignedCoachId() {
        return assignedCoachId;
    }

    public void setAssignedCoachId(Integer assignedCoachId) {
        this.assignedCoachId = assignedCoachId;
    }

    public String getPreferredTrainingTime() {
        return preferredTrainingTime;
    }

    public void setPreferredTrainingTime(String preferredTrainingTime) {
        this.preferredTrainingTime = preferredTrainingTime;
    }

    public String getTrainingFrequency() {
        return trainingFrequency;
    }

    public void setTrainingFrequency(String trainingFrequency) {
        this.trainingFrequency = trainingFrequency;
    }

    public Integer getTotalWorkoutSessions() {
        return totalWorkoutSessions;
    }

    public void setTotalWorkoutSessions(Integer totalWorkoutSessions) {
        this.totalWorkoutSessions = totalWorkoutSessions;
    }

    public Integer getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(Integer currentStreak) {
        this.currentStreak = currentStreak;
    }

    public Integer getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(Integer longestStreak) {
        this.longestStreak = longestStreak;
    }

    public BigDecimal getTotalCaloriesBurned() {
        return totalCaloriesBurned;
    }

    public void setTotalCaloriesBurned(BigDecimal totalCaloriesBurned) {
        this.totalCaloriesBurned = totalCaloriesBurned;
    }

    public Date getLastWorkoutDate() {
        return lastWorkoutDate;
    }

    public void setLastWorkoutDate(Date lastWorkoutDate) {
        this.lastWorkoutDate = lastWorkoutDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getInjuries() {
        return injuries;
    }

    public void setInjuries(String injuries) {
        this.injuries = injuries;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    // Business logic methods
    private void calculateBMI() {
        if (height != null && weight != null && height > 0) {
            double heightInMeters = height / 100.0;
            this.bmi = weight / (heightInMeters * heightInMeters);
            this.bmi = Math.round(this.bmi * 100.0) / 100.0; // Round to 2 decimal places
        }
    }

    public String getBMICategory() {
        if (bmi == null) return "Unknown";
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25) return "Normal";
        if (bmi < 30) return "Overweight";
        return "Obese";
    }

    public boolean isPackageActive() {
        if (packageEndDate == null) return false;
        return packageEndDate.after(new Date());
    }

    public void incrementWorkoutSession() {
        this.totalWorkoutSessions++;
        this.lastWorkoutDate = new Date();
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + memberId +
                ", memberCode='" + memberCode + '\'' +
                ", status='" + status + '\'' +
                ", bmi=" + bmi +
                ", fitnessGoal='" + fitnessGoal + '\'' +
                '}';
    }
}

