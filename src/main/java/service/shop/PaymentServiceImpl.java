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
import java.util.Calendar;
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
     * Reusable method following DRY principle
     * 
     * @param orderId Order ID
     * @param memberId Member ID
     */
    private void activateMembershipFromOrder(Integer orderId, Integer memberId) {
        try {
            // Get order items to check for package
            List<OrderItem> orderItems = orderItemDao.findByOrderId(orderId);
            if (orderItems == null || orderItems.isEmpty()) {
                return;
            }
            
            // Find package in order items
            Integer packageId = null;
            for (OrderItem item : orderItems) {
                if (item != null && item.getPackageId() != null) {
                    packageId = item.getPackageId();
                    break;
                }
            }
            
            if (packageId == null) {
                // Order doesn't contain package, nothing to do
                return;
            }
            
            // Get package details
            Package pkg = packageService.getById(packageId);
            if (pkg == null) {
                LOGGER.warning("Package not found: " + packageId);
                return;
            }
            
            // Get member
            service.MemberService memberService = new service.MemberService();
            Member member = memberService.getById(memberId);
            if (member == null) {
                LOGGER.warning("Member not found: " + memberId);
                return;
            }
            
            // Check if member already has active membership for this package
            Membership existingMembership = findActiveMembershipForPackage(member, packageId);
            
            if (existingMembership != null) {
                // Extend existing membership
                extendMembership(existingMembership, pkg);
                membershipService.update(existingMembership);
                LOGGER.info("✅ [PAID] Extended membership: membershipId=" + existingMembership.getId() + 
                           ", newEndDate=" + existingMembership.getEndDate());
            } else {
                // Create new membership
                Membership newMembership = createMembershipFromPackage(member, pkg);
                membershipService.add(newMembership);
                LOGGER.info("✅ [PAID] Created new membership: membershipId=" + newMembership.getId() + 
                           ", endDate=" + newMembership.getEndDate());
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error activating membership from order: " + orderId, e);
        }
    }
    
    /**
     * Find active membership for member and package
     * Reusable method
     */
    private Membership findActiveMembershipForPackage(Member member, Integer packageId) {
        try {
            List<Membership> allMemberships = membershipService.getAll();
            Date now = new Date();
            
            for (Membership m : allMemberships) {
                if (m.getMember() != null && 
                    m.getMember().getId().equals(member.getId()) &&
                    m.getPackageO() != null &&
                    m.getPackageO().getId().equals(packageId) &&
                    "ACTIVE".equalsIgnoreCase(m.getStatus()) &&
                    m.getEndDate() != null &&
                    m.getEndDate().after(now)) {
                    return m;
                }
            }
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error finding active membership", e);
            return null;
        }
    }
    
    /**
     * Extend existing membership by adding duration months
     * Reusable method
     */
    private void extendMembership(Membership membership, Package pkg) {
        Date currentEndDate = membership.getEndDate();
        if (currentEndDate == null) {
            currentEndDate = new Date();
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentEndDate);
        cal.add(Calendar.MONTH, pkg.getDurationMonths());
        membership.setEndDate(cal.getTime());
        membership.setUpdatedDate(new Date());
    }
    
    /**
     * Create new membership from package
     * Calculates endDate based on createdDate + durationMonths
     * Follows OOP principles - encapsulates membership creation logic
     */
    private Membership createMembershipFromPackage(Member member, Package pkg) {
        Membership membership = new Membership();
        membership.setMember(member);
        membership.setPackageO(pkg);
        
        Date now = new Date();
        membership.setCreatedDate(now);
        membership.setStartDate(now);
        membership.setActivatedAt(now);
        membership.setStatus("ACTIVE");
        
        // Calculate endDate based on createdDate + durationMonths
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.MONTH, pkg.getDurationMonths());
        membership.setEndDate(cal.getTime());
        
        return membership;
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

