package model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * UserMeal entity representing a meal entry in member_meals table
 * Follows JPA Entity best practices
 * Encapsulates meal data and provides timezone conversion utilities
 */
@Entity
@Table(name = "member_meals")
public class UserMeal {
    
    // Constants for timezone operations
    private static final ZoneId VN_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "member_id", nullable = false)
    private Integer memberId;
    
    @Column(name = "food_id", nullable = false)
    private Long foodId;
    
    @Transient // Not in database, populated from JOIN query
    private String foodName;
    
    @Column(name = "meal_type", length = 20)
    private String mealType;
    
    @Column(name = "eaten_at", nullable = false)
    private LocalDateTime eatenAt;
    
    @Column(name = "servings", nullable = false, precision = 8, scale = 2)
    private BigDecimal servings;
    
    @Column(name = "snap_calories", nullable = false, precision = 8, scale = 2)
    private BigDecimal snapCalories;
    
    @Column(name = "snap_protein_g", nullable = false, precision = 8, scale = 2)
    private BigDecimal snapProteinG;
    
    @Column(name = "snap_carbs_g", nullable = false, precision = 8, scale = 2)
    private BigDecimal snapCarbsG;
    
    @Column(name = "snap_fat_g", nullable = false, precision = 8, scale = 2)
    private BigDecimal snapFatG;
    
    // These are GENERATED columns in MySQL (computed automatically)
    // JPA cannot insert/update these columns - they are read-only
    @Column(name = "total_calories", precision = 10, scale = 2, insertable = false, updatable = false)
    private BigDecimal totalCalories;
    
    @Column(name = "total_protein_g", precision = 10, scale = 2, insertable = false, updatable = false)
    private BigDecimal totalProteinG;
    
    @Column(name = "total_carbs_g", precision = 10, scale = 2, insertable = false, updatable = false)
    private BigDecimal totalCarbsG;
    
    @Column(name = "total_fat_g", precision = 10, scale = 2, insertable = false, updatable = false)
    private BigDecimal totalFatG;
    
    // Constructors
    public UserMeal() {
        // Default values will be set by @PrePersist
    }
    
    /**
     * Pre-persist callback: Set eatenAt if not provided
     * Follows JPA best practices - lifecycle callbacks handle initialization
     */
    @PrePersist
    protected void onCreate() {
        if (this.eatenAt == null) {
            this.eatenAt = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getEatenAt() {
        return eatenAt;
    }

    public void setEatenAt(LocalDateTime eatenAt) {
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
    
    // ==================== TIMEZONE UTILITY METHODS ====================
    
    /**
     * Convert eatenAt to OffsetDateTime (UTC)
     * Utility method for timezone operations
     * @return OffsetDateTime in UTC or null if eatenAt is null
     */
    public OffsetDateTime getEatenAtAsOffsetDateTime() {
        return eatenAt != null ? eatenAt.atOffset(ZoneOffset.UTC) : null;
    }

    /**
     * Set eatenAt from OffsetDateTime
     * @param eatenAt OffsetDateTime to convert
     */
    public void setEatenAtAsOffsetDateTime(OffsetDateTime eatenAt) {
        this.eatenAt = eatenAt != null ? eatenAt.toLocalDateTime() : null;
    }
    
    /**
     * Get formatted eaten time in Vietnam timezone
     * Reusable method following DRY principle
     * @return formatted time string (HH:mm) or empty string if eatenAt is null
     */
    public String getFormattedEatenTime() {
        if (eatenAt == null) {
            return "";
        }
        
        try {
            OffsetDateTime eatenAtOffset = eatenAt.atOffset(ZoneOffset.UTC);
            java.time.ZonedDateTime vnTime = eatenAtOffset.atZoneSameInstant(VN_ZONE);
            return vnTime.format(TIME_FORMATTER);
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Get eaten date in Vietnam timezone as LocalDate
     * Reusable method following DRY principle
     * @return LocalDate in VN timezone or null if eatenAt is null
     */
    public java.time.LocalDate getEatenDateVN() {
        if (eatenAt == null) {
            return null;
        }
        
        try {
            OffsetDateTime eatenAtOffset = eatenAt.atOffset(ZoneOffset.UTC);
            return eatenAtOffset.atZoneSameInstant(VN_ZONE).toLocalDate();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Get eaten date as string (yyyy-MM-dd) for comparison
     * Convenience method that delegates to getEatenDateVN()
     * @return date string or empty string if eatenAt is null
     */
    public String getEatenDateString() {
        java.time.LocalDate date = getEatenDateVN();
        return date != null ? date.toString() : "";
    }
}
