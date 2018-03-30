package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.common.interfaces.HasCode;
import kz.kegoc.bln.common.enums.BatchStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of= {"code"})
@AllArgsConstructor
@NoArgsConstructor
public class BatchStatus implements HasCode {
    public static BatchStatus newInstance(BatchStatusEnum batchStatusEnum) {
        return new BatchStatus(batchStatusEnum.name());
    }

    private String code;
}
