package kz.kegoc.bln.queue.impl;

import kz.kegoc.bln.entity.data.MeasData;
import kz.kegoc.bln.entity.data.MeasDataRaw;
import kz.kegoc.bln.queue.AbstractMeteringDataQueue;
import kz.kegoc.bln.queue.MeteringDataQueue;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.concurrent.BlockingQueue;

@Stateless
public class MeasDataRawQueueImpl
    extends AbstractMeteringDataQueue<MeasDataRaw>
        implements MeteringDataQueue<MeasDataRaw> {

    @Inject
    public MeasDataRawQueueImpl(BlockingQueue<MeasDataRaw> queue) {
        super(queue);
    }
}
