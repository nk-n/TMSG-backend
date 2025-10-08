package ku.cs.tmsg.entity;

import jakarta.persistence.*;
import ku.cs.tmsg.entity.enums.CarAndDriverStatus;
import ku.cs.tmsg.entity.enums.CarType;
import ku.cs.tmsg.entity.enums.CarWeight;
import lombok.Data;

@Data
@Entity
@Table(name="รถขนส่ง")
public class Car {
    @Id
    @Column(name="เบอร์รถ",unique=true, nullable=false)
    private String id;
    @Column(name="ทะเบียนรถ")
    private String license;
    @Column(name="สถานะรถ")
    private CarAndDriverStatus status;
    @Column(name="น้ำหนัก")
    private CarWeight weight;
    @Column(name="ประเภทรถ")
    private CarType type;
    @Column(name="พร้อมขนส่ง")
    private boolean available;
    @Column(name="หมายเหตุรถ")
    private String note;
}
