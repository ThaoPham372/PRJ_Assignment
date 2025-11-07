package com.gym.service.shop;

/**
 * Exception thrown when product stock is insufficient
 */
public class InsufficientStockException extends Exception {
    private final Long productId;
    
    public InsufficientStockException(String message, Long productId) {
        super(message);
        this.productId = productId;
    }
    
    public Long getProductId() {
        return productId;
    }
}



