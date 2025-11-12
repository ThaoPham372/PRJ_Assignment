package model;

import model.shop.PaymentMethod;
import model.shop.PaymentStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Payment model - Represents a payment transaction
 * Maps to payments table in database - JPA Entity
 */
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;
    
    @Column(name = "member_id")
    private Integer memberId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private User user;
    
    @Column(name = "amount", precision = 15, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "method", length = 50)
    private PaymentMethod method;  // Use shop.PaymentMethod enum
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private PaymentStatus status;  // Use shop.PaymentStatus enum
    
    @Column(name = "reference_id", length = 255)
    private String referenceId;  // Unique reference (e.g., MoMo transaction ID)
    
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", length = 50)
    private TransactionType transactionType;
    
    @Column(name = "membership_id")
    private Integer membershipId;  // For PACKAGE transactions
    
    @Column(name = "order_id")
    private Integer orderId;       // For PRODUCT transactions
    
    @Column(name = "notes", length = 1000)
    private String notes;
    
    @Column(name = "paid_at")
    private Date paidAt;  // Timestamp when payment was confirmed (status = PAID)
    
    @Column(name = "external_ref", length = 255)
    private String externalRef;  // External reference from payment gateway (VNPay, MoMo, etc.)
    
    @PrePersist
    protected void onCreate() {
        if (paymentDate == null) {
            paymentDate = LocalDateTime.now();
        }
    }

    public Payment() {
    }

    // Getters and Setters
    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * Get payment date as java.util.Date for JSP fmt:formatDate compatibility
     */
    public java.util.Date getPaymentDateAsDate() {
        if (paymentDate == null) {
            return null;
        }
        return java.util.Date.from(paymentDate.atZone(java.time.ZoneId.systemDefault()).toInstant());
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Integer membershipId) {
        this.membershipId = membershipId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Date getPaidAt() {
        return paidAt;
    }
    
    public void setPaidAt(Date paidAt) {
        this.paidAt = paidAt;
    }
    
    public String getExternalRef() {
        return externalRef;
    }
    
    public void setExternalRef(String externalRef) {
        this.externalRef = externalRef;
    }

    /**
     * Transaction Type enum
     */
    public enum TransactionType {
        PACKAGE("PACKAGE", "Gói tập"),
        PRODUCT("PRODUCT", "Sản phẩm");

        private final String code;
        private final String displayName;

        TransactionType(String code, String displayName) {
            this.code = code;
            this.displayName = displayName;
        }

        public String getCode() {
            return code;
        }

        public String getDisplayName() {
            return displayName;
        }

        public static TransactionType fromCode(String code) {
            if (code == null) return null;
            for (TransactionType type : values()) {
                if (type.code.equalsIgnoreCase(code)) {
                    return type;
                }
            }
            return null;
        }
    }

}