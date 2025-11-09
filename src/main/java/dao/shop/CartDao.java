package dao.shop;

import dao.GenericDAO;
import model.shop.CartItem;
import jakarta.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for Cart operations - extends GenericDAO for CRUD
 * Follows OOP principles and reuses GenericDAO methods
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
            return Collections.emptyList();
        }
    }

    /**
     * Find cart item by member and product - reuses GenericDAO.findByField()
     */
    private CartItem findByMemberAndProduct(Integer memberId, Integer productId) {
        try {
            String jpql = "SELECT c FROM CartItem c WHERE c.memberId = :memberId AND c.productId = :productId";
            TypedQuery<CartItem> query = em.createQuery(jpql, CartItem.class);
            query.setParameter("memberId", memberId);
            query.setParameter("productId", productId);
            
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Add or update cart item - reuses GenericDAO.save() and update()
     */
    public void addOrUpdate(Integer memberId, Integer productId, Integer quantity) {
        try {
            beginTransaction();
            
            CartItem existingItem = findByMemberAndProduct(memberId, productId);
            
            if (existingItem != null) {
                // Update quantity
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                existingItem.setAddedAt(java.time.LocalDateTime.now());
                update(existingItem);
            } else {
                // Create new item
                CartItem newItem = new CartItem();
                newItem.setMemberId(memberId);
                newItem.setProductId(productId);
                newItem.setQuantity(quantity);
                newItem.setAddedAt(java.time.LocalDateTime.now());
                save(newItem);
            }
            
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, "Error adding/updating cart item", e);
            throw new RuntimeException("Failed to update cart", e);
        }
    }

    /**
     * Update quantity for a cart item - reuses GenericDAO.update()
     */
    public void updateQuantity(Integer memberId, Integer productId, Integer quantity) {
        try {
            beginTransaction();
            
            CartItem item = findByMemberAndProduct(memberId, productId);
            if (item == null) {
                rollbackTransaction();
                throw new RuntimeException("Cart item not found");
            }
            
            item.setQuantity(quantity);
            update(item);
            
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, "Error updating cart item quantity", e);
            throw new RuntimeException("Failed to update cart quantity", e);
        }
    }

    /**
     * Remove item from cart - reuses GenericDAO.delete()
     */
    public void remove(Integer memberId, Integer productId) {
        try {
            beginTransaction();
            
            CartItem item = findByMemberAndProduct(memberId, productId);
            if (item != null) {
                delete(item);
            }
            
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, "Error removing cart item", e);
            throw new RuntimeException("Failed to remove cart item", e);
        }
    }

    /**
     * Clear all items for a member - uses JPQL for bulk delete
     */
    public void clear(Integer memberId) {
        try {
            beginTransaction();
            
            String jpql = "DELETE FROM CartItem c WHERE c.memberId = :memberId";
            Query query = em.createQuery(jpql);
            query.setParameter("memberId", memberId);
            query.executeUpdate();
            
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, "Error clearing cart", e);
            throw new RuntimeException("Failed to clear cart", e);
        }
    }
}
