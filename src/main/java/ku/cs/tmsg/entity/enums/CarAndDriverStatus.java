package ku.cs.tmsg.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum CarAndDriverStatus {
    READY("พร้อม"),
    NOTREADY("ไม่พร้อม"),
    INPROGRESS("ระหว่างจัดส่ง");

    private final String displayName;

    CarAndDriverStatus(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static CarAndDriverStatus fromLabel(String label) {
        return Arrays.stream(values())
                .filter(w -> w.getDisplayName().equals(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid status: " + label));
    }
}
