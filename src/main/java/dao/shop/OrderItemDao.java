package dao.shop;

import dao.GenericDAO;
import model.shop.OrderItem;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for OrderItem entity - extends GenericDAO for CRUD
 */
public class OrderItemDao extends GenericDAO<OrderItem> {
    private static final Logger LOGGER = Logger.getLogger(OrderItemDao.class.getName());

    public OrderItemDao() {
        super(OrderItem.class);
    }

    /**
     * Insert batch of order items
     */
    public void insertBatch(Integer orderId, List<OrderItem> items) {
        insertBatch(orderId, items, null);
    }
    
    /**
     * Insert batch of order items using provided EntityManager (for transactions)
     * Reuses GenericDAO.persist() method
     */
    public void insertBatch(Integer orderId, List<OrderItem> items, EntityManager sharedEm) {
        if (items == null || items.isEmpty()) {
            return;
        }
        
        try {
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
                
                // Use GenericDAO persist method
                persist(item, sharedEm);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inserting order items", e);
            throw new RuntimeException("Failed to insert order items: " + e.getMessage(), e);
        }
    }

    /**
     * Find all items for an order
     * Reuses GenericDAO.findAllByField() method
     */
    public List<OrderItem> findByOrderId(Integer orderId) {
        try {
            return findAllByField("orderId", orderId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding order items for order: " + orderId, e);
            return new ArrayList<>();
        }
    }
    
}
