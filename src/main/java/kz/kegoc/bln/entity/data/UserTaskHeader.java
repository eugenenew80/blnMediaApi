package kz.kegoc.bln.entity.data;

import kz.kegoc.bln.entity.adm.User;
import kz.kegoc.bln.entity.common.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(of= {"id"})
@DynamicUpdate
public class UserTaskHeader implements HasId {
    private Long id;
    private String name;
    private SourceSystem sourceSystemCode;
    private Direction direction;
    private BatchStatus atStatus;
    private BatchStatus ptStatus;
    private ConnectionConfig config;
    private Boolean active;
    private Batch atBatch;
    private Batch ptBatch;
    private List<UserTaskLine> lines;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private User createBy;
    private User lastUpdateBy;
}
