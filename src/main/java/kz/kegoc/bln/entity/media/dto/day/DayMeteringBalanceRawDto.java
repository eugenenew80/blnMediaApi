package kz.kegoc.bln.entity.media.dto.day;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.DataStatus;
import lombok.Data;

@Data
public class DayMeteringBalanceRawDto {
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private LocalDateTime meteringDate;
	private String externalCode;
	private String paramCode;
	private String unitCode;
	private String dataSourceCode;
	private WayEntering wayEntering;
	private DataStatus status;
	private Double val;
}
