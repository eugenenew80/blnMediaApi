package kz.kegoc.bln.entity.media.dto.hour;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class HourMeteringDataRawListDto {
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private LocalDate meteringDate;
	private String externalCode;
	private List<HourMeteringDataRawDto> meteringData;
}
