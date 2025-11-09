package ku.cs.tmsg.repository;

import ku.cs.tmsg.exception.DatabaseException;
import org.springframework.security.core.Authentication;
import ku.cs.tmsg.service.DBResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class LogRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null ) {
            return authentication.getName();
        }
        return "SYSTEM";
    }

    public void save(String message, String model, String action) throws Exception {
        UUID log_id = UUID.randomUUID();
        String user_name = getCurrentUsername();
        String query = """
                INSERT INTO ประวัติ (history_id, message, model, action, staff)
                VALUES (?,?,?,?,?)
                """;
        int rows = jdbcTemplate.update(query, log_id, message, model, action, user_name);
        if (rows == 0) {
            throw new DatabaseException("can't create new log");
        }
    }
}
