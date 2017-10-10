package kz.kegoc.bln.entity.media.raw.dto;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

import kz.kegoc.bln.entity.media.WayEnteringData;
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import lombok.Data;

@Data
public class DayMeteringDataRawDto {
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private LocalDate meteringDate;
	private String code;
	private String externalCode;
	private String paramCode;
	private String unitCode;
	private WayEnteringData wayEntering;
	private MeteringDataStatus status;
	private String dataSourceCode;
	private Double val;
}
