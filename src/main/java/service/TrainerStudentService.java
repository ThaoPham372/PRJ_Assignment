package service;

import java.util.List;

import dao.TrainerStudentDAO;

/**
 * TrainerStudentService
 * - Bao bọc DAO, cung cấp API rõ ràng cho Controller
 * - Không chứa logic truy cập DB trực tiếp
 */
public class TrainerStudentService {

  private final TrainerStudentDAO dao;

  public TrainerStudentService() {
    this.dao = new TrainerStudentDAO();
  }

  /** Lấy danh sách học viên của PT (booking CONFIRMED/COMPLETED) */
  public List<Object[]> getTrainerStudents(int trainerId) {
    return dao.getStudentsByTrainer(trainerId);
  }

  /** Tìm kiếm + lọc theo gói tập */
  public List<Object[]> getTrainerStudentsWithFilter(int trainerId, String keyword, String packageName) {
    return dao.getStudentsByTrainerWithFilter(trainerId, keyword, packageName);
  }

  /** Thống kê: [0]=totalStudents, [1]=activeStudents, [2]=achievedGoalCount */
  public Object[] getStudentStatistics(int trainerId) {
    return dao.getStudentStatistics(trainerId);
  }

  /** Chi tiết một học viên của PT */
  public Object[] getStudentDetail(int memberId, int trainerId) {
    return dao.getStudentDetail(memberId, trainerId);
  }
}

