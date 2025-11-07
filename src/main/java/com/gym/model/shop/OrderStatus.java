package com.gym.model.shop;

/**
 * Order status enumeration
 * Only 3 statuses:
 * - PENDING: Chỉ đặt chưa thanh toán (chờ thanh toán)
 * - COMPLETED: Đã thanh toán
 * - CANCELLED: Đã bị hủy
 */
public enum OrderStatus {
    PENDING("PENDING", "Chờ thanh toán"),
    COMPLETED("COMPLETED", "Đã thanh toán"),
    CANCELLED("CANCELLED", "Đã bị hủy");

    private final String code;
    private final String displayName;

    OrderStatus(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static OrderStatus fromCode(String code) {
        if (code == null) {
            return PENDING;
        }
        for (OrderStatus status : values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        return PENDING;
    }
}



