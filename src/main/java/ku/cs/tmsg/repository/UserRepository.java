package ku.cs.tmsg.repository;

import ku.cs.tmsg.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User findByUsername(String username) {
        String query = "select ID,ชื่อ,พร้อมทำงาน,เบอร์โทร,ตำแหน่ง,รหัสผ่าน from พนักงานจัดส่ง where ID=?";
        List<User> users = jdbcTemplate.query(query, new UserMapper(), username);
        return users.isEmpty() ? null : users.getFirst();
    }

    public boolean existsByUsername(String username) {
        return findByUsername(username) != null;
    }

    public User save(User user) {
        String query = "insert into พนักงานจัดส่ง (ID,ชื่อ,พร้อมทำงาน,เบอร์โทร,ตำแหน่ง,รหัสผ่าน) values (?,?,?,?,?,?)";
        int status = jdbcTemplate.update(query, user.getId(), user.getName(), true, user.getPhone(), user.getRole(), user.getPassword());
        if (status == 0) {
            System.err.println("Can't create new user.");
            return null;
        }
        return user;
    }

    public List<User> findAll() {
        String query = "select ID,ชื่อ,พร้อมทำงาน,เบอร์โทร,ตำแหน่ง,รหัสผ่าน from พนักงานจัดส่ง where พร้อมทำงาน=1";
        List<User> users;
        try {
             users = jdbcTemplate.query(query, new UserMapper());
             return users;
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage()) {
            };
        }
    }

    public int editData(String id, String name, String phone, String password) {
        String query = """
        UPDATE พนักงานจัดส่ง 
            SET ชื่อ = ?, 
                เบอร์โทร = ?,
                รหัสผ่าน = ?
            WHERE id = ?
            """;

        int status = jdbcTemplate.update(query, name, phone, password, id);
        if (status == 0) {
            throw new DataAccessException("f user") {};
        }
        return status;
    }

    public User softDelete(String id) {
        User user = findByUsername(id);
        if (user == null || !user.isStatus()) {
            throw new UsernameNotFoundException("No such user");
        }

        String query = "UPDATE `พนักงานจัดส่ง` SET `พร้อมทำงาน` = ? WHERE `id` = ?";
        int status = jdbcTemplate.update(query, false, id);
        if (status == 0) {
            throw new DataAccessException("failed to delete user") {};
        }
        return user;
    }

    class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            String username = rs.getString("ID");
            String name = rs.getString("ชื่อ");
            String phone =  rs.getString("เบอร์โทร");
            boolean status = rs.getBoolean("พร้อมทำงาน");
            String role = rs.getString("ตำแหน่ง");
            String password = rs.getString("รหัสผ่าน");
            User user = new User();
            user.setId(username);
            user.setPassword(password);
            user.setPhone(phone);
            user.setRole(role);
            user.setStatus(status);
            user.setName(name);
            user.setPassword(password);
            return user;
        }
    }
}

