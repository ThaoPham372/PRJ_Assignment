package com.gym.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Model class for UserMeal entity
 * Represents a meal entry in user_meals table
 */
public class UserMeal {
    private Long id;
    private Long userId;
    private Long foodId;
    private String foodName;
    private String mealType;
    private OffsetDateTime eatenAt;
    private BigDecimal servings;
    private BigDecimal snapCalories;
    private BigDecimal snapProteinG;
    private BigDecimal snapCarbsG;
    private BigDecimal snapFatG;
    private BigDecimal totalCalories;
    private BigDecimal totalProteinG;
    private BigDecimal totalCarbsG;
    private BigDecimal totalFatG;

    // Constructors
    public UserMeal() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFoodId() {
        return foodId;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public OffsetDateTime getEatenAt() {
        return eatenAt;
    }

    public void setEatenAt(OffsetDateTime eatenAt) {
        this.eatenAt = eatenAt;
    }

    public BigDecimal getServings() {
        return servings;
    }

    public void setServings(BigDecimal servings) {
        this.servings = servings;
    }

    public BigDecimal getSnapCalories() {
        return snapCalories;
    }

    public void setSnapCalories(BigDecimal snapCalories) {
        this.snapCalories = snapCalories;
    }

    public BigDecimal getSnapProteinG() {
        return snapProteinG;
    }

    public void setSnapProteinG(BigDecimal snapProteinG) {
        this.snapProteinG = snapProteinG;
    }

    public BigDecimal getSnapCarbsG() {
        return snapCarbsG;
    }

    public void setSnapCarbsG(BigDecimal snapCarbsG) {
        this.snapCarbsG = snapCarbsG;
    }

    public BigDecimal getSnapFatG() {
        return snapFatG;
    }

    public void setSnapFatG(BigDecimal snapFatG) {
        this.snapFatG = snapFatG;
    }

    public BigDecimal getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(BigDecimal totalCalories) {
        this.totalCalories = totalCalories;
    }

    public BigDecimal getTotalProteinG() {
        return totalProteinG;
    }

    public void setTotalProteinG(BigDecimal totalProteinG) {
        this.totalProteinG = totalProteinG;
    }

    public BigDecimal getTotalCarbsG() {
        return totalCarbsG;
    }

    public void setTotalCarbsG(BigDecimal totalCarbsG) {
        this.totalCarbsG = totalCarbsG;
    }

    public BigDecimal getTotalFatG() {
        return totalFatG;
    }

    public void setTotalFatG(BigDecimal totalFatG) {
        this.totalFatG = totalFatG;
    }
    
    /**
     * Get formatted eaten time in Vietnam timezone
     * @return formatted time string (HH:mm) or empty string if eatenAt is null
     */
    public String getFormattedEatenTime() {
        if (eatenAt == null) {
            return "";
        }
        
        try {
            ZoneId vnZone = ZoneId.of("Asia/Ho_Chi_Minh");
            // Convert from stored timezone to VN timezone
            java.time.ZonedDateTime vnTime = eatenAt.atZoneSameInstant(vnZone);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return vnTime.format(formatter);
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Get eaten date in Vietnam timezone as LocalDate
     * @return LocalDate in VN timezone or null if eatenAt is null
     */
    public java.time.LocalDate getEatenDateVN() {
        if (eatenAt == null) {
            return null;
        }
        
        try {
            ZoneId vnZone = ZoneId.of("Asia/Ho_Chi_Minh");
            return eatenAt.atZoneSameInstant(vnZone).toLocalDate();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Get eaten date as string (yyyy-MM-dd) for comparison
     * @return date string or empty string if eatenAt is null
     */
    public String getEatenDateString() {
        java.time.LocalDate date = getEatenDateVN();
        if (date == null) {
            return "";
        }
        return date.toString();
    }
}

