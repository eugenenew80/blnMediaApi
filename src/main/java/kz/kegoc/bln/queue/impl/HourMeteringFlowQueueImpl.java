package kz.kegoc.bln.queue.impl;

import kz.kegoc.bln.entity.media.data.HourMeteringFlow;
import kz.kegoc.bln.queue.AbstractMeteringDataQueue;
import kz.kegoc.bln.queue.MeteringDataQueue;
import java.util.concurrent.BlockingQueue;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class HourMeteringFlowQueueImpl
    extends AbstractMeteringDataQueue<HourMeteringFlow>
        implements MeteringDataQueue<HourMeteringFlow> {

    @Inject
    public HourMeteringFlowQueueImpl(BlockingQueue<HourMeteringFlow> queue) {
        super(queue);
    }
}