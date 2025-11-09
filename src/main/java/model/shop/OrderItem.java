package model.shop;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Order item model (snapshot at order time) - JPA Entity
 */
@Entity
@Table(name = "order_details")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Integer orderItemId;  // Maps to order_detail_id in DB (keeping field name for compatibility)
    
    @Column(name = "order_id")
    private Integer orderId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;
    
    @Column(name = "product_id")
    private Integer productId;
    
    @Column(name = "package_id")
    private Integer packageId;  // For membership packages
    
    @Column(name = "product_name", length = 255)
    private String productName;
    
    @Column(name = "quantity")
    private Integer quantity;
    
    @Column(name = "unit_price", precision = 15, scale = 2)
    private BigDecimal unitPrice;
    
    @Column(name = "discount_percent", precision = 5, scale = 2)
    private BigDecimal discountPercent;  // New column in order_details
    
    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount;
    
    @Column(name = "subtotal", precision = 15, scale = 2, insertable = false, updatable = false)
    private BigDecimal subtotal; // DB computed (GENERATED column)
    
    @Column(name = "notes", length = 500)
    private String notes;  // New column in order_details

    public OrderItem() {
    }

    public OrderItem(Integer orderItemId, Integer orderId, Integer productId, String productName,
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
    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
    
    public Order getOrder() {
        return order;
    }
    
    public void setOrder(Order order) {
        this.order = order;
        if (order != null) {
            this.orderId = order.getOrderId();
        }
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
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



