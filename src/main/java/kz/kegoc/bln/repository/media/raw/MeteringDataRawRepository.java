package kz.kegoc.bln.repository.media.raw;

import kz.kegoc.bln.entity.media.Metering;
import kz.kegoc.bln.repository.common.Repository;

import javax.ejb.Local;
import java.time.LocalDateTime;
import java.util.List;

@Local
public interface MeteringDataRawRepository<T extends Metering>  extends Repository<T> {
    T selectByEntity(T entity);

    List selectReadyData(Long meteringPointId, LocalDateTime meteringDate, String paramCode);
}
