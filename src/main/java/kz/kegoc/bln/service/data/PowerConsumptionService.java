package kz.kegoc.bln.service.data;

import kz.kegoc.bln.entity.data.PowerConsumption;
import kz.kegoc.bln.service.common.EntityService;
import javax.ejb.Local;
import java.time.LocalDateTime;
import java.util.List;

@Local
public interface PowerConsumptionService extends EntityService<PowerConsumption> {
    List<PowerConsumption> findByExternalCode(String sourceMeteringPointCode, LocalDateTime meteringDateStart, LocalDateTime meteringDateEnd);
}
