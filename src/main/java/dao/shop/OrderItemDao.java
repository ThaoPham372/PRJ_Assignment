package dao.shop;

import dao.GenericDAO;
import model.shop.OrderItem;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for OrderItem entity - extends GenericDAO for CRUD operations
 * Follows OOP principles and reuses GenericDAO methods
 */
public class OrderItemDao extends GenericDAO<OrderItem> {
    private static final Logger LOGGER = Logger.getLogger(OrderItemDao.class.getName());

    public OrderItemDao() {
        super(OrderItem.class);
    }

    /**
     * Insert batch of order items - reuses GenericDAO.save()
     */
    public void insertBatch(Integer orderId, List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        
        try {
            beginTransaction();
            
            for (OrderItem item : items) {
                item.setOrderId(orderId);
                
                // Calculate discount_percent if not provided
                calculateDiscountPercentIfNeeded(item);
                
                // Use GenericDAO save method
                save(item);
            }
            
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, "Error inserting order items", e);
            throw new RuntimeException("Failed to insert order items: " + e.getMessage(), e);
        }
    }

    /**
     * Find all items for an order
     */
    public List<OrderItem> findByOrderId(Integer orderId) {
        try {
            String jpql = "SELECT oi FROM OrderItem oi WHERE oi.orderId = :orderId ORDER BY oi.orderItemId";
            TypedQuery<OrderItem> query = em.createQuery(jpql, OrderItem.class);
            query.setParameter("orderId", orderId);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding order items for order: " + orderId, e);
            return Collections.emptyList();
        }
    }

    /**
     * Helper method to calculate discount percent if needed
     */
    private void calculateDiscountPercentIfNeeded(OrderItem item) {
        if (item.getDiscountPercent() == null && 
            item.getDiscountAmount() != null && 
            item.getUnitPrice() != null && 
            item.getUnitPrice().compareTo(BigDecimal.ZERO) > 0) {
            
            BigDecimal discountPercent = item.getDiscountAmount()
                .divide(item.getUnitPrice(), 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
            item.setDiscountPercent(discountPercent);
        }
    }
}
