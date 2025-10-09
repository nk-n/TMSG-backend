package ku.cs.tmsg.controller;

import ku.cs.tmsg.service.LineAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/auth")
public class LineAuthController {

    @Autowired
    private LineAuthService lineAuthService;


    @PostMapping("/line")
    public ResponseEntity<?> verifyLineToken(@RequestBody Map<String, String> request) {
        String idToken = request.get("idToken");
        return ResponseEntity.ok(lineAuthService.verifyAndCreateJwt(idToken));
    }
}
