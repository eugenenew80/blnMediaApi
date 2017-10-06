package kz.kegoc.bln.queue.common;

import kz.kegoc.bln.entity.common.HasId;
import java.util.List;

public interface MeteringDataQueueService<T extends HasId> {
    void addMeteringData(T entity);
    void addMeteringListData(List<T> list);
}
