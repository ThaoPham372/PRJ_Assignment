package model.schedule;

/**
 * Day of week enumeration for trainer schedules
 */
public enum DayOfWeek {
    MONDAY("MONDAY", "Thứ Hai"),
    TUESDAY("TUESDAY", "Thứ Ba"),
    WEDNESDAY("WEDNESDAY", "Thứ Tư"),
    THURSDAY("THURSDAY", "Thứ Năm"),
    FRIDAY("FRIDAY", "Thứ Sáu"),
    SATURDAY("SATURDAY", "Thứ Bảy"),
    SUNDAY("SUNDAY", "Chủ Nhật");

    private final String code;
    private final String displayName;

    DayOfWeek(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static DayOfWeek fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (DayOfWeek day : values()) {
            if (day.code.equalsIgnoreCase(code)) {
                return day;
            }
        }
        return null;
    }
}

