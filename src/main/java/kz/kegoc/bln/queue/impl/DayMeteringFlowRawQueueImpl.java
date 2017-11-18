package kz.kegoc.bln.queue.impl;

import java.util.concurrent.BlockingQueue;
import javax.ejb.Stateless;
import javax.inject.Inject;
import kz.kegoc.bln.entity.media.raw.DayMeteringFlowRaw;
import kz.kegoc.bln.queue.AbstractMeteringDataQueue;
import kz.kegoc.bln.queue.MeteringDataQueue;

@Stateless
public class DayMeteringFlowRawQueueImpl
    extends AbstractMeteringDataQueue<DayMeteringFlowRaw>
        implements MeteringDataQueue<DayMeteringFlowRaw> {

    @Inject
    public DayMeteringFlowRawQueueImpl(BlockingQueue<DayMeteringFlowRaw> queue) {
        super(queue);
    }
}
