package kz.kegoc.bln.entity.data;

import kz.kegoc.bln.entity.common.DataSource;
import kz.kegoc.bln.entity.common.Metering;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of= {"id"})
public class MeasDataRaw implements Metering  {
	private Long id;
	private LocalDateTime measDate;
	private String sourceMeteringPointCode;
	private String sourceParamCode;
	private String sourceUnitCode;
	private DataSource dataSourceCode;
	private Double val;
	private LocalDateTime createDate;
	private LocalDateTime lastUpdateDate;
}
