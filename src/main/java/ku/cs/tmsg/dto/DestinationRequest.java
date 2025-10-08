package ku.cs.tmsg.dto;

import lombok.Data;

@Data
public class DestinationRequest {
    private String name;
    private String address;
    private String province;
    private String region;
    private int distance;
    private String route;
    private int timeUse;
}
