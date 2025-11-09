package ku.cs.tmsg.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;

import java.util.Date;

@Data
@Entity
@Table(name = "ประวัติสถานะ")
public class OrderStatus {

    @Id
    @Column(name = "history_status_id")
    private String historyStatusId;

    @Column(name = "timestamp_status")
    private Date timestampStatus;

    @Column(name = "สถานะ")
    private String orderStatus;

    @Column(name = "order_id")
    private String orderId;
}
