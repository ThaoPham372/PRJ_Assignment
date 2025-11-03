package com.gym.model.shop;

/**
 * Order status enumeration
 */
public enum OrderStatus {
    PENDING("PENDING", "Chờ xử lý"),
    CONFIRMED("CONFIRMED", "Đã xác nhận"),
    PREPARING("PREPARING", "Đang chuẩn bị"),
    READY("READY", "Sẵn sàng"),
    PROCESSING("processing", "Đang xử lý"),  // Keep for backward compatibility
    SHIPPED("shipped", "Đã giao hàng"),  // Keep for backward compatibility
    COMPLETED("COMPLETED", "Hoàn thành"),
    DELIVERED("delivered", "Đã nhận hàng"),  // Keep for backward compatibility
    CANCELLED("CANCELLED", "Đã hủy");

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



