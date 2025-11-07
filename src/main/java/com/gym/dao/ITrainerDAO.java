package com.gym.dao;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import com.gym.model.Trainer;

/**
 * TrainerDAO Interface
 * Định nghĩa các phương thức truy vấn dữ liệu cho entity Trainer
 */
public interface ITrainerDAO {

  /**
   * Lấy tất cả trainer từ database
   * 
   * @return Danh sách tất cả trainer
   */
  List<Trainer> getAllTrainers();

  /**
   * Lấy trainer theo user_id
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
   * Lấy danh sách trainer với báo cáo tiến độ học viên
   * 
   * @return Danh sách trainer kèm thông tin tiến độ học viên
   */
  List<Trainer> getTrainersWithProgressReport();

  /**
   * Lấy số buổi tập theo tháng (để tạo biểu đồ cột)
   * 
   * @param months Số tháng cần lấy (tính từ tháng hiện tại về trước)
   * @return Danh sách Map chứa: month (String), year (Integer), monthNumber
   *         (Integer), count (Long)
   */
  List<java.util.Map<String, Object>> getSessionsByMonth(int months);

  /**
   * Lấy phân loại loại hình tập luyện (để tạo biểu đồ tròn)
   * 
   * @return Danh sách Map chứa: trainingType (String), count (Long)
   */
  List<java.util.Map<String, Object>> getTrainingTypeDistribution();

  /**
   * Lấy tỷ lệ hoàn thành buổi tập theo tuần (để tạo biểu đồ vùng)
   * 
   * @param weeks Số tuần cần lấy (tính từ tuần hiện tại về trước)
   * @return Danh sách Map chứa: week (String), weekNumber (Integer),
   *         completionRate (Double)
   */
  List<java.util.Map<String, Object>> getCompletionRateByWeek(int weeks);

  /**
   * Lấy xu hướng học viên mới theo tháng (để tạo biểu đồ đường)
   * 
   * @param months Số tháng cần lấy (tính từ tháng hiện tại về trước)
   * @return Danh sách Map chứa: month (String), year (Integer), monthNumber
   *         (Integer), count (Long)
   */
  List<java.util.Map<String, Object>> getNewStudentsTrendByMonth(int months);

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
  java.util.Map<String, Long> getTrainingTypeDistribution(int trainerId, LocalDate from, LocalDate to);

  /**
   * Lấy số buổi tập theo từng tháng của trainer
   * 
   * @param trainerId ID của trainer
   * @return Map với key là YearMonth, value là số lượng buổi tập
   */
  java.util.Map<java.time.YearMonth, Long> getMonthlySessionCount(int trainerId);

  /**
   * Lấy tỷ lệ hoàn thành theo từng tuần của trainer
   * 
   * @param trainerId ID của trainer
   * @return Map với key là số tuần (1-52), value là tỷ lệ hoàn thành (0.0-1.0)
   */
  java.util.Map<Integer, Float> getWeeklyCompletionRate(int trainerId);

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
  int countCompletedSessionsWithFilter(int trainerId, LocalDate from, LocalDate to, String packageName,
      String trainingType);

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
  int countCancelledSessionsWithFilter(int trainerId, LocalDate from, LocalDate to, String packageName,
      String trainingType);

  /**
   * Lấy đánh giá trung bình theo từng tháng của trainer trong năm
   * 
   * @param trainerId ID của trainer
   * @param year      Năm cần lấy
   * @return Map với key là YearMonth, value là đánh giá trung bình
   */
  Map<YearMonth, Float> getMonthlyAverageRating(int trainerId, int year);

  /**
   * Lấy số buổi tập hoàn thành theo tuần để phân tích hiệu suất
   * 
   * @param trainerId ID của trainer
   * @param weeks     Số tuần cần lấy
   * @return Map với key là số tuần, value là số buổi hoàn thành
   */
  Map<Integer, Integer> getWeeklyCompletedSessions(int trainerId, int weeks);

  /**
   * Lấy số buổi tập hoàn thành theo tháng để phân tích hiệu suất
   * 
   * @param trainerId ID của trainer
   * @param months    Số tháng cần lấy
   * @return Map với key là YearMonth, value là số buổi hoàn thành
   */
  Map<YearMonth, Integer> getMonthlyCompletedSessions(int trainerId, int months);
}
