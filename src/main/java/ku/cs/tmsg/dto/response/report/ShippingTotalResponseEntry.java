package ku.cs.tmsg.dto.response.report;

import lombok.Data;

@Data
public class ShippingTotalResponseEntry {
    private int month;
    private int total;

    public ShippingTotalResponseEntry(int totalSum, int month) {
        this.month = month;
        this.total = totalSum;
    }
}
