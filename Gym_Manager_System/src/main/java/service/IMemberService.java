package service;

import model.Member;
import model.User;
import service.MemberService.ServiceResponse;
import service.MemberService.MemberWithUserInfo;
import service.MemberService.MemberStatistics;
import java.util.List;

/**
 * Interface for Member Service
 * Định nghĩa các business operations cho Member
 */
public interface IMemberService {
    
    /**
     * Đăng ký member mới (tạo cả User và Member)
     * @param user User object
     * @param member Member object
     * @return ServiceResponse chứa Member đã tạo hoặc error message
     */
    ServiceResponse<Member> registerMember(User user, Member member);
    
    /**
     * Cập nhật thông tin member và user
     * @param member Member object với thông tin mới
     * @param user User object với thông tin mới
     * @return ServiceResponse chứa Member đã cập nhật hoặc error message
     */
    ServiceResponse<Member> updateMemberProfile(Member member, User user);
    
    /**
     * Lấy thông tin đầy đủ của member (Member + User)
     * @param memberId ID của member
     * @return MemberWithUserInfo object chứa cả Member và User
     */
    MemberWithUserInfo getMemberFullInfo(int memberId);
    
    /**
     * Lấy member theo user ID
     * @param userId ID của user
     * @return Member object hoặc null
     */
    Member getMemberByUserId(int userId);
    
    /**
     * Lấy member theo ID
     * @param memberId ID của member
     * @return Member object hoặc null
     */
    Member getMemberById(int memberId);
    
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
     * Assign coach cho member
     * @param memberId ID của member
     * @param coachId ID của coach
     * @return ServiceResponse success/error
     */
    ServiceResponse<Member> assignCoach(int memberId, int coachId);
    
    /**
     * Kiểm tra membership có còn active không
     * @param memberId ID của member
     * @return true nếu membership còn hạn, false nếu không
     */
    boolean isMembershipActive(int memberId);
    
    /**
     * Lấy danh sách members theo coach
     * @param coachId ID của coach
     * @return List của members
     */
    List<Member> getMembersByCoach(int coachId);
    
    /**
     * Tìm kiếm members theo keyword
     * @param keyword Từ khóa tìm kiếm
     * @return List của members phù hợp
     */
    List<Member> searchMembers(String keyword);
    
    /**
     * Ghi nhận buổi tập (tự động update streak)
     * @param memberId ID của member
     * @return ServiceResponse success/error
     */
    ServiceResponse<Void> recordWorkoutSession(int memberId);
    
    /**
     * Lấy thống kê của member
     * @param memberId ID của member
     * @return MemberStatistics object
     */
    MemberStatistics getMemberStatistics(int memberId);
    
    /**
     * Deactivate member (soft delete)
     * @param memberId ID của member
     * @return ServiceResponse success/error
     */
    ServiceResponse<Void> deactivateMember(int memberId);
    
    /**
     * Lấy tổng số members
     * @return Số lượng members
     */
    int getTotalMembersCount();
}

