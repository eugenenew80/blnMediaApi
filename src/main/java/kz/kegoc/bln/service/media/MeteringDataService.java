package kz.kegoc.bln.service.media;

import kz.kegoc.bln.entity.media.MeteringData;
import kz.kegoc.bln.service.common.EntityService;
import java.util.List;

public interface MeteringDataService<T extends MeteringData> extends EntityService<T> {
    void saveAll(List<T> list);
}
