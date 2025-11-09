package ku.cs.tmsg.service;

import ku.cs.tmsg.dto.request.CarCreate;
import ku.cs.tmsg.dto.request.CarUpdate;
import ku.cs.tmsg.dto.request.NoteUpdate;
import ku.cs.tmsg.entity.Car;
import ku.cs.tmsg.entity.enums.CarAndDriverStatus;
import ku.cs.tmsg.entity.enums.CarType;
import ku.cs.tmsg.entity.enums.CarWeight;
import ku.cs.tmsg.repository.CarRepository;
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

    public void createCar(List<CarCreate> request) throws Exception {
        for (CarCreate carRequest : request) {
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

    public void updateCars(List<CarUpdate> request) throws Exception {
       for (CarUpdate carRequest : request) {
           carRepository.update(carRequest.getStatus(), carRequest.isAvailable(), carRequest.getId());
       }
    }

    public void updateNoteCar(NoteUpdate request) throws Exception {
        carRepository.updateNote(request.getId(), request.getNote());
    }

    public void deleteCar(String id) throws Exception {
        carRepository.delete(id);
    }
}
