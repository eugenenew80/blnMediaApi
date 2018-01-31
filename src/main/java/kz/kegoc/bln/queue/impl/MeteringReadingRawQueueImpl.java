package kz.kegoc.bln.queue.impl;

import kz.kegoc.bln.entity.data.MeteringReadingRaw;
import kz.kegoc.bln.queue.AbstractMeteringDataQueue;
import kz.kegoc.bln.queue.MeteringDataQueue;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.concurrent.BlockingQueue;

@Stateless
public class MeteringReadingRawQueueImpl
    extends AbstractMeteringDataQueue<MeteringReadingRaw>
        implements MeteringDataQueue<MeteringReadingRaw> {

    @Inject
    public MeteringReadingRawQueueImpl(BlockingQueue<MeteringReadingRaw> queue) {
        super(queue);
    }
}
