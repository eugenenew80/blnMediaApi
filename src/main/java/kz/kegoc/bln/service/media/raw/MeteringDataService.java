package kz.kegoc.bln.service.media.raw;

import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.service.common.EntityService;

import java.util.List;

public interface MeteringDataService<T extends HasId> extends EntityService<T> {
    void saveAll(List<T> list);
}
