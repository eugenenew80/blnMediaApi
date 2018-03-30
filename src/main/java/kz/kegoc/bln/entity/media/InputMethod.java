package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.common.interfaces.HasCode;
import kz.kegoc.bln.common.enums.InputMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of= {"code"})
@AllArgsConstructor
@NoArgsConstructor
public class InputMethod implements HasCode {
    public static InputMethod newInstance(InputMethodEnum inputMethodEnum) {
        return new InputMethod(inputMethodEnum.name());
    }

    private String code;
}
