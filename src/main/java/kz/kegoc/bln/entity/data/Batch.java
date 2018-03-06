package kz.kegoc.bln.entity.data;

import kz.kegoc.bln.entity.adm.User;
import kz.kegoc.bln.entity.common.BatchStatusEnum;
import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.entity.common.ParamTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import static kz.kegoc.bln.entity.data.ParamType.newInstance;

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
    private User createBy;
    private User lastUpdateBy;
}
