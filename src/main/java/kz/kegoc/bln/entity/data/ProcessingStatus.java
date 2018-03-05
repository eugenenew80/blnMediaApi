package kz.kegoc.bln.entity.data;

import kz.kegoc.bln.entity.common.HasCode;
import kz.kegoc.bln.entity.common.ProcessingStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"code"})
public class ProcessingStatus implements HasCode {
    public ProcessingStatus() { }

    private ProcessingStatus(String code) {
        this.code = code;
    }

    public static ProcessingStatus newInstance(ProcessingStatusEnum processingStatusEnum) {
        return new ProcessingStatus(processingStatusEnum.name());
    }

    private String code;
}
