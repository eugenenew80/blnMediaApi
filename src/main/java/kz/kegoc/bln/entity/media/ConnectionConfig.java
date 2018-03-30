package kz.kegoc.bln.entity.media;

import kz.kegoc.bln.common.interfaces.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of= {"id"})
public class ConnectionConfig implements HasId {
    private Long id;
    private String name;
    private SourceSystem sourceSystemCode;
    private String protocol;
    private String url;
    private String userName;
    private String pwd;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}
