package ku.cs.tmsg.service;

import ku.cs.tmsg.dto.response.TaskResponse;
import ku.cs.tmsg.entity.Car;
import ku.cs.tmsg.entity.Destination;
import ku.cs.tmsg.entity.Driver;
import ku.cs.tmsg.entity.Order;
import ku.cs.tmsg.repository.CarRepository;
import ku.cs.tmsg.repository.DestinationRepository;
import ku.cs.tmsg.repository.DriverRepository;
import ku.cs.tmsg.repository.OrderRepository;
import ku.cs.tmsg.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DriverTaskService {

    private final CarRepository carRepository;
    private OrderRepository orderRepository;
    private DriverRepository driverRepository;
    private DestinationRepository destinationRepository;

    private JwtUtil jwtUtil;

    @Autowired
    public DriverTaskService(OrderRepository orderRepository, DriverRepository driverRepository, DestinationRepository destinationRepository, JwtUtil jwtUtil, CarRepository carRepository) {
        this.orderRepository = orderRepository;
        this.driverRepository = driverRepository;
        this.destinationRepository = destinationRepository;
        this.jwtUtil = jwtUtil;
        this.carRepository = carRepository;
    }

    public List<TaskResponse> getTasks(String jwt, String type) {
        String phone = jwtUtil.getPhoneFromLineToken(jwt);
        List<Order> orders;
        if (type == "new") {
            orders = orderRepository.getNewOrder(phone);
        } else {
            orders = orderRepository.getCurrentOrder(phone);
        }
        List<TaskResponse> taskResponses = new ArrayList<>();
        for (Order order : orders) {
            TaskResponse taskResponse = new TaskResponse();

            // Order Details
            taskResponse.setOrderID(order.getId());
            taskResponse.setTaskGroupID(order.getGroupID());
            taskResponse.setDropID(order.getDrop());
            taskResponse.setNote(order.getNote());
            taskResponse.setDeadline(order.getDeadline());
            taskResponse.setCarryWeight(order.getGas_amount());
            taskResponse.setSource(order.getSource());
            taskResponse.setLoadingTime(order.getLoad_time());
            long hourAndAHalfAsMS = 90 * 60 * 1000;
            taskResponse.setStartTime(new Date(order.getLoad_time().getTime() - hourAndAHalfAsMS));

            // Car Details
            Car car = carRepository.getCarByOrderID(order.getId());
            taskResponse.setCarID(car.getId());
            taskResponse.setWeight(car.getWeight().getDisplayName());

            // Destination Details
            Destination dest = destinationRepository.getByOrderID(order.getId());
            taskResponse.setDestinationName(dest.getName());
            taskResponse.setDestinationLocation(dest.getAddress());
            taskResponse.setDestinationRoute(dest.getRoute());
            taskResponse.setDestinationDistance(dest.getDistance());

            taskResponses.add(taskResponse);
        }
        return taskResponses;
    }
}
