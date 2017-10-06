package kz.kegoc.bln.entity.media.dto;

import kz.kegoc.bln.entity.media.WayEnteringData;
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import lombok.Data;

@Data
public class MonthlyMeteringDataRawDto {
	private Short meteringYear;
	private Short meteringMonth;
	private String meteringPointCode;
	private String paramCode;
	private String unitCode;
	private WayEnteringData wayEntering;
	private MeteringDataStatus status;
	private String dataSourceCode;
	private Double val;
}
