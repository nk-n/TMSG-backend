package ku.cs.tmsg.repository;

import jakarta.transaction.Transactional;
import ku.cs.tmsg.entity.Driver;
import ku.cs.tmsg.entity.Order;
import ku.cs.tmsg.exception.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.UUID;

@Repository
public class OrderRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void save(Order order, String carNumber, String tel1, String tel2) throws Exception {
        try {
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=0");
            String tripId = UUID.randomUUID().toString();
            String query = """
            INSERT INTO `ค่าเที่ยว` (`จำนวนเงิน`, `trip_id`)
            VALUES (?, ?)
            """;
            jdbcTemplate.update(query, "0", tripId);

            query = """
                INSERT INTO ออเดอร์ (order_id,ต้นทาง,สถานะออเดอร์,หมายเหตุ,order_date,ปริมาณแก๊ส,ปริมาณแก๊สที่ส่งให้ลูกค้า,เวลาเข้าโหลด,เวลาที่ต้องเสร็จ,จุดดรอป,ปลายทาง)
                VALUES (?,?,?,?,?,?,?,?,?,?,?)
                """;
            jdbcTemplate.update(query, order.getId(), order.getSource(), order.getStatus().getDisplayName(), order.getNote(), order.getOrder_date(), order.getGas_amount(), order.getGas_send(), order.getLoad_time(), order.getDeadline(), order.getDrop(),  order.getDestination());

            String deliveryId1 = UUID.randomUUID().toString();
            query = """
                INSERT INTO `การจัดส่ง` (`delivery_id`,`เบอร์รถ`,`เบอร์โทร`,`trip_id`,`order_id`)
                VALUES (?,?,?,?,?)
                """;
            jdbcTemplate.update(query, deliveryId1, carNumber, tel1, tripId, order.getId());

            if (!Objects.equals(tel2, "")) {
                String deliveryId2 = UUID.randomUUID().toString();
                query = """
                INSERT INTO `การจัดส่ง` (`delivery_id`,`เบอร์รถ`,`เบอร์โทร`,`trip_id`,`order_id`)
                VALUES (?,?,?,?,?)
               """;
                jdbcTemplate.update(query,deliveryId2, carNumber, tel2, tripId, order.getId());
            }
        } finally {
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=1");
        }



    }
}
