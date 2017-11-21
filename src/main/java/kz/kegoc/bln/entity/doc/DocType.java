package kz.kegoc.bln.entity.doc;

import kz.kegoc.bln.entity.common.HasDates;
import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.entity.common.HasLang;
import kz.kegoc.bln.entity.common.HasName;
import kz.kegoc.bln.entity.common.Lang;
import kz.kegoc.bln.entity.doc.translate.DocTypeTranslate;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@EqualsAndHashCode(of= {"id"})
public class DocType implements HasId, HasName, HasLang, HasDates {
    private Long id;
    private Lang lang;
    private String name;

    @NotNull @Size(max = 30)
    private String code;

    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;

    private Map<Lang, DocTypeTranslate> translations;
}
