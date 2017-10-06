package kz.kegoc.bln.entity.media.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import kz.kegoc.bln.entity.media.WayEnteringData;
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import lombok.Data;
import java.util.Date;

@Data
public class HourlyMeteringDataRawDto {
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date meteringDate;
	private Byte hour;
	private String meteringPointCode;
	private String externalMeteringPointCode;
	private String paramCode;
	private String unitCode;
	private WayEnteringData wayEntering;
	private MeteringDataStatus status;
	private String dataSourceCode;
	private Double val;
}
