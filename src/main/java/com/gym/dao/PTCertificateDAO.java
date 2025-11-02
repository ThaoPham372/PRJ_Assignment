package com.gym.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gym.model.PTCertificate;
import com.gym.util.DatabaseUtil;

/**
 * PTCertificateDAO - Data Access Object for pt_certificates table
 * Handles CRUD operations for Personal Trainer certificates
 */
public class PTCertificateDAO {

  /**
   * Create new certificate
   */
  public Long createCertificate(PTCertificate certificate) {
    String sql = "INSERT INTO pt_certificates (pt_profile_id, certificate_name, certificate_type, " +
        "issuing_organization, certificate_number, issue_date, expiry_date, status, " +
        "certificate_image, created_date, updated_date) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

    try (Connection conn = DatabaseUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      stmt.setLong(1, certificate.getPtProfileId());
      stmt.setString(2, certificate.getCertificateName());
      stmt.setString(3, certificate.getCertificateType());
      stmt.setString(4, certificate.getIssuingOrganization());
      stmt.setString(5, certificate.getCertificateNumber());
      stmt.setString(6, certificate.getIssueDate());
      stmt.setString(7, certificate.getExpiryDate());
      stmt.setString(8, certificate.getStatus());
      stmt.setString(9, certificate.getCertificateImage());

      int affectedRows = stmt.executeUpdate();

      if (affectedRows > 0) {
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
          return generatedKeys.getLong(1);
        }
      }
    } catch (SQLException e) {
      System.err.println("Error creating certificate: " + e.getMessage());
    }

    return -1L; // Error
  }

  /**
   * Find certificates by PT profile ID
   */
  public List<PTCertificate> findByPtProfileId(Long ptProfileId) {
    String sql = "SELECT id, pt_profile_id, certificate_name, certificate_type, " +
        "issuing_organization, certificate_number, issue_date, expiry_date, status, " +
        "certificate_image, created_date, updated_date " +
        "FROM pt_certificates WHERE pt_profile_id = ? AND status = 'ACTIVE' " +
        "ORDER BY created_date DESC";

    List<PTCertificate> certificates = new ArrayList<>();

    try (Connection conn = DatabaseUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setLong(1, ptProfileId);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        certificates.add(mapResultSetToCertificate(rs));
      }
    } catch (SQLException e) {
      System.err.println("Error finding certificates by PT profile ID: " + e.getMessage());
    }

    return certificates;
  }

  /**
   * Find certificate by ID
   */
  public PTCertificate findById(Long certificateId) {
    String sql = "SELECT id, pt_profile_id, certificate_name, certificate_type, " +
        "issuing_organization, certificate_number, issue_date, expiry_date, status, " +
        "certificate_image, created_date, updated_date " +
        "FROM pt_certificates WHERE id = ?";

    try (Connection conn = DatabaseUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setLong(1, certificateId);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        return mapResultSetToCertificate(rs);
      }
    } catch (SQLException e) {
      System.err.println("Error finding certificate by ID: " + e.getMessage());
    }

    return null;
  }

  /**
   * Update certificate
   */
  public boolean updateCertificate(PTCertificate certificate) {
    String sql = "UPDATE pt_certificates SET certificate_name = ?, certificate_type = ?, " +
        "issuing_organization = ?, certificate_number = ?, issue_date = ?, " +
        "expiry_date = ?, status = ?, certificate_image = ?, updated_date = NOW() " +
        "WHERE id = ?";

    try (Connection conn = DatabaseUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, certificate.getCertificateName());
      stmt.setString(2, certificate.getCertificateType());
      stmt.setString(3, certificate.getIssuingOrganization());
      stmt.setString(4, certificate.getCertificateNumber());
      stmt.setString(5, certificate.getIssueDate());
      stmt.setString(6, certificate.getExpiryDate());
      stmt.setString(7, certificate.getStatus());
      stmt.setString(8, certificate.getCertificateImage());
      stmt.setLong(9, certificate.getId());

      int affectedRows = stmt.executeUpdate();
      return affectedRows > 0;

    } catch (SQLException e) {
      System.err.println("Error updating certificate: " + e.getMessage());
    }

    return false;
  }

  /**
   * Delete certificate (soft delete by setting status to INACTIVE)
   */
  public boolean deleteCertificate(Long certificateId) {
    String sql = "UPDATE pt_certificates SET status = 'INACTIVE', updated_date = NOW() WHERE id = ?";

    try (Connection conn = DatabaseUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setLong(1, certificateId);
      int affectedRows = stmt.executeUpdate();

      System.out.println("DEBUG: Certificate soft deletion affected " + affectedRows + " rows");
      return affectedRows > 0;

    } catch (SQLException e) {
      System.err.println("Error soft deleting certificate: " + e.getMessage());
    }

    return false;
  }

  /**
   * Map ResultSet to PTCertificate object
   */
  private PTCertificate mapResultSetToCertificate(ResultSet rs) throws SQLException {
    PTCertificate certificate = new PTCertificate();

    certificate.setId(rs.getLong("id"));
    certificate.setPtProfileId(rs.getLong("pt_profile_id"));
    certificate.setCertificateName(rs.getString("certificate_name"));
    certificate.setCertificateType(rs.getString("certificate_type"));
    certificate.setIssuingOrganization(rs.getString("issuing_organization"));
    certificate.setCertificateNumber(rs.getString("certificate_number"));
    certificate.setIssueDate(rs.getString("issue_date"));
    certificate.setExpiryDate(rs.getString("expiry_date"));
    certificate.setStatus(rs.getString("status"));
    certificate.setCertificateImage(rs.getString("certificate_image"));
    certificate.setCreatedDate(rs.getTimestamp("created_date"));
    certificate.setUpdatedDate(rs.getTimestamp("updated_date"));

    return certificate;
  }
}
