package ku.cs.tmsg.dto.response;

import lombok.Data;

@Data
public class SpecialTripResponse {
    private String special_trip_id;
    private String reason;
    private Double money;
}
