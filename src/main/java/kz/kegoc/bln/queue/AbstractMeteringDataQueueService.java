package kz.kegoc.bln.queue;

import kz.kegoc.bln.entity.media.MeteringData;
import org.redisson.api.RBlockingQueue;
import javax.inject.Inject;
import java.util.List;

public abstract class AbstractMeteringDataQueueService<T extends MeteringData> implements MeteringDataQueueService<T> {
    public void add(T entity) {
        queue.add(entity);
    }

    public void addAll(List<T> list) {
        queue.addAll(list);
    }

    @Inject
    private RBlockingQueue<T> queue;
}
