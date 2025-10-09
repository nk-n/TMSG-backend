package ku.cs.tmsg.controller;

import ku.cs.tmsg.dto.DriverLinkLineRequest;
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
//        for (Map.Entry<String, String> entry : request.entrySet()) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }
        String idToken = request.get("idToken");
        return ResponseEntity.ok(lineAuthService.verifyAndCreateJwt(idToken));
    }

    @PostMapping("/link-line")
    public ResponseEntity<?> linkLineToken(@RequestBody DriverLinkLineRequest request) {
        if (request == null) return ResponseEntity.badRequest().build();
        Map<String, String> response;

        try {
            response = lineAuthService.linkUser(request.getIdToken(), request.getPhone());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }


        if (response.get("STATUS") == null || !response.get("STATUS").equals("LINKED SUCCESSFUL")) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }
}
