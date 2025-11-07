package com.gym.dao;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gym.model.Trainer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

/**
 * TrainerDAO Implementation
 * Thực thi các phương thức truy vấn dữ liệu cho entity Trainer sử dụng JPA
 */
public class TrainerDAO implements ITrainerDAO {

  private final EntityManagerFactory entityManagerFactory;

  public TrainerDAO() {
    this.entityManagerFactory = Persistence.createEntityManagerFactory("gym-pu");
  }

  @Override
  public List<Trainer> getAllTrainers() {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      TypedQuery<Trainer> query = em.createQuery(
          "SELECT t FROM Trainer t JOIN FETCH t.user u WHERE u.role = 'PT' AND u.status = 'ACTIVE' ORDER BY u.name",
          Trainer.class);
      return query.getResultList();
    } catch (Exception ex) {
      System.err.println("Error getting all trainers: " + ex.getMessage());
      ex.printStackTrace();
      return new ArrayList<>();
    } finally {
      em.close();
    }
  }

  @Override
  public Trainer getTrainerById(int id) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      TypedQuery<Trainer> query = em.createQuery(
          "SELECT t FROM Trainer t JOIN FETCH t.user u WHERE t.userId = :id",
          Trainer.class);
      query.setParameter("id", id);
      return query.getSingleResult();
    } catch (NoResultException ex) {
      return null;
    } catch (Exception ex) {
      System.err.println("Error getting trainer by id: " + ex.getMessage());
      ex.printStackTrace();
      return null;
    } finally {
      em.close();
    }
  }

  @Override
  public List<Trainer> getTopRatedTrainers(int limit) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      TypedQuery<Trainer> query = em.createQuery(
          "SELECT t FROM Trainer t JOIN FETCH t.user u " +
              "WHERE u.role = 'PT' AND u.status = 'ACTIVE' " +
              "AND t.averageRating IS NOT NULL " +
              "ORDER BY t.averageRating DESC, t.sessionsThisMonth DESC",
          Trainer.class);
      query.setMaxResults(limit > 0 ? limit : 10);
      return query.getResultList();
    } catch (Exception ex) {
      System.err.println("Error getting top rated trainers: " + ex.getMessage());
      ex.printStackTrace();
      return new ArrayList<>();
    } finally {
      em.close();
    }
  }

  @Override
  public List<Trainer> getTrainerStatistics(LocalDate from, LocalDate to) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      // Lấy tất cả trainer active và filter theo các trường thống kê trong khoảng
      // thời gian
      // Giả sử các trường thống kê đã được tính toán và lưu trong bảng trainer
      // Nếu cần tính toán từ Schedule, có thể join với Schedule sau
      TypedQuery<Trainer> query = em.createQuery(
          "SELECT t FROM Trainer t JOIN FETCH t.user u " +
              "WHERE u.role = 'PT' AND u.status = 'ACTIVE' " +
              "ORDER BY t.averageRatingThisMonth DESC NULLS LAST, t.completionRate DESC NULLS LAST",
          Trainer.class);
      return query.getResultList();
    } catch (Exception ex) {
      System.err.println("Error getting trainer statistics: " + ex.getMessage());
      ex.printStackTrace();
      return new ArrayList<>();
    } finally {
      em.close();
    }
  }

  @Override
  public List<Trainer> getTrainersWithProgressReport() {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      // Lấy trainer kèm thông tin tiến độ học viên
      // Join với Student thông qua quan hệ gián tiếp (trainer phụ trách student)
      // Giả sử trainer được xác định qua role PT và có thể liên kết với student qua
      // Schedule
      TypedQuery<Trainer> query = em.createQuery(
          "SELECT DISTINCT t FROM Trainer t " +
              "JOIN FETCH t.user u " +
              "WHERE u.role = 'PT' AND u.status = 'ACTIVE' " +
              "AND (t.studentsCount > 0 OR t.completionRate IS NOT NULL) " +
              "ORDER BY t.completionRate DESC NULLS LAST, t.studentsCount DESC",
          Trainer.class);
      return query.getResultList();
    } catch (Exception ex) {
      System.err.println("Error getting trainers with progress report: " + ex.getMessage());
      ex.printStackTrace();
      return new ArrayList<>();
    } finally {
      em.close();
    }
  }

  @Override
  public List<Map<String, Object>> getSessionsByMonth(int months) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      // Sử dụng native query để GROUP BY theo năm và tháng
      String sql = "SELECT " +
          "YEAR(s.training_date) as year, " +
          "MONTH(s.training_date) as monthNumber, " +
          "COUNT(*) as count " +
          "FROM schedules s " +
          "WHERE s.training_date >= DATE_SUB(CURDATE(), INTERVAL ?1 MONTH) " +
          "GROUP BY YEAR(s.training_date), MONTH(s.training_date) " +
          "ORDER BY year DESC, monthNumber DESC";

      Query query = em.createNativeQuery(sql);
      query.setParameter(1, months);

      @SuppressWarnings("unchecked")
      List<Object[]> results = query.getResultList();
      List<Map<String, Object>> data = new ArrayList<>();

      for (Object[] row : results) {
        Map<String, Object> map = new LinkedHashMap<>();
        Integer year = ((Number) row[0]).intValue();
        Integer monthNumber = ((Number) row[1]).intValue();
        Long count = ((Number) row[2]).longValue();

        map.put("year", year);
        map.put("monthNumber", monthNumber);
        map.put("month", String.format("%04d-%02d", year, monthNumber)); // Format: "2025-01"
        map.put("monthLabel", YearMonth.of(year, monthNumber).getMonth().name() + " " + year);
        map.put("count", count);
        data.add(map);
      }

      return data;
    } catch (Exception ex) {
      System.err.println("Error getting sessions by month: " + ex.getMessage());
      ex.printStackTrace();
      return new ArrayList<>();
    } finally {
      em.close();
    }
  }

  @Override
  public List<Map<String, Object>> getTrainingTypeDistribution() {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      // Sử dụng native query để GROUP BY theo training_type
      String sql = "SELECT " +
          "COALESCE(s.training_type, 'Không xác định') as trainingType, " +
          "COUNT(*) as count " +
          "FROM schedules s " +
          "WHERE s.training_type IS NOT NULL " +
          "GROUP BY s.training_type " +
          "ORDER BY count DESC";

      Query query = em.createNativeQuery(sql);

      @SuppressWarnings("unchecked")
      List<Object[]> results = query.getResultList();
      List<Map<String, Object>> data = new ArrayList<>();

      for (Object[] row : results) {
        Map<String, Object> map = new LinkedHashMap<>();
        String trainingType = (String) row[0];
        Long count = ((Number) row[1]).longValue();

        map.put("trainingType", trainingType);
        map.put("count", count);
        data.add(map);
      }

      return data;
    } catch (Exception ex) {
      System.err.println("Error getting training type distribution: " + ex.getMessage());
      ex.printStackTrace();
      return new ArrayList<>();
    } finally {
      em.close();
    }
  }

  @Override
  public List<Map<String, Object>> getCompletionRateByWeek(int weeks) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      // Sử dụng native query để tính tỷ lệ hoàn thành theo tuần
      String sql = "SELECT " +
          "YEARWEEK(s.training_date, 1) as weekNumber, " +
          "YEAR(s.training_date) as year, " +
          "WEEK(s.training_date, 1) as week, " +
          "COUNT(*) as total, " +
          "SUM(CASE WHEN s.status = 'completed' THEN 1 ELSE 0 END) as completed " +
          "FROM schedules s " +
          "WHERE s.training_date >= DATE_SUB(CURDATE(), INTERVAL ?1 WEEK) " +
          "GROUP BY YEARWEEK(s.training_date, 1), YEAR(s.training_date), WEEK(s.training_date, 1) " +
          "ORDER BY year DESC, week DESC";

      Query query = em.createNativeQuery(sql);
      query.setParameter(1, weeks);

      @SuppressWarnings("unchecked")
      List<Object[]> results = query.getResultList();
      List<Map<String, Object>> data = new ArrayList<>();

      for (Object[] row : results) {
        Map<String, Object> map = new LinkedHashMap<>();
        Integer weekNumber = ((Number) row[0]).intValue();
        Integer year = ((Number) row[1]).intValue();
        Integer week = ((Number) row[2]).intValue();
        Long total = ((Number) row[3]).longValue();
        Long completed = ((Number) row[4]).longValue();

        double completionRate = total > 0 ? (double) completed / total : 0.0;

        map.put("weekNumber", weekNumber);
        map.put("year", year);
        map.put("week", week);
        map.put("weekLabel", "Tuần " + week + "/" + year);
        map.put("total", total);
        map.put("completed", completed);
        map.put("completionRate", Math.round(completionRate * 100.0) / 100.0);
        data.add(map);
      }

      return data;
    } catch (Exception ex) {
      System.err.println("Error getting completion rate by week: " + ex.getMessage());
      ex.printStackTrace();
      return new ArrayList<>();
    } finally {
      em.close();
    }
  }

  @Override
  public List<Map<String, Object>> getNewStudentsTrendByMonth(int months) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      // Sử dụng native query để lấy học viên mới theo tháng từ bảng user
      String sql = "SELECT " +
          "YEAR(u.createdDate) as year, " +
          "MONTH(u.createdDate) as monthNumber, " +
          "COUNT(*) as count " +
          "FROM user u " +
          "INNER JOIN students s ON u.user_id = s.user_id " +
          "WHERE u.createdDate >= DATE_SUB(CURDATE(), INTERVAL ?1 MONTH) " +
          "AND u.role = 'USER' AND u.status = 'ACTIVE' " +
          "GROUP BY YEAR(u.createdDate), MONTH(u.createdDate) " +
          "ORDER BY year DESC, monthNumber DESC";

      Query query = em.createNativeQuery(sql);
      query.setParameter(1, months);

      @SuppressWarnings("unchecked")
      List<Object[]> results = query.getResultList();
      List<Map<String, Object>> data = new ArrayList<>();

      for (Object[] row : results) {
        Map<String, Object> map = new LinkedHashMap<>();
        Integer year = ((Number) row[0]).intValue();
        Integer monthNumber = ((Number) row[1]).intValue();
        Long count = ((Number) row[2]).longValue();

        map.put("year", year);
        map.put("monthNumber", monthNumber);
        map.put("month", String.format("%04d-%02d", year, monthNumber)); // Format: "2025-01"
        map.put("monthLabel", YearMonth.of(year, monthNumber).getMonth().name() + " " + year);
        map.put("count", count);
        data.add(map);
      }

      return data;
    } catch (Exception ex) {
      System.err.println("Error getting new students trend by month: " + ex.getMessage());
      ex.printStackTrace();
      return new ArrayList<>();
    } finally {
      em.close();
    }
  }

  @Override
  public int countCompletedSessions(int trainerId, LocalDate from, LocalDate to) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      // Sử dụng native query vì Schedule entity có thể chưa có trainerId field
      String sql = "SELECT COUNT(*) FROM schedules s " +
          "WHERE s.trainer_id = ?1 " +
          "AND s.status = 'completed' " +
          "AND s.training_date >= ?2 AND s.training_date <= ?3";

      Query query = em.createNativeQuery(sql);
      query.setParameter(1, trainerId);
      query.setParameter(2, from);
      query.setParameter(3, to);

      Object result = query.getSingleResult();
      return result != null ? ((Number) result).intValue() : 0;
    } catch (Exception ex) {
      System.err.println("Error counting completed sessions: " + ex.getMessage());
      ex.printStackTrace();
      return 0;
    } finally {
      em.close();
    }
  }

  @Override
  public int countCancelledSessions(int trainerId, LocalDate from, LocalDate to) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      // Sử dụng native query vì Schedule entity có thể chưa có trainerId field
      String sql = "SELECT COUNT(*) FROM schedules s " +
          "WHERE s.trainer_id = ?1 " +
          "AND s.status = 'cancelled' " +
          "AND s.training_date >= ?2 AND s.training_date <= ?3";

      Query query = em.createNativeQuery(sql);
      query.setParameter(1, trainerId);
      query.setParameter(2, from);
      query.setParameter(3, to);

      Object result = query.getSingleResult();
      return result != null ? ((Number) result).intValue() : 0;
    } catch (Exception ex) {
      System.err.println("Error counting cancelled sessions: " + ex.getMessage());
      ex.printStackTrace();
      return 0;
    } finally {
      em.close();
    }
  }

  @Override
  public float calculateAverageRating(int trainerId, LocalDate from, LocalDate to) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      // Sử dụng native query vì rating có thể chưa có trong Schedule entity
      String sql = "SELECT AVG(s.rating) FROM schedules s " +
          "WHERE s.trainer_id = ?1 " +
          "AND s.training_date >= ?2 AND s.training_date <= ?3 " +
          "AND s.rating IS NOT NULL AND s.status = 'completed'";

      Query query = em.createNativeQuery(sql);
      query.setParameter(1, trainerId);
      query.setParameter(2, from);
      query.setParameter(3, to);

      Object result = query.getSingleResult();
      if (result == null) {
        return 0.0f;
      }
      return ((Number) result).floatValue();
    } catch (Exception ex) {
      System.err.println("Error calculating average rating: " + ex.getMessage());
      ex.printStackTrace();
      return 0.0f;
    } finally {
      em.close();
    }
  }

  @Override
  public Map<String, Long> getTrainingTypeDistribution(int trainerId, LocalDate from, LocalDate to) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      // Sử dụng native query để GROUP BY training_type
      String sql = "SELECT " +
          "COALESCE(s.training_type, 'Không xác định') as trainingType, " +
          "COUNT(*) as count " +
          "FROM schedules s " +
          "WHERE s.trainer_id = ?1 " +
          "AND s.training_date >= ?2 AND s.training_date <= ?3 " +
          "GROUP BY s.training_type " +
          "ORDER BY count DESC";

      Query query = em.createNativeQuery(sql);
      query.setParameter(1, trainerId);
      query.setParameter(2, from);
      query.setParameter(3, to);

      @SuppressWarnings("unchecked")
      List<Object[]> results = query.getResultList();
      Map<String, Long> distribution = new LinkedHashMap<>();

      for (Object[] row : results) {
        String trainingType = (String) row[0];
        Long count = ((Number) row[1]).longValue();
        distribution.put(trainingType, count);
      }

      return distribution;
    } catch (Exception ex) {
      System.err.println("Error getting training type distribution: " + ex.getMessage());
      ex.printStackTrace();
      return new LinkedHashMap<>();
    } finally {
      em.close();
    }
  }

  @Override
  public Map<YearMonth, Long> getMonthlySessionCount(int trainerId) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      // Sử dụng native query để GROUP BY theo năm và tháng
      String sql = "SELECT " +
          "YEAR(s.training_date) as year, " +
          "MONTH(s.training_date) as monthNumber, " +
          "COUNT(*) as count " +
          "FROM schedules s " +
          "WHERE s.trainer_id = ?1 " +
          "GROUP BY YEAR(s.training_date), MONTH(s.training_date) " +
          "ORDER BY year DESC, monthNumber DESC";

      Query query = em.createNativeQuery(sql);
      query.setParameter(1, trainerId);

      @SuppressWarnings("unchecked")
      List<Object[]> results = query.getResultList();
      Map<YearMonth, Long> monthlyCount = new LinkedHashMap<>();

      for (Object[] row : results) {
        Integer year = ((Number) row[0]).intValue();
        Integer monthNumber = ((Number) row[1]).intValue();
        Long count = ((Number) row[2]).longValue();
        YearMonth yearMonth = YearMonth.of(year, monthNumber);
        monthlyCount.put(yearMonth, count);
      }

      return monthlyCount;
    } catch (Exception ex) {
      System.err.println("Error getting monthly session count: " + ex.getMessage());
      ex.printStackTrace();
      return new LinkedHashMap<>();
    } finally {
      em.close();
    }
  }

  @Override
  public Map<Integer, Float> getWeeklyCompletionRate(int trainerId) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      // Sử dụng native query để tính tỷ lệ hoàn thành theo tuần
      String sql = "SELECT " +
          "WEEK(s.training_date, 1) as weekNumber, " +
          "COUNT(*) as total, " +
          "SUM(CASE WHEN s.status = 'completed' THEN 1 ELSE 0 END) as completed " +
          "FROM schedules s " +
          "WHERE s.trainer_id = ?1 " +
          "GROUP BY WEEK(s.training_date, 1) " +
          "ORDER BY weekNumber";

      Query query = em.createNativeQuery(sql);
      query.setParameter(1, trainerId);

      @SuppressWarnings("unchecked")
      List<Object[]> results = query.getResultList();
      Map<Integer, Float> weeklyRate = new LinkedHashMap<>();

      for (Object[] row : results) {
        Integer weekNumber = ((Number) row[0]).intValue();
        Long total = ((Number) row[1]).longValue();
        Long completed = ((Number) row[2]).longValue();

        float completionRate = total > 0 ? (float) completed / total : 0.0f;
        weeklyRate.put(weekNumber, completionRate);
      }

      return weeklyRate;
    } catch (Exception ex) {
      System.err.println("Error getting weekly completion rate: " + ex.getMessage());
      ex.printStackTrace();
      return new LinkedHashMap<>();
    } finally {
      em.close();
    }
  }

  @Override
  public int countCompletedSessionsWithFilter(int trainerId, LocalDate from, LocalDate to, String packageName,
      String trainingType) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      StringBuilder sql = new StringBuilder(
          "SELECT COUNT(*) FROM schedules s " +
              "INNER JOIN students st ON s.user_id = st.user_id " +
              "WHERE s.trainer_id = ?1 " +
              "AND s.status = 'completed' " +
              "AND s.training_date >= ?2 AND s.training_date <= ?3");

      if (packageName != null && !packageName.trim().isEmpty()) {
        sql.append(" AND st.training_package = ?4");
      }
      if (trainingType != null && !trainingType.trim().isEmpty()) {
        sql.append(packageName != null && !packageName.trim().isEmpty() ? " AND s.training_type = ?5"
            : " AND s.training_type = ?4");
      }

      Query query = em.createNativeQuery(sql.toString());
      query.setParameter(1, trainerId);
      query.setParameter(2, from);
      query.setParameter(3, to);

      int paramIndex = 4;
      if (packageName != null && !packageName.trim().isEmpty()) {
        query.setParameter(paramIndex++, packageName);
      }
      if (trainingType != null && !trainingType.trim().isEmpty()) {
        query.setParameter(paramIndex, trainingType);
      }

      Object result = query.getSingleResult();
      return result != null ? ((Number) result).intValue() : 0;
    } catch (Exception ex) {
      System.err.println("Error counting completed sessions with filter: " + ex.getMessage());
      ex.printStackTrace();
      return 0;
    } finally {
      em.close();
    }
  }

  @Override
  public int countCancelledSessionsWithFilter(int trainerId, LocalDate from, LocalDate to, String packageName,
      String trainingType) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      StringBuilder sql = new StringBuilder(
          "SELECT COUNT(*) FROM schedules s " +
              "INNER JOIN students st ON s.user_id = st.user_id " +
              "WHERE s.trainer_id = ?1 " +
              "AND s.status = 'cancelled' " +
              "AND s.training_date >= ?2 AND s.training_date <= ?3");

      if (packageName != null && !packageName.trim().isEmpty()) {
        sql.append(" AND st.training_package = ?4");
      }
      if (trainingType != null && !trainingType.trim().isEmpty()) {
        sql.append(packageName != null && !packageName.trim().isEmpty() ? " AND s.training_type = ?5"
            : " AND s.training_type = ?4");
      }

      Query query = em.createNativeQuery(sql.toString());
      query.setParameter(1, trainerId);
      query.setParameter(2, from);
      query.setParameter(3, to);

      int paramIndex = 4;
      if (packageName != null && !packageName.trim().isEmpty()) {
        query.setParameter(paramIndex++, packageName);
      }
      if (trainingType != null && !trainingType.trim().isEmpty()) {
        query.setParameter(paramIndex, trainingType);
      }

      Object result = query.getSingleResult();
      return result != null ? ((Number) result).intValue() : 0;
    } catch (Exception ex) {
      System.err.println("Error counting cancelled sessions with filter: " + ex.getMessage());
      ex.printStackTrace();
      return 0;
    } finally {
      em.close();
    }
  }

  @Override
  public Map<YearMonth, Float> getMonthlyAverageRating(int trainerId, int year) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      String sql = "SELECT " +
          "YEAR(s.training_date) as year, " +
          "MONTH(s.training_date) as monthNumber, " +
          "AVG(s.rating) as avgRating " +
          "FROM schedules s " +
          "WHERE s.trainer_id = ?1 " +
          "AND YEAR(s.training_date) = ?2 " +
          "AND s.rating IS NOT NULL AND s.status = 'completed' " +
          "GROUP BY YEAR(s.training_date), MONTH(s.training_date) " +
          "ORDER BY year DESC, monthNumber DESC";

      Query query = em.createNativeQuery(sql);
      query.setParameter(1, trainerId);
      query.setParameter(2, year);

      @SuppressWarnings("unchecked")
      List<Object[]> results = query.getResultList();
      Map<YearMonth, Float> monthlyRating = new LinkedHashMap<>();

      for (Object[] row : results) {
        Integer yearValue = ((Number) row[0]).intValue();
        Integer monthNumber = ((Number) row[1]).intValue();
        Float avgRating = ((Number) row[2]).floatValue();
        YearMonth yearMonth = YearMonth.of(yearValue, monthNumber);
        monthlyRating.put(yearMonth, avgRating);
      }

      return monthlyRating;
    } catch (Exception ex) {
      System.err.println("Error getting monthly average rating: " + ex.getMessage());
      ex.printStackTrace();
      return new LinkedHashMap<>();
    } finally {
      em.close();
    }
  }

  @Override
  public Map<Integer, Integer> getWeeklyCompletedSessions(int trainerId, int weeks) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      String sql = "SELECT " +
          "WEEK(s.training_date, 1) as weekNumber, " +
          "COUNT(*) as completed " +
          "FROM schedules s " +
          "WHERE s.trainer_id = ?1 " +
          "AND s.status = 'completed' " +
          "AND s.training_date >= DATE_SUB(CURDATE(), INTERVAL ?2 WEEK) " +
          "GROUP BY WEEK(s.training_date, 1) " +
          "ORDER BY weekNumber";

      Query query = em.createNativeQuery(sql);
      query.setParameter(1, trainerId);
      query.setParameter(2, weeks);

      @SuppressWarnings("unchecked")
      List<Object[]> results = query.getResultList();
      Map<Integer, Integer> weeklySessions = new LinkedHashMap<>();

      for (Object[] row : results) {
        Integer weekNumber = ((Number) row[0]).intValue();
        Integer completed = ((Number) row[1]).intValue();
        weeklySessions.put(weekNumber, completed);
      }

      return weeklySessions;
    } catch (Exception ex) {
      System.err.println("Error getting weekly completed sessions: " + ex.getMessage());
      ex.printStackTrace();
      return new LinkedHashMap<>();
    } finally {
      em.close();
    }
  }

  @Override
  public Map<YearMonth, Integer> getMonthlyCompletedSessions(int trainerId, int months) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      String sql = "SELECT " +
          "YEAR(s.training_date) as year, " +
          "MONTH(s.training_date) as monthNumber, " +
          "COUNT(*) as completed " +
          "FROM schedules s " +
          "WHERE s.trainer_id = ?1 " +
          "AND s.status = 'completed' " +
          "AND s.training_date >= DATE_SUB(CURDATE(), INTERVAL ?2 MONTH) " +
          "GROUP BY YEAR(s.training_date), MONTH(s.training_date) " +
          "ORDER BY year DESC, monthNumber DESC";

      Query query = em.createNativeQuery(sql);
      query.setParameter(1, trainerId);
      query.setParameter(2, months);

      @SuppressWarnings("unchecked")
      List<Object[]> results = query.getResultList();
      Map<YearMonth, Integer> monthlySessions = new LinkedHashMap<>();

      for (Object[] row : results) {
        Integer year = ((Number) row[0]).intValue();
        Integer monthNumber = ((Number) row[1]).intValue();
        Integer completed = ((Number) row[2]).intValue();
        YearMonth yearMonth = YearMonth.of(year, monthNumber);
        monthlySessions.put(yearMonth, completed);
      }

      return monthlySessions;
    } catch (Exception ex) {
      System.err.println("Error getting monthly completed sessions: " + ex.getMessage());
      ex.printStackTrace();
      return new LinkedHashMap<>();
    } finally {
      em.close();
    }
  }
}
