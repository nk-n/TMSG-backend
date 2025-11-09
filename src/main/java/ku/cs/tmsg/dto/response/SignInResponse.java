package ku.cs.tmsg.dto.response;

import lombok.Data;

@Data
public class SignInResponse {
    private String token;
    private String role;
    private String name;

    public SignInResponse(String token, String role, String name) {
        this.role = role;
        this.token = token;
        this.name = name;
    }
}
