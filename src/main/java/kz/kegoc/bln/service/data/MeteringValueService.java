package kz.kegoc.bln.service.data;

import kz.kegoc.bln.entity.common.MeteringValue;
import kz.kegoc.bln.service.common.EntityService;

import javax.ejb.Local;
import java.util.List;

@Local
public interface MeteringValueService<T extends MeteringValue> extends EntityService<T> {
    void saveAll(List<T> list);
}
