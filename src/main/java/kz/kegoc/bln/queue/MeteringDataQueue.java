package kz.kegoc.bln.queue;

import kz.kegoc.bln.entity.media.MeteringData;
import java.util.List;

public interface MeteringDataQueue<T extends MeteringData> {
    void add(T entity);
    void addAll(List<T> list);
}
