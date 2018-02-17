package kz.kegoc.bln.repository.data;

import kz.kegoc.bln.entity.common.Metering;
import kz.kegoc.bln.repository.common.Repository;

import javax.ejb.Local;
import java.time.LocalDateTime;
import java.util.List;

@Local
public interface MeteringDataRepository<T extends Metering>  extends Repository<T> {
    T selectByEntity(T entity);
    void saveAll(List<T> list);
}
