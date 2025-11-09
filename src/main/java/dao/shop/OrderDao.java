package dao.shop;

import dao.GenericDAO;
import model.shop.Order;
import model.shop.OrderStatus;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for Order entity - extends GenericDAO for CRUD
 */
public class OrderDao extends GenericDAO<Order> {
    private static final Logger LOGGER = Logger.getLogger(OrderDao.class.getName());

    public OrderDao() {
        super(Order.class);
    }

    /**
     * Insert new order and return generated order ID
     */
    public Integer insert(Order order) {
        return insert(order, null);
    }
    
    /**
     * Insert new order using provided EntityManager (for transactions)
     * Reuses GenericDAO.persist() method
     */
    public Integer insert(Order order, EntityManager sharedEm) {
        try {
            // Set default values before persisting
            if (order.getOrderDate() == null) {
                order.setOrderDate(java.time.LocalDateTime.now());
            }
            if (order.getCreatedAt() == null) {
                order.setCreatedAt(java.time.LocalDateTime.now());
            }
            if (order.getOrderStatus() == null) {
                order.setOrderStatus(OrderStatus.PENDING);
            }
            
            // Use GenericDAO persist method
            persist(order, sharedEm);
            
            return order.getOrderId();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inserting order", e);
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
     * Find order by ID (overrides GenericDAO to load User relationship)
     */
    @Override
    public Optional<Order> findByIdOptional(int orderId) {
        try {
            Order order = em.find(Order.class, orderId);
            if (order != null) {
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
            
            // Set delivery name from user if available
            for (Order order : orders) {
                if (order.getUser() != null && order.getDeliveryName() == null) {
                    order.setDeliveryName(order.getUser().getName());
                }
            }
            
            return orders;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding orders by member ID: " + memberId, e);
            return new ArrayList<>();
        }
    }

    /**
     * Update order status
     */
    public void updateOrderStatus(Integer orderId, OrderStatus orderStatus) {
        updateOrderStatus(orderId, orderStatus, null);
    }

    /**
     * Update order status using provided EntityManager (for transactions)
     */
    public void updateOrderStatus(Integer orderId, OrderStatus orderStatus, EntityManager sharedEm) {
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

}
