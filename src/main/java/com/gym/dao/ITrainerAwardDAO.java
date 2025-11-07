package com.gym.dao;

import java.time.LocalDate;
import java.util.List;

import com.gym.model.TrainerAward;

/**
 * TrainerAwardDAO Interface
 * Định nghĩa các phương thức truy vấn dữ liệu cho entity TrainerAward
 */
public interface ITrainerAwardDAO {

  /**
   * Lưu danh hiệu vào database
   * 
   * @param award TrainerAward object cần lưu
   * @return TrainerAward đã được lưu (có ID)
   */
  TrainerAward save(TrainerAward award);

  /**
   * Lấy tất cả danh hiệu của một trainer
   * 
   * @param trainerId ID của trainer
   * @return Danh sách danh hiệu của trainer
   */
  List<TrainerAward> findByTrainerId(int trainerId);

  /**
   * Lấy danh hiệu của trainer trong một tháng cụ thể
   * 
   * @param trainerId ID của trainer
   * @param month    Tháng cần lấy (LocalDate với ngày đầu tháng)
   * @return Danh sách danh hiệu trong tháng
   */
  List<TrainerAward> findByTrainerIdAndMonth(int trainerId, LocalDate month);

  /**
   * Kiểm tra xem trainer đã có danh hiệu trong tháng chưa
   * 
   * @param trainerId ID của trainer
   * @param awardName Tên danh hiệu
   * @param month    Tháng cần kiểm tra
   * @return true nếu đã có, false nếu chưa có
   */
  boolean existsByTrainerIdAndAwardNameAndMonth(int trainerId, String awardName, LocalDate month);

  /**
   * Xóa danh hiệu theo ID
   * 
   * @param id ID của danh hiệu cần xóa
   */
  void deleteById(int id);

  /**
   * Xóa tất cả danh hiệu của một trainer
   * 
   * @param trainerId ID của trainer
   */
  void deleteByTrainerId(int trainerId);
}

