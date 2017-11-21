package kz.kegoc.bln.entity.doc.translate;

import kz.kegoc.bln.entity.common.*;
import kz.kegoc.bln.entity.common.Lang;
import kz.kegoc.bln.entity.doc.DocUnderAccountingHeader;
import lombok.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of= {"id"})
public class DocUnderAccountingHeaderTranslate implements HasId, HasName, HasLang, HasDates {
    private Long id;

    @NotNull
    private Lang lang;

    @NotNull
    private DocUnderAccountingHeader header;

    @NotNull @Size(max = 100)
    private String name;

    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}
