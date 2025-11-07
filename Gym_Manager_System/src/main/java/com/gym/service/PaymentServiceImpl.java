package com.gym.service;

import com.gym.dao.PaymentDAO;
import com.gym.dao.shop.OrderDao;
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

    public PaymentServiceImpl() {
        this.paymentDAO = new PaymentDAO();
        this.orderDao = new OrderDao();
    }

    @Override
    public Integer createPaymentForOrder(Integer userId, Long orderId, BigDecimal amount,
                                        PaymentMethod method, String referenceId, jakarta.persistence.EntityManager sharedEm) {
        LOGGER.info("Creating payment for order: orderId=" + orderId + ", amount=" + amount + ", method=" + method);
        return paymentDAO.createPaymentForOrder(userId, orderId, amount, method, referenceId, sharedEm);
    }

    @Override
    public Integer createPaymentForMembership(Integer userId, Long membershipId, BigDecimal amount,
                                             PaymentMethod method, String referenceId, jakarta.persistence.EntityManager sharedEm) {
        LOGGER.info("Creating payment for membership: membershipId=" + membershipId + ", amount=" + amount + ", method=" + method);
        return paymentDAO.createPaymentForMembership(userId, membershipId, amount, method, referenceId, sharedEm);
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

        // ✅ Handle payment status transitions
        if (newStatus == PaymentStatus.PAID) {
            // Payment confirmed → activate related entity
            if (payment.getTransactionType() == Payment.TransactionType.PRODUCT) {
                // Update order status to CONFIRMED
                if (payment.getOrderId() != null) {
                    try {
                        orderDao.updateOrderStatus(payment.getOrderId(), OrderStatus.CONFIRMED);
                        LOGGER.info("✅ [PAID→CONFIRMED] Updated order status to CONFIRMED: orderId=" + payment.getOrderId());
                    } catch (Exception e) {
                        LOGGER.severe("Error updating order status: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } else if (payment.getTransactionType() == Payment.TransactionType.PACKAGE) {
                // ✅ Activate the membership (change status from INACTIVE to ACTIVE)
                if (payment.getMembershipId() != null) {
                    try {
                        com.gym.service.membership.MembershipService membershipService = 
                            new com.gym.service.membership.MembershipServiceImpl();
                        boolean activated = membershipService.activateMembership(payment.getMembershipId());
                        if (activated) {
                            LOGGER.info("✅ [PAID→ACTIVE] Activated membership: membershipId=" + payment.getMembershipId());
                        } else {
                            LOGGER.warning("Failed to activate membership: membershipId=" + payment.getMembershipId());
                        }
                    } catch (Exception e) {
                        LOGGER.severe("Error activating membership: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } else if (newStatus == PaymentStatus.REFUNDED || newStatus == PaymentStatus.CHARGEBACK) {
            // ✅ Payment refunded/chargeback → suspend membership
            if (payment.getTransactionType() == Payment.TransactionType.PACKAGE) {
                if (payment.getMembershipId() != null) {
                    try {
                        com.gym.service.membership.MembershipService membershipService = 
                            new com.gym.service.membership.MembershipServiceImpl();
                        String reason = newStatus == PaymentStatus.REFUNDED ? "Payment refunded" : "Payment chargeback";
                        boolean suspended = membershipService.suspendMembership(payment.getMembershipId(), reason);
                        if (suspended) {
                            LOGGER.warning("⚠️ [" + newStatus + "→SUSPENDED] Suspended membership: membershipId=" + payment.getMembershipId());
                        } else {
                            LOGGER.warning("Failed to suspend membership: membershipId=" + payment.getMembershipId());
                        }
                    } catch (Exception e) {
                        LOGGER.severe("Error suspending membership: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
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
    
    @Override
    public BigDecimal getRevenueThisMonth() {
        return paymentDAO.getRevenueThisMonth();
    }
}

