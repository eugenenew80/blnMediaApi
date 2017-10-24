package kz.kegoc.bln.gateway.emcos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"pointCode", "emcosParamCode"})
public class EmcosPointCfg {
	String pointCode;
	String emcosParamCode;
	String paramCode;
	String unitCode;
}
