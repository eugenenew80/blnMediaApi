package kz.kegoc.bln.producer.emcos.helper;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"pointCode", "emcosParamCode"})
public class EmcosPointParamCfg {
	String pointCode;
	String emcosParamCode;
	String unitCode;
}
