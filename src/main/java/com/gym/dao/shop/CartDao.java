package com.gym.dao.shop;

import com.gym.dao.BaseDAO;
import com.gym.dto.CartItemDTO;
import com.gym.model.shop.CartItem;
import com.gym.model.shop.Product;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for Cart operations - JPA Implementation
 */
public class CartDao extends BaseDAO<CartItem> {
    private static final Logger LOGGER = Logger.getLogger(CartDao.class.getName());

    public CartDao() {
        super();
    }

    /**
     * Get all cart items for a user (with product details) as DTOs for VIEW
     * ✅ FIX: Use DTO to avoid JPA transient field issues
     */
    public List<CartItemDTO> findByUserIdAsDTO(Long userId) {
        try {
            System.out.println("[CartDao] ===== Finding cart items (DTO) for user: " + userId + " =====");
            
            // Use JPQL constructor expression to directly create DTOs
            String jpql = "SELECT NEW com.gym.dto.CartItemDTO(" +
                         "c.cartId, c.userId, c.productId, c.quantity, c.addedAt, " +
                         "p.productName, p.price, p.unit, p.active) " +
                         "FROM CartItem c " +
                         "LEFT JOIN c.product p " +
                         "WHERE c.userId = :userId " +
                         "AND (p.active = true OR p.active IS NULL) " +
                         "ORDER BY c.addedAt DESC";
            
            TypedQuery<CartItemDTO> query = em.createQuery(jpql, CartItemDTO.class);
            query.setParameter("userId", userId);
            
            List<CartItemDTO> items = query.getResultList();
            System.out.println("[CartDao] Found " + items.size() + " cart items (DTO)");
            
            // Debug logging
            for (CartItemDTO item : items) {
                System.out.println("[CartDao] DTO: " + item.getProductName() + 
                                 " | Price: " + item.getPrice() + 
                                 " | Qty: " + item.getQuantity() + 
                                 " | Subtotal: " + item.getSubtotal());
            }
            
            return items;
        } catch (Exception e) {
            System.err.println("[CartDao] ERROR finding cart items: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Get all cart items for a user (as entities) for CHECKOUT processing
     */
    public List<CartItem> findByUserId(Long userId) {
        try {
            String jpql = "SELECT c FROM CartItem c " +
                         "LEFT JOIN FETCH c.product p " +
                         "WHERE c.userId = :userId " +
                         "ORDER BY c.addedAt DESC";
            
            TypedQuery<CartItem> query = em.createQuery(jpql, CartItem.class);
            query.setParameter("userId", userId);
            
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("[CartDao] ERROR finding cart items: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Add or update cart item (INSERT ... ON DUPLICATE KEY UPDATE for MySQL)
     */
    public void addOrUpdate(Long userId, Long productId, Integer quantity) {
        try {
            beginTransaction();
            
            // Try to find existing cart item
            String jpql = "SELECT c FROM CartItem c WHERE c.userId = :userId AND c.productId = :productId";
            TypedQuery<CartItem> query = em.createQuery(jpql, CartItem.class);
            query.setParameter("userId", userId);
            query.setParameter("productId", productId);
            
            CartItem existingItem;
            try {
                existingItem = query.getSingleResult();
                // Update quantity
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                existingItem.setAddedAt(java.time.LocalDateTime.now());
                em.merge(existingItem);
            } catch (NoResultException e) {
                // Create new item
                existingItem = new CartItem();
                existingItem.setUserId(userId);
                existingItem.setProductId(productId);
                existingItem.setQuantity(quantity);
                existingItem.setAddedAt(java.time.LocalDateTime.now());
                em.persist(existingItem);
            }
            
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, "Error adding/updating cart item", e);
            throw new RuntimeException("Failed to update cart", e);
        }
    }

    /**
     * Update quantity for a cart item
     */
    public void updateQuantity(Long userId, Long productId, Integer quantity) {
        try {
            beginTransaction();
            
            String jpql = "SELECT c FROM CartItem c WHERE c.userId = :userId AND c.productId = :productId";
            TypedQuery<CartItem> query = em.createQuery(jpql, CartItem.class);
            query.setParameter("userId", userId);
            query.setParameter("productId", productId);
            
            try {
                CartItem item = query.getSingleResult();
                item.setQuantity(quantity);
                em.merge(item);
                commitTransaction();
            } catch (NoResultException e) {
                rollbackTransaction();
                throw new RuntimeException("Cart item not found", e);
            }
        } catch (Exception e) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, "Error updating cart item quantity", e);
            throw new RuntimeException("Failed to update cart quantity", e);
        }
    }

    /**
     * Remove item from cart
     */
    public void remove(Long userId, Long productId) {
        try {
            beginTransaction();
            
            String jpql = "SELECT c FROM CartItem c WHERE c.userId = :userId AND c.productId = :productId";
            TypedQuery<CartItem> query = em.createQuery(jpql, CartItem.class);
            query.setParameter("userId", userId);
            query.setParameter("productId", productId);
            
            try {
                CartItem item = query.getSingleResult();
                em.remove(item);
                commitTransaction();
            } catch (NoResultException e) {
                rollbackTransaction();
                // Item not found, that's OK
            }
        } catch (Exception e) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, "Error removing cart item", e);
            throw new RuntimeException("Failed to remove cart item", e);
        }
    }

    /**
     * Clear all items for a user
     */
    public void clear(Long userId) {
        clear(userId, null);
    }

    /**
     * Clear all items for a user using provided EntityManager (for transactions)
     */
    public void clear(Long userId, EntityManager sharedEm) {
        EntityManager emToUse = sharedEm != null ? sharedEm : em;
        
        try {
            if (sharedEm == null) {
                beginTransaction();
            }
            
            String jpql = "DELETE FROM CartItem c WHERE c.userId = :userId";
            Query query = emToUse.createQuery(jpql);
            query.setParameter("userId", userId);
            query.executeUpdate();
            
            if (sharedEm == null) {
                commitTransaction();
            }
        } catch (Exception e) {
            if (sharedEm == null) {
                rollbackTransaction();
            }
            LOGGER.log(Level.SEVERE, "Error clearing cart", e);
            throw new RuntimeException("Failed to clear cart", e);
        }
    }
    
}
