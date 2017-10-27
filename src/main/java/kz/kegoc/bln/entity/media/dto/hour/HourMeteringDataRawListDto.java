package kz.kegoc.bln.entity.media.dto.hour;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HourMeteringDataRawListDto {
	private LocalDate meteringDate;
	private String externalCode;
	private List<HourMeteringDataRawDto> meteringData;
}
