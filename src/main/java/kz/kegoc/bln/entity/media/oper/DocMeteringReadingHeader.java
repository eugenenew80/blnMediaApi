package kz.kegoc.bln.entity.media.oper;

import kz.kegoc.bln.entity.common.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(of= {"id"})
public class DocMeteringReadingHeader implements HasId {
    private Long id;
    private String name;
    private String header;
    private LocalDate startDate;
    private LocalDate endDate;
    private TemplateMeteringReading template;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private List<DocMeteringReadingLine> lines;
}
