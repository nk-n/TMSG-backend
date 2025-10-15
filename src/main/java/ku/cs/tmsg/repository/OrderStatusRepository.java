package ku.cs.tmsg.repository;

import ku.cs.tmsg.entity.OrderStatus;
import ku.cs.tmsg.service.DBResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

@Repository
public class OrderStatusRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DBResultUtils dbResultUtils;

    public OrderStatus save(OrderStatus orderStatus){
        String query = """
                INSERT INTO ประวัติสถานะ (history_status_id, timestamp_status, สถานะ, order_id)
                VALUES (?, ?, ?, ?)
                """;
        return jdbcTemplate.update(query,
                orderStatus.getHistoryStatusId(),
                orderStatus.getTimestampStatus(),
                orderStatus.getOrderStatus(),
                orderStatus.getOrderId()) == 1 ? orderStatus : null;
    }

    public int getStatusCount(String orderID) {
        String query = """
                SELECT history_status_id FROM ประวัติสถานะ WHERE order_id = ?;
                """;
        return jdbcTemplate.query(query, new OrderStatusMapper(), orderID).size();
    }

    private class OrderStatusMapper implements RowMapper<OrderStatus> {
        @Override
        public OrderStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
            OrderStatus orderStatus = new OrderStatus();

            Set<String> columnNames = dbResultUtils.getColumnNames(rs);

            if (columnNames.contains("order_id")) {
                orderStatus.setOrderId(rs.getString("order_id"));
            }

            if (columnNames.contains("timestamp_status")) {
                orderStatus.setOrderStatus(rs.getString("timestamp_status"));
            }

            if (columnNames.contains("สถานะ")) {
                orderStatus.setOrderStatus(rs.getString("สถานะ"));
            }

            if (columnNames.contains("history_status_id")) {
                orderStatus.setHistoryStatusId(rs.getString("history_status_id"));
            }
            return orderStatus;
        }
    }

}
