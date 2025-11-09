package model.schedule;

/**
 * Cancelled by enumeration for PT bookings
 */
public enum CancelledBy {
    MEMBER("MEMBER", "Thành viên"),
    TRAINER("TRAINER", "Huấn luyện viên"),
    ADMIN("ADMIN", "Quản trị viên");

    private final String code;
    private final String displayName;

    CancelledBy(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static CancelledBy fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (CancelledBy cancelledBy : values()) {
            if (cancelledBy.code.equalsIgnoreCase(code)) {
                return cancelledBy;
            }
        }
        return null;
    }
}

