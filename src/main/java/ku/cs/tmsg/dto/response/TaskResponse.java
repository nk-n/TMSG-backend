package ku.cs.tmsg.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Data
public class TaskResponse {
    private String orderID;
    private int taskGroupID;
    private int dropID;
    private String source;
    private String weight;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "Asia/Bangkok")
    private Date loadingTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "Asia/Bangkok")
    private Date startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "Asia/Bangkok")
    private Date deadline;

    private String note;
    private String carID;
    private int carryWeight;
    private String destinationName;
    private String destinationLocation;
    private String destinationRoute;
}
