package ku.cs.tmsg.dto.request;

import lombok.Data;

@Data
public class CarCreate {
    private String status;
    private String license;
    private String id;
    private String weight;
    private String type;
}
