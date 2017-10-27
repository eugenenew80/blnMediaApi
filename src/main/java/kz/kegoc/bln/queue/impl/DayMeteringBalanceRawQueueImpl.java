package kz.kegoc.bln.queue.impl;

import java.util.concurrent.BlockingQueue;
import javax.ejb.Stateless;
import javax.inject.Inject;
import kz.kegoc.bln.entity.media.day.DayMeteringBalanceRaw;
import kz.kegoc.bln.queue.AbstractMeteringDataQueue;
import kz.kegoc.bln.queue.MeteringDataQueue;

@Stateless
public class DayMeteringBalanceRawQueueImpl
    extends AbstractMeteringDataQueue<DayMeteringBalanceRaw>
        implements MeteringDataQueue<DayMeteringBalanceRaw> {

    @Inject
    public DayMeteringBalanceRawQueueImpl(BlockingQueue<DayMeteringBalanceRaw> queue) {
        super(queue);
    }
}
