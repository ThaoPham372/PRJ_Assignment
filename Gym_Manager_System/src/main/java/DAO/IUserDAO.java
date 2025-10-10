package DAO;

import model.User;
import java.util.List;

/**
 * Interface for User Data Access Object
 * Định nghĩa các operations cơ bản với database cho User
 */
public interface IUserDAO {
    
    /**
     * Tạo user mới
     * @param user User object cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean createUser(User user);
    
    /**
     * Lấy user theo ID
     * @param userId ID của user
     * @return User object hoặc null nếu không tìm thấy
     */
    User getUserById(int userId);
    
    /**
     * Lấy user theo username
     * @param username Username
     * @return User object hoặc null nếu không tìm thấy
     */
    User getUserByUsername(String username);
    
    /**
     * Lấy user theo email
     * @param email Email
     * @return User object hoặc null nếu không tìm thấy
     */
    User getUserByEmail(String email);
    
    /**
     * Lấy tất cả users
     * @return List của tất cả users
     */
    List<User> getAllUsers();
    
    /**
     * Cập nhật thông tin user
     * @param user User object với thông tin mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateUser(User user);
    
    /**
     * Xóa user (soft delete - chuyển status thành inactive)
     * @param userId ID của user cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteUser(int userId);
    
    /**
     * Kiểm tra username đã tồn tại chưa
     * @param username Username cần kiểm tra
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean isUsernameExists(String username);
    
    /**
     * Kiểm tra email đã tồn tại chưa
     * @param email Email cần kiểm tra
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean isEmailExists(String email);
    
    /**
     * Xác thực user (login)
     * @param username Username
     * @param password Password (đã hash)
     * @return User object nếu xác thực thành công, null nếu thất bại
     */
    User authenticate(String username, String password);
    
    /**
     * Đếm tổng số users
     * @return Số lượng users
     */
    int getTotalUsersCount();

    /**
     * Update user status (e.g., active/inactive)
     */
    boolean setUserStatus(int userId, String status);
}
