package kz.kegoc.bln.repository;

import kz.kegoc.bln.common.repository.Repository;
import kz.kegoc.bln.imp.raw.PeriodTimeValueRaw;
import javax.ejb.Local;
import java.util.List;

@Local
public interface PeriodTimeValueRawRepository  extends Repository<PeriodTimeValueRaw> {
    void saveAll(List<PeriodTimeValueRaw> list);
}
