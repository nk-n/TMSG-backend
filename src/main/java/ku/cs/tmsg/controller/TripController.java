package ku.cs.tmsg.controller;

import ku.cs.tmsg.dto.request.OrderCreate;
import ku.cs.tmsg.dto.request.SpecialTripCreate;
import ku.cs.tmsg.dto.response.ApiResponse;
import ku.cs.tmsg.dto.response.OrderResponse;
import ku.cs.tmsg.dto.response.SpecialTripResponse;
import ku.cs.tmsg.service.OrderService;
import ku.cs.tmsg.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/trip")
public class TripController {
    @Autowired
    private TripService tripService;

    @GetMapping("/special-trip/{trip-id}")
    public ResponseEntity<ApiResponse<List<SpecialTripResponse>>> getAllSpecialTripsByTripId(@PathVariable("trip-id") String parameter){
        try {
            List<SpecialTripResponse> specialTrips = tripService.getSpecialTripsByTripId(parameter);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("get special trip success", specialTrips));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("get special trips failed: " + e.getMessage(), null));
        }
    }

    @DeleteMapping ("/special-trip/{trip-id}")
    public ResponseEntity<String> DeleteSpecialTripById(@PathVariable("trip-id") String parameter){
        try {
            tripService.deleteSpecialTripById(parameter);
            return ResponseEntity.status(HttpStatus.OK).body("delete special trip success");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("delete special trip failed: " + e.getMessage());
        }
    }

    @PostMapping("/special-trip")
    public ResponseEntity<String> CreateSpecialTrips(@RequestBody SpecialTripCreate request) {
        try {
            tripService.createSpecialTrip(request);
            return ResponseEntity.status(HttpStatus.OK).body("create special trip success");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("create special trip failed: " + e.getMessage());
        }
    }
}
