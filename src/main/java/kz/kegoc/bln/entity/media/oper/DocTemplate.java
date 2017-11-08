package kz.kegoc.bln.entity.media.oper;

import kz.kegoc.bln.entity.common.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(of= {"id"})
public class DocTemplate implements HasId {
    private Long id;

    @NotNull @Size(max = 100)
    private String name;

    @Size(max = 300)
    private String header;

    @NotNull
    private DocType docType;

    @NotNull
    private Group group;
}
