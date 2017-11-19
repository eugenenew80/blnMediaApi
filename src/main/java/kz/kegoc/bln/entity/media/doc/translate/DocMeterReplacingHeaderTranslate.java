package kz.kegoc.bln.entity.media.doc.translate;

import kz.kegoc.bln.entity.common.*;
import kz.kegoc.bln.entity.media.Lang;
import kz.kegoc.bln.entity.media.doc.DocMeterReplacingHeader;
import lombok.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of= {"id"})
public class DocMeterReplacingHeaderTranslate implements HasId, HasName, HasLang, HasDates {
    private Long id;

    @NotNull
    private Lang lang;

    @NotNull
    private DocMeterReplacingHeader header;

    @NotNull @Size(max = 100)
    private String name;

    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}
