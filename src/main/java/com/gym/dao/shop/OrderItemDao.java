package com.gym.dao.shop;

import com.gym.dao.BaseDAO;
import com.gym.model.shop.OrderItem;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for OrderItem entity - JPA Implementation
 */
public class OrderItemDao extends BaseDAO<OrderItem> {
    private static final Logger LOGGER = Logger.getLogger(OrderItemDao.class.getName());

    public OrderItemDao() {
        super();
    }

    /**
     * Insert batch of order items
     */
    public void insertBatch(Long orderId, List<OrderItem> items) {
        insertBatch(orderId, items, null);
    }
    
    /**
     * Insert batch of order items using provided EntityManager (for transactions)
     */
    public void insertBatch(Long orderId, List<OrderItem> items, EntityManager sharedEm) {
        if (items == null || items.isEmpty()) {
            return;
        }
        
        EntityManager emToUse = sharedEm != null ? sharedEm : em;
        
        try {
            if (sharedEm == null) {
                beginTransaction();
            }
            
            for (OrderItem item : items) {
                item.setOrderId(orderId);
                
                // Calculate discount_percent if not provided
                if (item.getDiscountPercent() == null && item.getDiscountAmount() != null && 
                    item.getUnitPrice() != null && item.getUnitPrice().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal discountPercent = item.getDiscountAmount()
                        .divide(item.getUnitPrice(), 4, java.math.RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
                    item.setDiscountPercent(discountPercent);
                }
                
                emToUse.persist(item);
            }
            
            if (sharedEm == null) {
                commitTransaction();
            } else {
                emToUse.flush();
            }
        } catch (Exception e) {
            if (sharedEm == null) {
                rollbackTransaction();
            }
            LOGGER.log(Level.SEVERE, "Error inserting order items: " + e.getMessage(), e);
            throw new RuntimeException("Failed to insert order items: " + e.getMessage(), e);
        }
    }

    /**
     * Find all items for an order
     */
    public List<OrderItem> findByOrderId(Long orderId) {
        try {
            String jpql = "SELECT oi FROM OrderItem oi WHERE oi.orderId = :orderId ORDER BY oi.orderItemId";
            
            TypedQuery<OrderItem> query = em.createQuery(jpql, OrderItem.class);
            query.setParameter("orderId", orderId);
            
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding order items for order: " + orderId, e);
            return new ArrayList<>();
        }
    }
    
}
