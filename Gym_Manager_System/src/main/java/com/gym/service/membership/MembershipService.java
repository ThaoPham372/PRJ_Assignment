package com.gym.service.membership;

import com.gym.model.membership.Membership;
import com.gym.model.membership.UserMembership;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Membership functionality
 */
public interface MembershipService {
    
    /**
     * Get all active memberships
     */
    List<Membership> getAllActiveMemberships();
    
    /**
     * Get membership by ID
     */
    Optional<Membership> getMembershipById(Long membershipId);
    
    /**
     * Get active membership for a user
     */
    Optional<UserMembership> getUserActiveMembership(Long userId);
    
    /**
     * Check if user has a specific membership
     */
    boolean hasMembership(Long userId, Long membershipId);
    
    /**
     * Create user membership after payment
     */
    UserMembership createUserMembership(Long userId, Long membershipId, Long orderId);
}


