package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.common.interfaces.HasCode;
import kz.kegoc.bln.common.enums.DirectionEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"code"})
public class Direction implements HasCode {
    public Direction() { }

    private Direction(String code) {
        this.code = code;
    }

    public static Direction newInstance(DirectionEnum directionEnum) {
        return new Direction(directionEnum.name());
    }

    private String code;
}
