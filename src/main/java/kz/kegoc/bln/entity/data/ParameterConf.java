package kz.kegoc.bln.entity.data;

import kz.kegoc.bln.entity.adm.User;
import kz.kegoc.bln.entity.common.HasId;
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
    private User createBy;
    private User lastUpdateBy;
}
