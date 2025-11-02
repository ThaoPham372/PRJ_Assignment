package com.gym.studentDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.gym.model.Student;
import com.gym.util.DatabaseUtil;

/**
 * StudentDao - Implementation of IStudentDao
 * Handles database operations for Student entity
 * Maps data from users table to Student model
 */
public class StudentDao implements IStudentDao {

  /**
   * Get all students from users table
   * Currently maps basic user data to Student model
   * Note: Additional fields (phone, package, progress, etc.) may need to be added
   * to database schema
   */
  @Override
  public List<Student> getAllStudents() {
    List<Student> students = new ArrayList<>();

    // Query users table and map to Student model
    // Note: This assumes users with role 'MEMBER' or 'STUDENT' are students
    // For now, we'll get all active users. Filter by role can be added later if
    // needed.
    String sql = "SELECT u.id, u.username, u.email, u.status, u.created_date, u.last_login " +
        "FROM users u " +
        "WHERE u.status = 'ACTIVE' " +
        "ORDER BY u.username ASC";

    try (Connection conn = DatabaseUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        Student student = mapResultSetToStudent(rs);
        students.add(student);
      }
    } catch (SQLException e) {
      System.err.println("Error getting all students: " + e.getMessage());
      e.printStackTrace();
    }

    return students;
  }

  @Override
  public Student getStudentById(Long studentId) {
    String sql = "SELECT u.id, u.username, u.email, u.status, u.created_date, u.last_login " +
        "FROM users u " +
        "WHERE u.id = ? AND u.status = 'ACTIVE'";

    try (Connection conn = DatabaseUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setLong(1, studentId);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        return mapResultSetToStudent(rs);
      }
    } catch (SQLException e) {
      System.err.println("Error getting student by ID: " + e.getMessage());
      e.printStackTrace();
    }

    return null;
  }


  @Override
  public List<Student> searchStudents(String searchTerm) {
    List<Student> students = new ArrayList<>();

    if (searchTerm == null || searchTerm.trim().isEmpty()) {
      return getAllStudents();
    }

    String trimmedTerm = searchTerm.trim();
    String sql;

    try (Connection conn = DatabaseUtil.getConnection()) {
      // Check if search term is numeric (for ID search)
      boolean isNumeric = false;
      Long searchId = null;
      try {
        searchId = Long.parseLong(trimmedTerm);
        isNumeric = true;
      } catch (NumberFormatException e) {
        // Not numeric, will search by name/email
      }

      String searchPattern = "%" + trimmedTerm + "%";

      if (isNumeric) {
        // Search by ID (exact match) OR name/email (like match)
        sql = "SELECT u.id, u.username, u.email, u.status, u.created_date, u.last_login " +
            "FROM users u " +
            "WHERE u.status = 'ACTIVE' " +
            "AND (u.id = ? OR u.username LIKE ? OR u.email LIKE ?) " +
            "ORDER BY u.username ASC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
          stmt.setLong(1, searchId);
          stmt.setString(2, searchPattern);
          stmt.setString(3, searchPattern);

          try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
              Student student = mapResultSetToStudent(rs);
              students.add(student);
            }
          }
        }
      } else {
        // Search by name (username) or email only
        sql = "SELECT u.id, u.username, u.email, u.status, u.created_date, u.last_login " +
            "FROM users u " +
            "WHERE u.status = 'ACTIVE' " +
            "AND (u.username LIKE ? OR u.email LIKE ?) " +
            "ORDER BY u.username ASC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
          stmt.setString(1, searchPattern);
          stmt.setString(2, searchPattern);

          try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
              Student student = mapResultSetToStudent(rs);
              students.add(student);
            }
          }
        }
      }
    } catch (SQLException e) {
      System.err.println("Error searching students: " + e.getMessage());
      e.printStackTrace();
    }

    return students;
  }

  /**
   * Helper method to map ResultSet to Student object
   * Maps available fields from users table to Student model
   * Additional fields (phone, package, progress) use default/placeholder values
   */
  private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
    Student student = new Student();

    // Map from users table
    student.setId(rs.getInt("id"));
    student.setName(rs.getString("username")); // Using username as name
    student.setEmail(rs.getString("email"));
    student.setStatus(rs.getString("status"));
    student.setCreatedDate(rs.getTimestamp("created_date"));

    Timestamp lastLogin = rs.getTimestamp("last_login");
    if (lastLogin != null) {
      student.setLastLogin(lastLogin);
    }

    // Set default/placeholder values for fields not in users table yet
    // These can be updated when database schema is extended
    student.setPhone(null); // Will be null until phone is added to schema
    return student;
  }

  /**
   * Calculate training months from created date
   */
  private Integer calculateTrainingMonths(Timestamp createdDate) {
    if (createdDate == null) {
      return 0;
    }

    long now = System.currentTimeMillis();
    long created = createdDate.getTime();
    long diffInMillis = now - created;
    long diffInMonths = diffInMillis / (1000L * 60 * 60 * 24 * 30); // Approximate

    return (int) diffInMonths;
  }
}
