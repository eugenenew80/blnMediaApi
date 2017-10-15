package kz.kegoc.bln.queue.impl.raw;

import javax.ejb.Stateless;
import kz.kegoc.bln.queue.AbstractMeteringDataQueueService;
import kz.kegoc.bln.entity.media.raw.DayMeteringDataRaw;
import kz.kegoc.bln.queue.MeteringDataQueueService;

@Stateless
public class DayMeteringDataRawQueueImpl
    extends AbstractMeteringDataQueueService<DayMeteringDataRaw>
        implements MeteringDataQueueService<DayMeteringDataRaw> {
}
