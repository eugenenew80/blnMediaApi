package kz.kegoc.bln.gateway.ftp;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ExportPoint {
    private String sourceMeteringPointCode;
    private LocalDateTime meteringDate;
    private Double valAp;
    private Double valAm;
}
