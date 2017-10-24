package kz.kegoc.bln.producer.emcos.gateway;

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
