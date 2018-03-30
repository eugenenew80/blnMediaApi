package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.common.interfaces.HasCode;
import kz.kegoc.bln.common.enums.ProcessingStatusEnum;
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
