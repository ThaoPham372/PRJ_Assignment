package model.shop;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Product entity representing a record in the products table.
 */
@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;
    
    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false, length = 50)
    private ProductType productType;
    
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(name = "cost_price", precision = 10, scale = 2)
    private BigDecimal costPrice;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;
    
    @Column(name = "unit", length = 20)
    private String unit;
    
    @Column(name = "is_active", nullable = false)
    private boolean active;
    
    @Column(name = "import_date")
    private LocalDateTime importDate;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Transient field for backward compatibility (not in database schema)
    @Transient
    private String imagePath;

    public Product() {
        this.active = true;
        this.stockQuantity = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Product(String productName, ProductType productType, BigDecimal price,
                   Integer stockQuantity, String unit, boolean active) {
        this();
        this.productName = productName;
        this.productType = productType;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.unit = unit;
        this.active = active;
    }

    // Getters and Setters
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
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

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
    
    // Alias for JPA compatibility
    public Boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getImportDate() {
        return importDate;
    }

    public void setImportDate(LocalDateTime importDate) {
        this.importDate = importDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Backward compatibility methods
    @Transient
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    // Backward compatibility: convert LocalDateTime to OffsetDateTime
    public java.time.OffsetDateTime getCreatedAtAsOffsetDateTime() {
        return createdAt != null ? createdAt.atOffset(java.time.ZoneOffset.UTC) : null;
    }

    public void setCreatedAtAsOffsetDateTime(java.time.OffsetDateTime createdAt) {
        this.createdAt = createdAt != null ? createdAt.toLocalDateTime() : null;
    }

    // Helper methods
    public boolean isInStock() {
        return stockQuantity != null && stockQuantity > 0;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
