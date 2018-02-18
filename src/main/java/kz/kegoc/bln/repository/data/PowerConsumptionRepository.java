package kz.kegoc.bln.repository.data;

import kz.kegoc.bln.entity.data.PowerConsumption;
import kz.kegoc.bln.repository.common.Repository;
import javax.ejb.Local;
import java.time.LocalDateTime;
import java.util.List;

@Local
public interface PowerConsumptionRepository extends Repository<PowerConsumption> {
    List<PowerConsumption> selectByExternalCode(String externalCode, LocalDateTime meteringDateStart, LocalDateTime meteringDateEnd);
}
