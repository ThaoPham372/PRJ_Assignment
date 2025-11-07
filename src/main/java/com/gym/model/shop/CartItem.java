package com.gym.model.shop;

import jakarta.persistence.*;
import com.gym.model.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Cart item model - JPA Entity
 */
@Entity
@Table(name = "cart")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;
    
    @Column(name = "user_id")
    private Long userId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    
    @Column(name = "product_id")
    private Long productId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;
    
    @Column(name = "quantity")
    private Integer quantity;
    
    @Column(name = "added_at")
    private LocalDateTime addedAt;

    // View helpers (from product join) - @Transient
    @Transient
    private String productName;
    
    @Transient
    private BigDecimal price;
    
    @Transient
    private String unit;
    
    @Transient
    private String imagePath;
    
    @PrePersist
    protected void onCreate() {
        if (addedAt == null) {
            addedAt = LocalDateTime.now();
        }
    }

    public CartItem() {
    }

    public CartItem(Long cartId, Long userId, Long productId, Integer quantity, LocalDateTime addedAt) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.addedAt = addedAt;
    }
    
    /**
     * Backward compatibility: Constructor with OffsetDateTime
     */
    public CartItem(Long cartId, Long userId, Long productId, Integer quantity, java.time.OffsetDateTime addedAt) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        if (addedAt != null) {
            this.addedAt = addedAt.toLocalDateTime();
        }
    }

    // Getters and Setters
    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
    
    /**
     * Backward compatibility: Get addedAt as OffsetDateTime
     */
    public java.time.OffsetDateTime getAddedAtAsOffsetDateTime() {
        if (addedAt == null) {
            return null;
        }
        return addedAt.atOffset(java.time.ZoneOffset.UTC);
    }
    
    /**
     * Backward compatibility: Set addedAt from OffsetDateTime
     */
    public void setAddedAt(java.time.OffsetDateTime addedAt) {
        if (addedAt != null) {
            this.addedAt = addedAt.toLocalDateTime();
        } else {
            this.addedAt = null;
        }
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            this.productId = product.getProductId();
            // Load product info for display
            this.productName = product.getProductName();
            this.price = product.getPrice();
            this.unit = product.getUnit();
        }
    }

    // View helpers
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Calculate subtotal for this cart item
     */
    public BigDecimal getSubtotal() {
        if (quantity != null && price != null) {
            return price.multiply(BigDecimal.valueOf(quantity));
        }
        return BigDecimal.ZERO;
    }
}



