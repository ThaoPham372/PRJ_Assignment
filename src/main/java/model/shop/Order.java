package model.shop;

import jakarta.persistence.*;
import model.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Order model - JPA Entity
 */
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;
    
    @Column(name = "member_id")
    private Integer memberId; //Member member; member.getId()
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private User user;
    
    @Column(name = "order_number", length = 50, unique = true)
    private String orderNumber;
    
    @Column(name = "order_date")
    private LocalDateTime orderDate;
    
    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;
    
    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", length = 50)
    private OrderStatus orderStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_method", length = 50)
    private DeliveryMethod deliveryMethod;
    
    @Column(name = "delivery_address", length = 500)
    private String deliveryAddress;
    
    @Column(name = "delivery_phone", length = 20)
    private String deliveryPhone;
    
    @Column(name = "notes", length = 1000)
    private String notes;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items;
    
    @Transient
    private PaymentMethod paymentMethod;
    
    @Transient
    private PaymentStatus paymentStatus;
    
    @Transient
    private String deliveryName;

    public Order() {
        this.items = new ArrayList<>();
        this.discountAmount = BigDecimal.ZERO;
        this.paymentStatus = PaymentStatus.PENDING;
        this.orderStatus = OrderStatus.PENDING;
    }

    // Getters and Setters
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
    
    /**
     * Backward compatibility: Get order date as OffsetDateTime
     */
    public java.time.OffsetDateTime getOrderDateAsOffsetDateTime() {
        if (orderDate == null) {
            return null;
        }
        return orderDate.atOffset(java.time.ZoneOffset.UTC);
    }
    
    /**
     * Backward compatibility: Set order date from OffsetDateTime
     */
    public void setOrderDate(java.time.OffsetDateTime orderDate) {
        if (orderDate != null) {
            this.orderDate = orderDate.toLocalDateTime();
        } else {
            this.orderDate = null;
        }
    }

    /**
     * Get order date as java.util.Date for JSP fmt:formatDate compatibility
     */
    public java.util.Date getOrderDateAsDate() {
        if (orderDate == null) {
            return null;
        }
        return java.util.Date.from(orderDate.atZone(java.time.ZoneId.systemDefault()).toInstant());
    }
    
    /**
     * Get createdAt as java.util.Date for JSP fmt:formatDate compatibility
     */
    public java.util.Date getCreatedAtAsDate() {
        if (createdAt == null) {
            return null;
        }
        return java.util.Date.from(createdAt.atZone(java.time.ZoneId.systemDefault()).toInstant());
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount != null ? discountAmount : BigDecimal.ZERO;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }
    
    /**
     * Calculate final amount after discount
     * @return totalAmount - discountAmount
     */
    public BigDecimal getFinalAmount() {
        if (totalAmount == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal discount = getDiscountAmount(); // Uses getDiscountAmount() which handles null
        return totalAmount.subtract(discount);
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * Backward compatibility: Get createdAt as OffsetDateTime
     */
    public java.time.OffsetDateTime getCreatedAtAsOffsetDateTime() {
        if (createdAt == null) {
            return null;
        }
        return createdAt.atOffset(java.time.ZoneOffset.UTC);
    }
    
    /**
     * Backward compatibility: Set createdAt from OffsetDateTime
     */
    public void setCreatedAt(java.time.OffsetDateTime createdAt) {
        if (createdAt != null) {
            this.createdAt = createdAt.toLocalDateTime();
        } else {
            this.createdAt = null;
        }
    }
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (orderDate == null) {
            orderDate = LocalDateTime.now();
        }
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items != null ? items : new ArrayList<>();
    }

    // Delivery information getters and setters
    // deliveryName is @Transient - get from User if needed
    public String getDeliveryName() {
        if (deliveryName != null) {
            return deliveryName;
        }
        if (user != null && user.getName() != null) {
            return user.getName();
        }
        return null;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    public void setDeliveryPhone(String deliveryPhone) {
        this.deliveryPhone = deliveryPhone;
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}