package kz.kegoc.bln.service.media.raw;

import kz.kegoc.bln.entity.media.Metering;
import kz.kegoc.bln.service.common.EntityService;
import java.util.List;

public interface MeteringDataRawService<T extends Metering> extends EntityService<T> {
    void saveAll(List<T> list);
}
