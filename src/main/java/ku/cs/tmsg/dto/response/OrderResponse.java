package ku.cs.tmsg.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class OrderResponse {
    private String car_id;

    private List<OrderDriverResponse> drivers;

    private String order_status;
    private String order_id;
    private String deadline;
    private String load_time;
    private String note;
    private String destination;
    private int time_use;
    private int gas_amount;
    private int drop;
    private int gas_send;
    private List<OrderDeliveryStatusResponse> delivery_status;

}
