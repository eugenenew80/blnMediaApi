package kz.kegoc.bln.service;

import kz.kegoc.bln.common.service.EntityService;
import kz.kegoc.bln.imp.raw.AtTimeValueRaw;
import javax.ejb.Local;
import java.util.List;

@Local
public interface AtTimeValueRawService extends EntityService<AtTimeValueRaw> {
    void saveAll(List<AtTimeValueRaw> list);
}
