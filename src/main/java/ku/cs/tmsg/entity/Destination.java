package ku.cs.tmsg.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name="สถานที่จัดส่งปลายทาง")
public class Destination {
    @Id
    @Column(name="ชื่อสถานที่",unique=true, nullable=false)
    private String name;
    @Column(name="จังหวัด")
    private String province;
    @Column(name="ภูมิภาค")
    private String region;
    @Column(name="พร้อมรับสินค้า")
    private boolean available;
    @Column(name="ที่อยู่")
    private String address;
    @Column(name="ระยะทางจาก_SCBPK")
    private int distance;
    @Column(name="ระยะเวลาที่ใช้เดินทาง_SCBPK")
    private int timeUse;
    @Column(name="เส้นทาง")
    private String route;
}
