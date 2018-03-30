package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.common.interfaces.HasCode;
import kz.kegoc.bln.common.enums.BatchStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"code"})
public class BatchStatus implements HasCode {
    public BatchStatus() { }

    private BatchStatus(String code) {
        this.code = code;
    }

    public static BatchStatus newInstance(BatchStatusEnum batchStatusEnum) {
        return new BatchStatus(batchStatusEnum.name());
    }

    private String code;
}
