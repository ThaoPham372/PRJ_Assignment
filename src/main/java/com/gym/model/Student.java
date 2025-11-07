package com.gym.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "students")
public class Student implements Serializable {

  private static final long serialVersionUID = 1L;

  // Share primary key with User table
  @Id
  @Column(name = "user_id")
  private Integer userId;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "weight")
  private Float weight;

  @Column(name = "height")
  private Float height;

  @Column(name = "bmi")
  private Float bmi;

  @Column(name = "emergency_contact_name")
  private String emergencyContactName;

  @Column(name = "emergency_contact_phone")
  private String emergencyContactPhone;

  @Column(name = "emergency_contact_relation")
  private String emergencyContactRelation;

  @Column(name = "emergency_contact_address")
  private String emergencyContactAddress;

  @Column(name = "training_package")
  private String trainingPackage;

  @Column(name = "training_duration")
  private String trainingDuration;

  @Column(name = "goal")
  private String goal;

  @Column(name = "training_progress")
  private Integer trainingProgress;

  @Column(name = "training_sessions")
  private Integer trainingSessions;

  @Column(name = "pt_note", columnDefinition = "TEXT")
  private String ptNote;

  // Cột phone_number đã bị xóa khỏi database, giữ field ở mức transient để tránh
  // lỗi schema
  @Transient
  private String phoneNumber;

  public Student() {
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

  public Float getWeight() {
    return weight;
  }

  public void setWeight(Float weight) {
    this.weight = weight;
  }

  public Float getHeight() {
    return height;
  }

  public void setHeight(Float height) {
    this.height = height;
  }

  public Float getBmi() {
    return bmi;
  }

  public void setBmi(Float bmi) {
    this.bmi = bmi;
  }

  public String getEmergencyContactName() {
    return emergencyContactName;
  }

  public void setEmergencyContactName(String emergencyContactName) {
    this.emergencyContactName = emergencyContactName;
  }

  public String getEmergencyContactPhone() {
    return emergencyContactPhone;
  }

  public void setEmergencyContactPhone(String emergencyContactPhone) {
    this.emergencyContactPhone = emergencyContactPhone;
  }

  public String getEmergencyContactRelation() {
    return emergencyContactRelation;
  }

  public void setEmergencyContactRelation(String emergencyContactRelation) {
    this.emergencyContactRelation = emergencyContactRelation;
  }

  public String getEmergencyContactAddress() {
    return emergencyContactAddress;
  }

  public void setEmergencyContactAddress(String emergencyContactAddress) {
    this.emergencyContactAddress = emergencyContactAddress;
  }

  public String getTrainingPackage() {
    return trainingPackage;
  }

  public void setTrainingPackage(String trainingPackage) {
    this.trainingPackage = trainingPackage;
  }

  public String getTrainingDuration() {
    return trainingDuration;
  }

  public void setTrainingDuration(String trainingDuration) {
    this.trainingDuration = trainingDuration;
  }

  public String getGoal() {
    return goal;
  }

  public void setGoal(String goal) {
    this.goal = goal;
  }

  public Integer getTrainingProgress() {
    return trainingProgress;
  }

  public void setTrainingProgress(Integer trainingProgress) {
    this.trainingProgress = trainingProgress;
  }

  public Integer getTrainingSessions() {
    return trainingSessions;
  }

  public void setTrainingSessions(Integer trainingSessions) {
    this.trainingSessions = trainingSessions;
  }

  public String getPtNote() {
    return ptNote;
  }

  public void setPtNote(String ptNote) {
    this.ptNote = ptNote;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
}
