package kz.kegoc.bln.entity.media.raw.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class HourMeteringDataRawListDto {
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private LocalDate meteringDate;
	private String meteringPointCode;
	private List<HourMeteringDataRawDto> meteringData;
}
