package kz.kegoc.bln.entity.doc;

import kz.kegoc.bln.entity.common.HasDates;
import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.entity.common.HasLang;
import kz.kegoc.bln.entity.common.HasName;
import kz.kegoc.bln.entity.dict.Meter;
import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.entity.common.Lang;
import kz.kegoc.bln.entity.doc.translate.DocMeterReplacingHeaderTranslate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Data
@EqualsAndHashCode(of= {"id"})
public class DocMeterReplacingHeader implements HasId, HasName, HasLang, HasDates {
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
    private Meter oldMeter;

    @NotNull
    private Meter newMeter;

    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private List<DocMeterReplacingLine> lines;
    private Map<Lang, DocMeterReplacingHeaderTranslate> translations;
}
