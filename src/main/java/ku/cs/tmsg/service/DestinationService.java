package ku.cs.tmsg.service;

import ku.cs.tmsg.dto.request.DestinationCreate;
import ku.cs.tmsg.entity.Destination;
import ku.cs.tmsg.repository.DestinationRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinationService {
    private DestinationRepository destinationRepository;

    @Autowired
    public DestinationService(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    public void createDestination(List<DestinationCreate> request) throws Exception {
        for (DestinationCreate destinationRequest : request) {
            Destination destination = new Destination();
            try {
                destination.setName(destinationRequest.getName());
                destination.setAvailable(true);
                destination.setAddress(destinationRequest.getAddress());
                destination.setRegion(destinationRequest.getRegion());
                destination.setDistance(destinationRequest.getDistance());
                destination.setProvince(destinationRequest.getProvince());
                destination.setRoute(destinationRequest.getRoute());
                destination.setTimeUse(destinationRequest.getTimeUse());
            } catch (Exception e) {
                throw new BadRequestException("bad request exception found please try again");
            }
            destinationRepository.save(destination);
        }
    }

    public List<Destination> getAllDestination() {
        return destinationRepository.get();
    }

    public void deleteDestination(String name) throws Exception {
        destinationRepository.delete(name);
    }
}
