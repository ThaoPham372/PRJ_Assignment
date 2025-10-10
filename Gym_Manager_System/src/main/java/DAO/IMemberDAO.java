package DAO;

import model.Member;
import java.util.List;

/**
 * Interface for Member Data Access Object
 * Định nghĩa các operations cơ bản với database cho Member
 */
public interface IMemberDAO {
    
    /**
     * Tạo member mới
     * @param member Member object cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean createMember(Member member);
    
    /**
     * Lấy member theo ID
     * @param memberId ID của member
     * @return Member object hoặc null nếu không tìm thấy
     */
    Member getMemberById(int memberId);
    
    /**
     * Lấy member theo User ID
     * @param userId ID của user
     * @return Member object hoặc null nếu không tìm thấy
     */
    Member getMemberByUserId(int userId);
    
    /**
     * Lấy member theo Member Code
     * @param memberCode Mã thành viên
     * @return Member object hoặc null nếu không tìm thấy
     */
    Member getMemberByCode(String memberCode);
    
    /**
     * Lấy tất cả members
     * @return List của tất cả members
     */
    List<Member> getAllMembers();
    
    /**
     * Lấy tất cả active members
     * @return List của active members
     */
    List<Member> getActiveMembers();
    
    /**
     * Lấy members theo Coach ID
     * @param coachId ID của coach
     * @return List của members được gán cho coach
     */
    List<Member> getMembersByCoachId(int coachId);
    
    /**
     * Cập nhật thông tin member
     * @param member Member object với thông tin mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateMember(Member member);
    
    /**
     * Xóa member (soft delete - chuyển status thành inactive)
     * @param memberId ID của member cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteMember(int memberId);
    
    /**
     * Tìm kiếm members theo keyword
     * @param keyword Từ khóa tìm kiếm (tên, email, member code)
     * @return List của members phù hợp với keyword
     */
    List<Member> searchMembers(String keyword);
    
    /**
     * Tăng số buổi tập của member
     * @param memberId ID của member
     * @return true nếu thành công, false nếu thất bại
     */
    boolean incrementWorkoutSession(int memberId);
    
    /**
     * Cập nhật streak của member
     * @param memberId ID của member
     * @param currentStreak Streak hiện tại
     * @param longestStreak Streak dài nhất
     * @return true nếu thành công, false nếu thất bại
     */
    boolean updateStreak(int memberId, int currentStreak, int longestStreak);
    
    /**
     * Đếm tổng số members
     * @return Số lượng members
     */
    int getTotalMembersCount();
    
    /**
     * Lấy số member cuối cùng để generate member code
     * @return Số cuối cùng trong member code
     */
    int getLastMemberNumber();
}

