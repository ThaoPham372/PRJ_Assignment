package com.gym.model.shop;

/**
 * Order status enumeration
 */
public enum OrderStatus {
    PENDING("pending", "Chờ xử lý"),
    PROCESSING("processing", "Đang xử lý"),
    SHIPPED("shipped", "Đã giao hàng"),
    DELIVERED("delivered", "Đã nhận hàng"),
    CANCELLED("cancelled", "Đã hủy");

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



