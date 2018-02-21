package kz.kegoc.bln.repository.data;

import kz.kegoc.bln.entity.data.PeriodTimeValue;
import kz.kegoc.bln.repository.common.Repository;
import javax.ejb.Local;
import java.time.LocalDateTime;
import java.util.List;

@Local
public interface PeriodTimeValueRepository extends Repository<PeriodTimeValue> {
    List<PeriodTimeValue> selectByExternalCode(String sourceMeteringPointCode, LocalDateTime meteringDateStart, LocalDateTime meteringDateEnd);
}
