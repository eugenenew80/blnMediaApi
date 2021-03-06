package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.common.interfaces.HasId;
import kz.kegoc.bln.entity.dict.Unit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(of= {"id"})
public class Parameter implements HasId {
    private Long id;
    private String code;
    private String name;
    private String shortName;
    private Unit unit;
    private Boolean isAt;
    private Boolean isPt;
    private List<ParameterConf> confs;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}
