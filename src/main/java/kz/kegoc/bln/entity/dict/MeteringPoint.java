package kz.kegoc.bln.entity.dict;

import kz.kegoc.bln.entity.common.*;
import kz.kegoc.bln.entity.media.LoadMeteringInfo;
import lombok.Data;

@Data
public class MeteringPoint implements HasId, HasCode, HasName {
    private Long id;
    private String name;
    private String code;
    private String externalCode;
    private LoadMeteringInfo loadInfo;
}
