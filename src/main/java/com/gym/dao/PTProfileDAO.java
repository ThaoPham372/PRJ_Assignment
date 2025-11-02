package com.gym.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.gym.model.PTProfile;
import com.gym.util.DatabaseUtil;

/**
 * PTProfileDAO - Data Access Object for pt_profiles table
 * Handles CRUD operations for Personal Trainer profiles
 */
public class PTProfileDAO {

  /**
   * Find PT profile by user ID
   */
  public PTProfile findByUserId(Long userId) {
    String sql = "SELECT id, user_id, full_name, email, phone_number, date_of_birth, gender, " +
        "specialization, address, avatar, experience_years, students_trained, " +
        "average_rating, sessions_this_month, created_date, updated_date " +
        "FROM pt_profiles WHERE user_id = ?";

    try (Connection conn = DatabaseUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setLong(1, userId);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        return mapResultSetToPTProfile(rs);
      }
    } catch (SQLException e) {
      System.err.println("Error finding PT profile by user ID: " + e.getMessage());
    }

    return null;
  }

  /**
   * Create new PT profile
   */
  public Long createPTProfile(PTProfile profile) {
    String sql = "INSERT INTO pt_profiles (user_id, full_name, email, phone_number, date_of_birth, " +
        "gender, specialization, address, avatar, experience_years, students_trained, " +
        "average_rating, sessions_this_month, created_date, updated_date) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

    try (Connection conn = DatabaseUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      stmt.setLong(1, profile.getUserId());
      stmt.setString(2, profile.getFullName());
      stmt.setString(3, profile.getEmail());
      stmt.setString(4, profile.getPhoneNumber());
      stmt.setString(5, profile.getDateOfBirth());
      stmt.setString(6, profile.getGender());
      stmt.setString(7, profile.getSpecialization());
      stmt.setString(8, profile.getAddress());
      stmt.setString(9, profile.getAvatar());
      stmt.setObject(10, profile.getExperienceYears());
      stmt.setObject(11, profile.getStudentsTrained());
      stmt.setObject(12, profile.getAverageRating());
      stmt.setObject(13, profile.getSessionsThisMonth());

      int affectedRows = stmt.executeUpdate();

      if (affectedRows > 0) {
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
          return generatedKeys.getLong(1);
        }
      }
    } catch (SQLException e) {
      System.err.println("Error creating PT profile: " + e.getMessage());
    }

    return -1L; // Error
  }

  /**
   * Update existing PT profile
   */
  public boolean updatePTProfile(PTProfile profile) {
    String sql = "UPDATE pt_profiles SET full_name = ?, email = ?, phone_number = ?, " +
        "date_of_birth = ?, gender = ?, specialization = ?, address = ?, " +
        "avatar = ?, experience_years = ?, students_trained = ?, " +
        "average_rating = ?, sessions_this_month = ?, updated_date = NOW() " +
        "WHERE user_id = ?";

    System.out.println("DEBUG: Updating PT profile for user_id = " + profile.getUserId());
    System.out.println("DEBUG: Profile data - FullName: " + profile.getFullName() +
        ", Email: " + profile.getEmail() +
        ", Phone: " + profile.getPhoneNumber());

    try (Connection conn = DatabaseUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, profile.getFullName());
      stmt.setString(2, profile.getEmail());
      stmt.setString(3, profile.getPhoneNumber());
      stmt.setString(4, profile.getDateOfBirth());
      stmt.setString(5, profile.getGender());
      stmt.setString(6, profile.getSpecialization());
      stmt.setString(7, profile.getAddress());
      stmt.setString(8, profile.getAvatar());
      stmt.setObject(9, profile.getExperienceYears());
      stmt.setObject(10, profile.getStudentsTrained());
      stmt.setObject(11, profile.getAverageRating());
      stmt.setObject(12, profile.getSessionsThisMonth());
      stmt.setLong(13, profile.getUserId());

      int affectedRows = stmt.executeUpdate();
      System.out.println("DEBUG: UPDATE query affected " + affectedRows + " rows");

      if (affectedRows == 0) {
        System.err.println("WARNING: No rows were updated for user_id = " + profile.getUserId());
        System.err.println("This might mean the PT profile doesn't exist in the database");
      }

      return affectedRows > 0;

    } catch (SQLException e) {
      System.err.println("ERROR: Failed to update PT profile for user_id = " + profile.getUserId());
      System.err.println("SQL Error: " + e.getMessage());
      System.err.println("SQL State: " + e.getSQLState());
      System.err.println("Error Code: " + e.getErrorCode());
      e.printStackTrace();
    }

    return false;
  }

  /**
   * Check if PT profile exists for user
   */
  public boolean existsByUserId(Long userId) {
    String sql = "SELECT COUNT(*) FROM pt_profiles WHERE user_id = ?";

    try (Connection conn = DatabaseUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setLong(1, userId);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        return rs.getInt(1) > 0;
      }
    } catch (SQLException e) {
      System.err.println("Error checking PT profile existence: " + e.getMessage());
    }

    return false;
  }

  /**
   * Map ResultSet to PTProfile object
   */
  private PTProfile mapResultSetToPTProfile(ResultSet rs) throws SQLException {
    PTProfile profile = new PTProfile();

    profile.setId(rs.getLong("id"));
    profile.setUserId(rs.getLong("user_id"));
    profile.setFullName(rs.getString("full_name"));
    profile.setEmail(rs.getString("email"));
    profile.setPhoneNumber(rs.getString("phone_number"));
    profile.setDateOfBirth(rs.getString("date_of_birth"));
    profile.setGender(rs.getString("gender"));
    profile.setSpecialization(rs.getString("specialization"));
    profile.setAddress(rs.getString("address"));
    profile.setAvatar(rs.getString("avatar"));
    profile.setExperienceYears(rs.getObject("experience_years", Integer.class));
    profile.setStudentsTrained(rs.getObject("students_trained", Integer.class));
    profile.setAverageRating(rs.getObject("average_rating", Double.class));
    profile.setSessionsThisMonth(rs.getObject("sessions_this_month", Integer.class));
    profile.setCreatedDate(rs.getTimestamp("created_date"));
    profile.setUpdatedDate(rs.getTimestamp("updated_date"));

    return profile;
  }
}
