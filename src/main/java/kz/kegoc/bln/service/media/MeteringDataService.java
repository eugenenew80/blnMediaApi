package kz.kegoc.bln.service.media;

import kz.kegoc.bln.entity.media.Metering;
import kz.kegoc.bln.service.common.EntityService;
import java.util.List;

public interface MeteringDataService<T extends Metering> extends EntityService<T> {
    void saveAll(List<T> list);
}
