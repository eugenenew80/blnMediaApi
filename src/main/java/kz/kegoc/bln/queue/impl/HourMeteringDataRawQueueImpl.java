package kz.kegoc.bln.queue.impl;

import kz.kegoc.bln.entity.media.raw.HourMeteringDataRaw;
import kz.kegoc.bln.queue.AbstractMeteringDataQueue;
import kz.kegoc.bln.queue.MeteringDataQueue;
import java.util.concurrent.BlockingQueue;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class HourMeteringDataRawQueueImpl
    extends AbstractMeteringDataQueue<HourMeteringDataRaw>
        implements MeteringDataQueue<HourMeteringDataRaw> {

    @Inject
    public HourMeteringDataRawQueueImpl(BlockingQueue<HourMeteringDataRaw> queue) {
        super(queue);
    }
}
