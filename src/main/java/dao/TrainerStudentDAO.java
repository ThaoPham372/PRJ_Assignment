package dao;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.persistence.Query;

/**
 * DAO for Trainer Student Management
 * Queries students (members) that are assigned to a trainer through PT bookings
 */
public class TrainerStudentDAO extends BaseDAO {
  private static final Logger LOGGER = Logger.getLogger(TrainerStudentDAO.class.getName());

  /**
   * Get list of students (members) assigned to a trainer
   * Returns distinct members with their information from pt_bookings, members,
   * and users tables
   * 
   * @param trainerId The trainer ID
   * @return List of Object arrays containing student information
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> getStudentsByTrainer(Integer trainerId) {
    try {
      // Use native query because JPQL doesn't support CASE WHEN in SUM
      String sql = """
          SELECT
              m.member_id,
              u.name,
              u.phone,
              u.email,
              u.gender,
              u.dob,
              m.weight,
              m.height,
              m.bmi,
              m.goal,
              m.pt_note,
              COUNT(DISTINCT b.booking_id) AS total_bookings,
              SUM(CASE WHEN b.booking_status = 'COMPLETED' THEN 1 ELSE 0 END) AS completed_sessions,
              SUM(CASE WHEN b.booking_status = 'CONFIRMED' THEN 1 ELSE 0 END) AS confirmed_sessions,
              SUM(CASE WHEN b.booking_status = 'PENDING' THEN 1 ELSE 0 END) AS pending_sessions
          FROM pt_bookings b
          JOIN members m ON b.member_id = m.member_id
          JOIN user u ON m.member_id = u.user_id
          WHERE b.trainer_id = ?
            AND b.booking_status IN ('PENDING', 'CONFIRMED', 'COMPLETED')
          GROUP BY m.member_id, u.name, u.phone, u.email, u.gender, u.dob,
                   m.weight, m.height, m.bmi, m.goal, m.pt_note
          ORDER BY u.name
          """;

      Query query = em.createNativeQuery(sql);
      query.setParameter(1, trainerId);

      return query.getResultList();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error getting students by trainer: " + trainerId, e);
      return Collections.emptyList();
    }
  }

  /**
   * Get students by trainer with search and filter
   * 
   * @param trainerId   The trainer ID
   * @param keyword     Search keyword (name, phone, email)
   * @param packageName Filter by package name (from membership)
   * @return List of Object arrays containing student information
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> getStudentsByTrainerWithFilter(Integer trainerId, String keyword, String packageName) {
    try {
      StringBuilder sql = new StringBuilder("""
          SELECT
              m.member_id,
              u.name,
              u.phone,
              u.email,
              u.gender,
              u.dob,
              m.weight,
              m.height,
              m.bmi,
              m.goal,
              m.pt_note,
              pkg.name AS package_name,
              COUNT(DISTINCT b.booking_id) AS total_bookings,
              SUM(CASE WHEN b.booking_status = 'COMPLETED' THEN 1 ELSE 0 END) AS completed_sessions,
              SUM(CASE WHEN b.booking_status = 'CONFIRMED' THEN 1 ELSE 0 END) AS confirmed_sessions,
              SUM(CASE WHEN b.booking_status = 'PENDING' THEN 1 ELSE 0 END) AS pending_sessions
          FROM pt_bookings b
          JOIN members m ON b.member_id = m.member_id
          JOIN user u ON m.member_id = u.user_id
          LEFT JOIN memberships mem ON mem.member_id = m.member_id AND mem.status = 'active'
          LEFT JOIN packages pkg ON mem.package_id = pkg.package_id
          WHERE b.trainer_id = ?
            AND b.booking_status IN ('PENDING', 'CONFIRMED', 'COMPLETED')
          """);

      int paramIndex = 1;
      if (keyword != null && !keyword.trim().isEmpty()) {
        sql.append(
            " AND (LOWER(u.name) LIKE LOWER(?) OR LOWER(u.phone) LIKE LOWER(?) OR LOWER(u.email) LIKE LOWER(?))");
      }

      if (packageName != null && !packageName.trim().isEmpty()) {
        sql.append(" AND pkg.name = ?");
      }

      sql.append("""
          GROUP BY m.member_id, u.name, u.phone, u.email, u.gender, u.dob,
                   m.weight, m.height, m.bmi, m.goal, m.pt_note, pkg.name
          ORDER BY u.name
          """);

      Query query = em.createNativeQuery(sql.toString());
      query.setParameter(paramIndex++, trainerId);

      if (keyword != null && !keyword.trim().isEmpty()) {
        String keywordParam = "%" + keyword + "%";
        query.setParameter(paramIndex++, keywordParam);
        query.setParameter(paramIndex++, keywordParam);
        query.setParameter(paramIndex++, keywordParam);
      }

      if (packageName != null && !packageName.trim().isEmpty()) {
        query.setParameter(paramIndex++, packageName);
      }

      return query.getResultList();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error getting students by trainer with filter: " + trainerId, e);
      return Collections.emptyList();
    }
  }

  /**
   * Get statistics for trainer's students
   * 
   * @param trainerId The trainer ID
   * @return Object array: [totalStudents, activeStudents, achievedGoalCount]
   */
  public Object[] getStudentStatistics(Integer trainerId) {
    try {
      // Use native query for CASE expressions
      String sql = """
          SELECT
              COUNT(DISTINCT m.member_id) AS total_students,
              COUNT(DISTINCT CASE WHEN b.booking_status IN ('PENDING', 'CONFIRMED', 'COMPLETED') THEN m.member_id END) AS active_students,
              COUNT(DISTINCT CASE WHEN m.goal IS NOT NULL AND m.goal != '' THEN m.member_id END) AS achieved_goal_count
          FROM pt_bookings b
          JOIN members m ON b.member_id = m.member_id
          WHERE b.trainer_id = ?
            AND b.booking_status IN ('PENDING', 'CONFIRMED', 'COMPLETED')
          """;

      Query query = em.createNativeQuery(sql);
      query.setParameter(1, trainerId);

      Object result = query.getSingleResult();
      if (result instanceof Object[]) {
        return (Object[]) result;
      } else {
        // If single result, wrap it
        return new Object[] { result, 0L, 0L };
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error getting student statistics: " + trainerId, e);
      return new Object[] { 0L, 0L, 0L };
    }
  }

  /**
   * Get student detail by member ID and trainer ID
   * 
   * @param memberId  The member ID
   * @param trainerId The trainer ID
   * @return Object array containing detailed student information
   */
  @SuppressWarnings("unchecked")
  public Object[] getStudentDetail(Integer memberId, Integer trainerId) {
    try {
      // Use native query for CASE expressions
      String sql = """
          SELECT
              m.member_id,
              u.name,
              u.phone,
              u.email,
              u.gender,
              u.dob,
              u.address,
              m.weight,
              m.height,
              m.bmi,
              m.goal,
              m.pt_note,
              pkg.name AS package_name,
              pkg.duration_months,
              COUNT(DISTINCT b.booking_id) AS total_bookings,
              SUM(CASE WHEN b.booking_status = 'COMPLETED' THEN 1 ELSE 0 END) AS completed_sessions,
              SUM(CASE WHEN b.booking_status = 'CONFIRMED' THEN 1 ELSE 0 END) AS confirmed_sessions
          FROM pt_bookings b
          JOIN members m ON b.member_id = m.member_id
          JOIN user u ON m.member_id = u.user_id
          LEFT JOIN memberships mem ON mem.member_id = m.member_id AND mem.status = 'active'
          LEFT JOIN packages pkg ON mem.package_id = pkg.package_id
          WHERE b.trainer_id = ?
            AND m.member_id = ?
            AND b.booking_status IN ('PENDING', 'CONFIRMED', 'COMPLETED')
          GROUP BY m.member_id, u.name, u.phone, u.email, u.gender, u.dob, u.address,
                   m.weight, m.height, m.bmi, m.goal, m.pt_note, pkg.name, pkg.duration_months
          """;

      Query query = em.createNativeQuery(sql);
      query.setParameter(1, trainerId);
      query.setParameter(2, memberId);

      List<Object[]> results = query.getResultList();
      if (results != null && !results.isEmpty()) {
        return results.get(0);
      }
      return null;
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error getting student detail: memberId=" + memberId + ", trainerId=" + trainerId, e);
      return null;
    }
  }
}
