package ku.cs.tmsg.service;

import ku.cs.tmsg.dto.request.CarUpdate;
import ku.cs.tmsg.dto.request.DriverCreate;
import ku.cs.tmsg.dto.request.DriverUpdate;
import ku.cs.tmsg.dto.request.NoteUpdate;
import ku.cs.tmsg.dto.response.DriverDataResponse;
import ku.cs.tmsg.entity.Driver;
import ku.cs.tmsg.entity.enums.CarAndDriverStatus;
import ku.cs.tmsg.exception.NotFoundException;
import ku.cs.tmsg.repository.DriverRepository;
import ku.cs.tmsg.security.JwtUtil;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {
    private DriverRepository driverRepository;
    private JwtUtil jwtUtil;

    @Autowired
    public DriverService(DriverRepository driverRepository, JwtUtil jwtUtil) {
        this.driverRepository = driverRepository;
        this.jwtUtil = jwtUtil;
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

    public DriverDataResponse getDriverData(String jwt) throws NotFoundException {
        DriverDataResponse driverDataResponse = new DriverDataResponse();
        String userID = jwtUtil.getUserIDFromToken(jwt);
        Driver driver = driverRepository.findByID(userID);
        if (driver == null) {
            throw new NotFoundException("User not found. Token might be expired.");
        }
        driverDataResponse.setName(driver.getName());
        driverDataResponse.setPhone(driver.getTel());
        driverDataResponse.setStatus(driver.isAvailable() ? "พร้อมรับงาน" : "ไม่พร้อมรับงาน");
        return driverDataResponse;
    }

    public void updateDriver(List<DriverUpdate> request) throws Exception {
        for (DriverUpdate driverRequest : request) {
            driverRepository.update(driverRequest.getStatus(), driverRequest.isAvailable(), driverRequest.getTel());
        }
    }

    public void updateNoteDriver(NoteUpdate request) throws Exception {
        driverRepository.updateNote(request.getId(), request.getNote());
    }

    public void deleteDriver(String id) throws Exception {
        driverRepository.delete(id);
    }
}
