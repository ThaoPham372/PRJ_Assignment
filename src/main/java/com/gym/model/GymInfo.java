package com.gym.model; 

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "gyms") 
public class GymInfo {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "gym_id") 
    private Long gymId; 

    @Column(name = "name") 
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "hotline")
    private String hotline; 

    @Column(name = "email") 
    private String email;

    @Column(name = "description")
    private String description;

    @CreationTimestamp 
    @Column(name = "created_date", updatable = false) 
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    public GymInfo() {
    }

    public GymInfo(Long gymId, String name, String address, String hotline, String email, String description, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.gymId = gymId;
        this.name = name;
        this.address = address;
        this.hotline = hotline;
        this.email = email;
        this.description = description;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
    

    public Long getGymId() {
        return gymId;
    }

    public void setGymId(Long gymId) {
        this.gymId = gymId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
    
    
}