package kz.kegoc.bln.queue.impl;

import java.util.concurrent.BlockingQueue;
import javax.ejb.Stateless;
import javax.inject.Inject;
import kz.kegoc.bln.entity.media.raw.MonthMeteringFlowRaw;
import kz.kegoc.bln.queue.AbstractMeteringDataQueue;
import kz.kegoc.bln.queue.MeteringDataQueue;

@Stateless
public class MonthMeteringFlowRawQueueImpl
    extends AbstractMeteringDataQueue<MonthMeteringFlowRaw>
        implements MeteringDataQueue<MonthMeteringFlowRaw> {

    @Inject
    public MonthMeteringFlowRawQueueImpl(BlockingQueue<MonthMeteringFlowRaw> queue) {
        super(queue);
    }
}
