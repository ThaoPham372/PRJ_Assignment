package com.gym.model.shop;

/**
 * Payment method enumeration
 */
public enum PaymentMethod {
    CASH("cash", "Tiền mặt"),
    CREDIT_CARD("credit_card", "Thẻ tín dụng"),
    BANK_TRANSFER("bank_transfer", "Chuyển khoản"),
    MOMO("momo", "MoMo"),
    VNPAY("vnpay", "VNPay");

    private final String code;
    private final String displayName;

    PaymentMethod(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PaymentMethod fromCode(String code) {
        if (code == null) {
            return CASH;
        }
        for (PaymentMethod method : values()) {
            if (method.code.equalsIgnoreCase(code)) {
                return method;
            }
        }
        return CASH;
    }
}



