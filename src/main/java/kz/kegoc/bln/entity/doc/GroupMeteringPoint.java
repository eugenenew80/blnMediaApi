package kz.kegoc.bln.entity.doc;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.entity.dict.MeteringPoint;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"id"})
public class GroupMeteringPoint implements HasId {
    private Long id;
    
    @NotNull
    private Group group;
    
    @NotNull
    private MeteringPoint meteringPoint;
    
    private Long listOrder;
	private LocalDate startDate;
	private LocalDate endDate; 
}
