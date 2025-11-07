package com.gym.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gym.dao.ITrainerAwardDAO;
import com.gym.dao.ITrainerDAO;
import com.gym.dao.TrainerAwardDAO;
import com.gym.dao.TrainerDAO;
import com.gym.dto.PerformanceTrendDTO;
import com.gym.dto.TrainerAwardDTO;
import com.gym.model.Trainer;
import com.gym.model.TrainerAward;

/**
 * TrainerService Implementation
 * Thực thi các phương thức xử lý business logic cho Trainer
 */
public class TrainerServiceImpl implements ITrainerService {

  private final ITrainerDAO trainerDAO;
  private final ITrainerAwardDAO trainerAwardDAO;

  public TrainerServiceImpl() {
    this.trainerDAO = new TrainerDAO();
    this.trainerAwardDAO = new TrainerAwardDAO();
  }

  public TrainerServiceImpl(ITrainerDAO trainerDAO) {
    this.trainerDAO = trainerDAO;
    this.trainerAwardDAO = new TrainerAwardDAO();
  }

  public TrainerServiceImpl(ITrainerDAO trainerDAO, ITrainerAwardDAO trainerAwardDAO) {
    this.trainerDAO = trainerDAO;
    this.trainerAwardDAO = trainerAwardDAO;
  }

  @Override
  public List<Trainer> getAllTrainers() {
    try {
      return trainerDAO.getAllTrainers();
    } catch (Exception ex) {
      System.err.println("Error getting all trainers in service: " + ex.getMessage());
      ex.printStackTrace();
      return new ArrayList<>();
    }
  }

  @Override
  public Trainer getTrainerById(int id) {
    if (id <= 0) {
      return null;
    }
    try {
      return trainerDAO.getTrainerById(id);
    } catch (Exception ex) {
      System.err.println("Error getting trainer by id in service: " + ex.getMessage());
      ex.printStackTrace();
      return null;
    }
  }

  @Override
  public List<Trainer> getTopRatedTrainers(int limit) {
    if (limit <= 0) {
      limit = 10; // Default limit
    }
    try {
      return trainerDAO.getTopRatedTrainers(limit);
    } catch (Exception ex) {
      System.err.println("Error getting top rated trainers in service: " + ex.getMessage());
      ex.printStackTrace();
      return new ArrayList<>();
    }
  }

  @Override
  public List<Trainer> getTrainerStatistics(LocalDate from, LocalDate to) {
    // Validate date range
    if (from == null || to == null) {
      // Default to current month if not provided
      LocalDate now = LocalDate.now();
      from = now.withDayOfMonth(1);
      to = now;
    }

    if (from.isAfter(to)) {
      // Swap if from > to
      LocalDate temp = from;
      from = to;
      to = temp;
    }

    try {
      List<Trainer> trainers = trainerDAO.getTrainerStatistics(from, to);

      // Tính toán thêm các chỉ số thống kê nếu cần
      for (Trainer trainer : trainers) {
        calculateAdditionalStatistics(trainer);
      }

      return trainers;
    } catch (Exception ex) {
      System.err.println("Error getting trainer statistics in service: " + ex.getMessage());
      ex.printStackTrace();
      return new ArrayList<>();
    }
  }

  @Override
  public List<Map<String, Object>> getMonthlyTrainerPerformance() {
    try {
      List<Trainer> trainers = trainerDAO.getAllTrainers();
      List<Map<String, Object>> performanceData = new ArrayList<>();

      // Lấy dữ liệu 6 tháng gần nhất
      YearMonth currentMonth = YearMonth.now();
      for (int i = 5; i >= 0; i--) {
        YearMonth month = currentMonth.minusMonths(i);
        Map<String, Object> monthData = new LinkedHashMap<>();

        monthData.put("month", month.toString()); // Format: "2025-01"
        monthData.put("monthLabel", month.getMonth().name() + " " + month.getYear()); // "JANUARY 2025"

        // Tính tổng số buổi tập trong tháng (giả sử từ sessions_this_month)
        int totalSessions = trainers.stream()
            .mapToInt(t -> t.getSessionsThisMonth() != null ? t.getSessionsThisMonth() : 0)
            .sum();

        // Tính tổng số buổi hủy
        int totalCancelled = trainers.stream()
            .mapToInt(t -> t.getCancelledSessionsThisMonth() != null ? t.getCancelledSessionsThisMonth() : 0)
            .sum();

        // Tính tổng số học viên mới
        int totalNewStudents = trainers.stream()
            .mapToInt(t -> t.getNewStudentsThisMonth() != null ? t.getNewStudentsThisMonth() : 0)
            .sum();

        // Tính đánh giá trung bình
        double avgRating = trainers.stream()
            .filter(t -> t.getAverageRatingThisMonth() != null)
            .mapToDouble(t -> t.getAverageRatingThisMonth())
            .average()
            .orElse(0.0);

        // Tính tỷ lệ hoàn thành trung bình
        double avgCompletionRate = trainers.stream()
            .filter(t -> t.getCompletionRate() != null)
            .mapToDouble(t -> t.getCompletionRate())
            .average()
            .orElse(0.0);

        monthData.put("totalSessions", totalSessions);
        monthData.put("totalCancelled", totalCancelled);
        monthData.put("totalNewStudents", totalNewStudents);
        monthData.put("averageRating", Math.round(avgRating * 10.0) / 10.0); // Round to 1 decimal
        monthData.put("averageCompletionRate", Math.round(avgCompletionRate * 100.0) / 100.0); // Round to 2 decimals

        // Tính phần trăm thay đổi so với tháng trước (nếu có)
        if (i < 5) {
          Map<String, Object> previousMonthData = performanceData.get(performanceData.size() - 1);
          int prevSessions = (Integer) previousMonthData.get("totalSessions");
          double sessionsChange = prevSessions > 0
              ? ((double) (totalSessions - prevSessions) / prevSessions) * 100
              : 0.0;
          monthData.put("sessionsChangePercent", Math.round(sessionsChange * 10.0) / 10.0);
        } else {
          monthData.put("sessionsChangePercent", 0.0);
        }

        performanceData.add(monthData);
      }

      return performanceData;
    } catch (Exception ex) {
      System.err.println("Error getting monthly trainer performance in service: " + ex.getMessage());
      ex.printStackTrace();
      return new ArrayList<>();
    }
  }

  /**
   * Tính toán thêm các chỉ số thống kê cho trainer
   * 
   * @param trainer Trainer object cần tính toán
   */
  private void calculateAdditionalStatistics(Trainer trainer) {
    // Tính tỷ lệ phần trăm hoàn thành nếu chưa có
    if (trainer.getCompletionRate() == null && trainer.getSessionsThisMonth() != null) {
      int totalSessions = trainer.getSessionsThisMonth();
      int cancelledSessions = trainer.getCancelledSessionsThisMonth() != null
          ? trainer.getCancelledSessionsThisMonth()
          : 0;

      if (totalSessions > 0) {
        float completionRate = (float) (totalSessions - cancelledSessions) / totalSessions;
        trainer.setCompletionRate(completionRate);
      }
    }

    // Có thể thêm các tính toán khác như:
    // - Tỷ lệ tăng trưởng học viên
    // - Hiệu suất so với tháng trước
    // - v.v.
  }

  /**
   * Tính toán phần trăm thay đổi so với tháng trước
   * 
   * @param currentValue  Giá trị hiện tại
   * @param previousValue Giá trị tháng trước
   * @return Phần trăm thay đổi (có thể âm nếu giảm)
   */
  private double calculatePercentageChange(double currentValue, double previousValue) {
    if (previousValue == 0) {
      return currentValue > 0 ? 100.0 : 0.0;
    }
    return ((currentValue - previousValue) / previousValue) * 100.0;
  }

  @Override
  public List<Map<String, Object>> getSessionsByMonth(int months) {
    try {
      if (months <= 0) {
        months = 6; // Default 6 months
      }
      return trainerDAO.getSessionsByMonth(months);
    } catch (Exception ex) {
      System.err.println("Error getting sessions by month in service: " + ex.getMessage());
      ex.printStackTrace();
      return new ArrayList<>();
    }
  }

  @Override
  public List<Map<String, Object>> getTrainingTypeDistribution() {
    try {
      return trainerDAO.getTrainingTypeDistribution();
    } catch (Exception ex) {
      System.err.println("Error getting training type distribution in service: " + ex.getMessage());
      ex.printStackTrace();
      return new ArrayList<>();
    }
  }

  @Override
  public List<Map<String, Object>> getCompletionRateByWeek(int weeks) {
    try {
      if (weeks <= 0) {
        weeks = 12; // Default 12 weeks
      }
      return trainerDAO.getCompletionRateByWeek(weeks);
    } catch (Exception ex) {
      System.err.println("Error getting completion rate by week in service: " + ex.getMessage());
      ex.printStackTrace();
      return new ArrayList<>();
    }
  }

  @Override
  public List<Map<String, Object>> getNewStudentsTrendByMonth(int months) {
    try {
      if (months <= 0) {
        months = 6; // Default 6 months
      }
      return trainerDAO.getNewStudentsTrendByMonth(months);
    } catch (Exception ex) {
      System.err.println("Error getting new students trend by month in service: " + ex.getMessage());
      ex.printStackTrace();
      return new ArrayList<>();
    }
  }

  @Override
  public List<Map<String, Object>> getTopRatedTrainersData(int limit) {
    try {
      if (limit <= 0) {
        limit = 10; // Default 10 trainers
      }
      List<Trainer> trainers = trainerDAO.getTopRatedTrainers(limit);
      List<Map<String, Object>> data = new ArrayList<>();

      for (Trainer trainer : trainers) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (trainer.getUser() != null) {
          map.put("trainerId", trainer.getUserId());
          map.put("trainerName", trainer.getUser().getName());
          map.put("username", trainer.getUser().getUsername());
        }
        map.put("averageRating", trainer.getAverageRating() != null ? trainer.getAverageRating() : 0.0);
        map.put("sessionsThisMonth", trainer.getSessionsThisMonth() != null ? trainer.getSessionsThisMonth() : 0);
        map.put("studentsCount", trainer.getStudentsCount() != null ? trainer.getStudentsCount() : 0);
        map.put("specialization", trainer.getSpecialization());
        map.put("yearsOfExperience", trainer.getYearsOfExperience() != null ? trainer.getYearsOfExperience() : 0);
        data.add(map);
      }

      return data;
    } catch (Exception ex) {
      System.err.println("Error getting top rated trainers data in service: " + ex.getMessage());
      ex.printStackTrace();
      return new ArrayList<>();
    }
  }

  @Override
  public int countCompletedSessions(int trainerId, LocalDate from, LocalDate to) {
    try {
      if (from == null || to == null) {
        LocalDate now = LocalDate.now();
        from = now.withDayOfMonth(1);
        to = now;
      }
      return trainerDAO.countCompletedSessions(trainerId, from, to);
    } catch (Exception ex) {
      System.err.println("Error counting completed sessions in service: " + ex.getMessage());
      ex.printStackTrace();
      return 0;
    }
  }

  @Override
  public int countCancelledSessions(int trainerId, LocalDate from, LocalDate to) {
    try {
      if (from == null || to == null) {
        LocalDate now = LocalDate.now();
        from = now.withDayOfMonth(1);
        to = now;
      }
      return trainerDAO.countCancelledSessions(trainerId, from, to);
    } catch (Exception ex) {
      System.err.println("Error counting cancelled sessions in service: " + ex.getMessage());
      ex.printStackTrace();
      return 0;
    }
  }

  @Override
  public float calculateCompletionRate(int trainerId, LocalDate from, LocalDate to) {
    try {
      if (from == null || to == null) {
        LocalDate now = LocalDate.now();
        from = now.withDayOfMonth(1);
        to = now;
      }

      // Logic tính toán tỷ lệ hoàn thành trong Service
      int completed = trainerDAO.countCompletedSessions(trainerId, from, to);
      int cancelled = trainerDAO.countCancelledSessions(trainerId, from, to);

      int total = completed + cancelled;
      if (total == 0) {
        return 0.0f;
      }

      return (float) completed / total;
    } catch (Exception ex) {
      System.err.println("Error calculating completion rate in service: " + ex.getMessage());
      ex.printStackTrace();
      return 0.0f;
    }
  }

  @Override
  public float calculateAverageRating(int trainerId, LocalDate from, LocalDate to) {
    try {
      if (from == null || to == null) {
        LocalDate now = LocalDate.now();
        from = now.withDayOfMonth(1);
        to = now;
      }
      return trainerDAO.calculateAverageRating(trainerId, from, to);
    } catch (Exception ex) {
      System.err.println("Error calculating average rating in service: " + ex.getMessage());
      ex.printStackTrace();
      return 0.0f;
    }
  }

  @Override
  public Map<String, Long> getTrainingTypeDistribution(int trainerId, LocalDate from, LocalDate to) {
    try {
      if (from == null || to == null) {
        LocalDate now = LocalDate.now();
        from = now.withDayOfMonth(1);
        to = now;
      }
      return trainerDAO.getTrainingTypeDistribution(trainerId, from, to);
    } catch (Exception ex) {
      System.err.println("Error getting training type distribution in service: " + ex.getMessage());
      ex.printStackTrace();
      return new LinkedHashMap<>();
    }
  }

  @Override
  public Map<java.time.YearMonth, Long> getMonthlySessionCount(int trainerId) {
    try {
      return trainerDAO.getMonthlySessionCount(trainerId);
    } catch (Exception ex) {
      System.err.println("Error getting monthly session count in service: " + ex.getMessage());
      ex.printStackTrace();
      return new LinkedHashMap<>();
    }
  }

  @Override
  public Map<Integer, Float> getWeeklyCompletionRate(int trainerId) {
    try {
      return trainerDAO.getWeeklyCompletionRate(trainerId);
    } catch (Exception ex) {
      System.err.println("Error getting weekly completion rate in service: " + ex.getMessage());
      ex.printStackTrace();
      return new LinkedHashMap<>();
    }
  }

  @Override
  public int countCompletedSessionsWithFilter(int trainerId, LocalDate from, LocalDate to, String packageName,
      String trainingType) {
    try {
      if (from == null || to == null) {
        LocalDate now = LocalDate.now();
        from = now.withDayOfMonth(1);
        to = now;
      }
      return trainerDAO.countCompletedSessionsWithFilter(trainerId, from, to, packageName, trainingType);
    } catch (Exception ex) {
      System.err.println("Error counting completed sessions with filter in service: " + ex.getMessage());
      ex.printStackTrace();
      return 0;
    }
  }

  @Override
  public int countCancelledSessionsWithFilter(int trainerId, LocalDate from, LocalDate to, String packageName,
      String trainingType) {
    try {
      if (from == null || to == null) {
        LocalDate now = LocalDate.now();
        from = now.withDayOfMonth(1);
        to = now;
      }
      return trainerDAO.countCancelledSessionsWithFilter(trainerId, from, to, packageName, trainingType);
    } catch (Exception ex) {
      System.err.println("Error counting cancelled sessions with filter in service: " + ex.getMessage());
      ex.printStackTrace();
      return 0;
    }
  }

  @Override
  public Map<YearMonth, Float> getMonthlyAverageRating(int trainerId, int year) {
    try {
      if (year <= 0) {
        year = LocalDate.now().getYear();
      }
      return trainerDAO.getMonthlyAverageRating(trainerId, year);
    } catch (Exception ex) {
      System.err.println("Error getting monthly average rating in service: " + ex.getMessage());
      ex.printStackTrace();
      return new LinkedHashMap<>();
    }
  }

  @Override
  public List<PerformanceTrendDTO> getWeeklyPerformanceTrend(int trainerId, int weeks) {
    try {
      if (weeks <= 0) {
        weeks = 8; // Default 8 weeks
      }
      Map<Integer, Integer> weeklySessions = trainerDAO.getWeeklyCompletedSessions(trainerId, weeks);
      List<PerformanceTrendDTO> trends = new ArrayList<>();

      Integer previousSessions = null;
      for (Map.Entry<Integer, Integer> entry : weeklySessions.entrySet()) {
        int weekNumber = entry.getKey();
        int completedSessions = entry.getValue();
        double changePercent = 0.0;
        PerformanceTrendDTO.TrendDirection direction = PerformanceTrendDTO.TrendDirection.STABLE;

        if (previousSessions != null && previousSessions > 0) {
          changePercent = ((double) (completedSessions - previousSessions) / previousSessions) * 100.0;
          if (changePercent > 0) {
            direction = PerformanceTrendDTO.TrendDirection.UP;
          } else if (changePercent < 0) {
            direction = PerformanceTrendDTO.TrendDirection.DOWN;
          }
        }

        PerformanceTrendDTO trend = new PerformanceTrendDTO(weekNumber, completedSessions, changePercent, direction);
        trends.add(trend);
        previousSessions = completedSessions;
      }

      return trends;
    } catch (Exception ex) {
      System.err.println("Error getting weekly performance trend in service: " + ex.getMessage());
      ex.printStackTrace();
      return new ArrayList<>();
    }
  }

  @Override
  public List<PerformanceTrendDTO> getMonthlyPerformanceTrend(int trainerId, int months) {
    try {
      if (months <= 0) {
        months = 6; // Default 6 months
      }
      Map<YearMonth, Integer> monthlySessions = trainerDAO.getMonthlyCompletedSessions(trainerId, months);
      List<PerformanceTrendDTO> trends = new ArrayList<>();

      Integer previousSessions = null;
      YearMonth previousMonth = null;
      for (Map.Entry<YearMonth, Integer> entry : monthlySessions.entrySet()) {
        YearMonth yearMonth = entry.getKey();
        int completedSessions = entry.getValue();
        double changePercent = 0.0;
        PerformanceTrendDTO.TrendDirection direction = PerformanceTrendDTO.TrendDirection.STABLE;

        if (previousSessions != null && previousSessions > 0) {
          changePercent = ((double) (completedSessions - previousSessions) / previousSessions) * 100.0;
          if (changePercent > 0) {
            direction = PerformanceTrendDTO.TrendDirection.UP;
          } else if (changePercent < 0) {
            direction = PerformanceTrendDTO.TrendDirection.DOWN;
          }
        }

        PerformanceTrendDTO trend = new PerformanceTrendDTO(yearMonth.getMonthValue(), yearMonth.getYear(),
            completedSessions, changePercent, direction);
        trends.add(trend);
        previousSessions = completedSessions;
        previousMonth = yearMonth;
      }

      return trends;
    } catch (Exception ex) {
      System.err.println("Error getting monthly performance trend in service: " + ex.getMessage());
      ex.printStackTrace();
      return new ArrayList<>();
    }
  }

  @Override
  public List<TrainerAwardDTO> assignMonthlyAwards(LocalDate month) {
    try {
      List<TrainerAwardDTO> awardsDTO = new ArrayList<>();
      if (month == null) {
        month = LocalDate.now().withDayOfMonth(1);
      }

      LocalDate monthStart = month.withDayOfMonth(1);
      LocalDate monthEnd = month.withDayOfMonth(month.lengthOfMonth());

      // Lấy tất cả trainers
      List<Trainer> trainers = trainerDAO.getAllTrainers();

      // 1. Top PT trong tháng (nhiều buổi tập nhất)
      Trainer topSessionsTrainer = null;
      int maxSessions = 0;
      for (Trainer trainer : trainers) {
        int sessions = trainerDAO.countCompletedSessions(trainer.getUserId(), monthStart, monthEnd);
        if (sessions > maxSessions) {
          maxSessions = sessions;
          topSessionsTrainer = trainer;
        }
      }
      if (topSessionsTrainer != null && topSessionsTrainer.getUser() != null) {
        String awardName = "Top PT trong tháng " + month.getMonthValue() + "/" + month.getYear();
        String description = "Huấn luyện viên có nhiều buổi tập nhất trong tháng (" + maxSessions + " buổi)";

        // Kiểm tra xem đã có danh hiệu này chưa
        if (!trainerAwardDAO.existsByTrainerIdAndAwardNameAndMonth(topSessionsTrainer.getUserId(), awardName,
            monthStart)) {
          // Lưu vào database
          TrainerAward award = new TrainerAward(topSessionsTrainer, awardName, monthStart);
          award.setDescription(description);
          trainerAwardDAO.save(award);
        }

        // Tạo DTO để trả về
        TrainerAwardDTO awardDTO = new TrainerAwardDTO(topSessionsTrainer.getUserId(),
            topSessionsTrainer.getUser().getName(), awardName, monthStart,
            TrainerAwardDTO.AwardType.TOP_SESSIONS_MONTH);
        awardDTO.setDescription(description);
        awardsDTO.add(awardDTO);
      }

      // 2. PT được đánh giá cao nhất
      Trainer topRatingTrainer = null;
      float maxRating = 0.0f;
      for (Trainer trainer : trainers) {
        float rating = trainerDAO.calculateAverageRating(trainer.getUserId(), monthStart, monthEnd);
        if (rating > maxRating) {
          maxRating = rating;
          topRatingTrainer = trainer;
        }
      }
      if (topRatingTrainer != null && topRatingTrainer.getUser() != null && maxRating > 0) {
        String awardName = "PT được đánh giá cao nhất tháng " + month.getMonthValue() + "/" + month.getYear();
        String description = "Huấn luyện viên có đánh giá trung bình cao nhất trong tháng ("
            + String.format("%.1f", maxRating) + " điểm)";

        // Kiểm tra xem đã có danh hiệu này chưa
        if (!trainerAwardDAO.existsByTrainerIdAndAwardNameAndMonth(topRatingTrainer.getUserId(), awardName,
            monthStart)) {
          // Lưu vào database
          TrainerAward award = new TrainerAward(topRatingTrainer, awardName, monthStart);
          award.setDescription(description);
          trainerAwardDAO.save(award);
        }

        // Tạo DTO để trả về
        TrainerAwardDTO awardDTO = new TrainerAwardDTO(topRatingTrainer.getUserId(),
            topRatingTrainer.getUser().getName(), awardName, monthStart,
            TrainerAwardDTO.AwardType.TOP_RATING);
        awardDTO.setDescription(description);
        awardsDTO.add(awardDTO);
      }

      // 3. PT có tỷ lệ hoàn thành cao nhất
      Trainer topCompletionTrainer = null;
      float maxCompletionRate = 0.0f;
      for (Trainer trainer : trainers) {
        float completionRate = calculateCompletionRate(trainer.getUserId(), monthStart, monthEnd);
        if (completionRate > maxCompletionRate) {
          maxCompletionRate = completionRate;
          topCompletionTrainer = trainer;
        }
      }
      if (topCompletionTrainer != null && topCompletionTrainer.getUser() != null && maxCompletionRate > 0) {
        String awardName = "PT có tỷ lệ hoàn thành cao nhất tháng " + month.getMonthValue() + "/" + month.getYear();
        String description = "Huấn luyện viên có tỷ lệ hoàn thành cao nhất trong tháng ("
            + String.format("%.1f%%", maxCompletionRate * 100) + ")";

        // Kiểm tra xem đã có danh hiệu này chưa
        if (!trainerAwardDAO.existsByTrainerIdAndAwardNameAndMonth(topCompletionTrainer.getUserId(), awardName,
            monthStart)) {
          // Lưu vào database
          TrainerAward award = new TrainerAward(topCompletionTrainer, awardName, monthStart);
          award.setDescription(description);
          trainerAwardDAO.save(award);
        }

        // Tạo DTO để trả về
        TrainerAwardDTO awardDTO = new TrainerAwardDTO(topCompletionTrainer.getUserId(),
            topCompletionTrainer.getUser().getName(), awardName, monthStart,
            TrainerAwardDTO.AwardType.TOP_COMPLETION_RATE);
        awardDTO.setDescription(description);
        awardsDTO.add(awardDTO);
      }

      return awardsDTO;
    } catch (Exception ex) {
      System.err.println("Error assigning monthly awards in service: " + ex.getMessage());
      ex.printStackTrace();
      return new ArrayList<>();
    }
  }

  @Override
  public List<TrainerAwardDTO> getTrainerAwards(int trainerId) {
    try {
      List<TrainerAward> awards = trainerAwardDAO.findByTrainerId(trainerId);
      List<TrainerAwardDTO> awardsDTO = new ArrayList<>();

      for (TrainerAward award : awards) {
        TrainerAwardDTO dto = new TrainerAwardDTO();
        dto.setTrainerId(award.getTrainer() != null ? award.getTrainer().getUserId() : null);
        dto.setTrainerName(
            award.getTrainer() != null && award.getTrainer().getUser() != null
                ? award.getTrainer().getUser().getName()
                : null);
        dto.setAwardName(award.getAwardName());
        dto.setAwardedMonth(award.getAwardedMonth());
        dto.setDescription(award.getDescription());

        // Xác định AwardType dựa trên awardName
        String awardName = award.getAwardName().toLowerCase();
        if (awardName.contains("nhiều buổi tập") || awardName.contains("top pt")) {
          dto.setAwardType(TrainerAwardDTO.AwardType.TOP_SESSIONS_MONTH);
        } else if (awardName.contains("đánh giá cao") || awardName.contains("rating")) {
          dto.setAwardType(TrainerAwardDTO.AwardType.TOP_RATING);
        } else if (awardName.contains("tỷ lệ hoàn thành") || awardName.contains("completion")) {
          dto.setAwardType(TrainerAwardDTO.AwardType.TOP_COMPLETION_RATE);
        }

        awardsDTO.add(dto);
      }

      return awardsDTO;
    } catch (Exception ex) {
      System.err.println("Error getting trainer awards in service: " + ex.getMessage());
      ex.printStackTrace();
      return new ArrayList<>();
    }
  }
}
