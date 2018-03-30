package kz.kegoc.bln.entity.dict;

import kz.kegoc.bln.common.interfaces.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class Unit implements HasId, HasCode, HasName {
	private Long id;
	private String code;
	private String name;
	private String shortName;
}
