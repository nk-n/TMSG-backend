package ku.cs.tmsg.controller;

import ku.cs.tmsg.dto.response.TaskResponse;
import ku.cs.tmsg.service.DriverTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/line/task")
public class DriverTaskController {

    @Autowired
    private DriverTaskService driverTaskService;

    @GetMapping("/new")
    public ResponseEntity<List<TaskResponse>> newTask(@RequestHeader("Authorization") String authHeader) {
        try {
            return ResponseEntity.ok().body(driverTaskService.getNewTasks(authHeader.substring(7)));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
