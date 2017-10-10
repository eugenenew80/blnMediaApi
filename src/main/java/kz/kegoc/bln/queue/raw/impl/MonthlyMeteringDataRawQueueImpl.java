package kz.kegoc.bln.queue.raw.impl;

import javax.ejb.Stateless;
import kz.kegoc.bln.queue.common.AbstractMeteringDataQueueService;
import kz.kegoc.bln.entity.media.raw.MonthMeteringDataRaw;
import kz.kegoc.bln.queue.raw.MonthlyMeteringDataRawQueue;

@Stateless
public class MonthlyMeteringDataRawQueueImpl extends AbstractMeteringDataQueueService<MonthMeteringDataRaw> implements MonthlyMeteringDataRawQueue {
	public MonthlyMeteringDataRawQueueImpl() {
		super("monthlyMeteringData");
	}
}
