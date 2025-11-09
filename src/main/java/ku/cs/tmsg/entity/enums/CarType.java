package ku.cs.tmsg.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Map;

public enum CarType {
    TENWHEEL("สิบล้อ"),
    TRACTOR("หัวลาก"),
    SEMITRAILER("กึ่งพ่วง");


    private final String displayName;

    CarType(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static CarType fromLabel(String label) {
        return Arrays.stream(values())
                .filter(w -> w.getDisplayName().equals(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid type: " + label));
    }
}
