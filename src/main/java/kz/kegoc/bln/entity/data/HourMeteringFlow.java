package kz.kegoc.bln.entity.data;

import kz.kegoc.bln.entity.dict.Meter;
import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.entity.common.DataSource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.time.LocalDateTime;

import kz.kegoc.bln.entity.common.DataStatus;
import kz.kegoc.bln.entity.common.Metering;

@Data
@EqualsAndHashCode(of= {"id"})
public class HourMeteringFlow implements Metering {
	private Long id;
	private MeteringPoint meteringPoint;
	private Meter meter;
	private LocalDate meteringDate;
	private Integer hour;
	private String externalCode;
	private String paramCode;
	private String unitCode;
	private DataSource dataSource;
	private DataStatus status;
	private Double val;
	private LocalDateTime createDate;
	private LocalDateTime lastUpdateDate;
}
