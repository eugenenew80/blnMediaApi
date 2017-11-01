package kz.kegoc.bln.service.media.oper;

import kz.kegoc.bln.entity.media.oper.DayMeteringDataOper;
import kz.kegoc.bln.service.common.EntityService;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Local;

@Local
public interface DayMeteringDataOperService extends EntityService<DayMeteringDataOper> { 
	List<DayMeteringDataOper> findByGroup(Long groupId, LocalDateTime operDate);
	
	List<DayMeteringDataOper> fillByGroup(Long groupId, LocalDateTime operDate);
}
