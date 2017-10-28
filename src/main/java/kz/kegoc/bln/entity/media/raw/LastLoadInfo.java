package kz.kegoc.bln.entity.media.raw;

import kz.kegoc.bln.entity.common.HasId;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LastLoadInfo implements HasId {
    private Long id;
    private String externalCode;
    private String paramCode;
    private LocalDateTime lastLoadDate;
}
