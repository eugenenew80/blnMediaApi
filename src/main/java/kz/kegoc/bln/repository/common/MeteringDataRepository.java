package kz.kegoc.bln.repository.common;

import kz.kegoc.bln.entity.common.HasId;
import java.util.List;

public interface MeteringDataRepository<T extends HasId> {
    T insert(T entity);
    void insertAll(List<T> list);
}
