package ku.cs.tmsg.service;

import ku.cs.tmsg.dto.request.OrderCreate;
import ku.cs.tmsg.dto.request.OrderUpdateStatus;
import ku.cs.tmsg.dto.request.UpdateSentGasWeightRequest;
import ku.cs.tmsg.dto.response.OrderResponse;
import ku.cs.tmsg.dto.response.UpdateSentGasWeightResponse;
import ku.cs.tmsg.dto.response.TotalOrderStatus;
import ku.cs.tmsg.entity.Order;
import ku.cs.tmsg.entity.enums.OrderStatus;
import ku.cs.tmsg.exception.DatabaseException;
import ku.cs.tmsg.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    @Autowired
    public OrderService(OrderRepository orderRepository) {this.orderRepository = orderRepository;}

    public void createOrder(List<OrderCreate> request) throws Exception {
        String dateFormat = "dd/MM/yyyy HH:mm";
        Date now = new Date();
        for (OrderCreate orderCreate : request) {
            Order order = new Order();
            order.setId(orderCreate.getId());
            order.setOrder_date(now);
            order.setDeadline(new SimpleDateFormat(dateFormat).parse(orderCreate.getDeadline()));
            order.setDrop(orderCreate.getDrop());
            order.setNote(orderCreate.getNote());
            order.setStatus(OrderStatus.WAITING);
            order.setDestination(orderCreate.getDestination());
            order.setSource(orderCreate.getSource());
            order.setGas_amount(orderCreate.getGas_amount());
            order.setGas_send(0);

            orderRepository.save(order, orderCreate.getCar_id(), orderCreate.getTel1(), orderCreate.getTel2());
        }
    }

    public UpdateSentGasWeightResponse updateSentGasWeight(UpdateSentGasWeightRequest request) throws DatabaseException {
        int rowAffected = orderRepository.updateSentGasWeight(request.getOrderID(), request.getWeight());
        if (rowAffected < 1) {
            throw new DatabaseException("Can't update sent gas weight");
        }
        UpdateSentGasWeightResponse response = new UpdateSentGasWeightResponse();
        response.setOrderID(request.getOrderID());
        response.setWeight(request.getWeight());
        return response;
    }


    public List<OrderResponse> getOrder(String order_status) {
        return orderRepository.get(order_status);
    }

    public TotalOrderStatus getTotalOrderStatus() {
        return orderRepository.getTotalOrderStatus();
    }

    public void updateStatus(OrderUpdateStatus orderStatus) {
        orderRepository.updateStatus(orderStatus.getOrder_id(), orderStatus.getStatus());
    }
}
