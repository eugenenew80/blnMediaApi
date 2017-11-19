package kz.kegoc.bln.queue.impl;

import java.util.concurrent.BlockingQueue;
import javax.ejb.Stateless;
import javax.inject.Inject;
import kz.kegoc.bln.entity.media.data.DayMeteringFlow;
import kz.kegoc.bln.queue.AbstractMeteringDataQueue;
import kz.kegoc.bln.queue.MeteringDataQueue;

@Stateless
public class DayMeteringFlowQueueImpl
    extends AbstractMeteringDataQueue<DayMeteringFlow>
        implements MeteringDataQueue<DayMeteringFlow> {

    @Inject
    public DayMeteringFlowQueueImpl(BlockingQueue<DayMeteringFlow> queue) {
        super(queue);
    }
}
