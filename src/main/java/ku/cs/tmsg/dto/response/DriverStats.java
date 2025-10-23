package ku.cs.tmsg.dto.response;

import lombok.Data;

@Data
public class DriverStats {
    private int distanceDeliveryDay;
    private int incomeDeliveryDay;
    private int distanceDeliveryMonth;
    private int incomeDeliveryMonth;
}
