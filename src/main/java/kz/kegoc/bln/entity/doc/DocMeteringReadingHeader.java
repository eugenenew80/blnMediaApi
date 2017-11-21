package kz.kegoc.bln.entity.doc;

import kz.kegoc.bln.entity.common.HasDates;
import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.entity.common.HasLang;
import kz.kegoc.bln.entity.common.HasName;
import kz.kegoc.bln.entity.doc.translate.DocMeteringReadingHeaderTranslate;
import kz.kegoc.bln.entity.common.Lang;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(of= {"id"})
public class DocMeteringReadingHeader implements HasId, HasName, HasLang, HasDates {
    private Long id;
    private String name;
    private Lang lang;

    @NotNull
    private DocType docType;

    @NotNull
    private Group group;

    @NotNull
    private LocalDate docDate;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private List<DocMeteringReadingLine> lines;
    private Map<Lang, DocMeteringReadingHeaderTranslate> translations;
}
