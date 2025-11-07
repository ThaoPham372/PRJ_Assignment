package com.gym.dao;

import com.gym.model.Trainer;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * TrainerDAO - Data Access Object for trainer table using JPA
 * Extends GenericDAO for basic CRUD operations
 * Handles trainer-specific information (specialization, experience, ratings, etc.)
 */
public class TrainerDAO extends GenericDAO<Trainer> {

    public TrainerDAO() {
        super(Trainer.class);
    }

    /**
     * Find trainer by user_id
     * Uses JPA find() which will load Trainer entity if it exists (JOINED inheritance)
     */
    public Optional<Trainer> findByUserId(int userId) {
        try {
            Trainer trainer = em.find(Trainer.class, userId);
            return Optional.ofNullable(trainer);
        } catch (Exception e) {
            System.err.println("[TrainerDAO] ERROR finding trainer by user_id: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Find trainer by name
     */
    public Trainer findByName(String name) {
        return findByField("name", name);
    }

    /**
     * Find trainer by email
     */
    public Trainer findByEmail(String email) {
        return findByField("email", email);
    }

    /**
     * Check if trainer exists by name
     */
    public boolean existsByName(String name) {
        Trainer trainer = findByField("name", name);
        return trainer != null;
    }

    /**
     * Check if trainer exists by email
     */
    public boolean existsByEmail(String email) {
        Trainer trainer = findByField("email", email);
        return trainer != null;
    }

    /**
     * Find trainer by name or email
     */
    public Trainer findByNameOrEmail(String nameOrEmail) {
        Trainer trainer = findByField("name", nameOrEmail);
        if (trainer == null) {
            trainer = findByField("email", nameOrEmail);
        }
        return trainer;
    }

    /**
     * Save trainer (insert new or update existing)
     * Uses GenericDAO save() method
     */
    public int saveTrainer(Trainer trainer) {
        try {
            return save(trainer);
        } catch (Exception e) {
            System.err.println("[TrainerDAO] ERROR saving trainer: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save trainer: " + e.getMessage(), e);
        }
    }

    /**
     * Update trainer
     * Uses GenericDAO update() method
     */
    public boolean updateTrainer(Trainer trainer) {
        try {
            int result = update(trainer);
            return result != -1;
        } catch (Exception e) {
            System.err.println("[TrainerDAO] ERROR updating trainer: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Find all trainers
     */
    public List<Trainer> findAllTrainers() {
        return findAll();
    }

    /**
     * Find trainers by specialization
     */
    public List<Trainer> findBySpecialization(String specialization) {
        try {
            TypedQuery<Trainer> query = em.createQuery(
                "SELECT t FROM Trainer t WHERE t.specialization = :specialization",
                Trainer.class
            );
            query.setParameter("specialization", specialization);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("[TrainerDAO] Error finding trainers by specialization: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Find trainers by certification level
     */
    public List<Trainer> findByCertificationLevel(String certificationLevel) {
        try {
            TypedQuery<Trainer> query = em.createQuery(
                "SELECT t FROM Trainer t WHERE t.certificationLevel = :certificationLevel",
                Trainer.class
            );
            query.setParameter("certificationLevel", certificationLevel);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("[TrainerDAO] Error finding trainers by certification level: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Find top rated trainers (by average rating)
     */
    public List<Trainer> findTopRatedTrainers(int limit) {
        try {
            TypedQuery<Trainer> query = em.createQuery(
                "SELECT t FROM Trainer t WHERE t.averageRating IS NOT NULL ORDER BY t.averageRating DESC",
                Trainer.class
            );
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("[TrainerDAO] Error finding top rated trainers: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Soft delete trainer (set status to INACTIVE)
     */
    public boolean softDelete(Trainer trainer) {
        try {
            trainer.setStatus("INACTIVE");
            return updateTrainer(trainer);
        } catch (Exception e) {
            System.err.println("[TrainerDAO] ERROR soft deleting trainer: " + e.getMessage());
            return false;
        }
    }

    // ========== Methods from TranerDAO1.java ==========
    
    /**
     * Lấy tất cả trainer từ database
     * 
     * @return Danh sách tất cả trainer
     */
    public List<Trainer> getAllTrainers() {
        try {
            TypedQuery<Trainer> query = em.createQuery(
                "SELECT t FROM Trainer t JOIN FETCH t.user u WHERE u.role = 'PT' AND u.status = 'ACTIVE' ORDER BY u.name",
                Trainer.class);
            return query.getResultList();
        } catch (Exception ex) {
            System.err.println("Error getting all trainers: " + ex.getMessage());
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Lấy trainer theo user_id
     * 
     * @param id user_id của trainer
     * @return Trainer object hoặc null nếu không tìm thấy
     */
    public Trainer getTrainerById(int id) {
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
        }
    }

    /**
     * Lấy danh sách trainer có đánh giá cao nhất
     * 
     * @param limit Số lượng trainer cần lấy
     * @return Danh sách trainer được sắp xếp theo rating giảm dần
     */
    public List<Trainer> getTopRatedTrainers(int limit) {
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
        }
    }

    /**
     * Lấy thống kê trainer trong khoảng thời gian
     * 
     * @param from Ngày bắt đầu
     * @param to   Ngày kết thúc
     * @return Danh sách trainer với thống kê trong khoảng thời gian
     */
    public List<Trainer> getTrainerStatistics(LocalDate from, LocalDate to) {
        try {
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
        }
    }

    /**
     * Lấy danh sách trainer với báo cáo tiến độ học viên
     * 
     * @return Danh sách trainer kèm thông tin tiến độ học viên
     */
    public List<Trainer> getTrainersWithProgressReport() {
        try {
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
        }
    }

    /**
     * Lấy số buổi tập theo tháng (để tạo biểu đồ cột)
     * 
     * @param months Số tháng cần lấy (tính từ tháng hiện tại về trước)
     * @return Danh sách Map chứa: month (String), year (Integer), monthNumber
     *         (Integer), count (Long)
     */
    public List<Map<String, Object>> getSessionsByMonth(int months) {
        try {
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
                map.put("month", String.format("%04d-%02d", year, monthNumber));
                map.put("monthLabel", YearMonth.of(year, monthNumber).getMonth().name() + " " + year);
                map.put("count", count);
                data.add(map);
            }

            return data;
        } catch (Exception ex) {
            System.err.println("Error getting sessions by month: " + ex.getMessage());
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Lấy phân loại loại hình tập luyện (để tạo biểu đồ tròn)
     * 
     * @return Danh sách Map chứa: trainingType (String), count (Long)
     */
    public List<Map<String, Object>> getTrainingTypeDistribution() {
        try {
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
        }
    }

    /**
     * Lấy tỷ lệ hoàn thành buổi tập theo tuần (để tạo biểu đồ vùng)
     * 
     * @param weeks Số tuần cần lấy (tính từ tuần hiện tại về trước)
     * @return Danh sách Map chứa: week (String), weekNumber (Integer),
     *         completionRate (Double)
     */
    public List<Map<String, Object>> getCompletionRateByWeek(int weeks) {
        try {
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
        }
    }

    /**
     * Lấy xu hướng học viên mới theo tháng (để tạo biểu đồ đường)
     * 
     * @param months Số tháng cần lấy (tính từ tháng hiện tại về trước)
     * @return Danh sách Map chứa: month (String), year (Integer), monthNumber
     *         (Integer), count (Long)
     */
    public List<Map<String, Object>> getNewStudentsTrendByMonth(int months) {
        try {
            String sql = "SELECT " +
                "YEAR(u.createdDate) as year, " +
                "MONTH(u.createdDate) as monthNumber, " +
                "COUNT(*) as count " +
                "FROM user u " +
                "INNER JOIN members m ON u.user_id = m.user_id " +
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
                map.put("month", String.format("%04d-%02d", year, monthNumber));
                map.put("monthLabel", YearMonth.of(year, monthNumber).getMonth().name() + " " + year);
                map.put("count", count);
                data.add(map);
            }

            return data;
        } catch (Exception ex) {
            System.err.println("Error getting new students trend by month: " + ex.getMessage());
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Đếm số buổi tập hoàn thành của trainer trong khoảng thời gian
     * 
     * @param trainerId ID của trainer
     * @param from      Ngày bắt đầu
     * @param to        Ngày kết thúc
     * @return Số buổi hoàn thành
     */
    public int countCompletedSessions(int trainerId, LocalDate from, LocalDate to) {
        try {
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
        }
    }

    /**
     * Đếm số buổi tập bị hủy của trainer trong khoảng thời gian
     * 
     * @param trainerId ID của trainer
     * @param from      Ngày bắt đầu
     * @param to        Ngày kết thúc
     * @return Số buổi bị hủy
     */
    public int countCancelledSessions(int trainerId, LocalDate from, LocalDate to) {
        try {
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
        }
    }

    /**
     * Tính trung bình rating của trainer trong khoảng thời gian
     * 
     * @param trainerId ID của trainer
     * @param from      Ngày bắt đầu
     * @param to        Ngày kết thúc
     * @return Trung bình rating (0.0 nếu không có dữ liệu)
     */
    public float calculateAverageRating(int trainerId, LocalDate from, LocalDate to) {
        try {
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
        }
    }

    /**
     * Lấy phân bố loại hình tập luyện của trainer
     * 
     * @param trainerId ID của trainer
     * @param from      Ngày bắt đầu
     * @param to        Ngày kết thúc
     * @return Map với key là training_type, value là số lượng buổi tập
     */
    public Map<String, Long> getTrainingTypeDistribution(int trainerId, LocalDate from, LocalDate to) {
        try {
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
        }
    }

    /**
     * Lấy số buổi tập theo từng tháng của trainer
     * 
     * @param trainerId ID của trainer
     * @return Map với key là YearMonth, value là số lượng buổi tập
     */
    public Map<YearMonth, Long> getMonthlySessionCount(int trainerId) {
        try {
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
        }
    }

    /**
     * Lấy tỷ lệ hoàn thành theo từng tuần của trainer
     * 
     * @param trainerId ID của trainer
     * @return Map với key là số tuần (1-52), value là tỷ lệ hoàn thành (0.0-1.0)
     */
    public Map<Integer, Float> getWeeklyCompletionRate(int trainerId) {
        try {
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
        }
    }

    /**
     * Đếm số buổi tập hoàn thành với filter theo gói tập và loại hình tập
     * 
     * @param trainerId    ID của trainer
     * @param from         Ngày bắt đầu
     * @param to           Ngày kết thúc
     * @param packageName  Tên gói tập (null nếu không filter)
     * @param trainingType Loại hình tập (null nếu không filter)
     * @return Số buổi hoàn thành
     */
    public int countCompletedSessionsWithFilter(int trainerId, LocalDate from, LocalDate to, String packageName,
        String trainingType) {
        try {
            StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM schedules s " +
                    "INNER JOIN members m ON s.user_id = m.user_id " +
                    "WHERE s.trainer_id = ?1 " +
                    "AND s.status = 'completed' " +
                    "AND s.training_date >= ?2 AND s.training_date <= ?3");

            if (packageName != null && !packageName.trim().isEmpty()) {
                sql.append(" AND m.training_package = ?4");
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
        }
    }

    /**
     * Đếm số buổi tập bị hủy với filter theo gói tập và loại hình tập
     * 
     * @param trainerId    ID của trainer
     * @param from         Ngày bắt đầu
     * @param to           Ngày kết thúc
     * @param packageName  Tên gói tập (null nếu không filter)
     * @param trainingType Loại hình tập (null nếu không filter)
     * @return Số buổi bị hủy
     */
    public int countCancelledSessionsWithFilter(int trainerId, LocalDate from, LocalDate to, String packageName,
        String trainingType) {
        try {
            StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM schedules s " +
                    "INNER JOIN students st ON s.user_id = st.user_id " +
                    "WHERE s.trainer_id = ?1 " +
                    "AND s.status = 'cancelled' " +
                    "AND s.training_date >= ?2 AND s.training_date <= ?3");

            if (packageName != null && !packageName.trim().isEmpty()) {
                sql.append(" AND m.training_package = ?4");
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
        }
    }

    /**
     * Lấy đánh giá trung bình theo từng tháng của trainer trong năm
     * 
     * @param trainerId ID của trainer
     * @param year      Năm cần lấy
     * @return Map với key là YearMonth, value là đánh giá trung bình
     */
    public Map<YearMonth, Float> getMonthlyAverageRating(int trainerId, int year) {
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
        }
    }

    /**
     * Lấy số buổi tập hoàn thành theo tuần để phân tích hiệu suất
     * 
     * @param trainerId ID của trainer
     * @param weeks     Số tuần cần lấy
     * @return Map với key là số tuần, value là số buổi hoàn thành
     */
    public Map<Integer, Integer> getWeeklyCompletedSessions(int trainerId, int weeks) {
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
        }
    }

    /**
     * Lấy số buổi tập hoàn thành theo tháng để phân tích hiệu suất
     * 
     * @param trainerId ID của trainer
     * @param months    Số tháng cần lấy
     * @return Map với key là YearMonth, value là số buổi hoàn thành
     */
    public Map<YearMonth, Integer> getMonthlyCompletedSessions(int trainerId, int months) {
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
        }
    }
}
