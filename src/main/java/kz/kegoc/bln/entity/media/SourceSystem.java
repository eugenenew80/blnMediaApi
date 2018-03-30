package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.common.interfaces.HasCode;
import kz.kegoc.bln.common.enums.SourceSystemEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of= {"code"})
@AllArgsConstructor
@NoArgsConstructor
public class SourceSystem implements HasCode {
    public static SourceSystem newInstance(SourceSystemEnum sourceSystemEnum) {
        return new SourceSystem(sourceSystemEnum.name());
    }

    private String code;
}
