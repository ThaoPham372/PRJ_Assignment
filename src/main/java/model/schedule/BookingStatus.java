package model.schedule;

/**
 * Booking status enumeration for PT bookings
 */
public enum BookingStatus {
    PENDING("PENDING", "Chờ xác nhận"),
    CONFIRMED("CONFIRMED", "Đã xác nhận"),
    COMPLETED("COMPLETED", "Hoàn thành"),
    CANCELLED("CANCELLED", "Đã hủy");

    private final String code;
    private final String displayName;

    BookingStatus(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static BookingStatus fromCode(String code) {
        if (code == null) {
            return PENDING;
        }
        for (BookingStatus status : values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        return PENDING;
    }
}

