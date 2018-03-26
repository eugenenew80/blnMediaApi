package kz.kegoc.bln.entity.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class Telemetry {
    private Long id;
    private LogPoint logPoint;
    private String dateTime;
    private Double val;
}
