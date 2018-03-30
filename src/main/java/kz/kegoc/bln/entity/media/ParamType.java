package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.common.interfaces.HasCode;
import kz.kegoc.bln.common.enums.ParamTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"code"})
public class ParamType implements HasCode {
    public ParamType() { }

    private ParamType(String code) {
        this.code = code;
    }

    public static ParamType newInstance(ParamTypeEnum paramTypeEnum) {
        return new ParamType(paramTypeEnum.name());
    }

    private String code;
}
