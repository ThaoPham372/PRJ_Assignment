/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gym.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author thaopham
 */
@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "user_id")
public class Student extends User {

  // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
  // consider using these annotations to enforce field validation
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
  @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
  @OneToOne(optional = false)
  private User user;

  public Student() {
  }

  public Student(Integer userId) {
    this.setId(userId);
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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
