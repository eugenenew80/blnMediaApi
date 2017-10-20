package kz.kegoc.bln.queue.impl;

import kz.kegoc.bln.entity.media.hour.HourMeteringDataRaw;
import kz.kegoc.bln.queue.AbstractMeteringDataQueue;
import kz.kegoc.bln.queue.MeteringDataQueue;
import org.redisson.api.RBlockingQueue;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class HourMeteringDataRawQueueImpl
    extends AbstractMeteringDataQueue<HourMeteringDataRaw>
        implements MeteringDataQueue<HourMeteringDataRaw> {

    @Inject
    public HourMeteringDataRawQueueImpl(RBlockingQueue<HourMeteringDataRaw> queue) {
        super(queue);
    }
}
