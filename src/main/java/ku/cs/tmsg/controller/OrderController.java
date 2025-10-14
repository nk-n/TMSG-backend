package ku.cs.tmsg.controller;

import ku.cs.tmsg.dto.request.OrderCreate;
import ku.cs.tmsg.dto.response.ApiResponse;
import ku.cs.tmsg.dto.response.OrderResponse;
import ku.cs.tmsg.service.CarService;
import ku.cs.tmsg.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(Map.of("Test1", "value1", "Test2", "value2"));
    }

    @PostMapping("/")
    public ResponseEntity<String> CreateOrder(@RequestBody List<OrderCreate> request) {
        try {
            orderService.createOrder(request);
            return ResponseEntity.status(HttpStatus.OK).body("create order success");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("create order failed: " + e.getMessage());
        }
    }

    @GetMapping("/{status}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> GetOrder(@PathVariable String status) {
        try {
            List<OrderResponse> orders = orderService.getOrder(status);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<List<OrderResponse>>("get orders success", orders));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<List<OrderResponse>>("get orders failed: " + e.getMessage(), null));
        }
    }
}
