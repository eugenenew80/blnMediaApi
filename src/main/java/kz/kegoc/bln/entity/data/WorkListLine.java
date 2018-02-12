package kz.kegoc.bln.entity.data;

import kz.kegoc.bln.entity.adm.User;
import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.entity.dict.MeteringPoint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of= {"id"})
public class WorkListLine implements HasId {
    private Long id;
    private WorkListHeader header;
    private MeteringPoint meteringPoint;
    private Boolean parameterAp;
    private Boolean parameterAm;
    private Boolean parameterRp;
    private Boolean parameterRm;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private User createBy;
    private User lastUpdateBy;
}
