package com.gym.dao.shop;

import com.gym.dao.BaseDAO;
import com.gym.model.shop.Order;
import com.gym.model.shop.OrderStatus;
import com.gym.model.shop.PaymentMethod;
import com.gym.model.shop.PaymentStatus;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for Order entity - JPA Implementation
 */
public class OrderDao extends BaseDAO<Order> {
    private static final Logger LOGGER = Logger.getLogger(OrderDao.class.getName());

    public OrderDao() {
        super();
    }

    /**
     * Insert new order and return generated order ID
     */
    public Long insert(Order order) {
        return insert(order, null);
    }
    
    /**
     * Insert new order using provided EntityManager (for transactions)
     */
    public Long insert(Order order, EntityManager sharedEm) {
        EntityManager emToUse = sharedEm != null ? sharedEm : em;
        
        try {
            if (sharedEm == null) {
                beginTransaction();
            }
            
            if (order.getOrderDate() == null) {
                order.setOrderDate(java.time.LocalDateTime.now());
            }
            if (order.getCreatedAt() == null) {
                order.setCreatedAt(java.time.LocalDateTime.now());
            }
            if (order.getOrderStatus() == null) {
                order.setOrderStatus(OrderStatus.PENDING);
            }
            
            emToUse.persist(order);
            
            if (sharedEm == null) {
                commitTransaction();
            } else {
                emToUse.flush();
            }
            
            return order.getOrderId();
        } catch (Exception e) {
            if (sharedEm == null) {
                rollbackTransaction();
            }
            LOGGER.log(Level.SEVERE, "Error inserting order: " + e.getMessage(), e);
            throw new RuntimeException("Failed to insert order: " + e.getMessage(), e);
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
                // Set delivery name from user if available
                if (order.getUser() != null && order.getDeliveryName() == null) {
                    order.setDeliveryName(order.getUser().getName());
                }
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
     * Find order by ID
     */
    public Optional<Order> findById(Long orderId) {
        try {
            Order order = em.find(Order.class, orderId);
            if (order != null) {
                // Load user if not already loaded
                if (order.getUser() == null) {
                    em.refresh(order);
                }
                // Set delivery name from user if available
                if (order.getUser() != null && order.getDeliveryName() == null) {
                    order.setDeliveryName(order.getUser().getName());
                }
                return Optional.of(order);
            }
            return Optional.empty();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding order by ID: " + orderId, e);
            return Optional.empty();
        }
    }

    /**
     * Find all orders by user ID
     */
    public List<Order> findByUserId(Long userId) {
        try {
            String jpql = "SELECT o FROM Order o " +
                         "LEFT JOIN FETCH o.user u " +
                         "WHERE o.userId = :userId " +
                         "ORDER BY o.createdAt DESC";
            
            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            query.setParameter("userId", userId);
            
            List<Order> orders = query.getResultList();
            
            // Set delivery name from user if available
            for (Order order : orders) {
                if (order.getUser() != null && order.getDeliveryName() == null) {
                    order.setDeliveryName(order.getUser().getName());
                }
            }
            
            return orders;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding orders by user ID: " + userId, e);
            return new ArrayList<>();
        }
    }

    /**
     * Update order status
     */
    public void updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        updateOrderStatus(orderId, orderStatus, null);
    }

    /**
     * Update order status using provided EntityManager (for transactions)
     */
    public void updateOrderStatus(Long orderId, OrderStatus orderStatus, EntityManager sharedEm) {
        EntityManager emToUse = sharedEm != null ? sharedEm : em;
        
        try {
            if (sharedEm == null) {
                beginTransaction();
            }
            
            Order order = emToUse.find(Order.class, orderId);
            if (order == null) {
                if (sharedEm == null) {
                    rollbackTransaction();
                }
                throw new RuntimeException("Order not found: " + orderId);
            }
            
            order.setOrderStatus(orderStatus);
            emToUse.merge(order);
            
            if (sharedEm == null) {
                commitTransaction();
            }
        } catch (Exception e) {
            if (sharedEm == null) {
                rollbackTransaction();
            }
            LOGGER.log(Level.SEVERE, "Error updating order status: " + e.getMessage(), e);
            throw new RuntimeException("Failed to update order status: " + e.getMessage(), e);
        }
    }

    /**
     * @deprecated Use PaymentService instead. This method is kept for backward compatibility.
     */
    @Deprecated
    public void updatePayment(Long orderId, PaymentStatus paymentStatus, PaymentMethod paymentMethod) {
        // This method is deprecated - payment info should be in payments table
        LOGGER.warning("updatePayment() is deprecated. Use PaymentService instead.");
    }
    
}
