package com.gym.service.shop;

import com.gym.dto.CartItemDTO;
import com.gym.model.shop.CartItem;

import java.util.List;

/**
 * Service interface for Cart operations
 */
public interface CartService {
    /**
     * Get all cart items for a user (as DTOs with product details)
     */
    List<CartItemDTO> view(Long userId);
    
    /**
     * Add product to cart (or update quantity if exists)
     */
    void add(Long userId, Long productId, Integer quantity);
    
    /**
     * Update quantity for a cart item
     */
    void setQuantity(Long userId, Long productId, Integer quantity);
    
    /**
     * Remove item from cart
     */
    void remove(Long userId, Long productId);
    
    /**
     * Calculate total amount for cart items
     */
    java.math.BigDecimal calculateTotal(List<CartItemDTO> items);
}



