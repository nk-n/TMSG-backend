package ku.cs.tmsg.service;

import ku.cs.tmsg.dto.request.TaskStatusUpdateRequest;
import ku.cs.tmsg.dto.response.OrderStatusResponse;
import ku.cs.tmsg.entity.OrderStatus;
import ku.cs.tmsg.exception.DatabaseException;
import ku.cs.tmsg.repository.OrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Service
public class OrderStatusService {
    @Autowired
    private OrderStatusRepository orderStatusRepository;

    public OrderStatusResponse setOrderStatus(TaskStatusUpdateRequest request) throws DatabaseException {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(request.getOrderID());
        orderStatus.setOrderStatus(request.getStatus());
        orderStatus.setTimestampStatus(Date.from(Instant.now().atZone(ZoneId.of("Asia/Bangkok")).toInstant()));
        orderStatus.setHistoryStatusId(UUID.randomUUID().toString());
        OrderStatus result = orderStatusRepository.save(orderStatus);
        if (result == null) {
            throw new DatabaseException("Order Status could not be saved");
        }
        OrderStatusResponse response = new OrderStatusResponse();
        response.setStatusID(result.getHistoryStatusId());
        response.setOrderID(orderStatus.getOrderId());
        response.setStatus(orderStatus.getOrderStatus());
        response.setTimestamp(orderStatus.getTimestampStatus());

        return response;
    }

    public int getOrderStatusCount(String orderID) {
        return orderStatusRepository.getStatusCount(orderID);
    }
}
