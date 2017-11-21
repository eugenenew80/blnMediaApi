package kz.kegoc.bln.queue;

import kz.kegoc.bln.entity.common.Metering;

import javax.ejb.Local;
import java.util.List;

@Local
public interface MeteringDataQueue<T extends Metering> {
    void add(T entity);
    void addAll(List<T> list);
}
