package com.gym.model.membership;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Membership model - Represents a user's membership subscription
 * Maps to memberships table in database
 */
public class Membership {
    private Long membershipId;
    private Integer userId;  // INT in DB, FK to user(user_id)
    private Long packageId;  // BIGINT in DB, FK to packages(package_id)
    private LocalDate startDate;
    private LocalDate endDate;  // Note: DB column is end_date, not expiry_date
    private String status;  // ENUM: 'ACTIVE', 'EXPIRED', 'CANCELLED'
    private String notes;  // TEXT, can be NULL
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    
    // Joined fields from packages table (for display)
    private String packageName;
    private Integer packageDurationMonths;
    private java.math.BigDecimal packagePrice;

    public Membership() {
    }

    public Membership(Long membershipId, Integer userId, Long packageId, LocalDate startDate, 
                     LocalDate endDate, String status, String notes, 
                     LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.membershipId = membershipId;
        this.userId = userId;
        this.packageId = packageId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.notes = notes;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    // Getters and Setters
    public Long getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Long membershipId) {
        this.membershipId = membershipId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    // Joined fields from packages
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getPackageDurationMonths() {
        return packageDurationMonths;
    }

    public void setPackageDurationMonths(Integer packageDurationMonths) {
        this.packageDurationMonths = packageDurationMonths;
    }

    public java.math.BigDecimal getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(java.math.BigDecimal packagePrice) {
        this.packagePrice = packagePrice;
    }

    /**
     * Check if membership is currently active (status = ACTIVE and not expired)
     */
    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(status) && 
               endDate != null && 
               endDate.isAfter(java.time.LocalDate.now()) || endDate.isEqual(java.time.LocalDate.now());
    }

    /**
     * Calculate remaining days until expiry
     */
    public long getRemainingDays() {
        if (endDate == null) {
            return 0;
        }
        java.time.LocalDate today = java.time.LocalDate.now();
        if (endDate.isBefore(today)) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(today, endDate);
    }
}
