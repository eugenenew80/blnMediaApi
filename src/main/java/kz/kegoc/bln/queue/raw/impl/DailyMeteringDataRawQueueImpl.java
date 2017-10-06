package kz.kegoc.bln.queue.raw.impl;

import javax.ejb.Stateless;
import kz.kegoc.bln.queue.common.AbstractMeteringDataQueueService;
import kz.kegoc.bln.entity.media.DailyMeteringDataRaw;
import kz.kegoc.bln.queue.raw.DailyMeteringDataRawQueue;

@Stateless
public class DailyMeteringDataRawQueueImpl extends AbstractMeteringDataQueueService<DailyMeteringDataRaw> implements DailyMeteringDataRawQueue {
	public DailyMeteringDataRawQueueImpl() {
		super("dailyMeteringData");
	}
}
