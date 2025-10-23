package ku.cs.tmsg.controller;

import ku.cs.tmsg.dto.response.ApiResponse;
import ku.cs.tmsg.dto.response.DriverDataResponse;
import ku.cs.tmsg.dto.response.DriverStats;
import ku.cs.tmsg.exception.NotFoundException;
import ku.cs.tmsg.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/line")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @GetMapping("/user-data")
    public ResponseEntity<ApiResponse<DriverDataResponse>> getDriverData(@RequestHeader("Authorization") String authHeader){
        try {
            DriverDataResponse driverDataResponse = driverService.getDriverData(authHeader.substring(7));
            return ResponseEntity.ok(new ApiResponse<>("OK", driverDataResponse));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<DriverStats>  getDriverStats(@RequestHeader("Authorization") String authHeader,
                                                       @RequestParam int year,
                                                       @RequestParam int month,
                                                       @RequestParam int date) {
        try {
            DriverStats response = driverService.getDriverStats(authHeader.substring(7), year, month, date);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
