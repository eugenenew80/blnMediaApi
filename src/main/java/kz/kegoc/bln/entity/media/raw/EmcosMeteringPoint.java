package kz.kegoc.bln.entity.media.raw;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"pointCode", "emcosParamCode"})
public class EmcosMeteringPoint {
	String pointCode;
	String emcosParamCode;
	String unitCode;
}
