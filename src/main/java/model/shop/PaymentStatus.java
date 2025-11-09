package model.shop;

/**
 * Payment status enumeration
 */
public enum PaymentStatus {
    PENDING("pending", "Chờ thanh toán"),
    PAID("paid", "Đã thanh toán"),
    FAILED("failed", "Thanh toán thất bại"),
    REFUNDED("refunded", "Đã hoàn tiền"),
    CANCELED("canceled", "Đã hủy"),
    EXPIRED("expired", "Hết hạn thanh toán"),
    CHARGEBACK("chargeback", "Tranh chấp/hoàn tiền ngân hàng");

    private final String code;
    private final String displayName;

    PaymentStatus(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PaymentStatus fromCode(String code) {
        if (code == null) {
            return PENDING;
        }
        for (PaymentStatus status : values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        return PENDING;
    }
}