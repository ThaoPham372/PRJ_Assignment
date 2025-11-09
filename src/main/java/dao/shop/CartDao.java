package dao.shop;

import dao.GenericDAO;
import model.shop.CartItem;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for Cart operations - extends GenericDAO for CRUD
 */
public class CartDao extends GenericDAO<CartItem> {
    private static final Logger LOGGER = Logger.getLogger(CartDao.class.getName());

    public CartDao() {
        super(CartItem.class);
    }

    /**
     * Get all cart items for a member (with product details loaded)
     */
    public List<CartItem> findByMemberId(Integer memberId) {
        try {
            String jpql = "SELECT c FROM CartItem c " +
                         "LEFT JOIN FETCH c.product p " +
                         "WHERE c.memberId = :memberId " +
                         "ORDER BY c.addedAt DESC";
            
            TypedQuery<CartItem> query = em.createQuery(jpql, CartItem.class);
            query.setParameter("memberId", memberId);
            
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding cart items for member: " + memberId, e);
            return new ArrayList<>();
        }
    }

    /**
     * Add or update cart item
     */
    public void addOrUpdate(Integer memberId, Integer productId, Integer quantity) {
        try {
            beginTransaction();
            
            // Try to find existing cart item
            String jpql = "SELECT c FROM CartItem c WHERE c.memberId = :memberId AND c.productId = :productId";
            TypedQuery<CartItem> query = em.createQuery(jpql, CartItem.class);
            query.setParameter("memberId", memberId);
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
                existingItem.setMemberId(memberId);
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
    public void updateQuantity(Integer memberId, Integer productId, Integer quantity) {
        try {
            beginTransaction();
            
            String jpql = "SELECT c FROM CartItem c WHERE c.memberId = :memberId AND c.productId = :productId";
            TypedQuery<CartItem> query = em.createQuery(jpql, CartItem.class);
            query.setParameter("memberId", memberId);
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
    public void remove(Integer memberId, Integer productId) {
        try {
            beginTransaction();
            
            String jpql = "SELECT c FROM CartItem c WHERE c.memberId = :memberId AND c.productId = :productId";
            TypedQuery<CartItem> query = em.createQuery(jpql, CartItem.class);
            query.setParameter("memberId", memberId);
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
     * Clear all items for a member
     */
    public void clear(Integer memberId) {
        clear(memberId, null);
    }

    /**
     * Clear all items for a member using provided EntityManager (for transactions)
     */
    public void clear(Integer memberId, EntityManager sharedEm) {
        EntityManager emToUse = sharedEm != null ? sharedEm : em;
        
        try {
            if (sharedEm == null) {
                beginTransaction();
            }
            
            String jpql = "DELETE FROM CartItem c WHERE c.memberId = :memberId";
            Query query = emToUse.createQuery(jpql);
            query.setParameter("memberId", memberId);
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
