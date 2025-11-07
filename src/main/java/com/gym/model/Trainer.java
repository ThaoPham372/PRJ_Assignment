package com.gym.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Trainer entity - Kế thừa từ User thông qua user_id
 * Bảng trainer ánh xạ đến class Trainer
 */
@Entity
@Table(name = "trainer")
public class Trainer implements Serializable {

  private static final long serialVersionUID = 1L;

  // Share primary key with User table
  @Id
  @Column(name = "user_id")
  private Integer userId;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  // Các trường từ schema hiện có
  @Column(name = "specialization", length = 500)
  private String specialization;

  @Column(name = "years_of_experience")
  private Integer yearsOfExperience;

  @Column(name = "certification_level", length = 100)
  private String certificationLevel;

  @Column(name = "students_count")
  private Integer studentsCount;

  @Column(name = "average_rating", columnDefinition = "DECIMAL(3,1)")
  private Float averageRating;

  @Column(name = "sessions_this_month")
  private Integer sessionsThisMonth;

  // 5 trường mới phục vụ module thống kê & báo cáo
  @Column(name = "cancelled_sessions_this_month")
  private Integer cancelledSessionsThisMonth;

  @Column(name = "total_hours_trained")
  private Integer totalHoursTrained;

  @Column(name = "average_rating_this_month", columnDefinition = "DECIMAL(3,1)")
  private Float averageRatingThisMonth;

  @Column(name = "completion_rate", columnDefinition = "DECIMAL(3,2)")
  private Float completionRate;

  @Column(name = "new_students_this_month")
  private Integer newStudentsThisMonth;

  public Trainer() {
  }

  public Trainer(Integer userId) {
    this.userId = userId;
  }

  // Getters and Setters
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

  public String getSpecialization() {
    return specialization;
  }

  public void setSpecialization(String specialization) {
    this.specialization = specialization;
  }

  public Integer getYearsOfExperience() {
    return yearsOfExperience;
  }

  public void setYearsOfExperience(Integer yearsOfExperience) {
    this.yearsOfExperience = yearsOfExperience;
  }

  public String getCertificationLevel() {
    return certificationLevel;
  }

  public void setCertificationLevel(String certificationLevel) {
    this.certificationLevel = certificationLevel;
  }

  public Integer getStudentsCount() {
    return studentsCount;
  }

  public void setStudentsCount(Integer studentsCount) {
    this.studentsCount = studentsCount;
  }

  public Float getAverageRating() {
    return averageRating;
  }

  public void setAverageRating(Float averageRating) {
    this.averageRating = averageRating;
  }

  public Integer getSessionsThisMonth() {
    return sessionsThisMonth;
  }

  public void setSessionsThisMonth(Integer sessionsThisMonth) {
    this.sessionsThisMonth = sessionsThisMonth;
  }

  // Getters and Setters cho 5 trường mới
  public Integer getCancelledSessionsThisMonth() {
    return cancelledSessionsThisMonth;
  }

  public void setCancelledSessionsThisMonth(Integer cancelledSessionsThisMonth) {
    this.cancelledSessionsThisMonth = cancelledSessionsThisMonth;
  }

  public Integer getTotalHoursTrained() {
    return totalHoursTrained;
  }

  public void setTotalHoursTrained(Integer totalHoursTrained) {
    this.totalHoursTrained = totalHoursTrained;
  }

  public Float getAverageRatingThisMonth() {
    return averageRatingThisMonth;
  }

  public void setAverageRatingThisMonth(Float averageRatingThisMonth) {
    this.averageRatingThisMonth = averageRatingThisMonth;
  }

  public Float getCompletionRate() {
    return completionRate;
  }

  public void setCompletionRate(Float completionRate) {
    this.completionRate = completionRate;
  }

  public Integer getNewStudentsThisMonth() {
    return newStudentsThisMonth;
  }

  public void setNewStudentsThisMonth(Integer newStudentsThisMonth) {
    this.newStudentsThisMonth = newStudentsThisMonth;
  }
}
