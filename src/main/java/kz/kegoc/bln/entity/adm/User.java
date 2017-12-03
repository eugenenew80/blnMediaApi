package kz.kegoc.bln.entity.adm;

import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.entity.common.HasName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(of= {"id"})
public class User implements HasId, HasName {
	private Long id;

	@NotNull @Size(max=30)
	private String name;

	@NotNull @Size(max=100)
	private String description;
}
