package com.gym.model;

import com.gym.model.shop.PaymentMethod;
import com.gym.model.shop.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Payment model - Represents a payment transaction
 * Maps to payments table in database
 */
public class Payment {
    private Integer paymentId;
    private Integer userId;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private PaymentMethod method;  // Use shop.PaymentMethod enum
    private PaymentStatus status;  // Use shop.PaymentStatus enum
    private String referenceId;  // Unique reference (e.g., MoMo transaction ID)
    private TransactionType transactionType;
    private Long membershipId;  // For PACKAGE transactions
    private Long orderId;       // For PRODUCT transactions
    private String notes;

    public Payment() {
    }

    // Getters and Setters
    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Long getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Long membershipId) {
        this.membershipId = membershipId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    /**
     * Convert shop.PaymentMethod to database ENUM value (UPPERCASE)
     */
    public static String methodToDbValue(PaymentMethod method) {
        if (method == null) return null;
        // Database uses UPPERCASE, enum uses lowercase
        switch (method) {
            case CASH: return "CASH";
            case CREDIT_CARD: return "CREDIT_CARD";
            case BANK_TRANSFER: return "BANK_TRANSFER";
            case MOMO: return "MOMO";
            case VNPAY: return "VNPAY";
            default: return method.getCode().toUpperCase();
        }
    }

    /**
     * Convert database ENUM value (UPPERCASE) to shop.PaymentMethod
     */
    public static PaymentMethod methodFromDbValue(String dbValue) {
        if (dbValue == null) return null;
        return PaymentMethod.fromCode(dbValue.toLowerCase());
    }

    /**
     * Convert shop.PaymentStatus to database ENUM value (UPPERCASE)
     */
    public static String statusToDbValue(PaymentStatus status) {
        if (status == null) return "PENDING";
        // Database uses UPPERCASE, enum uses lowercase
        switch (status) {
            case PENDING: return "PENDING";
            case PAID: return "PAID";
            case FAILED: return "FAILED";
            case REFUNDED: return "REFUNDED";
            default: return status.getCode().toUpperCase();
        }
    }

    /**
     * Convert database ENUM value (UPPERCASE) to shop.PaymentStatus
     */
    public static PaymentStatus statusFromDbValue(String dbValue) {
        if (dbValue == null) return PaymentStatus.PENDING;
        return PaymentStatus.fromCode(dbValue.toLowerCase());
    }
}

