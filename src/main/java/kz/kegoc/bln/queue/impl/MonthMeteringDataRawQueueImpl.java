package kz.kegoc.bln.queue.impl;

import java.util.concurrent.BlockingQueue;
import javax.ejb.Stateless;
import javax.inject.Inject;
import kz.kegoc.bln.entity.media.month.MonthMeteringDataRaw;
import kz.kegoc.bln.queue.AbstractMeteringDataQueue;
import kz.kegoc.bln.queue.MeteringDataQueue;

@Stateless
public class MonthMeteringDataRawQueueImpl
    extends AbstractMeteringDataQueue<MonthMeteringDataRaw>
        implements MeteringDataQueue<MonthMeteringDataRaw> {

    @Inject
    public MonthMeteringDataRawQueueImpl(BlockingQueue<MonthMeteringDataRaw> queue) {
        super(queue);
    }
}
