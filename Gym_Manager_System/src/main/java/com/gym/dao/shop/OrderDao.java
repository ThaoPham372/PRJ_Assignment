package com.gym.dao.shop;

import com.gym.model.shop.DeliveryMethod;
import com.gym.model.shop.Order;
import com.gym.model.shop.OrderStatus;
import com.gym.model.shop.PaymentMethod;
import com.gym.model.shop.PaymentStatus;
import com.gym.util.DatabaseUtil;

import java.sql.*;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for Order entity
 */
public class OrderDao {
    private static final Logger LOGGER = Logger.getLogger(OrderDao.class.getName());

    /**
     * Insert new order and return generated order ID
     * Uses provided connection if available, otherwise creates new one
     */
    public Long insert(Order order) {
        return insert(order, null);
    }
    
    /**
     * Insert new order using provided connection (for transactions)
     */
    public Long insert(Order order, Connection conn) {
        String sql = "INSERT INTO orders (user_id, order_number, order_date, total_amount, " +
                    "discount_amount, payment_method, payment_status, order_status, created_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())";

        boolean shouldCloseConnection = false;
        try {
            if (conn == null) {
                conn = DatabaseUtil.getConnection();
                shouldCloseConnection = true;
            }
            
            if (conn == null) {
                throw new SQLException("Database connection is null");
            }
            
            // Use RETURN_GENERATED_KEYS for MySQL
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, order.getUserId());
                stmt.setString(2, order.getOrderNumber());
                stmt.setTimestamp(3, Timestamp.from(order.getOrderDate().toInstant()));
                stmt.setBigDecimal(4, order.getTotalAmount());
                stmt.setBigDecimal(5, order.getDiscountAmount());
                stmt.setString(6, order.getPaymentMethod() != null ? order.getPaymentMethod().getCode() : null);
                stmt.setString(7, order.getPaymentStatus() != null ? order.getPaymentStatus().getCode() : "pending");
                stmt.setString(8, order.getOrderStatus() != null ? order.getOrderStatus().getCode() : "pending");
                
                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            Long orderId = rs.getLong(1);
                            LOGGER.info("Order inserted successfully with ID: " + orderId);
                            return orderId;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting order: " + e.getMessage(), e);
            e.printStackTrace();
            throw new RuntimeException("Failed to insert order: " + e.getMessage(), e);
        } finally {
            // Only close connection if we created it
            if (shouldCloseConnection && conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Error closing connection", e);
                }
            }
        }
        
        throw new RuntimeException("Failed to get generated order ID");
    }

    /**
     * Find order by order number
     */
    public Optional<Order> findByOrderNumber(String orderNumber) {
        String sql = "SELECT order_id, user_id, order_number, order_date, total_amount, " +
                    "discount_amount, final_amount, payment_method, payment_status, " +
                    "order_status, delivery_name, delivery_address, delivery_phone, delivery_method, created_at " +
                    "FROM orders WHERE order_number = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                return Optional.empty();
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, orderNumber);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(mapResultSetToOrder(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding order by order number: " + orderNumber, e);
        }
        
        return Optional.empty();
    }

    /**
     * Find order by ID
     */
    public Optional<Order> findById(Long orderId) {
        String sql = "SELECT order_id, user_id, order_number, order_date, total_amount, " +
                    "discount_amount, final_amount, payment_method, payment_status, " +
                    "order_status, delivery_name, delivery_address, delivery_phone, delivery_method, created_at " +
                    "FROM orders WHERE order_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                return Optional.empty();
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, orderId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(mapResultSetToOrder(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding order by ID: " + orderId, e);
        }
        
        return Optional.empty();
    }

    /**
     * Find all orders by user ID
     */
    public java.util.List<Order> findByUserId(Long userId) {
        java.util.List<Order> orders = new java.util.ArrayList<>();
        String sql = "SELECT order_id, user_id, order_number, order_date, total_amount, " +
                    "discount_amount, final_amount, payment_method, payment_status, " +
                    "order_status, created_at " +
                    "FROM orders WHERE user_id = ? " +
                    "ORDER BY created_at DESC";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                return orders;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, userId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding orders by user ID: " + userId, e);
        }
        
        return orders;
    }

    /**
     * Update payment status and method
     * Uses provided connection if available, otherwise creates new one
     */
    public void updatePayment(Long orderId, PaymentStatus paymentStatus, PaymentMethod paymentMethod) {
        updatePayment(orderId, paymentStatus, paymentMethod, null);
    }
    
    /**
     * Update payment status and method using provided connection (for transactions)
     */
    public void updatePayment(Long orderId, PaymentStatus paymentStatus, PaymentMethod paymentMethod, Connection conn) {
        String sql = "UPDATE orders SET payment_status = ?, payment_method = ? WHERE order_id = ?";

        boolean shouldCloseConnection = false;
        try {
            if (conn == null) {
                conn = DatabaseUtil.getConnection();
                shouldCloseConnection = true;
            }
            
            if (conn == null) {
                throw new SQLException("Database connection is null");
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, paymentStatus.getCode());
                stmt.setString(2, paymentMethod != null ? paymentMethod.getCode() : null);
                stmt.setLong(3, orderId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating order payment: " + e.getMessage(), e);
            e.printStackTrace();
            throw new RuntimeException("Failed to update order payment: " + e.getMessage(), e);
        } finally {
            // Only close connection if we created it
            if (shouldCloseConnection && conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Error closing connection", e);
                }
            }
        }
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getLong("order_id"));
        order.setUserId(rs.getLong("user_id"));
        order.setOrderNumber(rs.getString("order_number"));
        
        Timestamp orderDate = rs.getTimestamp("order_date");
        if (orderDate != null) {
            order.setOrderDate(orderDate.toInstant().atOffset(ZoneOffset.UTC));
        }
        
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setDiscountAmount(rs.getBigDecimal("discount_amount"));
        order.setFinalAmount(rs.getBigDecimal("final_amount"));
        
        String paymentMethodStr = rs.getString("payment_method");
        order.setPaymentMethod(PaymentMethod.fromCode(paymentMethodStr));
        
        String paymentStatusStr = rs.getString("payment_status");
        order.setPaymentStatus(PaymentStatus.fromCode(paymentStatusStr));
        
        String orderStatusStr = rs.getString("order_status");
        order.setOrderStatus(OrderStatus.fromCode(orderStatusStr));
        
        // Delivery information - check if columns exist in ResultSet
        java.sql.ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        boolean hasDeliveryName = false, hasDeliveryAddress = false, 
                hasDeliveryPhone = false, hasDeliveryMethod = false;
        
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i).toLowerCase();
            if ("delivery_name".equals(columnName)) hasDeliveryName = true;
            if ("delivery_address".equals(columnName)) hasDeliveryAddress = true;
            if ("delivery_phone".equals(columnName)) hasDeliveryPhone = true;
            if ("delivery_method".equals(columnName)) hasDeliveryMethod = true;
        }
        
        if (hasDeliveryName) {
            order.setDeliveryName(rs.getString("delivery_name"));
        }
        if (hasDeliveryAddress) {
            order.setDeliveryAddress(rs.getString("delivery_address"));
        }
        if (hasDeliveryPhone) {
            order.setDeliveryPhone(rs.getString("delivery_phone"));
        }
        if (hasDeliveryMethod) {
            String deliveryMethodStr = rs.getString("delivery_method");
            if (deliveryMethodStr != null) {
                order.setDeliveryMethod(DeliveryMethod.fromCode(deliveryMethodStr));
            }
        }
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            order.setCreatedAt(createdAt.toInstant().atOffset(ZoneOffset.UTC));
        }
        
        return order;
    }
}

