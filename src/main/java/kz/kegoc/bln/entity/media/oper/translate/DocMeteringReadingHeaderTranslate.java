package kz.kegoc.bln.entity.media.oper.translate;

import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.entity.media.oper.DocMeteringReadingHeader;
import kz.kegoc.bln.entity.media.raw.Lang;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of= {"id"})
public class DocMeteringReadingHeaderTranslate implements HasId {
    private Long id;
    private Lang lang;
    private DocMeteringReadingHeader header;
    private String name;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}
