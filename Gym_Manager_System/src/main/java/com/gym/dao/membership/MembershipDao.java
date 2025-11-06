package com.gym.dao.membership;

import com.gym.dao.GenericDAO;
import com.gym.model.membership.Membership;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for Membership entity - JPA Implementation
 * Handles CRUD operations for memberships table
 */
public class MembershipDao extends GenericDAO<Membership> {
    private static final Logger LOGGER = Logger.getLogger(MembershipDao.class.getName());

    public MembershipDao() {
        super(Membership.class);
    }

    /**
     * Create a new membership
     * @param userId User ID
     * @param packageId Package ID
     * @param startDate Start date
     * @param endDate End date (calculated: startDate + duration_months)
     * @param notes Optional notes
     * @return Generated membership_id
     * Note: Creates with status = 'INACTIVE'. Will be activated when payment status = 'PAID'.
     */
    public Long createMembership(Integer userId, Long packageId, LocalDate startDate, 
                                 LocalDate endDate, String notes) {
        try {
            Membership membership = new Membership();
            membership.setUserId(userId);
            membership.setPackageId(packageId);
            membership.setStartDate(startDate);
            membership.setEndDate(endDate);
            membership.setStatus("INACTIVE");  // ✅ Start as INACTIVE, activate when payment = PAID
            membership.setNotes(notes);
            membership.setActivatedAt(null);  // Not activated yet
            membership.setSuspendedAt(null);
            
            beginTransaction();
            em.persist(membership);
            commitTransaction();
            
            LOGGER.log(Level.INFO, "Created INACTIVE membership {0} for user {1}, waiting for payment", 
                      new Object[]{membership.getMembershipId(), userId});
            
            return membership.getMembershipId();
        } catch (Exception e) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, "Error creating membership", e);
            throw new RuntimeException("Failed to create membership: " + e.getMessage(), e);
        }
    }

    /**
     * Find active AND PAID membership for a user
     * Returns membership with:
     * - status = 'ACTIVE' 
     * - end_date >= CURDATE()
     * - payment.status = 'PAID' (payment must exist and be paid)
     * @param userId User ID
     * @return Optional Membership
     */
    public Optional<Membership> findActiveByUser(Integer userId) {
        try {
            // Use JPQL with JOIN to payments table to check payment status
            String jpql = "SELECT DISTINCT m FROM Membership m " +
                         "LEFT JOIN FETCH m.packageEntity p " +
                         "INNER JOIN Payment pay ON pay.membershipId = m.membershipId " +
                         "WHERE m.userId = :userId " +
                         "AND m.status = 'ACTIVE' " +
                         "AND m.endDate >= CURRENT_DATE " +
                         "AND pay.transactionType = 'PACKAGE' " +
                         "AND pay.status = 'PAID' " +
                         "ORDER BY m.endDate DESC";
            
            TypedQuery<Membership> query = em.createQuery(jpql, Membership.class);
            query.setParameter("userId", userId);
            query.setMaxResults(1);
            
            List<Membership> results = query.getResultList();
            if (!results.isEmpty()) {
                Membership membership = results.get(0);
                // Populate transient fields from joined packageEntity
                if (membership.getPackageEntity() != null) {
                    membership.setPackageName(membership.getPackageEntity().getName());
                    membership.setPackageDurationMonths(membership.getPackageEntity().getDurationMonths());
                    membership.setPackagePrice(membership.getPackageEntity().getPrice());
                }
                return Optional.of(membership);
            }
            
            return Optional.empty();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding active and paid membership for user: " + userId, e);
            return Optional.empty();
        }
    }

    /**
     * Find membership by ID
     */
    public Optional<Membership> findById(Long membershipId) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Membership> query = cb.createQuery(Membership.class);
            Root<Membership> root = query.from(Membership.class);
            
            // Join with Package to get package info
            root.fetch("packageEntity", jakarta.persistence.criteria.JoinType.LEFT);
            
            query.where(cb.equal(root.get("membershipId"), membershipId));
            
            TypedQuery<Membership> typedQuery = em.createQuery(query);
            typedQuery.setMaxResults(1);
            
            List<Membership> results = typedQuery.getResultList();
            if (!results.isEmpty()) {
                Membership membership = results.get(0);
                // Populate transient fields from joined packageEntity
                if (membership.getPackageEntity() != null) {
                    membership.setPackageName(membership.getPackageEntity().getName());
                    membership.setPackageDurationMonths(membership.getPackageEntity().getDurationMonths());
                    membership.setPackagePrice(membership.getPackageEntity().getPrice());
                }
                return Optional.of(membership);
            }
            
            return Optional.empty();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding membership by ID: " + membershipId, e);
            return Optional.empty();
        }
    }

    /**
     * Expire a membership (set status = 'EXPIRED')
     */
    public boolean expireMembership(Long membershipId) {
        try {
            beginTransaction();
            
            Membership membership = em.find(Membership.class, membershipId);
            if (membership == null) {
                rollbackTransaction();
                return false;
            }
            
            membership.setStatus("EXPIRED");
            em.merge(membership);
            
            commitTransaction();
            return true;
        } catch (Exception e) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, "Error expiring membership: " + membershipId, e);
            throw new RuntimeException("Failed to expire membership: " + e.getMessage(), e);
        }
    }

    /**
     * Activate a membership (set status = 'ACTIVE' and activated_at = NOW)
     * Used when payment is confirmed (status = PAID)
     */
    public boolean activateMembership(Long membershipId) {
        try {
            beginTransaction();
            
            Membership membership = em.find(Membership.class, membershipId);
            if (membership == null) {
                rollbackTransaction();
                LOGGER.log(Level.WARNING, "Cannot activate: Membership {0} not found", membershipId);
                return false;
            }
            
            // ✅ Only activate if currently INACTIVE
            if (!"INACTIVE".equals(membership.getStatus())) {
                rollbackTransaction();
                LOGGER.log(Level.WARNING, "Cannot activate: Membership {0} is not INACTIVE (current: {1})", 
                          new Object[]{membershipId, membership.getStatus()});
                return false;
            }
            
            membership.setStatus("ACTIVE");
            membership.setActivatedAt(java.time.LocalDateTime.now());  // ✅ Set activation timestamp
            em.merge(membership);
            
            commitTransaction();
            LOGGER.log(Level.INFO, "Activated membership {0} at {1}", 
                      new Object[]{membershipId, membership.getActivatedAt()});
            return true;
        } catch (Exception e) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, "Error activating membership: " + membershipId, e);
            throw new RuntimeException("Failed to activate membership: " + e.getMessage(), e);
        }
    }

    /**
     * Suspend a membership (set status = 'SUSPENDED' and suspended_at = NOW)
     * Used when payment is refunded or chargeback occurs
     */
    public boolean suspendMembership(Long membershipId, String reason) {
        try {
            beginTransaction();
            
            Membership membership = em.find(Membership.class, membershipId);
            if (membership == null) {
                rollbackTransaction();
                LOGGER.log(Level.WARNING, "Cannot suspend: Membership {0} not found", membershipId);
                return false;
            }
            
            // ✅ Only suspend if currently ACTIVE
            if (!"ACTIVE".equals(membership.getStatus())) {
                rollbackTransaction();
                LOGGER.log(Level.WARNING, "Cannot suspend: Membership {0} is not ACTIVE (current: {1})", 
                          new Object[]{membershipId, membership.getStatus()});
                return false;
            }
            
            membership.setStatus("SUSPENDED");
            membership.setSuspendedAt(java.time.LocalDateTime.now());  // ✅ Set suspension timestamp
            
            // Append reason to notes
            String existingNotes = membership.getNotes();
            String suspensionNote = "SUSPENDED at " + membership.getSuspendedAt() + ". Reason: " + reason;
            if (existingNotes != null && !existingNotes.isEmpty()) {
                membership.setNotes(existingNotes + "\n" + suspensionNote);
            } else {
                membership.setNotes(suspensionNote);
            }
            
            em.merge(membership);
            
            commitTransaction();
            LOGGER.log(Level.WARNING, "Suspended membership {0} at {1}. Reason: {2}", 
                      new Object[]{membershipId, membership.getSuspendedAt(), reason});
            return true;
        } catch (Exception e) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, "Error suspending membership: " + membershipId, e);
            throw new RuntimeException("Failed to suspend membership: " + e.getMessage(), e);
        }
    }

    /**
     * List all memberships for a user (for history)
     */
    public List<Membership> listByUser(Integer userId) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Membership> query = cb.createQuery(Membership.class);
            Root<Membership> root = query.from(Membership.class);
            
            // Join with Package to get package info
            root.fetch("packageEntity", jakarta.persistence.criteria.JoinType.INNER);
            
            query.where(cb.equal(root.get("userId"), userId));
            query.orderBy(cb.desc(root.get("createdDate")));
            
            TypedQuery<Membership> typedQuery = em.createQuery(query);
            List<Membership> memberships = typedQuery.getResultList();
            
            // Populate transient fields for each membership
            for (Membership membership : memberships) {
                if (membership.getPackageEntity() != null) {
                    membership.setPackageName(membership.getPackageEntity().getName());
                    membership.setPackageDurationMonths(membership.getPackageEntity().getDurationMonths());
                    membership.setPackagePrice(membership.getPackageEntity().getPrice());
                }
            }
            
            return memberships;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error listing memberships for user: " + userId, e);
            return new ArrayList<>();
        }
    }

    /**
     * Find all expired memberships for a user (for auto-expire logic)
     */
    public List<Membership> findExpiredByUser(Integer userId) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Membership> query = cb.createQuery(Membership.class);
            Root<Membership> root = query.from(Membership.class);
            
            // Join with Package to get package info
            root.fetch("packageEntity", jakarta.persistence.criteria.JoinType.INNER);
            
            List<Predicate> predicates = new ArrayList<>();
            
            // user_id = ?
            predicates.add(cb.equal(root.get("userId"), userId));
            
            // status = 'ACTIVE'
            predicates.add(cb.equal(root.get("status"), "ACTIVE"));
            
            // end_date < CURDATE()
            predicates.add(cb.lessThan(root.get("endDate"), LocalDate.now()));
            
            query.where(predicates.toArray(new Predicate[0]));
            
            TypedQuery<Membership> typedQuery = em.createQuery(query);
            List<Membership> memberships = typedQuery.getResultList();
            
            // Populate transient fields for each membership
            for (Membership membership : memberships) {
                if (membership.getPackageEntity() != null) {
                    membership.setPackageName(membership.getPackageEntity().getName());
                    membership.setPackageDurationMonths(membership.getPackageEntity().getDurationMonths());
                    membership.setPackagePrice(membership.getPackageEntity().getPrice());
                }
            }
            
            return memberships;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding expired memberships for user: " + userId, e);
            return new ArrayList<>();
        }
    }
}
