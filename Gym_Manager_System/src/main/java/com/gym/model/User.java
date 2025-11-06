package com.gym.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * User entity representing a record in the user table.
 * Root entity for JOINED inheritance strategy.
 * Contains authentication and core user information.
 */
@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username", length = 255)
    private String username;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone", length = 255)
    private String phone;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "gender", length = 20)
    private String gender;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "status", length = 255)
    private String status;

    @Column(name = "role", length = 255)
    private String role;

    @Column(name = "createdDate")
    private LocalDateTime createdDate;

    @Column(name = "lastUpdate")
    private LocalDateTime lastUpdate;

    @Column(name = "lastLogin")
    private LocalDateTime lastLogin;

    @Column(name = "failedLoginAttempts")
    private Integer failedLoginAttempts;

    @Column(name = "lockedUntil")
    private LocalDateTime lockedUntil;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    // Getters & Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Integer getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(Integer failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    // Backward compatibility methods for existing code
    // These methods help maintain compatibility with existing DAO/Service code
    public long getId() {
        return userId != null ? userId.longValue() : 0L;
    }

    public void setId(long id) {
        this.userId = (int) id;
    }

    public String getPasswordHash() {
        return password;
    }

    public void setPasswordHash(String passwordHash) {
        this.password = passwordHash;
    }

    public String getSalt() {
        // Salt is stored in password field if using BCrypt
        // Or could be separate column if needed
        return null;
    }

    public void setSalt(String salt) {
        // Salt handling - depends on implementation
    }

    public boolean isEmailVerified() {
        // Email verification status - could be derived from status field
        return "ACTIVE".equals(status) && email != null;
    }

    public void setEmailVerified(boolean emailVerified) {
        // Email verification status - could update status field
    }

    public java.sql.Timestamp getCreatedDateAsTimestamp() {
        if (createdDate == null) {
            return null;
        }
        return java.sql.Timestamp.valueOf(createdDate);
    }

    public void setCreatedDateFromTimestamp(java.sql.Timestamp timestamp) {
        if (timestamp != null) {
            this.createdDate = timestamp.toLocalDateTime();
        }
    }

    public java.sql.Timestamp getUpdatedDateAsTimestamp() {
        if (lastUpdate == null) {
            return null;
        }
        return java.sql.Timestamp.valueOf(lastUpdate);
    }

    public void setUpdatedDateFromTimestamp(java.sql.Timestamp timestamp) {
        if (timestamp != null) {
            this.lastUpdate = timestamp.toLocalDateTime();
        }
    }

    public java.sql.Timestamp getLastLoginAsTimestamp() {
        if (lastLogin == null) {
            return null;
        }
        return java.sql.Timestamp.valueOf(lastLogin);
    }

    public void setLastLoginFromTimestamp(java.sql.Timestamp timestamp) {
        if (timestamp != null) {
            this.lastLogin = timestamp.toLocalDateTime();
        }
    }

    public java.sql.Timestamp getLockedUntilAsTimestamp() {
        if (lockedUntil == null) {
            return null;
        }
        return java.sql.Timestamp.valueOf(lockedUntil);
    }

    public void setLockedUntilFromTimestamp(java.sql.Timestamp timestamp) {
        if (timestamp != null) {
            this.lockedUntil = timestamp.toLocalDateTime();
        }
    }

    public java.sql.Date getDobAsSqlDate() {
        if (dob == null) {
            return null;
        }
        return java.sql.Date.valueOf(dob);
    }

    public void setDobFromSqlDate(java.sql.Date date) {
        if (date != null) {
            this.dob = date.toLocalDate();
        }
    }
}
