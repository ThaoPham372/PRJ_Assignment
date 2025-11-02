package com.gym.service.membership;

import com.gym.dao.membership.MembershipDao;
import com.gym.dao.membership.UserMembershipDao;
import com.gym.model.membership.Membership;
import com.gym.model.membership.UserMembership;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of MembershipService
 */
public class MembershipServiceImpl implements MembershipService {
    private final MembershipDao membershipDao;
    private final UserMembershipDao userMembershipDao;

    public MembershipServiceImpl() {
        this.membershipDao = new MembershipDao();
        this.userMembershipDao = new UserMembershipDao();
    }

    @Override
    public List<Membership> getAllActiveMemberships() {
        return membershipDao.findAllActive();
    }

    @Override
    public Optional<Membership> getMembershipById(Long membershipId) {
        return membershipDao.findById(membershipId);
    }

    @Override
    public Optional<UserMembership> getUserActiveMembership(Long userId) {
        return userMembershipDao.findActiveByUserId(userId);
    }

    @Override
    public boolean hasMembership(Long userId, Long membershipId) {
        return userMembershipDao.hasMembership(userId, membershipId);
    }

    @Override
    public UserMembership createUserMembership(Long userId, Long membershipId, Long orderId) {
        Optional<Membership> membershipOpt = membershipDao.findById(membershipId);
        if (membershipOpt.isEmpty()) {
            throw new IllegalArgumentException("Membership not found: " + membershipId);
        }
        
        Membership membership = membershipOpt.get();
        LocalDate startDate = LocalDate.now();
        LocalDate expiryDate = startDate.plusMonths(membership.getDurationMonths());
        
        Long userMembershipId = userMembershipDao.create(userId, membershipId, startDate, expiryDate, orderId);
        if (userMembershipId == null) {
            throw new RuntimeException("Failed to create user membership");
        }
        
        // Return created user membership
        return userMembershipDao.findActiveByUserId(userId).orElse(null);
    }
}


