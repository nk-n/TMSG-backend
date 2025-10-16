package ku.cs.tmsg.dto.request;

import lombok.Data;

@Data
public class OrderUpdateStatus {
   private String order_id;
   private String status;
}
