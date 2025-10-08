package ku.cs.tmsg.service;

import ku.cs.tmsg.dto.CarRequest;
import ku.cs.tmsg.entity.Car;
import ku.cs.tmsg.entity.enums.CarAndDriverStatus;
import ku.cs.tmsg.entity.enums.CarType;
import ku.cs.tmsg.entity.enums.CarWeight;
import ku.cs.tmsg.exception.NotFoundException;
import ku.cs.tmsg.repository.CarRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    private CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public void createCar(List<CarRequest> request) throws Exception {
        for (CarRequest carRequest : request) {
            Car car = new Car();
            car.setId(carRequest.getId());
            car.setStatus(CarAndDriverStatus.fromLabel(carRequest.getStatus()));
            car.setWeight(CarWeight.fromLabel(carRequest.getWeight()));
            car.setType(CarType.fromLabel(carRequest.getType()));
            car.setAvailable(true);
            car.setNote(null);
            car.setLicense(carRequest.getLicense());
            carRepository.save(car);
        }
    }

    public List<Car> getAllCars() {
        return carRepository.get();
    }
}
