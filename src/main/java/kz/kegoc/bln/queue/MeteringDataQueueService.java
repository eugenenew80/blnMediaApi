package kz.kegoc.bln.queue;

import kz.kegoc.bln.entity.common.HasId;
import java.util.List;

public interface MeteringDataQueueService<T extends HasId> {
    void add(T entity);
    void addAll(List<T> list);
}
