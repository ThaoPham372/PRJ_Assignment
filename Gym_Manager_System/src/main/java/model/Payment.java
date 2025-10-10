package model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Payment model - Đại diện cho giao dịch thanh toán
 */
public class Payment {
    private int paymentId;
    private int memberId; // Foreign key to Member
    private Integer membershipPackageId; // Foreign key to MembershipPackage
    
    // Thông tin thanh toán
    private String paymentCode; // Mã giao dịch
    private String paymentType; // membership, personal_training, product, service, penalty
    private BigDecimal amount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private String currency; // VND, USD
    
    // Phương thức thanh toán
    private String paymentMethod; // cash, credit_card, debit_card, bank_transfer, e_wallet, qr_code
    private String paymentStatus; // pending, completed, failed, refunded, cancelled
    
    // Thông tin giao dịch
    private Date paymentDate;
    private Date dueDate;
    private String transactionId; // ID từ payment gateway
    private String bankTransactionId;
    
    // Thông tin hoàn tiền
    private Boolean isRefunded;
    private BigDecimal refundAmount;
    private Date refundDate;
    private String refundReason;
    
    // Ghi chú
    private String description;
    private String notes;
    private String receiptUrl; // Link đến hóa đơn PDF
    
    // Metadata
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;

    // Constructors
    public Payment() {
        this.paymentStatus = "pending";
        this.currency = "VND";
        this.isRefunded = false;
        this.createdAt = new Date();
    }

    public Payment(int memberId, BigDecimal amount, String paymentType, String paymentMethod) {
        this();
        this.memberId = memberId;
        this.amount = amount;
        this.finalAmount = amount;
        this.paymentType = paymentType;
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters
    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public Integer getMembershipPackageId() {
        return membershipPackageId;
    }

    public void setMembershipPackageId(Integer membershipPackageId) {
        this.membershipPackageId = membershipPackageId;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
        calculateFinalAmount();
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getBankTransactionId() {
        return bankTransactionId;
    }

    public void setBankTransactionId(String bankTransactionId) {
        this.bankTransactionId = bankTransactionId;
    }

    public Boolean getIsRefunded() {
        return isRefunded;
    }

    public void setIsRefunded(Boolean isRefunded) {
        this.isRefunded = isRefunded;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Date getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(Date refundDate) {
        this.refundDate = refundDate;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    // Business logic methods
    private void calculateFinalAmount() {
        if (amount != null) {
            BigDecimal discount = discountAmount != null ? discountAmount : BigDecimal.ZERO;
            this.finalAmount = amount.subtract(discount);
        }
    }

    public void completePayment() {
        this.paymentStatus = "completed";
        this.paymentDate = new Date();
    }

    public void refund(BigDecimal refundAmount, String reason) {
        this.isRefunded = true;
        this.refundAmount = refundAmount;
        this.refundDate = new Date();
        this.refundReason = reason;
        this.paymentStatus = "refunded";
    }

    public boolean isOverdue() {
        if (dueDate == null || "completed".equals(paymentStatus)) {
            return false;
        }
        return dueDate.before(new Date());
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", paymentCode='" + paymentCode + '\'' +
                ", amount=" + amount +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}

