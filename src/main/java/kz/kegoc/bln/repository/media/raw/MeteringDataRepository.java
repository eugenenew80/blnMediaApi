package kz.kegoc.bln.repository.media.raw;

import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.repository.common.Repository;

public interface MeteringDataRepository<T extends HasId>  extends Repository<T> {
    T selectByEntity(T entity); 
}
