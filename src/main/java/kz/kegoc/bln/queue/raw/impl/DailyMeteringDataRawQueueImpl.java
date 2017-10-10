package kz.kegoc.bln.queue.raw.impl;

import javax.ejb.Stateless;
import kz.kegoc.bln.queue.common.AbstractMeteringDataQueueService;
import kz.kegoc.bln.entity.media.raw.DayMeteringDataRaw;
import kz.kegoc.bln.queue.raw.DailyMeteringDataRawQueue;

@Stateless
public class DailyMeteringDataRawQueueImpl extends AbstractMeteringDataQueueService<DayMeteringDataRaw> implements DailyMeteringDataRawQueue {
	public DailyMeteringDataRawQueueImpl() {
		super("dailyMeteringData");
	}
}
