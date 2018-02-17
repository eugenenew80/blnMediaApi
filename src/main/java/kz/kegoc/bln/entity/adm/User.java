package kz.kegoc.bln.entity.adm;

import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.entity.common.HasName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class User implements HasId, HasName {
	private Long id;
	private String code;
	private String name;
}
