package kz.kegoc.bln.queue.raw.impl;

import javax.ejb.Stateless;
import kz.kegoc.bln.queue.common.AbstractMeteringDataQueueService;
import kz.kegoc.bln.entity.media.MonthlyMeteringDataRaw;
import kz.kegoc.bln.queue.raw.MonthlyMeteringDataRawQueue;

@Stateless
public class MonthlyMeteringDataRawQueueImpl extends AbstractMeteringDataQueueService<MonthlyMeteringDataRaw> implements MonthlyMeteringDataRawQueue {
	public MonthlyMeteringDataRawQueueImpl() {
		super("monthlyMeteringData");
	}
}
