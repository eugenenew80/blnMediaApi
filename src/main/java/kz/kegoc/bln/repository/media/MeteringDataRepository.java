package kz.kegoc.bln.repository.media;

import kz.kegoc.bln.entity.media.Metering;
import kz.kegoc.bln.repository.common.Repository;

public interface MeteringDataRepository<T extends Metering>  extends Repository<T> {
    T selectByEntity(T entity); 
}
