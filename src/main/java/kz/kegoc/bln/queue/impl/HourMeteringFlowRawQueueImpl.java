package kz.kegoc.bln.queue.impl;

import kz.kegoc.bln.entity.media.raw.HourMeteringFlowRaw;
import kz.kegoc.bln.queue.AbstractMeteringDataQueue;
import kz.kegoc.bln.queue.MeteringDataQueue;
import java.util.concurrent.BlockingQueue;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class HourMeteringFlowRawQueueImpl
    extends AbstractMeteringDataQueue<HourMeteringFlowRaw>
        implements MeteringDataQueue<HourMeteringFlowRaw> {

    @Inject
    public HourMeteringFlowRawQueueImpl(BlockingQueue<HourMeteringFlowRaw> queue) {
        super(queue);
    }
}
