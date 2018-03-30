package kz.kegoc.bln.service;

import kz.kegoc.bln.common.interfaces.MeteringValue;
import kz.kegoc.bln.common.service.EntityService;

import javax.ejb.Local;
import java.util.List;

@Local
public interface MeteringValueService<T extends MeteringValue> extends EntityService<T> {
    void saveAll(List<T> list);
}
