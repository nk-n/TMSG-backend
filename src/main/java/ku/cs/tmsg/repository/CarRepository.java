package ku.cs.tmsg.repository;

import ku.cs.tmsg.dto.request.CarUpdate;
import ku.cs.tmsg.entity.Car;
import ku.cs.tmsg.entity.enums.CarAndDriverStatus;
import ku.cs.tmsg.entity.enums.CarType;
import ku.cs.tmsg.entity.enums.CarWeight;
import ku.cs.tmsg.exception.DatabaseException;
import ku.cs.tmsg.exception.NotFoundException;
import ku.cs.tmsg.service.DBResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Repository
public class CarRepository {
    private JdbcTemplate jdbcTemplate;
    private DBResultUtils dbResultUtils;

    @Autowired
    public CarRepository(JdbcTemplate jdbcTemplate, DBResultUtils dbResultUtils) {
        this.jdbcTemplate = jdbcTemplate;
        this.dbResultUtils = dbResultUtils;
    }

    public void save(Car car) throws Exception {
        String query = """
                INSERT INTO รถขนส่ง (สถานะรถ,ทะเบียนรถ,เบอร์รถ,น้ำหนัก,ประเภทรถ,พร้อมขนส่ง,หมายเหตุรถ)
                VALUES (?,?,?,?,?,?,?)
                ON DUPLICATE KEY UPDATE
                    สถานะรถ = VALUES(สถานะรถ),
                    น้ำหนัก = VALUES(น้ำหนัก),
                    ประเภทรถ = VALUES(ประเภทรถ),
                    พร้อมขนส่ง = VALUES(พร้อมขนส่ง),
                    หมายเหตุรถ = VALUES(หมายเหตุรถ),
                    ทะเบียนรถ = VALUES(ทะเบียนรถ)
                """;
        int rows = jdbcTemplate.update(query, car.getStatus().getDisplayName(), car.getLicense(), car.getId(),
                car.getWeight().getDisplayName(), car.getType().getDisplayName(), car.isAvailable(), car.getNote());
        if (rows == 0) {
            throw new DatabaseException("can't create new car");
        }
    }

    public List<Car> get() {
        String query = "select สถานะรถ,ทะเบียนรถ,เบอร์รถ,น้ำหนัก,ประเภทรถ,พร้อมขนส่ง,หมายเหตุรถ from รถขนส่ง";
        List<Car> cars = jdbcTemplate.query(query, new CarRepository.CarMapper());
        return  cars;
    }

    public Car getCarByOrderID(String orderID) {
        String query = """
                SELECT c.เบอร์รถ, c.น้ำหนัก FROM รถขนส่ง AS c INNER JOIN
                การจัดส่ง AS d ON c.เบอร์รถ = d.เบอร์รถ INNER JOIN
                ออเดอร์ AS o ON d.order_id = o.order_id
                WHERE o.order_id = ?
                """;
        return jdbcTemplate.query(query, new CarMapper(), orderID).getFirst();
    }

    class CarMapper implements RowMapper<Car> {
        @Override
        public Car mapRow(ResultSet rs, int rowNum) throws SQLException {
            Car car = new Car();
            Set<String> columnNames = dbResultUtils.getColumnNames(rs);
            if (columnNames.contains("สถานะรถ")) {
                car.setStatus(CarAndDriverStatus.fromLabel(rs.getString("สถานะรถ")));
            }
            if (columnNames.contains("น้ำหนัก")) {
                car.setWeight(CarWeight.fromLabel(rs.getString("น้ำหนัก")));
            }
            if (columnNames.contains("ประเภทรถ")) {
                car.setType(CarType.fromLabel(rs.getString("ประเภทรถ")));
            }
            if (columnNames.contains("พร้อมขนส่ง")) {
                car.setAvailable(rs.getBoolean("พร้อมขนส่ง"));
            }
            if (columnNames.contains("ทะเบียนรถ")) {
                car.setLicense(rs.getString("ทะเบียนรถ"));
            }
            if (columnNames.contains("เบอร์รถ")) {
                car.setId(rs.getString("เบอร์รถ"));
            }
            if (columnNames.contains("หมายเหตุรถ")) {
                car.setNote(rs.getString("หมายเหตุรถ"));
            }
            if (columnNames.contains("ทะเบียนรถ")) {
                car.setLicense(rs.getString("ทะเบียนรถ"));
            }
            return car;
        }
    }

    public void update(CarAndDriverStatus status, boolean available, String id) throws Exception {
        String query = """
                UPDATE รถขนส่ง 
                SET 
                    สถานะรถ = ?,
                    พร้อมขนส่ง = ?
                WHERE เบอร์รถ = ?
                """;
        jdbcTemplate.update(query, status.getDisplayName(), available, id);
    }

    public void updateNote(String id, String note) throws Exception {
        String query = """
                UPDATE รถขนส่ง 
                SET 
                    หมายเหตุรถ = ?
                WHERE เบอร์รถ = ?
                """;
        jdbcTemplate.update(query, note, id);
    }

    public void delete(String id) throws Exception {
        String query = """
                UPDATE รถขนส่ง 
                SET 
                    พร้อมขนส่ง = ?
                WHERE เบอร์รถ = ?
                """;
        jdbcTemplate.update(query, false, id);
    }

}
