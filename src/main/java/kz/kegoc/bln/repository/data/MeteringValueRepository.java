package kz.kegoc.bln.repository.data;

import kz.kegoc.bln.entity.common.MeteringValue;
import kz.kegoc.bln.repository.common.Repository;

import javax.ejb.Local;
import java.util.List;

@Local
public interface MeteringValueRepository<T extends MeteringValue>  extends Repository<T> {
    T selectByEntity(T entity);
    void saveAll(List<T> list);
}
