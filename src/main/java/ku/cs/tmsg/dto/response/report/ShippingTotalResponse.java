package ku.cs.tmsg.dto.response.report;

import lombok.Data;

import java.util.List;

@Data
public class ShippingTotalResponse {
    private List<ShippingTotalResponseEntry> shipping_8_ton;
    private List<ShippingTotalResponseEntry> shipping_10_ton;
    private List<ShippingTotalResponseEntry> shipping_trailer;
}
