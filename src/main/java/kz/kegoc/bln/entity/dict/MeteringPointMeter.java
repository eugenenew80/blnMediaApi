package kz.kegoc.bln.entity.dict;

import kz.kegoc.bln.entity.common.HasId;
import lombok.*;

@Data
@EqualsAndHashCode(of= {"id"})
public class MeteringPointMeter implements HasId {
	private Long id;
	private MeteringPoint meteringPoint;
	private Meter meter;
}
