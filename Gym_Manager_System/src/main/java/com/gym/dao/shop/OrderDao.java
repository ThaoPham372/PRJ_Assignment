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
        // Use fallback method directly to handle legacy CONFIRMED status
        return findByUserIdFallback(userId);
    }
    
    /**
     * Find orders by user ID using raw SQL to handle legacy statuses (CONFIRMED, PREPARING, READY, etc.)
     * Converts legacy statuses to new statuses:
     * - CONFIRMED -> COMPLETED
     * - PREPARING, READY, PROCESSING, SHIPPED, DELIVERED -> COMPLETED
     * - Others -> PENDING or CANCELLED
     */
    private List<Order> findByUserIdFallback(Long userId) {
        try {
            // Use raw SQL to get order data and manually map to Order objects
            String sql = "SELECT order_id, user_id, order_number, order_date, total_amount, " +
                        "discount_amount, order_status, delivery_method, delivery_address, " +
                        "delivery_phone, notes, cancellation_reason, cancelled_at, created_at, updated_at " +
                        "FROM orders WHERE user_id = ? ORDER BY created_at DESC";
            
            jakarta.persistence.Query query = em.createNativeQuery(sql);
            query.setParameter(1, userId);
            
            @SuppressWarnings("unchecked")
            List<Object[]> results = query.getResultList();
            List<Order> orders = new ArrayList<>();
            
            for (Object[] row : results) {
                Order order = new Order();
                order.setOrderId(((Number) row[0]).longValue());
                order.setUserId(((Number) row[1]).longValue());
                order.setOrderNumber((String) row[2]);
                if (row[3] != null) {
                    if (row[3] instanceof java.sql.Timestamp) {
                        order.setOrderDate(((java.sql.Timestamp) row[3]).toLocalDateTime());
                    } else if (row[3] instanceof java.time.LocalDateTime) {
                        order.setOrderDate((java.time.LocalDateTime) row[3]);
                    }
                }
                order.setTotalAmount((java.math.BigDecimal) row[4]);
                order.setDiscountAmount(row[5] != null ? (java.math.BigDecimal) row[5] : java.math.BigDecimal.ZERO);
                
                // Handle order status - convert legacy statuses to new statuses
                String statusStr = (String) row[6];
                if (statusStr != null) {
                    // Convert legacy statuses to new statuses
                    if ("CONFIRMED".equalsIgnoreCase(statusStr) || 
                        "PREPARING".equalsIgnoreCase(statusStr) ||
                        "READY".equalsIgnoreCase(statusStr) ||
                        "PROCESSING".equalsIgnoreCase(statusStr) ||
                        "SHIPPED".equalsIgnoreCase(statusStr) ||
                        "DELIVERED".equalsIgnoreCase(statusStr)) {
                        // All these statuses mean order is completed/confirmed
                        order.setOrderStatus(OrderStatus.COMPLETED);
                        System.out.println("[OrderDao] Converting legacy status '" + statusStr + "' to COMPLETED for order " + order.getOrderId());
                    } else if ("CANCELLED".equalsIgnoreCase(statusStr) || "CANCELED".equalsIgnoreCase(statusStr)) {
                        order.setOrderStatus(OrderStatus.CANCELLED);
                    } else if ("PENDING".equalsIgnoreCase(statusStr)) {
                        order.setOrderStatus(OrderStatus.PENDING);
                    } else {
                        // Unknown status, try to parse or default to PENDING
                        try {
                            order.setOrderStatus(OrderStatus.fromCode(statusStr));
                        } catch (Exception e) {
                            System.err.println("[OrderDao] Unknown status: " + statusStr + ", defaulting to PENDING");
                            order.setOrderStatus(OrderStatus.PENDING);
                        }
                    }
                } else {
                    order.setOrderStatus(OrderStatus.PENDING);
                }
                
                if (row[7] != null) {
                    String deliveryMethodStr = (String) row[7];
                    try {
                        order.setDeliveryMethod(com.gym.model.shop.DeliveryMethod.fromCode(deliveryMethodStr));
                    } catch (Exception e) {
                        System.err.println("[OrderDao] Error parsing delivery method: " + deliveryMethodStr);
                        order.setDeliveryMethod(com.gym.model.shop.DeliveryMethod.PICKUP);
                    }
                }
                order.setDeliveryAddress((String) row[8]);
                order.setDeliveryPhone((String) row[9]);
                order.setNotes((String) row[10]);
                order.setCancellationReason((String) row[11]);
                if (row[12] != null) {
                    if (row[12] instanceof java.sql.Timestamp) {
                        order.setCancelledAt(((java.sql.Timestamp) row[12]).toLocalDateTime());
                    } else if (row[12] instanceof java.time.LocalDateTime) {
                        order.setCancelledAt((java.time.LocalDateTime) row[12]);
                    }
                }
                if (row[13] != null) {
                    if (row[13] instanceof java.sql.Timestamp) {
                        order.setCreatedAt(((java.sql.Timestamp) row[13]).toLocalDateTime());
                    } else if (row[13] instanceof java.time.LocalDateTime) {
                        order.setCreatedAt((java.time.LocalDateTime) row[13]);
                    }
                }
                
                orders.add(order);
            }
            
            System.out.println("[OrderDao] findByUserIdFallback - Loaded " + orders.size() + " orders using fallback method");
            
            // Load user information for each order
            for (Order order : orders) {
                try {
                    if (order.getUser() == null && order.getUserId() != null) {
                        com.gym.dao.UserDAO userDao = new com.gym.dao.UserDAO();
                        com.gym.model.User user = userDao.getUserById(order.getUserId().intValue());
                        if (user != null) {
                            order.setUser(user);
                            if (order.getDeliveryName() == null) {
                                order.setDeliveryName(user.getName());
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("[OrderDao] Error loading user for order " + order.getOrderId() + ": " + e.getMessage());
                }
            }
            
            return orders;
        } catch (Exception e) {
            System.err.println("[OrderDao] findByUserIdFallback - ERROR: " + e.getMessage());
            e.printStackTrace();
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
     * Cancel order (only if status is PENDING)
     * @param orderId Order ID
     * @param userId User ID (to verify ownership)
     * @param cancellationReason Optional cancellation reason
     * @return true if cancelled successfully, false if order cannot be cancelled
     */
    public boolean cancelOrder(Long orderId, Long userId, String cancellationReason) {
        return cancelOrder(orderId, userId, cancellationReason, null);
    }
    
    /**
     * Cancel order using provided EntityManager (for transactions)
     * Only allows cancellation if order status is PENDING
     */
    public boolean cancelOrder(Long orderId, Long userId, String cancellationReason, EntityManager sharedEm) {
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
                LOGGER.warning("Order not found for cancellation: orderId=" + orderId);
                return false;
            }
            
            // Verify ownership
            if (!order.getUserId().equals(userId)) {
                if (sharedEm == null) {
                    rollbackTransaction();
                }
                LOGGER.warning("User " + userId + " attempted to cancel order " + orderId + " which belongs to user " + order.getUserId());
                return false;
            }
            
            // Only allow cancellation if order is PENDING
            if (order.getOrderStatus() != OrderStatus.PENDING) {
                if (sharedEm == null) {
                    rollbackTransaction();
                }
                LOGGER.warning("Cannot cancel order " + orderId + " with status: " + order.getOrderStatus());
                return false;
            }
            
            // Update order status to CANCELLED
            order.setOrderStatus(OrderStatus.CANCELLED);
            order.setCancellationReason(cancellationReason);
            order.setCancelledAt(java.time.LocalDateTime.now());
            
            emToUse.merge(order);
            
            if (sharedEm == null) {
                commitTransaction();
            }
            
            LOGGER.info("✅ Order cancelled successfully: orderId=" + orderId + ", userId=" + userId);
            return true;
        } catch (Exception e) {
            if (sharedEm == null) {
                rollbackTransaction();
            }
            LOGGER.log(Level.SEVERE, "Error cancelling order: " + e.getMessage(), e);
            return false;
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
