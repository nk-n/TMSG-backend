package ku.cs.tmsg.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import ku.cs.tmsg.entity.enums.OrderStatus;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name="ออเดอร์")
public class Order {
    @Id
    @Column(name="order_id",unique=true, nullable=false)
    private String id;
    @Column(name="ต้นทาง")
    private String source;
    @Column(name="สถานะออเดอร์")
    private OrderStatus status;
    @Column(name="หมายเหตุ")
    private String note;
    @Column(name="order_date")
    private Date order_date;
    @Column(name="ปริมาณแก๊ส")
    private int gas_amount;
    @Column(name="ปริมาณแก๊สที่ส่งให้ลูกค้า")
    private int gas_send;
    @Column(name="เวลาเข้าโหลด")
    private Date load_time;
    @Column(name="เวลาที่ต้องเสร็จ")
    private Date deadline;
    @Column(name="จุดดรอป")
    private int drop;
    @Column(name="delivery_id")
    private String delivery_id;
    @Column(name="ชื่อสถานที่")
    private String destination;
}
