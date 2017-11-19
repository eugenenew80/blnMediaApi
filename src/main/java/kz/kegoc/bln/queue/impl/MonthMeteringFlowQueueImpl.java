package kz.kegoc.bln.queue.impl;

import java.util.concurrent.BlockingQueue;
import javax.ejb.Stateless;
import javax.inject.Inject;
import kz.kegoc.bln.entity.media.raw.MonthMeteringFlow;
import kz.kegoc.bln.queue.AbstractMeteringDataQueue;
import kz.kegoc.bln.queue.MeteringDataQueue;

@Stateless
public class MonthMeteringFlowQueueImpl
    extends AbstractMeteringDataQueue<MonthMeteringFlow>
        implements MeteringDataQueue<MonthMeteringFlow> {

    @Inject
    public MonthMeteringFlowQueueImpl(BlockingQueue<MonthMeteringFlow> queue) {
        super(queue);
    }
}
