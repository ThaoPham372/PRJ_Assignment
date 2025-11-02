package com.gym.model;

import java.sql.Timestamp;

/**
 * PTCertificate Model - Represents Personal Trainer certificate information
 */
public class PTCertificate {
  private Long id;
  private Long ptProfileId;
  private String certificateName;
  private String certificateType;
  private String issuingOrganization;
  private String certificateNumber;
  private String issueDate;
  private String expiryDate;
  private String status;
  private String certificateImage;
  private Timestamp createdDate;
  private Timestamp updatedDate;

  // Constructors
  public PTCertificate() {
  }

  public PTCertificate(Long ptProfileId, String certificateName, String certificateType) {
    this.ptProfileId = ptProfileId;
    this.certificateName = certificateName;
    this.certificateType = certificateType;
    this.status = "ACTIVE";
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPtProfileId() {
    return ptProfileId;
  }

  public void setPtProfileId(Long ptProfileId) {
    this.ptProfileId = ptProfileId;
  }

  public String getCertificateName() {
    return certificateName;
  }

  public void setCertificateName(String certificateName) {
    this.certificateName = certificateName;
  }

  public String getCertificateType() {
    return certificateType;
  }

  public void setCertificateType(String certificateType) {
    this.certificateType = certificateType;
  }

  public String getIssuingOrganization() {
    return issuingOrganization;
  }

  public void setIssuingOrganization(String issuingOrganization) {
    this.issuingOrganization = issuingOrganization;
  }

  public String getCertificateNumber() {
    return certificateNumber;
  }

  public void setCertificateNumber(String certificateNumber) {
    this.certificateNumber = certificateNumber;
  }

  public String getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(String issueDate) {
    this.issueDate = issueDate;
  }

  public String getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(String expiryDate) {
    this.expiryDate = expiryDate;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getCertificateImage() {
    return certificateImage;
  }

  public void setCertificateImage(String certificateImage) {
    this.certificateImage = certificateImage;
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
    return "PTCertificate{" +
        "id=" + id +
        ", ptProfileId=" + ptProfileId +
        ", certificateName='" + certificateName + '\'' +
        ", certificateType='" + certificateType + '\'' +
        ", issuingOrganization='" + issuingOrganization + '\'' +
        '}';
  }
}

