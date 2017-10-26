package kz.kegoc.bln.entity.dict;

import kz.kegoc.bln.entity.common.*;
import lombok.*;

@Data
@EqualsAndHashCode(of= {"id"})
public class Meter implements HasId, HasCode, HasName {
	private Long id;
	private String code;
	private String name;
	private String serialNumber;
}
