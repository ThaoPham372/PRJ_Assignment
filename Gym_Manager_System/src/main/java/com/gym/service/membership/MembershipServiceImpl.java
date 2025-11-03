package com.gym.service.membership;

import com.gym.dao.membership.MembershipDAO;
import com.gym.dao.membership.PackageDAO;
import com.gym.model.membership.Membership;
import com.gym.model.membership.Package;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of MembershipService
 */
public class MembershipServiceImpl implements MembershipService {
    private final PackageDAO packageDAO;
    private final MembershipDAO membershipDAO;

    public MembershipServiceImpl() {
        this.packageDAO = new PackageDAO();
        this.membershipDAO = new MembershipDAO();
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
        return membershipDAO.findActiveByUser(userId);
    }

    @Override
    public void checkAndExpire(Integer userId) {
        // Find all ACTIVE memberships that have passed end_date
        List<Membership> expiredMemberships = membershipDAO.findExpiredByUser(userId);
        
        // Set status = 'EXPIRED' for each expired membership
        for (Membership membership : expiredMemberships) {
            membershipDAO.expireMembership(membership.getMembershipId());
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
        
        // 4. Create membership with status = 'ACTIVE'
        Long membershipId = membershipDAO.createMembership(userId, packageId, startDate, endDate, notes);
        if (membershipId == null) {
            throw new RuntimeException("Failed to create membership");
        }
        
        System.out.println("[MembershipService] Created membership ID: " + membershipId + 
                         " for user ID: " + userId + ", package ID: " + packageId);
        
        // 5. Return created membership
        Optional<Membership> created = membershipDAO.findActiveByUser(userId);
        if (created.isEmpty()) {
            throw new RuntimeException("Membership created but could not be retrieved");
        }
        
        return created.get();
    }

    @Override
    public List<Membership> getMembershipHistory(Integer userId) {
        return membershipDAO.listByUser(userId);
    }
}
