package com.gym.service;

import com.gym.dao.PaymentDAO;
import com.gym.dao.shop.OrderDao;
import com.gym.dao.membership.MembershipDAO;
import com.gym.model.Payment;
import com.gym.model.shop.PaymentMethod;
import com.gym.model.shop.PaymentStatus;
import com.gym.model.shop.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Implementation of PaymentService
 */
public class PaymentServiceImpl implements PaymentService {
    private static final Logger LOGGER = Logger.getLogger(PaymentServiceImpl.class.getName());
    private final PaymentDAO paymentDAO;
    private final OrderDao orderDao;
    private final MembershipDAO membershipDAO;

    public PaymentServiceImpl() {
        this.paymentDAO = new PaymentDAO();
        this.orderDao = new OrderDao();
        this.membershipDAO = new MembershipDAO();
    }

    @Override
    public Integer createPaymentForOrder(Integer userId, Long orderId, BigDecimal amount,
                                        PaymentMethod method, String referenceId, java.sql.Connection conn) {
        LOGGER.info("Creating payment for order: orderId=" + orderId + ", amount=" + amount + ", method=" + method);
        return paymentDAO.createPaymentForOrder(userId, orderId, amount, method, referenceId, conn);
    }

    @Override
    public Integer createPaymentForMembership(Integer userId, Long membershipId, BigDecimal amount,
                                             PaymentMethod method, String referenceId, java.sql.Connection conn) {
        LOGGER.info("Creating payment for membership: membershipId=" + membershipId + ", amount=" + amount + ", method=" + method);
        return paymentDAO.createPaymentForMembership(userId, membershipId, amount, method, referenceId, conn);
    }

    @Override
    public boolean updatePaymentStatus(Integer paymentId, PaymentStatus newStatus) {
        return updatePaymentStatus(paymentId, newStatus, null);
    }

    /**
     * Update payment status with optional reference ID
     */
    public boolean updatePaymentStatus(Integer paymentId, PaymentStatus newStatus, String referenceId) {
        LOGGER.info("Updating payment status: paymentId=" + paymentId + ", newStatus=" + newStatus + ", referenceId=" + referenceId);
        
        // First, get the payment to check transaction type
        Optional<Payment> paymentOpt = paymentDAO.findById(paymentId);
        if (paymentOpt.isEmpty()) {
            LOGGER.warning("Payment not found: " + paymentId);
            return false;
        }

        Payment payment = paymentOpt.get();
        
        // Update payment status (and referenceId if provided)
        boolean updated;
        if (referenceId != null && !referenceId.trim().isEmpty()) {
            updated = paymentDAO.updateStatusAndReference(paymentId, newStatus, referenceId);
        } else {
            updated = paymentDAO.updateStatus(paymentId, newStatus);
        }
        
        if (!updated) {
            LOGGER.warning("Failed to update payment status: " + paymentId);
            return false;
        }

        // If payment is now PAID, update related entity
        if (newStatus == PaymentStatus.PAID) {
            if (payment.getTransactionType() == Payment.TransactionType.PRODUCT) {
                // Update order status to CONFIRMED
                if (payment.getOrderId() != null) {
                    try {
                        orderDao.updateOrderStatus(payment.getOrderId(), OrderStatus.CONFIRMED);
                        LOGGER.info("Updated order status to CONFIRMED: orderId=" + payment.getOrderId());
                    } catch (Exception e) {
                        LOGGER.severe("Error updating order status: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } else if (payment.getTransactionType() == Payment.TransactionType.PACKAGE) {
                // Membership should already be ACTIVE when created
                // But we can verify it here if needed
                LOGGER.info("Payment for package membership completed: membershipId=" + payment.getMembershipId());
            }
        }

        return true;
    }

    @Override
    public List<Payment> findPaymentsByOrder(Long orderId) {
        return paymentDAO.findByOrder(orderId);
    }

    @Override
    public List<Payment> findPaymentsByMembership(Long membershipId) {
        return paymentDAO.findByMembership(membershipId);
    }

    @Override
    public Optional<Payment> findPaymentByReferenceId(String referenceId) {
        return paymentDAO.findByReferenceId(referenceId);
    }
}

