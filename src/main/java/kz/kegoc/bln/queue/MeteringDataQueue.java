package kz.kegoc.bln.queue;

import kz.kegoc.bln.entity.media.Metering;
import java.util.List;

public interface MeteringDataQueue<T extends Metering> {
    void add(T entity);
    void addAll(List<T> list);
}
