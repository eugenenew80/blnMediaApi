package kz.kegoc.bln.entity.media.day;

import java.time.*;
import kz.kegoc.bln.entity.dict.*;
import kz.kegoc.bln.entity.media.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class DayMeteringDataOper implements Metering {
	private Long id;
	private MeteringPoint meteringPoint;
	private Meter meter;
	private LocalDate operDate;
	private String paramCode;
	private String unitCode;
	private String dataSourceCode;
	private WayEntering wayEntering;
	private Double startBalance;
	private Double endBalance;
	private Double dif;
	private DataStatus status;
	private LocalDateTime createDate;
	private LocalDateTime lastUpdateDate;
}
