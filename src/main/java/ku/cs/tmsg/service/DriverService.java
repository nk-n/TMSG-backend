package ku.cs.tmsg.service;

import ku.cs.tmsg.dto.request.CarUpdate;
import ku.cs.tmsg.dto.request.DriverCreate;
import ku.cs.tmsg.dto.request.DriverUpdate;
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

    public void createDriver(List<DriverCreate> request) throws Exception {
        for (DriverCreate driverRequest : request) {
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

    public void updateDriver(List<DriverUpdate> request) throws Exception {
        for (DriverUpdate driverRequest : request) {
            driverRepository.update(driverRequest.getStatus(), driverRequest.isAvailable(), driverRequest.getTel());
        }
    }
}
