package kz.kegoc.bln.entity.dict;

import kz.kegoc.bln.common.interfaces.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(of= {"id"})
public class Unit implements HasId, HasCode, HasName {
	private Long id;

	@NotNull @Size(max = 10)
	private String code;

	@NotNull @Size(max = 100)
	private String name;

	@NotNull @Size(max = 10)
	private String shortName;
}
