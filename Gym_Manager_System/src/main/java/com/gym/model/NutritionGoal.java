package com.gym.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Model class for User Nutrition Goal
 * Represents a user's nutrition goal (giảm cân, tăng cân, giữ dáng)
 */
public class NutritionGoal {
    private Long userId;
    private String goalType; // 'giam can', 'tang can', 'giu dang'
    private BigDecimal activityFactor;
    private BigDecimal dailyCaloriesTarget;
    private BigDecimal dailyProteinTarget;
    private OffsetDateTime updatedAt;

    // Constructors
    public NutritionGoal() {
        this.activityFactor = new BigDecimal("1.55"); // Default moderate activity
        this.goalType = "giu dang"; // Default: maintain weight
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getGoalType() {
        return goalType;
    }

    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }

    public BigDecimal getActivityFactor() {
        return activityFactor;
    }

    public void setActivityFactor(BigDecimal activityFactor) {
        this.activityFactor = activityFactor;
    }

    public BigDecimal getDailyCaloriesTarget() {
        return dailyCaloriesTarget;
    }

    public void setDailyCaloriesTarget(BigDecimal dailyCaloriesTarget) {
        this.dailyCaloriesTarget = dailyCaloriesTarget;
    }

    public BigDecimal getDailyProteinTarget() {
        return dailyProteinTarget;
    }

    public void setDailyProteinTarget(BigDecimal dailyProteinTarget) {
        this.dailyProteinTarget = dailyProteinTarget;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}



