package kz.kegoc.bln.entity.media.doc.translate;

import kz.kegoc.bln.entity.common.*;
import kz.kegoc.bln.entity.media.doc.DocMeteringReadingHeader;
import kz.kegoc.bln.entity.media.Lang;
import lombok.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of= {"id"})
public class DocMeteringReadingHeaderTranslate implements HasId, HasName, HasLang, HasDates {
    private Long id;
    private Lang lang;
    private DocMeteringReadingHeader header;
    private String name;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}
