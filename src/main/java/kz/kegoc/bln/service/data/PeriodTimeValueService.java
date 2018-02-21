package kz.kegoc.bln.service.data;

import kz.kegoc.bln.entity.data.PeriodTimeValue;
import kz.kegoc.bln.service.common.EntityService;
import javax.ejb.Local;
import java.time.LocalDateTime;
import java.util.List;

@Local
public interface PeriodTimeValueService extends EntityService<PeriodTimeValue> {
    List<PeriodTimeValue> findByExternalCode(String sourceMeteringPointCode, LocalDateTime meteringDateStart, LocalDateTime meteringDateEnd);
}
