package DAO;

import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO - Data Access Object cho User
 * Xử lý tất cả các operations với database cho User
 * Implementation của IUserDAO interface
 */
public class UserDAO implements IUserDAO {
    /**
     * Check if a column exists in a table using DB metadata
     */
    private boolean isColumnPresent(Connection conn, String tableName, String columnName) throws SQLException {
        try (ResultSet cols = conn.getMetaData().getColumns(null, null, tableName, columnName)) {
            return cols.next();
        }
    }

    /**
     * Get role_id from roles table by role_name, returns null if not found
     */
    private Integer getRoleId(Connection conn, String roleName) {
        String sql = "SELECT role_id FROM roles WHERE role_name = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roleName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException ignored) {
        }
        return null;
    }
    
    /**
     * Tạo user mới
     */
    @Override
    public boolean createUser(User user) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return false;

            // Detect schema
            boolean hasPassword = isColumnPresent(conn, "users", "password");
            boolean hasPasswordHash = isColumnPresent(conn, "users", "password_hash");
            boolean hasRole = isColumnPresent(conn, "users", "role");
            boolean hasRoleId = isColumnPresent(conn, "users", "role_id");

            if (hasPassword) {
                // Legacy/simple schema with password + role (text)
                java.util.List<String> cols = new java.util.ArrayList<>();
                java.util.List<Object> vals = new java.util.ArrayList<>();

                // Required base columns (presence checked defensively)
                if (isColumnPresent(conn, "users", "username")) { cols.add("username"); vals.add(user.getUsername()); }
                if (isColumnPresent(conn, "users", "password")) { cols.add("password"); vals.add(user.getPassword()); }
                if (isColumnPresent(conn, "users", "email")) { cols.add("email"); vals.add(user.getEmail()); }
                if (isColumnPresent(conn, "users", "full_name")) { cols.add("full_name"); vals.add(user.getFullName()); }

                // Optional columns
                if (isColumnPresent(conn, "users", "phone")) { cols.add("phone"); vals.add(user.getPhone()); }
                if (isColumnPresent(conn, "users", "date_of_birth")) { cols.add("date_of_birth"); vals.add(user.getDateOfBirth() != null ? new java.sql.Date(user.getDateOfBirth().getTime()) : null); }
                if (isColumnPresent(conn, "users", "gender")) { cols.add("gender"); vals.add(user.getGender()); }
                if (isColumnPresent(conn, "users", "address")) { cols.add("address"); vals.add(user.getAddress()); }
                if (isColumnPresent(conn, "users", "role")) { cols.add("role"); vals.add(user.getRole() != null ? user.getRole() : "member"); }
                if (isColumnPresent(conn, "users", "status")) { cols.add("status"); vals.add(user.getStatus() != null ? user.getStatus() : "active"); }

                if (cols.isEmpty()) return false;

                StringBuilder sb = new StringBuilder();
                sb.append("INSERT INTO users (");
                for (int i = 0; i < cols.size(); i++) {
                    if (i > 0) sb.append(", ");
                    sb.append(cols.get(i));
                }
                sb.append(") VALUES (");
                for (int i = 0; i < cols.size(); i++) {
                    if (i > 0) sb.append(", ");
                    sb.append("?");
                }
                sb.append(")");

                try (PreparedStatement ps = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS)) {
                    for (int i = 0; i < vals.size(); i++) {
                        Object v = vals.get(i);
                        if (v instanceof java.sql.Date) {
                            ps.setDate(i + 1, (java.sql.Date) v);
                        } else if (v instanceof Integer) {
                            ps.setInt(i + 1, (Integer) v);
                        } else {
                            ps.setObject(i + 1, v);
                        }
                    }

                    int affectedRows = ps.executeUpdate();
                    if (affectedRows > 0) {
                        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                user.setUserId(generatedKeys.getInt(1));
                            }
                        }
                        return true;
                    }
                }
            } else if (hasPasswordHash) {
                // 3NF schema with password_hash + role_id
                String sql = "INSERT INTO users (username, password_hash, email, full_name, phone, date_of_birth, gender, role_id, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, user.getUsername());
                    ps.setString(2, user.getPassword()); // already hashed before call
                    ps.setString(3, user.getEmail());
                    ps.setString(4, user.getFullName());
                    ps.setString(5, user.getPhone());
                    ps.setDate(6, user.getDateOfBirth() != null ? new java.sql.Date(user.getDateOfBirth().getTime()) : null);
                    ps.setString(7, user.getGender());
                    Integer roleId = hasRoleId ? getRoleId(conn, user.getRole() != null ? user.getRole() : "member") : null;
                    if (roleId == null) {
                        // fallback: default to member role id =  (lookup again to be safe)
                        roleId = getRoleId(conn, "member");
                    }
                    if (roleId == null) {
                        // cannot insert without role_id
                        return false;
                    }
                    ps.setInt(8, roleId);
                    ps.setString(9, user.getStatus() != null ? user.getStatus() : "active");

                    int affectedRows = ps.executeUpdate();
                    if (affectedRows > 0) {
                        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                user.setUserId(generatedKeys.getInt(1));
                            }
                        }
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Lấy user theo ID
     */
    @Override
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy user theo username
     */
    @Override
    public User getUserByUsername(String username) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return null;
            // Prefer new schema with roles mapping if available
            boolean hasRoleId = false;
            try (ResultSet cols = conn.getMetaData().getColumns(null, null, "users", "role_id")) {
                hasRoleId = cols.next();
            }
            String sql;
            if (hasRoleId) {
                sql = "SELECT u.*, r.role_name AS role FROM users u LEFT JOIN roles r ON u.role_id = r.role_id WHERE u.username = ?";
            } else {
                sql = "SELECT * FROM users WHERE username = ?";
            }
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return extractUserFromResultSet(rs);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy user theo email
     */
    @Override
    public User getUserByEmail(String email) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return null;
            boolean hasRoleId = false;
            try (ResultSet cols = conn.getMetaData().getColumns(null, null, "users", "role_id")) {
                hasRoleId = cols.next();
            }
            String sql;
            if (hasRoleId) {
                sql = "SELECT u.*, r.role_name AS role FROM users u LEFT JOIN roles r ON u.role_id = r.role_id WHERE u.email = ?";
            } else {
                sql = "SELECT * FROM users WHERE email = ?";
            }
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, email);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return extractUserFromResultSet(rs);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy tất cả users
     */
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    /**
     * Cập nhật user
     */
    @Override
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET " +
                    "username = ?, email = ?, full_name = ?, phone = ?, " +
                    "date_of_birth = ?, gender = ?, address = ?, " +
                    "avatar_url = ?, status = ?, updated_at = ? " +
                    "WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getPhone());
            ps.setDate(5, user.getDateOfBirth() != null ? new java.sql.Date(user.getDateOfBirth().getTime()) : null);
            ps.setString(6, user.getGender());
            ps.setString(7, user.getAddress());
            ps.setString(8, user.getAvatarUrl());
            ps.setString(9, user.getStatus());
            ps.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
            ps.setInt(11, user.getUserId());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Xóa user (soft delete)
     */
    @Override
    public boolean deleteUser(int userId) {
        String sql = "UPDATE users SET status = 'inactive', updated_at = ? WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setInt(2, userId);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Kiểm tra username đã tồn tại chưa
     */
    @Override
    public boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Kiểm tra email đã tồn tại chưa
     */
    @Override
    public boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Xác thực user (login)
     */
    @Override
    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND status = 'active'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, password); // Note: Password should be hashed before passing here
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Đếm tổng số users
     */
    @Override
    public int getTotalUsersCount() {
        String sql = "SELECT COUNT(*) FROM users";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Set user status
     */
    @Override
    public boolean setUserStatus(int userId, String status) {
        String sql = "UPDATE users SET status = ?, updated_at = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Helper method: Extract User từ ResultSet
     */
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        ResultSetMetaData md = rs.getMetaData();
        int colCount = md.getColumnCount();
        java.util.Set<String> cols = new java.util.HashSet<>();
        for (int i = 1; i <= colCount; i++) {
            cols.add(md.getColumnLabel(i).toLowerCase());
        }

        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        if (cols.contains("password")) {
            user.setPassword(rs.getString("password"));
        } else if (cols.contains("password_hash")) {
            user.setPassword(rs.getString("password_hash"));
        }
        user.setEmail(rs.getString("email"));
        if (cols.contains("full_name")) user.setFullName(rs.getString("full_name"));
        if (cols.contains("phone")) user.setPhone(rs.getString("phone"));
        if (cols.contains("date_of_birth")) user.setDateOfBirth(rs.getDate("date_of_birth"));
        if (cols.contains("gender")) user.setGender(rs.getString("gender"));
        if (cols.contains("address")) user.setAddress(rs.getString("address"));
        if (cols.contains("avatar_url")) user.setAvatarUrl(rs.getString("avatar_url"));
        // Role can come from text column 'role' or aliased join 'role'
        if (cols.contains("role")) user.setRole(rs.getString("role"));
        if (cols.contains("status")) user.setStatus(rs.getString("status"));
        if (cols.contains("created_at")) user.setCreatedAt(rs.getTimestamp("created_at"));
        if (cols.contains("updated_at")) user.setUpdatedAt(rs.getTimestamp("updated_at"));
        return user;
    }
}
