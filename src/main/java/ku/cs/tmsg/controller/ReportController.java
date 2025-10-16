package ku.cs.tmsg.controller;

import ku.cs.tmsg.dto.response.ApiResponse;
import ku.cs.tmsg.dto.response.report.ShippingTotalResponse;
import ku.cs.tmsg.dto.response.report.ShippingTotalResponseEntry;
import ku.cs.tmsg.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/delivery/{year}")
    public ResponseEntity<ApiResponse<ShippingTotalResponse>> GetShippingDeliveryTotalByYear(@PathVariable("year") int parameter){
        try {
            ShippingTotalResponse shipping = reportService.getTotalDelivery(parameter);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("get total shipping success", shipping));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("get total shipping failed: " + e.getMessage(), null));
        }
    }

    @GetMapping("/travel/{year}")
    public ResponseEntity<ApiResponse<ShippingTotalResponse>> GetShippingTravelTotalByYear(@PathVariable("year") int parameter){
        try {
            ShippingTotalResponse shipping = reportService.getTotalTravel(parameter);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("get total shipping success", shipping));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("get total shipping failed: " + e.getMessage(), null));
        }
    }

    @GetMapping("/distance/{year}")
    public ResponseEntity<ApiResponse<ShippingTotalResponse>> GetShippingDistanceTotalByYear(@PathVariable("year") int parameter){
        try {
            ShippingTotalResponse shipping = reportService.getTotalDistance(parameter);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("get total shipping success", shipping));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("get total shipping failed: " + e.getMessage(), null));
        }
    }

    @GetMapping("/car/{year}")
    public ResponseEntity<ApiResponse<ShippingTotalResponse>> GetShippingCarTotalByYear(@PathVariable("year") int parameter){
        try {
            ShippingTotalResponse shipping = reportService.getTotalCar(parameter);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("get total shipping success", shipping));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("get total shipping failed: " + e.getMessage(), null));
        }
    }

    @GetMapping("/driver/{year}")
    public ResponseEntity<ApiResponse<ShippingTotalResponse>> GetShippingDriverTotalByYear(@PathVariable("year") int parameter){
        try {
            ShippingTotalResponse shipping = reportService.getTotalDriver(parameter);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("get total shipping success", shipping));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("get total shipping failed: " + e.getMessage(), null));
        }
    }

}
