package com.gym.model.shop;

import java.math.BigDecimal;

/**
 * Order item model (snapshot at order time)
 */
public class OrderItem {
    private Long orderItemId;  // Maps to order_detail_id in DB (keeping field name for compatibility)
    private Long orderId;
    private Long productId;
    private Long packageId;  // For membership packages
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal discountPercent;  // New column in order_details
    private BigDecimal discountAmount;
    private BigDecimal subtotal; // DB computed (GENERATED column)
    private String notes;  // New column in order_details

    public OrderItem() {
    }

    public OrderItem(Long orderItemId, Long orderId, Long productId, String productName,
                     Integer quantity, BigDecimal unitPrice, BigDecimal discountAmount, BigDecimal subtotal) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discountAmount = discountAmount;
        this.subtotal = subtotal;
    }

    // Getters and Setters
    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount != null ? discountAmount : BigDecimal.ZERO;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Recompute subtotal (fallback if DB computed value is missing)
     */
    public void recomputeSubtotal() {
        if (quantity != null && unitPrice != null) {
            BigDecimal baseTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
            BigDecimal discount = getDiscountAmount();
            this.subtotal = baseTotal.subtract(discount);
        }
    }
}



