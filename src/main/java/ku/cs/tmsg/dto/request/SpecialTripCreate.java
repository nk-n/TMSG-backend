package ku.cs.tmsg.dto.request;

import lombok.Data;

@Data
public class SpecialTripCreate {
    private String trip_id;
    private String reason;
    private String money;
}
