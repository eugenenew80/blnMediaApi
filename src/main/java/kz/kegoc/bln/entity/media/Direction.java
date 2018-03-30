package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.common.interfaces.HasCode;
import kz.kegoc.bln.common.enums.DirectionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of= {"code"})
@AllArgsConstructor
@NoArgsConstructor
public class Direction implements HasCode {
    public static Direction newInstance(DirectionEnum directionEnum) {
        return new Direction(directionEnum.name());
    }

    private String code;
}
