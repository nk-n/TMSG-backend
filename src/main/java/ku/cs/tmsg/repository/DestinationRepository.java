package ku.cs.tmsg.repository;

import ku.cs.tmsg.entity.Car;
import ku.cs.tmsg.entity.Destination;
import ku.cs.tmsg.entity.Driver;
import ku.cs.tmsg.entity.enums.CarAndDriverStatus;
import ku.cs.tmsg.exception.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DestinationRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DestinationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

    class DestinationMapper implements RowMapper<Destination> {
        @Override
        public Destination mapRow(ResultSet rs, int rowNum) throws SQLException {
            Destination destination = new Destination();
            destination.setProvince(rs.getString("จังหวัด"));
            destination.setRegion(rs.getString("ภูมิภาค"));
            destination.setAvailable(rs.getBoolean("พร้อมรับสินค้า"));
            destination.setName(rs.getString("ชื่อสถานที่"));
            destination.setAddress(rs.getString("ที่อยู่"));
            destination.setDistance(rs.getInt("ระยะทางจาก_SCBPK"));
            destination.setTimeUse(rs.getInt("ระยะเวลาที่ใช้เดินทาง_SCBPK"));
            destination.setRoute(rs.getString("เส้นทาง"));
            return destination;
        }
    }

}
