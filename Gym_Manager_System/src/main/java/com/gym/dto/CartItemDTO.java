package com.gym.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for Cart Item with product details
 * Used to transfer cart data from DAO to View without JPA entity issues
 */
public class CartItemDTO {
    private Long cartId;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private LocalDateTime addedAt;
    
    // Product details
    private String productName;
    private BigDecimal price;
    private String unit;
    private Boolean active;
    
    // Calculated field
    private BigDecimal subtotal;
    
    public CartItemDTO() {
    }
    
    public CartItemDTO(Long cartId, Long userId, Long productId, Integer quantity, 
                       LocalDateTime addedAt, String productName, BigDecimal price, 
                       String unit, Boolean active) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.addedAt = addedAt;
        this.productName = productName;
        this.price = price;
        this.unit = unit;
        this.active = active;
        this.subtotal = calculateSubtotal();
    }
    
    private BigDecimal calculateSubtotal() {
        if (price != null && quantity != null) {
            return price.multiply(BigDecimal.valueOf(quantity));
        }
        return BigDecimal.ZERO;
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
        this.subtotal = calculateSubtotal();
    }
    
    public LocalDateTime getAddedAt() {
        return addedAt;
    }
    
    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
    
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
        this.subtotal = calculateSubtotal();
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
    
    public BigDecimal getSubtotal() {
        if (subtotal == null) {
            subtotal = calculateSubtotal();
        }
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    @Override
    public String toString() {
        return "CartItemDTO{" +
                "cartId=" + cartId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", subtotal=" + subtotal +
                '}';
    }
}

