package kz.kegoc.bln.entity.data;

import kz.kegoc.bln.entity.common.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of= {"id"})
public class EventLog  implements HasId {
    private Long id;
    private String source;
    private String eventType;
    private String code;
    private String text;
    private LocalDateTime createDate;
}
