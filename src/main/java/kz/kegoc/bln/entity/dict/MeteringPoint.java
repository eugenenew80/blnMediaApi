package kz.kegoc.bln.entity.dict;

import kz.kegoc.bln.entity.common.HasCode;
import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.entity.media.LoadMeteringInfo;
import lombok.Data;

@Data
public class MeteringPoint implements HasId, HasCode {
    private Long id;
    private String code;
    private String externalCode;
    private LoadMeteringInfo loadInfo;
}
