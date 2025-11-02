package com.gym.dao.shop;

import com.gym.model.shop.OrderItem;
import com.gym.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for OrderItem entity
 */
public class OrderItemDao {
    private static final Logger LOGGER = Logger.getLogger(OrderItemDao.class.getName());

    /**
     * Insert batch of order items
     * Uses provided connection if available, otherwise creates new one
     */
    public void insertBatch(Long orderId, List<OrderItem> items) {
        insertBatch(orderId, items, null);
    }
    
    /**
     * Insert batch of order items using provided connection (for transactions)
     */
    public void insertBatch(Long orderId, List<OrderItem> items, Connection conn) {
        if (items == null || items.isEmpty()) {
            return;
        }
        
        String sql = "INSERT INTO order_items (order_id, product_id, product_name, quantity, " +
                    "unit_price, discount_amount) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

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
                for (OrderItem item : items) {
                    stmt.setLong(1, orderId);
                    // Handle null product_id for membership items
                    if (item.getProductId() != null) {
                        stmt.setLong(2, item.getProductId());
                    } else {
                        stmt.setNull(2, Types.BIGINT);
                    }
                    stmt.setString(3, item.getProductName());
                    stmt.setInt(4, item.getQuantity());
                    stmt.setBigDecimal(5, item.getUnitPrice());
                    stmt.setBigDecimal(6, item.getDiscountAmount());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting order items: " + e.getMessage(), e);
            e.printStackTrace();
            throw new RuntimeException("Failed to insert order items: " + e.getMessage(), e);
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
     * Find all items for an order
     */
    public List<OrderItem> findByOrderId(Long orderId) {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT order_item_id, order_id, product_id, product_name, quantity, " +
                    "unit_price, discount_amount, subtotal " +
                    "FROM order_items WHERE order_id = ? ORDER BY order_item_id";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                return items;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, orderId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    items.add(mapResultSetToOrderItem(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding order items for order: " + orderId, e);
        }
        
        return items;
    }

    private OrderItem mapResultSetToOrderItem(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setOrderItemId(rs.getLong("order_item_id"));
        item.setOrderId(rs.getLong("order_id"));
        // Handle NULL product_id for membership items
        Long productId = rs.getLong("product_id");
        if (rs.wasNull()) {
            productId = null;
        }
        item.setProductId(productId);
        item.setProductName(rs.getString("product_name"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        item.setDiscountAmount(rs.getBigDecimal("discount_amount"));
        item.setSubtotal(rs.getBigDecimal("subtotal"));
        return item;
    }
}

