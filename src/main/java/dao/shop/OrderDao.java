package dao.shop;

import dao.GenericDAO;
import model.shop.Order;
import model.shop.OrderStatus;
import jakarta.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for Order entity - extends GenericDAO for CRUD operations
 * Follows OOP principles and reuses GenericDAO methods
 */
public class OrderDao extends GenericDAO<Order> {
    private static final Logger LOGGER = Logger.getLogger(OrderDao.class.getName());

    public OrderDao() {
        super(Order.class);
    }

    /**
     * Create order - reuses GenericDAO.save() method
     * Sets default values before saving
     */
    public Integer create(Order order) {
        try {
            // Set default values
            if (order.getOrderDate() == null) {
                order.setOrderDate(java.time.LocalDateTime.now());
            }
            if (order.getCreatedAt() == null) {
                order.setCreatedAt(java.time.LocalDateTime.now());
            }
            if (order.getOrderStatus() == null) {
                order.setOrderStatus(OrderStatus.PENDING);
            }
            
            // Use GenericDAO save method
            super.save(order);
            return order.getOrderId();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating order", e);
            throw new RuntimeException("Failed to create order: " + e.getMessage(), e);
        }
    }

    /**
     * Find order by order number
     */
    public Optional<Order> findByOrderNumber(String orderNumber) {
        try {
            String jpql = "SELECT o FROM Order o " +
                         "LEFT JOIN FETCH o.user u " +
                         "WHERE o.orderNumber = :orderNumber";
            
            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            query.setParameter("orderNumber", orderNumber);
            
            try {
                Order order = query.getSingleResult();
                setDeliveryNameIfNeeded(order);
                return Optional.of(order);
            } catch (NoResultException e) {
                return Optional.empty();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding order by order number: " + orderNumber, e);
            return Optional.empty();
        }
    }

    /**
     * Find order by ID - reuses GenericDAO.findById()
     */
    public Optional<Order> findByIdOptional(Integer orderId) {
        try {
            Order order = findById(orderId);
            if (order != null) {
                setDeliveryNameIfNeeded(order);
                return Optional.of(order);
            }
            return Optional.empty();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding order by ID: " + orderId, e);
            return Optional.empty();
        }
    }

    /**
     * Find all orders by member ID
     */
    public List<Order> findByMemberId(Integer memberId) {
        try {
            String jpql = "SELECT o FROM Order o " +
                         "LEFT JOIN FETCH o.user u " +
                         "WHERE o.memberId = :memberId " +
                         "ORDER BY o.createdAt DESC";
            
            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            query.setParameter("memberId", memberId);
            
            List<Order> orders = query.getResultList();
            orders.forEach(this::setDeliveryNameIfNeeded);
            
            return orders;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding orders by member ID: " + memberId, e);
            return Collections.emptyList();
        }
    }

    /**
     * Update order status - reuses GenericDAO.update()
     */
    public void updateOrderStatus(Integer orderId, OrderStatus orderStatus) {
        try {
            Order order = findById(orderId);
            if (order == null) {
                throw new RuntimeException("Order not found: " + orderId);
            }
            
            order.setOrderStatus(orderStatus);
            update(order);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating order status: " + e.getMessage(), e);
            throw new RuntimeException("Failed to update order status: " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to set delivery name from user if needed
     */
    private void setDeliveryNameIfNeeded(Order order) {
        if (order.getUser() != null && order.getDeliveryName() == null) {
            order.setDeliveryName(order.getUser().getName());
        }
    }
}
