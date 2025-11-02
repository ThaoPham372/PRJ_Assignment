package com.gym.model.membership;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * UserMembership model
 * Represents a user's membership subscription
 */
public class UserMembership {
    private Long userMembershipId;
    private Long userId;
    private Long membershipId;
    private LocalDate startDate;
    private LocalDate expiryDate;
    private String status;  // 'active', 'expired', 'cancelled', 'pending'
    private Boolean autoRenew;
    private Long orderId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    
    // Joined fields from membership
    private String membershipName;
    private String displayName;

    public UserMembership() {
    }

    // Getters and Setters
    public Long getUserMembershipId() {
        return userMembershipId;
    }

    public void setUserMembershipId(Long userMembershipId) {
        this.userMembershipId = userMembershipId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Long membershipId) {
        this.membershipId = membershipId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(Boolean autoRenew) {
        this.autoRenew = autoRenew;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getMembershipName() {
        return membershipName;
    }

    public void setMembershipName(String membershipName) {
        this.membershipName = membershipName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Check if membership is currently active
     */
    public boolean isActive() {
        return "active".equals(status) && 
               expiryDate != null && 
               expiryDate.isAfter(java.time.LocalDate.now());
    }
}


