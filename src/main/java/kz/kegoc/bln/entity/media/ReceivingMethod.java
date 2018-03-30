package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.common.enums.ReceivingMethodEnum;
import kz.kegoc.bln.common.interfaces.HasCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"code"})
public class ReceivingMethod implements HasCode {
    public ReceivingMethod() { }

    private ReceivingMethod(String code) {
        this.code = code;
    }

    public static ReceivingMethod newInstance(ReceivingMethodEnum receivingMethodEnum) {
        return new ReceivingMethod(receivingMethodEnum.name());
    }

    private String code;
}
