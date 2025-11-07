package com.gym.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "schedules")
@NamedQueries({
    @NamedQuery(name = "Schedule.findAll", query = "SELECT s FROM Schedule s ORDER BY s.trainingDate, s.startTime")
})
public class Schedule implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "id")
  private Integer id;

  // Link to student via schedules.user_id -> students.user_id
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
  private Student student;

  @Basic(optional = false)
  @NotNull
  @Column(name = "training_date")
  private LocalDate trainingDate;

  @Basic(optional = false)
  @NotNull
  @Column(name = "start_time", columnDefinition = "TIME")
  private LocalTime startTime;

  @Basic(optional = false)
  @NotNull
  @Column(name = "end_time", columnDefinition = "TIME")
  private LocalTime endTime;

  @Size(max = 50)
  @Column(name = "training_type")
  private String trainingType;

  @Size(max = 100)
  @Column(name = "location")
  private String location;

  @Size(max = 65535)
  @Column(name = "note", columnDefinition = "text")
  private String note;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", columnDefinition = "enum('pending','confirmed','completed','cancelled','rejected')")
  private ScheduleStatus status;

  @Column(name = "created_at")
  private Timestamp createdAt;

  @Column(name = "updated_at")
  private Timestamp updatedAt;

  public Schedule() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public LocalDate getTrainingDate() {
    return trainingDate;
  }

  public void setTrainingDate(LocalDate trainingDate) {
    this.trainingDate = trainingDate;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalTime startTime) {
    this.startTime = startTime;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalTime endTime) {
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

  public ScheduleStatus getStatus() {
    return status;
  }

  public void setStatus(ScheduleStatus status) {
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
