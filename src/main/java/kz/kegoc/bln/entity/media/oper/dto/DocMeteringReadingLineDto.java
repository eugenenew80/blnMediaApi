package kz.kegoc.bln.entity.media.oper.dto;

import kz.kegoc.bln.entity.media.WayEntering;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DocMeteringReadingLineDto {
	private Long id;
	private Long headerId;
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
	private String dataSourceCode;
	private WayEntering wayEntering;
	private Double startBalance;
	private Double endBalance;
	private Double flow;
}
