package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.entity.common.HasId;
import lombok.Data;
import java.util.Date;

@Data
public class LoadMeteringInfo implements HasId {
    private Long id;
    private Date lastRequestedDate;
    private Date lastLoadedDate;
}
