package ku.cs.tmsg.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderCreate {
    private String id;
    private String deadline;
    private int gas_amount;
    private String source;
    private String destination;
    private int drop;
    private String note;
    private String car_id;
    private String tel1;
    private String tel2;
    private String load_time;
}
