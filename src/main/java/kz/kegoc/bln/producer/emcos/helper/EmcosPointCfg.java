package kz.kegoc.bln.producer.emcos.helper;

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