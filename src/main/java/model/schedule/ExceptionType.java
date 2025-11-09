package model.schedule;

/**
 * Exception type enumeration for trainer exceptions
 */
public enum ExceptionType {
    OFF("OFF", "Nghỉ"),
    BUSY("BUSY", "Bận"),
    SPECIAL("SPECIAL", "Lịch đặc biệt");

    private final String code;
    private final String displayName;

    ExceptionType(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ExceptionType fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (ExceptionType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }
}

