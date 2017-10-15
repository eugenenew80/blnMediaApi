package kz.kegoc.bln.queue.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import kz.kegoc.bln.queue.AbstractMeteringDataQueueService;
import kz.kegoc.bln.entity.media.DayMeteringDataRaw;
import kz.kegoc.bln.queue.MeteringDataQueueService;
import org.redisson.api.RBlockingQueue;

@Stateless
public class DayMeteringDataRawQueueServiceImpl
    extends AbstractMeteringDataQueueService<DayMeteringDataRaw>
        implements MeteringDataQueueService<DayMeteringDataRaw> {

    @Inject
    public DayMeteringDataRawQueueServiceImpl(RBlockingQueue<DayMeteringDataRaw> queue) {
        super(queue);
    }
}
