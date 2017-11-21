package kz.kegoc.bln.entity.doc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import kz.kegoc.bln.entity.common.HasDates;
import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.entity.common.HasLang;
import kz.kegoc.bln.entity.common.HasName;
import kz.kegoc.bln.entity.common.Lang;
import kz.kegoc.bln.entity.doc.translate.GroupTranslate;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(of= {"id"})
public class Group implements HasId, HasName, HasLang, HasDates {
    private Long id;
    private Lang lang;
    private String name;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private List<GroupMeteringPoint> meteringPoints;
    private Map<Lang, GroupTranslate> translations;
}
