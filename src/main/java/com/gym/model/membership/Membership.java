package com.gym.model.membership;

import jakarta.persistence.*;
import com.gym.model.User;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Membership model - Represents a user's membership subscription Maps to
 * memberships table in database - JPA Entity
 */
@Entity
@Table(name = "memberships")
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membership_id")
    private Long membershipId;

    @Column(name = "user_id")
    private Integer userId;  // INT in DB, FK to user(user_id)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "package_id")
    private Long packageId;  // BIGINT in DB, FK to packages(package_id)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", insertable = false, updatable = false)
    private Package packageEntity;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;  // Note: DB column is end_date, not expiry_date

    @Column(name = "status", length = 50)
    private String status;  // ENUM: 'INACTIVE', 'ACTIVE', 'EXPIRED', 'CANCELLED', 'SUSPENDED'

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;  // TEXT, can be NULL

    @Column(name = "activated_at")
    private LocalDateTime activatedAt;  // Timestamp when membership was activated (payment = PAID)

    @Column(name = "suspended_at")
    private LocalDateTime suspendedAt;  // Timestamp when membership was suspended (payment refunded/chargeback)

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    // Joined fields from packages table (for display) - @Transient
    @Transient
    private String packageName;

    @Transient
    private Integer packageDurationMonths;

    @Transient
    private java.math.BigDecimal packagePrice;

    @Transient
    private String name;
    @Transient
    private String email;
    @Transient
    private String phone;
    @Transient
    private String packageType;

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

    public Membership(Long membershipId, Integer userId, Long packageId, LocalDate startDate,
            LocalDate endDate, String status, String notes,
            LocalDateTime activatedAt, LocalDateTime suspendedAt,
            LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.membershipId = membershipId;
        this.userId = userId;
        this.packageId = packageId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.notes = notes;
        this.activatedAt = activatedAt;
        this.suspendedAt = suspendedAt;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Package getPackageEntity() {
        return packageEntity;
    }

    public void setPackageEntity(Package packageEntity) {
        this.packageEntity = packageEntity;
        if (packageEntity != null) {
            this.packageId = packageEntity.getPackageId();
            // Load package info for display
            this.packageName = packageEntity.getName();
            this.packageDurationMonths = packageEntity.getDurationMonths();
            this.packagePrice = packageEntity.getPrice();
        }
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

    public LocalDateTime getActivatedAt() {
        return activatedAt;
    }

    public void setActivatedAt(LocalDateTime activatedAt) {
        this.activatedAt = activatedAt;
    }

    public LocalDateTime getSuspendedAt() {
        return suspendedAt;
    }

    public void setSuspendedAt(LocalDateTime suspendedAt) {
        this.suspendedAt = suspendedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }
    
    

    /**
     * Check if membership is currently active (status = ACTIVE and not expired)
     */
    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(status)
                && endDate != null
                && endDate.isAfter(java.time.LocalDate.now()) || endDate.isEqual(java.time.LocalDate.now());
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
