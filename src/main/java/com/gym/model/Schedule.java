package com.gym.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Schedule {
  private int id;
  private int userId;
  private int trainerId;
  private Date trainingDate;
  private Time startTime;
  private Time endTime;
  private String trainingType;
  private String location;
  private String note;
  private String status; // 'pending',...'rejected'
  private Timestamp createdAt;
  private Timestamp updatedAt;

  public Schedule() {
  }

  public Schedule(int id, int userId, int trainerId, Date trainingDate, Time startTime, Time endTime,
      String trainingType, String location, String note, String status, Timestamp createdAt, Timestamp updatedAt) {
    this.id = id;
    this.userId = userId;
    this.trainerId = trainerId;
    this.trainingDate = trainingDate;
    this.startTime = startTime;
    this.endTime = endTime;
    this.trainingType = trainingType;
    this.location = location;
    this.note = note;
    this.status = status;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  // Getter & Setter
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getTrainerId() {
    return trainerId;
  }

  public void setTrainerId(int trainerId) {
    this.trainerId = trainerId;
  }

  public Date getTrainingDate() {
    return trainingDate;
  }

  public void setTrainingDate(Date trainingDate) {
    this.trainingDate = trainingDate;
  }

  public Time getStartTime() {
    return startTime;
  }

  public void setStartTime(Time startTime) {
    this.startTime = startTime;
  }

  public Time getEndTime() {
    return endTime;
  }

  public void setEndTime(Time endTime) {
    this.endTime = endTime;
  }

  public String getTrainingType() {
    return trainingType;
  }

  public void setTrainingType(String trainingType) {
    this.trainingType = trainingType;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Timestamp getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }

  public Timestamp getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Timestamp updatedAt) {
    this.updatedAt = updatedAt;
  }
}
