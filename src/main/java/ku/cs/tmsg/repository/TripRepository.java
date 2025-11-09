package ku.cs.tmsg.repository;

import jakarta.transaction.Transactional;
import ku.cs.tmsg.dto.request.SpecialTripCreate;
import ku.cs.tmsg.dto.response.OrderDeliveryStatusResponse;
import ku.cs.tmsg.dto.response.OrderDriverResponse;
import ku.cs.tmsg.dto.response.OrderResponse;
import ku.cs.tmsg.dto.response.SpecialTripResponse;
import ku.cs.tmsg.entity.Driver;
import ku.cs.tmsg.entity.Order;
import ku.cs.tmsg.exception.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class TripRepository {
    private JdbcTemplate jdbcTemplate;
    private LogRepository logRepository;

    @Autowired
    public TripRepository(JdbcTemplate jdbcTemplate, LogRepository logRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.logRepository = logRepository;
    }


    public List<SpecialTripResponse> getSpecialTripByTripID(String tripId) {
        String query = """
                SELECT special_trip_id, `เหตุผล`, `จำนวนเงิน`
                FROM `ค่าเที่ยวพิเศษ` as special
                WHERE `trip_id` = ?
                ORDER BY `จำนวนเงิน`
                """;
        List<SpecialTripResponse> specialTrips = jdbcTemplate.query(query, new TripRepository.SpecialTripMapper(), tripId);

        return specialTrips;
    }

    class SpecialTripMapper implements RowMapper<SpecialTripResponse> {
        @Override
        public SpecialTripResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            SpecialTripResponse specialTrip = new SpecialTripResponse();
            specialTrip.setMoney(rs.getDouble("จำนวนเงิน"));
            specialTrip.setReason(rs.getString("เหตุผล"));
            specialTrip.setSpecial_trip_id(rs.getString("special_trip_id"));
            return specialTrip;
        }
    }

    public void saveSpecialTrip(SpecialTripCreate specialTripCreate) throws Exception {
        UUID specialTripId = UUID.randomUUID();
        System.out.println(specialTripId.toString() + " " + specialTripCreate.getTrip_id() + " " +  specialTripCreate.getReason() + " " + specialTripCreate.getMoney());
        String query = """
                INSERT INTO `ค่าเที่ยวพิเศษ` (special_trip_id,trip_id,`เหตุผล`,`จำนวนเงิน`)
                VALUES (?,?,?,?)
                """;
        int rows = jdbcTemplate.update(query, specialTripId.toString() , specialTripCreate.getTrip_id(), specialTripCreate.getReason(), specialTripCreate.getMoney());
        if (rows == 0) {
            throw new DatabaseException("can't add new special trip");
        }
        logRepository.save("create special trip", "special trip", "create");
    }

    public void deleteSpecialTripsById(String id) throws Exception {
        String query = """
                DELETE FROM `ค่าเที่ยวพิเศษ` 
                WHERE special_trip_id = ?
                """;
        int rows = jdbcTemplate.update(query, id);
        if (rows == 0) {
            throw new DatabaseException("can't delete special trip");
        }
        logRepository.save("delete special trip", "special trip", "delete");
    }
}
