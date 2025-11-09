package ku.cs.tmsg.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Map;

public enum CarWeight {
    EIGHTTON("8"),
    TENTON("10"),
    TRAILER("เทรลเลอร์");


    private final String displayName;

    CarWeight(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static CarWeight fromLabel(String label) {
        return Arrays.stream(values())
                .filter(w -> w.getDisplayName().equals(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid weight: " + label));
    }
}
