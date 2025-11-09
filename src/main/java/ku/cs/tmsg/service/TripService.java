package ku.cs.tmsg.service;

import ku.cs.tmsg.dto.request.OrderCreate;
import ku.cs.tmsg.dto.request.SpecialTripCreate;
import ku.cs.tmsg.dto.response.OrderResponse;
import ku.cs.tmsg.dto.response.SpecialTripResponse;
import ku.cs.tmsg.entity.Order;
import ku.cs.tmsg.entity.enums.OrderStatus;
import ku.cs.tmsg.repository.OrderRepository;
import ku.cs.tmsg.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class TripService {
    private TripRepository tripRepository;
    @Autowired
    public TripService(TripRepository tripRepository) {this.tripRepository = tripRepository;}

    public List<SpecialTripResponse> getSpecialTripsByTripId(String tripId) {
        return tripRepository.getSpecialTripByTripID(tripId);
    }

    public void createSpecialTrip(SpecialTripCreate request) throws Exception {
        tripRepository.saveSpecialTrip(request);
    }

    public void deleteSpecialTripById(String id) throws Exception {
        tripRepository.deleteSpecialTripsById(id);
    }
}
