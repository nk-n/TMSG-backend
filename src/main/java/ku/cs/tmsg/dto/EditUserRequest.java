package ku.cs.tmsg.dto;

import lombok.Data;

@Data
public class EditUserRequest {
    private String id;
    private String name;
    private String phone;
    private String password;
}
