package ku.cs.tmsg.controller;

import io.jsonwebtoken.lang.Maps;
import ku.cs.tmsg.dto.request.TaskStatusUpdateRequest;
import ku.cs.tmsg.dto.response.ApiResponse;
import ku.cs.tmsg.dto.response.OrderStatusResponse;
import ku.cs.tmsg.dto.response.TaskResponse;
import ku.cs.tmsg.exception.DatabaseException;
import ku.cs.tmsg.service.DriverTaskService;
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

    @GetMapping("/new")
    public ResponseEntity<List<TaskResponse>> newTask(@RequestHeader("Authorization") String authHeader) {
        try {
            return ResponseEntity.ok().body(driverTaskService.getNewTasks(authHeader.substring(7)));
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
}
