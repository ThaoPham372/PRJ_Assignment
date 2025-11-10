package service;

import dao.MembershipDAO;
import model.Membership;
import model.Package;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * MembershipService - Service xử lý business logic cho membership
 * 
 * Tuân thủ mô hình MVC và nguyên tắc Single Responsibility
 */
public class MembershipService {
    private final MembershipDAO membershipDAO;

    public MembershipService() {
        membershipDAO = new MembershipDAO();
    }

    // ==================== BASIC CRUD ====================

    public List<Membership> getAll() {
        return membershipDAO.findAll();
    }

    public Membership getMembershipById(int id) {
        return membershipDAO.findById(id);
    }

    public Membership getMembershipByUserId(int id) {
        return membershipDAO.findByUserId(id);
    }

    public int add(Membership membership) {
        return membershipDAO.save(membership);
    }

    public int update(Membership membership) {
        return membershipDAO.update(membership);
    }

    public int delete(Membership membership) {
        return membershipDAO.delete(membership);
    }

    public int deleteById(int id) {
        return membershipDAO.deleteById(id);
    }

    // ==================== ENHANCED METHODS ====================

    /**
     * Lấy tất cả memberships của một member
     * 
     * @param memberId ID của member
     * @return Danh sách memberships của member
     */
    public List<Membership> getMembershipsByMemberId(int memberId) {
        return membershipDAO.findByMemberId(memberId);
    }

    /**
     * Lấy tất cả memberships ACTIVE của một member (chưa hết hạn)
     * 
     * @param memberId ID của member
     * @return Danh sách memberships ACTIVE của member
     */
    public List<Membership> getActiveMembershipsByMemberId(int memberId) {
        return membershipDAO.findActiveByMemberId(memberId);
    }

    /**
     * Tìm membership ACTIVE của member với package cụ thể
     * 
     * @param memberId ID của member
     * @param packageId ID của package
     * @return Membership ACTIVE hoặc null nếu không tìm thấy
     */
    public Membership getActiveMembershipByMemberIdAndPackageId(int memberId, int packageId) {
        return membershipDAO.findActiveByMemberIdAndPackageId(memberId, packageId);
    }

    // ==================== VALIDATION METHODS ====================

    /**
     * Validation result class
     */
    public static class ValidationResult {
        private boolean valid;
        private List<String> errors;

        public ValidationResult(boolean valid, List<String> errors) {
            this.valid = valid;
            this.errors = errors != null ? errors : new ArrayList<>();
        }

        public boolean isValid() {
            return valid;
        }

        public List<String> getErrors() {
            return errors;
        }

        public static ValidationResult success() {
            return new ValidationResult(true, new ArrayList<>());
        }

        public static ValidationResult failure(String error) {
            List<String> errors = new ArrayList<>();
            errors.add(error);
            return new ValidationResult(false, errors);
        }

        public static ValidationResult failure(List<String> errors) {
            return new ValidationResult(false, errors);
        }
    }

    /**
     * Validate việc đặt gói mới cho member
     * 
     * @param memberId ID của member
     * @param packageToAdd Package muốn đặt
     * @return ValidationResult
     */
    public ValidationResult validateNewMembership(int memberId, Package packageToAdd) {
        List<String> errors = new ArrayList<>();

        // 1. Kiểm tra package có tồn tại và active không
        if (packageToAdd == null) {
            errors.add("Gói thành viên không tồn tại");
            return ValidationResult.failure(errors);
        }

        if (packageToAdd.getIsActive() == null || !packageToAdd.getIsActive()) {
            errors.add("Gói thành viên không còn hoạt động");
            return ValidationResult.failure(errors);
        }

        // 2. Kiểm tra member đã có gói ACTIVE chưa (có thể có nhiều gói)
        List<Membership> activeMemberships = getActiveMembershipsByMemberId(memberId);
        
        // 3. Kiểm tra xem đã có gói cùng loại chưa
        Membership existingSamePackage = getActiveMembershipByMemberIdAndPackageId(memberId, packageToAdd.getId());
        if (existingSamePackage != null) {
            // Nếu có gói cùng loại, sẽ cộng thời gian (không phải lỗi)
            // Không cần thêm error ở đây
        }

        // 4. Kiểm tra số lượng gói tối đa (nếu có quy định)
        // Có thể thêm logic kiểm tra số lượng gói tối đa ở đây
        // Ví dụ: không cho phép có quá 5 gói cùng lúc
        if (activeMemberships.size() >= 5) {
            errors.add("Bạn đã đạt số lượng gói thành viên tối đa (5 gói). Vui lòng đợi một số gói hết hạn trước khi đặt thêm.");
            return ValidationResult.failure(errors);
        }

        if (errors.isEmpty()) {
            return ValidationResult.success();
        } else {
            return ValidationResult.failure(errors);
        }
    }

    // ==================== MEMBERSHIP CREATION METHODS ====================

    /**
     * Tạo membership mới hoặc cộng thời gian vào membership hiện có (nếu cùng package)
     * 
     * @param memberId ID của member
     * @param packageToAdd Package muốn đặt
     * @return Membership đã được tạo hoặc cập nhật
     */
    public Membership createOrExtendMembership(int memberId, Package packageToAdd) {
        // 1. Validate trước
        ValidationResult validation = validateNewMembership(memberId, packageToAdd);
        if (!validation.isValid()) {
            throw new IllegalArgumentException(String.join(", ", validation.getErrors()));
        }

        // 2. Kiểm tra xem đã có gói cùng loại ACTIVE chưa
        Membership existingMembership = getActiveMembershipByMemberIdAndPackageId(memberId, packageToAdd.getId());

        if (existingMembership != null) {
            // Cộng thời gian vào gói hiện có
            return extendMembershipDuration(existingMembership, packageToAdd.getDurationMonths());
        } else {
            // Tạo membership mới
            return createNewMembership(memberId, packageToAdd);
        }
    }

    /**
     * Tạo membership mới
     * 
     * @param memberId ID của member
     * @param packageToAdd Package muốn đặt
     * @return Membership mới được tạo
     */
    private Membership createNewMembership(int memberId, Package packageToAdd) {
        Membership membership = new Membership();
        
        // Set member (cần load từ MemberService)
        service.MemberService memberService = new service.MemberService();
        model.Member member = memberService.getById(memberId);
        if (member == null) {
            throw new IllegalArgumentException("Member không tồn tại");
        }
        membership.setMember(member);
        
        // Set package
        membership.setPackageO(packageToAdd);
        
        // Set dates
        Date now = new Date();
        membership.setStartDate(now);
        
        // Tính endDate dựa trên durationMonths
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.MONTH, packageToAdd.getDurationMonths());
        membership.setEndDate(cal.getTime());
        
        // Set status
        membership.setStatus("ACTIVE");
        
        // Set created date
        membership.setCreatedDate(now);
        
        // Save
        int membershipId = add(membership);
        membership.setId(membershipId);
        
        System.out.println("[MembershipService] Created new membership ID: " + membershipId + 
                          " for member: " + memberId + ", package: " + packageToAdd.getId());
        
        return membership;
    }

    /**
     * Cộng thời gian vào membership hiện có
     * 
     * @param existingMembership Membership hiện có
     * @param additionalMonths Số tháng cộng thêm
     * @return Membership đã được cập nhật
     */
    private Membership extendMembershipDuration(Membership existingMembership, int additionalMonths) {
        Date currentEndDate = existingMembership.getEndDate();
        if (currentEndDate == null) {
            currentEndDate = new Date();
        }
        
        // Cộng thêm số tháng vào endDate
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentEndDate);
        cal.add(Calendar.MONTH, additionalMonths);
        existingMembership.setEndDate(cal.getTime());
        
        // Update updatedDate
        existingMembership.setUpdatedDate(new Date());
        
        // Update
        update(existingMembership);
        
        System.out.println("[MembershipService] Extended membership ID: " + existingMembership.getId() + 
                          " by " + additionalMonths + " months. New end date: " + existingMembership.getEndDate());
        
        return existingMembership;
    }

    // ==================== LEGACY METHOD (for backward compatibility) ====================

    /**
     * Chưa tối ưu - giữ lại để backward compatibility
     * @deprecated Sử dụng getActiveMembershipByMemberIdAndPackageId() thay thế
     */
    @Deprecated
    public Membership getByMemberIdAndPackageId(int memberId, int packageId) { 
        List<Membership> memberships = membershipDAO.findAll();
        for(Membership m : memberships) {
            if (m.getMember() != null && m.getMember().getId().equals(memberId) &&
                m.getPackageO() != null && m.getPackageO().getId().equals(packageId))
                return m;
        }
        return null;
    }
}
