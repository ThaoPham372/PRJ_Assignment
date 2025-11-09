package dao;

import model.Payment;
import model.shop.PaymentStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for Payment entity - extends GenericDAO for CRUD operations
 * Follows OOP principles and reuses GenericDAO methods
 */
public class PaymentDAO extends GenericDAO<Payment> {
    private static final Logger LOGGER = Logger.getLogger(PaymentDAO.class.getName());

    public PaymentDAO() {
        super(Payment.class);
    }

    /**
     * Find payments by order ID
     * Reuses GenericDAO.findByField() method
     */
    public List<Payment> findByOrder(Integer orderId) {
        try {
            String jpql = "SELECT p FROM Payment p WHERE p.orderId = :orderId ORDER BY p.paymentDate DESC";
            TypedQuery<Payment> query = em.createQuery(jpql, Payment.class);
            query.setParameter("orderId", orderId);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding payments by order: " + orderId, e);
            return Collections.emptyList();
        }
    }

    /**
     * Find payments by membership ID
     * Reuses GenericDAO.findByField() method
     */
    public List<Payment> findByMembership(Integer membershipId) {
        try {
            String jpql = "SELECT p FROM Payment p WHERE p.membershipId = :membershipId ORDER BY p.paymentDate DESC";
            TypedQuery<Payment> query = em.createQuery(jpql, Payment.class);
            query.setParameter("membershipId", membershipId);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding payments by membership: " + membershipId, e);
            return Collections.emptyList();
        }
    }

    /**
     * Find payment by ID - reuses GenericDAO.findById()
     */
    public Optional<Payment> findByIdOptional(Integer paymentId) {
        try {
            Payment payment = findById(paymentId);
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
            Payment payment = findByField("referenceId", referenceId);
            return Optional.ofNullable(payment);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding payment by reference ID: " + referenceId, e);
            return Optional.empty();
        }
    }

    /**
     * Get total revenue for current month (payments with status = PAID)
     * @return Total revenue as BigDecimal
     */
    public BigDecimal getRevenueThisMonth() {
        try {
            java.time.LocalDate firstDayOfMonth = java.time.LocalDate.now().withDayOfMonth(1);
            java.time.LocalDateTime startOfMonth = firstDayOfMonth.atStartOfDay();
            
            String jpql = "SELECT SUM(p.amount) FROM Payment p " +
                         "WHERE p.status = :status " +
                         "AND p.paymentDate >= :startOfMonth " +
                         "AND p.paymentDate < :startOfNextMonth";
            
            TypedQuery<BigDecimal> query = em.createQuery(jpql, BigDecimal.class);
            query.setParameter("status", PaymentStatus.PAID);
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
