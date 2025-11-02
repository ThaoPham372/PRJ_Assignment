package com.gym.model;

import java.sql.Timestamp;

/**
 * PTProfile Model - Represents Personal Trainer profile information
 */
public class PTProfile {
  private Long id;
  private Long userId;
  private String fullName;
  private String email;
  private String phoneNumber;
  private String dateOfBirth;
  private String gender;
  private String specialization;
  private String address;
  private String avatar;
  private Integer experienceYears;
  private Integer studentsTrained;
  private Double averageRating;
  private Integer sessionsThisMonth;
  private Timestamp createdDate;
  private Timestamp updatedDate;

  // Constructors
  public PTProfile() {
  }

  public PTProfile(Long userId, String fullName, String email) {
    this.userId = userId;
    this.fullName = fullName;
    this.email = email;
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getSpecialization() {
    return specialization;
  }

  public void setSpecialization(String specialization) {
    this.specialization = specialization;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public Integer getExperienceYears() {
    return experienceYears;
  }

  public void setExperienceYears(Integer experienceYears) {
    this.experienceYears = experienceYears;
  }

  public Integer getStudentsTrained() {
    return studentsTrained;
  }

  public void setStudentsTrained(Integer studentsTrained) {
    this.studentsTrained = studentsTrained;
  }

  public Double getAverageRating() {
    return averageRating;
  }

  public void setAverageRating(Double averageRating) {
    this.averageRating = averageRating;
  }

  public Integer getSessionsThisMonth() {
    return sessionsThisMonth;
  }

  public void setSessionsThisMonth(Integer sessionsThisMonth) {
    this.sessionsThisMonth = sessionsThisMonth;
  }

  public Timestamp getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Timestamp createdDate) {
    this.createdDate = createdDate;
  }

  public Timestamp getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Timestamp updatedDate) {
    this.updatedDate = updatedDate;
  }

  @Override
  public String toString() {
    return "PTProfile{" +
        "id=" + id +
        ", userId=" + userId +
        ", fullName='" + fullName + '\'' +
        ", email='" + email + '\'' +
        ", phoneNumber='" + phoneNumber + '\'' +
        ", specialization='" + specialization + '\'' +
        '}';
  }
}

