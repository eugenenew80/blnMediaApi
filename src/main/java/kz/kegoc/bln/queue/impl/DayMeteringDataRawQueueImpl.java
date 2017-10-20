package kz.kegoc.bln.queue.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import kz.kegoc.bln.entity.media.day.DayMeteringDataRaw;
import kz.kegoc.bln.queue.AbstractMeteringDataQueue;
import kz.kegoc.bln.queue.MeteringDataQueue;
import org.redisson.api.RBlockingQueue;

@Stateless
public class DayMeteringDataRawQueueImpl
    extends AbstractMeteringDataQueue<DayMeteringDataRaw>
        implements MeteringDataQueue<DayMeteringDataRaw> {

    @Inject
    public DayMeteringDataRawQueueImpl(RBlockingQueue<DayMeteringDataRaw> queue) {
        super(queue);
    }
}
