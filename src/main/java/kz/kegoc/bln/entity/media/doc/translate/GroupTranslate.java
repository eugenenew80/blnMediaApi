package kz.kegoc.bln.entity.media.doc.translate;

import kz.kegoc.bln.entity.common.HasDates;
import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.entity.common.HasLang;
import kz.kegoc.bln.entity.common.HasName;
import kz.kegoc.bln.entity.media.Lang;
import kz.kegoc.bln.entity.media.doc.DocUnderAccountingHeader;
import kz.kegoc.bln.entity.media.doc.Group;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of= {"id"})
public class GroupTranslate implements HasId, HasName, HasLang, HasDates {
    private Long id;

    @NotNull
    private Lang lang;

    @NotNull
    private Group group;

    @NotNull @Size(max = 100)
    private String name;

    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}
