package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.entity.common.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class Group implements HasId {
    private Long id;
    private String name;
}
