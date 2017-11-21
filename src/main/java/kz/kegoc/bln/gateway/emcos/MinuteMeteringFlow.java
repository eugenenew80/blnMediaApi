package kz.kegoc.bln.gateway.emcos;

import kz.kegoc.bln.entity.common.DataSource;
import kz.kegoc.bln.entity.common.DataStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MinuteMeteringFlow {
	private LocalDateTime meteringDate;
	private String externalCode;
	private String paramCode;
	private String unitCode;
	private DataSource dataSource;
	private DataStatus status;
	private Double val;
}
