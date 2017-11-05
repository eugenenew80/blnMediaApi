package kz.kegoc.bln.entity.media.oper;

import kz.kegoc.bln.entity.common.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class MeteringReadingTemplate implements HasId {
    private Long id;
    private String name;
    private String header;
    private DocType docType;
    private Group group;
}
