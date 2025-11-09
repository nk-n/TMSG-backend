package ku.cs.tmsg.dto.response;

import lombok.Data;

@Data
public class UpdateSentGasWeightResponse {
    private String orderID;
    private double weight;
}
