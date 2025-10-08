package ku.cs.tmsg.service;

import ku.cs.tmsg.dto.DriverRequest;
import ku.cs.tmsg.entity.Car;
import ku.cs.tmsg.entity.Driver;
import ku.cs.tmsg.entity.enums.CarAndDriverStatus;
import ku.cs.tmsg.repository.DriverRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {
    private DriverRepository driverRepository;

    @Autowired
    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public void createDriver(List<DriverRequest> request) throws Exception {
        for (DriverRequest driverRequest : request) {
            Driver driver = new Driver();
            try {
                driver.setTel(driverRequest.getTel());
                driver.setStatus(CarAndDriverStatus.READY);
                driver.setAvailable(true);
                driver.setNote(null);
                driver.setLine_ID(null);
                driver.setName(driverRequest.getName());
            } catch (Exception e) {
                throw new BadRequestException("bad request exception found please try again");
            }
            driverRepository.save(driver);
        }
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.get();
    }
}
