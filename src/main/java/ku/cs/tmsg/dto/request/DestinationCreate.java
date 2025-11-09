package ku.cs.tmsg.dto.request;

import lombok.Data;

@Data
public class DestinationCreate {
    private String name;
    private String address;
    private String province;
    private String region;
    private int distance;
    private String route;
    private int timeUse;
}
