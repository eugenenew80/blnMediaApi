package kz.kegoc.bln.entity.data;

import kz.kegoc.bln.entity.common.HasCode;
import kz.kegoc.bln.entity.common.DirectionEnum;
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
