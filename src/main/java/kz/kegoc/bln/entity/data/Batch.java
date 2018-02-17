package kz.kegoc.bln.entity.data;

import kz.kegoc.bln.entity.adm.User;
import kz.kegoc.bln.entity.common.HasId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of= {"id"})
public class Batch implements HasId {
    private Long id;
    private WorkListHeader workListHeader;
    private String sourceSystemCode;
    private String direction;
    private String dataType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long recCount;
    private String status;
    private String errMsg;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private User createBy;
    private User lastUpdateBy;
}
