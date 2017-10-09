package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.entity.common.HasId;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LoadMeteringInfo implements HasId {
    private Long id;
    private LocalDateTime lastRequestedDate;
    private LocalDateTime lastLoadedDate;
}
