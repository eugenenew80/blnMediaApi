package kz.kegoc.bln.entity.data;

import kz.kegoc.bln.entity.adm.User;
import kz.kegoc.bln.entity.common.HasId;
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
    private ParamType paramType;
    private List<ParameterConf> confs;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private User createBy;
    private User lastUpdateBy;
}
