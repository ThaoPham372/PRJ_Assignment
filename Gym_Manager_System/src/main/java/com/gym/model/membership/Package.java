package com.gym.model.membership;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Package model - Represents a membership package
 * Maps to packages table in database - JPA Entity
 */
@Entity
@Table(name = "packages")
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_id")
    private Long packageId;
    
    @Column(name = "name", length = 255)
    private String name;
    
    @Column(name = "duration_months")
    private Integer durationMonths;
    
    @Column(name = "price", precision = 15, scale = 2)
    private BigDecimal price;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "max_sessions")
    private Integer maxSessions;  // NULL means unlimited
    
    @Column(name = "is_active")
    private Boolean isActive;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    
    @Column(name = "gym_id")
    private Integer gymId;  // FK to gyms table, can be NULL
    
    @PrePersist
    protected void onCreate() {
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
        if (updatedDate == null) {
            updatedDate = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }

    public Package() {
    }

    public Package(Long packageId, String name, Integer durationMonths, BigDecimal price, 
                   String description, Integer maxSessions, Boolean isActive, 
                   LocalDateTime createdDate, LocalDateTime updatedDate, Integer gymId) {
        this.packageId = packageId;
        this.name = name;
        this.durationMonths = durationMonths;
        this.price = price;
        this.description = description;
        this.maxSessions = maxSessions;
        this.isActive = isActive;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.gymId = gymId;
    }

    // Getters and Setters
    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxSessions() {
        return maxSessions;
    }

    public void setMaxSessions(Integer maxSessions) {
        this.maxSessions = maxSessions;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getGymId() {
        return gymId;
    }

    public void setGymId(Integer gymId) {
        this.gymId = gymId;
    }

    /**
     * Check if package has unlimited sessions
     */
    public boolean hasUnlimitedSessions() {
        return maxSessions == null;
    }
}




