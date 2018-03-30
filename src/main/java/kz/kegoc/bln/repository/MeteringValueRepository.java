package kz.kegoc.bln.repository;

import kz.kegoc.bln.common.interfaces.MeteringValue;
import kz.kegoc.bln.common.repository.Repository;

import javax.ejb.Local;
import java.util.List;

@Local
public interface MeteringValueRepository<T extends MeteringValue>  extends Repository<T> {
    T selectByEntity(T entity);
    void saveAll(List<T> list);
}