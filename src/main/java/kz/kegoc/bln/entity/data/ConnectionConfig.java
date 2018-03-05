package kz.kegoc.bln.entity.data;

import kz.kegoc.bln.entity.adm.User;
import kz.kegoc.bln.entity.common.HasId;
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
    private User createBy;
    private User lastUpdateBy;
}
