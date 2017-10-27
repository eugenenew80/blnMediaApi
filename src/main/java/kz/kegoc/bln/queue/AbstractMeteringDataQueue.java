package kz.kegoc.bln.queue;

import kz.kegoc.bln.entity.media.Metering;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public abstract class AbstractMeteringDataQueue<T extends Metering> implements MeteringDataQueue<T> {

    public AbstractMeteringDataQueue(BlockingQueue<T> queue) {
        this.queue = queue;
    }

    public void add(T entity) {
        queue.add(entity);
    }

    public void addAll(List<T> list) {
        queue.addAll(list);
    }

    private BlockingQueue<T> queue;
}
