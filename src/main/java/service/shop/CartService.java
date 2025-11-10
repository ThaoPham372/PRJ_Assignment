package service.shop;

import dto.CartItemDTO;
import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for Cart operations
 * Follows Interface Segregation Principle - defines contract for cart operations
 */
public interface CartService {
    
    /**
     * View all cart items for a user
     * @param userId User ID
     * @return List of cart items as DTOs
     */
    List<CartItemDTO> view(Long userId);
    
    /**
     * Add product to cart or update quantity if already exists
     * @param userId User ID
     * @param productId Product ID
     * @param quantity Quantity to add
     * @throws RuntimeException if product not found or invalid quantity
     */
    void add(Long userId, Long productId, Integer quantity);
    
    /**
     * Update cart item quantity
     * @param userId User ID
     * @param productId Product ID
     * @param quantity New quantity
     * @throws RuntimeException if cart item not found
     */
    void setQuantity(Long userId, Long productId, Integer quantity);
    
    /**
     * Remove product from cart
     * @param userId User ID
     * @param productId Product ID
     */
    void remove(Long userId, Long productId);
    
    /**
     * Clear all items from cart
     * @param userId User ID
     */
    void clear(Long userId);
    
    /**
     * Calculate total amount for cart items
     * @param cartItems List of cart items
     * @return Total amount
     */
    BigDecimal calculateTotal(List<CartItemDTO> cartItems);
    
    /**
     * Get cart item count for a user
     * @param userId User ID
     * @return Number of items in cart
     */
    int getCartItemCount(Long userId);
}

