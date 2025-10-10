package ku.cs.tmsg.repository;

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

}
