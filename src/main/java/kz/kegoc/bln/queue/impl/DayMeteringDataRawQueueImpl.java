package kz.kegoc.bln.queue.impl;

import java.util.concurrent.BlockingQueue;
import javax.ejb.Stateless;
import javax.inject.Inject;
import kz.kegoc.bln.entity.media.raw.DayMeteringDataRaw;
import kz.kegoc.bln.queue.AbstractMeteringDataQueue;
import kz.kegoc.bln.queue.MeteringDataQueue;

@Stateless
public class DayMeteringDataRawQueueImpl
    extends AbstractMeteringDataQueue<DayMeteringDataRaw>
        implements MeteringDataQueue<DayMeteringDataRaw> {

    @Inject
    public DayMeteringDataRawQueueImpl(BlockingQueue<DayMeteringDataRaw> queue) {
        super(queue);
    }
}
