package kz.kegoc.bln.entity.media.dto.day;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.DataStatus;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DayMeteringDataRawDto {
	private LocalDate meteringDate;
	private String externalCode;
	private String paramCode;
	private String unitCode;
	private String dataSourceCode;
	private WayEntering wayEntering;
	private DataStatus status;
	private Double val;
}
