package ku.cs.tmsg.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Map;

public enum OrderStatus {
    WAITING("รอรับงาน"),
    INPROGRESS("ระหว่างจัดส่งสินค้า"),
    VERIFY("รออนุมัติ"),
    APPROVE("อนุมัติ");


    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }


    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static OrderStatus fromLabel(String label) {
        return Arrays.stream(values())
                .filter(w -> w.getDisplayName().equals(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid type: " + label));
    }
}
