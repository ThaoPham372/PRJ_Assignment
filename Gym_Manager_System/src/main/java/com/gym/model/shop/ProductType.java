package com.gym.model.shop;

/**
 * Product type enumeration
 */
public enum ProductType {
    SUPPLEMENT("supplement", "Thực phẩm bổ sung"),
    EQUIPMENT("equipment", "Thiết bị"),
    CLOTHING("clothing", "Trang phục"),
    OTHER("other", "Khác");

    private final String code;
    private final String displayName;

    ProductType(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ProductType fromCode(String code) {
        if (code == null) {
            return OTHER;
        }
        for (ProductType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return OTHER;
    }
}



