package ku.cs.tmsg.controller;

import ku.cs.tmsg.dto.response.ApiResponse;
import ku.cs.tmsg.dto.response.UserResponse;
import ku.cs.tmsg.entity.User;
import ku.cs.tmsg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        try {
            List<UserResponse> userResponses = userService.getAll();
            return ResponseEntity.ok(new ApiResponse<>("OK", userResponses));
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("Error" + e.getMessage(), null));
        }
    }
}
