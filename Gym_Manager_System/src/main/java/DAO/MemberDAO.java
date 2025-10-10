package DAO;

import model.Member;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MemberDAO - Data Access Object cho Member
 * Xử lý tất cả các operations với database cho Member
 * Implementation của IMemberDAO interface
 */
public class MemberDAO implements IMemberDAO {
    private boolean isColumnPresent(Connection conn, String tableName, String columnName) throws SQLException {
        try (ResultSet cols = conn.getMetaData().getColumns(null, null, tableName, columnName)) {
            return cols.next();
        }
    }
    
    /**
     * Tạo member mới
     */
    @Override
    public boolean createMember(Member member) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return false;

            // Dynamically build insert based on existing columns
            java.util.List<String> columns = new java.util.ArrayList<>();
            java.util.List<Object> values = new java.util.ArrayList<>();

            // Required relationship
            if (isColumnPresent(conn, "members", "user_id")) {
                columns.add("user_id");
                values.add(member.getUserId());
            } else {
                // Cannot insert without user_id
                return false;
            }

            if (isColumnPresent(conn, "members", "member_code")) {
                columns.add("member_code");
                values.add(member.getMemberCode());
            }

            if (isColumnPresent(conn, "members", "registration_date")) {
                columns.add("registration_date");
                java.util.Date reg = member.getRegistrationDate() != null ? member.getRegistrationDate() : new java.util.Date();
                values.add(new java.sql.Date(reg.getTime()));
            }

            if (isColumnPresent(conn, "members", "status")) {
                columns.add("status");
                values.add(member.getStatus() != null ? member.getStatus() : "active");
            }

            // Optional physical info
            if (isColumnPresent(conn, "members", "height")) { columns.add("height"); values.add(member.getHeight()); }
            if (isColumnPresent(conn, "members", "weight")) { columns.add("weight"); values.add(member.getWeight()); }
            if (isColumnPresent(conn, "members", "bmi")) { columns.add("bmi"); values.add(member.getBmi()); }
            if (isColumnPresent(conn, "members", "blood_type")) { columns.add("blood_type"); values.add(member.getBloodType()); }
            if (isColumnPresent(conn, "members", "medical_conditions")) { columns.add("medical_conditions"); values.add(member.getMedicalConditions()); }
            if (isColumnPresent(conn, "members", "fitness_goal")) { columns.add("fitness_goal"); values.add(member.getFitnessGoal()); }
            if (isColumnPresent(conn, "members", "target_weight")) { columns.add("target_weight"); values.add(member.getTargetWeight()); }
            if (isColumnPresent(conn, "members", "activity_level")) { columns.add("activity_level"); values.add(member.getActivityLevel()); }

            // Package info
            if (isColumnPresent(conn, "members", "membership_package_id")) { columns.add("membership_package_id"); values.add(member.getMembershipPackageId()); }
            if (isColumnPresent(conn, "members", "package_start_date")) { columns.add("package_start_date"); values.add(member.getPackageStartDate() != null ? new java.sql.Date(member.getPackageStartDate().getTime()) : null); }
            if (isColumnPresent(conn, "members", "package_end_date")) { columns.add("package_end_date"); values.add(member.getPackageEndDate() != null ? new java.sql.Date(member.getPackageEndDate().getTime()) : null); }

            // Emergency
            if (isColumnPresent(conn, "members", "emergency_contact_name")) { columns.add("emergency_contact_name"); values.add(member.getEmergencyContactName()); }
            if (isColumnPresent(conn, "members", "emergency_contact_phone")) { columns.add("emergency_contact_phone"); values.add(member.getEmergencyContactPhone()); }
            if (isColumnPresent(conn, "members", "emergency_contact_relation")) { columns.add("emergency_contact_relation"); values.add(member.getEmergencyContactRelation()); }
            if (isColumnPresent(conn, "members", "emergency_contact_address")) { columns.add("emergency_contact_address"); values.add(member.getEmergencyContactAddress()); }

            // Coach
            if (isColumnPresent(conn, "members", "assigned_coach_id")) { columns.add("assigned_coach_id"); values.add(member.getAssignedCoachId()); }
            if (isColumnPresent(conn, "members", "preferred_training_time")) { columns.add("preferred_training_time"); values.add(member.getPreferredTrainingTime()); }
            if (isColumnPresent(conn, "members", "training_frequency")) { columns.add("training_frequency"); values.add(member.getTrainingFrequency()); }

            // Stats
            if (isColumnPresent(conn, "members", "total_workout_sessions")) { columns.add("total_workout_sessions"); values.add(member.getTotalWorkoutSessions() != null ? member.getTotalWorkoutSessions() : 0); }
            if (isColumnPresent(conn, "members", "current_streak")) { columns.add("current_streak"); values.add(member.getCurrentStreak() != null ? member.getCurrentStreak() : 0); }
            if (isColumnPresent(conn, "members", "longest_streak")) { columns.add("longest_streak"); values.add(member.getLongestStreak() != null ? member.getLongestStreak() : 0); }
            if (isColumnPresent(conn, "members", "total_calories_burned")) { columns.add("total_calories_burned"); values.add(member.getTotalCaloriesBurned()); }
            if (isColumnPresent(conn, "members", "last_workout_date")) { columns.add("last_workout_date"); values.add(member.getLastWorkoutDate() != null ? new java.sql.Date(member.getLastWorkoutDate().getTime()) : null); }

            // Notes and audit
            if (isColumnPresent(conn, "members", "notes")) { columns.add("notes"); values.add(member.getNotes()); }
            if (isColumnPresent(conn, "members", "created_at")) { columns.add("created_at"); values.add(new Timestamp(System.currentTimeMillis())); }

            if (columns.isEmpty()) return false;

            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO members (");
            for (int i = 0; i < columns.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(columns.get(i));
            }
            sb.append(") VALUES (");
            for (int i = 0; i < columns.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append("?");
            }
            sb.append(")");

            try (PreparedStatement ps = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < values.size(); i++) {
                    Object v = values.get(i);
                    if (v instanceof java.sql.Date) {
                        ps.setDate(i + 1, (java.sql.Date) v);
                    } else if (v instanceof Timestamp) {
                        ps.setTimestamp(i + 1, (Timestamp) v);
                    } else if (v instanceof Integer) {
                        ps.setInt(i + 1, (Integer) v);
                    } else if (v instanceof Double) {
                        ps.setObject(i + 1, v);
                    } else if (v instanceof java.math.BigDecimal) {
                        ps.setBigDecimal(i + 1, (java.math.BigDecimal) v);
                    } else {
                        ps.setObject(i + 1, v);
                    }
                }

                int affected = ps.executeUpdate();
                if (affected > 0) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            member.setMemberId(rs.getInt(1));
                        }
                    }
                    return true;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error creating member: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Lấy member theo ID
     */
    @Override
    public Member getMemberById(int memberId) {
        String sql = "SELECT * FROM members WHERE member_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractMemberFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting member by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy member theo User ID
     */
    @Override
    public Member getMemberByUserId(int userId) {
        String sql = "SELECT * FROM members WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractMemberFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting member by user ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy member theo Member Code
     */
    @Override
    public Member getMemberByCode(String memberCode) {
        String sql = "SELECT * FROM members WHERE member_code = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, memberCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractMemberFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting member by code: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy tất cả members
     */
    @Override
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members ORDER BY registration_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                members.add(extractMemberFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all members: " + e.getMessage());
            e.printStackTrace();
        }
        return members;
    }
    
    /**
     * Lấy active members
     */
    @Override
    public List<Member> getActiveMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE status = 'active' ORDER BY registration_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                members.add(extractMemberFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting active members: " + e.getMessage());
            e.printStackTrace();
        }
        return members;
    }
    
    /**
     * Lấy members theo coach
     */
    @Override
    public List<Member> getMembersByCoachId(int coachId) {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE assigned_coach_id = ? AND status = 'active'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, coachId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    members.add(extractMemberFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting members by coach: " + e.getMessage());
            e.printStackTrace();
        }
        return members;
    }
    
    /**
     * Cập nhật member
     */
    @Override
    public boolean updateMember(Member member) {
        String sql = "UPDATE members SET " +
                    "height = ?, weight = ?, bmi = ?, blood_type = ?, medical_conditions = ?, " +
                    "fitness_goal = ?, target_weight = ?, activity_level = ?, " +
                    "membership_package_id = ?, package_start_date = ?, package_end_date = ?, " +
                    "emergency_contact_name = ?, emergency_contact_phone = ?, " +
                    "emergency_contact_relation = ?, emergency_contact_address = ?, " +
                    "assigned_coach_id = ?, preferred_training_time = ?, training_frequency = ?, " +
                    "total_workout_sessions = ?, current_streak = ?, longest_streak = ?, " +
                    "total_calories_burned = ?, last_workout_date = ?, " +
                    "notes = ?, status = ?, updated_at = ?, updated_by = ? " +
                    "WHERE member_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            int index = 1;
            ps.setObject(index++, member.getHeight());
            ps.setObject(index++, member.getWeight());
            ps.setObject(index++, member.getBmi());
            ps.setString(index++, member.getBloodType());
            ps.setString(index++, member.getMedicalConditions());
            ps.setString(index++, member.getFitnessGoal());
            ps.setObject(index++, member.getTargetWeight());
            ps.setString(index++, member.getActivityLevel());
            ps.setObject(index++, member.getMembershipPackageId());
            ps.setDate(index++, member.getPackageStartDate() != null ? 
                new java.sql.Date(member.getPackageStartDate().getTime()) : null);
            ps.setDate(index++, member.getPackageEndDate() != null ? 
                new java.sql.Date(member.getPackageEndDate().getTime()) : null);
            ps.setString(index++, member.getEmergencyContactName());
            ps.setString(index++, member.getEmergencyContactPhone());
            ps.setString(index++, member.getEmergencyContactRelation());
            ps.setString(index++, member.getEmergencyContactAddress());
            ps.setObject(index++, member.getAssignedCoachId());
            ps.setString(index++, member.getPreferredTrainingTime());
            ps.setString(index++, member.getTrainingFrequency());
            ps.setInt(index++, member.getTotalWorkoutSessions() != null ? member.getTotalWorkoutSessions() : 0);
            ps.setInt(index++, member.getCurrentStreak() != null ? member.getCurrentStreak() : 0);
            ps.setInt(index++, member.getLongestStreak() != null ? member.getLongestStreak() : 0);
            ps.setBigDecimal(index++, member.getTotalCaloriesBurned());
            ps.setDate(index++, member.getLastWorkoutDate() != null ? 
                new java.sql.Date(member.getLastWorkoutDate().getTime()) : null);
            ps.setString(index++, member.getNotes());
            ps.setString(index++, member.getStatus());
            ps.setTimestamp(index++, new Timestamp(System.currentTimeMillis()));
            ps.setString(index++, member.getUpdatedBy());
            ps.setInt(index++, member.getMemberId());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating member: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Xóa member (soft delete)
     */
    @Override
    public boolean deleteMember(int memberId) {
        String sql = "UPDATE members SET status = 'inactive', updated_at = ? WHERE member_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setInt(2, memberId);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting member: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Tìm kiếm members
     */
    @Override
    public List<Member> searchMembers(String keyword) {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT m.* FROM members m " +
                    "JOIN users u ON m.user_id = u.user_id " +
                    "WHERE u.full_name LIKE ? OR u.email LIKE ? OR m.member_code LIKE ? " +
                    "ORDER BY m.registration_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    members.add(extractMemberFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching members: " + e.getMessage());
            e.printStackTrace();
        }
        return members;
    }
    
    /**
     * Increment workout session count
     */
    @Override
    public boolean incrementWorkoutSession(int memberId) {
        String sql = "UPDATE members SET " +
                    "total_workout_sessions = total_workout_sessions + 1, " +
                    "last_workout_date = ?, " +
                    "updated_at = ? " +
                    "WHERE member_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
            ps.setDate(1, today);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.setInt(3, memberId);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error incrementing workout session: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Update streak
     */
    @Override
    public boolean updateStreak(int memberId, int currentStreak, int longestStreak) {
        String sql = "UPDATE members SET current_streak = ?, longest_streak = ?, updated_at = ? WHERE member_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, currentStreak);
            ps.setInt(2, longestStreak);
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            ps.setInt(4, memberId);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating streak: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Đếm tổng số members
     */
    @Override
    public int getTotalMembersCount() {
        String sql = "SELECT COUNT(*) FROM members";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error counting members: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Lấy số member cuối cùng để generate code
     */
    @Override
    public int getLastMemberNumber() {
        String sql = "SELECT TOP 1 member_code FROM members " +
                    "WHERE member_code LIKE 'GYM' + CAST(YEAR(GETDATE()) AS VARCHAR) + '%' " +
                    "ORDER BY member_id DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                String code = rs.getString("member_code");
                // Extract last 3 digits from code like "GYM2024001"
                String numberPart = code.substring(7); // Skip "GYM2024"
                return Integer.parseInt(numberPart);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting last member number: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Helper method: Extract Member từ ResultSet
     */
    private Member extractMemberFromResultSet(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setMemberId(rs.getInt("member_id"));
        member.setUserId(rs.getInt("user_id"));
        member.setMemberCode(rs.getString("member_code"));
        
        Date regDate = rs.getDate("registration_date");
        if (regDate != null) member.setRegistrationDate(new java.util.Date(regDate.getTime()));
        
        member.setHeight(rs.getObject("height") != null ? rs.getDouble("height") : null);
        member.setWeight(rs.getObject("weight") != null ? rs.getDouble("weight") : null);
        member.setBmi(rs.getObject("bmi") != null ? rs.getDouble("bmi") : null);
        member.setBloodType(rs.getString("blood_type"));
        member.setMedicalConditions(rs.getString("medical_conditions"));
        member.setFitnessGoal(rs.getString("fitness_goal"));
        member.setTargetWeight(rs.getObject("target_weight") != null ? rs.getDouble("target_weight") : null);
        member.setActivityLevel(rs.getString("activity_level"));
        
        member.setMembershipPackageId(rs.getObject("membership_package_id") != null ? 
            rs.getInt("membership_package_id") : null);
        
        Date pkgStart = rs.getDate("package_start_date");
        if (pkgStart != null) member.setPackageStartDate(new java.util.Date(pkgStart.getTime()));
        
        Date pkgEnd = rs.getDate("package_end_date");
        if (pkgEnd != null) member.setPackageEndDate(new java.util.Date(pkgEnd.getTime()));
        
        member.setEmergencyContactName(rs.getString("emergency_contact_name"));
        member.setEmergencyContactPhone(rs.getString("emergency_contact_phone"));
        member.setEmergencyContactRelation(rs.getString("emergency_contact_relation"));
        member.setEmergencyContactAddress(rs.getString("emergency_contact_address"));
        
        member.setAssignedCoachId(rs.getObject("assigned_coach_id") != null ? 
            rs.getInt("assigned_coach_id") : null);
        member.setPreferredTrainingTime(rs.getString("preferred_training_time"));
        member.setTrainingFrequency(rs.getString("training_frequency"));
        
        member.setTotalWorkoutSessions(rs.getInt("total_workout_sessions"));
        member.setCurrentStreak(rs.getInt("current_streak"));
        member.setLongestStreak(rs.getInt("longest_streak"));
        member.setTotalCaloriesBurned(rs.getBigDecimal("total_calories_burned"));
        
        Date lastWorkout = rs.getDate("last_workout_date");
        if (lastWorkout != null) member.setLastWorkoutDate(new java.util.Date(lastWorkout.getTime()));
        
        member.setNotes(rs.getString("notes"));
        member.setStatus(rs.getString("status"));
        
        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) member.setCreatedAt(new java.util.Date(created.getTime()));
        
        Timestamp updated = rs.getTimestamp("updated_at");
        if (updated != null) member.setUpdatedAt(new java.util.Date(updated.getTime()));
        
        member.setCreatedBy(rs.getString("created_by"));
        member.setUpdatedBy(rs.getString("updated_by"));
        
        return member;
    }
}

