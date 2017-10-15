package kz.kegoc.bln.queue.impl;

import kz.kegoc.bln.entity.media.HourMeteringDataRaw;
import kz.kegoc.bln.queue.AbstractMeteringDataQueueService;
import kz.kegoc.bln.queue.MeteringDataQueueService;
import org.redisson.api.RBlockingQueue;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class HourMeteringDataRawQueueServiceImpl
    extends AbstractMeteringDataQueueService<HourMeteringDataRaw>
        implements MeteringDataQueueService<HourMeteringDataRaw> {

    @Inject
    public HourMeteringDataRawQueueServiceImpl(RBlockingQueue<HourMeteringDataRaw> queue) {
        super(queue);
    }
}
