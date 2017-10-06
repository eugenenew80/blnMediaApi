package kz.kegoc.bln.entity.media.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import kz.kegoc.bln.entity.media.WayEnteringData;
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import lombok.Data;

@Data
public class DailyMeteringDataRawDto {
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date meteringDate;
	private String meteringPointCode;
	private String paramCode;
	private String unitCode;
	private WayEnteringData wayEntering;
	private MeteringDataStatus status;
	private String dataSourceCode;
	private Double val;
}
