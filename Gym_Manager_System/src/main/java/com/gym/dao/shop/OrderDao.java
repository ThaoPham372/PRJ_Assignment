package com.gym.dao.shop;

import com.gym.model.shop.DeliveryMethod;
import com.gym.model.shop.Order;
import com.gym.model.shop.OrderStatus;
import com.gym.model.shop.PaymentMethod;
import com.gym.model.shop.PaymentStatus;
import com.gym.util.DatabaseUtil;

import java.math.BigDecimal;
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
        // NOTE: Schema mới không còn payment_method và payment_status trong orders table
        // Payment info được lưu trong bảng payments riêng
        String sql = "INSERT INTO orders (user_id, order_number, order_date, total_amount, " +
                    "discount_amount, order_status, delivery_method, delivery_address, delivery_phone, notes, created_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";

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
                stmt.setString(6, order.getOrderStatus() != null ? order.getOrderStatus().getCode() : "pending");
                
                // Delivery information
                if (order.getDeliveryMethod() != null) {
                    stmt.setString(7, order.getDeliveryMethod().getCode());
                } else {
                    stmt.setString(7, "PICKUP");
                }
                setStringOrNull(stmt, 8, order.getDeliveryAddress());
                setStringOrNull(stmt, 9, order.getDeliveryPhone());
                setStringOrNull(stmt, 10, order.getNotes());
                
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
        // NOTE: Schema mới không còn payment_method và payment_status trong orders table
        // Schema có: delivery_notes (không phải delivery_name)
        // JOIN với user table để lấy tên người dùng
        String sql = "SELECT o.order_id, o.user_id, o.order_number, o.order_date, o.total_amount, " +
                    "o.discount_amount, o.order_status, o.delivery_method, o.delivery_address, " +
                    "o.delivery_phone, o.delivery_notes, o.notes, o.created_at, " +
                    "u.name as user_name " +
                    "FROM orders o " +
                    "LEFT JOIN user u ON o.user_id = u.user_id " +
                    "WHERE o.order_number = ?";

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
        // NOTE: Schema mới không còn payment_method và payment_status trong orders table
        // Schema có: delivery_notes (không phải delivery_name)
        // JOIN với user table để lấy tên người dùng
        String sql = "SELECT o.order_id, o.user_id, o.order_number, o.order_date, o.total_amount, " +
                    "o.discount_amount, o.order_status, o.delivery_method, o.delivery_address, " +
                    "o.delivery_phone, o.delivery_notes, o.notes, o.created_at, " +
                    "u.name as user_name " +
                    "FROM orders o " +
                    "LEFT JOIN user u ON o.user_id = u.user_id " +
                    "WHERE o.order_id = ?";

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
        // NOTE: Schema mới không còn payment_method và payment_status trong orders table
        // Schema có: delivery_notes (không phải delivery_name)
        // JOIN với user table để lấy tên người dùng
        String sql = "SELECT o.order_id, o.user_id, o.order_number, o.order_date, o.total_amount, " +
                    "o.discount_amount, o.order_status, o.delivery_method, o.delivery_address, " +
                    "o.delivery_phone, o.delivery_notes, o.notes, o.created_at, " +
                    "u.name as user_name " +
                    "FROM orders o " +
                    "LEFT JOIN user u ON o.user_id = u.user_id " +
                    "WHERE o.user_id = ? " +
                    "ORDER BY o.created_at DESC";

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
     * Update order status
     */
    public void updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        updateOrderStatus(orderId, orderStatus, null);
    }

    /**
     * Update order status using provided connection (for transactions)
     */
    public void updateOrderStatus(Long orderId, OrderStatus orderStatus, Connection conn) {
        String sql = "UPDATE orders SET order_status = ? WHERE order_id = ?";

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
                stmt.setString(1, orderStatus.getCode());
                stmt.setLong(2, orderId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating order status: " + e.getMessage(), e);
            e.printStackTrace();
            throw new RuntimeException("Failed to update order status: " + e.getMessage(), e);
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

    /**
     * @deprecated Use PaymentService instead. This method is kept for backward compatibility.
     */
    @Deprecated
    public void updatePayment(Long orderId, PaymentStatus paymentStatus, PaymentMethod paymentMethod, Connection conn) {
        // This method is deprecated - payment info should be in payments table
        // Keep method signature for compatibility but do nothing
        LOGGER.warning("updatePayment() is deprecated. Use PaymentService instead.");
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
        // final_amount is computed column, may not exist in SELECT
        try {
            order.setFinalAmount(rs.getBigDecimal("final_amount"));
        } catch (SQLException e) {
            // Column doesn't exist, calculate from total - discount
            BigDecimal total = order.getTotalAmount();
            BigDecimal discount = order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO;
            order.setFinalAmount(total.subtract(discount));
        }
        
        // NOTE: payment_method and payment_status are now in payments table, not orders
        // Set to null - payment info should be loaded separately from payments table
        order.setPaymentMethod(null);
        order.setPaymentStatus(PaymentStatus.PENDING);
        
        String orderStatusStr = rs.getString("order_status");
        order.setOrderStatus(OrderStatus.fromCode(orderStatusStr));
        
        // Delivery information - check if columns exist in ResultSet
        java.sql.ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        boolean hasDeliveryName = false, hasDeliveryAddress = false, 
                hasDeliveryPhone = false, hasDeliveryMethod = false,
                hasDeliveryNotes = false;
        
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i).toLowerCase();
            if ("delivery_name".equals(columnName)) hasDeliveryName = true;
            if ("delivery_address".equals(columnName)) hasDeliveryAddress = true;
            if ("delivery_phone".equals(columnName)) hasDeliveryPhone = true;
            if ("delivery_method".equals(columnName)) hasDeliveryMethod = true;
            if ("delivery_notes".equals(columnName)) hasDeliveryNotes = true;
            if ("user_name".equals(columnName)) hasDeliveryName = true; // Use user_name as delivery name fallback
        }
        
        // Note: Schema có delivery_notes nhưng không có delivery_name
        // deliveryName có thể lấy từ user table (user_name) hoặc để null
        if (hasDeliveryName) {
            order.setDeliveryName(rs.getString("delivery_name"));
        } else {
            // Try to get from user_name (JOIN with user table)
            try {
                String userName = rs.getString("user_name");
                if (userName != null && !userName.trim().isEmpty()) {
                    order.setDeliveryName(userName);
                }
            } catch (SQLException e) {
                // Column may not exist, skip
            }
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
        // delivery_notes có thể dùng để set vào notes hoặc một field riêng
        if (hasDeliveryNotes) {
            String deliveryNotes = rs.getString("delivery_notes");
            // Nếu chưa có deliveryName, có thể dùng delivery_notes
            if (!hasDeliveryName && deliveryNotes != null) {
                // Có thể parse delivery_notes để lấy tên nếu format cho phép
                // Hoặc để null
            }
        }

        // Notes field
        try {
            order.setNotes(rs.getString("notes"));
        } catch (SQLException e) {
            // Column may not exist, skip
        }
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            order.setCreatedAt(createdAt.toInstant().atOffset(ZoneOffset.UTC));
        }
        
        return order;
    }

    /**
     * Helper: Set String or null
     */
    private void setStringOrNull(PreparedStatement stmt, int index, String value) throws SQLException {
        if (value != null && !value.trim().isEmpty()) {
            stmt.setString(index, value);
        } else {
            stmt.setNull(index, Types.VARCHAR);
        }
    }
}

