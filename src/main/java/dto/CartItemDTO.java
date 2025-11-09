package dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object for Cart Item
 * Used to transfer cart data between layers (DAO -> Service -> Controller -> View)
 * Follows DTO pattern to decouple model from view
 */
public class CartItemDTO {
    private Integer cartId;
    private Integer memberId;
    private Integer productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private String unit;
    private String imagePath;
    private BigDecimal subtotal;

    public CartItemDTO() {
    }

    public CartItemDTO(Integer cartId, Integer memberId, Integer productId, 
                       String productName, Integer quantity, BigDecimal price, 
                       String unit, String imagePath) {
        this.cartId = cartId;
        this.memberId = memberId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.unit = unit;
        this.imagePath = imagePath;
        this.subtotal = calculateSubtotal();
    }

    // Getters and Setters
    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        // Recalculate subtotal when quantity changes
        this.subtotal = calculateSubtotal();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
        // Recalculate subtotal when price changes
        this.subtotal = calculateSubtotal();
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

    public BigDecimal getSubtotal() {
        if (subtotal == null) {
            subtotal = calculateSubtotal();
        }
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * Calculate subtotal for this cart item
     */
    private BigDecimal calculateSubtotal() {
        if (quantity != null && price != null) {
            return price.multiply(BigDecimal.valueOf(quantity));
        }
        return BigDecimal.ZERO;
    }
}

