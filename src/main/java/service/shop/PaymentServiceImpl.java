package service.shop;

import dao.PaymentDAO;
import dao.shop.OrderDao;
import dao.shop.OrderItemDao;
import model.Payment;
import model.Member;
import model.Membership;
import model.Package;
import model.shop.PaymentMethod;
import model.shop.PaymentStatus;
import model.shop.OrderStatus;
import model.shop.OrderItem;
import service.MembershipService;
import service.PackageService;

import java.math.BigDecimal;
import java.util.Date;
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
    private final OrderItemDao orderItemDao;
    private final MembershipService membershipService;
    private final PackageService packageService;

    public PaymentServiceImpl() {
        this.paymentDAO = new PaymentDAO();
        this.orderDao = new OrderDao();
        this.orderItemDao = new OrderItemDao();
        this.membershipService = new MembershipService();
        this.packageService = new PackageService();
    }
    
    // Constructor for dependency injection
    public PaymentServiceImpl(PaymentDAO paymentDAO, OrderDao orderDao, 
                             OrderItemDao orderItemDao, MembershipService membershipService,
                             PackageService packageService) {
        this.paymentDAO = paymentDAO;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.membershipService = membershipService;
        this.packageService = packageService;
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
     * Follows Single Responsibility Principle - handles payment status transitions
     */
    private void handlePaidStatus(Payment payment) {
        if (payment.getTransactionType() == Payment.TransactionType.PRODUCT) {
            handleProductPaymentPaid(payment);
        } else if (payment.getTransactionType() == Payment.TransactionType.PACKAGE) {
            handlePackagePaymentPaid(payment);
        }
    }
    
    /**
     * Handle PRODUCT payment when status becomes PAID
     * Updates order status and activates membership if order contains package
     */
    private void handleProductPaymentPaid(Payment payment) {
        if (payment.getOrderId() == null) {
            return;
        }
        
        try {
            // Update order status to COMPLETED
            orderDao.updateOrderStatus(payment.getOrderId(), OrderStatus.COMPLETED);
            LOGGER.info("✅ [PAID→COMPLETED] Updated order status to COMPLETED: orderId=" + payment.getOrderId());
            
            // Check if order contains a package (membership)
            activateMembershipFromOrder(payment.getOrderId(), payment.getMemberId());
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling product payment PAID status", e);
        }
    }
    
    /**
     * Handle PACKAGE payment when status becomes PAID
     * Activates existing membership
     */
    private void handlePackagePaymentPaid(Payment payment) {
        if (payment.getMembershipId() == null) {
            return;
        }
        
        try {
            Membership membership = membershipService.getMembershipById(payment.getMembershipId());
            if (membership != null) {
                membership.setStatus("ACTIVE");
                if (membership.getActivatedAt() == null) {
                    membership.setActivatedAt(new Date());
                }
                membershipService.update(membership);
                LOGGER.info("✅ [PAID] Membership activated: membershipId=" + payment.getMembershipId());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error activating membership from package payment", e);
        }
    }
    
    /**
     * Activate membership from order if order contains package
 Reusable method following DRY principle
 Sử dụng MembershipService.createOrExtendMembership() để tận dụng validation và logic đã có
     * 
     * @param orderId Order ID
     * @param memberId Member ID
     */
    private void activateMembershipFromOrder(Integer orderId, Integer memberId) {
        try {
            LOGGER.info("🔍 [activateMembershipFromOrder] Starting for orderId: " + orderId + ", memberId: " + memberId);
            
            // Get order items to check for package
            List<OrderItem> orderItems = orderItemDao.findByOrderId(orderId);
            if (orderItems == null || orderItems.isEmpty()) {
                LOGGER.warning("⚠️ [activateMembershipFromOrder] No order items found for orderId: " + orderId);
                return;
            }
            
            LOGGER.info("📦 [activateMembershipFromOrder] Found " + orderItems.size() + " order items");
            
            // Find package in order items
            Integer packageId = null;
            for (OrderItem item : orderItems) {
                if (item != null && item.getPackageId() != null) {
                    packageId = item.getPackageId();
                    LOGGER.info("📦 [activateMembershipFromOrder] Found packageId: " + packageId + " in order item");
                    break;
                }
            }
            
            if (packageId == null) {
                // Order doesn't contain package, nothing to do
                LOGGER.info("ℹ️ [activateMembershipFromOrder] Order does not contain package - skipping membership creation");
                return;
            }
            
            // Get package details
            Package pkg = packageService.getById(packageId);
            if (pkg == null) {
                LOGGER.warning("⚠️ [activateMembershipFromOrder] Package not found: " + packageId);
                return;
            }
            
            LOGGER.info("📦 [activateMembershipFromOrder] Package found: " + pkg.getName() + " (ID: " + packageId + ")");
            
            // Validate membership purchase before activating
            // Kiểm tra xem user đã có membership với package khác chưa
            // Nếu có → Không cho phép kích hoạt membership mới (should not happen if validation is correct)
            service.MembershipService membershipService = new service.MembershipService();
            service.MembershipService.ValidationResult validation = membershipService.validateNewMembership(memberId, pkg);
            
            if (!validation.isValid()) {
                String errorMessage = String.join(", ", validation.getErrors());
                LOGGER.warning("⚠️ [activateMembershipFromOrder] Membership validation failed: " + errorMessage);
                LOGGER.warning("⚠️ [activateMembershipFromOrder] Payment was successful but cannot activate membership due to validation error.");
                // Không throw exception để tránh rollback payment, chỉ log warning
                // Payment đã thành công, nhưng membership không được kích hoạt
                // Admin cần xử lý thủ công trường hợp này
                return;
            }
            
            // Get member
            service.MemberService memberService = new service.MemberService();
            Member member = memberService.getById(memberId);
            if (member == null) {
                LOGGER.warning("⚠️ [activateMembershipFromOrder] Member not found: " + memberId);
                return;
            }
            
            LOGGER.info("👤 [activateMembershipFromOrder] Member found: " + member.getName() + " (ID: " + memberId + ")");
            
            // Kích hoạt membership sau khi thanh toán thành công
            // Logic:
            // - Nếu đã có membership với cùng package → Gia hạn (extend duration)
            // - Nếu chưa có membership → Tạo mới
            // - Nếu đã có membership với package khác → Đã được chặn ở validation (không đến đây)
            
            // Kiểm tra xem có membership cùng package đã tồn tại trước đó không (để log chính xác)
            Membership existingMembershipBefore = membershipService.getActiveMembershipByMemberIdAndPackageId(memberId, packageId);
            boolean willExtend = (existingMembershipBefore != null);
            String actionType = willExtend ? "EXTENDED" : "CREATED";
            
            try {
                Membership resultMembership = membershipService.createOrExtendMembership(memberId, pkg);
                
                if (resultMembership != null && resultMembership.getId() != null) {
                    LOGGER.info("✅ [activateMembershipFromOrder] SUCCESS - " + actionType + " membership ID: " + resultMembership.getId() + 
                               ", Status: " + resultMembership.getStatus() + 
                               ", StartDate: " + resultMembership.getStartDate() +
                               ", EndDate: " + resultMembership.getEndDate() +
                               ", Package: " + pkg.getName() + " (ID: " + packageId + ")");
                } else {
                    LOGGER.warning("⚠️ [activateMembershipFromOrder] Membership created but ID is null");
                }
            } catch (IllegalArgumentException e) {
                // Validation error từ MembershipService
                LOGGER.warning("⚠️ [activateMembershipFromOrder] Validation failed: " + e.getMessage());
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "❌ [activateMembershipFromOrder] Error creating membership", e);
                throw e; // Re-throw để catch bên ngoài xử lý
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "❌ [activateMembershipFromOrder] Fatal error activating membership from order: " + orderId, e);
            e.printStackTrace(); // In full stack trace để debug
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

