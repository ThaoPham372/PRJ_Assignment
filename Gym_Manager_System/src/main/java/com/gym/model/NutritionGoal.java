package com.gym.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Model class for User Nutrition Goal
 * Represents a user's nutrition goal (giảm cân, tăng cân, giữ dáng)
 * JPA Entity for user_nutrition_goals table
 */
@Entity
@Table(name = "user_nutrition_goals")
public class NutritionGoal {
    @Id
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "goal_type")
    private String goalType; // 'giam can', 'tang can', 'giu dang'
    
    @Column(name = "activity_factor", precision = 5, scale = 2)
    private BigDecimal activityFactor;
    
    @Column(name = "daily_calories_target", precision = 10, scale = 2)
    private BigDecimal dailyCaloriesTarget;
    
    @Column(name = "daily_protein_target", precision = 10, scale = 2)
    private BigDecimal dailyProteinTarget;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    /**
     * Backward compatibility: Convert OffsetDateTime to LocalDateTime
     */
    public void setUpdatedAt(java.time.OffsetDateTime offsetDateTime) {
        if (offsetDateTime != null) {
            this.updatedAt = offsetDateTime.toLocalDateTime();
        } else {
            this.updatedAt = null;
        }
    }
    
    /**
     * Backward compatibility: Convert LocalDateTime to OffsetDateTime
     */
    public java.time.OffsetDateTime getUpdatedAtAsOffsetDateTime() {
        if (updatedAt != null) {
            return updatedAt.atOffset(java.time.ZoneOffset.UTC);
        }
        return null;
    }
    
    /**
     * Pre-persist and pre-update: Set updatedAt to current time
     */
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}



