package ku.cs.tmsg.dto.request;

import lombok.Data;

@Data
public class UpdateSentGasWeightRequest {
    private String orderID;
    private double weight;
}
