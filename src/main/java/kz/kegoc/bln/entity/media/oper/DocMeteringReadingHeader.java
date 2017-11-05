package kz.kegoc.bln.entity.media.oper;

import kz.kegoc.bln.entity.common.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of= {"id"})
public class DocMeteringReadingHeader implements HasId {
    private Long id;
    private String name;
    private String header;
    private TemplateMeteringReading template;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}
