package kz.kegoc.bln.queue.impl.raw;

import kz.kegoc.bln.entity.media.raw.HourMeteringDataRaw;
import kz.kegoc.bln.queue.AbstractMeteringDataQueueService;
import kz.kegoc.bln.queue.MeteringDataQueueService;
import javax.ejb.Stateless;

@Stateless
public class HourMeteringDataRawQueueImpl
    extends AbstractMeteringDataQueueService<HourMeteringDataRaw>
        implements MeteringDataQueueService<HourMeteringDataRaw> {
}
