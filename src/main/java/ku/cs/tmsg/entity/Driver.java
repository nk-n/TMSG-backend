package ku.cs.tmsg.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import ku.cs.tmsg.entity.enums.CarAndDriverStatus;
import lombok.Data;

@Data
@Entity
@Table(name="พนักงานขับรถ")
public class Driver {
    @Id
    @Column(name="เบอร์โทร",unique=true, nullable=false)
    private String tel;
    @Column(name="ชื่อ")
    private String name;
    @Column(name="Line_ID")
    private String line_ID;
    @Column(name="หมายเหตุพนักงานขับรถ")
    private String note;
    @Column(name="พร้อมทำงาน")
    private boolean available ;
    @Column(name="สถานะพนักงาน")
    private CarAndDriverStatus status;
}
