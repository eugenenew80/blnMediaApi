package kz.kegoc.bln.entity.media.doc;

import kz.kegoc.bln.entity.common.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(of= {"id"})
public class DocType implements HasId {
    private Long id;

    @NotNull @Size(max = 30)
    private String code;

    @NotNull @Size(max = 100)
    private String name;
}
