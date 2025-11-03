package com.gym.model.membership;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Package model - Represents a membership package
 * Maps to packages table in database
 */
public class Package {
    private Long packageId;
    private String name;
    private Integer durationMonths;
    private BigDecimal price;
    private String description;
    private Integer maxSessions;  // NULL means unlimited
    private Boolean isActive;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Integer gymId;  // FK to gyms table, can be NULL

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


