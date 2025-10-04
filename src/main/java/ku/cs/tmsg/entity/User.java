package ku.cs.tmsg.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="พนักงานจัดส่ง")
public class User {
    @Id
    @Column(name="ID",unique=true, nullable=false)
    private String id;
    @Column(name="ชื่อ")
    private String name;
    @Column(name="พนักงานจัดส่งพร้อมทำงาน")
    private boolean status;
    @Column(name="เบอร์โทร")
    private String phone;
    @Column(name="ตำแหน่ง")
    private String role;
    @Column(name="รหัสผ่าน")
    private String password;
}
