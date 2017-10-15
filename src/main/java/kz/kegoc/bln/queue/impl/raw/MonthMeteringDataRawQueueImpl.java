package kz.kegoc.bln.queue.impl.raw;

import javax.ejb.Stateless;
import kz.kegoc.bln.queue.AbstractMeteringDataQueueService;
import kz.kegoc.bln.entity.media.raw.MonthMeteringDataRaw;
import kz.kegoc.bln.queue.MeteringDataQueueService;

@Stateless
public class MonthMeteringDataRawQueueImpl extends AbstractMeteringDataQueueService<MonthMeteringDataRaw> implements MeteringDataQueueService<MonthMeteringDataRaw> {
}
