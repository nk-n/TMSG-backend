package ku.cs.tmsg.controller;

import io.jsonwebtoken.lang.Maps;
import ku.cs.tmsg.dto.request.OrderUpdateStatus;
import ku.cs.tmsg.dto.request.TaskStatusUpdateRequest;
import ku.cs.tmsg.dto.request.UpdateSentGasWeightRequest;
import ku.cs.tmsg.dto.response.ApiResponse;
import ku.cs.tmsg.dto.response.OrderStatusResponse;
import ku.cs.tmsg.dto.response.TaskResponse;
import ku.cs.tmsg.dto.response.UpdateSentGasWeightResponse;
import ku.cs.tmsg.exception.DatabaseException;
import ku.cs.tmsg.service.DriverTaskService;
import ku.cs.tmsg.service.OrderService;
import ku.cs.tmsg.service.OrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/line/task")
public class DriverTaskController {

    @Autowired
    private DriverTaskService driverTaskService;

    @Autowired
    private OrderStatusService orderStatusService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/new")
    public ResponseEntity<List<TaskResponse>> newTask(@RequestHeader("Authorization") String authHeader) {
        try {
            return ResponseEntity.ok().body(driverTaskService.getTasks(authHeader.substring(7), "new"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/current")
    public ResponseEntity<List<TaskResponse>> currentTask(@RequestHeader("Authorization") String authHeader) {
        try {
            return ResponseEntity.ok().body(driverTaskService.getTasks(authHeader.substring(7), "current"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/status/update")
    public ResponseEntity<OrderStatusResponse>  updateStatus(@RequestBody TaskStatusUpdateRequest request) {
        try {
            return ResponseEntity.ok(orderStatusService.setOrderStatus(request));
        } catch (DatabaseException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/status/{orderID}")
    public ResponseEntity<Map<String, Integer>>  getOrderStatus(@PathVariable String orderID) {
        int progress = orderStatusService.getOrderStatusCount(orderID);
        Map<String, Integer>  result = new HashMap<>();
        result.put("progress", progress);
        return ResponseEntity.ok(result);
    }

    @PutMapping ("/order/update")
    public ResponseEntity<String> GetOrder(@RequestBody OrderUpdateStatus request) {
        try {
            orderService.updateStatus(request);
            return ResponseEntity.status(HttpStatus.OK).body("updated order success");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("update order status failed on orderID " + request.getOrder_id());
        }
    }

    @PutMapping("/order/set-unload-weight")
    public ResponseEntity<UpdateSentGasWeightResponse> SetUnloadWeight(@RequestBody UpdateSentGasWeightRequest request) {
        try {
            UpdateSentGasWeightResponse response = orderService.updateSentGasWeight(request);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (DatabaseException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
