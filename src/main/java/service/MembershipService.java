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
 
 Tuân thủ mô hình MVC và nguyên tắc Single Responsibility
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
     * Logic mới:
     * - Chỉ cho phép mua package nếu: chưa có membership HOẶC đã có membership nhưng cùng package ID (gia hạn)
     * - Nếu đã có membership với package khác → KHÔNG cho phép mua package mới
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

        // 2. Kiểm tra member đã có gói ACTIVE chưa
        List<Membership> activeMemberships = getActiveMembershipsByMemberId(memberId);
        
        // 3. Nếu đã có active membership
        if (activeMemberships != null && !activeMemberships.isEmpty()) {
            // 3.1. Kiểm tra xem đã có gói cùng package ID chưa
            Membership existingSamePackage = getActiveMembershipByMemberIdAndPackageId(memberId, packageToAdd.getId());
            
            if (existingSamePackage != null) {
                // Có gói cùng loại → Cho phép gia hạn (không phải lỗi)
                // Không cần thêm error, cho phép tiếp tục
            } else {
                // 3.2. Đã có membership nhưng package ID KHÁC → KHÔNG cho phép mua package mới
                // Lấy package name của membership hiện có để hiển thị trong thông báo
                String currentPackageName = "gói tập hiện tại";
                if (activeMemberships.get(0).getPackageO() != null) {
                    currentPackageName = activeMemberships.get(0).getPackageO().getName();
                }
                
                String errorMessage = String.format(
                    "Bạn đã có gói tập trước đó (%s). Không thể mua thêm gói tập khác. " +
                    "Hãy liên hệ với admin nếu muốn đổi gói hoặc nâng hạng gói tập.",
                    currentPackageName
                );
                errors.add(errorMessage);
                return ValidationResult.failure(errors);
            }
        }

        // 4. Nếu chưa có membership hoặc có cùng package ID → Cho phép
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
     * Logic:
     * - Nếu đã có gói cùng package ID và còn hạn (ACTIVE + endDate > now) → Extend thời gian
     * - Nếu đã có gói cùng package ID nhưng đã hết hạn → Extend từ ngày hiện tại (renew)
     * - Nếu chưa có gói cùng package ID → Tạo mới
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

        // 2. Kiểm tra xem đã có gói cùng loại ACTIVE chưa (còn hạn)
        Membership existingActiveMembership = getActiveMembershipByMemberIdAndPackageId(memberId, packageToAdd.getId());

        if (existingActiveMembership != null) {
            // Có gói ACTIVE còn hạn → Cộng thời gian vào endDate hiện tại
            System.out.println("[MembershipService] Found active membership ID: " + existingActiveMembership.getId() + 
                             " for package " + packageToAdd.getId() + ". Extending duration.");
            return extendMembershipDuration(existingActiveMembership, packageToAdd.getDurationMonths());
        }
        
        // 3. Kiểm tra xem có gói cùng loại nhưng đã hết hạn không
        Membership existingExpiredMembership = getExpiredMembershipByMemberIdAndPackageId(memberId, packageToAdd.getId());
        
        if (existingExpiredMembership != null) {
            // Có gói cùng loại nhưng đã hết hạn → Renew từ ngày hiện tại
            System.out.println("[MembershipService] Found expired membership ID: " + existingExpiredMembership.getId() + 
                             " for package " + packageToAdd.getId() + ". Renewing from today.");
            return renewExpiredMembership(existingExpiredMembership, packageToAdd);
        }
        
        // 4. Chưa có gói cùng loại → Tạo mới
        System.out.println("[MembershipService] No existing membership found for package " + packageToAdd.getId() + ". Creating new membership.");
        return createNewMembership(memberId, packageToAdd);
    }

    /**
     * Tạo membership mới LUÔN (không kiểm tra extend)
     * Sử dụng khi thanh toán thành công - luôn tạo bảng membership mới
     * 
     * @param memberId ID của member
     * @param packageToAdd Package muốn đặt
     * @return Membership mới được tạo
     */
    public Membership createNewMembershipAlways(int memberId, Package packageToAdd) {
        // Validate cơ bản: package phải tồn tại và active
        if (packageToAdd == null) {
            throw new IllegalArgumentException("Gói thành viên không tồn tại");
        }
        
        if (packageToAdd.getIsActive() == null || !packageToAdd.getIsActive()) {
            throw new IllegalArgumentException("Gói thành viên không còn hoạt động");
        }
        
        // Luôn tạo mới, không kiểm tra extend
        return createNewMembership(memberId, packageToAdd);
    }
    
    /**
     * Tạo membership mới (private method - được gọi bởi createNewMembershipAlways hoặc createOrExtendMembership)
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
     * Cộng thời gian vào membership hiện có (còn hạn)
     * Tính từ endDate hiện tại để cộng thêm thời gian
     * 
     * Ví dụ: Gói còn 7 ngày, mua thêm 30 ngày → Tổng cộng 37 ngày từ hôm nay
     * 
     * @param existingMembership Membership hiện có (còn hạn)
     * @param additionalMonths Số tháng cộng thêm
     * @return Membership đã được cập nhật
     */
    private Membership extendMembershipDuration(Membership existingMembership, int additionalMonths) {
        Date currentEndDate = existingMembership.getEndDate();
        if (currentEndDate == null) {
            // Nếu không có endDate, tính từ hôm nay
            currentEndDate = new Date();
        }
        
        // Cộng thêm số tháng vào endDate hiện tại
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentEndDate);
        cal.add(Calendar.MONTH, additionalMonths);
        Date newEndDate = cal.getTime();
        existingMembership.setEndDate(newEndDate);
        
        // Đảm bảo status là ACTIVE
        existingMembership.setStatus("ACTIVE");
        
        // Update updatedDate
        existingMembership.setUpdatedDate(new Date());
        
        // Nếu chưa có activatedAt, set ngày hôm nay
        if (existingMembership.getActivatedAt() == null) {
            existingMembership.setActivatedAt(new Date());
        }
        
        // Update
        update(existingMembership);
        
        System.out.println("[MembershipService] ✅ Extended membership ID: " + existingMembership.getId() + 
                          " by " + additionalMonths + " months.");
        System.out.println("[MembershipService]    Old end date: " + currentEndDate);
        System.out.println("[MembershipService]    New end date: " + newEndDate);
        
        return existingMembership;
    }
    
    /**
     * Renew membership đã hết hạn (tạo lại từ ngày hiện tại)
     * 
     * @param expiredMembership Membership đã hết hạn
     * @param packageToAdd Package mới mua
     * @return Membership đã được renew
     */
    private Membership renewExpiredMembership(Membership expiredMembership, Package packageToAdd) {
        Date now = new Date();
        
        // Set startDate = hôm nay
        expiredMembership.setStartDate(now);
        
        // Tính endDate từ hôm nay + duration
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.MONTH, packageToAdd.getDurationMonths());
        expiredMembership.setEndDate(cal.getTime());
        
        // Set status = ACTIVE
        expiredMembership.setStatus("ACTIVE");
        
        // Set activatedAt = hôm nay
        expiredMembership.setActivatedAt(now);
        
        // Update updatedDate
        expiredMembership.setUpdatedDate(now);
        
        // Update
        update(expiredMembership);
        
        System.out.println("[MembershipService] ✅ Renewed expired membership ID: " + expiredMembership.getId() + 
                          " for package " + packageToAdd.getId() + ".");
        System.out.println("[MembershipService]    New start date: " + now);
        System.out.println("[MembershipService]    New end date: " + expiredMembership.getEndDate());
        
        return expiredMembership;
    }
    
    /**
     * Tìm membership đã hết hạn của member với package cụ thể
     * 
     * @param memberId ID của member
     * @param packageId ID của package
     * @return Membership đã hết hạn hoặc null nếu không tìm thấy
     */
    private Membership getExpiredMembershipByMemberIdAndPackageId(int memberId, int packageId) {
        try {
            Date now = new Date();
            List<Membership> allMemberships = membershipDAO.findAll();
            
            // Tìm membership cùng package nhưng đã hết hạn hoặc status không phải ACTIVE
            for (Membership m : allMemberships) {
                if (m.getMember() != null && 
                    m.getMember().getId().equals(memberId) &&
                    m.getPackageO() != null &&
                    m.getPackageO().getId().equals(packageId)) {
                    
                    // Kiểm tra nếu đã hết hạn (endDate < now) hoặc status không phải ACTIVE
                    boolean isExpired = (m.getEndDate() != null && m.getEndDate().before(now)) ||
                                       (!"ACTIVE".equalsIgnoreCase(m.getStatus()));
                    
                    if (isExpired) {
                        return m;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("[MembershipService] Error finding expired membership: " + e.getMessage());
            return null;
        }
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
