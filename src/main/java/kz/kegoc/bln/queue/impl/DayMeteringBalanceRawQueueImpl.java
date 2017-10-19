package kz.kegoc.bln.queue.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import kz.kegoc.bln.queue.AbstractMeteringDataQueue;
import kz.kegoc.bln.entity.media.DayMeteringBalanceRaw;
import kz.kegoc.bln.queue.MeteringDataQueue;
import org.redisson.api.RBlockingQueue;

@Stateless
public class DayMeteringBalanceRawQueueImpl
    extends AbstractMeteringDataQueue<DayMeteringBalanceRaw>
        implements MeteringDataQueue<DayMeteringBalanceRaw> {

    @Inject
    public DayMeteringBalanceRawQueueImpl(RBlockingQueue<DayMeteringBalanceRaw> queue) {
        super(queue);
    }
}
