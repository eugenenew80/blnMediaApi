package kz.kegoc.bln.entity.media.raw.dto;

import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.DataStatus;
import lombok.Data;

@Data
public class MonthMeteringDataRawDto {
	private Short year;
	private Short month;
	private String code;
	private String externalCode;
	private String paramCode;
	private String unitCode;
	private WayEntering wayEntering;
	private DataStatus status;
	private String dataSourceCode;
	private Double val;
}
