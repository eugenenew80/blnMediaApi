package kz.kegoc.bln.repository.common;

import kz.kegoc.bln.entity.common.HasId;
import java.util.List;

public interface MeteringDataRepository<T extends HasId>  extends Repository<T> {
    void insertAll(List<T> list);
}
