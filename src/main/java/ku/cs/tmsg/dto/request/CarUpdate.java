package ku.cs.tmsg.dto.request;

import ku.cs.tmsg.entity.enums.CarAndDriverStatus;
import lombok.Data;

@Data
public class CarUpdate {
    private boolean available;
    private CarAndDriverStatus status;
    private String id;
}
