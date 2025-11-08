package model;

/**
 * Product type enumeration
 * NOTE: Database stores values as UPPERCASE ('SUPPLEMENT', 'EQUIPMENT', 'APPAREL', 'ACCESSORY', 'OTHER')
 */
public enum ProductType {
    SUPPLEMENT("SUPPLEMENT", "Thực phẩm bổ sung"),
    EQUIPMENT("EQUIPMENT", "Thiết bị"),
    APPAREL("APPAREL", "Trang phục"),  // Database uses APPAREL, not CLOTHING
    ACCESSORY("ACCESSORY", "Phụ kiện"),
    OTHER("OTHER", "Khác");

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

    /**
     * Convert database value to ProductType enum
     * Handles both UPPERCASE (from database) and lowercase (from form inputs)
     */
    public static ProductType fromCode(String code) {
        if (code == null) {
            return OTHER;
        }
        // Normalize to uppercase for comparison (database stores uppercase)
        String normalizedCode = code.toUpperCase().trim();
        for (ProductType type : values()) {
            if (type.code.equals(normalizedCode)) {
                return type;
            }
        }
        return OTHER;
    }
}



