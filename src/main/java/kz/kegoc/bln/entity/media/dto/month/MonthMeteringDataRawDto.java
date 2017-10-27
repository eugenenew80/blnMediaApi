package kz.kegoc.bln.entity.media.dto.month;

import kz.kegoc.bln.entity.media.WayEntering;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import kz.kegoc.bln.entity.media.DataStatus;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MonthMeteringDataRawDto {
	private Short year;
	private Short month;
	private String externalCode;
	private String paramCode;
	private String unitCode;
	private WayEntering wayEntering;
	private DataStatus status;
	private String dataSourceCode;
	private Double val;
}
