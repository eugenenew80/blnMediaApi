package kz.kegoc.bln.repository.common;

import kz.kegoc.bln.entity.common.HasId;

public interface MeteringDataRepository<T extends HasId>  extends Repository<T> {
    T selectByEntity(T entity); 
}
