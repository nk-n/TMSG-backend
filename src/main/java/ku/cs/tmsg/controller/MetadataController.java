package ku.cs.tmsg.controller;

import ku.cs.tmsg.dto.request.CarCreate;
import ku.cs.tmsg.dto.request.DestinationCreate;
import ku.cs.tmsg.dto.request.DriverCreate;
import ku.cs.tmsg.dto.request.CarUpdate;
import ku.cs.tmsg.dto.response.ApiResponse;
import ku.cs.tmsg.entity.Car;
import ku.cs.tmsg.entity.Destination;
import ku.cs.tmsg.entity.Driver;
import ku.cs.tmsg.service.CarService;
import ku.cs.tmsg.service.DestinationService;
import ku.cs.tmsg.service.DriverService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metadata")
public class MetadataController {
    @Autowired
    private CarService carService;
    @Autowired
    private DestinationService destinationService;
    @Autowired
    private DriverService driverService;

    @PostMapping("/cars")
    public ResponseEntity<String> SaveCar(@RequestBody List<CarCreate> request) {
        try {
            carService.createCar(request);
            return ResponseEntity.status(HttpStatus.OK).body("create car success");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/drivers")
    public ResponseEntity<String> SaveDriver(@RequestBody List<DriverCreate> request) {
        try {
            driverService.createDriver(request);
            return ResponseEntity.status(HttpStatus.OK).body("create driver success");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/destinations")
    public ResponseEntity<String> SaveDestination(@RequestBody List<DestinationCreate> request) {
        try {
            destinationService.createDestination(request);
            return ResponseEntity.status(HttpStatus.OK).body("create destination success");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/cars")
    public ResponseEntity<ApiResponse<List<Car>>> GetAllCars() {
        try {
            List<Car> cars = carService.getAllCars();
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<List<Car>>("success get cars", cars));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @GetMapping("/drivers")
    public ResponseEntity<ApiResponse<List<Driver>>> GetAllDrivers() {
        try {
            List<Driver> drivers = driverService.getAllDrivers();
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<List<Driver>>("success get drivers", drivers));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @GetMapping("/destinations")
    public ResponseEntity<ApiResponse<List<Destination>>> GetAllDestinations() {
        try {
            List<Destination> destination = destinationService.getAllDestination();
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<List<Destination>>("success get destinations", destination));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @PutMapping("/car")
    public ResponseEntity<String> UpdateCarData(@RequestBody List<CarUpdate> request) {
        try {
            carService.updateCars(request);
            return ResponseEntity.status(HttpStatus.OK).body("update item success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("update item failed");
        }
    }
}
