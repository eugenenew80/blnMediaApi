package kz.kegoc.bln.gateway.ftp;

import kz.kegoc.bln.entity.data.PowerConsumption;
import lombok.Data;
import java.util.List;

@Data
public class ExportPoint {
    private String sourceMeteringPointCode;
    private List<PowerConsumption> data;
}
