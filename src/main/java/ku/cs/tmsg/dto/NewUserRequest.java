package ku.cs.tmsg.dto;

import lombok.Data;

@Data
public class NewUserRequest {
    private String username;
    private String password;
    private String phone;
    private String name;
}
