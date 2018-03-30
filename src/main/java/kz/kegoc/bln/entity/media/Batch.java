package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.common.enums.BatchStatusEnum;
import kz.kegoc.bln.common.interfaces.HasId;
import kz.kegoc.bln.common.enums.ParamTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import static kz.kegoc.bln.entity.media.ParamType.newInstance;

@Data
@EqualsAndHashCode(of= {"id"})
public class Batch implements HasId {
    public Batch() {}

    public Batch(WorkListHeader header, ParamTypeEnum paramTypeEnum) {
        this.workListHeader = header;
        this.sourceSystemCode = header.getSourceSystemCode();
        this.direction = header.getDirection();
        this.paramType = newInstance(paramTypeEnum);
        this.status = BatchStatus.newInstance(BatchStatusEnum.P);
        this.startDate = LocalDateTime.now();
    }

    private Long id;
    private WorkListHeader workListHeader;
    private SourceSystem sourceSystemCode;
    private Direction direction;
    private ParamType paramType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long recCount;
    private BatchStatus status;
    private String errMsg;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}
