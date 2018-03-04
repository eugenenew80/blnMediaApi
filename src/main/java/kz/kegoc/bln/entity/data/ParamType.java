package kz.kegoc.bln.entity.data;

import kz.kegoc.bln.entity.common.HasCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"code"})
public class ParamType implements HasCode {
    public ParamType() { }

    public ParamType(String code) {
        this.code = code;
    }

    private String code;
}
