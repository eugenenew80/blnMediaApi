package kz.kegoc.bln.queue.impl;

import kz.kegoc.bln.entity.data.MeasData;
import kz.kegoc.bln.queue.AbstractMeteringDataQueue;
import kz.kegoc.bln.queue.MeteringDataQueue;
import java.util.concurrent.BlockingQueue;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class MeasDataQueueImpl
    extends AbstractMeteringDataQueue<MeasData>
        implements MeteringDataQueue<MeasData> {

    @Inject
    public MeasDataQueueImpl(BlockingQueue<MeasData> queue) {
        super(queue);
    }
}
