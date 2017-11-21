package kz.kegoc.bln.entity.data;

import java.time.LocalDateTime;

import kz.kegoc.bln.entity.dict.Meter;
import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.entity.common.DataSource;
import kz.kegoc.bln.entity.common.DataStatus;
import kz.kegoc.bln.entity.common.Metering;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class MonthMeteringFlow implements Metering {
	private Long id;
	private MeteringPoint meteringPoint;
	private Meter meter;
	private Short year;
	private Short month;
	private String externalCode;
	private String paramCode;
	private String unitCode;
	private DataSource dataSource;
	private DataStatus status;
	private Double val;
	private LocalDateTime createDate;
	private LocalDateTime lastUpdateDate;
}
