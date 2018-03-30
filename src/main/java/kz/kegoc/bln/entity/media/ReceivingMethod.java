package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.common.enums.ReceivingMethodEnum;
import kz.kegoc.bln.common.interfaces.HasCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of= {"code"})
@AllArgsConstructor
@NoArgsConstructor
public class ReceivingMethod implements HasCode {
    public static ReceivingMethod newInstance(ReceivingMethodEnum receivingMethodEnum) {
        return new ReceivingMethod(receivingMethodEnum.name());
    }

    private String code;
}
