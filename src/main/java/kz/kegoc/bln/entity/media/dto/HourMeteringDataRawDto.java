package kz.kegoc.bln.entity.media.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.DataStatus;
import lombok.Data;
import java.time.LocalDate;

@Data
public class HourMeteringDataRawDto {
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private LocalDate meteringDate;
	private Integer hour;
	private String externalCode;
	private String paramCode;
	private String unitCode;
	private String dataSourceCode;
	private WayEntering wayEntering;
	private DataStatus status;
	private Double val;
}
