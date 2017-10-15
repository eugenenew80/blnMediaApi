package kz.kegoc.bln.queue.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import kz.kegoc.bln.queue.AbstractMeteringDataQueueService;
import kz.kegoc.bln.entity.media.DayMeteringDataRaw;
import kz.kegoc.bln.queue.MeteringDataQueueService;
import org.redisson.api.RBlockingQueue;

@Stateless
public class DayMeteringDataRawQueueImpl
    extends AbstractMeteringDataQueueService<DayMeteringDataRaw>
        implements MeteringDataQueueService<DayMeteringDataRaw> {

    @Inject
    public DayMeteringDataRawQueueImpl(RBlockingQueue<DayMeteringDataRaw> queue) {
        super(queue);
    }
}
