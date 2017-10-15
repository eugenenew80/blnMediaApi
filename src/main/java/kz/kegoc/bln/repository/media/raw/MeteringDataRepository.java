package kz.kegoc.bln.repository.media.raw;

import kz.kegoc.bln.entity.media.MeteringData;
import kz.kegoc.bln.repository.common.Repository;

public interface MeteringDataRepository<T extends MeteringData>  extends Repository<T> {
    T selectByEntity(T entity); 
}
