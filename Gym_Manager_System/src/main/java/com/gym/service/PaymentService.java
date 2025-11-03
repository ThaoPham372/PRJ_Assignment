package com.gym.service;

import com.gym.dao.PaymentDAO;
import com.gym.model.Payment;
import com.gym.model.shop.PaymentMethod;
import com.gym.model.shop.PaymentStatus;
import com.gym.model.shop.OrderStatus;
import com.gym.dao.shop.OrderDao;
import com.gym.dao.membership.MembershipDAO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Payment operations
 */
public interface PaymentService {
    
    /**
     * Create payment for order (transaction_type = 'PRODUCT')
     * @param userId User ID
     * @param orderId Order ID
     * @param amount Payment amount
     * @param method Payment method
     * @param referenceId Optional reference ID (e.g., MoMo transaction ID)
     * @param conn Optional connection (for transactions)
     * @return Created Payment ID
     */
    Integer createPaymentForOrder(Integer userId, Long orderId, BigDecimal amount,
                                 PaymentMethod method, String referenceId, java.sql.Connection conn);
    
    /**
     * Create payment for membership (transaction_type = 'PACKAGE')
     * @param userId User ID
     * @param membershipId Membership ID
     * @param amount Payment amount
     * @param method Payment method
     * @param referenceId Optional reference ID (e.g., MoMo transaction ID)
     * @param conn Optional connection (for transactions)
     * @return Created Payment ID
     */
    Integer createPaymentForMembership(Integer userId, Long membershipId, BigDecimal amount,
                                      PaymentMethod method, String referenceId, java.sql.Connection conn);
    
    /**
     * Update payment status and handle related entity updates
     * - If status = 'PAID':
     *   - For PRODUCT: update order.order_status = 'CONFIRMED'
     *   - For PACKAGE: update membership.status = 'ACTIVE'
     * @param paymentId Payment ID
     * @param newStatus New payment status
     * @return true if successful
     */
    boolean updatePaymentStatus(Integer paymentId, PaymentStatus newStatus);
    
    /**
     * Find payments by order ID
     */
    List<Payment> findPaymentsByOrder(Long orderId);
    
    /**
     * Find payments by membership ID
     */
    List<Payment> findPaymentsByMembership(Long membershipId);
    
    /**
     * Find payment by reference ID (useful for payment gateway callbacks)
     */
    Optional<Payment> findPaymentByReferenceId(String referenceId);
}

