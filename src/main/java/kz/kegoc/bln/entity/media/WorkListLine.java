package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.common.interfaces.HasId;
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
    private Parameter param;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}
