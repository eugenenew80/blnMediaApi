package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.common.interfaces.HasCode;
import kz.kegoc.bln.common.enums.SourceSystemEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"code"})
public class SourceSystem implements HasCode {
    public SourceSystem() { }

    private SourceSystem(String code) {
        this.code = code;
    }

    public static SourceSystem newInstance(SourceSystemEnum sourceSystemEnum) {
        return new SourceSystem(sourceSystemEnum.name());
    }

    private String code;
}
