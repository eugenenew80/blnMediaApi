package kz.kegoc.bln.gateway.emcos;

import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.WayEntering;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MinuteMeteringData {
	private LocalDateTime meteringDate;
	private String externalCode;
	private String paramCode;
	private String unitCode;
	private String dataSourceCode;
	private WayEntering wayEntering;
	private DataStatus status;
	private Double val;
}
