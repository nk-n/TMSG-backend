package ku.cs.tmsg.repository;

import ku.cs.tmsg.entity.Car;
import ku.cs.tmsg.entity.Destination;
import ku.cs.tmsg.entity.Driver;
import ku.cs.tmsg.entity.enums.CarAndDriverStatus;
import ku.cs.tmsg.exception.DatabaseException;
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
public class DestinationRepository {
    private JdbcTemplate jdbcTemplate;
    private DBResultUtils dbResultUtils;

    @Autowired
    public DestinationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.dbResultUtils = new DBResultUtils();
    }

    public void save(Destination destination) throws Exception {
        String query = """
                INSERT INTO สถานที่จัดส่งปลายทาง (จังหวัด,ภูมิภาค,พร้อมรับสินค้า,ชื่อสถานที่,ที่อยู่,ระยะทางจาก_SCBPK,ระยะเวลาที่ใช้เดินทาง_SCBPK,เส้นทาง)
                VALUES (?,?,?,?,?,?,?,?)
                ON DUPLICATE KEY UPDATE
                    จังหวัด = VALUES(จังหวัด),
                    ภูมิภาค = VALUES(ภูมิภาค),
                    พร้อมรับสินค้า = VALUES(พร้อมรับสินค้า),
                    ที่อยู่ = VALUES(ที่อยู่),
                    ระยะทางจาก_SCBPK = VALUES(ระยะทางจาก_SCBPK),
                    ระยะเวลาที่ใช้เดินทาง_SCBPK = VALUES(ระยะเวลาที่ใช้เดินทาง_SCBPK),
                    เส้นทาง = VALUES(เส้นทาง)
                """;
        int rows = jdbcTemplate.update(query, destination.getProvince(), destination.getRegion(),destination.isAvailable(), destination.getName(), destination.getAddress(), destination.getDistance(), destination.getTimeUse(), destination.getRoute());
        if (rows == 0) {
            throw new DatabaseException("can't create new destination");
        }
    }

    public List<Destination> get() {
        String query = "select จังหวัด,ภูมิภาค,พร้อมรับสินค้า,ชื่อสถานที่,ที่อยู่,ระยะทางจาก_SCBPK,ระยะเวลาที่ใช้เดินทาง_SCBPK,เส้นทาง from สถานที่จัดส่งปลายทาง";
        List<Destination> destinations = jdbcTemplate.query(query, new DestinationRepository.DestinationMapper());
        return destinations;
    }

    public Destination getByOrderID(String orderID) {
        String query = """
                SELECT ds.ชื่อสถานที่, ds.ที่อยู่, ds.เส้นทาง, ds.ระยะทางจาก_SCBPK FROM สถานที่จัดส่งปลายทาง AS ds INNER JOIN
                ออเดอร์ AS o ON ds.ชื่อสถานที่ = o.ปลายทาง WHERE o.order_id = ?
                """;
        return jdbcTemplate.query(query, new DestinationMapper(), orderID).getFirst();
    }

    class DestinationMapper implements RowMapper<Destination> {
        @Override
        public Destination mapRow(ResultSet rs, int rowNum) throws SQLException {
            Destination destination = new Destination();
            Set<String> columns = dbResultUtils.getColumnNames(rs);
            if (columns.contains("จังหวัด")) {
                destination.setProvince(rs.getString("จังหวัด"));
            }
            if (columns.contains("ภูมิภาค")) {
                destination.setRegion(rs.getString("ภูมิภาค"));
            }
            if (columns.contains("พร้อมรับสินค้า")) {
                destination.setAvailable(rs.getBoolean("พร้อมรับสินค้า"));
            }
            if (columns.contains("ชื่อสถานที่")) {
                destination.setName(rs.getString("ชื่อสถานที่"));
            }
            if (columns.contains("ที่อยู่")) {
                destination.setAddress(rs.getString("ที่อยู่"));
            }
            if (columns.contains("ระยะทางจาก_scbpk")) {
                destination.setDistance(rs.getInt("ระยะทางจาก_scbpk"));
            }
            if (columns.contains("ระยะเวลาที่ใช้เดินทาง_scbpk")) {
                destination.setTimeUse(rs.getInt("ระยะเวลาที่ใช้เดินทาง_scbpk"));
            }
            if (columns.contains("เส้นทาง")) {
                destination.setRoute(rs.getString("เส้นทาง"));
            }
            return destination;
        }
    }

    public void delete(String name) throws Exception {
        String query = """
                UPDATE สถานที่จัดส่งปลายทาง 
                SET 
                    พร้อมรับสินค้า = ?
                WHERE ชื่อสถานที่ = ?
                """;
        jdbcTemplate.update(query, false, name);
    }

}
