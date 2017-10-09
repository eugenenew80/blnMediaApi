package kz.kegoc.bln.entity.media.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class HourlyMeteringDataRawListDto {
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private LocalDateTime meteringDate;
	private String meteringPointCode;
	private List<HourlyMeteringDataRawDto> meteringData;
}
