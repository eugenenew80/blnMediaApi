package kz.kegoc.bln.entity.media.oper;

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
	private DataSource dataSource;
	private WayEntering wayEntering;
	private Double startBalance;
	private Double endBalance;
	private Double dif;
	private LocalDateTime createDate;
	private LocalDateTime lastUpdateDate;
}
