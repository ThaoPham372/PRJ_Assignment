package com.gym.model;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * TrainerAward entity - Lưu danh hiệu của Trainer
 */
@Entity
@Table(name = "trainer_awards")
public class TrainerAward implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "trainer_id", referencedColumnName = "user_id")
  private Trainer trainer;

  @Column(name = "award_name", length = 100, nullable = false)
  private String awardName;

  @Column(name = "awarded_month")
  private LocalDate awardedMonth;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  public TrainerAward() {
  }

  public TrainerAward(Trainer trainer, String awardName, LocalDate awardedMonth) {
    this.trainer = trainer;
    this.awardName = awardName;
    this.awardedMonth = awardedMonth;
  }

  // Getters and Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Trainer getTrainer() {
    return trainer;
  }

  public void setTrainer(Trainer trainer) {
    this.trainer = trainer;
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
}
