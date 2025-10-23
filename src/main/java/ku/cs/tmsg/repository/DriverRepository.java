package ku.cs.tmsg.repository;

import ku.cs.tmsg.dto.response.DriverStats;
import ku.cs.tmsg.entity.Car;
import ku.cs.tmsg.entity.Driver;
import ku.cs.tmsg.entity.User;
import ku.cs.tmsg.entity.enums.CarAndDriverStatus;
import ku.cs.tmsg.entity.enums.CarType;
import ku.cs.tmsg.entity.enums.CarWeight;
import ku.cs.tmsg.exception.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class DriverRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DriverRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Driver driver) throws Exception {
        String query = """
                INSERT INTO พนักงานขับรถ (เบอร์โทร,ชื่อ,Line_ID,หมายเหตุพนักงานขับรถ,พร้อมทำงาน,สถานะพนักงาน)
                VALUES (?,?,?,?,?,?)
                ON DUPLICATE KEY UPDATE
                    ชื่อ = VALUES(ชื่อ),
                    Line_ID = VALUES(Line_ID),
                    หมายเหตุพนักงานขับรถ = VALUES(หมายเหตุพนักงานขับรถ),
                    พร้อมทำงาน = VALUES(พร้อมทำงาน),
                    สถานะพนักงาน = VALUES(สถานะพนักงาน)
                """;
        int rows = jdbcTemplate.update(query, driver.getTel(), driver.getName(), driver.getLine_ID(), driver.getNote(), driver.isAvailable(), driver.getStatus().getDisplayName());
        if (rows == 0) {
            throw new DatabaseException("can't create new driver");
        }
    }

    public List<Driver> get() {
        String query = "select เบอร์โทร,ชื่อ,Line_ID,หมายเหตุพนักงานขับรถ,พร้อมทำงาน,สถานะพนักงาน from พนักงานขับรถ";
        List<Driver> drivers = jdbcTemplate.query(query, new DriverRepository.DriverMapper());
        return  drivers;
    }

    public int updateLineIdByPhone(String phone, String lineId) {
        System.out.println(lineId.length());
        String sql = "UPDATE `พนักงานขับรถ` SET `LINE_ID` = ? WHERE `เบอร์โทร` = ?";
        return jdbcTemplate.update(sql, lineId, phone);
    }

    public Driver findByPhone(String phone) {
        String query = "select เบอร์โทร,ชื่อ,Line_ID,หมายเหตุพนักงานขับรถ,พร้อมทำงาน,สถานะพนักงาน from พนักงานขับรถ where เบอร์โทร=?";
        List<Driver> drivers = jdbcTemplate.query(query, new DriverRepository.DriverMapper(), phone);
        return drivers.isEmpty() ? null : drivers.getFirst();
    }

    public boolean isExists(String phone) {
        return findByPhone(phone) != null;
    }

    public Driver findByID(String ID) {
        String query = "select เบอร์โทร,ชื่อ,Line_ID,หมายเหตุพนักงานขับรถ,พร้อมทำงาน,สถานะพนักงาน from พนักงานขับรถ where Line_ID=?";
        List<Driver> drivers = jdbcTemplate.query(query, new DriverRepository.DriverMapper(), ID);
        return drivers.isEmpty() ? null : drivers.getFirst();
    }

    public boolean isExistsByID(String id) {
        return findByID(id) != null;
    }

    class DriverMapper implements RowMapper<Driver> {
        @Override
        public Driver mapRow(ResultSet rs, int rowNum) throws SQLException {
            Driver driver = new Driver();
            driver.setStatus(CarAndDriverStatus.fromLabel(rs.getString("สถานะพนักงาน")));
            driver.setName(rs.getString("ชื่อ"));
            driver.setTel(rs.getString("เบอร์โทร"));
            driver.setNote(rs.getString("หมายเหตุพนักงานขับรถ"));
            driver.setAvailable(rs.getBoolean("พร้อมทำงาน"));
            driver.setLine_ID(rs.getString("Line_ID"));
            return driver;
        }
    }

    public void update(CarAndDriverStatus status, boolean available, String tel) throws Exception {
        String query = """
                UPDATE พนักงานขับรถ 
                SET 
                    สถานะพนักงาน = ?,
                    พร้อมทำงาน = ?
                WHERE เบอร์โทร = ?
                """;
        jdbcTemplate.update(query, status.getDisplayName(), available, tel);
    }

    public void updateNote(String tel, String note) throws Exception {
        String query = """
                UPDATE พนักงานขับรถ 
                SET 
                    หมายเหตุพนักงานขับรถ = ?
                WHERE เบอร์โทร = ?
                """;
        jdbcTemplate.update(query, note, tel);
    }

    public void delete(String tel) throws Exception {
        String query = """
                UPDATE พนักงานขับรถ 
                SET 
                    พร้อมทำงาน = ?
                WHERE เบอร์โทร = ?
                """;
        jdbcTemplate.update(query, false, tel);
    }

    public DriverStats getDriverStats(String ID, int year, int month, int day) {
        DriverStats driverStats = new DriverStats();
        String queryDistanceDay = """
            SELECT COALESCE(SUM(`ระยะทางจาก_SCBPK`)) as total
            FROM `การจัดส่ง` AS d
            JOIN `ออเดอร์` AS o ON d.order_id = o.order_id
            JOIN `สถานที่จัดส่งปลายทาง` AS dest ON o.ปลายทาง = dest.ชื่อสถานที่
            WHERE d.เบอร์โทร = ?
            AND YEAR(o.order_date) = ?
            AND MONTH(o.order_date) = ?
            AND DAY(o.order_date) = ?
       """;
        String queryDistanceMonth = """
            SELECT COALESCE(SUM(`ระยะทางจาก_SCBPK`)) as total
            FROM `การจัดส่ง` AS d
            JOIN `ออเดอร์` AS o ON d.order_id = o.order_id
            JOIN `สถานที่จัดส่งปลายทาง` AS dest ON o.ปลายทาง = dest.ชื่อสถานที่
            WHERE d.เบอร์โทร = ?
            AND YEAR(o.order_date) = ?
            AND MONTH(o.order_date) = ?
       """;
        String queryIncomeDay = """
           SELECT COALESCE(p.จำนวนเงิน + SUM(sp.จำนวนเงิน)) 
           FROM `การจัดส่ง` AS d
           JOIN `ออเดอร์` AS o ON o.order_id = d.order_id
           JOIN `ค่าเที่ยว` AS p ON d.trip_id = p.trip_id
           JOIN `ค่าเที่ยวพิเศษ` AS sp ON sp.trip_id = p.trip_id
           WHERE d.เบอร์โทร = ?
           AND YEAR(o.order_date) = ?
           AND MONTH(o.order_date) = ?
           AND DAY(o.order_date) = ?
           GROUP BY d.เบอร์โทร
       """;
        String queryIncomeMonth = """
           SELECT COALESCE(p.จำนวนเงิน + SUM(sp.จำนวนเงิน))
           FROM `การจัดส่ง` AS d
           JOIN `ออเดอร์` AS o ON o.order_id = d.order_id
           JOIN `ค่าเที่ยว` AS p ON d.trip_id = p.trip_id
           JOIN `ค่าเที่ยวพิเศษ` AS sp ON sp.trip_id = p.trip_id
           WHERE d.เบอร์โทร = ?
           AND YEAR(o.order_date) = ?
           AND MONTH(o.order_date) = ?
           GROUP BY d.เบอร์โทร
       """;
        driverStats.setDistanceDeliveryDay(jdbcTemplate.queryForObject(queryDistanceDay, Integer.class ,ID, year, month, day));
        driverStats.setDistanceDeliveryMonth(jdbcTemplate.queryForObject(queryDistanceMonth, Integer.class ,ID, year, month));
        driverStats.setIncomeDeliveryDay(jdbcTemplate.queryForObject(queryIncomeDay, Integer.class ,ID, year, month, day));
        driverStats.setIncomeDeliveryMonth(jdbcTemplate.queryForObject(queryIncomeMonth, Integer.class ,ID, year, month));
        return driverStats;
    }

}
