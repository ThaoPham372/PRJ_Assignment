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

    /**
     * Get total revenue for today (payments with status = PAID)
     * @return Total revenue as BigDecimal
     */
    public BigDecimal getRevenueToday() {
        try {
            java.time.LocalDate today = java.time.LocalDate.now();
            java.time.LocalDateTime startOfDay = today.atStartOfDay();
            java.time.LocalDateTime endOfDay = today.atTime(23, 59, 59);
            
            String jpql = "SELECT SUM(p.amount) FROM Payment p " +
                         "WHERE p.status = :status " +
                         "AND p.paymentDate >= :startOfDay " +
                         "AND p.paymentDate <= :endOfDay";
            
            TypedQuery<BigDecimal> query = em.createQuery(jpql, BigDecimal.class);
            query.setParameter("status", PaymentStatus.PAID);
            query.setParameter("startOfDay", startOfDay);
            query.setParameter("endOfDay", endOfDay);
            
            BigDecimal result = query.getSingleResult();
            return result != null ? result : BigDecimal.ZERO;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting revenue today", e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * Get total revenue for yesterday (payments with status = PAID)
     * @return Total revenue as BigDecimal
     */
    public BigDecimal getRevenueYesterday() {
        try {
            java.time.LocalDate yesterday = java.time.LocalDate.now().minusDays(1);
            java.time.LocalDateTime startOfDay = yesterday.atStartOfDay();
            java.time.LocalDateTime endOfDay = yesterday.atTime(23, 59, 59);
            
            String jpql = "SELECT SUM(p.amount) FROM Payment p " +
                         "WHERE p.status = :status " +
                         "AND p.paymentDate >= :startOfDay " +
                         "AND p.paymentDate <= :endOfDay";
            
            TypedQuery<BigDecimal> query = em.createQuery(jpql, BigDecimal.class);
            query.setParameter("status", PaymentStatus.PAID);
            query.setParameter("startOfDay", startOfDay);
            query.setParameter("endOfDay", endOfDay);
            
            BigDecimal result = query.getSingleResult();
            return result != null ? result : BigDecimal.ZERO;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting revenue yesterday", e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * Get total revenue for last month (payments with status = PAID)
     * @return Total revenue as BigDecimal
     */
    public BigDecimal getRevenueLastMonth() {
        try {
            java.time.LocalDate firstDayOfLastMonth = java.time.LocalDate.now().minusMonths(1).withDayOfMonth(1);
            java.time.LocalDateTime startOfLastMonth = firstDayOfLastMonth.atStartOfDay();
            java.time.LocalDateTime startOfThisMonth = java.time.LocalDate.now().withDayOfMonth(1).atStartOfDay();
            
            String jpql = "SELECT SUM(p.amount) FROM Payment p " +
                         "WHERE p.status = :status " +
                         "AND p.paymentDate >= :startOfLastMonth " +
                         "AND p.paymentDate < :startOfThisMonth";
            
            TypedQuery<BigDecimal> query = em.createQuery(jpql, BigDecimal.class);
            query.setParameter("status", PaymentStatus.PAID);
            query.setParameter("startOfLastMonth", startOfLastMonth);
            query.setParameter("startOfThisMonth", startOfThisMonth);
            
            BigDecimal result = query.getSingleResult();
            return result != null ? result : BigDecimal.ZERO;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting revenue last month", e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * Get total revenue for current year (payments with status = PAID)
     * @return Total revenue as BigDecimal
     */
    public BigDecimal getRevenueThisYear() {
        try {
            java.time.LocalDate firstDayOfYear = java.time.LocalDate.now().withDayOfYear(1);
            java.time.LocalDateTime startOfYear = firstDayOfYear.atStartOfDay();
            
            String jpql = "SELECT SUM(p.amount) FROM Payment p " +
                         "WHERE p.status = :status " +
                         "AND p.paymentDate >= :startOfYear";
            
            TypedQuery<BigDecimal> query = em.createQuery(jpql, BigDecimal.class);
            query.setParameter("status", PaymentStatus.PAID);
            query.setParameter("startOfYear", startOfYear);
            
            BigDecimal result = query.getSingleResult();
            return result != null ? result : BigDecimal.ZERO;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting revenue this year", e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * Get total revenue for last year (payments with status = PAID)
     * @return Total revenue as BigDecimal
     */
    public BigDecimal getRevenueLastYear() {
        try {
            java.time.LocalDate firstDayOfLastYear = java.time.LocalDate.now().minusYears(1).withDayOfYear(1);
            java.time.LocalDateTime startOfLastYear = firstDayOfLastYear.atStartOfDay();
            java.time.LocalDate firstDayOfThisYear = java.time.LocalDate.now().withDayOfYear(1);
            java.time.LocalDateTime startOfThisYear = firstDayOfThisYear.atStartOfDay();
            
            String jpql = "SELECT SUM(p.amount) FROM Payment p " +
                         "WHERE p.status = :status " +
                         "AND p.paymentDate >= :startOfLastYear " +
                         "AND p.paymentDate < :startOfThisYear";
            
            TypedQuery<BigDecimal> query = em.createQuery(jpql, BigDecimal.class);
            query.setParameter("status", PaymentStatus.PAID);
            query.setParameter("startOfLastYear", startOfLastYear);
            query.setParameter("startOfThisYear", startOfThisYear);
            
            BigDecimal result = query.getSingleResult();
            return result != null ? result : BigDecimal.ZERO;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting revenue last year", e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * Get total amount spent by a member (all payments with status = PAID)
     * @param memberId Member ID
     * @return Total amount spent as BigDecimal
     */
    public BigDecimal getTotalSpentByMember(Integer memberId) {
        try {
            String jpql = "SELECT SUM(p.amount) FROM Payment p " +
                         "WHERE p.memberId = :memberId " +
                         "AND p.status = :status";
            
            TypedQuery<BigDecimal> query = em.createQuery(jpql, BigDecimal.class);
            query.setParameter("memberId", memberId);
            query.setParameter("status", PaymentStatus.PAID);
            
            BigDecimal result = query.getSingleResult();
            return result != null ? result : BigDecimal.ZERO;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting total spent by member: " + memberId, e);
            return BigDecimal.ZERO;
        }
    }
}
