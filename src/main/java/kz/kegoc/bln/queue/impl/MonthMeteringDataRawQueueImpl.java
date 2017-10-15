package kz.kegoc.bln.queue.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import kz.kegoc.bln.queue.AbstractMeteringDataQueueService;
import kz.kegoc.bln.entity.media.MonthMeteringDataRaw;
import kz.kegoc.bln.queue.MeteringDataQueueService;
import org.redisson.api.RBlockingQueue;

@Stateless
public class MonthMeteringDataRawQueueImpl
    extends AbstractMeteringDataQueueService<MonthMeteringDataRaw>
        implements MeteringDataQueueService<MonthMeteringDataRaw> {

    @Inject
    public MonthMeteringDataRawQueueImpl(RBlockingQueue<MonthMeteringDataRaw> queue) {
        super(queue);
    }
}
