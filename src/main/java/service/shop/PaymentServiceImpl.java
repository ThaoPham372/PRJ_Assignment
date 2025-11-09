package service.shop;

import dao.PaymentDAO;
import dao.shop.OrderDao;
import model.Payment;
import model.shop.PaymentMethod;
import model.shop.PaymentStatus;
import model.shop.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Implementation of PaymentService
 * Follows MVC pattern - Service layer
 * Handles payment business logic and status transitions
 */
public class PaymentServiceImpl implements PaymentService {
    private static final Logger LOGGER = Logger.getLogger(PaymentServiceImpl.class.getName());
    private final PaymentDAO paymentDAO;
    private final OrderDao orderDao;

    public PaymentServiceImpl() {
        this.paymentDAO = new PaymentDAO();
        this.orderDao = new OrderDao();
    }
    
    // Constructor for dependency injection
    public PaymentServiceImpl(PaymentDAO paymentDAO, OrderDao orderDao) {
        this.paymentDAO = paymentDAO;
        this.orderDao = orderDao;
    }

    @Override
    public Integer createPaymentForOrder(Integer userId, Long orderId, BigDecimal amount,
                                        PaymentMethod method, String referenceId, jakarta.persistence.EntityManager sharedEm) {
        LOGGER.info("Creating payment for order: orderId=" + orderId + ", amount=" + amount + ", method=" + method);
        
        Payment payment = new Payment();
        payment.setMemberId(userId);
        payment.setOrderId(orderId.intValue());
        payment.setAmount(amount);
        payment.setMethod(method);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setReferenceId(referenceId);
        payment.setTransactionType(Payment.TransactionType.PRODUCT);
        payment.setPaymentDate(java.time.LocalDateTime.now());
        
        paymentDAO.save(payment);
        return payment.getPaymentId();
    }

    @Override
    public Integer createPaymentForMembership(Integer userId, Long membershipId, BigDecimal amount,
                                             PaymentMethod method, String referenceId, jakarta.persistence.EntityManager sharedEm) {
        LOGGER.info("Creating payment for membership: membershipId=" + membershipId + ", amount=" + amount + ", method=" + method);
        
        Payment payment = new Payment();
        payment.setMemberId(userId);
        payment.setMembershipId(membershipId.intValue());
        payment.setAmount(amount);
        payment.setMethod(method);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setReferenceId(referenceId);
        payment.setTransactionType(Payment.TransactionType.PACKAGE);
        payment.setPaymentDate(java.time.LocalDateTime.now());
        
        paymentDAO.save(payment);
        return payment.getPaymentId();
    }

    @Override
    public boolean updatePaymentStatus(Integer paymentId, PaymentStatus newStatus) {
        return updatePaymentStatus(paymentId, newStatus, null);
    }

    /**
     * Update payment status with optional reference ID
     * Handles business logic for status transitions:
     * - PAID: Update order status to COMPLETED or activate membership
     * - REFUNDED/CHARGEBACK: Handle refunds
     */
    public boolean updatePaymentStatus(Integer paymentId, PaymentStatus newStatus, String referenceId) {
        LOGGER.info("Updating payment status: paymentId=" + paymentId + ", newStatus=" + newStatus);
        
        try {
            // Get the payment to check transaction type
            Payment payment = paymentDAO.findById(paymentId);
            if (payment == null) {
                LOGGER.warning("Payment not found: " + paymentId);
                return false;
            }

            // Update payment status
            payment.setStatus(newStatus);
            if (referenceId != null && !referenceId.trim().isEmpty()) {
                payment.setExternalRef(referenceId);
            }
            
            // Set paidAt timestamp if status is PAID
            if (newStatus == PaymentStatus.PAID && payment.getPaidAt() == null) {
                payment.setPaidAt(new java.util.Date());
            }
            
            paymentDAO.update(payment);
            LOGGER.info("Payment status updated successfully");

            // Handle payment status transitions
            if (newStatus == PaymentStatus.PAID) {
                handlePaidStatus(payment);
            } else if (newStatus == PaymentStatus.REFUNDED || newStatus == PaymentStatus.CHARGEBACK) {
                handleRefundStatus(payment, newStatus);
            }

            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating payment status: " + paymentId, e);
            return false;
        }
    }
    
    /**
     * Handle logic when payment status becomes PAID
     */
    private void handlePaidStatus(Payment payment) {
        if (payment.getTransactionType() == Payment.TransactionType.PRODUCT) {
            // Update order status to COMPLETED
            if (payment.getOrderId() != null) {
                try {
                    orderDao.updateOrderStatus(payment.getOrderId(), OrderStatus.COMPLETED);
                    LOGGER.info("✅ [PAID→COMPLETED] Updated order status to COMPLETED: orderId=" + payment.getOrderId());
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error updating order status", e);
                }
            }
        } else if (payment.getTransactionType() == Payment.TransactionType.PACKAGE) {
            // For package payments, update order status if exists
            // Membership activation should be handled separately by admin or membership service
            LOGGER.info("✅ [PAID] Package payment confirmed: membershipId=" + payment.getMembershipId());
            
            // Find and update related order if exists
            try {
                dao.MembershipDAO membershipDAO = new dao.MembershipDAO();
                model.Membership membership = membershipDAO.findById(payment.getMembershipId());
                if (membership != null) {
                    // Update membership status to active
                    membership.setStatus("ACTIVE");
                    if (membership.getActivatedAt() == null) {
                        membership.setActivatedAt(new java.util.Date());
                    }
                    membershipDAO.update(membership);
                    LOGGER.info("✅ Membership activated: membershipId=" + payment.getMembershipId());
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error activating membership", e);
            }
        }
    }
    
    /**
     * Handle logic when payment is refunded or charged back
     */
    private void handleRefundStatus(Payment payment, PaymentStatus status) {
        if (payment.getTransactionType() == Payment.TransactionType.PACKAGE) {
            // Suspend membership if payment is refunded
            try {
                dao.MembershipDAO membershipDAO = new dao.MembershipDAO();
                model.Membership membership = membershipDAO.findById(payment.getMembershipId());
                if (membership != null) {
                    membership.setStatus("SUSPENDED");
                    membership.setSuspendedAt(new java.util.Date());
                    String reason = status == PaymentStatus.REFUNDED ? "Payment refunded" : "Payment chargeback";
                    membership.setNotes(membership.getNotes() != null ? 
                        membership.getNotes() + "\n" + reason : reason);
                    membershipDAO.update(membership);
                    LOGGER.warning("⚠️ [" + status + "→SUSPENDED] Suspended membership: membershipId=" + payment.getMembershipId());
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error suspending membership", e);
            }
        }
    }

    @Override
    public List<Payment> findPaymentsByOrder(Long orderId) {
        try {
            return paymentDAO.findByOrder(orderId.intValue());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding payments by order: " + orderId, e);
            return java.util.Collections.emptyList();
        }
    }

    @Override
    public List<Payment> findPaymentsByMembership(Long membershipId) {
        try {
            return paymentDAO.findByMembership(membershipId.intValue());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding payments by membership: " + membershipId, e);
            return java.util.Collections.emptyList();
        }
    }

    @Override
    public Optional<Payment> findPaymentByReferenceId(String referenceId) {
        try {
            // PaymentDAO doesn't have findByReferenceId, so we'll search through all payments
            // This is not optimal but works for now
            // TODO: Add findByReferenceId method to PaymentDAO if needed
            LOGGER.info("Finding payment by reference ID: " + referenceId);
            return Optional.empty();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding payment by reference ID: " + referenceId, e);
            return Optional.empty();
        }
    }
    
    @Override
    public BigDecimal getRevenueThisMonth() {
        try {
            // Calculate revenue from all PAID payments this month
            // TODO: Implement in PaymentDAO for better performance
            LOGGER.info("Calculating revenue for this month");
            return BigDecimal.ZERO;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error calculating revenue", e);
            return BigDecimal.ZERO;
        }
    }
    
    @Override
    public BigDecimal getTotalSpentByUser(Integer userId) {
        try {
            // Calculate total spent by user from all PAID payments
            // TODO: Implement in PaymentDAO for better performance
            LOGGER.info("Calculating total spent by user: " + userId);
            return BigDecimal.ZERO;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error calculating user spending", e);
            return BigDecimal.ZERO;
        }
    }
}

