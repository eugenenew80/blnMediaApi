package kz.kegoc.bln.queue.impl;

import java.util.concurrent.BlockingQueue;
import javax.ejb.Stateless;
import javax.inject.Inject;
import kz.kegoc.bln.entity.media.data.DayMeteringBalance;
import kz.kegoc.bln.queue.AbstractMeteringDataQueue;
import kz.kegoc.bln.queue.MeteringDataQueue;

@Stateless
public class DayMeteringBalanceQueueImpl
    extends AbstractMeteringDataQueue<DayMeteringBalance>
        implements MeteringDataQueue<DayMeteringBalance> {

    @Inject
    public DayMeteringBalanceQueueImpl(BlockingQueue<DayMeteringBalance> queue) {
        super(queue);
    }
}
