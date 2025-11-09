package ku.cs.tmsg.repository;

import ku.cs.tmsg.dto.response.report.ShippingTotalResponse;
import ku.cs.tmsg.dto.response.report.ShippingTotalResponseEntry;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class ReportRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ReportRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public ShippingTotalResponse getTotalShipping(int year, String type) {
        String queryDelivery = """ 
              SELECT sub.`น้ำหนัก`, sub.month, SUM(sub.total_sum) as total_sum, sub.order_id FROM (
              SELECT DISTINCT car.`น้ำหนัก`, MONTH(o.`เวลาที่ต้องเสร็จ`) AS month, o.`ปริมาณแก๊สที่ส่งให้ลูกค้า` AS total_sum, o.order_id
              FROM
                   `การจัดส่ง` AS d
                   JOIN `ออเดอร์` AS o ON o.order_id = d.order_id
                   JOIN `รถขนส่ง` AS car ON car.`เบอร์รถ` = d.`เบอร์รถ`
              WHERE
                   o.`สถานะออเดอร์` = 'อนุมัติ'
                   AND YEAR(o.`เวลาที่ต้องเสร็จ`) = ?
              ) as sub
              GROUP BY sub.month, sub.`น้ำหนัก`
              ORDER BY
              sub.`น้ำหนัก`, sub.month
                """;
        String queryTravel = """
               SELECT sub.`น้ำหนัก`, sub.month, COUNT(sub.order_id) as total_sum FROM (
               SELECT DISTINCT car.`น้ำหนัก`, MONTH(o.`เวลาที่ต้องเสร็จ`) AS month, o.order_id
               FROM
                    `การจัดส่ง` AS d
                    JOIN `ออเดอร์` AS o ON o.order_id = d.order_id
                    JOIN `รถขนส่ง` AS car ON car.`เบอร์รถ` = d.`เบอร์รถ`
               WHERE
                    o.`สถานะออเดอร์` = 'อนุมัติ'
                    AND YEAR(o.`เวลาที่ต้องเสร็จ`) = ?
               ) as sub
               GROUP BY sub.month, sub.`น้ำหนัก`
               ORDER BY
               sub.`น้ำหนัก`, sub.month
                """;
        String queryDistance = """
              SELECT sub.`น้ำหนัก`, sub.month, SUM(sub.total_sum) as total_sum, sub.order_id FROM (
              SELECT DISTINCT car.`น้ำหนัก`, MONTH(o.`เวลาที่ต้องเสร็จ`) AS month, dest.`ระยะทางจาก_SCBPK` AS total_sum, o.order_id
              FROM
                   `การจัดส่ง` AS d
                   JOIN `ออเดอร์` AS o ON o.order_id = d.order_id
                   JOIN `รถขนส่ง` AS car ON car.`เบอร์รถ` = d.`เบอร์รถ`
                   JOIN `สถานที่จัดส่งปลายทาง` AS dest ON o.`ปลายทาง` = dest.`ชื่อสถานที่`
              WHERE
                   o.`สถานะออเดอร์` = 'อนุมัติ'
                   AND YEAR(o.`เวลาที่ต้องเสร็จ`) = ?
              ) as sub
              GROUP BY sub.month, sub.`น้ำหนัก`
              ORDER BY
              sub.`น้ำหนัก`, sub.month
                """;
        String queryCar = """
                  SELECT sub.`น้ำหนัก`, sub.month, COUNT(sub.total_sum) as total_sum FROM (
                  SELECT DISTINCT car.`น้ำหนัก`, MONTH(o.`เวลาที่ต้องเสร็จ`) AS month, d.เบอร์รถ AS total_sum
                  FROM
                       `การจัดส่ง` AS d
                       JOIN `ออเดอร์` AS o ON o.order_id = d.order_id
                       JOIN `รถขนส่ง` AS car ON car.`เบอร์รถ` = d.`เบอร์รถ`
                       JOIN `สถานที่จัดส่งปลายทาง` AS dest ON o.`ปลายทาง` = dest.`ชื่อสถานที่`
                  WHERE
                       o.`สถานะออเดอร์` = 'อนุมัติ'
                       AND YEAR(o.`เวลาที่ต้องเสร็จ`) = ?
                  ) as sub
                  GROUP BY sub.month, sub.`น้ำหนัก`
                  ORDER BY
                  sub.`น้ำหนัก`, sub.month
                """;
        String queryDriver = """
              SELECT sub.`น้ำหนัก`, sub.month, COUNT(sub.total_sum) as total_sum FROM (
              SELECT DISTINCT car.`น้ำหนัก`, MONTH(o.`เวลาที่ต้องเสร็จ`) AS month, d.เบอร์โทร AS total_sum
              FROM
                   `การจัดส่ง` AS d
                   JOIN `ออเดอร์` AS o ON o.order_id = d.order_id
                   JOIN `รถขนส่ง` AS car ON car.`เบอร์รถ` = d.`เบอร์รถ`
                   JOIN `สถานที่จัดส่งปลายทาง` AS dest ON o.`ปลายทาง` = dest.`ชื่อสถานที่`
              WHERE
                   o.`สถานะออเดอร์` = 'อนุมัติ'
                   AND YEAR(o.`เวลาที่ต้องเสร็จ`) = ?
              ) as sub
              GROUP BY sub.month, sub.`น้ำหนัก`
              ORDER BY
              sub.`น้ำหนัก`, sub.month
                """;
        List<ShippingEntry> shipping = List.of();
        if (Objects.equals(type, "ยอดขน")) {
           shipping = jdbcTemplate.query(queryDelivery, new ShippingMapper() ,year);
        } else if (Objects.equals(type, "เที่ยววิ่ง")) {
            shipping = jdbcTemplate.query(queryTravel, new ShippingMapper() ,year);
        } else if (Objects.equals(type, "ระยะทาง")) {
            shipping = jdbcTemplate.query(queryDistance, new ShippingMapper() ,year);
        } else if (Objects.equals(type, "รถ")) {
            shipping = jdbcTemplate.query(queryCar, new ShippingMapper() ,year);
        } else if (Objects.equals(type, "พนักงานขนส่ง")) {
            shipping = jdbcTemplate.query(queryDriver, new ShippingMapper() ,year);
        }
        ShippingTotalResponse entry = new ShippingTotalResponse();
        entry.setShipping_8_ton(new ArrayList<>());
        entry.setShipping_10_ton(new ArrayList<>());
        entry.setShipping_trailer(new ArrayList<>());
        for (ShippingEntry element : shipping) {
            switch (element.getWeight()) {
                case "8" :
                    entry.getShipping_8_ton().add(new ShippingTotalResponseEntry(element.getTotal_sum(), element.getMonth())); break;
                case "10" :
                    entry.getShipping_10_ton().add(new ShippingTotalResponseEntry(element.getTotal_sum(), element.getMonth())); break;
                case "เทรลเลอร์" :
                    entry.getShipping_trailer().add(new ShippingTotalResponseEntry(element.getTotal_sum(), element.getMonth())); break;
            }
        }

        return entry;
    }

    class ShippingMapper implements RowMapper<ShippingEntry> {
        @Override
        public ShippingEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
            ShippingEntry shippingEntry = new ShippingEntry();
            shippingEntry.setWeight(rs.getString("น้ำหนัก"));
            shippingEntry.setTotal_sum(rs.getInt("total_sum"));
            shippingEntry.setMonth(rs.getInt("month"));

            return shippingEntry;
        }
    }
    @Data
    class ShippingEntry {
        private String weight;
        private int month;
        private int total_sum;
    }
}
