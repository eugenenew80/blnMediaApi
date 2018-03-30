package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.common.interfaces.HasId;
import kz.kegoc.bln.entity.dict.Unit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of= {"id"})
public class ParameterConf implements HasId {
    private Long id;
    private SourceSystem sourceSystemCode;
    private String sourceParamCode;
    private String sourceUnitCode;
    private Unit sourceUnit;
    private Parameter param;
    private Integer interval;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}
