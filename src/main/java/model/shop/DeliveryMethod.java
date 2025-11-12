package model.shop;

/**
 * Delivery method enumeration
 */
public enum DeliveryMethod {
    PICKUP("pickup", "Nhận trực tiếp tại cửa hàng"),
    DELIVERY("delivery", "Giao hàng online");

    private final String code;
    private final String displayName;

    DeliveryMethod(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static DeliveryMethod fromCode(String code) {
        if (code == null) {
            return DELIVERY;
        }
        for (DeliveryMethod method : values()) {
            if (method.code.equalsIgnoreCase(code)) {
                return method;
            }
        }
        return DELIVERY;
    }
}