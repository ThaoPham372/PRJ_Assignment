package com.gym.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import com.gym.dto.PerformanceTrendDTO;
import com.gym.dto.TrainerAwardDTO;
import com.gym.model.Trainer;

/**
 * TrainerService Interface
 * Định nghĩa các phương thức xử lý business logic cho Trainer
 */
public interface ITrainerService {

  /**
   * Lấy tất cả trainer
   * 
   * @return Danh sách tất cả trainer
   */
  List<Trainer> getAllTrainers();

  /**
   * Lấy trainer theo ID
   * 
   * @param id user_id của trainer
   * @return Trainer object hoặc null nếu không tìm thấy
   */
  Trainer getTrainerById(int id);

  /**
   * Lấy danh sách trainer có đánh giá cao nhất
   * 
   * @param limit Số lượng trainer cần lấy
   * @return Danh sách trainer được sắp xếp theo rating giảm dần
   */
  List<Trainer> getTopRatedTrainers(int limit);

  /**
   * Lấy thống kê trainer trong khoảng thời gian
   * 
   * @param from Ngày bắt đầu
   * @param to   Ngày kết thúc
   * @return Danh sách trainer với thống kê trong khoảng thời gian
   */
  List<Trainer> getTrainerStatistics(LocalDate from, LocalDate to);

  /**
   * Lấy dữ liệu hiệu suất trainer theo tháng để dựng biểu đồ
   * 
   * @return Danh sách Map chứa dữ liệu JSON-ready cho biểu đồ
   */
  List<Map<String, Object>> getMonthlyTrainerPerformance();

  /**
   * Lấy số buổi tập theo tháng (để tạo biểu đồ cột)
   * 
   * @param months Số tháng cần lấy
   * @return Danh sách Map chứa dữ liệu JSON-ready
   */
  List<Map<String, Object>> getSessionsByMonth(int months);

  /**
   * Lấy phân loại loại hình tập luyện (để tạo biểu đồ tròn)
   * 
   * @return Danh sách Map chứa dữ liệu JSON-ready
   */
  List<Map<String, Object>> getTrainingTypeDistribution();

  /**
   * Lấy tỷ lệ hoàn thành buổi tập theo tuần (để tạo biểu đồ vùng)
   * 
   * @param weeks Số tuần cần lấy
   * @return Danh sách Map chứa dữ liệu JSON-ready
   */
  List<Map<String, Object>> getCompletionRateByWeek(int weeks);

  /**
   * Lấy xu hướng học viên mới theo tháng (để tạo biểu đồ đường)
   * 
   * @param months Số tháng cần lấy
   * @return Danh sách Map chứa dữ liệu JSON-ready
   */
  List<Map<String, Object>> getNewStudentsTrendByMonth(int months);

  /**
   * Lấy danh sách top trainer theo đánh giá (format JSON-ready)
   * 
   * @param limit Số lượng trainer cần lấy
   * @return Danh sách Map chứa thông tin trainer format JSON-ready
   */
  List<Map<String, Object>> getTopRatedTrainersData(int limit);

  /**
   * Đếm số buổi tập hoàn thành của trainer trong khoảng thời gian
   * 
   * @param trainerId ID của trainer
   * @param from      Ngày bắt đầu
   * @param to        Ngày kết thúc
   * @return Số buổi hoàn thành
   */
  int countCompletedSessions(int trainerId, LocalDate from, LocalDate to);

  /**
   * Đếm số buổi tập bị hủy của trainer trong khoảng thời gian
   * 
   * @param trainerId ID của trainer
   * @param from      Ngày bắt đầu
   * @param to        Ngày kết thúc
   * @return Số buổi bị hủy
   */
  int countCancelledSessions(int trainerId, LocalDate from, LocalDate to);

  /**
   * Tính tỷ lệ hoàn thành buổi tập của trainer
   * Logic: completed / (completed + cancelled)
   * 
   * @param trainerId ID của trainer
   * @param from      Ngày bắt đầu
   * @param to        Ngày kết thúc
   * @return Tỷ lệ hoàn thành (0.0 - 1.0)
   */
  float calculateCompletionRate(int trainerId, LocalDate from, LocalDate to);

  /**
   * Tính trung bình rating của trainer trong khoảng thời gian
   * 
   * @param trainerId ID của trainer
   * @param from      Ngày bắt đầu
   * @param to        Ngày kết thúc
   * @return Trung bình rating (0.0 nếu không có dữ liệu)
   */
  float calculateAverageRating(int trainerId, LocalDate from, LocalDate to);

  /**
   * Lấy phân bổ loại hình tập luyện của trainer
   * 
   * @param trainerId ID của trainer
   * @param from      Ngày bắt đầu
   * @param to        Ngày kết thúc
   * @return Map với key là training_type, value là số lượng buổi tập
   */
  Map<String, Long> getTrainingTypeDistribution(int trainerId, LocalDate from, LocalDate to);

  /**
   * Lấy số buổi tập theo từng tháng của trainer
   * 
   * @param trainerId ID của trainer
   * @return Map với key là YearMonth, value là số lượng buổi tập
   */
  Map<java.time.YearMonth, Long> getMonthlySessionCount(int trainerId);

  /**
   * Lấy tỷ lệ hoàn thành theo từng tuần của trainer
   * 
   * @param trainerId ID của trainer
   * @return Map với key là số tuần (1-52), value là tỷ lệ hoàn thành (0.0-1.0)
   */
  Map<Integer, Float> getWeeklyCompletionRate(int trainerId);

  /**
   * Đếm số buổi tập hoàn thành với filter theo gói tập và loại hình tập
   */
  int countCompletedSessionsWithFilter(int trainerId, LocalDate from, LocalDate to, String packageName,
      String trainingType);

  /**
   * Đếm số buổi tập bị hủy với filter theo gói tập và loại hình tập
   */
  int countCancelledSessionsWithFilter(int trainerId, LocalDate from, LocalDate to, String packageName,
      String trainingType);

  /**
   * Lấy đánh giá trung bình theo từng tháng của trainer trong năm
   */
  Map<YearMonth, Float> getMonthlyAverageRating(int trainerId, int year);

  /**
   * Phân tích hiệu suất theo tuần - so sánh với tuần trước
   */
  List<PerformanceTrendDTO> getWeeklyPerformanceTrend(int trainerId, int weeks);

  /**
   * Phân tích hiệu suất theo tháng - so sánh với tháng trước
   */
  List<PerformanceTrendDTO> getMonthlyPerformanceTrend(int trainerId, int months);

  /**
   * Tự động gán danh hiệu cho trainer trong tháng
   */
  List<TrainerAwardDTO> assignMonthlyAwards(LocalDate month);

  /**
   * Lấy danh hiệu của trainer
   */
  List<TrainerAwardDTO> getTrainerAwards(int trainerId);
}
