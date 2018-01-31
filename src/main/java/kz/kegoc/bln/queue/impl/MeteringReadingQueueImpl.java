package kz.kegoc.bln.queue.impl;

import java.util.concurrent.BlockingQueue;
import javax.ejb.Stateless;
import javax.inject.Inject;
import kz.kegoc.bln.entity.data.MeteringReading;
import kz.kegoc.bln.queue.AbstractMeteringDataQueue;
import kz.kegoc.bln.queue.MeteringDataQueue;

@Stateless
public class MeteringReadingQueueImpl
    extends AbstractMeteringDataQueue<MeteringReading>
        implements MeteringDataQueue<MeteringReading> {

    @Inject
    public MeteringReadingQueueImpl(BlockingQueue<MeteringReading> queue) {
        super(queue);
    }
}
