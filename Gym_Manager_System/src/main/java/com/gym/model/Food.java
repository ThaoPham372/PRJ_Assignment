package com.gym.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Food entity representing a food item in the foods table
 * Converted to JPA Entity
 */
@Entity
@Table(name = "foods")
public class Food {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, length = 120, unique = true)
    private String name;
    
    @Column(name = "serving_label", length = 60)
    private String servingLabel;
    
    @Column(name = "calories", nullable = false, precision = 8, scale = 2)
    private BigDecimal calories;
    
    @Column(name = "protein_g", nullable = false, precision = 8, scale = 2)
    private BigDecimal proteinG;
    
    @Column(name = "carbs_g", nullable = false, precision = 8, scale = 2)
    private BigDecimal carbsG;
    
    @Column(name = "fat_g", nullable = false, precision = 8, scale = 2)
    private BigDecimal fatG;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Constructors
    public Food() {
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Food(Long id, String name, String servingLabel, BigDecimal calories, 
                BigDecimal proteinG, BigDecimal carbsG, BigDecimal fatG, Boolean isActive) {
        this();
        this.id = id;
        this.name = name;
        this.servingLabel = servingLabel;
        this.calories = calories;
        this.proteinG = proteinG;
        this.carbsG = carbsG;
        this.fatG = fatG;
        this.isActive = isActive;
    }
    
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
        if (this.isActive == null) {
            this.isActive = true;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServingLabel() {
        return servingLabel;
    }

    public void setServingLabel(String servingLabel) {
        this.servingLabel = servingLabel;
    }

    public BigDecimal getCalories() {
        return calories;
    }

    public void setCalories(BigDecimal calories) {
        this.calories = calories;
    }

    public BigDecimal getProteinG() {
        return proteinG;
    }

    public void setProteinG(BigDecimal proteinG) {
        this.proteinG = proteinG;
    }

    public BigDecimal getCarbsG() {
        return carbsG;
    }

    public void setCarbsG(BigDecimal carbsG) {
        this.carbsG = carbsG;
    }

    public BigDecimal getFatG() {
        return fatG;
    }

    public void setFatG(BigDecimal fatG) {
        this.fatG = fatG;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Backward compatibility methods for OffsetDateTime
    public OffsetDateTime getCreatedAtAsOffsetDateTime() {
        return createdAt != null ? createdAt.atOffset(ZoneOffset.UTC) : null;
    }

    public void setCreatedAtAsOffsetDateTime(OffsetDateTime createdAt) {
        this.createdAt = createdAt != null ? createdAt.toLocalDateTime() : null;
    }
    
    public OffsetDateTime getUpdatedAtAsOffsetDateTime() {
        return updatedAt != null ? updatedAt.atOffset(ZoneOffset.UTC) : null;
    }

    public void setUpdatedAtAsOffsetDateTime(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt != null ? updatedAt.toLocalDateTime() : null;
    }
}
