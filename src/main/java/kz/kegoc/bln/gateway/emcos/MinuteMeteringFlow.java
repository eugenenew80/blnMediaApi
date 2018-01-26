package kz.kegoc.bln.gateway.emcos;

import kz.kegoc.bln.entity.common.DataSource;
import kz.kegoc.bln.entity.common.DataStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MinuteMeteringFlow {
	private LocalDateTime meteringDate;
	private String sourceMeteringPointCode;
	private String sourceParamCode;
	private String sourceUnitCode;
	private DataSource dataSourceCode;
	private String paramCode;
	private DataStatus status;
	private Double val;
}
