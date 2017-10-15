package kz.kegoc.bln.queue.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import kz.kegoc.bln.queue.AbstractMeteringDataQueue;
import kz.kegoc.bln.entity.media.MonthMeteringDataRaw;
import kz.kegoc.bln.queue.MeteringDataQueue;
import org.redisson.api.RBlockingQueue;

@Stateless
public class MonthMeteringDataRawQueueImpl
    extends AbstractMeteringDataQueue<MonthMeteringDataRaw>
        implements MeteringDataQueue<MonthMeteringDataRaw> {

    @Inject
    public MonthMeteringDataRawQueueImpl(RBlockingQueue<MonthMeteringDataRaw> queue) {
        super(queue);
    }
}
