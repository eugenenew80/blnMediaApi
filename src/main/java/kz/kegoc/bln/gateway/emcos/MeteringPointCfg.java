package kz.kegoc.bln.gateway.emcos;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of= {"pointCode", "emcosParamCode"})
public class MeteringPointCfg {
	private String pointCode;
	private String emcosParamCode;
	private String paramCode;
	private String unitCode;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
}
