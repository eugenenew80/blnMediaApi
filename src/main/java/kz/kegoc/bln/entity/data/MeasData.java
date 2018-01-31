package kz.kegoc.bln.entity.data;

import kz.kegoc.bln.entity.common.IntervalType;
import kz.kegoc.bln.entity.dict.Meter;
import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.entity.common.DataSource;
import kz.kegoc.bln.entity.dict.Unit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.time.LocalDateTime;

import kz.kegoc.bln.entity.common.DataStatus;
import kz.kegoc.bln.entity.common.Metering;

@Data
@EqualsAndHashCode(of= {"id"})
public class MeasData implements Metering {
	private Long id;
	private DataSource dataSourceCode;
	private String sourceMeteringPointCode;
	private String sourceParamCode;
	private String sourceUnitCode;
	private LocalDateTime measDate;
	private IntervalType intervalType;
	private Double val;
	private MeteringPoint meteringPoint;
	private Meter meter;
	private DataStatus status;
	private String paramCode;
	private Unit unit;
	private LocalDateTime createDate;
	private LocalDateTime lastUpdateDate;
}
