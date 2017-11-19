package kz.kegoc.bln.entity.media.data;

import java.time.*;

import kz.kegoc.bln.entity.dict.Meter;
import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.entity.media.DataSource;
import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.Metering;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class DayMeteringBalance implements Metering {
	private Long id;
	private MeteringPoint meteringPoint;
	private Meter meter;
	private LocalDateTime meteringDate;
	private String externalCode;
	private String paramCode;
	private String unitCode;
	private DataSource dataSource;
	private DataStatus status;
	private Double val;
	private LocalDateTime createDate;
	private LocalDateTime lastUpdateDate;
}
