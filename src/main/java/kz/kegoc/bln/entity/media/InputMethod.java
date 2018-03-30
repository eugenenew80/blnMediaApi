package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.common.interfaces.HasCode;
import kz.kegoc.bln.common.enums.InputMethodEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"code"})
public class InputMethod implements HasCode {
    public InputMethod() { }

    private InputMethod(String code) {
        this.code = code;
    }

    public static InputMethod newInstance(InputMethodEnum inputMethodEnum) {
        return new InputMethod(inputMethodEnum.name());
    }

    private String code;
}
