package com.gym.model.shop;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Product model
 */
public class Product {
    private Long productId;
    private String productName;
    private ProductType productType;
    private BigDecimal price;
    private Integer stockQuantity;
    private String unit;
    private boolean active;
    private String imagePath;
    private OffsetDateTime createdAt;

    public Product() {
    }

    public Product(Long productId, String productName, ProductType productType, BigDecimal price,
                   Integer stockQuantity, String unit, boolean active, String imagePath, OffsetDateTime createdAt) {
        this.productId = productId;
        this.productName = productName;
        this.productType = productType;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.unit = unit;
        this.active = active;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isInStock() {
        return stockQuantity != null && stockQuantity > 0;
    }
}



