package kz.kegoc.bln.entity.media.raw;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"pointCode", "emcosParamCode"})
public class EmcosMeteringPointCfg {
	String pointCode;
	String emcosParamCode;
	String unitCode;
}
