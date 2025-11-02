package com.gym.scheduleDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gym.model.Schedule;
import com.gym.util.DatabaseUtil;

public class ScheduleDao implements IScheduleDao {
  private Connection getConnection() throws Exception {
    return DatabaseUtil.getConnection();
  }

  @Override
  public void insert(Schedule schedule) throws Exception {
    String sql = "INSERT INTO schedules (user_id, training_date, start_time, end_time, training_type, location, note, status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, schedule.getUserId());
      ps.setDate(2, schedule.getTrainingDate());
      ps.setTime(3, schedule.getStartTime());
      ps.setTime(4, schedule.getEndTime());
      ps.setString(5, schedule.getTrainingType());
      ps.setString(6, schedule.getLocation());
      ps.setString(7, schedule.getNote());
      ps.setString(8, schedule.getStatus());
      ps.executeUpdate();
    }
  }

  @Override
  public void update(Schedule schedule) throws Exception {
    String sql = "UPDATE schedules SET user_id=?, training_date=?, start_time=?, end_time=?, training_type=?, location=?, note=?, status=?, updated_at=CURRENT_TIMESTAMP WHERE id=?";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, schedule.getUserId());
      ps.setDate(2, schedule.getTrainingDate());
      ps.setTime(3, schedule.getStartTime());
      ps.setTime(4, schedule.getEndTime());
      ps.setString(5, schedule.getTrainingType());
      ps.setString(6, schedule.getLocation());
      ps.setString(7, schedule.getNote());
      ps.setString(8, schedule.getStatus());
      ps.setInt(9, schedule.getId());
      ps.executeUpdate();
    }
  }

  @Override
  public void delete(int id) throws Exception {
    String sql = "DELETE FROM schedules WHERE id=?";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, id);
      ps.executeUpdate();
    }
  }

  @Override
  public Schedule getById(int id) throws Exception {
    String sql = "SELECT * FROM schedules WHERE id=?";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next())
          return mapRow(rs);
      }
    }
    return null;
  }

  @Override
  public List<Schedule> getAll() throws Exception {
    List<Schedule> list = new ArrayList<>();
    String sql = "SELECT * FROM schedules ORDER BY training_date, start_time";
    try (Connection conn = getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql)) {
      while (rs.next()) {
        list.add(mapRow(rs));
      }
    }
    return list;
  }

  @Override
  public List<Schedule> getByTrainerId(int trainerId) throws Exception {
    // trainer_id đã bị xóa khỏi database
    // Method này giữ lại để tương thích interface nhưng trả về tất cả schedules
    // Hoặc có thể throw UnsupportedOperationException nếu không cần dùng nữa
    // return getAll(); // Nếu muốn trả về tất cả
    throw new UnsupportedOperationException(
        "getByTrainerId không còn được hỗ trợ vì trainer_id đã bị xóa khỏi database");
  }

  @Override
  public void updateStatus(int id, String status) throws Exception {
    String sql = "UPDATE schedules SET status=?, updated_at=CURRENT_TIMESTAMP WHERE id=?";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, status);
      ps.setInt(2, id);
      ps.executeUpdate();
    }
  }

  private Schedule mapRow(ResultSet rs) throws Exception {
    Schedule s = new Schedule();
    s.setId(rs.getInt("id"));
    s.setUserId(rs.getInt("user_id"));
    // trainer_id đã bị xóa khỏi database, không đọc nữa
    // s.setTrainerId(rs.getInt("trainer_id"));
    s.setTrainingDate(rs.getDate("training_date"));
    s.setStartTime(rs.getTime("start_time"));
    s.setEndTime(rs.getTime("end_time"));
    s.setTrainingType(rs.getString("training_type"));
    s.setLocation(rs.getString("location"));
    s.setNote(rs.getString("note"));
    s.setStatus(rs.getString("status"));
    s.setCreatedAt(rs.getTimestamp("created_at"));
    s.setUpdatedAt(rs.getTimestamp("updated_at"));
    return s;
  }
}
