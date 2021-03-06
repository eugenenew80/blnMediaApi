package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.common.interfaces.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(of= {"id"})
@DynamicUpdate
public class WorkListHeader implements HasId {
    private Long id;
    private String name;
    private String workListType;
    private SourceSystem sourceSystemCode;
    private Direction direction;
    private ConnectionConfig config;
    private BatchStatus atStatus;
    private BatchStatus ptStatus;
    private Boolean active;
    private List<WorkListLine> lines;
    private Batch atBatch;
    private Batch ptBatch;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}
