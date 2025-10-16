package ku.cs.tmsg.service;

import ku.cs.tmsg.dto.response.report.ShippingTotalResponse;
import ku.cs.tmsg.dto.response.report.ShippingTotalResponseEntry;
import ku.cs.tmsg.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
    private ReportRepository reportRepository;
    @Autowired
    public ReportService(ReportRepository reportRepository) {this.reportRepository = reportRepository;}


    public ShippingTotalResponse getTotalDelivery(int year) {
        return reportRepository.getTotalShipping(year, "ยอดขน");
    }

    public ShippingTotalResponse getTotalTravel(int year) {
        return reportRepository.getTotalShipping(year, "เที่ยววิ่ง");
    }

    public ShippingTotalResponse getTotalDistance(int year) {
        return reportRepository.getTotalShipping(year, "ระยะทาง");
    }

    public ShippingTotalResponse getTotalCar(int year) {
        return reportRepository.getTotalShipping(year, "รถ");
    }

    public ShippingTotalResponse getTotalDriver(int year) {
        return reportRepository.getTotalShipping(year, "พนักงานขับรถ");
    }
}
