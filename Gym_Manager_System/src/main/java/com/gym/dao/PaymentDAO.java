package com.gym.dao;

import com.gym.model.Payment;
import com.gym.model.shop.PaymentMethod;
import com.gym.model.shop.PaymentStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for Payment entity - JPA Implementation
 * Handles CRUD operations for payments table
 */
public class PaymentDAO extends BaseDAO<Payment> {
    private static final Logger LOGGER = Logger.getLogger(PaymentDAO.class.getName());

    public PaymentDAO() {
        super();
    }

    /**
     * Create payment for order (transaction_type = 'PRODUCT')
     */
    public Integer createPaymentForOrder(Integer userId, Long orderId, BigDecimal amount, 
                                        PaymentMethod method, String referenceId) {
        return createPaymentForOrder(userId, orderId, amount, method, referenceId, null);
    }

    /**
     * Create payment for order using provided EntityManager (for transactions)
     */
    public Integer createPaymentForOrder(Integer userId, Long orderId, BigDecimal amount, 
                                        PaymentMethod method, String referenceId, EntityManager sharedEm) {
        EntityManager emToUse = sharedEm != null ? sharedEm : em;
        
        try {
            if (sharedEm == null) {
                beginTransaction();
            }
            
            Payment payment = new Payment();
            payment.setUserId(userId);
            payment.setAmount(amount);
            payment.setMethod(method);
            payment.setStatus(PaymentStatus.PENDING);
            payment.setReferenceId(referenceId);
            payment.setTransactionType(Payment.TransactionType.PRODUCT);
            payment.setOrderId(orderId);
            payment.setPaymentDate(java.time.LocalDateTime.now());
            
            emToUse.persist(payment);
            
            if (sharedEm == null) {
                commitTransaction();
            } else {
                emToUse.flush();
            }
            
            return payment.getPaymentId();
        } catch (Exception e) {
            if (sharedEm == null) {
                rollbackTransaction();
            }
            LOGGER.log(Level.SEVERE, "Error creating payment for order", e);
            throw new RuntimeException("Failed to create payment for order: " + e.getMessage(), e);
        }
    }

    /**
     * Create payment for membership (transaction_type = 'PACKAGE')
     */
    public Integer createPaymentForMembership(Integer userId, Long membershipId, BigDecimal amount,
                                             PaymentMethod method, String referenceId) {
        return createPaymentForMembership(userId, membershipId, amount, method, referenceId, null);
    }

    /**
     * Create payment for membership using provided EntityManager (for transactions)
     */
    public Integer createPaymentForMembership(Integer userId, Long membershipId, BigDecimal amount,
                                             PaymentMethod method, String referenceId, EntityManager sharedEm) {
        EntityManager emToUse = sharedEm != null ? sharedEm : em;
        
        try {
            if (sharedEm == null) {
                beginTransaction();
            }
            
            Payment payment = new Payment();
            payment.setUserId(userId);
            payment.setAmount(amount);
            payment.setMethod(method);
            payment.setStatus(PaymentStatus.PENDING);
            payment.setReferenceId(referenceId);
            payment.setTransactionType(Payment.TransactionType.PACKAGE);
            payment.setMembershipId(membershipId);
            payment.setPaymentDate(java.time.LocalDateTime.now());
            
            emToUse.persist(payment);
            
            if (sharedEm == null) {
                commitTransaction();
            } else {
                emToUse.flush();
            }
            
            return payment.getPaymentId();
        } catch (Exception e) {
            if (sharedEm == null) {
                rollbackTransaction();
            }
            LOGGER.log(Level.SEVERE, "Error creating payment for membership", e);
            throw new RuntimeException("Failed to create payment for membership: " + e.getMessage(), e);
        }
    }

    /**
     * Update payment status
     */
    public boolean updateStatus(Integer paymentId, PaymentStatus newStatus) {
        try {
            beginTransaction();
            
            Payment payment = em.find(Payment.class, paymentId);
            if (payment == null) {
                rollbackTransaction();
                return false;
            }
            
            payment.setStatus(newStatus);
            
            // ✅ Set paid_at timestamp when payment is PAID
            if (newStatus == PaymentStatus.PAID && payment.getPaidAt() == null) {
                payment.setPaidAt(java.time.LocalDateTime.now());
            }
            
            em.merge(payment);
            
            commitTransaction();
            return true;
        } catch (Exception e) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, "Error updating payment status: " + paymentId, e);
            throw new RuntimeException("Failed to update payment status: " + e.getMessage(), e);
        }
    }

    /**
     * Update payment status and reference ID
     */
    public boolean updateStatusAndReference(Integer paymentId, PaymentStatus newStatus, String referenceId) {
        try {
            beginTransaction();
            
            Payment payment = em.find(Payment.class, paymentId);
            if (payment == null) {
                rollbackTransaction();
                return false;
            }
            
            payment.setStatus(newStatus);
            payment.setReferenceId(referenceId);
            
            // ✅ Set paid_at timestamp when payment is PAID
            if (newStatus == PaymentStatus.PAID && payment.getPaidAt() == null) {
                payment.setPaidAt(java.time.LocalDateTime.now());
            }
            
            // ✅ Set external_ref from payment gateway
            if (referenceId != null && !referenceId.isEmpty()) {
                payment.setExternalRef(referenceId);
            }
            
            em.merge(payment);
            
            commitTransaction();
            return true;
        } catch (Exception e) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, "Error updating payment status and reference: " + paymentId, e);
            throw new RuntimeException("Failed to update payment: " + e.getMessage(), e);
        }
    }

    /**
     * Find payments by order ID
     */
    public List<Payment> findByOrder(Long orderId) {
        try {
            String jpql = "SELECT p FROM Payment p WHERE p.orderId = :orderId ORDER BY p.paymentDate DESC";
            TypedQuery<Payment> query = em.createQuery(jpql, Payment.class);
            query.setParameter("orderId", orderId);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding payments by order: " + orderId, e);
            return new ArrayList<>();
        }
    }

    /**
     * Find payments by membership ID
     */
    public List<Payment> findByMembership(Long membershipId) {
        try {
            String jpql = "SELECT p FROM Payment p WHERE p.membershipId = :membershipId ORDER BY p.paymentDate DESC";
            TypedQuery<Payment> query = em.createQuery(jpql, Payment.class);
            query.setParameter("membershipId", membershipId);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding payments by membership: " + membershipId, e);
            return new ArrayList<>();
        }
    }

    /**
     * Find payment by ID
     */
    public Optional<Payment> findById(Integer paymentId) {
        try {
            Payment payment = em.find(Payment.class, paymentId);
            return Optional.ofNullable(payment);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding payment by ID: " + paymentId, e);
            return Optional.empty();
        }
    }

    /**
     * Find payment by reference ID
     */
    public Optional<Payment> findByReferenceId(String referenceId) {
        try {
            String jpql = "SELECT p FROM Payment p WHERE p.referenceId = :referenceId";
            TypedQuery<Payment> query = em.createQuery(jpql, Payment.class);
            query.setParameter("referenceId", referenceId);
            
            try {
                Payment payment = query.getSingleResult();
                return Optional.of(payment);
            } catch (NoResultException e) {
                return Optional.empty();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding payment by reference ID: " + referenceId, e);
            return Optional.empty();
        }
    }

    /**
     * Get total revenue for current month (payments with status = 'PAID')
     * @return Total revenue as BigDecimal
     */
    public BigDecimal getRevenueThisMonth() {
        try {
            // Get first day of current month
            java.time.LocalDate firstDayOfMonth = java.time.LocalDate.now().withDayOfMonth(1);
            java.time.LocalDateTime startOfMonth = firstDayOfMonth.atStartOfDay();
            
            String jpql = "SELECT SUM(p.amount) FROM Payment p " +
                         "WHERE p.status = 'PAID' " +
                         "AND p.paymentDate >= :startOfMonth " +
                         "AND p.paymentDate < :startOfNextMonth";
            
            TypedQuery<BigDecimal> query = em.createQuery(jpql, BigDecimal.class);
            query.setParameter("startOfMonth", startOfMonth);
            query.setParameter("startOfNextMonth", startOfMonth.plusMonths(1));
            
            BigDecimal result = query.getSingleResult();
            return result != null ? result : BigDecimal.ZERO;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting revenue this month", e);
            return BigDecimal.ZERO;
        }
    }
    
}
