package com.gym.service.membership;

import com.gym.model.membership.Membership;
import com.gym.model.membership.Package;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Membership functionality
 */
public interface MembershipService {
    
    /**
     * Get all active packages (optionally filtered by gym_id)
     */
    List<Package> getAllActivePackages(Integer gymId);
    
    /**
     * Get all active packages
     */
    List<Package> getAllActivePackages();
    
    /**
     * Get package by ID
     */
    Optional<Package> getPackageById(Long packageId);
    
    /**
     * Get current active membership for a user
     * Returns membership with status = ACTIVE and end_date >= today
     */
    Optional<Membership> getCurrentMembership(Integer userId);
    
    /**
     * Check and expire memberships that have passed end_date
     * Automatically sets status = 'EXPIRED' for expired memberships
     */
    void checkAndExpire(Integer userId);
    
    /**
     * Purchase a package (create new membership)
     * Flow:
     * 1. Get package from packages table
     * 2. startDate = CURRENT_DATE
     * 3. endDate = startDate + duration_months (from package)
     * 4. Create membership with status = 'ACTIVE'
     * 5. If user has expired ACTIVE memberships, auto-expire them
     * @return Created Membership
     */
    Membership purchasePackage(Integer userId, Long packageId, String notes);
    
    /**
     * Get membership history for a user
     */
    List<Membership> getMembershipHistory(Integer userId);
}
