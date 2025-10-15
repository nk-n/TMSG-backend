package ku.cs.tmsg.dto.request;

import lombok.Data;

@Data
public class TaskStatusUpdateRequest {
    private String orderID;
    private String status;
}
