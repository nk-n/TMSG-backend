package ku.cs.tmsg.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/api/line")
public class OrderController {

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(Map.of("Test1", "value1", "Test2", "value2"));
    }
}
