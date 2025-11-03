package com.gym.dao;

import com.gym.model.Payment;
import com.gym.model.shop.PaymentMethod;
import com.gym.model.shop.PaymentStatus;
import com.gym.util.DatabaseUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for Payment entity
 * Handles CRUD operations for payments table
 */
public class PaymentDAO {
    private static final Logger LOGGER = Logger.getLogger(PaymentDAO.class.getName());

    /**
     * Create payment for order (transaction_type = 'PRODUCT')
     */
    public Integer createPaymentForOrder(Integer userId, Long orderId, BigDecimal amount, 
                                        PaymentMethod method, String referenceId) {
        return createPaymentForOrder(userId, orderId, amount, method, referenceId, null);
    }

    /**
     * Create payment for order using provided connection (for transactions)
     */
    public Integer createPaymentForOrder(Integer userId, Long orderId, BigDecimal amount, 
                                        PaymentMethod method, String referenceId, Connection conn) {
        String sql = "INSERT INTO payments (user_id, amount, payment_date, method, status, " +
                    "reference_id, transaction_type, order_id, notes) " +
                    "VALUES (?, ?, CURRENT_TIMESTAMP, ?, 'PENDING', ?, 'PRODUCT', ?, NULL)";

        boolean shouldCloseConnection = false;
        try {
            if (conn == null) {
                conn = DatabaseUtil.getConnection();
                shouldCloseConnection = true;
            }
            
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Database connection is null");
                return null;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, userId);
                stmt.setBigDecimal(2, amount);
                stmt.setString(3, Payment.methodToDbValue(method));
                setStringOrNull(stmt, 4, referenceId);
                stmt.setLong(5, orderId);

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            return rs.getInt(1);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating payment for order", e);
            throw new RuntimeException("Failed to create payment for order: " + e.getMessage(), e);
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

        return null;
    }

    /**
     * Create payment for membership (transaction_type = 'PACKAGE')
     */
    public Integer createPaymentForMembership(Integer userId, Long membershipId, BigDecimal amount,
                                             PaymentMethod method, String referenceId) {
        return createPaymentForMembership(userId, membershipId, amount, method, referenceId, null);
    }

    /**
     * Create payment for membership using provided connection (for transactions)
     */
    public Integer createPaymentForMembership(Integer userId, Long membershipId, BigDecimal amount,
                                             PaymentMethod method, String referenceId, Connection conn) {
        String sql = "INSERT INTO payments (user_id, amount, payment_date, method, status, " +
                    "reference_id, transaction_type, membership_id, notes) " +
                    "VALUES (?, ?, CURRENT_TIMESTAMP, ?, 'PENDING', ?, 'PACKAGE', ?, NULL)";

        boolean shouldCloseConnection = false;
        try {
            if (conn == null) {
                conn = DatabaseUtil.getConnection();
                shouldCloseConnection = true;
            }
            
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Database connection is null");
                return null;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, userId);
                stmt.setBigDecimal(2, amount);
                stmt.setString(3, Payment.methodToDbValue(method));
                setStringOrNull(stmt, 4, referenceId);
                stmt.setLong(5, membershipId);

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            return rs.getInt(1);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating payment for membership", e);
            throw new RuntimeException("Failed to create payment for membership: " + e.getMessage(), e);
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

        return null;
    }

    /**
     * Update payment status
     */
    public boolean updateStatus(Integer paymentId, PaymentStatus newStatus) {
        String sql = "UPDATE payments SET status = ? WHERE payment_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Database connection is null");
                return false;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, Payment.statusToDbValue(newStatus));
                stmt.setInt(2, paymentId);

                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating payment status: " + paymentId, e);
            throw new RuntimeException("Failed to update payment status: " + e.getMessage(), e);
        }
    }

    /**
     * Update payment status and reference ID
     */
    public boolean updateStatusAndReference(Integer paymentId, PaymentStatus newStatus, String referenceId) {
        String sql = "UPDATE payments SET status = ?, reference_id = ? WHERE payment_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Database connection is null");
                return false;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, Payment.statusToDbValue(newStatus));
                setStringOrNull(stmt, 2, referenceId);
                stmt.setInt(3, paymentId);

                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating payment status and reference: " + paymentId, e);
            throw new RuntimeException("Failed to update payment: " + e.getMessage(), e);
        }
    }

    /**
     * Find payments by order ID
     */
    public List<Payment> findByOrder(Long orderId) {
        List<Payment> payments = new ArrayList<>();
        
        String sql = "SELECT payment_id, user_id, amount, payment_date, method, status, " +
                    "reference_id, transaction_type, membership_id, order_id, notes " +
                    "FROM payments " +
                    "WHERE order_id = ? " +
                    "ORDER BY payment_date DESC";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                return payments;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, orderId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    payments.add(mapResultSetToPayment(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding payments by order: " + orderId, e);
        }

        return payments;
    }

    /**
     * Find payments by membership ID
     */
    public List<Payment> findByMembership(Long membershipId) {
        List<Payment> payments = new ArrayList<>();
        
        String sql = "SELECT payment_id, user_id, amount, payment_date, method, status, " +
                    "reference_id, transaction_type, membership_id, order_id, notes " +
                    "FROM payments " +
                    "WHERE membership_id = ? " +
                    "ORDER BY payment_date DESC";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                return payments;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, membershipId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    payments.add(mapResultSetToPayment(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding payments by membership: " + membershipId, e);
        }

        return payments;
    }

    /**
     * Find payment by ID
     */
    public Optional<Payment> findById(Integer paymentId) {
        String sql = "SELECT payment_id, user_id, amount, payment_date, method, status, " +
                    "reference_id, transaction_type, membership_id, order_id, notes " +
                    "FROM payments WHERE payment_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                return Optional.empty();
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, paymentId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(mapResultSetToPayment(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding payment by ID: " + paymentId, e);
        }

        return Optional.empty();
    }

    /**
     * Find payment by reference ID
     */
    public Optional<Payment> findByReferenceId(String referenceId) {
        String sql = "SELECT payment_id, user_id, amount, payment_date, method, status, " +
                    "reference_id, transaction_type, membership_id, order_id, notes " +
                    "FROM payments WHERE reference_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                return Optional.empty();
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, referenceId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(mapResultSetToPayment(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding payment by reference ID: " + referenceId, e);
        }

        return Optional.empty();
    }

    /**
     * Map ResultSet to Payment object
     */
    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();

        payment.setPaymentId(rs.getInt("payment_id"));
        payment.setUserId(rs.getInt("user_id"));
        payment.setAmount(rs.getBigDecimal("amount"));

        Timestamp paymentDate = rs.getTimestamp("payment_date");
        if (paymentDate != null) {
            payment.setPaymentDate(paymentDate.toLocalDateTime());
        }

        String methodStr = rs.getString("method");
        payment.setMethod(Payment.methodFromDbValue(methodStr));

        String statusStr = rs.getString("status");
        payment.setStatus(Payment.statusFromDbValue(statusStr));

        payment.setReferenceId(rs.getString("reference_id"));

        String transactionTypeStr = rs.getString("transaction_type");
        payment.setTransactionType(Payment.TransactionType.fromCode(transactionTypeStr));

        long membershipId = rs.getLong("membership_id");
        if (!rs.wasNull()) {
            payment.setMembershipId(membershipId);
        }

        long orderId = rs.getLong("order_id");
        if (!rs.wasNull()) {
            payment.setOrderId(orderId);
        }

        payment.setNotes(rs.getString("notes"));

        return payment;
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

