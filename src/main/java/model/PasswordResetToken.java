package model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity for password_reset_tokens table
 * Stores temporary tokens for password reset via email
 */
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long tokenId;
    
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    
    @Column(name = "token", nullable = false, unique = true, length = 100)
    private String token;
    
    @Column(name = "email", nullable = false)
    private String email;
    
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    
    @Column(name = "used", nullable = false)
    private Boolean used = false;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (used == null) {
            used = false;
        }
    }
    
    // Constructors
    public PasswordResetToken() {}
    
    public PasswordResetToken(Integer userId, String token, String email, LocalDateTime expiresAt) {
        this.userId = userId;
        this.token = token;
        this.email = email;
        this.expiresAt = expiresAt;
        this.used = false;
    }
    
    // Getters and Setters
    public Long getTokenId() {
        return tokenId;
    }
    
    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public Boolean getUsed() {
        return used;
    }
    
    public void setUsed(Boolean used) {
        this.used = used;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    // Helper methods
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
    
    public boolean isValid() {
        return !used && !isExpired();
    }
}

