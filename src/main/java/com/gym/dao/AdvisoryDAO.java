package com.gym.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.gym.util.DatabaseUtil;

/**
 * AdvisoryDAO - Data Access Object cho bảng advisory_requests
 * Xử lý việc lưu các yêu cầu tư vấn vào database
 */
public class AdvisoryDAO {

  /**
   * Lưu yêu cầu tư vấn vào database
   * 
   * @param fullName Họ và tên
   * @param phone    Số điện thoại
   * @param email    Email
   * @param address  Địa chỉ
   * @return true nếu lưu thành công, false nếu có lỗi
   */
  public boolean saveAdvisoryRequest(String fullName, String phone, String email, String address) {
    String sql = "INSERT INTO advisory_requests (full_name, phone, email, address, created_at) " +
        "VALUES (?, ?, ?, ?, NOW())";

    try (Connection conn = DatabaseUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, fullName);
      stmt.setString(2, phone);
      stmt.setString(3, email);
      stmt.setString(4, address);

      int affectedRows = stmt.executeUpdate();
      return affectedRows > 0;

    } catch (SQLException e) {
      System.err.println("Error saving advisory request: " + e.getMessage());
      e.printStackTrace();
      return false;
    }
  }
}






