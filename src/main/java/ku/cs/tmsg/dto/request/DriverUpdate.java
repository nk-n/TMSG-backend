package ku.cs.tmsg.dto.request;

import ku.cs.tmsg.entity.enums.CarAndDriverStatus;
import lombok.Data;

@Data
public class DriverUpdate {
    private boolean available;
    private CarAndDriverStatus status;
    private String tel;
}
