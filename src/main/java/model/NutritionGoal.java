package model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Model class for Member Nutrition Goal
 * Represents a member's nutrition goal (giảm cân, tăng cân, giữ dáng)
 * JPA Entity for member_nutrition_goals table
 */
@Entity
@Table(name = "member_nutrition_goals")
public class NutritionGoal {
    @Id
    @Column(name = "member_id")
    private Integer memberId;
    
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
        // Default values will be set by @PrePersist
    }

    // Getters and Setters
    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }
    
    // Backward compatibility - deprecated, use getMemberId() instead
    @Deprecated
    public Long getUserId() {
        return memberId != null ? memberId.longValue() : null;
    }

    @Deprecated
    public void setUserId(Long userId) {
        this.memberId = userId != null ? userId.intValue() : null;
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
     * Get updatedAt as java.util.Date for JSP fmt:formatDate compatibility
     * Similar to Order.getOrderDateAsDate() pattern
     */
    public java.util.Date getUpdatedAtAsDate() {
        if (updatedAt == null) {
            return null;
        }
        return java.util.Date.from(updatedAt.atZone(java.time.ZoneId.systemDefault()).toInstant());
    }
    
    /**
     * Pre-persist callback: Initialize default values and set updatedAt
     * Follows JPA best practices - lifecycle callbacks handle initialization
     */
    @PrePersist
    protected void onCreate() {
        if (this.activityFactor == null) {
            this.activityFactor = new BigDecimal("1.55"); // Default moderate activity
        }
        if (this.goalType == null) {
            this.goalType = "giu dang"; // Default: maintain weight
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Pre-update callback: Update timestamp
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}



