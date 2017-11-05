package kz.kegoc.bln.entity.media.oper.dto;

import java.time.*;
import kz.kegoc.bln.entity.media.*;
import lombok.Data;

@Data
public class DayMeteringDataOperDto  {
	private Long id;
	private Long meteringPointId;
	private String meteringPointCode;
	private String meteringPointExternalCode;
	private String meteringPointName;
	private Long meterId;
	private String meterCode;
	private String meterName;
	private String meterSerialNumber;
	private LocalDate operDate;
	private String paramCode;
	private String unitCode;
	private DataSource dataSource;
	private WayEntering wayEntering;
	private Double startBalance;
	private Double endBalance;
	private Double dif;
}
