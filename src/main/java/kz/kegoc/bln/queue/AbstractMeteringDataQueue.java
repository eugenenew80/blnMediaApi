package kz.kegoc.bln.queue;

import kz.kegoc.bln.entity.media.Metering;
import org.redisson.api.RBlockingQueue;
import java.util.List;

public abstract class AbstractMeteringDataQueue<T extends Metering> implements MeteringDataQueue<T> {

    public AbstractMeteringDataQueue(RBlockingQueue<T> queue) {
        this.queue = queue;
    }

    public void add(T entity) {
        queue.add(entity);
    }

    public void addAll(List<T> list) {
        queue.addAll(list);
    }

    private RBlockingQueue<T> queue;
}
