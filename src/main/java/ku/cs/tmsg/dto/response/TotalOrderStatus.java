package ku.cs.tmsg.dto.response;

import lombok.Data;

@Data
public class TotalOrderStatus {
    private int totalWaitingOrder;
    private int totalDeliveryOrder;
    private int totalWaitingApproveOrder;
    private int totalApproveOrder;
}
