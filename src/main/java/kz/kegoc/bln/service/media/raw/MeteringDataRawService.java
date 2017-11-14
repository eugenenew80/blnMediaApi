package kz.kegoc.bln.service.media.raw;

import kz.kegoc.bln.entity.media.Metering;
import kz.kegoc.bln.service.common.EntityService;

import javax.ejb.Local;
import java.time.LocalDateTime;
import java.util.List;

@Local
public interface MeteringDataRawService<T extends Metering> extends EntityService<T> {
    void saveAll(List<T> list);
    
    T findByEntity(T entity);

    List<T> findReadyData(Long meteringPointId, LocalDateTime meteringDate, String paramCode);
}
