package kz.kegoc.bln.service;

import kz.kegoc.bln.common.service.EntityService;
import kz.kegoc.bln.imp.raw.PeriodTimeValueRaw;
import javax.ejb.Local;
import java.util.List;

@Local
public interface PeriodTimeValueRawService extends EntityService<PeriodTimeValueRaw> {
    void saveAll(List<PeriodTimeValueRaw> list);
}
