package ku.cs.tmsg.repository;

import jakarta.transaction.Transactional;
import ku.cs.tmsg.dto.response.OrderDeliveryStatusResponse;
import ku.cs.tmsg.dto.response.OrderDriverResponse;
import ku.cs.tmsg.dto.response.OrderResponse;
import ku.cs.tmsg.entity.Car;
import ku.cs.tmsg.entity.Driver;
import ku.cs.tmsg.entity.Order;
import ku.cs.tmsg.entity.enums.CarAndDriverStatus;
import ku.cs.tmsg.entity.enums.CarType;
import ku.cs.tmsg.entity.enums.CarWeight;
import ku.cs.tmsg.entity.enums.OrderStatus;
import ku.cs.tmsg.exception.DatabaseException;
import ku.cs.tmsg.service.DBResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

@Repository
public class OrderRepository {
    private JdbcTemplate jdbcTemplate;
    private DBResultUtils dbResultUtils;

    @Autowired
    public OrderRepository(JdbcTemplate jdbcTemplate, DBResultUtils dbResultUtils) {
        this.jdbcTemplate = jdbcTemplate;
        this.dbResultUtils = dbResultUtils;
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

    public List<OrderResponse> get(String orderStatus) {
        String query = """
                SELECT `เบอร์รถ`, d.`order_id`, `หมายเหตุ`, `เวลาที่ต้องเสร็จ`, `สถานะออเดอร์`, `ปลายทาง`, `จุดดรอป`, `ปริมาณแก๊ส`, `ปริมาณแก๊สที่ส่งให้ลูกค้า`, driver.`ชื่อ`, driver.`เบอร์โทร`, `เวลาเข้าโหลด`, dest.`ระยะเวลาที่ใช้เดินทาง_SCBPK`, pay.trip_id, pay.`จำนวนเงิน`, dest.`ระยะทางจาก_SCBPK`
                FROM `การจัดส่ง` as d
                JOIN `ออเดอร์` as o ON o.order_id = d.order_id
                JOIN `พนักงานขับรถ` as driver ON d.เบอร์โทร = driver.`เบอร์โทร`
                JOIN `สถานที่จัดส่งปลายทาง` as dest ON dest.`ชื่อสถานที่` = o.`ปลายทาง`
                JOIN `ค่าเที่ยว` as pay ON pay.trip_id = d.trip_id
                WHERE `สถานะออเดอร์` = ?
                ORDER BY o.`เวลาที่ต้องเสร็จ`
                """;
        List<OrderResponse> orders = jdbcTemplate.query(query, new OrderRepository.OrderResponseExtractor(), orderStatus);

        String queryDeliveryStatus = """
                SELECT timestamp_status, `สถานะ`
                FROM `ประวัติสถานะ`
                WHERE order_id = ?
                """;
        if (orders != null) {
            for (OrderResponse orderResponse : orders) {
                orderResponse.setDelivery_status(new ArrayList<>());
                List<OrderDeliveryStatusResponse> statusList = jdbcTemplate.query(queryDeliveryStatus, new OrderRepository.OrderDeliveryStatusMapper(), orderResponse.getOrder_id());
                orderResponse.getDelivery_status().addAll(statusList);
            }
        }

        return orders;
    }

    public List<Order> getNewOrder(String phone) {
        String query = """
            SELECT
              order_id,
              ต้นทาง,
              หมายเหตุ,
              ปริมาณแก๊ส,
              เวลาเข้าโหลด,
              เวลาที่ต้องเสร็จ,
              จุดดรอป,
              ปลายทาง,
              ลำดับเที่ยว
            FROM ออเดอร์
            WHERE ลำดับเที่ยว =
                (SELECT o.ลำดับเที่ยว FROM ออเดอร์ AS o
                INNER JOIN การจัดส่ง AS d
                ON o.order_id = d.order_id
                WHERE d.เบอร์โทร = ? AND o.สถานะออเดอร์ = 'รอรับงาน' ORDER BY o.เวลาเข้าโหลด ASC LIMIT 1)
            """;
        return jdbcTemplate.query(query, new OrderRepository.OrderMapper(), phone);
    }

    class OrderDeliveryStatusMapper implements RowMapper<OrderDeliveryStatusResponse> {
        @Override
        public OrderDeliveryStatusResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            OrderDeliveryStatusResponse status = new OrderDeliveryStatusResponse();
            status.setStatus(rs.getString("สถานะ"));
            status.setTimestamp(rs.getString("timestamp_status"));
            return status;
        }
    }

    public class OrderResponseExtractor implements ResultSetExtractor<List<OrderResponse>> {

        @Override
        public List<OrderResponse> extractData(ResultSet rs) throws SQLException {
            Map<String, OrderResponse> orderMap = new LinkedHashMap<>();

            while (rs.next()) {
                String orderId = rs.getString("order_id");
                OrderResponse order = orderMap.get(orderId);

                if (order == null) {
                    order = new OrderResponse();
                    order.setOrder_id(orderId);
                    order.setCar_id(rs.getString("เบอร์รถ"));
                    order.setNote(rs.getString("หมายเหตุ"));
                    order.setDeadline(rs.getString("เวลาที่ต้องเสร็จ"));
                    order.setLoad_time(rs.getString("เวลาเข้าโหลด"));
                    order.setOrder_status(rs.getString("สถานะออเดอร์"));
                    order.setDestination(rs.getString("ปลายทาง"));
                    order.setDistance(rs.getInt("ระยะทางจาก_SCBPK"));
                    order.setDrop(rs.getInt("จุดดรอป"));
                    order.setTime_use(rs.getInt("ระยะเวลาที่ใช้เดินทาง_SCBPK"));
                    order.setGas_amount(rs.getInt("ปริมาณแก๊ส"));
                    order.setGas_send(rs.getInt("ปริมาณแก๊สที่ส่งให้ลูกค้า"));
                    order.setTrip_id(rs.getString("trip_id"));
                    order.setMoney(rs.getDouble("จำนวนเงิน"));
                    order.setDrivers(new ArrayList<>());
                    orderMap.put(orderId, order);
                }

                OrderDriverResponse driver = new OrderDriverResponse();
                driver.setName(rs.getString("ชื่อ"));
                driver.setTel(rs.getString("เบอร์โทร"));
                order.getDrivers().add(driver);
            }

            return new ArrayList<>(orderMap.values());
        }
    }

    public void updateStatus(String orderId, String status) {
        String query = """
                UPDATE `ออเดอร์`
                SET `สถานะออเดอร์` = ?
                WHERE `order_id` = ?
                """;
        jdbcTemplate.update(query, status, orderId);
    }


    private class OrderMapper implements RowMapper<Order> {
        @Override
        public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
            Order order = new Order();
            Set<String> columnNames = dbResultUtils.getColumnNames(rs);
            if (columnNames.contains("order_id")) {
                order.setId(rs.getString("order_id"));
            }
            if (columnNames.contains("ต้นทาง")) {
                order.setSource(rs.getString("ต้นทาง"));
            }
            if (columnNames.contains("สถานะออเดอร์")) {
                order.setStatus(OrderStatus.fromLabel(rs.getString("สถานะออเดอร์")));
            }
            if (columnNames.contains("หมายเหตุ")) {
                order.setNote(rs.getString("หมายเหตุ"));
            }
            if (columnNames.contains("order_date")) {
                order.setOrder_date(rs.getDate("order_date"));
            }
            if (columnNames.contains("ปริมาณแก๊ส")) {
                order.setGas_amount(rs.getInt("ปริมาณแก๊ส"));
            }
            if (columnNames.contains("ปริมาณแก๊สที่ส่งให้ลูกค้า")) {
                order.setGas_send(rs.getInt("ปริมาณแก๊สที่ส่งให้ลูกค้า"));
            }
            if (columnNames.contains("เวลาเข้าโหลด")) {
                order.setLoad_time(rs.getDate("เวลาเข้าโหลด"));
            }
            if (columnNames.contains("เวลาที่ต้องเสร็จ")) {
                order.setDeadline(rs.getDate("เวลาที่ต้องเสร็จ"));
            }
            if (columnNames.contains("จุดดรอป")) {
                order.setDrop(rs.getInt("จุดดรอป"));
            }
            if (columnNames.contains("ปลายทาง")) {
                order.setDestination(rs.getString("ปลายทาง"));
            }
            if (columnNames.contains("ลำดับเที่ยว")) {
                order.setGroupID(rs.getInt("ลำดับเที่ยว"));
            }
            return order;
        }
    }

}
