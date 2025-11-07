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
    
    /**
     * Activate a membership (change status from INACTIVE to ACTIVE)
     * Used when payment is confirmed (status = PAID)
     * @param membershipId Membership ID
     * @return true if successfully activated, false otherwise
     */
    boolean activateMembership(Long membershipId);
    
    /**
     * Suspend a membership (change status from ACTIVE to SUSPENDED)
     * Used when payment is refunded or chargeback occurs
     * @param membershipId Membership ID
     * @param reason Reason for suspension (e.g., "Payment refunded", "Chargeback")
     * @return true if successfully suspended, false otherwise
     */
    boolean suspendMembership(Long membershipId, String reason);
    
    /**
     * Count active memberships with paid status
     * Returns count of memberships where status = 'ACTIVE', end_date >= CURRENT_DATE, and payment.status = 'PAID'
     * @return Count of active paid memberships
     */
    long countActiveMemberships();
    
    /**
     * Get all memberships with status ACTIVE, EXPIRED, or SUSPENDED
     * Returns all memberships regardless of payment status
     * @return List of memberships with user and package data
     */
    java.util.List<Membership> getAllActiveAndSuspendedMemberships();
    
    /**
     * Count active memberships (status = 'ACTIVE') for management page
     * @return Count of active memberships
     */
    long countActiveMembershipsForManagement();
    
    /**
     * Count expired and suspended memberships (status = 'EXPIRED' OR 'SUSPENDED')
     * @return Count of expired and suspended memberships
     */
    long countSuspendedMemberships();
    
    /**
     * Count all memberships with status ACTIVE, EXPIRED, or SUSPENDED
     * @return Total count
     */
    long countAllMembershipsForManagement();
}