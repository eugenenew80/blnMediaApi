package kz.kegoc.bln.service.common;

import kz.kegoc.bln.entity.common.HasId;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

public abstract class AbstractMeteringDataQueueService<T extends HasId> implements MeteringDataQueueService<T> {

    @PostConstruct
    public void init() {
        queue = redissonClient.getBlockingQueue(queueName);
    }

    public AbstractMeteringDataQueueService() { }

    public AbstractMeteringDataQueueService(String queueName) {
        this.queueName = queueName;
    }


    public void addMeteringData(T entity) {
        queue.add(entity);
    }

    public void addMeteringListData(List<T> list) {
        queue.addAll(list);
    }


    @Inject
    private RedissonClient redissonClient;
    private RBlockingQueue<T> queue;
    private String queueName;
}
