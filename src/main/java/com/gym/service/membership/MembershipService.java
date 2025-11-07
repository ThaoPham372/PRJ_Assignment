package com.gym.service.membership;

import com.gym.dao.membership.MembershipDao;
import com.gym.dao.membership.PackageDAO;
import com.gym.model.membership.Membership;
import com.gym.model.membership.Package;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of IMembershipService
 */
public class MembershipService implements IMembershipService {
    private final PackageDAO packageDAO;
    private final MembershipDao membershipDao;

    public MembershipService() {
        this.packageDAO = new PackageDAO();
        this.membershipDao = new MembershipDao();
    }

    @Override
    public List<Package> getAllActivePackages(Integer gymId) {
        return packageDAO.findAllActive(gymId);
    }

    @Override
    public List<Package> getAllActivePackages() {
        return packageDAO.findAllActive(null);
    }

    @Override
    public Optional<Package> getPackageById(Long packageId) {
        return packageDAO.findById(packageId);
    }

    @Override
    public Optional<Membership> getCurrentMembership(Integer userId) {
        // First check and expire any expired memberships
        checkAndExpire(userId);
        
        // Then return current active membership
        return membershipDao.findActiveByUser(userId);
    }

    @Override
    public void checkAndExpire(Integer userId) {
        // Find all ACTIVE memberships that have passed end_date
        List<Membership> expiredMemberships = membershipDao.findExpiredByUser(userId);
        
        // Set status = 'EXPIRED' for each expired membership
        for (Membership membership : expiredMemberships) {
            membershipDao.expireMembership(membership.getMembershipId());
            System.out.println("[MembershipService] Auto-expired membership ID: " + membership.getMembershipId() + 
                             " for user ID: " + userId);
        }
    }

    @Override
    public Membership purchasePackage(Integer userId, Long packageId, String notes) {
        // 1. Get package from packages table
        Optional<Package> packageOpt = packageDAO.findById(packageId);
        if (packageOpt.isEmpty()) {
            throw new IllegalArgumentException("Package not found: " + packageId);
        }
        
        Package pkg = packageOpt.get();
        if (!pkg.getIsActive()) {
            throw new IllegalStateException("Package is not active: " + packageId);
        }
        
        // 2. Check and expire any expired memberships for this user first
        checkAndExpire(userId);
        
        // 3. Calculate dates
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(pkg.getDurationMonths());
        
        // 4. Create membership with status = 'INACTIVE'
        // ✅ NEW: Membership starts INACTIVE and will be activated when payment.status = 'PAID'
        Long membershipId = membershipDao.createMembership(userId, packageId, startDate, endDate, notes);
        if (membershipId == null) {
            throw new RuntimeException("Failed to create membership");
        }
        
        System.out.println("[MembershipService] Created membership ID: " + membershipId + 
                         " with status=INACTIVE (waiting for payment) for user ID: " + userId + ", package ID: " + packageId);
        
        // 5. Return created membership
        // Note: User won't be able to use it until payment.status = 'PAID'
        Optional<Membership> created = membershipDao.findById(membershipId);
        if (created.isEmpty()) {
            throw new RuntimeException("Membership created but could not be retrieved");
        }
        
        return created.get();
    }

    @Override
    public List<Membership> getMembershipHistory(Integer userId) {
        return membershipDao.listByUser(userId);
    }
    
    @Override
    public boolean activateMembership(Long membershipId) {
        // Activate membership (set status = 'ACTIVE')
        boolean activated = membershipDao.activateMembership(membershipId);
        if (activated) {
            System.out.println("[MembershipService] Activated membership ID: " + membershipId);
        } else {
            System.out.println("[MembershipService] Failed to activate membership ID: " + membershipId);
        }
        return activated;
    }
    
    @Override
    public boolean suspendMembership(Long membershipId, String reason) {
        // Suspend membership (set status = 'SUSPENDED')
        boolean suspended = membershipDao.suspendMembership(membershipId, reason);
        if (suspended) {
            System.out.println("[MembershipService] Suspended membership ID: " + membershipId + ". Reason: " + reason);
        } else {
            System.out.println("[MembershipService] Failed to suspend membership ID: " + membershipId);
        }
        return suspended;
    }
    
    @Override
    public long countActiveMemberships() {
        return membershipDao.countActiveMemberships();
    }
    
    @Override
    public java.util.List<Membership> getAllActiveAndSuspendedMemberships() {
        return membershipDao.getAllActiveAndSuspendedMemberships();
    }
    
    @Override
    public long countActiveMembershipsForManagement() {
        return membershipDao.countActiveMembershipsForManagement();
    }
    
    @Override
    public long countSuspendedMemberships() {
        return membershipDao.countSuspendedMemberships();
    }
    
    @Override
    public long countAllMembershipsForManagement() {
        return membershipDao.countAllMembershipsForManagement();
    }

    public void delete(Membership membership) {
        membershipDao.delete(membership);
    }

    public List<Membership> getAll() {
        return membershipDao.findAll();
    }

    public void add(Membership membership) {
        membershipDao.save(membership);
    }
}