package com.gym.dto;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO cho danh hiệu của Trainer
 */
public class TrainerAwardDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer trainerId;
  private String trainerName;
  private String awardName;
  private LocalDate awardedMonth;
  private String description;
  private AwardType awardType;

  public enum AwardType {
    TOP_SESSIONS_MONTH, // Top PT trong tháng (nhiều buổi tập nhất)
    TOP_RATING, // PT được đánh giá cao nhất
    TOP_COMPLETION_RATE // PT có tỷ lệ hoàn thành cao nhất
  }

  public TrainerAwardDTO() {
  }

  public TrainerAwardDTO(Integer trainerId, String trainerName, String awardName, LocalDate awardedMonth,
      AwardType awardType) {
    this.trainerId = trainerId;
    this.trainerName = trainerName;
    this.awardName = awardName;
    this.awardedMonth = awardedMonth;
    this.awardType = awardType;
  }

  // Getters and Setters
  public Integer getTrainerId() {
    return trainerId;
  }

  public void setTrainerId(Integer trainerId) {
    this.trainerId = trainerId;
  }

  public String getTrainerName() {
    return trainerName;
  }

  public void setTrainerName(String trainerName) {
    this.trainerName = trainerName;
  }

  public String getAwardName() {
    return awardName;
  }

  public void setAwardName(String awardName) {
    this.awardName = awardName;
  }

  public LocalDate getAwardedMonth() {
    return awardedMonth;
  }

  public void setAwardedMonth(LocalDate awardedMonth) {
    this.awardedMonth = awardedMonth;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public AwardType getAwardType() {
    return awardType;
  }

  public void setAwardType(AwardType awardType) {
    this.awardType = awardType;
  }
}
