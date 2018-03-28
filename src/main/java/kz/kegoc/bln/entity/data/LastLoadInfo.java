package kz.kegoc.bln.entity.data;

import kz.kegoc.bln.entity.common.HasId;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LastLoadInfo implements HasId {
    private Long id;
    private String sourceSystemCode;
    private String sourceMeteringPointCode;
    private String sourceParamCode;
    private LocalDateTime lastLoadDate;
    private Batch lastBatch;
}
