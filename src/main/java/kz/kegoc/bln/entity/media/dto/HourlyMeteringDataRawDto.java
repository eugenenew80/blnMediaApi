package kz.kegoc.bln.entity.media.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kz.kegoc.bln.entity.media.WayEnteringData;
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HourlyMeteringDataRawDto {
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private LocalDateTime meteringDate;
	private Byte hour;
	private String code;
	private String externalCode;
	private String paramCode;
	private String unitCode;
	private WayEnteringData wayEntering;
	private MeteringDataStatus status;
	private String dataSourceCode;
	private Double val;
}
