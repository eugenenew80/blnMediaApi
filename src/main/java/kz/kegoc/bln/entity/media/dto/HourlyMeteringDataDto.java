package kz.kegoc.bln.entity.media.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
public class HourlyMeteringDataDto {
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date meteringDate;
	private Byte hour;
	private String meteringPointCode;
	private String paramCode;
	private String unitCode;
	private String dataSourceCode;
	private MeteringDataStatus status;
	private Double val;
}
