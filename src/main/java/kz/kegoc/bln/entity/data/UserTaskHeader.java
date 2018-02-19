package kz.kegoc.bln.entity.data;

import kz.kegoc.bln.entity.adm.User;
import kz.kegoc.bln.entity.common.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(of= {"id"})
public class UserTaskHeader implements HasId {
    private Long id;
    private String name;
    private String sourceSystemCode;
    private String direction;
    private String status;
    private ConnectionConfig config;
    private Boolean active;
    private Batch batch;
    private List<UserTaskLine> lines;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private User createBy;
    private User lastUpdateBy;
}
