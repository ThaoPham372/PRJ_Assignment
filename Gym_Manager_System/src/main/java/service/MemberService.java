package service;

import DAO.MemberDAO;
import DAO.UserDAO;
import model.Member;
import model.User;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * MemberService - Business Logic Layer cho Member
 * Xử lý tất cả business logic và validation
 * Implementation của IMemberService interface
 */
public class MemberService implements IMemberService {
    private MemberDAO memberDAO;
    private UserDAO userDAO;
    
    public MemberService() {
        this.memberDAO = new MemberDAO();
        this.userDAO = new UserDAO();
    }
    
    /**
     * Đăng ký member mới (kèm tạo User account)
     */
    @Override
    public ServiceResponse<Member> registerMember(User user, Member member) {
        try {
            // 1. Validate input
            ValidationResult validation = validateMemberRegistration(user, member);
            if (!validation.isValid()) {
                return ServiceResponse.error(validation.getMessage());
            }
            
            // 2. Tạo User account trước
            if (userDAO.createUser(user)) {
                // 3. Lấy user_id vừa tạo
                User createdUser = userDAO.getUserByUsername(user.getUsername());
                
                if (createdUser != null) {
                    // 4. Set user_id cho member
                    member.setUserId(createdUser.getId());
                    
                    // 5. Generate member code
                    member.setMemberCode(generateMemberCode());
                    
                    // 6. Tính BMI nếu có height và weight
                    if (member.getHeight() != null && member.getWeight() != null) {
                        calculateAndSetBMI(member);
                    }
                    
                    // 7. Set default values
                    member.setRegistrationDate(new Date());
                    member.setStatus("active");
                    member.setTotalWorkoutSessions(0);
                    member.setCurrentStreak(0);
                    member.setLongestStreak(0);
                    member.setTotalCaloriesBurned(BigDecimal.ZERO);
                    
                    // 8. Tạo member
                    if (memberDAO.createMember(member)) {
                        return ServiceResponse.success(member, "Member registered successfully");
                    }
                }
            }
        } catch (Exception e) {
            return ServiceResponse.error("Registration failed: " + e.getMessage());
        }
        return ServiceResponse.error("Failed to register member");
    }
    
    /**
     * Cập nhật thông tin member
     */
    @Override
    public ServiceResponse<Member> updateMemberProfile(Member member, User user) {
        try {
            // 1. Validate
            if (member == null || user == null) {
                return ServiceResponse.error("Member and User information are required");
            }
            
            // 2. Tính lại BMI nếu có thay đổi height/weight
            if (member.getHeight() != null && member.getWeight() != null) {
                calculateAndSetBMI(member);
            }
            
            // 3. Cập nhật User info
            boolean userUpdated = userDAO.updateUser(user);
            
            // 4. Cập nhật Member info
            boolean memberUpdated = memberDAO.updateMember(member);
            
            if (userUpdated && memberUpdated) {
                return ServiceResponse.success(member, "Profile updated successfully");
            }
            
        } catch (Exception e) {
            return ServiceResponse.error("Update failed: " + e.getMessage());
        }
        return ServiceResponse.error("Failed to update profile");
    }
    
    /**
     * Lấy thông tin đầy đủ của member (bao gồm User info)
     */
    @Override
    public MemberWithUserInfo getMemberFullInfo(int memberId) {
        Member member = memberDAO.getMemberById(memberId);
        if (member != null) {
            User user = userDAO.getUserById(member.getUserId());
            return new MemberWithUserInfo(member, user);
        }
        return null;
    }
    
    /**
     * Lấy member by user ID
     */
    @Override
    public Member getMemberByUserId(int userId) {
        return memberDAO.getMemberByUserId(userId);
    }
    
    /**
     * Lấy member by ID
     */
    @Override
    public Member getMemberById(int memberId) {
        return memberDAO.getMemberById(memberId);
    }
    
    /**
     * Lấy tất cả members
     */
    @Override
    public List<Member> getAllMembers() {
        return memberDAO.getAllMembers();
    }
    
    /**
     * Lấy active members
     */
    @Override
    public List<Member> getActiveMembers() {
        return memberDAO.getActiveMembers();
    }
    
    /**
     * Assign coach cho member
     */
    @Override
    public ServiceResponse<Member> assignCoach(int memberId, int coachId) {
        Member member = memberDAO.getMemberById(memberId);
        if (member != null) {
            member.setAssignedCoachId(coachId);
            if (memberDAO.updateMember(member)) {
                return ServiceResponse.success(member, "Coach assigned successfully");
            }
        }
        return ServiceResponse.error("Failed to assign coach");
    }
    
    /**
     * Kiểm tra gói membership còn hạn không
     */
    @Override
    public boolean isMembershipActive(int memberId) {
        Member member = memberDAO.getMemberById(memberId);
        return member != null && member.isPackageActive();
    }
    
    /**
     * Lấy danh sách members theo coach
     */
    @Override
    public List<Member> getMembersByCoach(int coachId) {
        return memberDAO.getMembersByCoachId(coachId);
    }
    
    /**
     * Search members
     */
    @Override
    public List<Member> searchMembers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllMembers();
        }
        return memberDAO.searchMembers(keyword);
    }
    
    /**
     * Ghi nhận buổi tập
     */
    @Override
    public ServiceResponse<Void> recordWorkoutSession(int memberId) {
        try {
            // 1. Increment session count
            if (memberDAO.incrementWorkoutSession(memberId)) {
                
                // 2. Update streak
                Member member = memberDAO.getMemberById(memberId);
                if (member != null) {
                    updateStreak(member);
                }
                
                return ServiceResponse.success(null, "Workout session recorded");
            }
        } catch (Exception e) {
            return ServiceResponse.error("Failed to record session: " + e.getMessage());
        }
        return ServiceResponse.error("Failed to record workout session");
    }
    
    /**
     * Get member statistics
     */
    @Override
    public MemberStatistics getMemberStatistics(int memberId) {
        Member member = memberDAO.getMemberById(memberId);
        if (member != null) {
            MemberStatistics stats = new MemberStatistics();
            stats.setTotalSessions(member.getTotalWorkoutSessions());
            stats.setCurrentStreak(member.getCurrentStreak());
            stats.setLongestStreak(member.getLongestStreak());
            stats.setBmi(member.getBmi());
            stats.setBmiCategory(member.getBMICategory());
            stats.setTotalCaloriesBurned(member.getTotalCaloriesBurned());
            stats.setLastWorkoutDate(member.getLastWorkoutDate());
            stats.setMembershipActive(member.isPackageActive());
            
            // Calculate more statistics
            if (member.getPackageEndDate() != null) {
                long diff = member.getPackageEndDate().getTime() - System.currentTimeMillis();
                int daysRemaining = (int) (diff / (1000 * 60 * 60 * 24));
                stats.setDaysRemainingInPackage(daysRemaining > 0 ? daysRemaining : 0);
            }
            
            return stats;
        }
        return null;
    }
    
    /**
     * Deactivate member
     */
    @Override
    public ServiceResponse<Void> deactivateMember(int memberId) {
        if (memberDAO.deleteMember(memberId)) {
            return ServiceResponse.success(null, "Member deactivated successfully");
        }
        return ServiceResponse.error("Failed to deactivate member");
    }
    
    /**
     * Get total members count
     */
    @Override
    public int getTotalMembersCount() {
        return memberDAO.getTotalMembersCount();
    }
    
    // ==================== PRIVATE HELPER METHODS ====================
    
    /**
     * Generate unique member code
     */
    private String generateMemberCode() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int lastNumber = memberDAO.getLastMemberNumber();
        return String.format("GYM%d%03d", year, lastNumber + 1);
    }
    
    /**
     * Calculate and set BMI
     */
    private void calculateAndSetBMI(Member member) {
        if (member.getHeight() != null && member.getWeight() != null && member.getHeight() > 0) {
            double heightInMeters = member.getHeight() / 100.0;
            double bmi = member.getWeight() / (heightInMeters * heightInMeters);
            member.setBmi(Math.round(bmi * 100.0) / 100.0);
        }
    }
    
    /**
     * Update streak logic
     */
    private void updateStreak(Member member) {
        Date lastWorkout = member.getLastWorkoutDate();
        Date today = new Date();
        
        if (lastWorkout != null) {
            // Calculate days difference
            long diffInMillis = today.getTime() - lastWorkout.getTime();
            int daysDiff = (int) (diffInMillis / (1000 * 60 * 60 * 24));
            
            if (daysDiff <= 1) {
                // Continue streak
                int newStreak = member.getCurrentStreak() + 1;
                member.setCurrentStreak(newStreak);
                
                if (newStreak > member.getLongestStreak()) {
                    member.setLongestStreak(newStreak);
                }
            } else {
                // Reset streak
                member.setCurrentStreak(1);
            }
        } else {
            // First workout
            member.setCurrentStreak(1);
            member.setLongestStreak(1);
        }
        
        memberDAO.updateStreak(member.getMemberId(), 
                               member.getCurrentStreak(), 
                               member.getLongestStreak());
    }
    
    /**
     * Validate member registration
     */
    private ValidationResult validateMemberRegistration(User user, Member member) {
        // Validate User
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return ValidationResult.invalid("Username is required");
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            return ValidationResult.invalid("Password must be at least 6 characters");
        }
        if (user.getEmail() == null || !isValidEmail(user.getEmail())) {
            return ValidationResult.invalid("Valid email is required");
        }
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            return ValidationResult.invalid("Full name is required");
        }
        
        // Validate Member
        if (member.getEmergencyContactName() == null || member.getEmergencyContactName().trim().isEmpty()) {
            return ValidationResult.invalid("Emergency contact name is required");
        }
        if (member.getEmergencyContactPhone() == null || member.getEmergencyContactPhone().trim().isEmpty()) {
            return ValidationResult.invalid("Emergency contact phone is required");
        }
        
        return ValidationResult.valid();
    }
    
    /**
     * Simple email validation
     */
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    // ==================== INNER CLASSES ====================
    
    /**
     * Response wrapper
     */
    public static class ServiceResponse<T> {
        private boolean success;
        private String message;
        private T data;
        
        private ServiceResponse(boolean success, String message, T data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }
        
        public static <T> ServiceResponse<T> success(T data, String message) {
            return new ServiceResponse<>(true, message, data);
        }
        
        public static <T> ServiceResponse<T> error(String message) {
            return new ServiceResponse<>(false, message, null);
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public T getData() { return data; }
    }
    
    /**
     * Validation result
     */
    private static class ValidationResult {
        private boolean valid;
        private String message;
        
        private ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
        
        public static ValidationResult valid() {
            return new ValidationResult(true, null);
        }
        
        public static ValidationResult invalid(String message) {
            return new ValidationResult(false, message);
        }
        
        public boolean isValid() { return valid; }
        public String getMessage() { return message; }
    }
    
    /**
     * Member with User info
     */
    public static class MemberWithUserInfo {
        private Member member;
        private User user;
        
        public MemberWithUserInfo(Member member, User user) {
            this.member = member;
            this.user = user;
        }
        
        public Member getMember() { return member; }
        public User getUser() { return user; }
    }
    
    /**
     * Member statistics
     */
    public static class MemberStatistics {
        private Integer totalSessions;
        private Integer currentStreak;
        private Integer longestStreak;
        private Double bmi;
        private String bmiCategory;
        private BigDecimal totalCaloriesBurned;
        private Date lastWorkoutDate;
        private Boolean membershipActive;
        private Integer daysRemainingInPackage;
        
        // Getters and Setters
        public Integer getTotalSessions() { return totalSessions; }
        public void setTotalSessions(Integer totalSessions) { this.totalSessions = totalSessions; }
        
        public Integer getCurrentStreak() { return currentStreak; }
        public void setCurrentStreak(Integer currentStreak) { this.currentStreak = currentStreak; }
        
        public Integer getLongestStreak() { return longestStreak; }
        public void setLongestStreak(Integer longestStreak) { this.longestStreak = longestStreak; }
        
        public Double getBmi() { return bmi; }
        public void setBmi(Double bmi) { this.bmi = bmi; }
        
        public String getBmiCategory() { return bmiCategory; }
        public void setBmiCategory(String bmiCategory) { this.bmiCategory = bmiCategory; }
        
        public BigDecimal getTotalCaloriesBurned() { return totalCaloriesBurned; }
        public void setTotalCaloriesBurned(BigDecimal totalCaloriesBurned) { 
            this.totalCaloriesBurned = totalCaloriesBurned; 
        }
        
        public Date getLastWorkoutDate() { return lastWorkoutDate; }
        public void setLastWorkoutDate(Date lastWorkoutDate) { this.lastWorkoutDate = lastWorkoutDate; }
        
        public Boolean getMembershipActive() { return membershipActive; }
        public void setMembershipActive(Boolean membershipActive) { 
            this.membershipActive = membershipActive; 
        }
        
        public Integer getDaysRemainingInPackage() { return daysRemainingInPackage; }
        public void setDaysRemainingInPackage(Integer daysRemainingInPackage) { 
            this.daysRemainingInPackage = daysRemainingInPackage; 
        }
    }
}

