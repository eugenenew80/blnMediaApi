package kz.kegoc.bln.repository.media.raw;

import kz.kegoc.bln.entity.media.Metering;
import kz.kegoc.bln.repository.common.Repository;

import javax.ejb.Local;

@Local
public interface MeteringDataRawRepository<T extends Metering>  extends Repository<T> {
    T selectByEntity(T entity); 
}
