package kz.kegoc.bln.entity.dict;

import java.util.List;

import kz.kegoc.bln.entity.common.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class MeteringPoint implements HasId, HasCode, HasName {
    private Long id;
    private String name;
    private String code;
    private String externalCode;
    private List<MeteringPointMeter> meters;
}
