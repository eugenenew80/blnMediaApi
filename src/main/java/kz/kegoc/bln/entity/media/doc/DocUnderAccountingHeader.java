package kz.kegoc.bln.entity.media.doc;

import kz.kegoc.bln.entity.common.*;
import kz.kegoc.bln.entity.dict.*;
import kz.kegoc.bln.entity.media.Lang;
import kz.kegoc.bln.entity.media.doc.translate.DocUnderAccountingHeaderTranslate;
import lombok.*;
import javax.validation.constraints.NotNull;
import java.time.*;
import java.util.*;

@Data
@EqualsAndHashCode(of= {"id"})
public class DocUnderAccountingHeader implements HasId, HasName, HasLang, HasDates {
    private Long id;
    private Lang lang;
    private String name;

    @NotNull
    private LocalDate docDate;

    @NotNull
    private DocType docType;

    @NotNull
    private MeteringPoint meteringPoint;

    @NotNull
    private Meter meter;

    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private List<DocUnderAccountingMeasLine> measLines;
    private List<DocUnderAccountingCalcLine> calcLines;
    private Map<Lang, DocUnderAccountingHeaderTranslate> translations;
}
