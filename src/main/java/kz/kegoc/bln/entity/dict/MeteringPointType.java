package kz.kegoc.bln.entity.dict;

import kz.kegoc.bln.entity.common.HasCode;
import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.entity.common.HasName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class MeteringPointType implements HasId, HasCode, HasName {
	private Long id;
	private String code;
	private String name;
}
